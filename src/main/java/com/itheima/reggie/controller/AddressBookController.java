package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.AddressBook;
import com.itheima.reggie.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description:地址簿管理
 * @Author: wx
 * @Date: 2023/7/23 13:00
 */
@RestController
@RequestMapping("/addressBook")
@Slf4j
public class AddressBookController {
    @Autowired
    public AddressBookService addressBookService;

    /**
     * 新增
     *
     * @param addressBook
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody AddressBook addressBook) {
        log.info("addressBook:{}", addressBook);

        addressBook.setUserId(BaseContext.getCurrentId());
        addressBookService.save(addressBook);

        return R.success("添加成功");
    }


    /**
     * 查询指定用户的地址
     *
     * @param addressBook
     * @return
     */
    @GetMapping("/list")
    public R<List<AddressBook>> list(AddressBook addressBook) {
        log.info("addressBook:{}", addressBook);

        addressBook.setUserId(BaseContext.getCurrentId());

        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(addressBook.getUserId() != null, AddressBook::getUserId, addressBook.getUserId());
        queryWrapper.orderByDesc(AddressBook::getUpdateTime);

        List<AddressBook> list = addressBookService.list(queryWrapper);

        return R.success(list);
    }

    /**
     * 设置默认地址
     * @param addressBook
     * @return
     */
    @PutMapping("/default")
    public R<String> setDefault(@RequestBody AddressBook addressBook){
        log.info("address:{}",addressBook);

        //先将所有地址取消默认地址选项
        LambdaUpdateWrapper<AddressBook> updateWrapper = new LambdaUpdateWrapper<>();

        updateWrapper.eq(AddressBook::getUserId,BaseContext.getCurrentId());
        updateWrapper.set(AddressBook::getIsDefault,0);

        addressBookService.update(updateWrapper);

        //再将当前地址设为默认地址
        addressBook.setIsDefault(1);
        addressBookService.updateById(addressBook);

        return R.success("设置默认地址成功");
    }

    /**
     * 根据id查询对象地址
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<AddressBook> get(@PathVariable Long id){
        log.info("id:{}",id);
        AddressBook addressBook = addressBookService.getById(id);
        if(addressBook!=null){
            return R.success(addressBook);
        }else{
            return R.error("没有找到该对象");
        }
    }

    @PutMapping
    public R<String> update(@RequestBody AddressBook addressBook){
        Long id = BaseContext.getCurrentId();
        log.info("当前用户id为：{}",id);

        addressBookService.updateById(addressBook);

        return R.success("修改成功");
    }

    @DeleteMapping
    public R<String> delete(@RequestParam Long ids){
        addressBookService.removeById(ids);

        return R.success("删除成功");
    }

    @GetMapping("/default")
    public R<AddressBook> defaultAddressBook(){
        Long currentId = BaseContext.getCurrentId();
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(AddressBook::getUserId,currentId);
        queryWrapper.eq(AddressBook::getIsDefault,1);

        AddressBook one = addressBookService.getOne(queryWrapper);

        if(one!=null){
            return R.success(one);
        }else{
            return R.error("没有找到该对象");
        }
    }
}



