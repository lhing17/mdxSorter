package cn.gsein.slk;

import org.apache.commons.collections4.CollectionUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * 将Map写入lni文件
 *
 * @author G. Seinfeld
 * @since 2020-08-07
 */
public class LniWriter {

    public static final int MAX_LEVEL = 20;

    public static void main(String[] args) throws IOException {
        String[] fileNames = {
                "E:\\IdeaProjects\\jzjh3\\wlhd\\units\\unitdata.xlsx",
                "E:\\IdeaProjects\\jzjh3\\wlhd\\units\\unitbalance.xlsx",
                "E:\\IdeaProjects\\jzjh3\\wlhd\\units\\unitabilities.xlsx",
                "E:\\IdeaProjects\\jzjh3\\wlhd\\units\\unitui.xlsx",
                "E:\\IdeaProjects\\jzjh3\\wlhd\\units\\unitweapons.xlsx",
        };
        String abiltyFileName = "E:\\IdeaProjects\\jzjh3\\wlhd\\units\\campaignunitfunc.txt";
        Map<String, Map<String, String>> abilityMap = AbilityReader.read(abiltyFileName);
        XlsxReader.read(fileNames[0], abilityMap, false);
        XlsxReader.read(fileNames[1], abilityMap, true);
        XlsxReader.read(fileNames[2], abilityMap, true);
        XlsxReader.read(fileNames[3], abilityMap, true);
        XlsxReader.read(fileNames[4], abilityMap, true);

        Map<String, Map<String, String>> sortMap = new TreeMap<>(String::compareTo);
        sortMap.putAll(abilityMap);

//        printDiy(sortMap);
        write(sortMap);

    }

    public static void printDiy(Map<String, Map<String, String>> sortMap){
        sortMap.keySet().removeIf(k -> k.charAt(1) != '0');
        System.out.println(sortMap.keySet());
        System.out.println(sortMap.keySet().stream().filter(k->k.charAt(0)<97).count());
    }

