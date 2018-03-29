/*
 * Copyright 2017 BangChen Information Technology Ltd., Co.
 * Licensed under the Apache License 2.0.
 */
package com.bc.pmphvideo.controller;

import com.bc.pmphvideo.Properties;
import com.bc.pmphvideo.controller.bean.ResponseBean;
import com.bc.pmphvideo.domain.VideoToken;
import com.bc.pmphvideo.service.VideoService;
import com.bc.pmphvideo.service.exception.VideoServiceException;
import java.io.IOException;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * 控制器
 *
 * @author L.X <gugia@qq.com>
 */
@Slf4j
@Controller
public class VideoController {

    @Resource
    VideoService videoService;

    @ResponseBody
    @RequestMapping(value = "/v/upload", method = RequestMethod.POST)
    public ResponseBean upload(@RequestParam("file") MultipartFile file) throws IOException {
        if (Properties.map.size() >= 30) {
            return new ResponseBean(new VideoServiceException("短时间内转码的视频过多，请稍后再试"));
        }
        return new ResponseBean(videoService.upload(file));
    }

    @ResponseBody
    @RequestMapping(value = "/v/query", method = RequestMethod.GET)
    public ResponseBean query(@RequestParam("key") String key) {
        VideoToken token = Properties.map.get(key);
        if (null == token) {
            return new ResponseBean(new VideoServiceException("未找到对应的视频上传记录"));
        }
        token.setQueried(true);
        Properties.map.put(key, token);
        return new ResponseBean(token);
    }
}
