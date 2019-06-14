package cn.gsein.mdx.trigger131;

import cn.gsein.mdx.io.BlizzardDataInputStream;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.Arrays;

/**
 * @author G. Seinfeld
 * @date 2019/06/14
 */
@Getter
@Setter
public class MapName {
    private int prefix; // 前缀 0x1
    private int index; // 下标 0x0
    private String name; // 地图名
    private int[] suffix; // 12个字节的后缀，0x0 0x0 0xFFFFFFFF

    public MapName fromFile(BlizzardDataInputStream wtg) throws IOException {
        this.prefix = wtg.readInt();
        this.index = wtg.readInt();
        this.name = wtg.readString();
        this.suffix = new int[3];
        suffix[0] = wtg.readInt();
        suffix[1] = wtg.readInt();
        suffix[2] = wtg.readInt();
        return this;
    }

    @Override
    public String toString() {
        return "MapName{" +
                "prefix=" + prefix +
                ", index=" + index +
                ", name='" + name + '\'' +
                ", suffix=" + Arrays.toString(suffix) +
                '}';
    }
}
