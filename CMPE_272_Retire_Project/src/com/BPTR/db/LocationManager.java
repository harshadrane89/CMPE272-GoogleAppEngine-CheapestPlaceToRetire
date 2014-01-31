package com.BPTR.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.BPTR.dto.PlaceInfoObj;
import com.BPTR.input.NewLocationObj;

public class LocationManager {

	PlaceInfoObj locationDTO;
	
	Connection con=null;
	PreparedStatement ps=null;
	ResultSet rs=null;
	
	
	public String populateLocationMaster(NewLocationObj transferObj)
	{
		try {
			
			transferObj.setLocationID(getLocationID());
			DataSource conObj=new DataSource();
			con=conObj.getConnection();
			String statement="INSERT INTO LOCATION_MASTER(COUNTRY_ID,LOCATION_ID,LOCATION_NAME,LATITUDE,LONGITUDE,APARTMENT_LEASED_MAX,APARTMENT_LEASED_MIN,HOUSE_OWNED_MAX,HOUSE_OWNED_MIN,GROCERIES,UTILITIES,OTHER_EXPENSES,MEDICAL_EXPENSES,LOCATION_TYPE,CLIMATE) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			ps=con.prepareStatement(statement);
			ps.setString(1, transferObj.getCountryId());
			ps.setString(2, transferObj.getLocationID());
			ps.setString(3, transferObj.getLocationName());
			ps.setString(4, transferObj.getLat());
			ps.setString(5, transferObj.getLang());
			ps.setString(6, transferObj.getaMax());
			ps.setString(7, transferObj.getaMin());
			ps.setString(8, transferObj.gethMax());
			ps.setString(9, transferObj.gethMin());
			ps.setString(10, transferObj.getGrocery());
			ps.setString(11, transferObj.getUtility());
			ps.setString(12, transferObj.getOtherExp());
			ps.setString(13, transferObj.getMedicalExp());
			ps.setString(14, transferObj.getLocationType());
			ps.setString(15, transferObj.getClimate());
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
		
		populateLocationInfo(transferObj.getLocationID(), transferObj.getLocInfo());
		 return "The new location is succesfully added";
	
	}
	
	
	String getLocationID()
	{
		String location_id="";
		try {
			DataSource conObj=new DataSource();
			con=conObj.getConnection();
			String statement="SELECT MAX(LOCATION_ID)+1 FROM LOCATION_MASTER";
			ps=con.prepareStatement(statement);
			rs=ps.executeQuery();
			while(rs.next())
			{
				location_id=rs.getString(1);
			}
			
			
		}
		catch (Exception e){
			e.printStackTrace();
			
		}
		finally{
			if(con!=null)
			{
				try {
					con.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			if(rs!=null)
			{ 
				
				try {
					rs.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
			if(ps!=null)
			{
				try {
					ps.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		return location_id;
	}
	
	void populateLocationInfo(String locationId,String locationInfo)
	{

		try {
			DataSource conObj=new DataSource();
			con=conObj.getConnection();
			String statement="INSERT INTO LOCATION_DESC VALUES("+locationId+",1,"+locationInfo+")";
			ps=con.prepareStatement(statement);
			rs=ps.executeQuery();
			
			
			
		}
		catch (Exception e){
			e.printStackTrace();
			
		}
		finally{
			if(con!=null)
			{
				try {
					con.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			if(rs!=null)
			{ 
				
				try {
					rs.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
			if(ps!=null)
			{
				try {
					ps.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		
	
	}
	
	
}
