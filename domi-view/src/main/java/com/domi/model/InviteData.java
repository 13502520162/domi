package com.domi.model;

public class InviteData {
	private Integer inviteId;
	private String inviteDate;
	private Integer inviteCount = 0;
	private String account;

	public Integer getInviteId() {
		return inviteId;
	}
	public void setInviteId(Integer inviteId) {
		this.inviteId = inviteId;
	}
	public String getInviteDate() {
		return inviteDate;
	}
	public void setInviteDate(String inviteDate) {
		this.inviteDate = inviteDate;
	}
	public Integer getInviteCount() {
		return inviteCount;
	}
	public void setInviteCount(Integer inviteCount) {
		this.inviteCount = inviteCount;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	
	
}
