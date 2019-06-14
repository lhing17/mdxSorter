package cn.gsein.mdx.trigger131;

import cn.gsein.mdx.io.BlizzardDataInputStream;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

/**
 * @author G. Seinfeld
 * @date 2019/06/14
 */
@Getter
@Setter
public class Category {
    private int prefix; // 前缀 0x4
    private int index; // 0x02000000 + offset
    private String name;
    private int unknownNum0;
    private int unknownNum1;
    private int unknownNum2;

    public Category fromFile(BlizzardDataInputStream wtg) throws IOException {
        prefix = Constants.PREFIX_CATEGORY;
        index = wtg.readInt();
        name = wtg.readString();
        unknownNum0 = wtg.readInt();
        unknownNum1 = wtg.readInt();
        unknownNum2 = wtg.readInt();
        return this;
    }

}
