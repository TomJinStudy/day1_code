package com.jin.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jin.Controller.util.Basecontext;
import com.jin.Controller.util.R;
import com.jin.Service.OrderDetailService;
import com.jin.Service.OrdersService;
import com.jin.domain.Orders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Date;

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
    @GetMapping("/userPage")
    public R<Page>  userPage(int page,int pageSize,HttpSession session){
        Long userId = (Long) session.getAttribute("user");
        Page<Orders> page1=new Page(page,pageSize);
        LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Orders::getUserId,userId);
        ordersService.page(page1,wrapper);
        return R.success(page1);
    }
    @GetMapping("/page")
    public R<Page>  fenPage1(int page, int pageSize, Orders orders, LocalDateTime beginTime, LocalDateTime endTime){
        Page<Orders> page1=new Page(page,pageSize);
        LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(orders.getNumber()!=null, Orders::getNumber,orders.getNumber());
        wrapper.ge(beginTime!=null,Orders::getOrderTime,beginTime)
                .le(endTime!=null,Orders::getOrderTime,endTime);
        ordersService.page(page1,wrapper);
        return R.success(page1);
    }
    @PutMapping
    public  R<String> updateOrder(@RequestBody Orders orders){
        /*orders.setStatus(3);*/
        ordersService.updateById(orders);
        return R.success("更改成功");
    }

}
