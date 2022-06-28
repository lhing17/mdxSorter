package cn.gsein.mdx.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * MDX文件整理工具
 *
 * @author G. Seinfeld
 * @date 2019/06/10
 */
public class MdxSorter {

    private static List<String> getMdxFiles(File root) {
        List<String> mdxFiles = new ArrayList<>();
        File[] rootFiles = root.listFiles();
        if (rootFiles != null) {
            for (File file : rootFiles) {
                if (file.isDirectory()) {
                    mdxFiles.addAll(getMdxFiles(file));
                } else if (file.getName().endsWith(".mdx")) {
                    mdxFiles.add(file.getAbsolutePath());
                }
            }
        }
        return mdxFiles;
    }

    /**
     * 对mdx文件进行分类
     *
     * @param mode 1是复制 2是剪切
     */
    public static void sort(String rootPath, String dest, int mode) {
        File root = new File(rootPath);

        List<String> mdxFiles = getMdxFiles(root);
        int mdxCounter = 0;
        int counter = 0;
        for (String mdxFile : mdxFiles) {
            String mdxName = mdxFile.substring(mdxFile.lastIndexOf(File.separator));
            mdxName = mdxName.substring(0, mdxName.lastIndexOf(".mdx"));
            String completeMdxName = dest + File.separator + mdxName;
            mdxCounter++;
            try {
                List<String> blpFiles = MdxParser.parse(mdxFile);
                for (String blpFile : blpFiles) {
                    if (blpFile != null && !"".equals(blpFile)){
                        counter++;
                        int index = blpFile.lastIndexOf(File.separator);
                        String path = index == -1 ? "" : blpFile.substring(0, index);
                        System.out.println(blpFile);
                        System.out.println("from: " + root.getAbsolutePath() + File.separator + blpFile);
                        System.out.println("to: " + Paths.get(completeMdxName, path));
                        if (mode == 1){
                            FileUtil.copyGeneralFile(root.getAbsolutePath() + File.separator + blpFile,
                                    Paths.get(completeMdxName, path).toString());
                        } else {
                            FileUtil.cutGeneralFile(root.getAbsolutePath() + File.separator + blpFile,
                                    Paths.get(completeMdxName, path).toString());
                        }

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (mode == 1){
                FileUtil.copyGeneralFile(mdxFile, dest + File.separator + mdxName);
            } else {
                FileUtil.cutGeneralFile(mdxFile, dest + File.separator + mdxName);
            }
        }
        System.out.println(mdxCounter);
        System.out.println(counter);
    }

    public static void main(String[] args) throws IOException {
        sort("E:\\War3Map\\拆地图\\杰八零\\jbl", "E:\\War3Map\\拆地图\\杰八零\\output", 1);
    }
}
