/*
 * Copyright 2017 BangChen Information Technology Ltd., Co.
 * Licensed under the Apache License 2.0.
 */
package com.bc.pmphvideo.controller.bean;

import com.bc.pmphvideo.service.exception.VideoServiceException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * AOP实现
 *
 * @author L.X <gugia@qq.com>
 */
@Slf4j
@Aspect
@Component
public class ResponseBeanAspect {

    @Pointcut("execution(public * com.bc.pmphvideo.controller.*.*(..))")
    public void pointcut() {

    }

    @Around("pointcut()")
    public Object controllerMethodHandler(ProceedingJoinPoint pjp) throws Throwable {
        long startTime = System.currentTimeMillis();
        ResponseBean<?> responseBean;
        try {
            responseBean = (ResponseBean<?>) pjp.proceed();
            log.info(pjp.getSignature() + "use time:" + (System.currentTimeMillis() - startTime));
        } catch (Throwable ex) {
            responseBean = exceptionHandler(pjp, ex);
        }
        return responseBean;
    }

    private ResponseBean<?> exceptionHandler(ProceedingJoinPoint pjp, Throwable ex) {
        ResponseBean<?> responseBean = new ResponseBean();
        StringBuilder sb = new StringBuilder();
        sb.append(pjp.getSignature().toString());
        sb.append(" 发生错误:{}");
        if (ex instanceof VideoServiceException) {
            responseBean.setCode(2);
            // 如果是已检查的异常，不打印异常堆栈
            responseBean.setMsg(ex.getMessage());
            log.error(sb.toString(), ex.toString());
        } else if (ex instanceof IndexOutOfBoundsException) {
            responseBean.setMsg("下标越界异常");
            responseBean.setCode(ResponseBean.INDEX_OUT_OF_BOUNDS);
            log.error(sb.toString(), ex.toString());
        } else if (ex instanceof ClassCastException) {
            responseBean.setMsg("类型转换异常");
            responseBean.setCode(ResponseBean.CLASS_CAST);
            log.error(sb.toString(), ex.toString());
        } else {
            responseBean.setMsg(ex.toString());
            responseBean.setCode(ResponseBean.UNKNOWN_ERROR);
            // 未知异常应打印堆栈
            log.error(pjp.getSignature() + " 发生未知错误", ex);
        }
        return responseBean;
    }
}
