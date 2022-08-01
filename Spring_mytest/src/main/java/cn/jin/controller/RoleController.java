package cn.jin.controller;

import cn.jin.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.management.relation.Role;
import java.util.List;
@Controller
@RequestMapping("/Role")
public class RoleController {
@Autowired
private RoleService roleService;

  /*  public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }*/
@RequestMapping("/list")
    public ModelAndView list(){
      ModelAndView modelAndView = new ModelAndView();
         List<Role>  list=roleService.list();
         modelAndView.addObject("Role-list",list);
         modelAndView.setViewName("Role-list");
         return modelAndView;
  }
    @RequestMapping("/save")
    public String save(Role role){
        roleService.save(role);
        return"Redirect:/Role/list";
    }
}
