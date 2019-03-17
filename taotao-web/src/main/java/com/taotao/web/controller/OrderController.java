package com.taotao.web.controller;

import com.taotao.web.bean.Cart;
import com.taotao.web.bean.Item;
import com.taotao.web.bean.Order;
import com.taotao.web.bean.User;
import com.taotao.web.service.CartService;
import com.taotao.web.service.ItemService;
import com.taotao.web.service.OrderService;
import com.taotao.web.threadlocal.UserThreadLocal;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("order")
public class OrderController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartService cartService;

    /**
     * 去订单确认页
     * @param itemId
     * @return
     */
    @RequestMapping(value = "{itemId}",method = RequestMethod.GET)
    public ModelAndView toOrder(@PathVariable Long itemId){
        ModelAndView mv = new ModelAndView();
        Item item = this.itemService.queryItemById(itemId);
        mv.setViewName("order");
        mv.addObject("item",item);
        return mv;
    }

    /**
     * 基于购物车下单
     * @return
     */
    @RequestMapping(value = "create",method = RequestMethod.GET)
    public ModelAndView toCartOrder(){
        ModelAndView mv = new ModelAndView("order-cart");
        User user = UserThreadLocal.get();
        List<Cart> carts = this.cartService.queryCartListByUserId(user.getId());
        mv.addObject("carts",carts);
        return mv;
    }

    @RequestMapping(value = "submit",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> submitOrder(Order order){
        Map<String, Object> result = new HashMap<>();
        String orderId = this.orderService.submitOrder(order);
        if(StringUtils.isEmpty(orderId)){
            // 订单提交失败
            result.put("status",300);
        }else {
            // 订单提交成功
            result.put("status",200);
            result.put("data",orderId);
        }
        return result;
    }

    @RequestMapping(value = "success",method = RequestMethod.GET)
    public ModelAndView success(@RequestParam("id")String orderId){
        ModelAndView mv = new ModelAndView("success");
        Order order = this.orderService.queryOrderById(orderId);
        mv.addObject("order",order);
        // 当前时间先后推2天，格式化 01月05日
        mv.addObject("date",new DateTime().plusDays(2).toString("MM月dd日"));
        return mv;
    }
}
