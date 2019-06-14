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
public class TriggerData_131 {
    private int prefix; // 0x08
    private String name;
    private String description;
    private int unknown;
    private int index;
    private int isEnabled;
    private int isCustomText;
    private int isNotInitiallyOn;
    private int isRanOnMapInit;
    private int categoryIndex;
    private int ecaNums; // 指事件、条件和动作数量

    public TriggerData_131 fromFile(BlizzardDataInputStream wtg) throws IOException {
        prefix = Constants.PREFIX_TRIGGER;
        name = wtg.readString();
        description = wtg.readString();
        unknown = wtg.readInt();
        index = wtg.readInt();
        isEnabled = wtg.readInt();
        isCustomText = wtg.readInt();
        isNotInitiallyOn = wtg.readInt();
        isRanOnMapInit = wtg.readInt();
        categoryIndex = wtg.readInt();
        ecaNums = wtg.readInt();
        return this;
    }

    @Override
    public String toString() {
        return "TriggerData_131{" +
                "prefix=" + prefix +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", unknown=" + unknown +
                ", index=" + index +
                ", isEnabled=" + isEnabled +
                ", isCustomText=" + isCustomText +
                ", isNotInitiallyOn=" + isNotInitiallyOn +
                ", isRanOnMapInit=" + isRanOnMapInit +
                ", categoryIndex=" + categoryIndex +
                ", ecaNums=" + ecaNums +
                '}';
    }
}
