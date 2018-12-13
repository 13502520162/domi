package com.domi.model.loanmarket;

import java.util.Date;

import com.domi.support.utils.DateUtil;

/**
 * 借款平台
 * @author shexiao
 *
 */
public class LoanPlatform {
	
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
	private String userRole = "";// 用户角色
	private String userMaterial = "";// 用户资料
	private String userUsage = "";// 用途
	private int userCount = 0;//申请人数
	private int beOnline = 0;// 是否立即上线
	private Date subscribeTime;// 预约上线时间
	private int hasSubscribe = 0;// 是否已经预约上线
	private int isTop = 0;// 是否置顶
	private int platformState = 0;// 平台状态 0无1最热2推荐
	private int state = 0;// 状态 0待上线1已上线2下线
	private Date addTime;
	private Date updatetime;
	private Date offlineTime;// 下线时间
	private String platformUrl = ""; //平台链接
	
	
	private String range = ""; //期限
	private String settleRule = "";
	private String settleDate = "";
	private String subscribeTimeStr = "";
	
	private float passRatio = 96.75f;	
	private String labels = "";	
	
	private int orderNo = 999;
	
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
	public String getPlatformUrl() {
		return platformUrl;
	}
	public void setPlatformUrl(String platformUrl) {
		this.platformUrl = platformUrl;
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
	public String getUserRole() {
		return userRole;
	}
	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}
	public String getUserMaterial() {
		return userMaterial;
	}
	public void setUserMaterial(String userMaterial) {
		this.userMaterial = userMaterial;
	}
	public String getUserUsage() {
		return userUsage;
	}
	public void setUserUsage(String userUsage) {
		this.userUsage = userUsage;
	}
	public int getUserCount() {
		return userCount;
	}
	public void setUserCount(int userCount) {
		this.userCount = userCount;
	}
	public int getBeOnline() {
		return beOnline;
	}
	public void setBeOnline(int beOnline) {
		this.beOnline = beOnline;
	}
	public Date getSubscribeTime() {
		return subscribeTime;
	}
	public void setSubscribeTime(Date subscribeTime) {
		if (subscribeTime != null) {
			this.subscribeTimeStr = DateUtil.getDateString(subscribeTime, "yyyy-MM-dd HH:mm");
		}
		this.subscribeTime = subscribeTime;
	}
	public int getHasSubscribe() {
		return hasSubscribe;
	}
	public void setHasSubscribe(int hasSubscribe) {
		this.hasSubscribe = hasSubscribe;
	}
	public int getIsTop() {
		return isTop;
	}
	public void setIsTop(int isTop) {
		this.isTop = isTop;
	}
	public int getPlatformState() {
		return platformState;
	}
	public void setPlatformState(int platformState) {
		this.platformState = platformState;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public Date getAddTime() {
		return addTime;
	}
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
	public Date getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}
	public Date getOfflineTime() {
		return offlineTime;
	}
	public void setOfflineTime(Date offlineTime) {
		this.offlineTime = offlineTime;
	}
	public String getLabels() {
		return labels;
	}
	public void setLabels(String labels) {
		this.labels = labels;
	}
	public float getPassRatio() {
		return passRatio;
	}
	public void setPassRatio(float passRatio) {
		this.passRatio = passRatio;
	}
	public int getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(int orderNo) {
		this.orderNo = orderNo;
	}
}
