package com.domi.model.loanmarket;

import java.util.Date;

public class VisitHistoryInfo {

	private int id;
	private int userId;
	private int platformId;
	private String platformName;
	
	private Date visitTime = new Date();
	/**访问类型  0：点击   1：注册**/
	private int type = 0;
	
	public VisitHistoryInfo(){
		
	}
	
	public VisitHistoryInfo(int userId, int platformId, String platformName, int type){
		this.userId = userId;		
		this.platformId = platformId;
		this.platformName = platformName;
		this.type = type;
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
	public int getPlatformId() {
		return platformId;
	}
	public void setPlatformId(int platformId) {
		this.platformId = platformId;
	}
	public String getPlatformName() {
		return platformName;
	}
	public void setPlatformName(String platformName) {
		this.platformName = platformName;
	}
	public Date getVisitTime() {
		return visitTime;
	}
	public void setVisitTime(Date visitTime) {
		this.visitTime = visitTime;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
	
	
		
}
