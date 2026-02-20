package org.bau.stdlib.pdf;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.font.FontRenderContext;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.text.AttributedString;
import java.util.HashMap;
import java.util.Map;

public class MeasureCharacterWidths {

    private final static int FONT_SIZE = 1000;
    public static void main(String[] args) {

        String fontName = "Helvetica";


        String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        System.out.println("Available fonts");
        for (String f : fonts) {
            System.out.print(f + ";");
        }
        System.out.println();

        Font font = new Font(fontName, Font.PLAIN, FONT_SIZE);
        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setFont(font);

        FontMetrics metrics = g2d.getFontMetrics();
        FontRenderContext frc = new FontRenderContext(null, true, true);

        System.out.println("Font: " + font.getFontName() + " size " + FONT_SIZE);
        System.out.print("private static final int[] WIDTHS = { ");
        int[] stdWidths = new int[256];
        for (int codePoint = 32; codePoint <= 255; codePoint++) {
            if (codePoint > 32) {
                System.out.print(", ");
            }
            int width = metrics.charWidth((char) codePoint);
            stdWidths[codePoint] = width;
            System.out.print(width);
        }
        System.out.println(" };");

        System.out.print("private static final int[] KERNING = { ");
        for (int codePoint = 32; codePoint <= 255; codePoint++) {
            if ((codePoint >= 0x7f && codePoint < 0xa0) || codePoint == 0xad) {
                continue;
            }
            for (int codePoint2 = 32; codePoint2 <= 255; codePoint2++) {
                if ((codePoint2 >= 0x7f && codePoint2 < 0xa0) || codePoint2 == 0xad) {
                    continue;
                }
                float f = charsWidth(frc, font, (char) codePoint, (char) codePoint2);
                int off = (int) (f - stdWidths[codePoint2] - stdWidths[codePoint]);
                if (off != 0) {
                    System.out.println((char) codePoint + "" + (char) codePoint2 + ":" + off);
                }
            }
        }
        System.out.println(" };");

        g2d.dispose();
    }

    private static float charsWidth(FontRenderContext frc, Font font, char... c) {

        Map<TextAttribute, Object> attributes = new HashMap<>();
        font = font.deriveFont(Map.of(TextAttribute.KERNING, TextAttribute.KERNING_ON));
        attributes.put(TextAttribute.FONT, font);
        attributes.put(TextAttribute.KERNING, TextAttribute.KERNING_ON);

        AttributedString as = new AttributedString(new String(c));
        as.addAttributes(attributes, 0, c.length);

        TextLayout layout = new TextLayout(as.getIterator(), frc);

        float advance = layout.getAdvance();

        return advance;

//        GlyphVector gv = font.createGlyphVector(frc, new String(c));
//        Point2D p0 = gv.getGlyphPosition(0);
//        Point2D p2 = gv.getGlyphPosition(2);
//        return (float) (p2.getX() - p0.getX());
    }

}
