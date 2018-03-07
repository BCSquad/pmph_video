/*
 * Copyright 2017 BangChen Information Technology Ltd., Co.
 * Licensed under the Apache License 2.0.
 */
package com.bc.pmphvideo.web;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Web配置
 *
 * @author L.X <gugia@qq.com>
 */
@Configuration
public class WebConfiguration {

    @Bean
    public AsyncTaskExecutor getAsync() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(7);
        executor.setMaxPoolSize(42);
        executor.setQueueCapacity(11);
        executor.setThreadNamePrefix("AsyncExec-");
        executor.initialize();
        return executor;
    }

    @Bean
    public FilterRegistrationBean filterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new CrossDomainFilter());
        registration.addUrlPatterns("/*");
        registration.addInitParameter("privileged", "true");
        registration.setName("CrossDomainFilter");
        registration.setOrder(1);
        return registration;
    }
}
