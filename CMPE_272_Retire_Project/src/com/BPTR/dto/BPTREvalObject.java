package com.BPTR.dto;

public class BPTREvalObject {
	
	String location_id;
	String location_name;
    boolean isLocation;
	int expense_1;
    int expense_2_wMed;
    int leased_min;
    int leased_max;
    int owned_min;
    int owned_max;
    double leased_avg;
    double owned_avg;
    String comment;
    String iterationNumber;
    
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getIterationNumber() {
		return iterationNumber;
	}
	public void setIterationNumber(String iterationNumber) {
		this.iterationNumber = iterationNumber;
	}
	public String getLocation_id() {
		return location_id;
	}
	public void setLocation_id(String location_id) {
		this.location_id = location_id;
	}
	public String getLocation_name() {
		return location_name;
	}
	public void setLocation_name(String location_name) {
		this.location_name = location_name;
	}
	public boolean isLocation() {
		return isLocation;
	}
	public void setLocation(boolean isLocation) {
		this.isLocation = isLocation;
	}
	public int getExpense_1() {
		return expense_1;
	}
	public void setExpense_1(int expense_1) {
		this.expense_1 = expense_1;
	}
	public int getExpense_2_wMed() {
		return expense_2_wMed;
	}
	public void setExpense_2_wMed(int expense_2_wMed) {
		this.expense_2_wMed = expense_2_wMed;
	}
	public int getLeased_min() {
		return leased_min;
	}
	public void setLeased_min(int leased_min) {
		this.leased_min = leased_min;
	}
	public int getLeased_max() {
		return leased_max;
	}
	public void setLeased_max(int leased_max) {
		this.leased_max = leased_max;
	}
	public int getOwned_min() {
		return owned_min;
	}
	public void setOwned_min(int owned_min) {
		this.owned_min = owned_min;
	}
	public int getOwned_max() {
		return owned_max;
	}
	public void setOwned_max(int owned_max) {
		this.owned_max = owned_max;
	}
	public double getLeased_avg() {
		return leased_avg;
	}
	public void setLeased_avg(double leased_avg) {
		this.leased_avg = leased_avg;
	}
	public double getOwned_avg() {
		return owned_avg;
	}
	public void setOwned_avg(double owned_avg) {
		this.owned_avg = owned_avg;
	}
    
    
    
}
