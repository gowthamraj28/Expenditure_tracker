<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Login</title>
	<link rel="stylesheet" href="/ExpenditureTracker/STATIC/style.css">
</head>
<body>
	<div id="navbar"></div>
 		<script>
 			fetch("/ExpenditureTracker/base.html")
 				.then(response => response.text())
 				.then(data =>{
					document.getElementById("navbar").innerHTML = data;
			}); 				
 		</script>
        <h1>Login</h1>
        
        <form action="login" method="POST" class="loginform">
            <div class="groupform">
                <label for="email">Email:</label>
                <input type="email" id="email" name="email" placeholder="abc@gmail.com" required><br><br>
            </div>

            <div class="groupform">
                <label for="password">Password:</label>
                <input type="password" id="password" name="password" placeholder="Password" required><br><br>
            </div>
            
            <div class="groupform">
                <button type="submit" class="loginbutton">Login</button>
            </div>
        </form>
        <br>
        <p>New User? <a href="register.jsp">Register</a></p>

</body>
</html>