package cn.jin.service.ipml;

import cn.jin.dao.Roledao;
import cn.jin.service.RoleService;

import javax.management.relation.Role;
import java.util.List;

public class RoleServiceimpl implements RoleService {
    private Roledao roledao;

    public void setRoledao(Roledao roledao) {
        this.roledao = roledao;
    }

    @Override
    public List<Role> list() {
        List<Role> roledao1= roledao.findall();
        return roledao1;
    }

    @Override
    public void save(Role role) {
        roledao.save(role);
    }

}
