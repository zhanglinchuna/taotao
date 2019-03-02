package com.taotao.sso.controller;

import com.taotao.common.utils.CookieUtils;
import com.taotao.sso.pojo.User;
import com.taotao.sso.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("user")
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    private static final String COOKIE_NAME = "TT_TOKEN";

    /**
     * 注册页面
     * @return
     */
    @RequestMapping(value = "register",method = RequestMethod.GET)
    public String toRegister(){
        return "register";
    }

    /**
     * 登入页面
     * @return
     */
    @RequestMapping(value = "login",method = RequestMethod.GET)
    public String toLogin(){
       return "login";
    }
    /**
     * 检测用户注册数据是否可用
     * @param param
     * @param type
     * @return
     */
    @RequestMapping(value = "check/{param}/{type}",method = RequestMethod.GET)
    public ResponseEntity<Boolean> check(@PathVariable("param")String param,
                                         @PathVariable("type")Integer type){
        try {
            Boolean bool = this.userService.check(param,type);
            if(null == bool){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
            // 为了兼容前端的业务逻辑
            return ResponseEntity.ok(!bool);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     * 注册
     * @param user
     * @return
     */
    @RequestMapping(value = "doRegister",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> doRegister(@Valid User user, BindingResult bindingResult){
        Map<String, Object> result = new HashMap<>();
        // 注册功能提交参数校验
        if(bindingResult.hasErrors()){
            // 校验有错误
            List<String> msgs = new ArrayList<>();
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            for (ObjectError objectError:allErrors){
                String msg = objectError.getDefaultMessage();
                msgs.add(msg);
            }
            result.put("status","400");
            result.put("data", StringUtils.join(msgs,"|"));
            return result;
        }
        Boolean bool = this.userService.saveUser(user);
        if(bool){
            // 注册成功
            result.put("status","200");
        }else {
            // 注册失败
            result.put("status","500");
            result.put("data","注册失败！");
        }
        return result;
    }

    /**
     * 登入
     * @param username
     * @param password
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "doLogin",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> doLogin(@RequestParam("username") String username,
                                      @RequestParam("password") String password,
                                      HttpServletRequest request, HttpServletResponse response){
        Map<String,Object> result = new HashMap<>();
        try {
            String token = this.userService.doLogin(username, password);
            if(null == token){
                // 登入失败
                result.put("status",400);
            }else{
                // 登入成功，需要将token写入到cookie中
                result.put("status",200);
                CookieUtils.setCookie(request,response,COOKIE_NAME,token);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 首页显示登入信息
     * @param token
     * @return
     */
    @RequestMapping(value = "{token}",method = RequestMethod.GET)
    public ResponseEntity<User> queryUserByToken(@PathVariable("token") String token){
        try {
            User user = this.userService.queryUserByToken(token);
            if(null == user){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
