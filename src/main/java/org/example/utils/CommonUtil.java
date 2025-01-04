package org.example.utils;

import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.internal.Engine;
import org.jxls.common.Context;
import org.jxls.expression.JexlExpressionEvaluator;
import org.jxls.transform.Transformer;
import org.jxls.util.JxlsHelper;
import org.jxls.util.TransformerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

public class CommonUtil {

    public static byte[] generateStream(HttpServletResponse response, String excelTemplate, InputStream inputStream, ByteArrayOutputStream outputStream,
                                        Context context) throws IOException {
        //转成excel 输出流
        handleInputStream(inputStream, outputStream, context);
        byte[] buf = outputStream.toByteArray();
        //默认就是excel 类型
        String fileName = generateFileName(excelTemplate);
        //获取字节数组
        buf = generateByteBuf(buf, outputStream);
        //设置响应头
        setResponseHeader(response, URLEncoder.encode(fileName, "UTF-8"), buf);
        return buf;
    }

    public static byte[] generateStream(String excelTemplate, InputStream inputStream, ByteArrayOutputStream outputStream,
                                        Context context) throws IOException {
        //转成excel 输出流
        handleInputStream(inputStream, outputStream, context);
        byte[] buf = outputStream.toByteArray();
        //默认就是excel 类型
        String fileName = generateFileName(excelTemplate);
        //获取字节数组
        buf = generateByteBuf(buf, outputStream);
        return buf;
    }

    public static void handleInputStream(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {

        JxlsHelper jxlsHelper = JxlsHelper.getInstance();
        Transformer transformer = TransformerFactory.createTransformer(inputStream, outputStream);
        //默认是打开时渲染
        transformer.setFullFormulaRecalculationOnOpening(true);
        //获得配置
        JexlExpressionEvaluator evaluator = (JexlExpressionEvaluator) transformer.getTransformationConfig().getExpressionEvaluator();
        JexlBuilder jexlBuilder = new JexlBuilder();
        JexlEngine engine = new Engine(jexlBuilder);
        evaluator.setJexlEngine(engine);
        jxlsHelper.processTemplate(context, transformer);
    }

    private static String generateFileName(String excelTemplate) {
        int i = excelTemplate.lastIndexOf(".");
        String tmpName = excelTemplate.substring(0, i);
        return tmpName + ".pdf";
    }

    private static byte[] generateByteBuf(byte[] bytes, ByteArrayOutputStream outputStream) {
        ByteArrayInputStream byteArrayInputStream = null;
        //判断输出文件 类型

        byteArrayInputStream = new ByteArrayInputStream(bytes);
        outputStream.reset();
        PdfUtil.excelToPDF(byteArrayInputStream, outputStream);
        bytes = outputStream.toByteArray();

        closeStream(byteArrayInputStream);
        return bytes;
    }

    private static void setResponseHeader(HttpServletResponse response, String fileName, byte[] buf) throws IOException {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        RequestContextHolder.setRequestAttributes(sra, true);
        response.reset();
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ";" + "filename*=utf-8''" + fileName);
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-store");
        response.setContentLength(buf.length);
        response.getOutputStream().write(buf);
    }

    //关流
    private static void closeStream(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
