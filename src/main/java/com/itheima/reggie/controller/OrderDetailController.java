package com.itheima.reggie.controller;

import com.itheima.reggie.service.OrderDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: wx
 * @Date: 2023/7/24 18:57
 */
@RestController
@RequestMapping("/")
@Slf4j
public class OrderDetailController {
    @Autowired
    public OrderDetailService orderDetailService;
}
