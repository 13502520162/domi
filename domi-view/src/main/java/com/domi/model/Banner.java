package com.domi.model;

public class Banner implements Comparable<Banner>{

	private int id = -1;
	private String url = "";
	private String imgUrl = "";
	private String background = "";

	/**
	 * 排序
	 */
	private int orderNo = 0;
	
	/**
	 * 用于区分首页banner还是限时特惠banner. 
	 */
	private int type = 2;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public int getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(int orderNo) {
		this.orderNo = orderNo;
	}
	public String getBackground() {
		return background;
	}

	public void setBackground(String background) {
		this.background = background;
	}
	@Override
	public int compareTo(Banner o) {		
		return this.getOrderNo() - o.getOrderNo();
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
	
}
