package com.jin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jin.domain.book;

import java.util.List;

public interface bookservice extends IService<book> {
    boolean save(book book);

    boolean update(book book);

    boolean delete(Integer id);

    book getbyId(Integer id);

    List<book> getall();

    IPage fenpage(Integer current, Integer size, book book);

    IPage fenpage(Integer current, Integer size);
}
