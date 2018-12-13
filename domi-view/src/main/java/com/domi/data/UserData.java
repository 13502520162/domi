package com.domi.data;

public class UserData {

	private int id;
	private String cellphone;
	private String name = "";
	private String idNumber = "";
	private int zmScore;
	private String educationBackground = "";
	private int loanMoney;
	private String loanType = "";
	private String labels = "";
	
	private int registerPlatformCount;
	private int clickCount;
	private int hasSubmitInfo;
	private String address;
	private int hadGenInviteLink = 0;
	
	
	public int getHadGenInviteLink() {
		return hadGenInviteLink;
	}
	public void setHadGenInviteLink(int hadGenInviteLink) {
		this.hadGenInviteLink = hadGenInviteLink;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public String getEducationBackground() {
		return educationBackground;
	}
	public void setEducationBackground(String educationBackground) {
		this.educationBackground = educationBackground;
	}
	public int getLoanMoney() {
		return loanMoney;
	}
	public void setLoanMoney(int loanMoney) {
		this.loanMoney = loanMoney;
	}
	public String getLoanType() {
		return loanType;
	}
	public void setLoanType(String loanType) {
		this.loanType = loanType;
	}
	public String getLabels() {
		return labels;
	}
	public void setLabels(String labels) {
		this.labels = labels;
	}
	public int getRegisterPlatformCount() {
		return registerPlatformCount;
	}
	public void setRegisterPlatformCount(int registerPlatformCount) {
		this.registerPlatformCount = registerPlatformCount;
	}
	public int getClickCount() {
		return clickCount;
	}
	public void setClickCount(int clickCount) {
		this.clickCount = clickCount;
	}
	public String getCellphone() {
		return cellphone;
	}
	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}
	public int getHasSubmitInfo() {
		return hasSubmitInfo;
	}
	public void setHasSubmitInfo(int hasSubmitInfo) {
		this.hasSubmitInfo = hasSubmitInfo;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
}
