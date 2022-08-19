package com.jin.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jin.domain.Orders;
import com.jin.domain.User;

import javax.servlet.http.HttpSession;

public interface OrdersService extends IService<Orders> {

    void submit(Orders orders, HttpSession session);
}
