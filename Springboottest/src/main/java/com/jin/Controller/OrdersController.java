package com.jin.Controller;

import com.jin.Controller.util.R;
import com.jin.Service.OrderDetailService;
import com.jin.Service.OrdersService;
import com.jin.domain.Orders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/order")
public class OrdersController {
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private OrderDetailService orderDetailService;
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders, HttpSession session){
        ordersService.submit(orders,session);
        return R.success("提交成功");
    }
}
