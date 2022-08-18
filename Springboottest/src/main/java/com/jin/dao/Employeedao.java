package com.jin.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jin.domain.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface Employeedao extends BaseMapper<Employee> {
}
