package cn.gsein.mdx.trigger131;

import cn.gsein.mdx.io.BlizzardDataInputStream;
import lombok.Getter;
import lombok.Setter;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * war3 1.31版本的触发器数据
 *
 * @author G. Seinfeld
 * @date 2019/06/14
 */
@Getter
@Setter
public class WtgData_131 {
    private WtgHeader wtgHeader;
    private int globalVarNumber;
    private List<GlobalVarData_131> globalVars = new ArrayList<>();
    private int totalNumber;
    private MapName mapName;

    private List<Category> categories;

    public void fromFile(BlizzardDataInputStream wtg) throws IOException {
        // 1. 读取文件头
        String keyword = wtg.readCharsAsString(4);
        int header = wtg.readInt();
        if (!"WTG!".equals(keyword) || header != 0x80000004) {
            throw new IllegalArgumentException("不是合法的WTG文件");
        }
        int version = wtg.readInt();
        List<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < 17; i++) {
            numbers.add(wtg.readInt());
        }
        wtgHeader = new WtgHeader(keyword, header, version, numbers);
        System.out.println(wtgHeader);

        // 2. 读取全局变量声明
        globalVarNumber = wtg.readInt();
        for (int i = 0; i < globalVarNumber; i++) {
            globalVars.add(new GlobalVarData_131().fromFile(wtg));
        }
        System.out.println(globalVars);
        // 3. 读取地图名
        totalNumber = wtg.readInt(); // 读取地图名、组别、触发器、变量、注释、自定义脚本的总数
        System.out.println(totalNumber);
        mapName = new MapName().fromFile(wtg);
        System.out.println(mapName);

    }

    public static void main(String[] args) throws IOException {
        WtgData_131 wtgData_131 = new WtgData_131();
        wtgData_131.fromFile(new BlizzardDataInputStream(new FileInputStream("src/main/resources/war3map.wtg")));
    }

}
