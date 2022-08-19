package com.jin.Service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jin.Controller.util.Basecontext;
import com.jin.Controller.util.myexception;
import com.jin.Service.DishService;
import com.jin.Service.SetmealDishService;
import com.jin.Service.SetmealService;
import com.jin.dao.Dishdao;
import com.jin.dao.Setmealdao;
import com.jin.domain.Dish;
import com.jin.domain.Setmeal;
import com.jin.domain.SetmealDish;
import com.jin.dto.SetmealDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class Setmealserviceimpl extends ServiceImpl<Setmealdao, Setmeal> implements SetmealService {
    @Autowired
    private SetmealDishService setmealDishService;
    @Override
    public void saveSetmealandDish(SetmealDto setmealDto) {
        this.save(setmealDto);
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        Long id = setmealDto.getId();
        for (SetmealDish temp:setmealDishes) {
            temp.setSetmealId(id);
        }
        setmealDishService.saveBatch(setmealDishes);

    }

    @Override
    public void deleteWithDish(List<Long> ids) {
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(Setmeal::getId,ids);
        wrapper.eq(Setmeal::getStatus,1);
        int count = this.count(wrapper);
        if(count>0){
          throw new myexception("删除异常请重试");
        }
       this.removeByIds(ids);
        LambdaQueryWrapper<SetmealDish> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(SetmealDish::getSetmealId,ids);
        setmealDishService.remove(wrapper1);
    }

    @Override
    public void startandstop(int o, List<Long> ids) {
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(Setmeal::getId,ids);
        wrapper.eq(Setmeal::getStatus,o);
        int count = this.count(wrapper);
        if (count==0){
            LambdaQueryWrapper<Setmeal> wrapper1 = new LambdaQueryWrapper<>();
            wrapper1.in(Setmeal::getId,ids);
            List<Setmeal> list = this.list(wrapper1);
            List<Setmeal> collect = list.stream().map((item) -> {
                item.setStatus(o);
                return item;
            }).collect(Collectors.toList());
            this.updateBatchById(collect);
        }else {
            throw new myexception("操作异常请重试");
        }
    }

    @Override
    public void updataDishandSetmeal(SetmealDto setmealDto) {
       this.updateById(setmealDto);
        Long id = setmealDto.getId();
        LambdaQueryWrapper<SetmealDish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SetmealDish::getSetmealId,id);
        setmealDishService.remove(wrapper);//删除菜单
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        for (SetmealDish temp:setmealDishes) {
            temp.setSetmealId(id);
        }
        setmealDishService.saveBatch(setmealDishes);


    }

}
