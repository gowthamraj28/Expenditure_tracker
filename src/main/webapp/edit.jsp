<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="com.dao.Expense" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Edit</title>
<link rel="stylesheet" href="/ExpenditureTracker/STATIC/style.css">
</head>
<body>
	<%
 	if(session.getAttribute("email") == null)
 	{
 		response.sendRedirect("login.jsp");
 	}
	%>
 	<%
		response.setHeader("Cache-Control","no-cache, no-store, must-revalidate"); //HTTP 1.1
	%>
    <div id="navbar"></div>
    <script>
        fetch("/ExpenditureTracker/base.html")
            .then(response => response.text())
            .then(data => {
                document.getElementById("navbar").innerHTML = data;
            });
    </script>

    <%
        Expense editing_item = (Expense) request.getAttribute("editing_item");
        if (editing_item != null) {
    %>
        <h4>Edit Expense</h4>
        <div class="inline-form"> 
	        <form method="POST" action="edit" class="expense-form">
	            <input type="hidden" name="id" value="<%= editing_item.getItemid() %>">
	            <input type="text" name="itemname" value="<%= editing_item.getItemName() %>" required>
	            <input type="number" name="amount" step="0.01" value="<%= editing_item.getAmount() %>" required>
	            <input type="date" name="dateofexpenses" value="<%= editing_item.getDate() %>" required>
	            <button type="submit" class="add">Update</button>
	        </form>
        </div>
    <%
        }
    %>
</body>
</html>
