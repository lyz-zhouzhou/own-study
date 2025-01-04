package org.example.utils;

import com.aspose.cells.*;
import com.itextpdf.text.DocumentException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class PdfUtil {

    static {
        try {
            InputStream is = PdfUtil.class.getClassLoader().getResourceAsStream("license.xml");
            License aposeLic = new License();
            aposeLic.setLicense(is);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void excelToPDF(InputStream inputStream, OutputStream outputStream) {
        try {
            Workbook workbook = new Workbook(inputStream);
            WorksheetCollection worksheets = workbook.getWorksheets();
            Worksheet worksheet = worksheets.get(0);
            PageSetup pageSetup = worksheet.getPageSetup();
            pageSetup.setPaperSize(PaperSizeType.PAPER_A_4);
            //水平居中
            pageSetup.setCenterHorizontally(true);
            //页面显示的缩放比例
            pageSetup.setZoom(93);
            PdfSaveOptions saveOptions = new PdfSaveOptions();
            // 设置导出 PDF 时的图像类型
            //saveOptions.setImageType(ImageFormat.getJpeg());
            //指定输出文档的 PDF 标准合规级别。
            //saveOptions.setCompliance(PdfCompliance.PDF_A_1_B);
            saveOptions.setAllColumnsInOnePagePerSheet(true);
            //默认值是true。禁用此属性可能会提供更好的性能。但是，当无法使用默认或指定的文本/字符字体来渲染时，
            //生成的pdf中可能会出现不可读的字符（例如块）。对于这种情况，用户应将此属性保留为 true，以便可以搜索替代字体并使用替代字体来呈现文本；
            saveOptions.setCheckFontCompatibility(false);
            workbook.save(outputStream, saveOptions);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

