import cn.jin.dao.userdao;
import cn.jin.domain.user;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class mybatisTest {

/* 搜索操作*/
    @Test
    public void test() throws IOException {
        InputStream resourceAsStream = Resources.getResourceAsStream("sqlmapconfig.xml");
        SqlSessionFactory build = new SqlSessionFactoryBuilder().build(resourceAsStream);
        SqlSession sqlSession = build.openSession();
        List<user> objects = sqlSession.selectList("mapper.findall");
        System.out.println(objects);
        sqlSession.close();

    }
    /*插入操作*/
    @Test
    public void test1() throws IOException {
        user user = new user();

        user.setUsername("tom1");
        user.setPassword("15");
        InputStream resourceAsStream = Resources.getResourceAsStream("sqlmapconfig.xml");
        SqlSessionFactory build = new SqlSessionFactoryBuilder().build(resourceAsStream);
        SqlSession sqlSession = build.openSession();
        userdao mapper = sqlSession.getMapper(userdao.class);
        mapper.insert(user);
        sqlSession.commit();
        sqlSession.close();

    }
    /*更新操作*/
    @Test
    public void test2() throws IOException {
        user user = new user();
        user.setId(3);
        user.setUsername("lucy");
        InputStream resourceAsStream = Resources.getResourceAsStream("sqlmapconfig.xml");
        SqlSessionFactory build = new SqlSessionFactoryBuilder().build(resourceAsStream);
        SqlSession sqlSession = build.openSession();
        userdao mapper = sqlSession.getMapper(userdao.class);
        mapper.update(user);
        sqlSession.commit();
        sqlSession.close();

    }
    /*删除操作*/
    @Test
    public void test3() throws IOException {
        user user = new user();
        user.setId(1);
        user.setUsername("lucy");
        InputStream resourceAsStream = Resources.getResourceAsStream("sqlmapconfig.xml");
        SqlSessionFactory build = new SqlSessionFactoryBuilder().build(resourceAsStream);
        SqlSession sqlSession = build.openSession();
        userdao mapper = sqlSession.getMapper(userdao.class);
        mapper.delete(5);
        sqlSession.commit();
        sqlSession.close();

    }
    /* 分页操作*/
    @Test
    public void test4() throws IOException {
        InputStream resourceAsStream = Resources.getResourceAsStream("sqlmapconfig.xml");
        SqlSessionFactory build = new SqlSessionFactoryBuilder().build(resourceAsStream);
        SqlSession sqlSession = build.openSession();
        userdao mapper = sqlSession.getMapper(userdao.class);
        PageHelper.startPage(1, 3);
        List<user> findall = mapper.findall();
        for (user item:findall
             ) {
            System.out.println(item); }
        PageInfo<user> pageInfo= new PageInfo<user>(findall);
        System.out.println("当前页："+pageInfo.getPageNum());
        System.out.println("每页条数："+pageInfo.getPageSize());
        System.out.println("总数："+pageInfo.getTotal());
        System.out.println("页数："+pageInfo.getPages());
        System.out.println("下一页："+pageInfo.getNextPage());
        System.out.println("上一页："+pageInfo.getPrePage());
        System.out.println("是否第一页："+pageInfo.isIsFirstPage());
        System.out.println("是否最后一页："+pageInfo.isIsLastPage());
        sqlSession.close();
    }
}
