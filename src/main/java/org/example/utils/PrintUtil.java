package org.example.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.logging.log4j.util.Strings;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPrintable;
import org.apache.pdfbox.printing.Scaling;
import org.springframework.stereotype.Component;

import javax.print.PrintService;
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

    public String print(byte[] bytes, String printName, JSONObject jsonObject, Integer paperNumber) throws Exception {
        try {
            InputStream inputStream = new ByteArrayInputStream(bytes);
            PDDocument pdDocument = PDDocument.load(inputStream);
            PrinterJob printerJob = PrinterJob.getPrinterJob();
            if (!Strings.isEmpty(printName)) {
                PrintService printService = pickPrinter(printName);
                if (Objects.isNull(printService)) {
                    return "打印失败，未找到名称为" + printName + "的打印机，请检查";
                }
            } else {
                return "打印机名称不可为空";
            }

            PDFPrintable pdfPrintable = new PDFPrintable(pdDocument, Scaling.SHRINK_TO_FIT);
            Book book = new Book();
            PageFormat pageFormat = new PageFormat();
            if (Objects.nonNull(jsonObject)) {
                setPageFormat(pageFormat, jsonObject);
            }
            setPageFormat(pageFormat);
            book.append(pdfPrintable, pageFormat, pdDocument.getNumberOfPages());//打印页数
            printerJob.setPageable(book);
            printerJob.setCopies(Objects.nonNull(paperNumber) && paperNumber > 0 ? paperNumber : 1);//份数
            HashPrintRequestAttributeSet pars = new HashPrintRequestAttributeSet();
            pars.add(Sides.ONE_SIDED);//单双页设置
            printerJob.print(pars);
            return "0";
        } catch (Exception e) {
            e.printStackTrace();
            return "打印失败";
        }
    }

    /**
     * 纸张默认设置
     */
    private void setPageFormat(PageFormat pageFormat) {
        pageFormat.setOrientation(PageFormat.PORTRAIT);//纵向
        Paper paper = new Paper();
        paper.setSize(595, 842);
        paper.setImageableArea(10, 10, 575, 822);
        pageFormat.setPaper(paper);//设置纸张属性
    }

    /**
     * 纸张自定义设置
     *
     * @param jsonObject 设置参数
     *                   {
     *                   "paperOrientation": 1,
     *                   "width" : 595,
     *                   "height" : 842,
     *                   "x" : 10,
     *                   "y" : 10,
     *                   "allowPrintWidth" : 575,
     *                   "allowPrintHeight" : 822
     *                   }
     */
    private void setPageFormat(PageFormat pageFormat, JSONObject jsonObject) {
        pageFormat.setOrientation(jsonObject.containsKey("paperOrientation") ? jsonObject.getInteger("paperOrientation") : 1);//纵向
        Paper paper = new Paper();
        Double width = jsonObject.containsKey("width") ? jsonObject.getDouble("width") : 595;
        Double height = jsonObject.containsKey("height") ? jsonObject.getDouble("height") : 842;
        Double x = jsonObject.containsKey("x") ? jsonObject.getDouble("x") : 10;
        Double y = jsonObject.containsKey("y") ? jsonObject.getDouble("y") : 10;
        Double allowPrintWidth = jsonObject.containsKey("allowPrintWidth") ? jsonObject.getDouble("allowPrintWidth") : 575;
        Double allowPrintHeight = jsonObject.containsKey("allowPrintHeight") ? jsonObject.getDouble("allowPrintHeight") : 822;
        paper.setSize(width, height);
        paper.setImageableArea(x, y, allowPrintWidth, allowPrintHeight);
        pageFormat.setPaper(paper);//设置纸张属性
    }

    /**
     * 通过本机连接的打印机名称进行打印机匹配
     *
     * @param printerName
     * @return
     */
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

}
