package com.meizidelegate.source;

import com.alibaba.fastjson.JSONObject;
import com.meizidelegate.source.model.MeiziListItem;
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
import java.util.List;

@RestController
public class DownloadMeiziTu {

    /**
     * 抓取所有的妹子图
     * */
    public void downloadAllMeiZiTuPics(int page, String folderName) {

        String url = "https://api.meizitu.net/wp-json/wp/v2/posts?page=" + page;
        MultiValueMap<String, String> params= new LinkedMultiValueMap<String, String>();

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
        ResponseEntity<String> response = client.exchange(url, HttpMethod.GET, requestEntity, String.class);
        String result = response.getBody();

        // json解析
        List<MeiziListItem> meiziItems = JSONObject.parseArray(result, MeiziListItem.class);
        for (int i = 0; i < meiziItems.size(); i++) {
            MeiziListItem item = meiziItems.get(i);


            int picNumbers = item.getImg_num();

            String subStr = item.getThumb_src().substring(23);
            String picName = subStr.split("/")[2];
            String picNamePrefix = picName.substring(0, picName.length() - 6);


            for (int j = 1; j < picNumbers; j++) {
                String picUrl = item.getThumb_src().replace(".jpg", "");
                picUrl = picUrl.substring(0, picUrl.length() - 2);
                if (j < 10) {
                    picUrl = picUrl + "0" + j + ".jpg";
                } else {
                    picUrl = picUrl + j + ".jpg";
                }
                downloadImg(folderName, picUrl, String.valueOf(j));
            };

            folderName = String.valueOf((Integer.valueOf(folderName) + 1));
        }

        if (meiziItems != null && meiziItems.size() > 0) {
            String log = "page: " + page + " | size: " + meiziItems.size();
            System.out.println(log);
            downloadAllMeiZiTuPics(page + 1, folderName);
        }
    }

    public void downloadImg(String fileName, String imgUrl, String imgName) {

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