    public static void write(Map<String, Map<String, String>> abilityMap) {
        String outputPath = "src/main/resources/out.ini";
        try (PrintWriter writer = new PrintWriter(outputPath)) {
            for (Map.Entry<String, Map<String, String>> entry : abilityMap.entrySet()) {
                String k = entry.getKey();
                k = writeKey(writer, k);
                if (k == null) {
                    continue;
                }

                // 处理多等级的map
                Map<String, String> data = entry.getValue();

                // 设置_parent
                setParent(k, data);
                Map<String, Object> cache = new HashMap<>(data.size());
                for (Map.Entry<String, String> stringEntry : data.entrySet()) {
                    String key = stringEntry.getKey();

                    // 将Buttonpos拆为1和2，即x和y
                    if ("Buttonpos".equals(key)) {
                        String[] pos = stringEntry.getValue().split(",");
                        cache.put("Buttonpos_1", pos[0].trim());
                        if (pos.length > 1) {
                            cache.put("Buttonpos_2", pos[1].trim());
                        }
                        continue;
                    }


                    char lastChar = key.charAt(key.length() - 1);
//                    if (Character.isDigit(lastChar)) {
//                        // 去掉后面的数字
//                        int pointer = trimTailingNumber(key);
//                        String newKey = key.substring(0, pointer + 1);
//                        int whichLevel = Integer.parseInt(key.substring(pointer + 1));
//
//                        // 不合并的字段
//                        List<String> unchecked = Arrays.asList("base", "mod", "code", "effect", "Targetattach", "Requires");
//
//                        // Targetattach不合并
//                        if (unchecked.contains(newKey)) {
//                            cache.put(key, stringEntry.getValue());
//                            continue;
//                        }
//
//                        // 字段合并
//                        if (cache.containsKey(newKey)) {
//                            try {
//                                List<String> list = (List<String>) cache.get(newKey);
//                                if (whichLevel - 1 < list.size()) {
//                                    list.set(whichLevel - 1, stringEntry.getValue());
//                                }
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        } else {
//                            List<String> list = new ArrayList<>(Collections.nCopies(MAX_LEVEL, null));
//                            if (whichLevel - 1 < list.size()) {
//                                list.set(whichLevel - 1, stringEntry.getValue());
//                            }
//                            cache.put(newKey, list);
//
//                        }
//                    } else {
                        cache.put(key, stringEntry.getValue());
//                    }
                }

                // 按字母排序，_parent放在第一位
                TreeMap<String, Object> cacheTree = new TreeMap<>((s1, s2) -> {
                    if (s1.equals(s2)) {
                        return 0;
                    }
                    if ("_parent".equals(s1)) {
                        return -1;
                    } else if ("_parent".equals(s2)) {
                        return 1;
                    }
                    return s1.compareTo(s2);
                });
                cacheTree.putAll(cache);

                for (Map.Entry<String, Object> stringEntry : cacheTree.entrySet()) {

                    Object value = stringEntry.getValue();
                    String key = stringEntry.getKey();
                    writer.write(key + "=");
                    if (value instanceof List) {
                        List<String> list = (List<String>) value;
                        if (containsOnlyFirstElement(list)) {
                            writeValue(writer, list.get(0));
                        } else {
                            writerArray(writer, stringEntry, list);
                        }
                    } else {
                        List<String> split = Arrays.asList("Name", "Tip", "Ubertip", "Art", "Hotkey");
                        if (split.contains(key)) {
                            String[] list = ((String) value).split(",");
                            if (list.length > 1) {
                                writerArray(writer, stringEntry, Arrays.asList(list));
                            } else {
                                writeValue(writer, (String) value);
                            }
                        } else {
                            writeValue(writer, (String) value);
                        }
                    }
                    writer.println();
                }
                writer.println();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static String writeKey(PrintWriter writer, String k) {
        // A0 变 A1
        if (k.startsWith("A0")) {
            k = k.replace("A0", "A1");
        }
        // 去掉buff
        if (k.startsWith("B") || k.startsWith("X")) {
            return null;
        }
        writer.println("[" + k + "]");
        return k;
    }

    private static void setParent(String k, Map<String, String> data) {
        // slk里的code是_parent模板
        if (data.containsKey("code")) {
            data.put("_parent", data.get("code"));
        }

        // 没有_parent的话给一个默认值
        if (!data.containsKey("_parent")) {
            if (k.startsWith("I1")) {
                data.put("_parent", "manh");
            } else if (k.startsWith("R0")) {
                data.put("_parent", "Rhpm");
            } else {
                data.put("_parent", k);
            }
        }
    }

    private static int trimTailingNumber(String key) {
        char[] chars = key.toCharArray();
        int pointer = chars.length - 1;
        for (int i = chars.length - 1; i >= 0; i--) {
            pointer = i;
            if (!Character.isDigit(chars[i])) {
                break;
            }
        }
        return pointer;
    }

    private static boolean containsOnlyFirstElement(List<String> list) {
        if (CollectionUtils.isEmpty(list)) {
            return false;
        }
        if (list.get(0) == null) {
            return false;
        }
        for (int i = 1; i < list.size(); i++) {
            if (list.get(i) != null) {
                return false;
            }
        }
        return true;
    }

    private static void writeValue(PrintWriter writer, String value) {
        try {
            Integer.parseInt(value);
            writer.write(value);
        } catch (Exception e) {
            try {
                Double.parseDouble(value);
                writer.write(value);
            } catch (Exception ex) {
                if (value.contains("\\")) {
                    value = value.replace("\\", "\\\\");
                }
                if (value.startsWith("\"") && value.endsWith("\"")) {
                    writer.write(value);
                } else {
                    writer.write("\"" + value + "\"");
                }
            }
        }
    }

    private static void writerArray(PrintWriter writer, Map.Entry<String, Object> stringEntry, List<String> list) {
        writer.write("{");
        for (String s : list) {
            if (s != null && !"".equals(s)) {
                writeValue(writer, s);
                writer.write(",");
            }
        }
        writer.write("}");
    }
}
