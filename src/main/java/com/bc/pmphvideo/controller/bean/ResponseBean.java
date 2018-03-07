/*
 * Copyright 2017 BangChen Information Technology Ltd., Co.
 * Licensed under the Apache License 2.0.
 */
package com.bc.pmphvideo.controller.bean;

import java.io.Serializable;

/**
 * 控制器通用返回格式
 *
 * @author L.X <gugia@qq.com>
 * @param <T> 返回类型
 */
public class ResponseBean<T> implements Serializable {

    /**
     * 成功
     */
    public static final int SUCCESS = 1;

    /**
     * 错误的请求参数
     */
    public static final int WRONG_REQ_PARA = 20;

    /**
     * 未经检查的错误
     */
    public static final int UNCHECKED_ERROR = 21;
    /**
     * 下标越界异常 IndexOutOfBoundsException
     */
    public static final int INDEX_OUT_OF_BOUNDS = 30;
    /**
     * 类型转换异常ClassCastException
     */
    public static final int CLASS_CAST = 40;
    /**
     * 数据库连接超时
     */
    public static final int DATABASE_TIME_OUT = 90;
    /**
     * MongoDBException
     */
    public static final int MONGO_EXCEPTION = 95;

    /**
     * 未知错误
     */
    public static final int UNKNOWN_ERROR = 99;

    /**
     * 没有操作权限
     */
    public static final int NO_PERMISSION = 100;

    /**
     * 重新登陆
     */
    public static final int LOGIN_AGAIN = 110;

    private int code = SUCCESS;
    private String msg = "success";
    private T data;

    public ResponseBean() {
        super();
    }

    public ResponseBean(T data) {
        super();
        this.data = data;
    }

    public ResponseBean(Throwable ex) {
        super();
        this.msg = ex.getMessage();
        this.code = UNCHECKED_ERROR;
    }

    /**
     * @return the msg
     */
    public String getMsg() {
        return msg;
    }

    /**
     * @param msg the msg to set
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * @return the code
     */
    public int getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * @return the data
     */
    public T getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(T data) {
        this.data = data;
    }
}
