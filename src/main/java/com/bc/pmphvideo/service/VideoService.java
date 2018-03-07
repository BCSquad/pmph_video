/*
 * Copyright 2017 BangChen Information Technology Ltd., Co.
 * Licensed under the Apache License 2.0.
 */
package com.bc.pmphvideo.service;

import com.bc.pmphvideo.Properties;
import com.bc.pmphvideo.domain.VideoToken;
import com.bc.pmphvideo.service.exception.VideoServiceException;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * 视频上传与转码服务
 *
 * @author L.X <gugia@qq.com>
 */
@Slf4j
@Service
public class VideoService {

    @Resource
    VideoTask videoTask;

    Random random = new Random();

    public String upload(MultipartFile file) throws IOException {
        if (null == file || file.isEmpty()) {
            throw new VideoServiceException("未获取到上传文件");
        }
        if (checkContentType(file.getOriginalFilename()) > 0) {
            throw new VideoServiceException("不受支持的视频文件格式");
        }
        String uploadPath;
        String os = System.getProperty("os.name");
        if (os.toLowerCase().startsWith("win")) {
            uploadPath = Properties.PATH_WINDOWS_PREFIX;
        } else {
            uploadPath = Properties.PATH_LINUX_PREFIX;
        }
        uploadPath = uploadPath.concat(Properties.PATH_ORIG)
                .concat(File.separator)
                .concat(String.valueOf(System.currentTimeMillis()))
                .concat("-")
                .concat(file.getOriginalFilename());
        file.transferTo(new File(uploadPath));
        VideoToken token = new VideoToken();
        token.setOrigFileName(file.getOriginalFilename());
        token.setOrigFileSize(file.getSize());
        token.setOrigPath(uploadPath);
        token.setMessage("正在转码");
        String key = String.valueOf(System.currentTimeMillis()).concat(String.valueOf(random.nextInt()));
        Properties.map.put(key, token);
        videoTask.transcoding(key);
        return key;
    }

    private int checkContentType(String filename) {
        String suffix = filename.substring(filename.lastIndexOf(".") + 1, filename.length()).toLowerCase();
        // ffmpeg能解析的格式：（asx，asf，mpg，wmv，3gp，mp4，mov，avi，flv等）
        switch (suffix) {
            case "avi":
                return 0;
            case "mpg":
                return 0;
            case "wmv":
                return 0;
            case "3gp":
                return 0;
            case "mov":
                return 0;
            case "mp4":
                return 0;
            case "asf":
                return 0;
            case "asx":
                return 0;
            // 对ffmpeg无法解析的文件格式(wmv9，rm，rmvb等),
            case "flv":
                return 0;
            case "wmv9":
                return 1;
            case "rm":
                return 1;
            case "rmvb":
                return 1;
            default:
                return 2;
        }
    }
}
