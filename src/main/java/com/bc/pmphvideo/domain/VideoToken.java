/*
 * Copyright 2017 BangChen Information Technology Ltd., Co.
 * Licensed under the Apache License 2.0.
 */
package com.bc.pmphvideo.domain;

import lombok.Data;

/**
 * 视频Token
 *
 * @author L.X <gugia@qq.com>
 */
@Data
public class VideoToken {

    private boolean isDone = false;

    private boolean isError = false;
    
    private boolean isQueried = false;

    private String origPath;

    private String origFileName;

    private Long origFileSize;

    private String path;

    private String fileName;

    private Long fileSize;

    private String message;
}
