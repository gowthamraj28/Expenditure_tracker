package com.expendituretracker;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet{
	protected void doPost(HttpServletRequest req,HttpServletResponse res) throws IOException{
		String name = req.getParameter("name");
		String email = req.getParameter("email");
		String password = req.getParameter("password");
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/etracker?user=root&password=tiger");
			pstmt = con.prepareStatement("insert into usersdata(email,password,name) VALUES(?,?,?)");
			
			pstmt.setString(1, email);
			pstmt.setString(2, password);
			pstmt.setString(3,name);
			
			int rows = pstmt.executeUpdate();
		
			if(rows>0) {
				res.sendRedirect("login.jsp");
			}
			else {
				res.sendRedirect(req.getContextPath()+"/register.jsp");
			}
			
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		finally {
			try {
				if(pstmt != null)
				{
					pstmt.close();
				}	
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if(con!=null)
				{
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
	    request.getRequestDispatcher("register.jsp").forward(request, response);
	}
	

}
