package cn.gsein.mdx.model;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class LittleEndianWriter {
    private RandomAccessFile writer;

    public RandomAccessFile getWriter() {
        return writer;
    }

    public void setWriter(RandomAccessFile writer) {
        this.writer = writer;
    }

    public LittleEndianWriter(RandomAccessFile writer) {
        this.writer = writer;
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public void writeInt(int i) throws IOException {
        byte[] b = new byte[4];
        b[3] = (byte) ((i & 0xFF000000) >>> 24);
        b[2] = (byte) ((i & 0xFF0000) >>> 16);
        b[1] = (byte) ((i & 0xFF00) >>> 8);
        b[0] = (byte) (i & 0xFF);
        System.out.println(bytesToHex(b));
        writer.write(b);
    }

    public void writeStr(String s, int length) throws IOException {
        byte[] b = Arrays.copyOf(s.getBytes(StandardCharsets.UTF_8), length);
        writer.write(b);
    }

    public void writeBytes(byte[] b) throws IOException{
        writer.write(b);
    }
}
