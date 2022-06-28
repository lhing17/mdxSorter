package cn.gsein.mdx.model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author G. Seinfeld
 * @since 2020-10-14
 */
public class Test {
    public static void main(String[] args) throws Exception {
        createImage("康康", new Font("华文行楷", Font.PLAIN, 40), new File("e:/b.png"), 256, 128);
    }

    // 根据str,font的样式以及输出文件目录
    public static void createImage(String str, Font font, File outFile,
                                   Integer width, Integer height) throws Exception {
        // 创建图片
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) image.getGraphics();

        Path path = Paths.get("src", "main", "resources", "background.png");
        BufferedImage background = ImageIO.read(path.toFile());
        Image scaledInstance = background.getScaledInstance(256, 128, Image.SCALE_SMOOTH);
        g.drawImage(scaledInstance, 0, 0, null);
        //高清代码
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);

        g.setClip(0, 0, width, height);
//        g.setColor(new Color(199, 36, 251));
        // 先用黑色填充整张图片,也就是背景
//        g.fillRect(0, 0, width, height);
        // 再换成红色
        g.setColor(new Color(103, 14, 170));
        // 设置画笔字体
        g.setFont(font);

        /* 用于获得垂直居中y */
        Rectangle clip = g.getClipBounds();
        FontMetrics fm = g.getFontMetrics(font);

        int ascent = fm.getAscent();
        int descent = fm.getDescent();
        int y = (clip.height - (ascent + descent)) / 2 + ascent;

        int stringWidth = fm.stringWidth(str);
        int x = (clip.width - stringWidth) / 2;


        //描边
        FontRenderContext frc = g.getFontRenderContext();
        TextLayout tl = new TextLayout(str, font, frc);
        AffineTransform tx = AffineTransform.getTranslateInstance(x, y);
        Shape sha = tl.getOutline(tx);
        LinearGradientPaint paint = new LinearGradientPaint(x + stringWidth * 0.5f, 0, x + stringWidth * 0.5f, clip.height, new float[]{0, 1f}, new Color[]{new Color(96, 14, 118), new Color(183, 27, 26)});


        g.setPaint(paint);
        g.setStroke(new BasicStroke(5));
        g.draw(sha);

        // 填充
        g.setColor(Color.white);
        g.fill(sha);


        // 画出字符串
//        g.draw(sha);
//        g.setColor(Color.white);
//        g.fill(sha);

        g.dispose();
        // 输出png图片
        ImageIO.write(image, "png", outFile);
    }

}
