package org.example.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.itextpdf.text.pdf.Barcode128;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;

@Component
public class BarcodeGenerator {
    /**
     * 生成条形码图片
     *
     * @param barcodeText 条形码内容
     * @param width       图片宽度
     * @param height      图片高度
     * @return 条形码图片的字节数组
     */
    public byte[] generateBarcodeImage(String barcodeText, int width, int height) throws WriterException, IOException {
        // 使用 Code128Writer 生成条形码
        Code128Writer barcodeWriter = new Code128Writer();
        BitMatrix bitMatrix = barcodeWriter.encode(barcodeText, BarcodeFormat.CODE_128, width, height);

        // 将条形码图片转换为字节数组
        BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", baos);
        return baos.toByteArray();
    }

    /**
     * 生成条形码
     *
     * @param barCodeInfo   条形码内容
     * @param barCodeWidth  条形码宽度（像素）
     * @param barCodeHeight 条形码高度（像素）
     * @param heightSpace   条形码下方留白高度（像素）
     * @param fontSize      文本字体大小
     * @return 条形码图片的字节数组
     */
    public byte[] code128(String barCodeInfo, int barCodeWidth, int barCodeHeight, int heightSpace, int fontSize) throws IOException {
        // 计算画布大小
        int imageWidth = barCodeWidth;
        int imageHeight = barCodeHeight + heightSpace;

        // 创建画布
        BufferedImage img = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = (Graphics2D) img.getGraphics();

        // 设置背景颜色
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, imageWidth, imageHeight);

        // 生成条形码
        Barcode128 barcode128 = new Barcode128();
        barcode128.setCode(barCodeInfo);
        Image codeImg = barcode128.createAwtImage(Color.BLACK, Color.WHITE);

        // 绘制条形码
        int startX = 0;
        int imageStartY = 0;
        g.drawImage(codeImg, startX, imageStartY, barCodeWidth, barCodeHeight, Color.WHITE, null);

        // 绘制文本
        Font font = new Font("", Font.PLAIN, fontSize);
        FontRenderContext fontRenderContext = g.getFontRenderContext();
        int codeWidth = (int) font.getStringBounds(barCodeInfo, fontRenderContext).getWidth();
        int stringStartY = imageHeight - 8; // 文本的起始 Y 坐标

        AttributedString ats = new AttributedString(barCodeInfo);
        ats.addAttribute(TextAttribute.FONT, font, 0, barCodeInfo.length());
        AttributedCharacterIterator iter = ats.getIterator();

        g.setColor(Color.BLACK);
        g.drawString(iter, startX + (barCodeWidth - codeWidth) / 2, stringStartY); // 居中显示文本

        // 释放资源
        g.dispose();

        // 保存为字节数组
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            ImageIO.write(img, "png", os);
            return os.toByteArray();
        }
    }
}