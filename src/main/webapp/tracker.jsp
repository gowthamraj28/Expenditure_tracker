<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.dao.Expense" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Tracker</title>
<link rel="stylesheet" href="/ExpenditureTracker/STATIC/style.css">

</head>
<body>

	<%
 	if(session.getAttribute("email") == null)
 	{
 		response.sendRedirect("login.jsp");
 	}
	%>
	<div id="navbar"></div>
 		<script>
 			fetch("/ExpenditureTracker/base1.html")
 				.then(response => response.text())
 				.then(data =>{
					document.getElementById("navbar").innerHTML = data;
			}); 				
 		</script>
        <!-- <h3 class="welcome-msg">Welcome, <%= session.getAttribute("name") %> </h3> -->
        
        <%
			response.setHeader("Cache-Control","no-cache, no-store, must-revalidate"); //HTTP 1.1
			response.setHeader("Pragma","no-cache");//HTTP 1.0
			response.setHeader("Expires","0");//Proxies
		%>
            
            <h4 style="margin-top:100px">Add a new Expense</h4>
            <br>
            <div class="inline-form">
                
                <form  action="tracker" class="expense-form">
                    <input type="text" name="itemname" class="box" placeholder="Enter the Description of Expenditure" required>
                    <input type="number" name="amount" step="0.01" class="price" placeholder="Enter the Amount" required>
                    <input type="date" name="dateofexpense" class="dateofexpenditure" required>
                    <button class="add">Add</button>
                </form>  
            </div>
        <hr>

        <form action="generatereport" class="report-form">
            <label for="start_date">Start Date:</label>
            <input type="date" id="start_date" class="dateofexpenditure" name="startdate" required>

            <label for="end_date">End Date:</label>
            <input type="date" id="end_date" class="dateofexpenditure" name="enddate" required>

            <button type="submit" class="add" >Generate Report</button>
        </form>

        <hr>
        <h4>Your Expenses</h4>     
        <div class="expense-container">
        <table class="table table-hover table-dark">
            <thead>
                <tr>
                    <th scope="col">ItemName</th>
                    <th scope="col">Amount</th>
                    <th scope="col">Date</th>
                    <th scope="col">Options</th>
                </tr>
            </thead>
            <tbody>
		    	<%
		    	List<com.dao.Expense> expenses = (List<com.dao.Expense>)request.getAttribute("expenses");
		    	if(expenses != null)
		    	{
		    		for(com.dao.Expense item: expenses){
		    	%>
		    		<tr>
			            <td><%=item.getItemName() %></td>
			            <td><%=item.getAmount() %></td>
			            <td><%=item.getDate() %></td>
		            <td>
		                <a href="edit?id=<%= item.getItemid() %>" class="btn-edit">Edit</a>
		                <a href="delete?id=<%= item.getItemid() %>" class="btn-edit" onclick="return confirm('Sure! Want to delete this item');">Delete</a>
		            </td>
       				</tr>
		    	<%
		    		}
		    	}
		    	%>
		       		
			</tbody>
        </table>
        </div>
</body>
</html>