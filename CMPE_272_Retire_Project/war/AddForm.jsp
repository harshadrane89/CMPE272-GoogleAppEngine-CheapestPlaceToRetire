<%@page import="java.util.List"%>
<%@page import="com.BPTR.db.CountryMaster"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<style>
body{ background:url(images/bg_2.jpg) repeat-x center top;}
</style>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="menu.css" type="text/css" media="screen">.
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
			<li><a href="Trend.jsp">See The Trend</a></li>
			<li><a href="Travel.jsp">Travel Desk</a></li>
			<li><a href="AddForm.jsp">Add Location</a></li>
			<li><a href="AboutUS.jsp">About Us</a></li>
			</ul>
			</nav>
		
	</table>
	
	
 	<% List<String> countryList = new CountryMaster().getCountryList();%> 
	<form method="GET" name="calBPTR" action="/AddLocation"   >
		<table cellpadding="25">
			<tr>
				<td>Please select the country</td>
				<td><select name="country" >
					<option value="Select Country"> Select Country</option>
					<% for(String country:countryList){%>
					<option value=<%=country%>><%=country%></option>
					<%} %>
				</select>
				or
				</td>
				<td>
				Enter Country <input type="text" size="30" name="CountryName">
				</td>

			</tr>
			<tr>
				<td>Location Name</td>
				<td><input type="text" name="loc_name"></td>
			</tr>
			<tr>
			<td>Climate</td>
				<td><input type="radio" name="climate" value="Hot" >Hot
					<input type="radio" name="climate" value="Warm" checked="checked">Warm
					<input type="radio" name="climate" value="Cold" >Cold</td>
			</tr>
			<tr>
			<td>Location</td>
				<td><input type="radio" name="loc" value="Uptown" >Uptown
					<input type="radio" name="loc" value="Downtown" checked="checked">Downtown</td>
			</tr>
			<tr>
				<td>Latitude</td>
				<td><input type="text" name="lat"></td>
			</tr>
			<tr>
				<td>Longitude</td>
				<td><input type="text" name="long"></td>
			</tr>
			<tr>
				<td>Apartment Leased Expense(Min - Max)</td>
				<td><input type="text" name="aMin"></td>
				<td>-</td>
				<td><input type="text" name="aMax"></td>
			</tr>
			<tr>
				<td>House Owned Range(Min - Max)</td>
				<td><input type="text" name="hMin"></td>
				<td>-</td>
				<td><input type="text" name="hMax"></td>
			</tr>
			<tr>
				<td>Groceries Expenses</td>
				<td><input type="text" name="gExp"></td>
			</tr>
			<tr>
				<td>Utilities Expenses</td>
				<td><input type="text" name="uExp"></td>
			</tr>
			<tr>
				<td>Other Expenses</td>
				<td><input type="text" name="oExp"></td>
			</tr>
			<tr>
				<td>Medical Expenses</td>
				<td><input type="text" name="mExp"></td>
			</tr>
				<tr>
				<td>Information</td>
				<td><textarea rows="5" cols="30" name="info"> </textarea></td>
			</tr>
		</table>
		<center>
			<input type="submit" value="ADD LOCATION">
		</center>
	</form>
	<b>
	<%if(request.getAttribute("output")!=null)
		{
		 	out.print(request.getAttribute("output"));
		}
		%>
	</b>	
</body>
</html>