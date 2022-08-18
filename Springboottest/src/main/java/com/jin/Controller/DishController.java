package com.jin.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jin.Controller.util.R;
import com.jin.Service.CategoryService;
import com.jin.Service.DishFlavorService;
import com.jin.Service.DishService;
import com.jin.domain.Category;
import com.jin.domain.Dish;
import com.jin.domain.DishFlavor;
import com.jin.dto.DishDto;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("dish")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private CategoryService categoryService;
    @PostMapping
    public R<String> saveDishandflavor(@RequestBody DishDto dishDto){
       dishService.saveDishandflavor(dishDto);
       return  R.success("菜品保存成功");
    }
    @GetMapping("page")
    public R<Page> listpage( int page,int pageSize,Dish dish){
        Page<Dish> dishPage=new Page<>(page,pageSize);
        Page<DishDto> dishDtoPage =new Page<>();
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(Strings.isNotEmpty(dish.getName()),Dish::getName,dish.getName());
        wrapper.orderByDesc(Dish::getUpdateTime);
        dishService.page(dishPage,wrapper);
        BeanUtils.copyProperties(dishPage,dishDtoPage);
        List<Dish> records = dishPage.getRecords();
        List<DishDto> collect = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Long categoryId = item.getCategoryId();
            Category byId = categoryService.getById(categoryId);
            if (byId != null) {
                String name = byId.getName();
                dishDto.setCategoryName(name);
            }
            return dishDto;
        }).collect(Collectors.toList());
        dishDtoPage.setRecords(collect);
        return R.success(dishDtoPage);

    }
    @GetMapping("/{id}")
    public R<DishDto> DishwithFlavor(@PathVariable Long id){
        DishDto dishDto = dishService.updataDishwithFlavbor(id);
        return R.success(dishDto);
    }
    @PutMapping
    public R<String> updata(@RequestBody DishDto dishDto){
        dishService.updataDishandflavor(dishDto);
        return R.success("更新成功");
    }
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish){
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(dish.getCategoryId() !=null,Dish::getCategoryId,dish.getCategoryId());
        wrapper.eq(Dish::getStatus,1);
        wrapper.orderByAsc(Dish::getSort).orderByAsc(Dish::getCreateTime);
        List<Dish> list = dishService.list(wrapper);
        List<DishDto> collect = list.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Long categoryId = item.getCategoryId();
            Category byId = categoryService.getById(categoryId);
            if (byId != null) {
                String name = byId.getName();
                dishDto.setCategoryName(name);
            }

            Long id = item.getId();
            LambdaQueryWrapper<DishFlavor> wrapper1 = new LambdaQueryWrapper<>();
            wrapper1.eq(DishFlavor::getDishId,id);
            List<DishFlavor> list1 = dishFlavorService.list(wrapper1);
            dishDto.setFlavors(list1);
            return dishDto;
        }).collect(Collectors.toList());

        return R.success(collect);

    }
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        dishService.deleteWithFlavor(ids);
        return R.success("删除成功");
    }
    @PostMapping("/status/{o}")
    public R<String> stop(@PathVariable int o,@RequestParam List<Long> ids){
       dishService.startandstop(o,ids);

        return R.success("更新成功");
    }
  /*  @PostMapping("/status")
    public R<String> Steat(@RequestParam List<Long> ids){
        dishService.start(ids);

        return R.success("更新成功");
    }*/



}
