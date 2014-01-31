<%@page import="java.util.ArrayList"%>
<%@page import="com.BPTR.dto.PlaceInfoObj"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<style>
body{ background:url(images/bg_2.jpg) repeat-x center top;}
</style>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Welcome</title>
<link rel="stylesheet"
	href="http://code.jquery.com/ui/1.10.2/themes/smoothness/jquery-ui.css" />
	<link rel="stylesheet" href="menu.css" type="text/css" media="screen">
<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
<script src="http://code.jquery.com/ui/1.10.2/jquery-ui.js"></script>



 <script> 
	$(function() {
		$("#tabs").tabs();
	});
	</script> 
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
	<br>
	<table cellspacing="50">
		
			<nav>
			<ul>
			<li><a href="Welcome.jsp">Home</a></li>
			<li><a href="Trend.jsp">See The Trend</a></li>
			<li><a href="Travel.jsp">Travel Desk</a></li>
			<li><a href="AddForm.jsp">Add Location</a></li>
			<li><a href="AboutUS.jsp">About Us</a></li>
			</ul>
			</nav>
		
	</table>
	<br>

	<div id="tabs">
		<ul>
			<li><a href="#tabs-1">Find Your Place</a></li>
			
		</ul>
		<div id="tabs-1">
			<div class="input">
				<jsp:include page="Main.jsp"></jsp:include>
			</div>
		</div>
		
	
	</div>
	
</body>
</html>