package com.taotao.web.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.service.ApiService;
import com.taotao.web.bean.Cart;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class CartService {

    @Autowired
    private ApiService apiService;

    @Value("${TAOTAO_CART_UTL}")
    private String TAOTAO_CART_UTL;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public List<Cart> queryCartListByUserId(Long userId) {
        // 查询购物车系统提供的接口获取购物车列表
        try {
            String url = TAOTAO_CART_UTL + "/service/cart?userId=" + userId;
            String jsonData = this.apiService.doGet(url);
            if (StringUtils.isEmpty(jsonData)){
                return null;
            }
            return MAPPER.readValue(jsonData,MAPPER.getTypeFactory().constructCollectionType(List.class,Cart.class));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
