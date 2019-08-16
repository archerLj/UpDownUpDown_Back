package com.meizidelegate.source;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.meizidelegate.source.model.BiaoqingItem;
import com.meizidelegate.source.model.VideoInfo;
import com.meizidelegate.source.model.VideoInfoWrapper;
import com.meizidelegate.source.model.XiaoYinXinagVideo;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 小印象只能一个category一个category的下，75/86/87/88
 * */
@RestController
public class DownloadXiaoYinXiangVideos {

    private Set<String> allIDs = new HashSet<>();

    public void download(int category, String lastId) {
        String url;
        if (lastId == null) {
            url = "https://api.actuive.com/v1/Index/videoListByCategory?op=0&page_size=10&category_id=" + category + "&last_id=" + lastId;
        } else {
            url = "https://api.actuive.com/v1/Index/videoListByCategory?op=1&page_size=10&category_id=" + category;
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

        HashMap<String, Object> list = JSON.parseObject(resultMap.get("data").toString(), HashMap.class);
        List<XiaoYinXinagVideo> videos = JSON.parseArray(JSON.toJSONString(list.get("video_list")), XiaoYinXinagVideo.class);

        String lastVideoId = null;
        if (videos != null && videos.size() > 0) {
            for (int i = 0; i < videos.size(); i++) {
                XiaoYinXinagVideo video = videos.get(i);

                if (!allIDs.contains(video.getVideo_id())) {
                    allIDs.add(video.getVideo_id());
                    writeYulus(video.getVideo_id(), video.getTitle(), video.getCover_img(), video.getPublic_video_down_url());
                } else {
                    System.out.println("----- 重复 ----------");
                }


//                System.out.println(video.getVideo_id() + " : " + video.getTitle());
//                downloadPreviewImg(String.valueOf(category),video.getCover_img(), video.getVideo_id());
//                downloadVideo(String.valueOf(category), video.getPublic_video_down_url(), video.getVideo_id());
                lastVideoId = video.getVideo_id();
            }

            download(category, lastVideoId);
        }
//        else {
//            if (category == 75) {
//                download(86, null);
//            } else if (category == 86) {
//                download(87, null);
//            } else if (category == 87) {
//                download(88, null);
//            }
//        }
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

    public void downloadPreviewImg(String fileName, String imgUrl, String imgName) {

        FileOutputStream outputStream = null;
        InputStream inputStream = null;
        try {
            URL url = new URL(imgUrl);
            URLConnection connection = url.openConnection();
            inputStream = connection.getInputStream();

            File path = new File(ResourceUtils.getURL("classpath:").getPath());
            File imagesFolder = new File(path.getAbsolutePath(), "static/videos/preview" + fileName);
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

    public void downloadVideo(String fileName, String videoUrl, String videoName) {

        FileOutputStream outputStream = null;
        InputStream inputStream = null;
        try {
            URL url = new URL(videoUrl);
            URLConnection connection = url.openConnection();
            inputStream = connection.getInputStream();

            File path = new File(ResourceUtils.getURL("classpath:").getPath());
            File imagesFolder = new File(path.getAbsolutePath(), "static/videos/video" + fileName);
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
