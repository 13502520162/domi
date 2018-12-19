package com.domi.model;

import java.util.Date;


public class AccountBook {
	
	private int id;
	private int userId;
	private String platformName;
	private float repaymentMoney;
	private String repaymentDate;
	/**还款方式  0：一次性还清    1：分期付款*/
	private int repaymentType;
	private int loanTerm;
	private String loanTime;
	private int repaymentDay;
	private Date addTime = new Date();
	private String remark;
	
	private float money;
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
	public String getPlatformName() {
		return platformName;
	}
	public void setPlatformName(String platformName) {
		this.platformName = platformName;
	}
	public float getRepaymentMoney() {
		return repaymentMoney;
	}
	public void setRepaymentMoney(float repaymentMoney) {
		this.repaymentMoney = repaymentMoney;
	}
	public String getRepaymentDate() {
		return repaymentDate;
	}
	public void setRepaymentDate(String repaymentDate) {
		this.repaymentDate = repaymentDate;
	}
	public int getRepaymentType() {
		return repaymentType;
	}
	public void setRepaymentType(int repaymentType) {
		this.repaymentType = repaymentType;
	}
	public int getLoanTerm() {
		return loanTerm;
	}
	public void setLoanTerm(int loanTerm) {
		this.loanTerm = loanTerm;
	}
	public String getLoanTime() {
		return loanTime;
	}
	public void setLoanTime(String loanTime) {
		this.loanTime = loanTime;
	}
	public int getRepaymentDay() {
		return repaymentDay;
	}
	public void setRepaymentDay(int repaymentDay) {
		this.repaymentDay = repaymentDay;
	}
	public Date getAddTime() {
		return addTime;
	}
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public float getMoney() {
		return money;
	}
	public void setMoney(float money) {
		this.money = money;
	}
	
}
