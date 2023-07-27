package com.itheima.reggie.common;

/**
 * @Description:基于ThreaaLocal工具类，用于保存和获取当前登录用户id
 * @Author: wx
 * @Date: 2023/7/17 20:34
 */
public class BaseContext {
    public static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }

    public static Long getCurrentId(){
        return threadLocal.get();
    }
}
