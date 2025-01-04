package org.example.bean;

import com.alibaba.fastjson.JSONObject;
import com.google.zxing.WriterException;
import org.example.base.BaseRender;
import org.example.utils.BarcodeGenerator;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component(value = "order.xlsx")
public class Order extends BaseRender {
    @Resource
    private BarcodeGenerator barcodeGenerator;

    @Override
    public List<JSONObject> renderItemsObj(List<JSONObject> itemsObj) {
        ArrayList<JSONObject> items = new ArrayList<>();
        for (JSONObject jsonObject : itemsObj) {
            JSONObject renderJsonObject = new JSONObject();
            renderJsonObject.put("itemNumber", jsonObject.get("itemNumber"));
            renderJsonObject.put("itemDes", jsonObject.get("itemDes"));
            renderJsonObject.put("quantity", jsonObject.get("quantity"));
            renderJsonObject.put("uom", jsonObject.get("uom"));
            renderJsonObject.put("subQuantity", jsonObject.get("subQuantity"));
            renderJsonObject.put("subUom", jsonObject.get("subUom"));
            renderJsonObject.put("location", jsonObject.get("location"));
            items.add(renderJsonObject);
        }
        return items;
    }

    @Override
    public Map<String, Object> renderOtherParams(Map<String, Object> otherParams) {
        try {
            Map<String, Object> renderOtherMap = new HashMap<>();
            renderOtherMap.put("orderNumber", otherParams.get("orderNumber"));
            renderOtherMap.put("customerName", otherParams.get("customerName"));
            renderOtherMap.put("remarks", otherParams.get("remarks"));
            byte[] bytes = barcodeGenerator.generateBarcodeImage((String) otherParams.get("orderNumber"), 23, 1);
            otherParams.put("image.QRCode", bytes);
            return renderOtherMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
