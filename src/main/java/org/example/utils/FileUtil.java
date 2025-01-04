package org.example.utils;

import org.jxls.common.Context;
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

public class FileUtil {

    /**
     * 页面可下载打印文件方法
     * @param response 响应体
     * @param excelTemplate 模板名称
     * @param inputStream 输入流
     * @param outputStream 输出流
     * @param context 模板数据容器
     * @return 需要打印的文件字节流
     * @throws IOException io异常
     */
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

    /**
     * 页面不可下载打印文件方法
     * @param excelTemplate 模板名称
     * @param inputStream 输入流
     * @param outputStream 输出流
     * @param context 模板数据容器
     * @return 需要打印的文件字节流
     * @throws IOException io异常
     */
    public static byte[] generateStream(String excelTemplate, InputStream inputStream, ByteArrayOutputStream outputStream,
                                        Context context) throws IOException {
        //转成excel 输出流
        handleInputStream(inputStream, outputStream, context);
        byte[] buf = outputStream.toByteArray();
        //默认就是pdf类型
        generateFileName(excelTemplate);
        //获取字节数组
        buf = generateByteBuf(buf, outputStream);
        return buf;
    }

    /**
     * 数据绑定
     * @param inputStream 输入流(模板excel)
     * @param outputStream 输出流(数据绑定后的excel)
     * @param context excel数据容器
     * @throws IOException io异常
     */
    public static void handleInputStream(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        JxlsHelper jxlsHelper = JxlsHelper.getInstance();
        Transformer transformer = TransformerFactory.createTransformer(inputStream, outputStream);
        jxlsHelper.processTemplate(context, transformer);
    }

    /**
     * 获取文件名称
     * @param excelTemplate 模板名称
     * @return 文件最终名称
     */
    private static String generateFileName(String excelTemplate) {
        int i = excelTemplate.lastIndexOf(".");
        String tmpName = excelTemplate.substring(0, i);
        return tmpName + ".pdf";
    }

    /**
     * excel文件转pdf文件
     * @param bytes excel文件字节流
     * @param outputStream
     * @return
     */
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
