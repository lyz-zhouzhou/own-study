package org.example.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPrintable;
import org.apache.pdfbox.printing.Scaling;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.print.PrintService;
import javax.print.attribute.HashPrintJobAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.standard.Sides;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterJob;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Objects;

@Component
public class PrintUtil {

    public String print(byte[] bytes, String printName, JSONObject jsonObject) throws Exception {
        try {
            InputStream inputStream = new ByteArrayInputStream(bytes);
            PDDocument pdDocument = PDDocument.load(inputStream);
            PrinterJob printerJob = PrinterJob.getPrinterJob();
            if (Objects.nonNull(jsonObject)) {
                String printerName = jsonObject.getString("printerName");
                PrintService printService = pickPrinter(printerName);
                if (Objects.isNull(printService)) {
                    return "打印失败，未找到名称为" + printerName + "的打印机，请检查";
                }
            }
            PDFPrintable pdfPrintable = new PDFPrintable(pdDocument, Scaling.SHRINK_TO_FIT);
            Book book = new Book();
            PageFormat pageFormat = new PageFormat();
            pageFormat.setOrientation(PageFormat.PORTRAIT);//纵向
            pageFormat.setPaper(setPageSize(jsonObject));//设置纸张
            book.append(pdfPrintable, pageFormat, pdDocument.getNumberOfPages());
            printerJob.setPageable(book);
            printerJob.setCopies(1);//份数
            HashPrintRequestAttributeSet pars = new HashPrintRequestAttributeSet();
            pars.add(Sides.ONE_SIDED);//单双页设置
            printerJob.print(pars);
            return "0";
        } catch (Exception e) {
            e.printStackTrace();
            return "打印失败";
        }
    }

    private PrintService pickPrinter(String printerName) {
        PrintService[] printServices = PrinterJob.lookupPrintServices();
        if (printServices == null || printServices.length == 0) {
            return null;
        }
        PrintService printService = null;
        for (int i = 0; i < printServices.length; i++) {
            if (printServices[i].getName().contains(printerName)) {
                printService = printServices[i];
                break;
            }
        }
        return printService;
    }

    /**
     * 设置打印纸张
     *
     * @param jsonObject
     * @return
     */
    private Paper setPageSize(JSONObject jsonObject) {
        Paper paper = new Paper();
        //纸张默认设置
        if (Objects.isNull(jsonObject)) {
            paper.setSize(595, 842);
            paper.setImageableArea(10, 10, 575, 822);
        }
        Double width = jsonObject.containsKey("width") ? jsonObject.getDouble("width") : 595;
        Double height = jsonObject.containsKey("height") ? jsonObject.getDouble("height") : 842;
        Double x = jsonObject.containsKey("x") ? jsonObject.getDouble("x") : 10;
        Double y = jsonObject.containsKey("y") ? jsonObject.getDouble("y") : 10;
        Double allowPrintWidth = jsonObject.containsKey("allowPrintWidth") ? jsonObject.getDouble("allowPrintWidth") : 575;
        Double allowPrintHeight = jsonObject.containsKey("allowPrintHeight") ? jsonObject.getDouble("allowPrintHeight") : 822;

        paper.setSize(width, height);
        paper.setImageableArea(x, y, allowPrintWidth, allowPrintHeight);

        return paper;
    }

}
