package com.jin.Service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jin.Controller.util.myexception;
import com.jin.Service.CategoryService;
import com.jin.Service.DishService;
import com.jin.Service.EmpoyeeService;
import com.jin.Service.SetmealService;
import com.jin.dao.Categorydao;
import com.jin.dao.Employeedao;
import com.jin.domain.Category;
import com.jin.domain.Dish;
import com.jin.domain.Employee;
import com.jin.domain.Setmeal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Categoryserviceimpl extends ServiceImpl<Categorydao, Category> implements CategoryService {

    @Autowired
    private SetmealService setmealService;
    @Autowired
    private DishService dishService;
    @Override
    public void remove(Long ids) {

        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<Dish>();
        queryWrapper.eq(Dish::getCategoryId,ids);
        int count = dishService.count(queryWrapper);
        if (count>0){
            throw new myexception("不能删除");
        }
        LambdaQueryWrapper<Setmeal> queryWrapper1 = new LambdaQueryWrapper<Setmeal>();
        queryWrapper1.eq(Setmeal::getCategoryId,ids);
        int count1 = setmealService.count(queryWrapper1);
        if (count1>0){
            throw new myexception("不能删除");
        }
        super.removeById(ids);

    }
}
