package com.domi.model;

import java.util.Date;

public class User {
	
	private int id = -1;
	private String cellphone = "";
	private String password = "";
	private String cookie =  "";
	private String platform = "";
	private String token =  "";
	private Date registerTime = new Date();
	private String os_version ="";
	private String os_name = "";
	private int inviteId = 0;
	private int hadGenInviteLink = 0;
	
	/*借款相关信息*/
	private int hasSubmitInfo=0;
	
	private String name;
	private String idNumber;
	private int zmScore;
	private String educationBackground;
	private int loanMoney;
	private String loanType;
	private String labels;
	
	private Date submitInfoTime;
	
	private String address="";
	
	
	
	public int getHadGenInviteLink() {
		return hadGenInviteLink;
	}
	public void setHadGenInviteLink(int hadGenInviteLink) {
		this.hadGenInviteLink = hadGenInviteLink;
	}

	public int getInviteId() {
		return inviteId;
	}
	public void setInviteId(int inviteId) {
		this.inviteId = inviteId;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCellphone() {
		return cellphone;
	}
	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getCookie() {
		return cookie;
	}
	public void setCookie(String cookie) {
		this.cookie = cookie;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Date getRegisterTime() {
		return registerTime;
	}
	public void setRegisterTime(Date registerTime) {
		this.registerTime = registerTime;
	}
	public int getHasSubmitInfo() {
		return hasSubmitInfo;
	}
	public void setHasSubmitInfo(int hasSubmitInfo) {
		this.hasSubmitInfo = hasSubmitInfo;
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
	public String getOs_name() {
		return os_name;
	}
	public void setOs_name(String os_name) {
		this.os_name = os_name;
	}
	public String getOs_version() {
		return os_version;
	}
	public void setOs_version(String os_version) {
		this.os_version = os_version;
	}
	public Date getSubmitInfoTime() {
		return submitInfoTime;
	}
	public void setSubmitInfoTime(Date submitInfoTime) {
		this.submitInfoTime = submitInfoTime;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
}
