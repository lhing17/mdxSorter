package cn.gsein.mdx.model;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Md5Scanner {
    private static final char[] hexCode = "0123456789ABCDEF".toCharArray();

    /**
     * 计算文件 MD5
     *
     * @param file
     * @return 返回文件的md5字符串，如果计算过程中任务的状态变为取消或暂停，返回null， 如果有其他异常，返回空字符串
     */
    protected static String calcMD5(File file) {
        try (InputStream stream = Files.newInputStream(file.toPath(), StandardOpenOption.READ)) {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] buf = new byte[8192];
            int len;
            while ((len = stream.read(buf)) > 0) {
                digest.update(buf, 0, len);
            }
            return toHexString(digest.digest());
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String toHexString(byte[] data) {
        StringBuilder r = new StringBuilder(data.length * 2);
        for (byte b : data) {
            r.append(hexCode[(b >> 4) & 0xF]);
            r.append(hexCode[(b & 0xF)]);
        }
        return r.toString();
    }

    public static Map<String, String> scanMd5(String dirName) throws IOException {
        Map<String, String> map = new HashMap<>();
        Path start = Paths.get(dirName);
        Files.walkFileTree(start, new FileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
                File file = path.toFile();
                map.put(file.getName(), calcMD5(file));
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }
        });

        return map;
    }

    public static void main(String[] args) throws IOException {
        Map<String, String> map = scanMd5("E:\\IdeaProjects\\hero_legend\\hero_legend\\resource\\war3mapImported");
        Map<String, List<String>> reverseMap = new HashMap<>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (!reverseMap.containsKey(value)) {
                List<String> keys = new ArrayList<>();
                keys.add(key);
                reverseMap.put(value, keys);
            } else {
                reverseMap.get(value).add(key);
            }
        }
        int count = 0;
        for (Map.Entry<String, List<String>> entry : reverseMap.entrySet()) {
            if (entry.getValue().size() > 1) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
                count++;
            }
        }
        System.out.println(count);

    }
}
