package com.jin.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jin.Controller.util.Basecontext;
import com.jin.Controller.util.R;
import com.jin.Service.EmpoyeeService;
import com.jin.domain.Employee;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Collections;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmpoyeeService employeeService;
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest httpServletRequest,@RequestBody Employee employee){
        String password = employee.getPassword();
               password = DigestUtils.md5DigestAsHex(password.getBytes());    //将密码解密
        LambdaQueryWrapper<Employee> emp = new LambdaQueryWrapper<>();
        emp.eq(Employee::getUsername, employee.getUsername());
        Employee employee1 = employeeService.getOne(emp);
        if(employee1==null){
            return R.error("用户不存在");
        }
        if(employee1.getStatus()==0){
            return R.error("用户被禁用");
        }
        if(!employee1.getPassword().equals(password)){
          return R.error("登入失败");
        }
         httpServletRequest.getSession().setAttribute("employee",employee1.getId());
        Long employee2 = (Long) httpServletRequest.getSession().getAttribute("employee");
        Basecontext.setThreadLocal(employee2);
        return R.success(employee1);
    }
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

      @PostMapping
    public  R<String> save(@RequestBody Employee employee,HttpServletRequest request){
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
       /* employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
          Long o = (Long)request.getSession().getAttribute("employee");
          employee.setUpdateUser(o);
          employee.setCreateUser(o);*/

         employeeService.save(employee);
         return R.success("保存成功");
      }
      @GetMapping("/page")
      public R<Page> fenye(int page,int pageSize,String name){
          Page employeePage = new Page(page, pageSize);
          LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
          wrapper.like(Strings.isNotEmpty(name),Employee::getName,name);
          employeeService.page(employeePage, wrapper);
          return R.success(employeePage);
      }
      @PutMapping
    public  R<String> update(HttpServletRequest request, @RequestBody Employee employee ){
         Long attribute = (Long) request.getSession().getAttribute("employee");
       /*   employee.setUpdateUser(attribute);
          employee.setUpdateTime(LocalDateTime.now());*/

          employeeService.updateById(employee);
          return  R.success("更新成功");
      }
      @GetMapping("/{id}")
    public  R<Employee> findbyId(@PathVariable Long id){

          Employee byId = employeeService.getById(id);
          return R.success(byId);

      }

}
