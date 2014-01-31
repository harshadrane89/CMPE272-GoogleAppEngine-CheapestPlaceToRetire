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
<title>Welcome</title>
<link rel="stylesheet"
	href="http://code.jquery.com/ui/1.10.2/themes/smoothness/jquery-ui.css" />
<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
<script src="http://code.jquery.com/ui/1.10.2/jquery-ui.js"></script>

<script>
	$(function() {
		$("#tabs").tabs();
	});
</script>
	
<script src="//maps.google.com/maps?file=api&amp;v=2&amp;key=AIzaSyB_n88RbQ818PvaPBQHuyV_jzzium4KFLA"
            type="text/javascript"></script>
 <script type="text/javascript">
 <% 
 ArrayList<PlaceInfoObj> output = null;
 output = (ArrayList<PlaceInfoObj>) request.getAttribute("output");
 int counter = 0;%>
 function initialize() {
	 
	
	 
     if (GBrowserIsCompatible()) {
       var map = new GMap2(document.getElementById("map_canvas"));
       map.setCenter(new GLatLng(39.011902, -98.4842465), 13);
       map.setZoom(3);;
       map.setUIToDefault();

       // Create a base icon for all of our markers that specifies the
       // shadow, icon dimensions, etc.
       var baseIcon = new GIcon(G_DEFAULT_ICON);
       baseIcon.shadow = "http://www.google.com/mapfiles/shadow50.png";
       baseIcon.iconSize = new GSize(20, 34);
       baseIcon.shadowSize = new GSize(37, 34);
       baseIcon.iconAnchor = new GPoint(9, 34);
       baseIcon.infoWindowAnchor = new GPoint(6, 1);

       // Creates a marker whose info window displays the letter corresponding
       // to the given index.
       function createMarker(point, index) {
         // Create a lettered icon for this point using our icon class
         var letter = String.fromCharCode("A".charCodeAt(0) + index);
         var letteredIcon = new GIcon(baseIcon);
         letteredIcon.image = "http://www.google.com/mapfiles/marker" + letter + ".png";
		
         // Set up our GMarkerOptions object
         markerOptions = { icon:letteredIcon };
         var marker = new GMarker(point, markerOptions);

         GEvent.addListener(marker, "click", function() {
           marker.openInfoWindowHtml("Location " + "</b>");
         });
         return marker;
       }
       var latlong;
       // Add 10 markers to the map at random locations
       <%
       ArrayList<PlaceInfoObj> outputDT = null;
		if (request.getAttribute("output") != null) {
			outputDT = (ArrayList<PlaceInfoObj>) request.getAttribute("output");
			
			for (PlaceInfoObj ref : outputDT) {
				
				
       %>
       
          latlng = new GLatLng(<%=ref.getLat()%>,<%=ref.getLang()%>);
         map.addOverlay(createMarker(latlng, <%= counter++ %>));
       <% }} %>
     }
   }
 
 <%  ArrayList<PlaceInfoObj> outputDTM = null;
	if (request.getAttribute("output") != null) {
		outputDTM = (ArrayList<PlaceInfoObj>) request.getAttribute("output");
		
		for (PlaceInfoObj ref : outputDTM) {
		if(ref.getRecreationInfo().size()>0){
		%>
 function <% out.print("initializeSub"+ref.getLocation_id()+"()");%> {
     if (GBrowserIsCompatible()) {
       var map = new GMap2(document.getElementById(<% out.print("map_canvas-"+ref.getLocation_id());%>));
       map.setCenter(new GLatLng(<%=ref.getLat()%>,<%=ref.getLang()%>), 13);
       map.setZoom(3);;
       map.setUIToDefault();

       // Create a base icon for all of our markers that specifies the
       // shadow, icon dimensions, etc.
       var baseIcon = new GIcon(G_DEFAULT_ICON);
       baseIcon.shadow = "http://www.google.com/mapfiles/shadow50.png";
       baseIcon.iconSize = new GSize(20, 34);
       baseIcon.shadowSize = new GSize(37, 34);
       baseIcon.iconAnchor = new GPoint(9, 34);
       baseIcon.infoWindowAnchor = new GPoint(6, 1);

       // Creates a marker whose info window displays the letter corresponding
       // to the given index.
       function createMarker(point, index) {
         // Create a lettered icon for this point using our icon class
         var letter = String.fromCharCode("A".charCodeAt(0) + index);
         var letteredIcon = new GIcon(baseIcon);
         letteredIcon.image = "http://www.google.com/mapfiles/marker" + letter + ".png";
		
         // Set up our GMarkerOptions object
         markerOptions = { icon:letteredIcon };
         var marker = new GMarker(point, markerOptions);

         GEvent.addListener(marker, "click", function() {
           marker.openInfoWindowHtml("Location " + "</b>");
         });
         return marker;
       }
       var latlong;
       // Add 10 markers to the map at random locations
       <%
      
		if(ref.getRecreationInfo().size()>0)
		{
			
		for(Recreation recreation:ref.getRecreationInfo())
		{		
       %>
       
          latlng = new GLatLng(<%=recreation.getLat()%>,<%=recreation.getLang()%>);
         map.addOverlay(createMarker(latlng));
       <% }} %>
       
       
     }
     
   }
 <%}}}%>
 
    </script>
</head>

<body onload="initialize()" onunload="GUnload()">
	 
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
	<center>
	<br>
	<h1>These are the results</h1>
	<div id="map_canvas" style="width: 1000px; height: 300px"></div>
	</center>
	
	<div id="tabs">
		<ul>

			<%
				ArrayList<PlaceInfoObj> outputDTO = null;
				
				
				char mark='@';
				if (request.getAttribute("output") != null) {
					outputDTO = (ArrayList<PlaceInfoObj>) request.getAttribute("output");
					for (PlaceInfoObj ref : outputDTO) {
						mark=(char) (mark+1);
						
			%>
			<li><a href="<%="#tabs-" + ref.getLocation_id()%>"><%=ref.getLocationName() %>  <img src=<%= "http://www.google.com/mapfiles/marker"+mark+".png" %>></a></li>
			<%
				}
			%>
		</ul>

		<%
			for (PlaceInfoObj ref : outputDTO) {
		%>
		<div id="<%="tabs-" + ref.getLocation_id()%>">
			
			<center>
			<h1>
				<%=ref.getLocationName()%></h1>
			</center>
			
			<center>	
			<h2>
				<font color="red"><%=ref.getComment() %> </font>
			</h2>
			<h2>
				Some Vital Information About <%=ref.getLocationName()%></h2>
				
			<table cellpadding="10">
				
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
			<div id=<%= "map_canvas-" +ref.getLocation_id()%> style="width: 1000px; height: 300px"></div>
			<%		
		} %>
		</div>
		<%
			}
			}
				if(outputDTO.size()==0){	
		%>
		<center><h1>  NO DATA<h1></center>
		<%} %>

	</div>
	
	<center>
	<a href="Welcome.jsp"><h1>Start A New Search<h1></a>
	</center>
	<%-- 	<jsp:include page="Output.jsp"></jsp:include> --%>

</body>
</html>