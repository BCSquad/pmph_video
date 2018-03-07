/*
 * Copyright 2017 BangChen Information Technology Ltd., Co.
 * Licensed under the Apache License 2.0.
 */
package com.bc.pmphvideo.service;

import com.bc.pmphvideo.Properties;
import com.bc.pmphvideo.domain.VideoToken;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 转码异步任务
 *
 * @author L.X <gugia@qq.com>
 */
@Slf4j
@Component
public class VideoTask {

    @Async
    public void transcoding(String key) {
        VideoToken token = Properties.map.get(key);
        /* 设置新名称和保存路径 */
        String fileName = java.util.UUID.randomUUID().toString().concat(".mp4");
        token.setFileName(fileName);
        String filePath;
        /* 命令拼接 */
        StringBuilder command = new StringBuilder();
        String os = System.getProperty("os.name");
        if (os.toLowerCase().startsWith("win")) {
            filePath = Properties.PATH_WINDOWS_PREFIX + Properties.PATH + File.separator + fileName;
            command.append(Properties.PATH_WINDOWS_PREFIX.concat("ffmpeg.exe"));
            command.append(" -i ");
            command.append(token.getOrigPath());
            command.append(Properties.CMD_WINDOWS);
            command.append(filePath);
        } else {
            filePath = Properties.PATH_LINUX_PREFIX + Properties.PATH + File.separator + fileName;
            command.append("ffmpeg -i ");
            command.append(token.getOrigPath());
            command.append(Properties.CMD_LINUX);
            command.append(filePath);
        }
        Runtime rt = Runtime.getRuntime();
        Process proc;
        try {
            proc = rt.exec(command.toString());
            //log.info("转码结果：{}", String.valueOf(proc.waitFor()));
        } catch (IOException ex) {
            log.error("视频转码出现错误", ex);
            token.setDone(true);
            token.setError(true);
            token.setMessage("转码失败，错误代码0x01");
            Properties.map.put(key, token);
            deleteFile(filePath);
            clear(key);
            return;
        }
        try {
            InputStream is = proc.getErrorStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                log.info(line);
            }
        } catch (IOException ex) {
            log.error("读取错误流出现错误", ex);
            token.setDone(true);
            token.setError(true);
            token.setMessage("转码失败，错误代码0x02");
            Properties.map.put(key, token);
            deleteFile(filePath);
            clear(key);
            return;
        }
        token.setDone(true);
        token.setFileName(fileName);
        token.setPath(filePath);
        File file = new File(filePath);
        token.setFileSize(file.length());//转码后的文件大小
        token.setMessage("转码成功");
        Properties.map.put(key, token);
        clear(key);
    }

    @Async
    public void clear(String key) {
        try {
            Thread.sleep(Properties.KEEPTIME * 60 * 1000);
        } catch (InterruptedException ex) {
            log.warn(ex.getMessage());
        }
        VideoToken token = Properties.map.get(key);
        if (null != token) {
            /* 超过一定时间VideoToken未被查询，删除视频文件 */
            if (!token.isQueried()) {
                deleteFile(token.getPath());
                deleteFile(token.getOrigPath());
            }
            Properties.map.remove(key);
        }
    }

    public static boolean deleteFile(String path) {
        File file = new File(path);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            return file.delete();
        } else {
            return false; //文件不存在;
        }
    }
}
