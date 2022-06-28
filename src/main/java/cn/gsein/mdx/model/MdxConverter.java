package cn.gsein.mdx.model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MdxConverter {
    public static List<Map<String, Object>> parseTextures(State state, int size) throws IOException {
        RandomAccessFile reader = state.getReader();
        long startPos = reader.getFilePointer();
        List<Map<String, Object>> textures = new ArrayList<>();
        while (reader.getFilePointer() < startPos + size) {
            Map<String, Object> texture = new HashMap<>();
            texture.put("replaceId", state.readInt());
            texture.put("image", state.readStr(256));
            texture.put("unknown", state.readInt());
            texture.put("flags", state.readInt());
            textures.add(texture);
        }
        return textures;
    }

    public static void convert(String fromFileName, String toFileName) throws IOException {
        RandomAccessFile reader = new RandomAccessFile(fromFileName, "r");
        LittleEndianWriter writer = new LittleEndianWriter(new RandomAccessFile(toFileName, "rw"));
        State state = new State(reader);
        if (!"MDLX".equals(state.keyword())) {
            throw new IllegalStateException("Not a mdx model");
        }
        writer.writeStr("MDLX", 4);
        List<Map<String, Object>> textures;
        while (reader.getFilePointer() < reader.length()) {
            String keyword = state.keyword();
            writer.writeStr(keyword, 4);

            int size = state.readInt();
            writer.writeInt(size);
            if ("TEXS".equals(keyword)) {
                textures = parseTextures(state, size);
                for (Map<String, Object> texture : textures) {
                    String path = (String) texture.get("image");
                    if (path != null && !"".equals(path) && !path.contains("\\")) {
                        path = "war3mapImported\\" + path;
                    }
                    writer.writeInt((Integer) texture.get("replaceId"));
                    writer.writeStr(path, 256);
                    writer.writeInt((Integer) texture.get("unknown"));
                    writer.writeInt((Integer) texture.get("flags"));
                }
            } else {
                byte[] b = new byte[size];
                reader.readFully(b);
                writer.writeBytes(b);
            }
        }
        reader.close();
        writer.getWriter().close();
    }

    public static void main(String[] args) throws IOException {
        //convert("src/main/resources/huabao.mdx", "src/main/resources/bfx.mdx");
        Path path= Paths.get("E:\\War3Map\\拆地图\\杰八零\\output\\used");
        Map<String, String> map = new HashMap<>();
        Files.walkFileTree(path, new FileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path path, BasicFileAttributes basicFileAttributes) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path path, BasicFileAttributes basicFileAttributes) throws IOException {
                String fileName = path.toString();
                int dotIndex = fileName.lastIndexOf(".");
                String toFileName = fileName.substring(0, dotIndex) + "0" + fileName.substring(dotIndex);
                if (fileName.endsWith(".mdx") || fileName.endsWith(".MDX")){
                    map.put(fileName, toFileName);
                    System.out.println(path);
//                    System.out.println(toFileName);
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path path, IOException e) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path path, IOException e) throws IOException {
                return FileVisitResult.CONTINUE;
            }
        });

        for (Map.Entry<String, String> entry : map.entrySet()) {
            convert(entry.getKey(), entry.getValue());
        }
    }
}
