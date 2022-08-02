package cn.jin.service;

import cn.jin.dao.userdao;
import cn.jin.domain.user;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class userservicetest {
    public static void main(String[] args) throws IOException {
        InputStream resource = Resources.getResourceAsStream("sqlmapconfig.xml");
        SqlSessionFactory build = new SqlSessionFactoryBuilder().build(resource);
        SqlSession sqlSession = build.openSession();
        userdao mapper = sqlSession.getMapper(userdao.class);
        List<user> findall = mapper.findall();
        System.out.println(findall);

    }
}
