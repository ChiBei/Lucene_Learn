package com.cg.dao;

import com.cg.pojo.Sku;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SkuDaoImpl implements SkuDao{
    public List<Sku> querySkuList() {
        // 数据库链接
        Connection connection = null;
        // 预编译statement
        PreparedStatement preparedStatement = null;
        // 结果集
        ResultSet resultSet = null;
        // 商品列表
        List<Sku> list = new ArrayList<Sku>();
        try {
            // 加载数据库驱动
            Class.forName("com.mysql.jdbc.Driver");
            // 连接数据库
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/lu01","test","4050");
            // SQL语句
            String sql = "SELECT * FROM dlut_spider";
            // 创建preparedStatement
            preparedStatement = connection.prepareStatement(sql);
            // 获取结果集
            resultSet = preparedStatement.executeQuery();
            // 结果集解析
            while (resultSet.next()) {
                Sku sku = new Sku();
                sku.setId(resultSet.getString("id"));
                sku.setNews_name(resultSet.getString("news_name"));
                sku.setNews_time(resultSet.getString("news_time"));
                sku.setNews_link(resultSet.getString("news_link"));
                list.add(sku);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
