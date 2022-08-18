package com.jin.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jin.domain.Category;
import com.jin.domain.Dish;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface Dishdao extends BaseMapper<Dish> {
}
