package com.jin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jin.domain.book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class testservice {
    @Autowired
    private  bookservice bookservice;
    @Test
    public void test(){
        book book = bookservice.getbyId(2);
        System.out.println(book);
    }
    /*
     * 添加操作
     * */
    @Test
    public  void  test9(){

        book book = new book();

        book.setType("ittyu11");
        book.setName("zhangsan11");
        book.setDescription("他是个大笨蛋");
        bookservice.save(book);
    }
    @Test
    public void test1(){
        book book = new book();

        book.setType("ittyu11");
        book.setName("zhangsan11");
        book.setDescription("他是个大笨蛋");
        bookservice.save(book);
    }
    @Test
    public void test2(){/*  更新操作 */
        book book = new book();
        book.setId(6);
        book.setType("lichen");
        book.setName("chulian");
        book.setDescription("她一个机灵鬼");
        bookservice.update(book);
    }
    @Test
    public void test3(){/*  删除 */
        bookservice.delete(5);
    }
    @Test
    public void test4(){/*  查询操作 */

        List<book> books = bookservice.getall();
        System.out.println(books);
    }
    @Test
    public void test5(){/*  更新操作 */

        IPage iPage = bookservice.fenpage(2, 2, null);
        System.out.println(iPage.getRecords());
        System.out.println(iPage.getPages());
        System.out.println(iPage.getTotal());
        System.out.println(iPage.getCurrent());

    }


}
