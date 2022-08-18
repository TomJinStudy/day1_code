package com.jin.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jin.domain.Dish;
import com.jin.domain.Setmeal;
import com.jin.domain.SetmealDish;
import com.jin.dto.SetmealDto;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {

    void saveSetmealandDish(SetmealDto setmealDto);

    void deleteWithDish(List<Long> ids);

    void startandstop(int o, List<Long> ids);

    void updataDishandSetmeal(SetmealDto setmealDto);
}
