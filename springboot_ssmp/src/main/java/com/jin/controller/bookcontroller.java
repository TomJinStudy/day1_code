package com.jin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jin.controller.utils.R;
import com.jin.domain.book;
import com.jin.service.bookservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("books")
public class bookcontroller {
    @Autowired
    private bookservice bookservice;
          @PostMapping
          public R save(@RequestBody book book){
              boolean save = bookservice.save(book);

              return new R(save,save ?"添加成功":"添加失败" );
          }
          @PutMapping
           public R update(@RequestBody book book){
              boolean update = bookservice.update(book);
              return new R(update,update?"更新成功":"更新失败" );
           }
         @DeleteMapping("{id}")
          public R delete(@PathVariable Integer id){
             boolean delete = bookservice.delete(id);
             return  new R(delete,delete?"删除成功":"删除失败");
          }
        @GetMapping("{id}")
           public R getbyId(@PathVariable Integer id){
              return  new R( true,bookservice.getbyId(id));
           }
        @GetMapping
          public   R getall(){

              return  new R( true,bookservice.getall());
          }
     /*     @GetMapping("{current}/{size}")
         public R getPage(@PathVariable int current,@PathVariable int size){
              IPage fenpage = bookservice.fenpage(current, size);
              if(current>fenpage.getPages()){
                   fenpage = bookservice.fenpage((int) fenpage.getPages(), size);
              }
               return new R( true,fenpage);
          }*/
    @GetMapping("{current}/{size}")
    public R getPage(@PathVariable int current,@PathVariable int size, book book){

        IPage fenpage = bookservice.fenpage(current, size,book);
        if(current>fenpage.getPages()){
            fenpage = bookservice.fenpage((int) fenpage.getPages(), size,book);
        }
        return new R( true,fenpage);
    }

}
