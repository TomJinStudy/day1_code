package com.jin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jin.dao.bookdao;
import com.jin.domain.book;
import com.jin.service.bookservice;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class bookserviceimpl extends ServiceImpl<bookdao,book> implements bookservice {
@Autowired
    private bookdao bookdao;
    @Override
    public boolean save(book book) {
        bookdao.insert(book);
        return true;
    }

    @Override
    public IPage fenpage(Integer current, Integer size) {
        IPage iPage=new Page(current,size);
        return bookdao.selectPage(iPage,null);
    }

    @Override
    public IPage fenpage(Integer current, Integer size, book book) {
        LambdaQueryWrapper<book> qw=new LambdaQueryWrapper<book>();
        qw.like(Strings.isNotEmpty(book.getType()), com.jin.domain.book::getType,book.getType());
        qw.like(Strings.isNotEmpty(book.getName()), com.jin.domain.book::getName,book.getName());
        qw.like(Strings.isNotEmpty(book.getDescription()), com.jin.domain.book::getDescription,book.getDescription());
        IPage iPage=new Page(current,size);
        return bookdao.selectPage(iPage,qw);
    }

    @Override
    public boolean update(book book) {

        bookdao.updateById(book);
        return true;
    }

    @Override
    public boolean delete(Integer id) {
        return bookdao.deleteById(id)>0;
    }

    @Override
    public book getbyId(Integer id) {
        return bookdao.selectById(id);
    }

    @Override
    public List<book> getall() {
        return bookdao.selectList(null);
    }
}
