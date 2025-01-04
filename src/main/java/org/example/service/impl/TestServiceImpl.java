package org.example.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.MapUtils;
import org.apache.logging.log4j.util.Strings;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.example.base.BaseRender;
import org.example.service.TestService;
import org.example.utils.FileUtil;
import org.example.utils.PrintUtil;
import org.example.utils.SpringContextHolder;
import org.jxls.common.Context;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.*;

@Service
public class TestServiceImpl implements TestService {
    @Resource
    private PrintUtil printUtil;

    @Override
    public String printTest(String templateName, HttpServletResponse response) {
        try {
            //根据模板名称寻找是否存在此模板
            if (!isTemplateExist(templateName)) {
                return "打印模板不存在";
            }
            InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("excelTemplate/" + templateName);
            //调用外部接口获得模板数据(如何判断获取哪个模板的数据)
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
            // 渲染数据
            Map<String, BaseRender> beansOfType = SpringContextHolder.getContext().getBeansOfType(BaseRender.class);
            if (MapUtils.isNotEmpty(beansOfType) && beansOfType.containsKey(templateName)) {
                BaseRender baseRender = beansOfType.get(templateName);
                baseRender.beforeRender(items, data);
                List<JSONObject> itemsObj = baseRender.getItemsObj();
                Map<String, Object> otherParams = baseRender.getOtherParams();
                //处理数据
                Context context = new Context(otherParams);
                context.putVar("items", itemsObj);
                //获得可打印文件字节流
                byte[] bytes = FileUtil.generateStream(response, templateName, resourceAsStream, new ByteArrayOutputStream(), context);
                //printUtil.print(bytes, "AA", null);

            } else {
                return "未获取到渲染器";
            }
            return "打印成功";
        } catch (Exception e) {
            e.printStackTrace();
            return "打印失败";
        }
    }

    /**
     * 标准打印
     *
     * @param templateName
     * @param printName
     * @return
     */
    @Override
    public String print(String templateName, String printName) {
        try {
            //根据模板名称寻找是否存在此模板
            if (!isTemplateExist(templateName)) {
                return "打印模板不存在";
            }
            if (Strings.isEmpty(printName)) {
                return "未指定打印机";
            }
            //获取模板
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
            printUtil.print(bytes, printName, null, null);
            return "打印成功";
        } catch (Exception e) {
            e.printStackTrace();
            return "打印失败";
        }
    }

    /**
     * 自定义打印
     *
     * @param templateName 模板名称
     * @param printName    打印机名称
     * @param jsonObject   纸张属性
     */
    @Override
    public String printCustomize(String templateName, String printName, JSONObject jsonObject, Integer paperNumber) {
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
            byte[] bytes = FileUtil.generateStream(templateName, resourceAsStream, new ByteArrayOutputStream(), context);
            //获得打印参数(纸张设置、打印机名称)
            //调用打印方法进行打印
            printUtil.print(bytes, printName, jsonObject, paperNumber);
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
