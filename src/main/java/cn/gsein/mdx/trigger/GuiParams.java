/*
 * Decompiled with CFR 0_119.
 */
package cn.gsein.mdx.trigger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class GuiParams {
    private static GuiParams cache = null;
    private HashMap<String, Integer> table = new HashMap();

    private void readGuiFile(File f) throws IOException {
        String line;
        BufferedReader b = new BufferedReader(new FileReader(f));
        while ((line = b.readLine()) != null) {
            String[] s = line.split(":");
            this.table.put(s[0], Integer.parseInt(s[1]));
        }
        b.close();
    }

    public static GuiParams getParams() throws IOException {
        if (cache == null) {
            cache = new GuiParams();
        }
        return cache;
    }

    private GuiParams() throws IOException {
        this.readGuiFile(new File("src/main/resources/misc/GuiParams2.req"));
        this.readGuiFile(new File("src/main/resources/misc/GuiParams.req"));
        this.readGuiFile(new File("src/main/resources/misc/GuiOperators.req"));
    }

    public int num(String name) throws IOException {
        try {
            return this.table.get(name);
        }
        catch (NullPointerException e) {
            throw new IOException("Unknown GUI Function: \"" + name + "\"");
        }
    }
}

