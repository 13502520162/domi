package com.domi.model.loanmarket;


/**
 * 借款平台
 * @author shexiao
 *
 */
public class AppLoanPlatformDto {
	
	private int id = -1;
	private String name = ""; // 名称
	private String platformDesc = "";// 描述
	private String logo = ""; 
	private int minMoney = 0;// 最低额度
	private int maxMoney = 0;// 最高额度
	private String dayRange = "";// 天范围
	private String monthRange = "";// 月范围
	private float monthRatio = 0f;// 月利率
	
	private String applyDesc = "";// 申请条件
	private String requireDesc = "";// 所需条件
	private String applyPoint = "";// 申请要点
	
	private String range = ""; //期限
	private String settleRule = "";
	private String settleDate = "";
	private String subscribeTimeStr = "";
	
	private float passRaitio ;
	private String labels;	
	
	public String getSubscribeTimeStr() {
		return subscribeTimeStr;
	}
	public void setSubscribeTimeStr(String subscribeTimeStr) {
		this.subscribeTimeStr = subscribeTimeStr;
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
	public String getRange() {
		return range;
	}
	public void setRange(String range) {
		this.range = range;
	}
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
	public int getMinMoney() {
		return minMoney;
	}
	public void setMinMoney(int minMoney) {
		this.minMoney = minMoney;
	}
	public int getMaxMoney() {
		return maxMoney;
	}
	public void setMaxMoney(int maxMoney) {
		this.maxMoney = maxMoney;
	}
	public String getDayRange() {
		return dayRange;
	}
	public void setDayRange(String dayRange) {
		this.dayRange = dayRange;
	}
	public String getMonthRange() {
		return monthRange;
	}
	public void setMonthRange(String monthRange) {
		this.monthRange = monthRange;
	}
	public float getMonthRatio() {
		return monthRatio;
	}
	public void setMonthRatio(float monthRatio) {
		this.monthRatio = monthRatio;
	}
	public String getApplyDesc() {
		return applyDesc;
	}
	public void setApplyDesc(String applyDesc) {
		this.applyDesc = applyDesc;
	}
	public String getRequireDesc() {
		return requireDesc;
	}
	public void setRequireDesc(String requireDesc) {
		this.requireDesc = requireDesc;
	}
	public String getApplyPoint() {
		return applyPoint;
	}
	public void setApplyPoint(String applyPoint) {
		this.applyPoint = applyPoint;
	}
	public String getLabels() {
		return labels;
	}
	public void setLabels(String labels) {
		this.labels = labels;
	}
	public float getPassRaitio() {
		return passRaitio;
	}
	public void setPassRaitio(float passRaitio) {
		this.passRaitio = passRaitio;
	}
	
}
