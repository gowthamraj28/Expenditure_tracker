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

@WebServlet("/delete")
public class DeleteServlet extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
//        System.out.println("id: "+id);
        HttpSession session = req.getSession();
        Integer useridObj = (Integer) session.getAttribute("userid");
        if (useridObj == null) {
            res.sendRedirect("login.jsp");
            return;
        }
        int userid = useridObj;
 
        Connection con  =null;
		PreparedStatement pstmt = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/etracker?user=root&password=tiger");
			pstmt = con.prepareStatement("DELETE FROM expensesofusers WHERE itemid=?");
		    
			pstmt.setInt(1, id);
//	        pstmt.setInt(2, userid);
		    pstmt.executeUpdate();
		    
		    res.sendRedirect(req.getContextPath() + "/expenses");
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

