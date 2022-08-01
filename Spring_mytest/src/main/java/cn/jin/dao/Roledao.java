package cn.jin.dao;

import javax.management.relation.Role;
import java.util.List;

public  interface Roledao {
    public List<Role> findall();
    void  save(Role role);
}
