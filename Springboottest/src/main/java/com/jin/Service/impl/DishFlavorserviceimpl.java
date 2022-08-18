package com.jin.Service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jin.Service.DishFlavorService;
import com.jin.Service.DishService;
import com.jin.dao.DishFlavordao;
import com.jin.dao.Dishdao;
import com.jin.domain.Dish;
import com.jin.domain.DishFlavor;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorserviceimpl extends ServiceImpl<DishFlavordao, DishFlavor> implements DishFlavorService {
}
