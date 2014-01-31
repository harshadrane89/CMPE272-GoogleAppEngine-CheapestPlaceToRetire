package com.BPTR.dto;

import java.util.List;

public class PlaceInfoObj 
{
	String country_id;
	String country_name;
	String location_id;
	String locationType;
	
	String locationName;
	String lat;
	String lang;
	String groceries;
	String utilities;
	String medicalExpenses;
	String otherExpenses;
	String climate;
	String aMax;
	String aMin;
	String comment;
	String monthlyExpwM;
	String monthlyExp;
	String info;
	List<String> images;
	List<Recreation> recreationInfo;
	String searchCount;
	
	public String getSearchCount() {
		return searchCount;
	}
	public void setSearchCount(String searchCount) {
		this.searchCount = searchCount;
	}
	public List<Recreation> getRecreationInfo() {
		return recreationInfo;
	}
	public void setRecreationInfo(List<Recreation> recreationInfo) {
		this.recreationInfo = recreationInfo;
	}
	public String getMonthlyExpwM() {
		return monthlyExpwM;
	}
	public void setMonthlyExpwM(String monthlyExpwM) {
		this.monthlyExpwM = monthlyExpwM;
	}
	public String getMonthlyExp() {
		return monthlyExp;
	}
	public void setMonthlyExp(String monthlyExp) {
		this.monthlyExp = monthlyExp;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public List<String> getImages() {
		return images;
	}
	public void setImages(List<String> images) {
		this.images = images;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getLocationType() {
		return locationType;
	}
	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}
	public String getCountry_id() {
		return country_id;
	}
	public void setCountry_id(String country_id) {
		this.country_id = country_id;
	}
	public String getCountry_name() {
		return country_name;
	}
	public void setCountry_name(String country_name) {
		this.country_name = country_name;
	}
	public String getLocation_id() {
		return location_id;
	}
	public void setLocation_id(String location_id) {
		this.location_id = location_id;
	}
	String hMax;
	String hMin;
	
	
	public String getLocationName() {
		return locationName;
	}
	public void setLocationName(String locationName) {
		this.locationName = locationName;
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
	public String getGroceries() {
		return groceries;
	}
	public void setGroceries(String groceries) {
		this.groceries = groceries;
	}
	public String getUtilities() {
		return utilities;
	}
	public void setUtilities(String utilities) {
		this.utilities = utilities;
	}
	public String getMedicalExpenses() {
		return medicalExpenses;
	}
	public void setMedicalExpenses(String medicalExpenses) {
		this.medicalExpenses = medicalExpenses;
	}
	public String getOtherExpenses() {
		return otherExpenses;
	}
	public void setOtherExpenses(String otherExpenses) {
		this.otherExpenses = otherExpenses;
	}
	public String getClimate() {
		return climate;
	}
	public void setClimate(String climate) {
		this.climate = climate;
	}
	public String getaMax() {
		return aMax;
	}
	public void setaMax(String aMax) {
		this.aMax = aMax;
	}
	public String getaMin() {
		return aMin;
	}
	public void setaMin(String aMin) {
		this.aMin = aMin;
	}
	public String gethMax() {
		return hMax;
	}
	public void sethMax(String hMax) {
		this.hMax = hMax;
	}
	public String gethMin() {
		return hMin;
	}
	public void sethMin(String hMin) {
		this.hMin = hMin;
	}
	
	
}

