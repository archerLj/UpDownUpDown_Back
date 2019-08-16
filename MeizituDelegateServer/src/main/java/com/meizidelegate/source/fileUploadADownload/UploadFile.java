package com.meizidelegate.source.fileUploadADownload;

import com.alibaba.fastjson.JSON;
import com.meizidelegate.source.model.ResponseCommon;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@RestController
public class UploadFile {

    /**
     *
     * 检查分片是否存在
     *
     * */
    @PostMapping("checkChunk")
    @ResponseBody
    public ResponseCommon checkChunk(@RequestParam(value = "md5file") String md5file, @RequestParam(value = "chunk") Integer chunk) {

        ResponseCommon responseCommon = new ResponseCommon();

        // 这里通过判断分片对应的文件存不存在来判断分片有没有上传
        try {
            File path = new File(ResourceUtils.getURL("classpath:").getPath());
            File fileUpload = new File(path.getAbsolutePath(), "static/uploaded/" + md5file + "/" + chunk + ".tmp");

            if (fileUpload.exists()) {
                responseCommon.setCode("0001");
                responseCommon.setMsg("分片已上传");
            } else {
                responseCommon.setCode("0002");
                responseCommon.setMsg("分片未上传");
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
            responseCommon.setCode("1111");
            responseCommon.setMsg(e.getLocalizedMessage());
        }

        return responseCommon;
    }

    /**
     *
     * 所有分片上传完成后，合并分片
     *
     * */
    public boolean merge(@RequestParam(value = "truncks") Integer truncks,
                                @RequestParam(value = "md5File") String md5File,
                                @RequestParam(value = "fileName") String fileName) {

        ResponseCommon responseCommon = new ResponseCommon();

        FileOutputStream outputStream = null;
        try {
            File path = new File(ResourceUtils.getURL("classpath:").getPath());
            File md5FilePath = new File(path.getAbsolutePath(), "static/uploaded/" + md5File);
            File finalFile = new File(path.getAbsolutePath(), "static/uploaded/" + fileName);

            outputStream = new FileOutputStream(finalFile);

            byte[] buffer = new byte[1024];
            for (int i=1; i<=truncks; i++) {
                String chunckFile = i + ".tmp";
                File tmpFile = new File(md5FilePath + "/" + chunckFile);
                InputStream inputStream = new FileInputStream(tmpFile);
                int len = 0;
                while ((len = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, len);
                }
                inputStream.close();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;

        } catch (IOException e) {
            e.printStackTrace();
            return false;

        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }


    /**
     *
     * 文件上传，这里我们接受分片文件，总文件的md5值，以及分片索引
     * 这里，我们索引从1开始。
     *
     * */
    @PostMapping("upload")
    @ResponseBody
    public ResponseCommon upload(@RequestParam(value = "file") MultipartFile file,
                          @RequestParam(value = "md5File") String md5File,
                          @RequestParam(value = "truncks") Integer truncks,
                          @RequestParam(value = "currentTrunck") Integer currentTrunck) {

        ResponseCommon responseCommon = new ResponseCommon();

        try {
            File path = new File(ResourceUtils.getURL("classpath:").getPath());
            File fileUpload;
            if (truncks == 1) {
                fileUpload = new File(path.getAbsolutePath(), "static/uploaded/" + md5File + "/1.tmp");
            } else {
                fileUpload = new File(path.getAbsolutePath(), "static/uploaded/" + md5File + "/" + currentTrunck + ".tmp");
            }

            if (!fileUpload.exists()) {
                fileUpload.mkdirs();
            }
            file.transferTo(fileUpload);

            if (currentTrunck == truncks) {
                boolean result = this.merge(truncks, md5File, file.getOriginalFilename());
                if (result) {
                    responseCommon.setCode("0000");
                    responseCommon.setMsg("上传成功");
                } else {
                    responseCommon.setCode("1111");
                    responseCommon.setMsg("文件合并失败");
                }

            } else {
                responseCommon.setCode("0000");
                responseCommon.setMsg("上传成功");
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            responseCommon.setCode("1111");
            responseCommon.setMsg(e.getLocalizedMessage());

        } catch (IOException e) {
            e.printStackTrace();
            responseCommon.setCode("1111");
            responseCommon.setMsg(e.getLocalizedMessage());
        }

        return responseCommon;
    }
}
