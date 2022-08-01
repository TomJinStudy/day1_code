package cn.jin.dao.impl;

import cn.jin.dao.Roledao;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.management.relation.Role;
import java.util.List;

public class Roledaoimpl implements Roledao {
    private JdbcTemplate jdbcTemplate;

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Role> findall() {
        List<Role> list = jdbcTemplate.query("Select * from account", new BeanPropertyRowMapper<Role>(Role.class));
        return  list;
    }

    @Override
    public void save(Role role) {
        jdbcTemplate.update("insert into role values(?,?,?)",null,role.getRoleName(),role.getRoleValue());
    }
    


}
