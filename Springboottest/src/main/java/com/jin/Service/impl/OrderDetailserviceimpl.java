package com.jin.Service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jin.Service.OrderDetailService;
import com.jin.Service.OrdersService;
import com.jin.dao.OrderDao;
import com.jin.dao.OrderDetailDao;
import com.jin.domain.OrderDetail;
import com.jin.domain.Orders;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailserviceimpl extends ServiceImpl<OrderDetailDao, OrderDetail> implements OrderDetailService {


}
