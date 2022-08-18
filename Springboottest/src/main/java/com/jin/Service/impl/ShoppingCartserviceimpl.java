package com.jin.Service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jin.Service.ShoppingCartService;
import com.jin.Service.UserService;
import com.jin.dao.ShoppingCartDao;
import com.jin.dao.UserDao;
import com.jin.domain.ShoppingCart;
import com.jin.domain.User;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartserviceimpl extends ServiceImpl<ShoppingCartDao, ShoppingCart> implements ShoppingCartService {


}
