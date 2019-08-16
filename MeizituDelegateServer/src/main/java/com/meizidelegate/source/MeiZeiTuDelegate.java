package com.meizidelegate.source;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.meizidelegate.source.model.BiaoqingItem;
import com.meizidelegate.source.model.LastException;
import com.meizidelegate.source.model.MeiziListItem;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

@RestController
public class MeiZeiTuDelegate {

    @RequestMapping(value = "/uploadException", method = RequestMethod.POST)
    public void getExceptionFromiOS(@RequestBody LastException lastException) {

        System.out.println(lastException);
    }

    @RequestMapping(value = "/getOpenID")
    public String getOpenId(@RequestParam String appID, @RequestParam String secret, @RequestParam String code, HttpServletResponse response) {
        HttpMethod method =HttpMethod.GET;
        MultiValueMap<String, String> params= new LinkedMultiValueMap<String, String>();
        try {
            String methodUrl = "https://api.weixin.qq.com/sns/jscode2session?appid=" + appID + "&secret=" + secret + "&js_code=" + code + "&grant_type=authorization_code";
            String result = httpRestClient(methodUrl,method,params);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getWeiChatOpenId(String url, HttpMethod method, MultiValueMap<String, String> params) throws IOException {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(10 * 1000);
        requestFactory.setReadTimeout(10 * 1000);
        RestTemplate client = new RestTemplate(requestFactory);
        HttpHeaders headers = new HttpHeaders();
        //  请勿轻易改变此提交方式，大部分的情况下，提交方式都是表单提交
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(params, headers);
        //  执行HTTP请求
        ResponseEntity<String> response = client.exchange(url, method, requestEntity, String.class);
        return response.getBody();
    }

    @RequestMapping(value = "/getMeiziImage")
    public void getImage(@RequestParam String originUrl, HttpServletResponse response) {

        OutputStream outputStream = null;
        InputStream inputStream = null;
        try {
            URL url = new URL(originUrl);
            URLConnection connection = url.openConnection();
            inputStream = connection.getInputStream();

            response.setContentType("image/*");
            outputStream = response.getOutputStream();
            int len = 0;
            byte[] buff = new byte[1024];
            while ((len = inputStream.read(buff)) != -1) {
                outputStream.write(buff,0,len);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @RequestMapping(value = "/getMeiziImageList", method = RequestMethod.GET)
    public String hello(String methodUrl) {

        HttpMethod method =HttpMethod.GET;
        MultiValueMap<String, String> params= new LinkedMultiValueMap<String, String>();
        try {
            String result = httpRestClient(methodUrl,method,params);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }
    }

    public static String httpRestClient(String url, HttpMethod method, MultiValueMap<String, String> params) throws IOException {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(10 * 1000);
        requestFactory.setReadTimeout(10 * 1000);
        RestTemplate client = new RestTemplate(requestFactory);
        HttpHeaders headers = new HttpHeaders();
        //  请勿轻易改变此提交方式，大部分的情况下，提交方式都是表单提交
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set(HttpHeaders.REFERER, "https://app.mmzztt.com");
        headers.set(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Linux; Android 7.1.1; OS105 Build/NGI77B; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/62.0.3202.84 Mobile Safari/537.36");
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(params, headers);
        //  执行HTTP请求
        ResponseEntity<String> response = client.exchange(url, method, requestEntity, String.class);
        return response.getBody();
    }
}
