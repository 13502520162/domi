package com.domi.model;

import java.util.Date;

public class SafeCall {

	private String requestInfo;
	private String responseInfo;
	private int userId;
	private Date addTime = new Date();
	private String errCode;
	
	public SafeCall(){
		
	}
	
	public SafeCall(int userId, String requestInfo, String responseInfo, String errCode){
		this.userId = userId;
		this.requestInfo = requestInfo;
		this.responseInfo = responseInfo;
		this.errCode = errCode;
	}
	
	
	public String getRequestInfo() {
		return requestInfo;
	}

	public void setRequestInfo(String requestInfo) {
		this.requestInfo = requestInfo;
	}

	public String getResponseInfo() {
		return responseInfo;
	}

	public void setResponseInfo(String responseInfo) {
		this.responseInfo = responseInfo;
	}

	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public Date getAddTime() {
		return addTime;
	}
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
	public String getErrCode() {
		return errCode;
	}
	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}
	
	
	
}
