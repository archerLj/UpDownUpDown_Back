package com.meizidelegate.source;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.meizidelegate.source.model.JiePai;
import com.meizidelegate.source.model.Music;
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
import java.util.HashMap;
import java.util.List;

/*
*
* 下载喜马拉雅音乐
* */
public class DownloadMusic {

    public void download(int pageId) {

        String url = "https://mobile.ximalaya.com/mobile/v1/album/track?albumId=18986420&pageId=" + pageId + "&pageSize=20&isAsc=true";


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

        HashMap<String, Object> resultMap = JSON.parseObject(result, HashMap.class);
        HashMap<String, Object> listMap = JSON.parseObject(JSON.toJSONString(resultMap.get("data")), HashMap.class);


        // json解析
        List<Music> meiziItems = JSONObject.parseArray(JSON.toJSONString(listMap.get("list")), Music.class);

        if (meiziItems != null && meiziItems.size() > 0) {
            for (int i = 0; i < meiziItems.size(); i++) {
                Music item = meiziItems.get(i);
                downloadImg(item.getPlayPathHq(), item.getTitle());
            }
            download(pageId + 1);
        } else {
            System.out.println("=================");
            System.out.println("End");
            System.out.println("=================");
        }

    }

    public void downloadImg(String musicUrl, String musicName) {

        FileOutputStream outputStream = null;
        InputStream inputStream = null;
        try {
            URL url = new URL(musicUrl);
            URLConnection connection = url.openConnection();
            inputStream = connection.getInputStream();

            File path = new File(ResourceUtils.getURL("classpath:").getPath());
            File imagesFolder = new File(path.getAbsolutePath(), "static/musics/");
            if (!imagesFolder.exists()) {
                imagesFolder.mkdirs();
            }

            File imageSaveFile = new File(imagesFolder + "/" + musicName + ".m4a");

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
