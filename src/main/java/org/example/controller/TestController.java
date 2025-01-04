package org.example.controller;


import com.alibaba.fastjson.JSONObject;
import org.example.utils.FileUtil;
import org.jxls.common.Context;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/print")
public class TestController {

    @javax.annotation.Resource
    private ResourceLoader resourceLoader;

    @GetMapping("/pdf")
    public ResponseEntity<Void> exportToPdf(HttpServletResponse response) {
        try {
            // 加载 Excel 模板
            Resource resource = resourceLoader.getResource("classpath:excelTemplate/order.xlsx");
            InputStream inputStream = resource.getInputStream();

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}

