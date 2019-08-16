package com.meizidelegate.source;

import com.alibaba.fastjson.JSON;
import com.meizidelegate.source.model.VideoInfo;
import com.meizidelegate.source.model.VideoInfoWrapper;
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
import java.util.*;

@RestController
public class DownloadDuSheVideos {

    private Set<String> allIDs = new HashSet<>();

    public void download(int startIndex) {
        String url = "https://dswxapp.dushemovie.com/dsmovieapi/ssl2/video/list_videos/1?count=10&startIndex=" + startIndex;
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

        List<VideoInfoWrapper> wrappers = JSON.parseArray(JSON.toJSONString(resultMap.get("videoDataList")), VideoInfoWrapper.class);
        if (wrappers != null && wrappers.size() > 0) {
            for (int i = 0; i < wrappers.size(); i++) {
                VideoInfoWrapper wrapper = wrappers.get(i);
                VideoInfo videoInfo = wrapper.getVideoInfo();

                if (!allIDs.contains(videoInfo.getId())) {
                    allIDs.add(videoInfo.getId());
                    writeYulus(videoInfo.getId(), videoInfo.getTitle(), videoInfo.getThumbnailUrl(), videoInfo.getVideoUrl());
                }

//                System.out.println(videoInfo.getId() + " : " + videoInfo.getTitle());
//                downloadPreviewImg(videoInfo.getThumbnailUrl(), videoInfo.getId());
//                downloadVideo(videoInfo.getVideoUrl(), videoInfo.getId());
            }

            download(startIndex + 10);
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
