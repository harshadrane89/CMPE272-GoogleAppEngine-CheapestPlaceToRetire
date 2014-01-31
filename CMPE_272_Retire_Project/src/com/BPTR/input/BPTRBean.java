package com.BPTR.input;

import java.util.List;
 
public class BPTRBean {
 
	private String monthlyIncome;
	private boolean location;
	private boolean housingPref;
	private List<String> housing_type;
	private long houseCost;
	private boolean medical_expenses;
	private List<String> otherPreferences;
	private int numberOfResults=5;
	
	public int getNumberOfResults() {
		return numberOfResults;
	}
	public void setNumberOfResults(int numberOfResults) {
		this.numberOfResults = numberOfResults;
	}
	public String getMonthlyIncome() {
		return monthlyIncome;
	}
	public void setMonthlyIncome(String monthlyIncome) {
		this.monthlyIncome = monthlyIncome;
	}
	public boolean isLocation() {
		return location;
	}
	public void setLocation(boolean location) {
		this.location = location;
	}
	public boolean isHousingPref() {
		return housingPref;
	}
	public void setHousingPref(boolean housingPref) {
		this.housingPref = housingPref;
	}
	public List<String> getHousing_type() {
		return housing_type;
	}
	public void setHousing_type(List<String> housing_type) {
		this.housing_type = housing_type;
	}
	public long getHouseCost() {
		return houseCost;
	}
	public void setHouseCost(long houseCost) {
		this.houseCost = houseCost;
	}
	public boolean isMedical_expenses() {
		return medical_expenses;
	}
	public void setMedical_expenses(boolean medical_expenses) {
		this.medical_expenses = medical_expenses;
	}
	public List<String> getOtherPreferences() {
		return otherPreferences;
	}
	public void setOtherPreferences(List<String> otherPreferences) {
		this.otherPreferences = otherPreferences;
	}
	
	
		
}
