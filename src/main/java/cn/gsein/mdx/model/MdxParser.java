package cn.gsein.mdx.model;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * MDX文件结构解析器
 *
 * @author G. Seinfeld
 * @date 2019/06/10
 */
public class MdxParser {
    public static List<Map<String, Object>> parseTextures(State state, int size) throws IOException {
        RandomAccessFile reader = state.getReader();
        long startPos = reader.getFilePointer();
        List<Map<String, Object>> textures = new ArrayList<>();
        while(reader.getFilePointer() < startPos + size) {
            Map<String, Object> texture = new HashMap<>();
            texture.put("replaceId", state.readInt());
            texture.put("image", state.readStr(256));
            texture.put("unknown", state.readInt());
            texture.put("flags", state.readInt());
            textures.add(texture);
        }
        return textures;
    }
    public static List<String> parse(String filename) throws IOException {
        RandomAccessFile reader = new RandomAccessFile(filename, "r");
        State state = new State(reader);
        if (!"MDLX".equals(state.keyword())) {
            throw new IllegalStateException("Not a mdx model");
        }
        Map<String, Object> keywordMap = new HashMap<>();
        List<Map<String, Object>> textures = new ArrayList<>();
        while (reader.getFilePointer() < reader.length()) {
            String keyword = state.keyword();
            int size = state.readInt();
            keywordMap.put(keyword, size);
            if ("TEXS".equals(keyword)){
                textures = parseTextures(state, size);
            } else{
                reader.skipBytes(size);
            }
        }
        System.out.println(keywordMap);
        System.out.println(textures);
        List<String> blpPaths = textures.stream().map(texture->(String)texture.get("image")).collect(Collectors.toList());
        reader.close();
        return blpPaths;
    }

    public static void main(String[] args) throws IOException {
        //System.out.println(parse("src/main/resources/S_XSCZ_wulotus.MDX"));
        System.out.println(parse("src/main/resources/laser.MDX"));
    }
}
