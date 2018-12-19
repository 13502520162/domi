package com.domi.model;

import java.util.Date;

public class LoginPosition {

	private int id;
	private int userId;
	private String address;
	private Date loginTime = new Date();
	
	public LoginPosition() {
		
	}
	
	public LoginPosition(int userId, String address){
		this.userId = userId;
		this.address = address;
	}
	
	
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
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Date getLoginTime() {
		return loginTime;
	}
	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}
	
	
}
