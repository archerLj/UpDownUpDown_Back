package com.meizidelegate.source;

import com.alibaba.fastjson.JSON;
import com.meizidelegate.source.model.HuabanADS;
import com.meizidelegate.source.model.Kanlema;
import com.meizidelegate.source.model.KanlemaStream;
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

public class DownloadKanlema {
    public void download(int pageNo) {
        /**
         * 动画学术趴： 111186
         * 搞笑院长： 33344312
         * 第十放映室：33354554
         * 互联网的那些事： 2258067
         * 科学懂不懂： 33520525
         *
         * */
        String url = "http://api.klm123.com/user/getVideoList?pageSize=10&pageNo=" + pageNo + "&userId=111186";

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
        HashMap<String, Object> data = JSON.parseObject(JSON.toJSONString(resultMap.get("data")), HashMap.class);

        List<Kanlema> kanlemas = JSON.parseArray(JSON.toJSONString(data.get("videos")), Kanlema.class);
        String lastId = null;
        if (kanlemas != null && kanlemas.size() > 0) {
            for (int i = 0; i < kanlemas.size(); i++) {
                Kanlema kanlema = kanlemas.get(i);
                lastId = kanlema.getDocid();


                KanlemaStream stream;
                if (kanlema.getStreams().size() == 3) {
                    stream = kanlema.getStreams().get(2);
                } else if (kanlema.getStreams().size() == 2) {
                    stream = kanlema.getStreams().get(1);
                } else {
                    stream = kanlema.getStreams().get(0);
                }

                writeYulus(kanlema.getDocid(), kanlema.getTitle(), kanlema.getCover(), stream.getUrl());
                downloadPreviewImg(kanlema.getCover(), kanlema.getDocid());
                downloadVideo(stream.getUrl(), kanlema.getDocid());
            }

            download(pageNo + 1);
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
