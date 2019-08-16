package com.meizidelegate.source;

import com.alibaba.fastjson.JSON;
import com.meizidelegate.source.model.HuabanADS;
import com.meizidelegate.source.model.VideoInfo;
import com.meizidelegate.source.model.VideoInfoWrapper;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;

public class DownloadHuabanAD {
    public void download(String lastID) {
        String url;
        if (lastID == null) {
            url = "https://api.huaban.com/videos/categories/14?limit=10";
        } else {
            url = "https://api.huaban.com/videos/categories/14?max=" + lastID + "&limit=10";
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
        ResponseEntity<String> response = client.exchange(url, HttpMethod.GET, requestEntity, String.class);

        String result = response.getBody();
        HashMap<String, Object> resultMap = JSON.parseObject(result, HashMap.class);

        List<HuabanADS> huabanADSs = JSON.parseArray(JSON.toJSONString(resultMap.get("videos")), HuabanADS.class);
        String lastId = null;
        if (huabanADSs != null && huabanADSs.size() > 0) {
            for (int i = 0; i < huabanADSs.size(); i++) {
                HuabanADS huabanADS1 = huabanADSs.get(i);
                lastId = huabanADS1.getSeq();
                String preview = "https://hbimg.huabanimg.com//" + huabanADS1.getFile().getKey() + "_/fw/480";
                writeYulus(huabanADS1.getSeq(), huabanADS1.getRaw_text(), preview, huabanADS1.getOrig_source());

                downloadPreviewImg(preview, huabanADS1.getSeq());
                downloadVideo(huabanADS1.getOrig_source(), huabanADS1.getSeq());
            }

            download(lastId);
        } else {
            System.out.println("=========================");
            System.out.println("======   End    =========");
            System.out.println("=========================");
        }
    }

    public void writeYulus(String videoId, String videoTitle, String previewImg, String videoUrl) {
        FileWriter fw = null;
        PrintWriter printWriter = null;
        try {
            File path = new File(ResourceUtils.getURL("classpath:").getPath());
            File yuluFile = new File(path.getAbsolutePath(), "static/titles.txt");
            fw = new FileWriter(yuluFile, true);

            printWriter = new PrintWriter(fw);
            printWriter.println("[" + videoId + "]");
            printWriter.println(videoTitle);
            printWriter.println(previewImg);
            printWriter.println(videoUrl);
            printWriter.println("=========================================");
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

    public void downloadPreviewImg(String imgUrl, String imgName) {

        FileOutputStream outputStream = null;
        InputStream inputStream = null;
        try {
            URL url = new URL(imgUrl);
            URLConnection connection = url.openConnection();
            inputStream = connection.getInputStream();

            File path = new File(ResourceUtils.getURL("classpath:").getPath());
            File imagesFolder = new File(path.getAbsolutePath(), "static/videos/preview");
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

    public void downloadVideo(String videoUrl, String videoName) {

        FileOutputStream outputStream = null;
        InputStream inputStream = null;
        try {
            URL url = new URL(videoUrl);
            URLConnection connection = url.openConnection();
            inputStream = connection.getInputStream();

            File path = new File(ResourceUtils.getURL("classpath:").getPath());
            File imagesFolder = new File(path.getAbsolutePath(), "static/videos/video");
            if (!imagesFolder.exists()) {
                imagesFolder.mkdirs();
            }

            File imageSaveFile = new File(imagesFolder + "/" + videoName + ".mp4");


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
