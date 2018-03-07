/*
 * Copyright 2017 BangChen Information Technology Ltd., Co.
 * Licensed under the Apache License 2.0.
 */
package com.bc.pmphvideo;

import com.bc.pmphvideo.domain.VideoToken;
import java.util.HashMap;

/**
 * 全局变量
 *
 * @author L.X <gugia@qq.com>
 */
public class Properties {

    public static final String PATH_WINDOWS_PREFIX = "C:\\pmphvideo\\";

    public static final String PATH_LINUX_PREFIX = "/home/ftp/pmphvideo/";

    public static final String PATH = "after";

    public static final String PATH_ORIG = "before";

    //public static final String CMD_WINDOWS = " -ab 64 -acodec libmp3lame -ac 2 -ar 22050 -r 24 -qscale 4 -y ";
    public static final String CMD_WINDOWS = " -ab 64 -ac 2 -ar 22050 -r 24 -qscale 4 -y ";

    public static final String CMD_LINUX = " -ab 64 -ac 2 -ar 22050 -r 24 -y ";

    public static HashMap<String, VideoToken> map = new HashMap<>(16);

    public static final int KEEPTIME = 5;//VideoToken保留时间，单位分钟
}
