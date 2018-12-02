package com.taotao.manage.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PropertieService {

    // 上传的图片存储路径
    @Value("${REPOSITORY_PATH}")
    public String REPOSITORY_PATH;

    // 上传图片服务器地址
    @Value("${IMAGE_BASE_URL}")
    public String IMAGE_BASE_URL;
}
