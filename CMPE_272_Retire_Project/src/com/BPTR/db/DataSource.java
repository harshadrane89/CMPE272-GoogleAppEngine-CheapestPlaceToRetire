package com.BPTR.db;

import com.google.appengine.api.rdbms.AppEngineDriver;

import java.io.IOException;
import java.sql.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DataSource {

	
	Connection getConnection()
	{
		Connection con=null;
		try {
			DriverManager.registerDriver(new AppEngineDriver());
			con = DriverManager.getConnection("jdbc:google:rdbms://my-cmpe-272:db/CPTR");

		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		
		return con;
	}
	
	
	public static void main(String[] args)throws Exception {
		DataSource ref=new DataSource();
		Connection con=ref.getConnection();
		if(con!=null)
		System.out.println(con.getSchema());
		
		if(con!=null)
		con.close();
	}
}
