package org.example.other.jdbc;

import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * @Author zhangchunsheng
 * @CreateTime: 2024/12/30
 */
public class TestJdbc {

    /**
    * @Author: zhangchunsheng
    * @Date: 2024/12/30 16:27
     * 查询所有员工信息
    */
    @Test
    public void testSelect() throws Exception{
        //1. 加载驱动(准备建桥材料):mysql8+可省略
        Class.forName("com.mysql.cj.jdbc.Driver");
        //2. 获取连接(建桥)
        String url = "jdbc:mysql:///db_241125";
        String username = "root";
        String password = "root";
        Connection connection = DriverManager.getConnection(url, username, password);
        //3. 创建Statement对象
        Statement statement = connection.createStatement();
        //4. 执行SQL语句
        String sql = "SELECT eid,ename,salary,email,address FROM t_employee";
        ResultSet rs = statement.executeQuery(sql);
        //5. 处理结果集(select)
        while (rs.next()){
            //获取数据
            int eId = rs.getInt("eid");
            String eName = rs.getString("ename");
            double salary = rs.getDouble("salary");
            String email = rs.getString("email");
            String address = rs.getString("address");
            //显示数据
            System.out.print("eId = " + eId);
            System.out.print("eName = " + eName);
            System.out.print("salary = " + salary);
            System.out.print("email = " + email);
            System.out.println("address = " + address);
        }
        //6. 释放资源
        rs.close();
        statement.close();
        connection.close();

    }


    /**
    * @Author: zhangchunsheng
    * @Date: 2024/12/30 16:36
     *      * 通过员工id获取员工信息
    */
    @Test
    public void testSelectEmpById() throws Exception{
        //1. 加载驱动(准备建桥材料):mysql8+可省略
        Class.forName("com.mysql.cj.jdbc.Driver");
        //2. 获取连接(建桥)
        String url = "jdbc:mysql:///db_241125";
        String username = "root";
        String password = "root";
        Connection connection = DriverManager.getConnection(url, username, password);
        //3. 创建Statement对象
        Statement statement = connection.createStatement();
        //4. 执行SQL语句
        String sql = "SELECT eid,ename,salary,email,address FROM t_employee WHERE eid = 1";
        ResultSet rs = statement.executeQuery(sql);
        //5. 处理结果集(select)
        if (rs.next()){
            //获取数据
            int eId = rs.getInt("eid");
            String eName = rs.getString("ename");
            double salary = rs.getDouble("salary");
            String email = rs.getString("email");
            String address = rs.getString("address");
            //显示数据
            System.out.print("eId = " + eId);
            System.out.print("eName = " + eName);
            System.out.print("salary = " + salary);
            System.out.print("email = " + email);
            System.out.println("address = " + address);
        }
        //6. 释放资源
        rs.close();
        statement.close();
        connection.close();

    }

}
