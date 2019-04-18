package com.taotao.web.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.httpclient.HttpResult;
import com.taotao.common.service.ApiService;
import com.taotao.pojo.Item;
import com.taotao.web.bean.Order;
import com.taotao.web.bean.OrderItem;
import com.taotao.web.bean.User;
import com.taotao.web.threadlocal.UserThreadLocal;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class OrderService {
    @Autowired
    private ApiService apiService;

    @Value("${TAOTAO_ORDER_URL}")
    private String TAOTAO_ORDER_URL;

    @Value("${TAOTAO_MANAGE_URL}")
    private String TAOTAO_MANAGE_URL;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public String submitOrder(Order order){
        User user = UserThreadLocal.get();//从当前线程中获取user对象
        order.setUserId(user.getId());
        order.setBuyerNick(user.getUsername());

        boolean orderOff = true;
        // 获取订单中的商品数量，修改后台商品库存数量
        String update_url = TAOTAO_MANAGE_URL + "/rest/item/updateItemNum";
        List<OrderItem> orderItems = order.getOrderItems();
        for (OrderItem orderItem:orderItems){
            // 商品id
            Long itemId = orderItem.getItemId();
            // 商品数量
            Integer num = orderItem.getNum();
            String select_url = TAOTAO_MANAGE_URL + "/rest/item/" + itemId;
            try {
                String itemStr = this.apiService.doGet(select_url);
                // 反序列化时忽略json中多余的字段
                MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                Item item = MAPPER.readValue(itemStr, Item.class);
                // 后台商品库存数量
                Integer numSum = item.getNum();

                if (numSum > num){
                    item.setNum(numSum - num);
                    item.setUpdated(new Date());
                    HttpResult httpResult = this.apiService.doPostJson(update_url, MAPPER.writeValueAsString(item));
                    if(httpResult.getCode().intValue() != 201){
                        orderOff = false;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 订单系统生成订单
        String url = TAOTAO_ORDER_URL + "/order/create";
        if(orderOff){
            try {
                HttpResult httpResult = this.apiService.doPostJson(url, MAPPER.writeValueAsString(order));
                if(httpResult.getCode().intValue() == 200){
                    String jsonData = httpResult.getData();
                    JsonNode jsonNode = MAPPER.readTree(jsonData);
                    if(jsonNode.get("status").intValue() == 200){
                        // 订单提交成功，返回订单号
                        return jsonNode.get("data").asText();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public Order queryOrderById(String orderId){
        String url = TAOTAO_ORDER_URL + "/order/query/" + orderId;
        try {
            String jsonData = this.apiService.doGet(url);
            if(StringUtils.isNotEmpty(jsonData)){
                return MAPPER.readValue(jsonData,Order.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
