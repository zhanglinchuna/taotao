package com.taotao.sso.controller;

import com.taotao.sso.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping("user")
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 注册页面
     * @return
     */
    @RequestMapping(value = "register",method = RequestMethod.GET)
    public String toRegister(){
        return "register";
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
}
