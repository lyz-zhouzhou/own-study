package org.example.controller;

import com.google.zxing.WriterException;
import org.example.service.TestService;
import org.example.utils.BarcodeGenerator;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/print")
public class TestController {
    private static final String TEMPLATE_DIR = "src/main/resources/excelTemplate";
    @Resource
    private TestService testService;
    @Resource
    private BarcodeGenerator barcodeGenerator;

    @GetMapping("/test")
    public ResponseEntity<Void> test(HttpServletResponse response) throws IOException, WriterException {
        // 生成条形码图片
        String barcodeText = "test";
        byte[] barcodeBytes = barcodeGenerator.generateBarcodeImage(barcodeText, 300, 100);

        // 设置响应头
        response.setContentType(MediaType.IMAGE_PNG_VALUE); // 设置 MIME 类型为 image/png
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=barcode.png"); // 设置下载文件名

        // 将字节数组写入响应输出流
        try (OutputStream outputStream = response.getOutputStream()) {
            outputStream.write(barcodeBytes);
            outputStream.flush();
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/pdf")
    public ResponseEntity<Void> exportToPdf(HttpServletResponse response) throws IOException {
        try {
            testService.printTest("order_qrcode.xlsx",response);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
    /**
     * 下载模板文件
     *
     * @param templateName 模板名称（如 "order_template.xlsx"）
     * @return 文件流
     */
    @GetMapping("/download/{templateName}")
    public ResponseEntity<byte[]> downloadTemplate(@PathVariable String templateName) {
        try {
            // 加载模板文件
            ClassPathResource resource = new ClassPathResource("excelTemplate/" + templateName);
            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }

            // 读取文件内容
            byte[] fileBytes;
            try (InputStream inputStream = resource.getInputStream()) {
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                int nRead;
                byte[] data = new byte[1024]; // 缓冲区大小
                while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                    buffer.write(data, 0, nRead);
                }
                buffer.flush();
                fileBytes = buffer.toByteArray();
            }

            // 返回文件
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + templateName)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(fileBytes);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 更新模板文件
     *
     * @param templateName 模板名称（如 "order_template.xlsx"）
     * @param file         上传的文件
     * @return 更新结果
     */
    @PostMapping("/update/{templateName}")
    public ResponseEntity<String> updateTemplate(
            @PathVariable String templateName,
            @RequestParam("file") MultipartFile file) {
        try {
            // 检查文件是否为空
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("文件不能为空");
            }

            // 创建模板目录（如果不存在）
            File dir = new File(TEMPLATE_DIR);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // 保存文件
            Path filePath = Paths.get(TEMPLATE_DIR, templateName);
            Files.write(filePath, file.getBytes());

            return ResponseEntity.ok("模板更新成功: " + templateName);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("模板更新失败: " + e.getMessage());
        }
    }
}


