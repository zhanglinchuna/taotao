package com.taotao.web.handlerInterceptor;

import com.taotao.common.utils.CookieUtils;
import com.taotao.web.bean.User;
import com.taotao.web.service.PropertieService;
import com.taotao.web.service.UserService;
import com.taotao.web.threadlocal.UserThreadLocal;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserLoginHandlerInterceptor implements HandlerInterceptor {

    public static final String COOKIE_NAME = "TT_TOKEN";

    @Autowired
    private PropertieService propertieService;

    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        UserThreadLocal.set(null); // 清空当前线程的user对象
        String loginUrl = propertieService.TAOTAO_SSO_URL + "/user/login.html";
        String token = CookieUtils.getCookieValue(httpServletRequest, COOKIE_NAME);
        if(StringUtils.isEmpty(token)){
            // 未登入状态
            httpServletResponse.sendRedirect(loginUrl);
            return false;
        }
        User user = userService.queryUserByToten(token);
        if(null == user){
            // 未登入状态
            httpServletResponse.sendRedirect(loginUrl);
            return false;
        }
        UserThreadLocal.set(user);//将user对象存放到当前线程中
        // 处于登入状态
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
