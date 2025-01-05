package org.example.bean;

import com.alibaba.fastjson.JSONObject;
import org.example.base.BaseRender;
import org.example.utils.BarcodeGenerator;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

@Component("order_qrcode.xlsx")
public class OrderQRCode extends BaseRender {

    @Resource
    private BarcodeGenerator barcodeGenerator;

    @Override
    public List<JSONObject> renderItemsObj(List<JSONObject> itemsObj) {
        try {
            ArrayList<JSONObject> items = new ArrayList<>();
            for (JSONObject jsonObject : itemsObj) {
                JSONObject renderJsonObject = new JSONObject();
                //renderJsonObject.put("itemNumber", jsonObject.getString("itemNumber"));
                //byte[] bytes = barcodeGenerator.generateBarcodeImage(jsonObject.getString("itemNumber"), 300, 100);
                //byte[] bytes = barcodeGenerator.code128(jsonObject.getString("itemNumber"), 250, 150, 40, 24);
                byte[] bytes = barcodeGenerator.generateQRCodeImage(jsonObject.getString("itemNumber"), 300, 100);
                renderJsonObject.put("image", bytes);
                renderJsonObject.put("itemDes", jsonObject.get("itemDes"));
                renderJsonObject.put("quantity", jsonObject.get("quantity"));
                renderJsonObject.put("uom", jsonObject.get("uom"));
                renderJsonObject.put("subQuantity", jsonObject.get("subQuantity"));
                renderJsonObject.put("subUom", jsonObject.get("subUom"));
                renderJsonObject.put("location", jsonObject.get("location"));
                items.add(renderJsonObject);
            }
            return items;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Map<String, Object> renderOtherParams(Map<String, Object> otherParams) {
        try {
            Map<String, Object> renderOtherMap = new HashMap<>();
            renderOtherMap.put("orderNumber", otherParams.get("orderNumber"));
            renderOtherMap.put("customerName", otherParams.get("customerName"));
            renderOtherMap.put("remarks", otherParams.get("remarks"));
            //byte[] bytes = barcodeGenerator.code128((String) otherParams.get("orderNumber"), 250, 150, 40, 24);
            //byte[] bytes = barcodeGenerator.generateBarcodeImage((String) otherParams.get("orderNumber"), 300, 100);
            byte[] bytes = barcodeGenerator.generateQRCodeImage((String) otherParams.get("orderNumber"), 300, 100);
            renderOtherMap.put("image.QRCode", bytes);
            return renderOtherMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
