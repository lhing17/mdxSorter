package cn.gsein.mdx.model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author G. Seinfeld
 * @date 2019/09/16
 */
public class UsageReader {
    public static void main(String[] args) {
        List<String> mdxList = readListFromFile("mdx");

        StringBuilder unusedMdx = new StringBuilder();
        String resourceRoot = "G:\\git_repos\\JZJH2\\jzjh2\\resource";
        File dir = new File(resourceRoot);
        File[] files = dir.listFiles((dir1, name) -> name.endsWith(".mdx"));
        assert files != null;
        for (File file : files) {
            if (!mdxList.contains(file.getName())) {
                unusedMdx.append(file.getName()).append("\n");
                System.out.println(file.getName());
            }
        }
        System.out.println("--------------------我是华丽的分割线---------------------");
        dir = new File(resourceRoot + "\\war3mapImported");
        files = dir.listFiles((dir1, name) -> name.endsWith(".mdx"));
        assert files != null;
        for (File file : files) {
            if (!mdxList.contains("war3mapImported\\\\" + file.getName())) {
                unusedMdx.append("war3mapImported\\\\").append(file.getName()).append("\n");
                System.out.println(file.getName());
            }
        }
        FileUtil.writeToFile(unusedMdx.toString(), new File("src\\main\\resources\\unusedMdx.txt"));
    }

    private static List<String> readListFromFile(String type) {
        List<String> list = new ArrayList<>();
        InputStream inputStream = UsageReader.class.getClassLoader().getResourceAsStream(type + ".txt");
        assert inputStream != null;
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String filename = line.split("\\t")[1];
                if (!filename.contains("\\") || filename.startsWith("\"war3mapImported")) {
                    list.add(filename.replace("\"", ""));
                }
            }
            System.out.println(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}
