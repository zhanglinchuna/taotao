package com.taotao.web.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PropertieService {

    @Value("${TAOTAO_SSO_UTL}")
    public String TAOTAO_SSO_URL;
}
