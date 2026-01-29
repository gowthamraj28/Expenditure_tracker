package com.expendituretracker;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.dao.LoginDao;

import java.sql.*;

@WebServlet("/login")
public class LoginServlet extends HttpServlet{
	protected void doPost(HttpServletRequest req,HttpServletResponse res) throws IOException, ServletException {
		
		String email = req.getParameter("email");
		String password = req.getParameter("password");
		
		LoginDao ldao = new LoginDao();
		int userid = ldao.checkLogin(email,password);
		if(userid != -1) {
			HttpSession session = req.getSession();
			session.setAttribute("userid", userid);
			session.setAttribute("email", email);
			
			req.setAttribute("userid", userid);
			RequestDispatcher rd = req.getRequestDispatcher("/expenses");
			rd.forward(req,res);
//			res.sendRedirect("tracker.jsp");
		}
		else {
			res.sendRedirect("login.jsp");
		}	
	}
		@Override
		protected void doGet(HttpServletRequest request, HttpServletResponse response)
		        throws ServletException, IOException {
		    request.getRequestDispatcher("login.jsp").forward(request, response);
		}

}
