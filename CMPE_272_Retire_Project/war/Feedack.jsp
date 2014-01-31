<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<style>
body{ background:url(images/bg_2.jpg) repeat-x center top;}
</style>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="menu.css" type="text/css" media="screen">
<title>About US</title>
</head>
<body>

<table>
	<tr>
		<td>
		<img alt="" src="/images/logo.png">
		</td>
		<td><font face="Arial Black">
			<h1>Best Place To Retire</h1>
			</font>
		</td>
	</tr>
	</table>

<table cellspacing="50">
		
			<nav>
			<ul>
			<li><a href="Welcome.jsp">Home</a></li>
			<li><a href="Trend.jsp">See Trend</a></li>
			<li><a href="Travel.jsp">Travel Desk</a></li>
			<li><a href="AddForm.jsp">Add Location</a></li>
			
			<li><a href="AboutUS.jsp">About Us</a></li>
			</ul>
			</nav>
		
	</table>
	
	<form method="post" action="/Feedback" >
	<table>
		<tr>
			<td>
				Name : 
			</td>
			<td>
				<input type="text" name="uName" size="30" >
			</td>
		</tr>
		<tr>
			<td>
				Email : 
			</td>
			<td>
				<input type="text" name="uName" size="30" >
			</td>
		</tr>
		<tr>
			<td>
				Contents : 
			</td>
			<td>
				<input type="text" name="uName" size="30" >
			</td>
		</tr>
	</table>
	<input type="button" value="Submit">
	</form>
</body>
</html>