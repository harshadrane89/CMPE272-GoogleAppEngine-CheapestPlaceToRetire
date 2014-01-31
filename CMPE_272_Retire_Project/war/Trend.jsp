<%@page import="com.BPTR.dto.Recreation"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
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
<link rel="stylesheet" href="menu.css" type="text/css" media="screen">
<title>Trends</title>
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
		<form method="get" action="/Trend">
		<input type="submit" value="Get The Trends !!!">
		</form>	
		
		<%if (request.getAttribute("output") != null) {
			ArrayList<PlaceInfoObj> outputDTO = (ArrayList<PlaceInfoObj>) request.getAttribute("output");
			for (PlaceInfoObj ref : outputDTO) {
		%>		
		<center>
			<h1>
				<%=ref.getLocationName()%></h1>
			</center>
			
			<center>	
			
			<h2>
				Some Vital Information About <%=ref.getLocationName()%></h2>
				
			<table cellpadding="10">
				<tr>
					
						<td>
						<b>
							Number of times appeared in search</b>
						</td>
						<td>
						<b>
							<%=ref.getSearchCount()%></b>	
						</td>
						
				</tr>
				<tr>
						<td>
							Apartment Cost (max)
						</td>
						<td>
							<%=ref.getaMax()%>	
						</td>
				</tr>
				
				<tr>
						<td>
							Apartment Cost (min)
						</td>
						<td>
							<%=ref.getaMin()%>	
						</td>
				</tr>
				<tr>
						<td>
							House Cost (max)
						</td>
						<td>
							<%=ref.gethMax()%>	
						</td>
				</tr>
				<tr>
						<td>
							House Cost (min)
						</td>
						<td>
							<%=ref.gethMin()%>	
						</td>
				</tr>
				<tr>
						<td>
							Groceries Expense
						</td>
						<td>
							<%=ref.getGroceries()%>	
						</td>
				</tr>
				<tr>
						<td>
							Utilities Expense
						</td>
						<td>
							<%=ref.getUtilities()%>	
						</td>
				</tr>
				<tr>
						<td>
							Other Expenses
						</td>
						<td>
							<%=ref.getOtherExpenses()%>	
						</td>
				</tr>
				<tr>
					
						<td>
						<b>
							Monthly Expenses
							</b> 
						</td>
						<td>
						<b>
							<%=ref.getMonthlyExp()%> + Housing Expenses
							</b>	
						</td>
					
				</tr>
				<tr>
						<td>
							Medical Expenses
						</td>
						<td>
							<%=ref.getMedicalExpenses()%>	
						</td>
				</tr>
				<tr>
						<td>
							Monthly Expenses with Medicine  
						</td>
						<td>
							<%=ref.getMonthlyExpwM()%>	
						</td>
				</tr>
				<tr>
						<td>
							Climate
						</td>
						<td>
							<%=ref.getClimate()%>	
						</td>
				</tr>
				
			
			</table>
			All Figures are in USD
			</center>	
			<br>
			<br>
			<p>
			<% if(ref.getInfo()!=null){
				out.print(ref.getInfo());
			} 
			%>
			</p>
			<%if(ref.getImages().size()>0){ %>
			<table>
				<tr>
					<% for(String imgPath: ref.getImages()) {%>
					<td>
						<img alt="" src=<%=imgPath %>>
					</td>
					<%} %>
				</tr>
			</table>
			<%} %>
		<%if(ref.getRecreationInfo().size()>0){
			out.print("Recreation Info");
			%>
			<table>
				
					<% for(Recreation dto: ref.getRecreationInfo()) {%>
					<tr>
						<td>
							<%= dto.getType()+" - "%>						
						</td>
						<td>
							<%= dto.getName()%>
						</td>
					</tr>
					
					<%} %>
				
			</table>
			
			<%}}		
		} %>
</body>
</html>