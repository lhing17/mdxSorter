package cn.gsein.mdx.trigger131;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 前80个字节是文件头部分
 *
 * @author G. Seinfeld
 * @date 2019/06/14
 */
@Getter
@Setter
public class WtgHeader {
    /**
     * 关键字 WTG!
     */
    private String keyword;
    /**
     * 文件头部标识 0x80000004
     */
    private int header;
    /**
     * 版本标识：7
     */
    private int version;
    /**
     * 未知标识：1
     */
    private int unknown0;
    private int zero0;
    /**
     * 未知标识：0
     */
    private int unknown1;
    private int zero1;
    /**
     * 类别数量
     */
    private int categoryNumber;
    private int zero2;
    /**
     * 触发器数量
     */
    private int triggerNumber;
    private int zero3;
    /**
     * 注释数量
     */
    private int commentNumber;
    private int zero4;
    /**
     * 自定义脚本数量
     */
    private int customScriptNumber;
    private int zero5;
    /**
     * 全局变量数量
     */
    private int globalVarNumber;
    private int zero6;
    /**
     * 未知标识：0
     */
    private int unknown2;
    private int zero7;
    /**
     * 未知标识：2
     */
    private int unknown3;

    WtgHeader(String keyword, int header, int version, List<Integer> numbers) {
        this.keyword = keyword;
        this.header = header;
        this.version = version;
        this.unknown0 = numbers.get(0);
        this.zero0 = numbers.get(1);
        this.unknown1 = numbers.get(2);
        this.zero1 = numbers.get(3);
        this.categoryNumber = numbers.get(4);
        this.zero2 = numbers.get(5);
        this.triggerNumber = numbers.get(6);
        this.zero3 = numbers.get(7);
        this.commentNumber = numbers.get(8);
        this.zero4 = numbers.get(9);
        this.customScriptNumber = numbers.get(10);
        this.zero5 = numbers.get(11);
        this.globalVarNumber = numbers.get(12);
        this.zero6 = numbers.get(13);
        this.unknown2 = numbers.get(14);
        this.zero7 = numbers.get(15);
        this.unknown3 = numbers.get(16);
    }

    @Override
    public String toString() {
        return "WtgHeader{" +
                "keyword='" + keyword + '\'' +
                ", header=" + header +
                ", version=" + version +
                ", categoryNumber=" + categoryNumber +
                ", triggerNumber=" + triggerNumber +
                ", commentNumber=" + commentNumber +
                ", customScriptNumber=" + customScriptNumber +
                ", globalVarNumber=" + globalVarNumber +
                '}';
    }
}
