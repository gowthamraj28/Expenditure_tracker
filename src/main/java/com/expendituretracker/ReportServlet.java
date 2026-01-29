package com.expendituretracker;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

@WebServlet("/generatereport")
public class ReportServlet extends HttpServlet{
	protected void doGet(HttpServletRequest req,HttpServletResponse res) throws IOException {
		
		String startDate = req.getParameter("startdate");
		String endDate = req.getParameter("enddate");
//		PrintWriter out =  res.getWriter();
//		out.println(startDate);
//		out.println(endDate);
//		out.println("Pdf");
		
		res.setContentType("application/pdf");
		res.setHeader("Content-Disposition","attachment; filename=expenses.pdf");
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Document document = null;
		try {
			HttpSession session = req.getSession();
			int userid = (int) session.getAttribute("userid");
			document = new Document();
			PdfWriter.getInstance(document, res.getOutputStream());
			document.open();
			
			document.add(new Paragraph("Expenditure Tracker Report From "+startDate+" To "+endDate));
			document.add(new Paragraph(" "));
			
			Class.forName("com.mysql.jdbc.Driver");
			con =  DriverManager.getConnection("jdbc:mysql://localhost:3306/etracker?user=root&password=tiger");
			pstmt = con.prepareStatement("select itemname,amount,dateofexpenses from expensesofusers where dateofexpenses BETWEEN ? AND ? AND userid = ?");
			pstmt.setDate(1, java.sql.Date.valueOf(startDate));
			pstmt.setDate(2, java.sql.Date.valueOf(endDate));
			pstmt.setInt(3, userid);
			rs = pstmt.executeQuery();
			
			PdfPTable table = new PdfPTable(3);
			table.addCell("ItemName");
			table.addCell("Amount");
			table.addCell("Date");
			
			while(rs.next()) {
				table.addCell(rs.getString("itemname"));
				table.addCell(String.valueOf(rs.getDouble("amount")));
				table.addCell(rs.getString("dateofexpenses"));
			}
			document.add(table);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			document.close();
		}
		
		
	}
}
