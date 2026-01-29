package com.expendituretracker;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.dao.Expense;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/tracker")
public class TrackerServlet extends HttpServlet{	
	@Override
	protected void doGet(HttpServletRequest req,HttpServletResponse res) throws IOException, ServletException {
		
		String itemname = req.getParameter("itemname");
		double amount = Double.parseDouble(req.getParameter("amount"));
		String dateOfExpense = req.getParameter("dateofexpense");
		
		java.sql.Date sqlDate;
		if (dateOfExpense == null || dateOfExpense.isEmpty()) { 
			sqlDate = new java.sql.Date(System.currentTimeMillis()); 
		}
		else{
			sqlDate = java.sql.Date.valueOf(dateOfExpense); 
		}
		
		HttpSession session = req.getSession();
		Integer useridObj = (Integer) session.getAttribute("userid");
		if(useridObj == null)
		{
			res.sendRedirect("login.jsp");
			return;			
		}
		int userid = useridObj;
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/etracker?user=root&password=tiger");
			pstmt = con.prepareStatement("insert into expensesofusers(itemname,amount,dateofexpenses,userid) VALUES(?,?,?,?)");
			
			pstmt.setString(1, itemname);
			pstmt.setDouble(2, amount);
			pstmt.setDate(3,sqlDate);
			pstmt.setInt(4, userid);
			
//			PrintWriter out = res.getWriter();
//			out.println(userid);
			
			pstmt.executeUpdate();
//			req.getRequestDispatcher("tracker.jsp").forward(req, res);
			res.sendRedirect(req.getContextPath()+"/expenses");
			
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
}