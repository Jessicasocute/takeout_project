package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.ShoppingCart;
import com.itheima.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Description:
 * @Author: wx
 * @Date: 2023/7/23 16:40
 */
@RestController
@RequestMapping("/shoppingCart")
@Slf4j
public class ShoppingCartController {
    @Autowired
    public ShoppingCartService shoppingCartService;

    /**
     * 查看购物车
     * @return
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){
        Long currentId = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,currentId);
        queryWrapper.orderByAsc(ShoppingCart::getCreateTime);

        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);
        return R.success(list);
    }

    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody  ShoppingCart shoppingCart){
        log.info("购物车信息：{}",shoppingCart);
        //设置当前用户id
        shoppingCart.setUserId(BaseContext.getCurrentId());

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();

        if(shoppingCart.getDishId()!=null){
            //添加到购物车的是菜品
            queryWrapper.eq(ShoppingCart::getDishId,shoppingCart.getDishId());
        }else{
            //添加到购物车的是套餐
            queryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }
        //查询当前菜品或者套餐是否在购物车中
        ShoppingCart one = shoppingCartService.getOne(queryWrapper);

        if(one!=null){
            //如果已经存在，就在原来数量基础上加一
            one.setNumber(one.getNumber()+1);
            shoppingCartService.updateById(one);
        }else{
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            one = shoppingCart;
        }
        return R.success(one);
    }

    @PostMapping("/sub")
    public R<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart){
        log.info("购物车信息：{}",shoppingCart);

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();

        if(shoppingCart.getDishId()!=null){
            //当前购物车商品为菜品
            queryWrapper.eq(ShoppingCart::getDishId,shoppingCart.getDishId());
        }else{
            //当前购物车商品为套餐
            queryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }
        //查询当前商品
        ShoppingCart one = shoppingCartService.getOne(queryWrapper);

        if(one.getNumber()==0){
            //当前商品数量为0，在购物车中删掉该商品
            shoppingCartService.removeById(one.getId());
        }
        if(one.getNumber()>0){
            //只有当商品数量大于0才可以减
            one.setNumber(one.getNumber()-1);
            shoppingCartService.updateById(one);
        }

        return R.success(one);
    }

    @DeleteMapping("/clean")
    public R<String> clean(){
        //获取当前用户id
        Long currentId = BaseContext.getCurrentId();

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,currentId);

        shoppingCartService.remove(queryWrapper);

        return R.success("清空购物车成功");

    }
}
