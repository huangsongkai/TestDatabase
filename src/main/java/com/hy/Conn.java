package com.hy;

import java.sql.*;
import java.text.ParseException;
import java.util.UUID;

public class Conn {
	Connection con;
	PreparedStatement stmt = null;
	public Connection getConnection() throws ParseException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("数据库驱动加载成功");
		} catch(ClassNotFoundException e){
			e.printStackTrace();
		}
		try {
			con = DriverManager.getConnection("jdbc:mysql://rm-2zel7207po0m4etmzeo.mysql.rds.aliyuncs.com/yitongbx?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&useAffectedRows=false&autoReconnect=true&failOverReadOnly=false","tongess_market","yitong_!123");
			System.out.println("数据库连接成功");
			for (int i = 0; i < 5000000; i++) {//i = 插入条数
				Timestamp date = RandomValue.randomDate("2007-11-30 00:00:00", "2020-9-1 00:00:00");
				String tel = RandomValue.getTel();
				int num = 0;
				String sql = "insert into `yitongbx`.`account_user_info` (`name`, `create_time`, `open_id`, `card_id`, `password`,`nickname`, `username`, `del_flag`, `status`) " +
						"values ( ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				System.out.println(sql);
				stmt = con.prepareStatement(sql);
				String name = RandomValue.getChineseName();
				stmt.setString(1, name);
				stmt.setTimestamp(2, date);
				stmt.setString(3, UUID.randomUUID().toString());
				stmt.setString(4, UUID.randomUUID().toString());
				stmt.setString(5, UUID.randomUUID().toString());
				stmt.setString(6, RandomValue.getBxName());
				while(yzTel(con,tel) > 0){
					tel = RandomValue.getTel();
				}
				stmt.setString(7,tel);
				stmt.setInt(8, 0);
				stmt.setInt(9, 1);
				int result =stmt.executeUpdate();// 返回值代表收到影响的行数
				System.out.println("插入成功 计数:"+i+" ---"+stmt.toString());
				}
			} catch (SQLException e) {
				e.printStackTrace();

			}
		return con;
	}
	public int yzTel(Connection con , String tel) throws SQLException {
		String sql1 = "select COUNT(0) AS num from account_user_info where account_user_info.username = '"+tel+"'";
		PreparedStatement ps = con.prepareStatement(sql1);
		ResultSet rs = ps.executeQuery();
		int num = 0;
		while(rs.next()) {
			num = rs.getInt("num");
		}
		System.out.println(num);
		return num;
	}

	public static void main(String[] args) throws ParseException {
		Conn c = new Conn();
		c.getConnection();
	}
}