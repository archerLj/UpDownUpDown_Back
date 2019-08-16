package com.meizidelegate.source;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

@RestController
public class ConvertTools {

    @RequestMapping(value = "/covertToUTF8", method = RequestMethod.GET)
    public void hello(String sourceHtml, HttpServletResponse response) {
        response.setContentType("test/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        try {
            PrintWriter out = response.getWriter();
            out.print(sourceHtml);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
