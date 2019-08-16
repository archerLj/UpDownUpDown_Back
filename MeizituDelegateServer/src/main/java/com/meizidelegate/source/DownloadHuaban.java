package com.meizidelegate.source;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.meizidelegate.source.model.HuabanImage;
import com.meizidelegate.source.model.JiePai;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DownloadHuaban {

    List<String> boradIDs = new ArrayList<>();
    int imgCount = 1;
    int folder = 1;

    public DownloadHuaban() {

        boradIDs.add("487358");
        boradIDs.add("24329051");
        boradIDs.add("30095371");
        boradIDs.add("3418371");
        boradIDs.add("30324099");
        boradIDs.add("27831946");
        boradIDs.add("2443219");
        boradIDs.add("18976743");
        boradIDs.add("19029229");
        boradIDs.add("17462167");
        boradIDs.add("16078717");
        boradIDs.add("6730637");
        boradIDs.add("481662");
        boradIDs.add("13910871");
        boradIDs.add("16347552");
        boradIDs.add("19403052");
        boradIDs.add("18850117");
        boradIDs.add("47823395");
        boradIDs.add("18273078");
        boradIDs.add("52334297");
        boradIDs.add("19626708");
        boradIDs.add("3783723");
        boradIDs.add("48488461");
        boradIDs.add("17407784");
        boradIDs.add("52283153");
        boradIDs.add("13641647");
    }

    /**
     * 抓取所有的妹子图
     * */
    public void download(int boardIdIndex, String lastPinId) {

        String url;
        if (lastPinId == null) {
            url = "https://api.huaban.com/boards/" + this.boradIDs.get(boardIdIndex) + "/pins?limit=20";
        } else {
            url = "https://api.huaban.com/boards/" + this.boradIDs.get(boardIdIndex) + "/pins?max=" + lastPinId + "&limit=20";
        }


        MultiValueMap<String, String> params= new LinkedMultiValueMap<String, String>();

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(10 * 1000);
        requestFactory.setReadTimeout(10 * 1000);
        RestTemplate client = new RestTemplate(requestFactory);
        HttpHeaders headers = new HttpHeaders();
        //  请勿轻易改变此提交方式，大部分的情况下，提交方式都是表单提交
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(params, headers);
        //  执行HTTP请求
        ResponseEntity<String> response = client.exchange(url, HttpMethod.GET, requestEntity, String.class);
        String result = response.getBody();

        HashMap<String, Object> resultMap = JSON.parseObject(result, HashMap.class);
        // json解析
        List<HuabanImage> huabanImages = JSONObject.parseArray(JSON.toJSONString(resultMap.get("pins")), HuabanImage.class);

        String lastPinID = null;
        if (huabanImages != null && huabanImages.size() > 0) {
            for (int i = 0; i < huabanImages.size(); i++) {
                HuabanImage item = huabanImages.get(i);
                lastPinID = item.getPin_id();
                String imgUrl = "https://hbimg.huabanimg.com//" + item.getFile().getKey() + "_/fw/480";

                if (imgCount > 60) {
                    folder = folder + 1;
                    imgCount = 1;
                }
                downloadImg(imgUrl, String.valueOf(folder) + "_" + String.valueOf(imgCount), String.valueOf(folder));
                imgCount = imgCount + 1;
            }

            download(boardIdIndex, lastPinID);
        } else {
            if (boardIdIndex < (boradIDs.size() - 1)) {
                download(boardIdIndex + 1, null);
            } else {
                System.out.println("===================");
                System.out.println("====      End    ===");
                System.out.println("===================");
            }
        }

    }

    public void downloadImg(String imgUrl, String imgName, String folderName) {

        FileOutputStream outputStream = null;
        InputStream inputStream = null;
        try {
            URL url = new URL(imgUrl);
            URLConnection connection = url.openConnection();
            inputStream = connection.getInputStream();

            File path = new File(ResourceUtils.getURL("classpath:").getPath());
            File imagesFolder = new File(path.getAbsolutePath(), "static/images/" + folderName);
            if (!imagesFolder.exists()) {
                imagesFolder.mkdirs();
            }

            File imageSaveFile = new File(imagesFolder + "/" + imgName + ".jpg");

            if (!imageSaveFile.isFile()) {
                imageSaveFile.createNewFile();
            }

            outputStream = new FileOutputStream(imageSaveFile);

            int index;
            byte[] bytes = new byte[1024];
            outputStream = new FileOutputStream(imageSaveFile);
            while ((index = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, index);
                outputStream.flush();
            }

            inputStream.close();
            outputStream.close();

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
}
