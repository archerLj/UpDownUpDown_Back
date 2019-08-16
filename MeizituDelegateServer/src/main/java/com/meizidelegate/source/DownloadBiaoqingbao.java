package com.meizidelegate.source;

import com.alibaba.fastjson.JSON;
import com.meizidelegate.source.model.BiaoqingItem;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RestController;
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

@RestController
public class DownloadBiaoqingbao {

    public void downloadAllBiaoqingPics(int page, int id) {
        String url = "http://meme.51biaoqing.com/meme-core/recommend/albumGroup/detail?page=" + page + "&size=18&type=2&id=" + id;
        MultiValueMap<String, String> params= new LinkedMultiValueMap<String, String>();

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(10 * 1000);
        requestFactory.setReadTimeout(10 * 1000);
        RestTemplate client = new RestTemplate(requestFactory);
        HttpHeaders headers = new HttpHeaders();
        //  请勿轻易改变此提交方式，大部分的情况下，提交方式都是表单提交
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        headers.set("User-Agent","Mozilla/5.0 (Linux; U; Android 7.1.1; zh-cn; OS105 Build/NGI77B) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30");
        headers.set("User-Agent-Platform","android");
        headers.set("User-Agent-Device-Type","OS105");
        headers.set("User-Agent-Device-Id","eb7a748504c86bd8");
        headers.set("User-Agent-Channel","55550003");
        headers.set("User-Agent-System-Version","7.1.1");
        headers.set("User-Agent-App-Version","3.2.0");
        headers.set("WeMeme-Timestamp","1563865458160");
        headers.set("QTech-App-Key","quanminbiaoqing");
        headers.set("WeMeme-Signature","5496e0a6fc2073d31f9f4e9d2740616f");


        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(params, headers);
        ResponseEntity<String> response = client.exchange(url, HttpMethod.GET, requestEntity, String.class);

        String result = response.getBody();
        HashMap<String, Object> resultMap = JSON.parseObject(result, HashMap.class);

        List<BiaoqingItem> items = JSON.parseArray(JSON.toJSONString(resultMap.get("data")), BiaoqingItem.class);
        if (items != null && items.size() > 0) {
            for (int i=0; i<items.size(); i++) {
                BiaoqingItem item = items.get(i);
                if (item.getCover() != null && item.getCover().getGif() != null) {
                    boolean isGif = item.getCover().getGif();
                    if (isGif) {
                        if (item.getCover().getGifUrl() != null) {
                            downloadGif(String.valueOf(id), item.getCover().getGifUrl(), true, String.valueOf(item.getCover().getImgId()));
                        }
                    } else {
                        if (item.getCover().getUrl() != null) {
                            downloadGif(String.valueOf(id), item.getCover().getUrl(), false, String.valueOf(item.getCover().getImgId()));
                        }
                    }
//                    System.out.println(item.getCover());
                }
            }

            downloadAllBiaoqingPics(page + 1, id);
        } else {
            if (page == 1 && (items == null || items.size() == 0)) {
            } else {
                downloadAllBiaoqingPics(1, id + 1);
            }
        }
    }

    public void downloadGif(String fileName, String imgUrl, boolean isGif, String imageName) {

        FileOutputStream outputStream = null;
        InputStream inputStream = null;
        try {
            URL url = new URL(imgUrl);
            URLConnection connection = url.openConnection();
            inputStream = connection.getInputStream();

            File path = new File(ResourceUtils.getURL("classpath:").getPath());
            File imagesFolder = new File(path.getAbsolutePath(), "static/images/" + fileName);
            if (!imagesFolder.exists()) {
                imagesFolder.mkdirs();
            }

            File imageSaveFile;
            if (isGif) {
                imageSaveFile = new File(imagesFolder + "/" + imageName + ".gif");
            } else {
                imageSaveFile = new File(imagesFolder + "/" + imageName + ".jpg");
            }


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
