package cn.gsein.slk;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author G. Seinfeld
 * @since 2020-08-04
 */
public class AbilityReader {

    public static void main(String[] args) throws IOException {
        String fileName = "E:\\IdeaProjects\\jzjh3\\wlhd\\units\\campaignabilitystrings.txt";
        Map<String, Map<String, String>> abilityMap = read(fileName);
        System.out.println(abilityMap);

    }

    public static Map<String, Map<String, String>> read(String fileName) throws IOException {
        Map<String, Map<String, String>> abilityMap = new HashMap<>(2000);
        String currentKey = "";
        Map<String, String> currentMap = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName));) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("[")) {
                    currentKey = line.substring(1, 5);
                    currentMap = new HashMap<>();
                    abilityMap.put(currentKey, currentMap);
                } else if (line.contains("=")) {
                    String[] kv = line.split("=");
                    String key = kv[0];
                    String value;
                    if (kv.length > 1) {
                        value = kv[1];
                    } else {
                        value = "";
                    }
                    currentMap.put(key.trim(), value.trim());
                }
            }
        }
        return abilityMap;
    }
}
