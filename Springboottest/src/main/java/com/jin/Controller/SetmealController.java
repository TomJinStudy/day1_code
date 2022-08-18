package com.jin.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jin.Controller.util.R;
import com.jin.Service.CategoryService;
import com.jin.Service.SetmealDishService;
import com.jin.Service.SetmealService;
import com.jin.domain.Category;
import com.jin.domain.Setmeal;
import com.jin.domain.SetmealDish;
import com.jin.dto.SetmealDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("setmeal")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private SetmealDishService setmealDishService;
    @Autowired
    private CategoryService categoryService;
     @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){

        setmealService.saveSetmealandDish(setmealDto);

         return R.success("保存成功");
     }
     @GetMapping("page")
    public R<Page>  fenpage(int page,int pageSize,String name){
         Page<Setmeal> page1=new Page<>(page,pageSize);
         Page<SetmealDto> page2=new Page<>();
         LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
         wrapper.like(name!=null,Setmeal::getName,name);
         setmealService.page(page1,wrapper);
         BeanUtils.copyProperties(page1,page2,"records");
         List<Setmeal> records = page1.getRecords();
         List<SetmealDto> collect = records.stream().map((item) -> {
             SetmealDto setmealDto = new SetmealDto();
             BeanUtils.copyProperties(item, setmealDto);
             Long categoryId = item.getCategoryId();
             Category byId = categoryService.getById(categoryId);
             if (byId != null) {
                 String name1 = byId.getName();
                 setmealDto.setCategoryName(name1);
             }
             return setmealDto;
         }).collect(Collectors.toList());
            page2.setRecords(collect);
         return R.success(page2);

     }
     @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        setmealService.deleteWithDish(ids);
        return R.success("删除成功");
     }
     @PostMapping("/status/{o}")
    public  R<String> startandstop(@PathVariable int o,@RequestParam List<Long> ids){
         setmealService.startandstop(o,ids);
         return  R.success("修改成功");
     }
     @GetMapping("/{id}")
    public R<SetmealDto> huiyan(@PathVariable Long id){
         SetmealDto setmealDto = new SetmealDto();
         Setmeal byId = setmealService.getById(id);
         BeanUtils.copyProperties(byId,setmealDto);
         LambdaQueryWrapper<SetmealDish> wrapper = new LambdaQueryWrapper<>();
         wrapper.eq(SetmealDish::getSetmealId,id);
         List<SetmealDish> list = setmealDishService.list(wrapper);
         setmealDto.setSetmealDishes(list);
         return R.success(setmealDto);

     }
     @PutMapping
    public R<String> updata(@RequestBody SetmealDto setmealDto){
         setmealService.updataDishandSetmeal(setmealDto);
         return  R.success("更新成功");
     }
    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal){
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(setmeal.getCategoryId() !=null,Setmeal::getCategoryId,setmeal.getCategoryId());
        wrapper.eq(setmeal.getStatus()!=null,Setmeal::getStatus,setmeal.getStatus());
        wrapper.orderByAsc(Setmeal::getUpdateTime);
        List<Setmeal> list = setmealService.list(wrapper);
        return R.success(list);

    }

}
