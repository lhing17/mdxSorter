package cn.gsein.slk;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * @author G. Seinfeld
 * @since 2020-08-04
 */
public class XlsxReader {

    public static void main(String[] args) throws IOException {
        String fileName = "E:\\IdeaProjects\\jzjh3\\wlhd\\units\\itemdata.xlsx";
        String abiltyFileName = "E:\\IdeaProjects\\jzjh3\\wlhd\\units\\itemfunc.txt";
        Map<String, Map<String, String>> abilityMap = AbilityReader.read(abiltyFileName);
        read(fileName, abilityMap, false);

        System.out.println(abilityMap);

    }

    public static void read(String fileName, Map<String, Map<String, String>> abilityMap, boolean contains) throws IOException {
        FileInputStream inputStream = new FileInputStream(fileName);
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);
        int rowNum = sheet.getPhysicalNumberOfRows();

        Map<Integer, String> keyMap = new HashMap<>();

        List<String> keyList = new ArrayList<>();
        // 根据第一行生成key map
        Row row = sheet.getRow(0);
        int columns = row.getPhysicalNumberOfCells();
        for (int i = 0; i < columns; i++) {
            String key;
            try {
                key = row.getCell(i).getStringCellValue();
            } catch (Exception e) {
                continue;
            }
            keyMap.put(i, key);
        }

        // 第二行开始，填充数据
        for (int i = 1; i < rowNum; i++) {
            row = sheet.getRow(i);
            String id = row.getCell(0).getStringCellValue();
            keyList.add(id);
            Map<String, String> data = abilityMap.computeIfAbsent(id, key -> new HashMap<>(30));
            for (int j = 1; j < columns; j++) {
                if (row.getCell(j) != null) {
                    row.getCell(j).setCellType(CellType.STRING);
                    String value = row.getCell(j).getStringCellValue();
                    if (value != null && !"".equals(value)) {
                        String key = keyMap.get(j);
                        data.put(key, value);
                    }
                }
            }
            abilityMap.put(id, data);
        }
        if (!contains) {
            abilityMap.keySet().removeIf(key -> !keyList.contains(key));
        }
    }
}
