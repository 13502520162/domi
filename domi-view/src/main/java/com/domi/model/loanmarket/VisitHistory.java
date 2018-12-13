package com.domi.model.loanmarket;

import java.util.Date;

import com.domi.support.utils.DateUtil;

public class VisitHistory {

	private int id;
	private int userId;
	private int platformId;
	private String platformName;
	private String intro;
	private int hasRegiste;
	private Date updateTime = new Date();
	
	private int visitCount = 1;
	private int registeCount = 0;
	
	private String platformLogoUrl = "";
//	private String updateTimeForApp = "";
	
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
	public String getIntro() {
		return intro;
	}
	public void setIntro(String intro) {
		this.intro = intro;
	}
	public int getHasRegiste() {
		return hasRegiste;
	}
	public void setHasRegiste(int hasRegiste) {
		this.hasRegiste = hasRegiste;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public int getRegisteCount() {
		return registeCount;
	}
	public void setRegisteCount(int registeCount) {
		this.registeCount = registeCount;
	}
	public int getVisitCount() {
		return visitCount;
	}
	public void setVisitCount(int visitCount) {
		this.visitCount = visitCount;
	}
	public String getUpdateTimeForApp() {
		return DateUtil.getDatetimeString(updateTime);
	}
	public String getPlatformLogoUrl() {
		return platformLogoUrl;
	}
	public void setPlatformLogoUrl(String platformLogoUrl) {
		this.platformLogoUrl = platformLogoUrl;
	}
	
}
