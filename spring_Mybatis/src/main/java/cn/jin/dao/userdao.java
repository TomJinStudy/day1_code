package cn.jin.dao;

import cn.jin.domain.user;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface userdao {
    @Select("select * from user")
    public List<user> findall();
   @Insert("insert into user values(#{id},#{username},#{password})")
    public void  insert(user user);
   @Update(" update user set username=#{username} where id= #{id}")
   public void update(user user);
   @Delete(" delete  from user where id=#{id}")
   public void  delete(int id);

}
