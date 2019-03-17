package com.taotao.cart.handlerInterceptor;

import com.taotao.common.utils.CookieUtils;
import com.taotao.cart.bean.User;
import com.taotao.cart.service.PropertieService;
import com.taotao.cart.service.UserService;
import com.taotao.cart.threadlocal.UserThreadLocal;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserHandlerInterceptor implements HandlerInterceptor {

    public static final String COOKIE_NAME = "TT_TOKEN";

    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {

        UserThreadLocal.set(null); // 清空当前线程的user对象

        String token = CookieUtils.getCookieValue(httpServletRequest, COOKIE_NAME);
        if(StringUtils.isEmpty(token)){
            // 未登入状态，放行
            return true;
        }
        User user = userService.queryUserByToten(token);
        if(null == user){
            // 未登入状态，放行
            return true;
        }
        // 处于登入状态
        UserThreadLocal.set(user);//将user对象存放到当前线程中
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
