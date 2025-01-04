package org.example.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.example.service.TestService;
import org.example.utils.FileUtil;
import org.jxls.common.Context;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TestServiceImpl implements TestService {
    @Override
    public String print(String templateName) {
        try {
            //根据模板名称寻找是否存在此模板
            if (!isTemplateExist(templateName)) {
                return "打印模板不存在";
            }
            InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("excelTemplate/" + templateName);
            //调用外部接口获得模板数据(如何判断获取哪个模板的数据)
            // 准备数据
            Map<String, Object> data = new HashMap<>();
            data.put("orderNumber", "SO2000");
            data.put("customerName", "客户");
            data.put("remarks", "备注");

            List<JSONObject> items = new ArrayList<>();
            // 第一条明细数据
            JSONObject one = new JSONObject();
            one.put("itemNumber", "item A");
            one.put("itemDes", "A 描述");
            one.put("quantity", "5");
            one.put("uom", "箱");
            one.put("subQuantity", "500.00");
            one.put("subUom", "包");
            one.put("location", "locationA");
            items.add(one);
            // 第二条明细数据
            JSONObject two = new JSONObject();
            two.put("itemNumber", "item A");
            two.put("itemDes", "A 描述");
            two.put("quantity", "5");
            two.put("uom", "箱");
            two.put("subQuantity", "500.00");
            two.put("subUom", "包");
            two.put("location", "locationA");
            items.add(two);

            //处理数据
            Context context = new Context(data);
            context.putVar("items", items);
            //获得可打印文件字节流
            byte[] bytes = FileUtil.generateStream(templateName, resourceAsStream, new ByteArrayOutputStream(), context);
            return "打印成功";
        } catch (Exception e) {
            e.printStackTrace();
            return "打印失败";
        }
    }

    @Override
    public String printDownload(String templateName, HttpServletResponse response) {
        try {
            //根据模板名称寻找是否存在此模板
            if (!isTemplateExist(templateName)) {
                return "模板不存在";
            }
            InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("excelTemplate/" + templateName);
            //调用外部接口获得模板数据(如何判断获取哪个模板的数据)
            // 准备数据
            Map<String, Object> data = new HashMap<>();
            data.put("orderNumber", "SO2000");
            data.put("customerName", "客户");
            data.put("remarks", "备注");

            List<JSONObject> items = new ArrayList<>();
            // 第一条明细数据
            JSONObject one = new JSONObject();
            one.put("itemNumber", "item A");
            one.put("itemDes", "A 描述");
            one.put("quantity", "5");
            one.put("uom", "箱");
            one.put("subQuantity", "500.00");
            one.put("subUom", "包");
            one.put("location", "locationA");
            items.add(one);
            // 第二条明细数据
            JSONObject two = new JSONObject();
            two.put("itemNumber", "item A");
            two.put("itemDes", "A 描述");
            two.put("quantity", "5");
            two.put("uom", "箱");
            two.put("subQuantity", "500.00");
            two.put("subUom", "包");
            two.put("location", "locationA");
            items.add(two);

            //处理数据
            Context context = new Context(data);
            context.putVar("items", items);
            //获得可打印文件字节流
            FileUtil.generateStream(response, templateName, resourceAsStream, new ByteArrayOutputStream(), context);
            return "打印成功";
        } catch (Exception e) {
            e.printStackTrace();
            return "打印失败";
        }
    }

    /**
     * 检查模板是否存在
     *
     * @param templateName 模板名称
     * @return 是否存在
     */
    private boolean isTemplateExist(String templateName) {
        ClassPathResource resource = new ClassPathResource("excelTemplate/" + templateName);
        return resource.exists();
    }
}
