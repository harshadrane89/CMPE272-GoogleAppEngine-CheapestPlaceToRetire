package com.BPTR.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.BPTR.dto.PlaceInfoObj;
import com.BPTR.dto.Recreation;

public class TrendsValue {
	
	public List<PlaceInfoObj> getTrends()
	{

		List<PlaceInfoObj> finalDisplayList = new ArrayList<PlaceInfoObj>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = new DataSource().getConnection();

			String sql = "SELECT * FROM TREND_MASTER ORDER BY SEARCH_COUNT DESC";
			System.out.println("THE SQL IS " + sql);
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			PlaceInfoObj dto = null;
			while (rs.next()) {
				dto = new PlaceInfoObj();
				dto.setaMax(rs.getString("APARTMENT_LEASED_MAX"));
				dto.setaMin(rs.getString("APARTMENT_LEASED_MIN"));
				dto.setLocation_id(rs.getString("LOCATION_ID"));
				dto.setLocationName(rs.getString("LOCATION_NAME"));
				dto.sethMax(rs.getString("HOUSE_OWNED_MAX"));
				dto.sethMin(rs.getString("HOUSE_OWNED_MIN"));
				dto.setGroceries(rs.getString("GROCERIES"));
				dto.setUtilities(rs.getString("UTILITIES"));
				dto.setOtherExpenses(rs.getString("OTHER_EXPENSES"));
				dto.setMedicalExpenses(rs.getString("MEDICAL_EXPENSES"));
				dto.setLocationType(rs.getString("LOCATION_TYPE"));
				dto.setClimate(rs.getString("CLIMATE"));
				dto.setSearchCount(rs.getString("SEARCH_COUNT"));
				dto.setMonthlyExp((Integer.parseInt(dto.getGroceries())+Integer.parseInt(dto.getUtilities())+Integer.parseInt(dto.getOtherExpenses()))+"");
				dto.setInfo(getInfo(dto.getLocation_id()));
				dto.setImages(getPicture(dto.getLocation_id()));
				dto.setRecreationInfo(getRecreation(dto.getLocation_id()));
				finalDisplayList.add(dto);
			}

			System.out.println("FINAL DISPLAY SIZE" + finalDisplayList.size());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return new ArrayList<PlaceInfoObj>(finalDisplayList.subList(0, 10));
	
	}
	
	List<String> getPicture(String locationId) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> info = new ArrayList<String>();
		try {
			String sql = "SELECT IMAGE_PATH FROM LOCATION_IMAGES WHERE LOCATION_ID = "
					+ locationId;
			con = new DataSource().getConnection();
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				info.add(rs.getString("IMAGE_PATH"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		} finally {

			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		return info;
	}

	String getInfo(String locationId) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String info = "";
		try {
			String sql = "SELECT DESCRIPTION FROM LOCATION_DESC WHERE LOCATION_ID = "
					+ locationId;
			con = new DataSource().getConnection();
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				info = info + rs.getString("DESCRIPTION");
			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		} finally {

			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		return info;
	}

	List<Recreation> getRecreation(String location_id) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Recreation ref = null;
		List<Recreation> info = new ArrayList<Recreation>();
		try {
			String sql = "SELECT * FROM LOCATION_RECREATION WHERE LOCATION_ID = "
					+ location_id;
			con = new DataSource().getConnection();
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				ref = new Recreation();
				ref.setType(rs.getString("RECREATION_TYPE"));
				ref.setName(rs.getString("RECREATION_NAME"));
				ref.setLat(rs.getString("LATITUDE"));
				ref.setLang(rs.getString("LONGITUDE"));
				info.add(ref);
			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		} finally {

			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		return info;

	}

	

}
