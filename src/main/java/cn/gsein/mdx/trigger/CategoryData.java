/*
 * Decompiled with CFR 0_119.
 */
package cn.gsein.mdx.trigger;

import cn.gsein.mdx.io.BlizzardDataInputStream;
import cn.gsein.mdx.io.BlizzardDataOutputStream;

import java.io.IOException;

public class CategoryData {
    private int index;
    private String name;
    private boolean isComment;

    public void fromFile(BlizzardDataInputStream b) throws IOException {
        this.index = b.readInt();
        this.name = b.readString();
        this.isComment = b.readInt() == 1;
    }

    public void toFile(BlizzardDataOutputStream b) throws IOException {
        b.writeInt(this.index);
        b.writeString(this.name);
        b.writeBool(this.isComment);
    }

    @Override
    public String toString() {
        return "CategoryData{" +
                "index=" + index +
                ", name='" + name + '\'' +
                ", isComment=" + isComment +
                '}';
    }
}

