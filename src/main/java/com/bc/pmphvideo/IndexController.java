/*
 * Copyright 2017 BangChen Information Technology Ltd., Co.
 * Licensed under the Apache License 2.0.
 */
package com.bc.pmphvideo;

import com.bc.pmphvideo.service.FileService;
import com.mongodb.gridfs.GridFSDBFile;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 测试控制器
 *
 * @author L.X <gugia@qq.com>
 */
@Slf4j
@Controller
public class IndexController {

    @RequestMapping("/v")
    public String index() {
        return "index";
    }

    @Resource
    FileService fileService;

    @ResponseBody
    @RequestMapping(value = "/v/image/{id}", method = RequestMethod.GET)
    public void outputImage(@PathVariable("id") String id, HttpServletResponse response) {
        response.setContentType("image/png");
        GridFSDBFile file = fileService.get(id);
        if (null == file) {
            log.error("未找到id为'{}'的图片文件", id);
        }
        try (OutputStream out = response.getOutputStream()) {
            file.writeTo(out);
            out.flush();
            out.close();
        } catch (IOException ex) {
            log.error("文件下载时出现IO异常：{}", ex.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping(value = "/v/play/{filename}", method = RequestMethod.GET)
    public void playVideo(@PathVariable("filename") String filename, HttpServletResponse response) throws IOException {
        String path;
        String os = System.getProperty("os.name");
        if (os.toLowerCase().startsWith("win")) {
            path = Properties.PATH_WINDOWS_PREFIX;
        } else {
            path = Properties.PATH_LINUX_PREFIX;
        }
        path = path.concat(Properties.PATH).concat(File.separator).concat(filename).concat(".mp4");
        File file = new File(path);
        if (file.exists() && file.isFile()) {
            ServletOutputStream out;
            try (FileInputStream in = new FileInputStream(file)) {
                out = response.getOutputStream();
                byte[] buff;
                while (in.available() > 0) {
                    buff = new byte[in.available() > 10240 ? 10240 : in.available()];
                    in.read(buff, 0, buff.length);
                    out.write(buff, 0, buff.length);
                }
                in.close();
                out.flush();
                out.close();
            }
        } else {
            log.warn("视频'{}'不存在", filename);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/v/download", method = RequestMethod.GET)
    public void downloadVideo(@RequestParam("realname") String realname, @RequestParam("filename") String filename,
            HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/force-download");
        try {
            StringBuilder sb = new StringBuilder("attachment;fileName=");
            String userAgent = request.getHeader("User-Agent");
            if (userAgent.toLowerCase().contains("mozilla")) {
                filename = URLEncoder.encode(filename, "UTF-8");
            } else {
                filename = new String(filename.getBytes("utf-8"), "ISO8859-1");
            }
            sb.append(filename);
            sb.append(".mp4");
            response.setHeader("Content-Disposition", sb.toString().replace("+", "%20"));
        } catch (UnsupportedEncodingException e) {
            log.warn("修改编码格式的时候失败");
        }
        String path;
        String os = System.getProperty("os.name");
        if (os.toLowerCase().startsWith("win")) {
            path = Properties.PATH_WINDOWS_PREFIX;
        } else {
            path = Properties.PATH_LINUX_PREFIX;
        }
        path = path.concat(Properties.PATH).concat(File.separator).concat(realname).concat(".mp4");
        File file = new File(path);
        if (file.exists() && file.isFile()) {
            ServletOutputStream out;
            try (FileInputStream in = new FileInputStream(file)) {
                out = response.getOutputStream();
                byte[] buff;
                while (in.available() > 0) {
                    buff = new byte[in.available() > 10240 ? 10240 : in.available()];
                    in.read(buff, 0, buff.length);
                    out.write(buff, 0, buff.length);
                }
                in.close();
                out.flush();
                out.close();
            }
        } else {
            log.warn("视频'{}'不存在", realname);
        }
    }
}
