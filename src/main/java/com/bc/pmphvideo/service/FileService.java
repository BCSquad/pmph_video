/*
 * Copyright 2017 BangChen Information Technology Ltd., Co.
 * Licensed under the Apache License 2.0.
 */
package com.bc.pmphvideo.service;

import com.bc.pmphvideo.service.exception.VideoServiceException;
import com.mongodb.gridfs.GridFSDBFile;
import javax.annotation.Resource;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsCriteria;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

/**
 * MongoDB文件服务
 *
 * @author L.X <gugia@qq.com>
 */
@Service
public class FileService {

    @Resource
    GridFsTemplate gridFsTemplate;

    /**
     * 根据MySQL中存储的MongoDB主键获取指定文件
     *
     * @param id 文件在MongoDB中的主键
     * @return GridFSDBFile对象
     */
    public GridFSDBFile get(String id) {
        if (null == id || id.isEmpty()) {
            throw new VideoServiceException("获取文件时ID为空");
        }
        return gridFsTemplate.findOne(Query.query(new GridFsCriteria("_id").is(id)));
    }
}
