package com.taotao.web.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.service.ApiService;
import com.taotao.web.bean.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    @Autowired
    private PropertieService propertieService;

    @Autowired
    private ApiService apiService;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public User queryUserByToten(String toten){
        String url = propertieService.TAOTAO_SSO_URL + "/service/user/" + toten;
        try {
            String jsonData = this.apiService.doGet(url);
            if(!StringUtils.isEmpty(jsonData)){
                return MAPPER.readValue(jsonData,User.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
