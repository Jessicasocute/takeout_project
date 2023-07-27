package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Orders;
import com.itheima.reggie.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @Description:
 * @Author: wx
 * @Date: 2023/7/24 18:52
 */
@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {
    @Autowired
    public OrderService orderService;

    @GetMapping("/userPage")
    public R<Page> page(int page,int pageSize){
        log.info("page:{},pageSize:{}",page,pageSize);

        Page<Orders> pageInfo = new Page<>(page,pageSize);

        Long currentId = BaseContext.getCurrentId();
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(Orders::getUserId,currentId);

        orderService.page(pageInfo,queryWrapper);

        return R.success(pageInfo);

    }

    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        log.info("orders:{}",orders);

        orderService.submit(orders);

        return R.success("下单成功");
    }
}
