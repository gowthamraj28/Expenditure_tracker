package com.expendituretracker;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet{
	protected void doGet(HttpServletRequest req,HttpServletResponse res) throws IOException {
		HttpSession session = req.getSession();
		session.removeAttribute("email");
		session.removeAttribute("userid");
		session.invalidate();
		res.sendRedirect("login.jsp");
	}
}

//import java.io.IOException;
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//
//
//public class LogoutServlet extends HttpServlet {
//    private static final long serialVersionUID = 1L;
//
//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse res)
//            throws ServletException, IOException {
//        HttpSession session = req.getSession(false);
//        if (session != null) {
//            session.invalidate();
//        }
//        res.sendRedirect("login.jsp");
//    }
//}

