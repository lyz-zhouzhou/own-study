package org.example.service;

import javax.servlet.http.HttpServletResponse;

public interface TestService {
    String print(String templateName);

    String printDownload(String templateName,HttpServletResponse response);
}
