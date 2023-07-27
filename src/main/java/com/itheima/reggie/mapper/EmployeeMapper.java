package com.itheima.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.reggie.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description:
 * @Author: wx
 * @Date: 2023/7/11 11:20
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
