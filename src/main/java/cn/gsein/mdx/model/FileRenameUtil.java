package cn.gsein.mdx.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * @author G. Seinfeld
 * @since 2020-08-04
 */
public class FileRenameUtil {
    public static void main(String[] args) throws IOException {
        Path path = Paths.get("E:\\IdeaProjects\\jzjh3\\jzjh3\\resource\\war3mapImported");
        Files.walkFileTree(path, new FileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                String fileName = file.toString();
                if (fileName.endsWith("0.mdx") || fileName.endsWith("0.MDX")){
                    String newFileName = fileName.substring(0, fileName.length() - 5) + ".mdx";
                    System.out.println(newFileName);
                    File oldFile = new File(fileName);
                    File newFile = new File(newFileName);
                    boolean b = oldFile.renameTo(newFile);
                }

                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }
        });
    }
}
