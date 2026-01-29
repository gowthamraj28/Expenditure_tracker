<!DOCTYPE html>
<html>
<head>
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

        <div class="container">
            <h2>Welcome to Expenditure Tracker</h2>
            <br>
            <h3>New Users Register</h3>
            <h3>Login to Track the Expenditure!!</h3>
        </div>
</body>
</html>