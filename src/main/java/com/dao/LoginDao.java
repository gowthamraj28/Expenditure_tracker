package com.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpSession;

public class LoginDao {
	public int checkLogin(String email,String password)
	{
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection("jdbc:mysql://localhost:3306/etracker?user=root&password=tiger");
				pstmt = con.prepareStatement("select * from usersdata where email = ? and password = ?");
				
				pstmt.setString(1, email);
				pstmt.setString(2, password);
				
				
					
				rs = pstmt.executeQuery();
				if(rs.next())
				{
					int userid = rs.getInt("userid");
					return userid;
				}
				
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			} 
			
		return -1;
	}
}
