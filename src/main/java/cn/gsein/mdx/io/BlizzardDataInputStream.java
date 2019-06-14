/*
 * Decompiled with CFR 0_119.
 */
package cn.gsein.mdx.io;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class BlizzardDataInputStream extends BufferedInputStream {
    private long offset = 0;

    public BlizzardDataInputStream(InputStream in) {
        super(in);
    }

    private static int convertToInt(byte[] b) {
        return b[0] & 255 | (b[1] & 255) << 8 | (b[2] & 255) << 16 | (b[3] & 255) << 24;
    }

    private static int convertNToInt(byte[] b) {
        int result = 0;
        int i = 0;
        while (i < b.length) {
            result |= (b[i] & 255) << i * 8;
            ++i;
        }
        return result;
    }

    public static void main(String[] args) {
    }

    private static short convertToShort(byte[] b) {
        return (short) (b[0] << 8 | b[1] & 255);
    }

    @Override
    public long skip(long count) throws IOException {
        System.out.println("Skipping: " + count);
        throw new Error("BDIS: skip is buggy!");
    }

    public long getOffset() {
        return this.offset;
    }

    public byte readByte() throws IOException {
        byte[] b = new byte[1];
        ++this.offset;
        this.read(b);
        return b[0];
    }

    @Override
    public int read(byte[] b) throws IOException {
        this.offset += (long) b.length;
        return super.read(b);
    }

    public void skipUntilZeroInt() throws IOException {
        int numZeroBytes = 0;
        do {
            ++this.offset;
            int readByte = this.read();
            if (readByte == 0) {
                if (++numZeroBytes != 4) continue;
                return;
            }
            if (readByte == -1) {
                return;
            }
            numZeroBytes = 0;
        } while (true);
    }

    public short readShort() throws IOException {
        byte[] b = new byte[2];
        this.offset += 2;
        this.read(b, 0, 2);
        return BlizzardDataInputStream.convertToShort(b);
    }

    public int readInt() throws IOException {
        byte[] b = new byte[4];
        this.offset += 4;
        this.read(b, 0, 4);
        return BlizzardDataInputStream.convertToInt(b);
    }

    public int readNByteInt(int numBytes) throws IOException {
        byte[] b = new byte[numBytes];
        this.offset += (long) numBytes;
        this.read(b, 0, numBytes);
        return BlizzardDataInputStream.convertNToInt(b);
    }

    public boolean readBool() throws IOException {
        if (this.readInt() == 1) {
            return true;
        }
        return false;
    }

    public float readFloat() throws IOException {
        return Float.intBitsToFloat(this.readInt());
    }

    public char readChar() throws IOException {
        ++this.offset;
        return (char) this.read();
    }

    public char[] readChars(int charCount) throws IOException {
        char[] c = new char[charCount];
        int i = 0;
        while (i < charCount) {
            c[i] = this.readChar();
            ++i;
        }
        return c;
    }

    public String readCharsAsString(int charCount) throws IOException {
        char[] c = this.readChars(charCount);
        int i = 0;
        while (i < charCount) {
            if (c[i] == '\u0000') {
                return String.valueOf(c, 0, i);
            }
            ++i;
        }
        return String.valueOf(c);
    }

    public String readCharsAsStringCheckNull(int charCount) throws IOException {
        char[] result = this.readChars(charCount);
        boolean isNull = true;
        int i = 0;
        while (i < charCount) {
            if (result[i] != '\u0000') {
                isNull = false;
            }
            ++i;
        }
        if (isNull) {
            return null;
        }
        return String.valueOf(result);
    }

    public String readExportSig() throws IOException {
        char[] c = this.readChars(4);
        int i = 0;
        while (i < 4) {
            if (c[i] == '\uffff') {
                return null;
            }
            ++i;
        }
        return String.valueOf(c);
    }

    public String readString() throws IOException {
        StringBuilder sb = new StringBuilder(16);
        int curVal = this.read();
        while (curVal != 0) {
            if (curVal == -1) {
                return null;
            }
            sb.append((char) curVal);
            curVal = this.read();
            ++this.offset;
        }
        ++this.offset;
        return sb.toString();
    }

    @Override
    public void close() throws IOException {
        this.in.close();
    }
}

