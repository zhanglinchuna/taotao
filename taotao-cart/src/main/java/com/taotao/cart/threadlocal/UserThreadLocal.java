package com.taotao.cart.threadlocal;

import com.taotao.cart.bean.User;

public class UserThreadLocal {
    private static final ThreadLocal<User> LOCAL = new ThreadLocal<>();
    public static void set(User user){
        LOCAL.set(user);
    }
    public static User get(){
        return LOCAL.get();
    }
}
