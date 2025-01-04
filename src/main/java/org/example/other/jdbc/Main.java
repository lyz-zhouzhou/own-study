package org.example.other.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) throws Exception {
        //1. 加载驱动(准备建桥材料)
        Class.forName("com.mysql.cj.jdbc.Driver");

        //2. 获取连接(建桥)
        String url = "jdbc:mysql://localhost:3306/db_241125";
        String username = "root";
        String password = "root";
        Connection connection = DriverManager.getConnection(url, username, password);

        //3. 创建Statement对象
        Statement statement = connection.createStatement();
        //4. 执行SQL语句
        ResultSet resultSet = statement.executeQuery("select * from account");
        while (resultSet.next()) {
            System.out.println(resultSet.getString("name"));
        }
        String sql = "insert into account values(5,5000)";
        //num:数据库受影响行数
        int num = statement.executeUpdate(sql);
        //5. 处理结果集(select)


        //6. 释放资源
        resultSet.close();
        statement.close();
        connection.close();

    }

    public void AA() {
        System.out.println(2);
    }
}
