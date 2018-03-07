/*
 * Copyright 2017 BangChen Information Technology Ltd., Co.
 * Licensed under the Apache License 2.0.
 */
package com.bc.pmphvideo.service.exception;

/**
 * 自定义异常
 *
 * @author L.X <gugia@qq.com>
 */
public class VideoServiceException extends RuntimeException {

    public VideoServiceException(String message) {
        super(message);
    }
}
