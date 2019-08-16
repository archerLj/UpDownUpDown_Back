package com.meizidelegate.source;

import com.meizidelegate.source.fileUploadADownload.DownLoadFile;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class SourceApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(SourceApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(this.getClass());
    }

}
