package com.domi.model;

import java.util.Date;

import com.domi.support.utils.DateUtil;

public class InviteUser {
	private Integer inviteId;
	private String account;
	private String password;
	private String token;
	private Date addtime;
	private String addtimeFormat;
	
	
	public String getAddtimeFormat() {
		return addtimeFormat;
	}
	public void setAddtimeFormat(String addtimeFormat) {
		this.addtimeFormat = addtimeFormat;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}

	public Integer getInviteId() {
		return inviteId;
	}
	public void setInviteId(Integer inviteId) {
		this.inviteId = inviteId;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Date getAddtime() {
		return addtime;
	}
	public void setAddtime(Date addtime) {
		if (addtime != null) {
			this.addtimeFormat = DateUtil.getDateString(addtime, "yyyy-MM-dd HH:mm");
		}
		this.addtime = addtime;
	}
	
	
}
