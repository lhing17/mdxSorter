package cn.gsein.mdx.model;

import java.io.EOFException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

/**
 * @author G. Seinfeld
 * @date 2019/06/10
 */
public class State {
    private RandomAccessFile reader;

    public RandomAccessFile getReader() {
        return reader;
    }

    public void setReader(RandomAccessFile reader) {
        this.reader = reader;
    }

    public State(RandomAccessFile reader) {
        this.reader = reader;
    }

    public String keyword() throws IOException {
        byte[] b = new byte[4];
        reader.readFully(b);
        return new String(b);
    }

    public void assertKeyword(String keyword, String errorText) throws IOException {
        assert keyword().equals(keyword) : errorText;
    }

    public int readInt() throws IOException {
        int ch1 = reader.read();
        int ch2 = reader.read();
        int ch3 = reader.read();
        int ch4 = reader.read();
        if ((ch1 | ch2 | ch3 | ch4) < 0)
            throw new EOFException();
        return ((ch4 << 24) + (ch3 << 16) + (ch2 << 8) + (ch1));
    }

    public String readStr(int length) throws IOException {
        byte[] b = new byte[length];
        reader.readFully(b);

        int realLength = length;
        while(realLength > 0 && b[realLength - 1] == 0){
            realLength--;
        }
        byte[] rb = Arrays.copyOf(b, realLength);
        return new String(rb);
    }
}
