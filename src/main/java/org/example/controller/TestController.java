package org.example.controller;


import com.alibaba.fastjson.JSONObject;
import org.example.utils.CommonUtil;
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
            Resource resource = resourceLoader.getResource("classpath:excelTemplate/demoexcel.xlsx");
            InputStream inputStream = resource.getInputStream();

            // 准备数据
            Map<String, Object> data = new HashMap<>();
            data.put("order_number", "SO2000");
            data.put("customer_name", "客户");
            data.put("remarks", "备注");
            Context context = new Context(data);

            List<JSONObject> items = new ArrayList<>();
            // 第一条明细数据
            JSONObject itemDateO = new JSONObject();
            itemDateO.put("item_number", "item A");
            itemDateO.put("item_des", "A 描述");
            itemDateO.put("quantity", "5");
            itemDateO.put("uom", "箱");
            itemDateO.put("sub_quantity", "500.00");
            itemDateO.put("sub_uom", "包");
            itemDateO.put("location", "locationA");
            items.add(itemDateO);
            // 第二条明细数据
            JSONObject itemDateT = new JSONObject();
            itemDateO.put("itemNumber", "item A");
            itemDateO.put("itemDes", "A 描述");
            itemDateO.put("quantity", "5");
            itemDateO.put("uom", "箱");
            itemDateO.put("subQuantity", "500.00");
            itemDateO.put("subUom", "包");
            itemDateO.put("location", "locationA");
            items.add(itemDateT);
            context.putVar("items", items);


            // 调用 CommonUtil 生成 PDF
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            CommonUtil.generateStream(response, "template.xlsx", inputStream, outputStream, context);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}

