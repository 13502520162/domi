package com.domi.model;

public class LoanInfo {

	private int id;
	private int userId;
	private String name;
	private String idNumber;
	private int zmScore;
	private int educationBackground;
	private int loanMoney;
	private int loanType;
	private String lables;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIdNumber() {
		return idNumber;
	}
	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}
	public int getZmScore() {
		return zmScore;
	}
	public void setZmScore(int zmScore) {
		this.zmScore = zmScore;
	}
	public int getEducationBackground() {
		return educationBackground;
	}
	public void setEducationBackground(int educationBackground) {
		this.educationBackground = educationBackground;
	}
	public int getLoanMoney() {
		return loanMoney;
	}
	public void setLoanMoney(int loanMoney) {
		this.loanMoney = loanMoney;
	}
	public int getLoanType() {
		return loanType;
	}
	public void setLoanType(int loanType) {
		this.loanType = loanType;
	}
	public String getLables() {
		return lables;
	}
	public void setLables(String lables) {
		this.lables = lables;
	}
	
	
}
