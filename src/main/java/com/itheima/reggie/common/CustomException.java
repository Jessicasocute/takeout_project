package com.itheima.reggie.common;

/**
 * @Description:自定义业务异常类
 * @Author: wx
 * @Date: 2023/7/18 10:19
 */
public class CustomException extends RuntimeException{
    public CustomException (String message){
        super(message);
    }
}
