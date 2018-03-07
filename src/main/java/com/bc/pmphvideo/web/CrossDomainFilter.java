/*
 * Copyright 2017 BangChen Information Technology Ltd., Co.
 * Licensed under the Apache License 2.0.
 */
package com.bc.pmphvideo.web;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * 跨域过滤器
 *
 * @author L.X <gugia@qq.com>
 */
@Slf4j
public class CrossDomainFilter implements Filter {

    private String filterName;

    @Override
    public void init(FilterConfig fc) throws ServletException {
        filterName = fc.getFilterName();
        log.debug("{} initialized", filterName);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain fc) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        log.debug("This is BaseFilter, url :{}", req.getRequestURI());
        HttpServletResponse resp = (HttpServletResponse) response;
        resp.setHeader("Access-Control-Allow-Origin", req.getHeader("Origin"));
        resp.setHeader("Access-Control-Allow-Credentials", "true");
        resp.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        resp.setHeader("Access-Control-Max-Age", "3600");
        resp.setHeader("Access-Control-Allow-Headers", "x-requested-with");
        fc.doFilter(req, response);
    }

    @Override
    public void destroy() {
        log.debug("{} is going to be destroyed.", filterName);
    }
}
