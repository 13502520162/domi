package com.domi.model;

public class Version {

	private int id;
	private int currentVersion = -1;
	private String downLoadUrl = "";
	private String description = "";
	private int type=-1;
	private int isForceUpdate = 0;
	
	private String remark = "";
	public int getCurrentVersion() {
		return currentVersion;
	}
	public void setCurrentVersion(int currentVersion) {
		this.currentVersion = currentVersion;
	}
	public String getDownLoadUrl() {
		return downLoadUrl;
	}
	public void setDownLoadUrl(String downLoadUrl) {
		this.downLoadUrl = downLoadUrl;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getIsForceUpdate() {
		return isForceUpdate;
	}
	public void setIsForceUpdate(int isForceUpdate) {
		this.isForceUpdate = isForceUpdate;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
