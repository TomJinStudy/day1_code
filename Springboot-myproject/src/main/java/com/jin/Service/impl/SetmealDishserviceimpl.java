package com.jin.Service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jin.Service.SetmealDishService;
import com.jin.Service.SetmealService;
import com.jin.dao.SetmealDishdao;
import com.jin.dao.Setmealdao;
import com.jin.domain.Setmeal;
import com.jin.domain.SetmealDish;
import com.jin.dto.SetmealDto;
import org.springframework.stereotype.Service;

@Service
public class SetmealDishserviceimpl extends ServiceImpl<SetmealDishdao, SetmealDish> implements SetmealDishService {
}
