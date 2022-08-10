package com.jin.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jin.domain.book;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface bookdao extends BaseMapper<book> {

    @Select("select * from book where id=#{id}")
    public book getbyId(Integer id);
}
