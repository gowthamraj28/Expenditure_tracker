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

import com.dao.Expense;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/edit")
public class EditServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        int editid = Integer.parseInt(req.getParameter("id"));

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/etracker?user=root&password=tiger");
             PreparedStatement pstmt = con.prepareStatement("SELECT itemid, itemname, amount, dateofexpenses FROM expensesofusers WHERE itemid=?")) {

            Class.forName("com.mysql.jdbc.Driver");
            pstmt.setInt(1, editid);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Expense exp = new Expense();
                    exp.setItemid(rs.getInt("itemid"));
                    exp.setItemName(rs.getString("itemname"));
                    exp.setAmount(rs.getDouble("amount"));
                    exp.setDate(rs.getDate("dateofexpenses"));
                    req.setAttribute("editing_item", exp);
                }
            }
            req.getRequestDispatcher("/edit.jsp").forward(req, res);

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        int id = Integer.parseInt(req.getParameter("id"));
        String itemname = req.getParameter("itemname");
        double amount = Double.parseDouble(req.getParameter("amount"));
        String dateStr = req.getParameter("dateofexpenses");
        java.sql.Date sqlDate = java.sql.Date.valueOf(dateStr);

        HttpSession session = req.getSession();
        Integer useridObj = (Integer) session.getAttribute("userid");
        if (useridObj == null) {
            res.sendRedirect("login.jsp");
            return;
        }

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/etracker?user=root&password=tiger");
             PreparedStatement pstmt = con.prepareStatement(
                     "UPDATE expensesofusers SET itemname=?, amount=?, dateofexpenses=? WHERE itemid=? AND userid=?")) {

            Class.forName("com.mysql.jdbc.Driver");
            pstmt.setString(1, itemname);
            pstmt.setDouble(2, amount);
            pstmt.setDate(3, sqlDate);
            pstmt.setInt(4, id);
            pstmt.setInt(5, useridObj);
            pstmt.executeUpdate();

            res.sendRedirect(req.getContextPath() + "/expenses");

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
