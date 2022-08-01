package cn.jin.service;

import javax.management.relation.Role;
import java.util.List;

public interface RoleService {
    public  List<Role> list();

     void save(Role role);
}
