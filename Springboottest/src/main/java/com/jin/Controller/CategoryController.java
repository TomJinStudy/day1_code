package com.jin.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jin.Controller.util.R;
import com.jin.Service.CategoryService;
import com.jin.domain.Category;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
   @PostMapping
    public R<String> save(@RequestBody Category category){
        categoryService.save(category);
        return R.success("添加成功");
    }
    @GetMapping("/page")
    public R<Page> fenye1(int page, int pageSize){
        Page<Category> employeePage = new Page<Category>(page, pageSize);
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Category::getSort);
        categoryService.page(employeePage, queryWrapper);
        return R.success(employeePage);
    }
  @DeleteMapping
  public  R<String> remove(Long ids){
      categoryService.remove(ids);
       return R.success("删除成功");
  }

 @PutMapping
    public  R<String> update(@RequestBody Category category){
       categoryService.updateById(category);
       return R.success("修改成功");
 }

    /*@DeleteMapping
    public R<String> delete(Long id){
       categoryService.removeById(id);
       return R.success("删除成功");
    }*/
    @GetMapping("list")
    public R<List<Category>> list(Category category){
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(category.getType()!=null, Category::getType,category.getType());
        wrapper.orderByDesc(Category::getUpdateTime);
        List<Category> list = categoryService.list(wrapper);
        return R.success(list);

    }
}
