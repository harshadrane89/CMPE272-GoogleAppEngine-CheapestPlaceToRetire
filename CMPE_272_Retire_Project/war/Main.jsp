<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

</head>
<body>

	<form method="get" name="calBPTR" action="/CalculateBPTR"   id="calBPTR">
		<table cellspacing="30" width="1000">
			<tr>
				<td>Monthly Income</td>
				<td><input type="text" name="income" size="25" onblur=""> </td>
			</tr>
			<tr>
				<td>Location</td>
				<td><input type="radio" name="loc" value="Uptown" >Uptown
					<input type="radio" name="loc" value="Downtown" checked="checked">Downtown</td>
			</tr>
			<tr>
				<td>Housing Preferences</td>
				<td><input type="radio" name="hp" value="Leased" onchange="" checked="checked">Leased\Rented
					<input type="radio" name="hp" value="Owned">Owned</td>
			</tr>
			<tr>
				<td>Housing Investment</td>
				<td><input type="text" name="hi" size="30"></td>
			</tr>
			<tr>
				<td>Type of Housing</td>
				<td><input type="checkbox" name="HousingPreferences" value="Apartment">Apartment
				</td>
				<td><input type="checkbox" name="HousingPreferences" value="Duplex Apartment">Duplex
					Apartment</td>
			</tr>
			<tr>
				<td></td>
				<td><input type="checkbox" name="HousingPreferences" value="Duplex House">Duplex House</td>
				<td><input type="checkbox" name="HousingPreferences" value="Mansion">Mansion
				</td>
			<tr>
			<tr>
				<td>Medical Expenses</td>
				<td><input type="radio" name="me" value="Yes">Yes <input
					type="radio" name="me" value="No" checked="checked">No</td>
			</tr>
			<tr>
				<td>Number of Results </td>
				<td><input type="text" name="nor" size="25" value="5"></td>
			</tr>
		</table>
		<input type="button" name="BTNsubmit" value="Get Lucky !!!"  onclick="validateForm()"/>
	</form>
	
	
<script type='text/javascript'>

function validateForm()
{
	
	var income=document.forms["calBPTR"]["income"].value;
	var hi=document.forms["calBPTR"]["hi"].value;
	var nor=document.forms["calBPTR"]["nor"].value;
	var ref=document.forms["calBPTR"];
	
	if(isEmpty(income))
		{
			alert("Please enter monthly income value");
			return;		
			
		}
	
	if(isEmpty(hi))
	{
		alert("Please enter housing investment amount");
		return;		
		
	}
	
	if(isEmpty(nor))
	{
		alert("Please enter number of results");
		return;		
		
	}
	
	document.getElementById("calBPTR").submit();
}

function isEmpty(elem)
{
	
	    return (!elem || /^\s*$/.test(elem));
	
}

function isNumeric(elem, helperMsg){
	var numericExpression = /^[0-9]+$/;
	if(elem.value.match(numericExpression)){
		return true;
	}else{
		alert(helperMsg);
		elem.focus();
		return false;	
	}
}
</script>
	
</body>
</html>
