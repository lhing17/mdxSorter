package cn.gsein.mdx.trigger;

import cn.gsein.mdx.io.BlizzardDataInputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author G. Seinfeld
 * @date 2019/06/14
 */
public class TriggerIO {
    private String wtg_header1;
    private int wtg_header2;
    private int wct_fileVersion;
    private int wtg_unknownInt;

    class WctHeader {
        private int fileVersion;
        private String customScriptComment;
        private int headerSize;
        private String headerCode;

        public WctHeader(int fileVersion, String customScriptComment, int headerSize, String headerCode) {
            this.fileVersion = fileVersion;
            this.customScriptComment = customScriptComment;
            this.headerSize = headerSize;
            this.headerCode = headerCode;
        }

        @Override
        public String toString() {
            return "WctHeader{" +
                    "fileVersion=" + fileVersion +
                    ", customScriptComment='" + customScriptComment + '\'' +
                    ", headerSize=" + headerSize +
                    ", headerCode='" + headerCode + '\'' +
                    '}';
        }
    }

    /**
     * WTG文件结构：
     * 四位文件头(WTG!)
     * 整数文件头
     * 类别数
     * 数据（下标4字节整数 字符串读到0为止 是否为注释4字节整数）
     * 未知4字节整数
     * 全局变量数
     * 数据 （下标4字节整数 字符串读到0为止 是否为注释4字节整数）
     * 触发数
     * 数据     *
     * WCT文件结构：
     *
     * @param wct_File
     * @param wtg_File
     * @throws IOException
     */
    public void readTriggerFiles(File wct_File, File wtg_File) throws IOException {
        BlizzardDataInputStream wct = new BlizzardDataInputStream(new FileInputStream(wct_File));
        BlizzardDataInputStream wtg = new BlizzardDataInputStream(new FileInputStream(wtg_File));
        int wct_numTriggers;
        // 1. 读取wtg的文件头
        readWtgHeader(wtg);
        // 2. 读取wct文件头
        this.wct_fileVersion = wct.readInt();
        String customScriptComment = wct.readString();
        int headersize = wct.readInt();
        String headercode = "";
        if (headersize > 0) {
            headercode = wct.readString();
        }
        WctHeader header = new WctHeader(wct_fileVersion, customScriptComment, headersize, headercode);
        System.out.println(header);
        int numCategories = wtg.readInt();
        int i = 0;
        List<CategoryData> categoryDataList = new ArrayList<>();
        while (i < numCategories) {
            CategoryData c = new CategoryData();
            c.fromFile(wtg);
            categoryDataList.add(c);
            ++i;
        }
        System.out.println(categoryDataList);

        this.wtg_unknownInt = wtg.readInt();

        int numVars = wtg.readInt();
        int i2 = 0;
        List<GlobalVarData> globalVarDataList = new ArrayList<>();
        while (i2 < numVars) {
            GlobalVarData c = new GlobalVarData();
            c.fromFile(wtg);
            ++i2;
            globalVarDataList.add(c);
        }
        System.out.println(globalVarDataList);
        int wtg_numTriggers = wtg.readInt();
        if (wtg_numTriggers != (wct_numTriggers = wct.readInt())) {
            throw new RuntimeException("The number of triggers in the wtg file does not match that in the wct file!");
        }
        int i3 = 0;
        List<TriggerData> triggerDataList = new ArrayList<>();
        while (i3 < wtg_numTriggers) {
            TriggerData c = new TriggerData();
            c.fromFiles(wtg, wct);
            triggerDataList.add(c);
            ++i3;
        }
        System.out.println(triggerDataList);
        wct.close();
        wtg.close();
    }

    private void readWtgHeader(BlizzardDataInputStream wtg) throws IOException {
        this.wtg_header1 = wtg.readCharsAsString(4);
        this.wtg_header2 = wtg.readInt();
    }

    public static void main(String[] args) throws IOException {
        TriggerIO triggerIO = new TriggerIO();
        File wtgFile = new File("src/main/resources/war3map.wtg");
        File wctFile = new File("src/main/resources/war3map.wct");
        triggerIO.readTriggerFiles(wctFile, wtgFile);
        System.out.println(triggerIO.wtg_header1);
        System.out.println(triggerIO.wtg_header2);
        System.out.println(triggerIO.wct_fileVersion);
    }

}
