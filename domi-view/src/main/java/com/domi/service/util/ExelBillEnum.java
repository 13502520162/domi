package com.domi.service.util;

/**
 * 
 * @author chenhuanshuo
 *
 * @DATE 2017年4月8日
 */
public enum ExelBillEnum {
	/**单例对象**/
	CHANNEL("Channel","通道:0@分期呗,1@海印"),          
	ORDERTYPE("OrderType","订单类型:0@商品,1@白条"),
	ORDERNUM("OrderNum"," 订单编号"),
	USERNAME("UserName","用户姓名"), 
	USERPHONE("UserPhone","用户手机"), 
	TITLE("Title","账单名称"), 
	MONEY("Money","应还总金额"), 
	PRIMECOST("PrimeCost","应还本金"), 
	SERVICEFEE("ServiceFee","服务费"), 
	INTEREST("Interest","利息"), 
	LATEFEE ("LateFee","滞纳金"),
	POUNDAGE("Poundage","手续费"), 
	DEDIT("Dedit","违约金"), 
	TERM("Term","总分期数"),
	CURRENTTERM("CurrentTerm","当前期数"),
	LATESTPAYMENTDATE("LatestPaymentDate","最迟还款日"),
	PAYMENTDATE("PaymentDate","还款时间"),
	STATE("State","账单状态:0@未还款,1@异常单,2@坏账,3@已还款,4@已失效"),
	REMARK("Remark","账单备注"),
	COMMENT("Comment","备注信息"),
	
	/**空字符串**/
	EMPTY_MSG("-1","全部"),
	/**错误信息**/
	FAIL_MSG("-2","信息获取失败:"),
	;
	/**常量信息**/
	private String msg;
	/**常量值**/
	private String code;
	
	private ExelBillEnum(){}
	private ExelBillEnum(String code,String msg){
		this.code = code;
		this.msg = msg;
	}
	
	public static String getMsgByCode(String code){
		try {
			ExelBillEnum[] constansArr = ExelBillEnum.values();
			for (ExelBillEnum constans : constansArr) {
				if(constans.getCode().equalsIgnoreCase(code)){
					return constans.msg;
				}
			}
			return EMPTY_MSG.msg;
		} catch (Exception e) {
			e.printStackTrace();
			ExelBillEnum.FAIL_MSG.msg = "信息获取失败:"+e.getMessage();
			return FAIL_MSG.msg;
		}
		
	}
	
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
