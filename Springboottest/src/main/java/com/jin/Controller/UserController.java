package com.jin.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jin.Controller.util.R;
import com.jin.Service.UserService;
import com.jin.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.jws.soap.SOAPBinding;
import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserService userService;
  @PostMapping("login")
    public R<User> login(@RequestBody Map map, HttpSession session){
      String phone = map.get("phone").toString();
     
      User user = new User();
      LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
      wrapper.eq(User::getPhone,phone);
      User user1 = userService.getOne(wrapper);
      if(user1==null){
          user.setStatus(1);
          user.setPhone(phone);
          userService.save(user);
          session.setAttribute("user",user.getId());
          return R.success(user);
      }
      session.setAttribute("user",user1.getId());
      return R.success(user1);

  }

}
