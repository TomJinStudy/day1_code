package com.jin.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jin.domain.Dish;
import com.jin.domain.DishFlavor;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DishFlavordao extends BaseMapper<DishFlavor> {
}
