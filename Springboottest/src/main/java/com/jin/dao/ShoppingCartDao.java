package com.jin.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jin.domain.ShoppingCart;
import com.jin.domain.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ShoppingCartDao extends BaseMapper<ShoppingCart> {
}
