package com.jin.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jin.domain.Setmeal;
import com.jin.domain.SetmealDish;
import com.jin.dto.SetmealDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SetmealDishdao extends BaseMapper<SetmealDish> {
}
