package com.domi.model.loanmarket;

public class LoanPlatformData {

	private int id;
	private String name = ""; // 名称
	private String platformDesc = "";// 描述
	private String logo = ""; 
	
	private int clickPeopleCount;
	private int clickCount;
	private int registerPeopleCount;
	private int registerCount;
	
	
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
	public String getPlatformDesc() {
		return platformDesc;
	}
	public void setPlatformDesc(String platformDesc) {
		this.platformDesc = platformDesc;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public int getClickPeopleCount() {
		return clickPeopleCount;
	}
	public void setClickPeopleCount(int clickPeopleCount) {
		this.clickPeopleCount = clickPeopleCount;
	}
	public int getClickCount() {
		return clickCount;
	}
	public void setClickCount(int clickCount) {
		this.clickCount = clickCount;
	}
	public int getRegisterPeopleCount() {
		return registerPeopleCount;
	}
	public void setRegisterPeopleCount(int registerPeopleCount) {
		this.registerPeopleCount = registerPeopleCount;
	}
	public int getRegisterCount() {
		return registerCount;
	}
	public void setRegisterCount(int registerCount) {
		this.registerCount = registerCount;
	}

	
	
}
