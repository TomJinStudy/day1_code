package com.jin;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.junit.Test;

import java.sql.Connection;

public class test1 {
    @Test
    public  void test1 ()  throws Exception{
        ComboPooledDataSource DataSource = new ComboPooledDataSource();
        DataSource.setDriverClass("com.mysql.jdbc.Driver");
        DataSource.setJdbcUrl("jdbc:mysql://localhost:3306/test");
        DataSource.setUser("root");
        DataSource.setPassword("1234");
        Connection connection = DataSource.getConnection();
        System.out.println(connection);
    }
}
