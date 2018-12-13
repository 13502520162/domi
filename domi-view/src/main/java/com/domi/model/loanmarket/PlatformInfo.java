package com.domi.model.loanmarket;

import java.util.Date;

public class PlatformInfo {
	private int id = -1;
	private int loanPlatformId = -1;// 借款平台id
	private String settleRule = "";// 结算规则
	private String settleDate = "";// 结算日期
	private String backendUrl = "";// 后台链接
	private String backendAccount = "";// 后台帐号
	private String backendPassword = "";// 后台密码
	private String companyName = "";// 公司名称
	private String companyAddr = "";// 公司地址
	private String companyPhone = "";// 公司电话
	private String receiptType = "";// 发票类型
	private String receiptContent = "";// 发票内容
	private String taxNumber = "";// 税号
	private String openbank = "";// 开户行
	private String bankAccount = "";// 开户帐号
	private String sendAddr = "";// 寄送地址
	private String receiveName = "";// 收件人名称
	private String recervePhone = "";// 收件人手机
	private Date addTime;
	private Date updateTime;
	
	private int state = 0;
	private String name = "";
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getLoanPlatformId() {
		return loanPlatformId;
	}
	public void setLoanPlatformId(int loanPlatformId) {
		this.loanPlatformId = loanPlatformId;
	}
	public String getSettleRule() {
		return settleRule;
	}
	public void setSettleRule(String settleRule) {
		this.settleRule = settleRule;
	}
	public String getSettleDate() {
		return settleDate;
	}
	public void setSettleDate(String settleDate) {
		this.settleDate = settleDate;
	}
	public String getBackendUrl() {
		return backendUrl;
	}
	public void setBackendUrl(String backendUrl) {
		this.backendUrl = backendUrl;
	}
	public String getBackendAccount() {
		return backendAccount;
	}
	public void setBackendAccount(String backendAccount) {
		this.backendAccount = backendAccount;
	}
	public String getBackendPassword() {
		return backendPassword;
	}
	public void setBackendPassword(String backendPassword) {
		this.backendPassword = backendPassword;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getCompanyAddr() {
		return companyAddr;
	}
	public void setCompanyAddr(String companyAddr) {
		this.companyAddr = companyAddr;
	}
	public String getCompanyPhone() {
		return companyPhone;
	}
	public void setCompanyPhone(String companyPhone) {
		this.companyPhone = companyPhone;
	}
	public String getReceiptType() {
		return receiptType;
	}
	public void setReceiptType(String receiptType) {
		this.receiptType = receiptType;
	}
	public String getReceiptContent() {
		return receiptContent;
	}
	public void setReceiptContent(String receiptContent) {
		this.receiptContent = receiptContent;
	}
	public String getTaxNumber() {
		return taxNumber;
	}
	public void setTaxNumber(String taxNumber) {
		this.taxNumber = taxNumber;
	}
	public String getOpenbank() {
		return openbank;
	}
	public void setOpenbank(String openbank) {
		this.openbank = openbank;
	}
	public String getBankAccount() {
		return bankAccount;
	}
	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}
	public String getSendAddr() {
		return sendAddr;
	}
	public void setSendAddr(String sendAddr) {
		this.sendAddr = sendAddr;
	}
	public String getReceiveName() {
		return receiveName;
	}
	public void setReceiveName(String receiveName) {
		this.receiveName = receiveName;
	}
	public String getRecervePhone() {
		return recervePhone;
	}
	public void setRecervePhone(String recervePhone) {
		this.recervePhone = recervePhone;
	}
	public Date getAddTime() {
		return addTime;
	}
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	
}
