package com.meizidelegate.source;

import com.alibaba.fastjson.JSON;
import com.meizidelegate.source.model.Yulu;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;

@RestController
public class DownloadYulu {

    public void getAllYulu(int page) {

        String url;
        if (page == 0) {
            url = "https://api.yulu.58is.cn/v3/feeds/lists/1005?page=0&base=&full=true&r=" + System.currentTimeMillis();
        } else {
            url = "https://api.yulu.58is.cn/v3/feeds/lists/1005?page=" + page + "&base=&full=false&r=" + System.currentTimeMillis();
        }

        MultiValueMap<String, String> params= new LinkedMultiValueMap<String, String>();

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(10 * 1000);
        requestFactory.setReadTimeout(10 * 1000);
        RestTemplate client = new RestTemplate(requestFactory);
        HttpHeaders headers = new HttpHeaders();
        //  请勿轻易改变此提交方式，大部分的情况下，提交方式都是表单提交
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//        headers.set(HttpHeaders.REFERER, "https://app.mmzztt.com");
//        headers.set(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Linux; Android 7.1.1; OS105 Build/NGI77B; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/62.0.3202.84 Mobile Safari/537.36");
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(params, headers);
        //  执行HTTP请求
        ResponseEntity<String> response = client.exchange(url, HttpMethod.GET, requestEntity, String.class);
        String result = response.getBody();

        HashMap<String , Object> resultMap = JSON.parseObject(result, HashMap.class);
        List<Yulu> yulus = JSON.parseArray(JSON.toJSONString(resultMap.get("data")), Yulu.class);

        if (yulus != null && yulus.size() > 0) {

            for (int i=0; i<yulus.size(); i++) {
                Yulu yulu = yulus.get(i);
                if (yulu.getId() != null && !yulu.getId().isEmpty()) {
                    downloadImg(yulu.getIcon(), yulu.getId());
                    writeYulus(yulu.getTitle(), yulu.getId());
                }
            }

            getAllYulu(page + 1);
        } else {
            System.out.println("=-==========================");
            System.out.println("没有了");
        }
    }

    public void writeYulus(String text, String id) {
        FileWriter fw = null;
        PrintWriter printWriter = null;
        try {
            File path = new File(ResourceUtils.getURL("classpath:").getPath());
            File yuluFile = new File(path.getAbsolutePath(), "static/yulu.txt");
            fw = new FileWriter(yuluFile, true);

            printWriter = new PrintWriter(fw);
            printWriter.println(id + ":");
            printWriter.println(text);
            printWriter.println("---------------------------------------------------");
            printWriter.flush();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fw.flush();
                printWriter.close();
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void downloadImg(String imgUrl, String imgName) {

        FileOutputStream outputStream = null;
        InputStream inputStream = null;
        try {
            URL url = new URL(imgUrl);
            URLConnection connection = url.openConnection();
            inputStream = connection.getInputStream();

            File path = new File(ResourceUtils.getURL("classpath:").getPath());
            File imagesFolder = new File(path.getAbsolutePath(), "static/images/");
            if (!imagesFolder.exists()) {
                imagesFolder.mkdirs();
            }

            File imageSaveFile = new File(imagesFolder + "/" + imgName + ".png");

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
