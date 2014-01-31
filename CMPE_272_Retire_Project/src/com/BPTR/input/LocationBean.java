package com.BPTR.input;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.BPTR.db.CountryMaster;

public class LocationBean extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2294522623819176208L;
	
	private String location_name;
	private String lat;
	private String lang;
	private String aptMin;
	private String aptMax;
	private String houseMin;
	private String houseMax;
	private String groceryExpenses;
	private String utilitiesExpenses;
	private String otherExpenses;
	private String medicalExpenses;
	
	public String getLocation_name() {
		return location_name;
	}
	public void setLocation_name(String location_name) {
		this.location_name = location_name;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public String getLang() {
		return lang;
	}
	public void setLang(String lang) {
		this.lang = lang;
	}
	public String getAptMin() {
		return aptMin;
	}
	public void setAptMin(String aptMin) {
		this.aptMin = aptMin;
	}
	public String getAptMax() {
		return aptMax;
	}
	public void setAptMax(String aptMax) {
		this.aptMax = aptMax;
	}
	public String getHouseMin() {
		return houseMin;
	}
	public void setHouseMin(String houseMin) {
		this.houseMin = houseMin;
	}
	public String getHouseMax() {
		return houseMax;
	}
	public void setHouseMax(String houseMax) {
		this.houseMax = houseMax;
	}
	public String getGroceryExpenses() {
		return groceryExpenses;
	}
	public void setGroceryExpenses(String groceryExpenses) {
		this.groceryExpenses = groceryExpenses;
	}
	public String getUtilitiesExpenses() {
		return utilitiesExpenses;
	}
	public void setUtilitiesExpenses(String utilitiesExpenses) {
		this.utilitiesExpenses = utilitiesExpenses;
	}
	public String getOtherExpenses() {
		return otherExpenses;
	}
	public void setOtherExpenses(String otherExpenses) {
		this.otherExpenses = otherExpenses;
	}
	public String getMedicalExpenses() {
		return medicalExpenses;
	}
	public void setMedicalExpenses(String medicalExpenses) {
		this.medicalExpenses = medicalExpenses;
	}
	
	
}
