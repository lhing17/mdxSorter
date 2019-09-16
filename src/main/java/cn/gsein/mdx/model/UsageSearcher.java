package cn.gsein.mdx.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 搜索mdx和blp文件是否被使用的工具
 *
 * @author G. Seinfeld
 * @date 2019/09/16
 */
public class UsageSearcher {

    private static final Pattern MDX_PATTERN = Pattern.compile("\".*?\\.(mdl|mdx)\"");
    private static final Pattern BLP_PATTERN = Pattern.compile("\".*?\\.blp\"");
    private static List<String> mdxCache = new ArrayList<>();
    private static List<String> blpCache = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        String rootDir = "G:\\git_repos\\JZJH2\\jzjh2"; // lni格式地图的根目录，即.w3x文件所在目录
        Files.walkFileTree(Paths.get(rootDir), new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                return super.preVisitDirectory(dir, attrs);
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                System.out.println(file);
                if (file.toString().endsWith(".j") || file.toString().endsWith(".lua") || (file.toString().endsWith(
                        ".ini") && !file.endsWith("imp.ini"))) {
                    FileReader fileReader = new FileReader(file.toString());
                    findUsageAndAppendToFile(new StringBuilder(), file.toString(), fileReader, "mdx");
                    fileReader = new FileReader(file.toString());
                    findUsageAndAppendToFile(new StringBuilder(), file.toString(), fileReader, "blp");
                }

                return FileVisitResult.CONTINUE;
            }
        });
    }

    private static void findUsageAndAppendToFile(StringBuilder buffer, String filename, FileReader fileReader,
                                                 String type) {
        try (BufferedReader reader = new BufferedReader(fileReader)) {
            String line;
            while ((line = reader.readLine()) != null) {
                Pattern pattern;
                List<String> list;
                if ("mdx".equals(type)) {
                    pattern = MDX_PATTERN;
                    list = mdxCache;
                } else {
                    pattern = BLP_PATTERN;
                    list = blpCache;
                }
                Matcher matcher = pattern.matcher(line);
                while (matcher.find()) {
                    String s = matcher.group();
                    s = s.replace(".mdl", ".mdx");
                    if (!list.contains(s)) {
                        list.add(s);
                        buffer.append(filename).append("\t").append(s).append("\n");
                    }
                }
            }
            FileUtil.writeToFile(buffer.toString(), new File("src\\main\\resources\\" + type + ".txt"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
