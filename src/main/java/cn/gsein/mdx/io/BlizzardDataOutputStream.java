/*
 * Decompiled with CFR 0_119.
 */
package cn.gsein.mdx.io;

import java.io.*;

public class BlizzardDataOutputStream
extends BufferedOutputStream {
    public BlizzardDataOutputStream(File file) throws FileNotFoundException {
        super(new FileOutputStream(file));
    }

    public BlizzardDataOutputStream(File file, boolean append) throws FileNotFoundException {
        super(new FileOutputStream(file, append));
    }

    static byte[] convertIntToByteArray(int i) {
        byte[] result = new byte[4];
        long l = i >= 0 ? (long)i : 0x100000000L + (long)i;
        int j = 0;
        while (j < 4) {
            result[j] = (byte)(l % 256);
            l /= 256;
            ++j;
        }
        return result;
    }

    static byte[] convertIntToNByteArray(int i, int numBytes) {
        byte[] result = new byte[numBytes];
        long l = i >= 0 ? (long)i : 0x100000000L + (long)i;
        int j = 0;
        while (j < numBytes) {
            result[j] = (byte)(l % 256);
            l /= 256;
            ++j;
        }
        return result;
    }

    public void writeChars(char[] toWrite) throws IOException {
        byte[] b = new byte[toWrite.length];
        int i = 0;
        while (i < toWrite.length) {
            b[i] = (byte)toWrite[i];
            ++i;
        }
        this.write(b);
    }

    public void writeInt(int toWrite) throws IOException {
        this.write(BlizzardDataOutputStream.convertIntToByteArray(toWrite));
    }

    public void writeNByteInt(int toWrite, int numBytes) throws IOException {
        this.write(BlizzardDataOutputStream.convertIntToNByteArray(toWrite, numBytes));
    }

    public void writeBool(boolean toWrite) throws IOException {
        this.writeInt(toWrite ? 1 : 0);
    }

    public void writeByte(int toWrite) throws IOException {
        this.write(new byte[]{(byte)toWrite});
    }

    public void writeFloat(float toWrite) throws IOException {
        this.writeInt(Float.floatToIntBits(toWrite));
    }

    public void writeFourByteString(String toWrite) throws IOException {
        byte[] result = new byte[4];
        int i = 0;
        while (i < 4) {
            result[i] = toWrite != null && i < toWrite.length() ? (byte)toWrite.charAt(i) : 0;
            ++i;
        }
        this.write(result);
    }

    public void writeNByteString(String toWrite, int charCount) throws IOException {
        byte[] result = new byte[charCount];
        int i = 0;
        while (i < charCount) {
            result[i] = toWrite != null && i < toWrite.length() ? (byte)toWrite.charAt(i) : 0;
            ++i;
        }
        this.write(result);
    }

    public void writeString(String toWrite) throws IOException {
        int length = toWrite == null ? 0 : toWrite.length();
        byte[] result = new byte[length + 1];
        int i = 0;
        while (i < length) {
            result[i] = (byte)toWrite.charAt(i);
            ++i;
        }
        result[length] = 0;
        this.write(result);
    }
}

