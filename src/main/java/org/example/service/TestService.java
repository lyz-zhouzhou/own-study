package org.example.service;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletResponse;

public interface TestService {
    String printTest(String templateName, HttpServletResponse response);

    String print(String templateName, String printName);

    String printCustomize(String templateName, String printName, JSONObject jsonObject);
}
