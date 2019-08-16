package com.meizidelegate.source.fileUploadADownload;

import com.meizidelegate.source.model.ResponseCommon;
import org.apache.commons.io.IOUtils;
import org.springframework.util.DigestUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@RestController
public class DownLoadFile {

    @GetMapping("getTruncks")
    @ResponseBody
    public ResponseCommon getTruncks() {

        ResponseCommon responseCommon = new ResponseCommon();
        responseCommon.setCode("0000");


        try {
            File file = new File("/Users/archerlj/Desktop/Untitled/myFilm.mp4");
            Long fileLength = file.length();
            Long trunck = fileLength/(1024 * 1024);

            if (fileLength%(1024*1024) == 0) {
                responseCommon.setTruncks(trunck.intValue());
            } else {
                responseCommon.setTruncks(trunck.intValue() + 1);
            }

            String fileMD5 = DigestUtils.md5DigestAsHex(new FileInputStream(file));
            responseCommon.setFileMD5(fileMD5);
            responseCommon.setCode("0000");

        } catch (IOException e) {
            e.printStackTrace();
            responseCommon.setCode("1111");
            responseCommon.setMsg(e.getLocalizedMessage());
        }

        return responseCommon;
    }

    @GetMapping("downloadFile")
    @ResponseBody
    public void downloadFile(Integer trunck, HttpServletRequest request, HttpServletResponse response) {

        try {
            RandomAccessFile file = new RandomAccessFile("/Users/archerlj/Desktop/Untitled/myFilm.mp4", "r");
            long offset = (trunck - 1) * 1024 * 1024;
            file.seek(offset);

            byte[] buffer = new byte[1024];
            int len = 0;
            int allLen = 0;
            for (int i=0; i<1024; i++) {
                len = file.read(buffer);

                if (len == -1) {
                    break;
                }

                allLen += len;
                response.getOutputStream().write(buffer, 0, len);
                file.seek(offset + (1024 * (i + 1)));
            }

            file.close();
            response.setContentLength(allLen);
            response.flushBuffer();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
