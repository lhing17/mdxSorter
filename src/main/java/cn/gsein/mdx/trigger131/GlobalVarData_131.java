/*
 * Decompiled with CFR 0_119.
 */
package cn.gsein.mdx.trigger131;

import cn.gsein.mdx.io.BlizzardDataInputStream;
import cn.gsein.mdx.io.BlizzardDataOutputStream;

import java.io.IOException;

public class GlobalVarData_131 {
    private String name;
    private String type;
    private int unknown;
    private boolean isArray;
    private int arraySize;
    private boolean isInited;
    private String initValue;
    private int index; //0x06000000+offset
    private int categoryIndex; //0x02000000 + offset

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return "GlobalVarData_131{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", unknown=" + unknown +
                ", isArray=" + isArray +
                ", arraySize=" + arraySize +
                ", isInited=" + isInited +
                ", initValue='" + initValue + '\'' +
                ", index=" + Integer.toHexString(index) +
                ", categoryIndex=" + Integer.toHexString(categoryIndex) +
                '}';
    }

    public GlobalVarData_131 fromFile(BlizzardDataInputStream r) throws IOException {
        this.name = r.readString();
        this.type = r.readString();
        this.unknown = r.readInt();
        this.isArray = r.readInt() == 1;
        this.arraySize = r.readInt();
        this.isInited = r.readInt() == 1;
        this.initValue = r.readString();
        this.index = r.readInt();
        this.categoryIndex = r.readInt();
        return this;
    }

    public void toFile(BlizzardDataOutputStream b) throws IOException {
        b.writeString(this.name);
        b.writeString(this.type);
        b.writeInt(this.unknown);
        b.writeBool(this.isArray);
        b.writeInt(this.arraySize);
        b.writeBool(this.isInited);
        b.writeString(this.initValue);
    }


}

