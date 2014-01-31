package com.BPTR.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CountryMaster {

	Connection con=null;
	PreparedStatement ps=null;
	ResultSet rs=null;
	
	public List<String> getCountryList()
	{
		List<String> coutryMaster=new ArrayList<String>();
		
		try {
			DataSource conObj=new DataSource();
			con=conObj.getConnection();
			String statement="SELECT DISTINCT COUNTRY_NAME FROM COUNTRY_MASTER";
			ps=con.prepareStatement(statement);
			rs=ps.executeQuery();
			while(rs.next())
			{
				coutryMaster.add(rs.getString(1));
			}
//			coutryMaster.add("Select Country");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		finally{
			if(con!=null)
			{
				try {
					con.close();
				} catch (Exception e2) {
					// TODO: handle exception
					e2.printStackTrace();
				}
			}
			
			if(ps!=null)
			{
				try {
					ps.close();
				} catch (Exception e2) {
					// TODO: handle exception
					e2.printStackTrace();
				}
			}
			
			if(rs!=null)
			{
				try {
					rs.close();
				} catch (Exception e2) {
					// TODO: handle exception
					e2.printStackTrace();
				}
			}
		}
		return coutryMaster; 
	}
	
	public String getCountryId(String countryName)
	{

		String country_id="";
		try {
			DataSource conObj=new DataSource();
			con=conObj.getConnection();
			String statement="SELECT COUNTRY_ID FROM COUNTRY_MASTER WHERE COUNTRY_NAME = "+countryName;
			ps=con.prepareStatement(statement);
			rs=ps.executeQuery();
			while(rs.next())
			{
				country_id=rs.getString(1);
			}
			
			if(rs!=null)
			{
				rs.close();
			}
			
			if(ps!=null)
			{
				ps.close();
			}
			
			if(country_id.equalsIgnoreCase("")||country_id!=null)
			{
				country_id=addCountry(countryName);
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		finally{
			if(con!=null)
			{
				try {
					con.close();
				} catch (Exception e2) {
					// TODO: handle exception
					e2.printStackTrace();
				}
			}
			
			if(ps!=null)
			{
				try {
					ps.close();
				} catch (Exception e2) {
					// TODO: handle exception
					e2.printStackTrace();
				}
			}
			
			if(rs!=null)
			{
				try {
					rs.close();
				} catch (Exception e2) {
					// TODO: handle exception
					e2.printStackTrace();
				}
			}
		}
		 
		return country_id;
	}
	
	public String addCountry(String countryName)
	{
		String country_id="";
		try {
			DataSource conObj=new DataSource();
			con=conObj.getConnection();
			String statement="SELECT MAX(COUNTRY_ID)+1 FROM COUNTRY_MASTER";
			ps=con.prepareStatement(statement);
			rs=ps.executeQuery();
			while(rs.next())
			{
				country_id=rs.getString(1);
			}
			
			if(rs!=null)
			{
				rs.close();
			}
			
			if(ps!=null)
			{
				ps.close();
			}
			
			statement="INSERT INTO COUNTRY_MASTER VALUES(?,?)";
			ps=con.prepareStatement(statement);
			ps.setString(1,country_id);
			ps.setString(2,countryName);
			ps.execute();
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		finally{
			if(con!=null)
			{
				try {
					con.close();
				} catch (Exception e2) {
					// TODO: handle exception
					e2.printStackTrace();
				}
			}
			
			if(ps!=null)
			{
				try {
					ps.close();
				} catch (Exception e2) {
					// TODO: handle exception
					e2.printStackTrace();
				}
			}
			
			if(rs!=null)
			{
				try {
					rs.close();
				} catch (Exception e2) {
					// TODO: handle exception
					e2.printStackTrace();
				}
			}
		}
		return country_id;
		 
	}
}
