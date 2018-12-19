package com.domi.model;

public class Config {

	private String servicePhone;
	private String serviceQQ;
	private String serviceWx;
	private String bank1ImgUrl;
	private String bank1GotoUrl;
	private String bank2ImgUrl;
	private String bank2GotoUrl;
	private String moreBankUrl;
	private int startPingang = 0;
	
	private int alipay_mode = 0;
	private int check_version = 100;
	
	private int android_mode = 0;
	private int android_check_version = 100;
	
	public String getServicePhone() {
		return servicePhone;
	}
	public void setServicePhone(String servicePhone) {
		this.servicePhone = servicePhone;
	}
	public String getServiceQQ() {
		return serviceQQ;
	}
	public void setServiceQQ(String serviceQQ) {
		this.serviceQQ = serviceQQ;
	}
	public String getServiceWx() {
		return serviceWx;
	}
	public void setServiceWx(String serviceWx) {
		this.serviceWx = serviceWx;
	}
	public String getBank1ImgUrl() {
		return bank1ImgUrl;
	}
	public void setBank1ImgUrl(String bank1ImgUrl) {
		this.bank1ImgUrl = bank1ImgUrl;
	}
	public String getBank1GotoUrl() {
		return bank1GotoUrl;
	}
	public void setBank1GotoUrl(String bank1GotoUrl) {
		this.bank1GotoUrl = bank1GotoUrl;
	}
	public String getBank2ImgUrl() {
		return bank2ImgUrl;
	}
	public void setBank2ImgUrl(String bank2ImgUrl) {
		this.bank2ImgUrl = bank2ImgUrl;
	}
	public String getBank2GotoUrl() {
		return bank2GotoUrl;
	}
	public void setBank2GotoUrl(String bank2GotoUrl) {
		this.bank2GotoUrl = bank2GotoUrl;
	}
	public String getMoreBankUrl() {
		return moreBankUrl;
	}
	public void setMoreBankUrl(String moreBankUrl) {
		this.moreBankUrl = moreBankUrl;
	}
	public int getStartPingang() {
		return startPingang;
	}
	public void setStartPingang(int startPingang) {
		this.startPingang = startPingang;
	}
	public int getAlipay_mode() {
		return alipay_mode;
	}
	public void setAlipay_mode(int alipay_mode) {
		this.alipay_mode = alipay_mode;
	}
	public int getCheck_version() {
		return check_version;
	}
	public void setCheck_version(int check_version) {
		this.check_version = check_version;
	}
	public int getAndroid_check_version() {
		return android_check_version;
	}
	public void setAndroid_check_version(int android_check_version) {
		this.android_check_version = android_check_version;
	}
	public int getAndroid_mode() {
		return android_mode;
	}
	public void setAndroid_mode(int android_mode) {
		this.android_mode = android_mode;
	}
}
