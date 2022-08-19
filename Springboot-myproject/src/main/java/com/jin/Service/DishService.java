package com.jin.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jin.domain.Category;
import com.jin.domain.Dish;
import com.jin.dto.DishDto;

import java.util.List;

public interface DishService extends IService<Dish> {

    void saveDishandflavor(DishDto dishDto);

    DishDto updataDishwithFlavbor(Long id);

    void updataDishandflavor(DishDto dishDto);

    void deleteWithFlavor(List<Long> ids);

    void startandstop(int o,List<Long> ids);


}
