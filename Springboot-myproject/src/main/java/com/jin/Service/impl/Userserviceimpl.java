package com.jin.Service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jin.Controller.util.myexception;
import com.jin.Service.DishFlavorService;
import com.jin.Service.DishService;
import com.jin.Service.UserService;
import com.jin.dao.Dishdao;
import com.jin.dao.UserDao;
import com.jin.domain.Dish;
import com.jin.domain.DishFlavor;
import com.jin.domain.User;
import com.jin.dto.DishDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class Userserviceimpl extends ServiceImpl<UserDao, User> implements UserService {


}
