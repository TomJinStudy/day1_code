package com.jin.Service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jin.Controller.util.R;
import com.jin.Controller.util.myexception;
import com.jin.Service.DishFlavorService;
import com.jin.Service.DishService;
import com.jin.Service.EmpoyeeService;
import com.jin.dao.Dishdao;
import com.jin.dao.Employeedao;
import com.jin.domain.Dish;
import com.jin.domain.DishFlavor;
import com.jin.domain.Employee;
import com.jin.dto.DishDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
@Transactional
public class Dishserviceimpl extends ServiceImpl<Dishdao, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;

    @Override
    public void saveDishandflavor(DishDto dishDto) {
        this.save(dishDto);
        Long id = dishDto.getId();
        List<DishFlavor> flavors = dishDto.getFlavors();
        for (DishFlavor temp:flavors) {
            temp.setDishId(id);
        }
        dishFlavorService.saveBatch(flavors);
    }

    @Override
    public DishDto updataDishwithFlavbor(Long id) {
        DishDto dishDto = new DishDto();
        Dish dish = this.getById(id);

        BeanUtils.copyProperties(dish,dishDto);
        LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(DishFlavor::getDishId, dish.getId());
        List<DishFlavor> list = dishFlavorService.list(wrapper);
        dishDto.setFlavors(list);

        return dishDto;

    }

    @Override
    public void updataDishandflavor(DishDto dishDto) {
        this.updateById(dishDto);
        LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(dishFlavorLambdaQueryWrapper);
        Long id = dishDto.getId();
        List<DishFlavor> flavors = dishDto.getFlavors();
        for (DishFlavor temp:flavors) {
            temp.setDishId(id);
        }
        dishFlavorService.saveBatch(flavors);

    }

    @Override
    public void deleteWithFlavor(List<Long> ids) {
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Dish::getStatus,1);
        wrapper.in(Dish::getId,ids);
        int count1 = super.count(wrapper);
        if(count1>0){
            throw new myexception("删除异常请重试");
        }
        this.removeByIds(ids);
        LambdaQueryWrapper<DishFlavor> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(DishFlavor::getDishId,ids);
        dishFlavorService.remove(wrapper1);
    }

    @Override
    public void startandstop(int o, List<Long> ids) {
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(Dish::getId,ids);
        wrapper.eq(Dish::getStatus,o);
        int count = this.count(wrapper);
        if (count==0){
            LambdaQueryWrapper<Dish> wrapper1 = new LambdaQueryWrapper<>();
            wrapper1.in(Dish::getId,ids);
            List<Dish> list1 = this.list(wrapper1);
            for (Dish temp:list1) {
                temp.setStatus(o);
                this.updateById(temp);}
        } else {
            throw new myexception("修改异常请重试");
        }
    }



   /* @Override
    public void start(int o,List<Long> ids) {
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(Dish::getId,ids);
        wrapper.eq(Dish::getStatus,0);
        int count = this.count(wrapper);
        if (count==ids.size()){
            List<Dish> list1 = this.list(wrapper);
            for (Dish temp:list1) {
                temp.setStatus(1);
                this.updateById(temp);}
        }else if(count==0){
            List<Dish> list = this.list(wrapper);
            for (Dish temp:list) {
                temp.setStatus(0);
                this.updateById(temp);
            }
        }else {
            throw new myexception("修改异常请重试");
        }
    }

*/
   /* */
}
