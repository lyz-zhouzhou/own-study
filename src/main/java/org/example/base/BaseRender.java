package org.example.base;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public abstract class BaseRender {
    private List<JSONObject> itemsObj;
    private Map<String,Object> otherParams;


    public List<JSONObject> getItemsObj() {
        return this.itemsObj;
    }

    public Map<String, Object> getOtherParams() {
        return this.otherParams;
    }

    public void beforeRender(List<JSONObject> itemsObj, Map<String, Object> otherParams) {
        this.itemsObj = itemsObj;
        this.otherParams = otherParams;

        this.itemsObj = renderItemsObj(itemsObj);
        this.otherParams = renderOtherParams(otherParams);

    }

    public abstract List<JSONObject> renderItemsObj(List<JSONObject> itemsObj) ;
    public abstract Map<String, Object> renderOtherParams(Map<String, Object> otherParams);
}
