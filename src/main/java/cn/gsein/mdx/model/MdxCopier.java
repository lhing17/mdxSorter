package cn.gsein.mdx.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class MdxCopier {
    public static void main(String[] args) throws IOException {
        // 将文件夹中以0.mdx结尾的文件和以.blp结尾的文件复制到新文件夹中
//        Path from = Paths.get("E:\\War3Map\\拆地图\\杰八零\\output\\used");
//        Path to = Paths.get("E:\\War3Map\\拆地图\\杰八零\\output\\used2");
//        Files.createDirectories(to);
//        MdxCopier.copy(from, to);

        // 将文件夹中0.mdx结尾的文件改名为.mdx结尾
//        Path from = Paths.get("E:\\War3Map\\拆地图\\杰八零\\output\\used2");
//        Files.walk(from).forEach(path -> {
//            if (path.toString().endsWith("0.mdx")) {
//                Path newPath = Paths.get(path.toString().replace("0.mdx", ".mdx"));
//                try {
//                    Files.move(path, newPath);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });

        Map<String, Integer> map = new HashMap<>();
        Path path = Paths.get("F:\\jzjh\\logs\\log.txt");
        Files.readAllLines(path).stream()
                .skip(110898)
                .filter(s->s.contains("调用了函数"))
                .map(s->s.substring(s.indexOf("调用了函数")+6))
                .map(String::trim)
                .forEach(s -> {
                    int count = map.getOrDefault(s, 0);
                    map.put(s, count + 1);
                });
        map.entrySet().stream().sorted(Map.Entry.comparingByValue()).forEach(System.out::println);

        System.out.println(map.values().stream().reduce(0, Integer::sum));
    }

    private static void copy(Path from, Path to) throws IOException {
        Files.walk(from).filter(p -> p.toString().endsWith("0.mdx") || p.toString().endsWith(".blp")).forEach(p -> {
            try {
                Files.copy(p, to.resolve(p.getFileName()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
