/*
 * Decompiled with CFR 0_119.
 */
package cn.gsein.mdx.trigger;

import cn.gsein.mdx.io.BlizzardDataInputStream;
import cn.gsein.mdx.io.BlizzardDataOutputStream;

import java.io.IOException;

public class GlobalVarData {
    private String name;
    private String type;
    private int unknown;
    private boolean isArray;
    private int arraySize;
    private boolean isInited;
    private String initValue;

    public String getName() {
        return this.name;
    }

    public String toString() {
        String result = "";
        result = result + "->Var: " + this.name;
        result = result + "\n   Type: " + this.type;
        result = result + "\n   unknown: " + this.unknown;
        result = result + "\n   isArray?: " + this.isArray;
        result = result + "\n   arraySize: " + this.arraySize;
        result = result + "\n   is Inited?: " + this.isInited;
        result = result + "\n   initialValue?: " + this.initValue;
        return result;
    }

    public GlobalVarData fromFile(BlizzardDataInputStream r) throws IOException {
        this.name = r.readString();
        this.type = r.readString();
        this.unknown = r.readInt();
        this.isArray = r.readInt() == 1;
        this.arraySize = r.readInt();
        this.isInited = r.readInt() == 1;
        this.initValue = r.readString();
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

