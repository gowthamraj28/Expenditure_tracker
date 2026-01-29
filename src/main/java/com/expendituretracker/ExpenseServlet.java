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

@WebServlet("/expenses")
public class ExpenseServlet extends HttpServlet{
	protected void doGet(HttpServletRequest req,HttpServletResponse res) throws IOException, ServletException {
		HttpSession session = req.getSession();
		Integer useridObj = (Integer) session.getAttribute("userid");
		if (useridObj == null) {
		    res.sendRedirect("login.jsp");
		    return;
		}
		int userid = useridObj;

		List<Expense> expenses = new ArrayList<>();
		Connection con  =null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/etracker?user=root&password=tiger");
		    pstmt = con.prepareStatement("SELECT itemid,itemname, amount, dateofexpenses FROM expensesofusers WHERE userid=?");
		    
		    pstmt.setInt(1, userid);
		    rs = pstmt.executeQuery();
		    while (rs.next()) {
		        Expense exp = new Expense();
		        exp.setItemid(rs.getInt("itemid"));
		        exp.setItemName(rs.getString("itemname"));
		        exp.setAmount(rs.getDouble("amount"));
		        exp.setDate(rs.getDate("dateofexpenses"));
		        expenses.add(exp);
		    }
		req.setAttribute("expenses", expenses);
		req.getRequestDispatcher("/tracker.jsp").forward(req, res);
		}
		catch(ClassNotFoundException | SQLException e)
		{
			e.printStackTrace();
		}
		finally {
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	protected void doPost(HttpServletRequest req,HttpServletResponse res) throws IOException, ServletException {
		HttpSession session = req.getSession();
		Integer useridObj = (Integer) session.getAttribute("userid");
		if (useridObj == null) {
		    res.sendRedirect("login.jsp");
		    return;
		}
		int userid = useridObj;

		List<Expense> expenses = new ArrayList<>();
		Connection con  =null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/etracker?user=root&password=tiger");
		    pstmt = con.prepareStatement("SELECT itemid,itemname, amount, dateofexpenses FROM expensesofusers WHERE userid=? ORDER By dateofexpenses DESC");
		    
		    pstmt.setInt(1, userid);
		    rs = pstmt.executeQuery();
		    while (rs.next()) {
		        Expense exp = new Expense();
		        exp.setItemid(rs.getInt("itemid"));
		        exp.setItemName(rs.getString("itemname"));
		        exp.setAmount(rs.getDouble("amount"));
		        exp.setDate(rs.getDate("dateofexpenses"));
		        expenses.add(exp);
		    }
		req.setAttribute("expenses", expenses);
		req.getRequestDispatcher("/tracker.jsp").forward(req, res);
		}
		catch(ClassNotFoundException | SQLException e)
		{
			e.printStackTrace();
		}
		finally {
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
		
	
	
	
}
