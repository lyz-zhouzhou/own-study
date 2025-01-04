package org.example.utils;

import com.alibaba.fastjson.JSONObject;
import jdk.internal.dynalink.support.ClassMap;
import org.apache.commons.collections.MapUtils;
import org.example.base.BaseRenderer;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class RendererUtil {

    public static void Renderer(Class<?> templateClass, Map<String, BaseRenderer> baseRendererMap, String excelTemplateName, List<JSONObject> itemsObj, Map<String, Object> otherParams) {
        if (MapUtils.isNotEmpty(baseRendererMap) && baseRendererMap.containsKey(excelTemplateName)) {

            BaseRenderer baseRenderer = baseRendererMap.get(excelTemplateName);
            baseRenderer.beforeRenderer(itemsObj, otherParams);
        }
    }
}
