package cn.gsein.slk;

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

    public static void main(String[] args) throws IOException {
        String fileName = "E:\\IdeaProjects\\jzjh3\\wlhd\\units\\abilitydata.xlsx";
        String abiltyFileName = "E:\\IdeaProjects\\jzjh3\\wlhd\\units\\campaignabilitystrings.txt";
        Map<String, Map<String, String>> abilityMap = AbilityReader.read(abiltyFileName);
        XlsxReader.read(fileName, abilityMap);

        Map<String, Map<String, String>> sortMap = new TreeMap<>(String::compareTo);
        sortMap.putAll(abilityMap);

        write(sortMap);

    }

    public static void write(Map<String, Map<String, String>> abilityMap) {
        String outputPath = "src/main/resources/out.ini";
        try (PrintWriter writer = new PrintWriter(outputPath)) {
            for (Map.Entry<String, Map<String, String>> entry : abilityMap.entrySet()) {
                writer.println("[" + entry.getKey() + "]");

                // 判断技能是否只有1级
                int levels = getLevel(entry);

                // 处理多等级的map
                Map<String, String> data = entry.getValue();
                Map<String, Object> cache = new HashMap<>(data.size());
                for (Map.Entry<String, String> stringEntry : data.entrySet()) {
                    String key = stringEntry.getKey();
                    if ("Buttonpos".equals(key)) {
                        String[] pos = stringEntry.getValue().split(",");
                        cache.put("Buttonpos_1", pos[0].trim());
                        cache.put("Buttonpos_2", pos[1].trim());
                        continue;
                    }


                    char lastChar = key.charAt(key.length() - 1);
                    if (Character.isDigit(lastChar)) {
                        // 去掉后面的数字
                        int pointer = trimTailingNumber(key);
                        String newKey = key.substring(0, pointer + 1);
                        int whichLevel = Integer.parseInt(key.substring(pointer + 1));
                        if ("Targetattach".equals(newKey)) {
                            cache.put(key, stringEntry.getValue());
                            continue;
                        }
                        if (cache.containsKey(newKey)) {

                            try {
                                List<String> list = (List<String>) cache.get(newKey);
                                if (whichLevel - 1 < list.size()) {
                                    list.set(whichLevel - 1, stringEntry.getValue());
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            List<String> list = new ArrayList<>(Collections.nCopies(levels, null));
                            if (whichLevel - 1 < list.size()) {
                                list.set(whichLevel - 1, stringEntry.getValue());
                            }
                            cache.put(newKey, list);

                        }
                    } else {
                        cache.put(key, stringEntry.getValue());
                    }
                }
                for (Map.Entry<String, Object> stringEntry : cache.entrySet()) {

                    Object value = stringEntry.getValue();
                    writer.write(stringEntry.getKey() + "=");
                    if (value instanceof List) {
                        List<String> list = (List<String>) value;
                        if (list.size() == 1) {
                            writeValue(writer, stringEntry, list.get(0));
                        } else{
                            writer.write("{");
                            for (String s : list) {
                                writeValue(writer, stringEntry, s);
                                writer.write(",");
                            }
                            writer.write("}");
                        }
                    } else {
                        writeValue(writer, stringEntry, (String) value);
                    }
                    writer.println();
                }
                writer.println();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static int getLevel(Map.Entry<String, Map<String, String>> entry) {
        String levels = entry.getValue().get("levels");
        if (levels == null || "1".equals(levels.trim())) {
            return 1;
        }
        return Integer.parseInt(levels);
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

    private static void writeValue(PrintWriter writer, Map.Entry<String, Object> stringEntry, String value) {
        try {
            Integer.parseInt(value);
            writer.write(value);
        } catch (Exception e) {
            try {
                Double.parseDouble(value);
                writer.write(value);
            } catch (Exception ex) {
                writer.write("\"" + value + "\"");
            }
        }
    }
}
