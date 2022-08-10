package com.jin.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jin.domain.book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class testbookdao {
    @Autowired
    private  bookdao bookdao;
    @Test
    public void test(){
        book book = bookdao.getbyId(2);
        System.out.println(book);
    }
    /*
    * 添加操作
    * */
    @Test
    public void test1(){
        book book = new book();
        book.setType("ittyu");
        book.setName("zhangsan");
        book.setDescription("他是一个大笨蛋");
        bookdao.insert(book);
    }
    @Test
    public void test2(){/*  更新操作 */
        book book = new book();
        book.setId(6);
        book.setType("lichen");
        book.setName("chulian");
        book.setDescription("她一个机灵鬼");
        bookdao.updateById(book);
    }
    @Test
    public void test3(){/*  删除 */
       bookdao.deleteById(5);
    }
    @Test
    public void test4(){/*  查询操作 */

        List<book> books = bookdao.selectList(null);
        System.out.println(books);
    }
    @Test
    public void test5(){/*  更新操作 */
        IPage iPage=new Page(2,2);
        /*QueryWrapper<book> qw=new QueryWrapper<>();*/
        LambdaQueryWrapper<book> qw = new LambdaQueryWrapper<book>();
        qw.like(true,book::getName,"spring");
        IPage iPage1 = bookdao.selectPage(iPage, qw);
        System.out.println(iPage.getRecords());
        System.out.println(iPage.getPages());
        System.out.println(iPage.getTotal());
        System.out.println(iPage.getCurrent());

    }
}
