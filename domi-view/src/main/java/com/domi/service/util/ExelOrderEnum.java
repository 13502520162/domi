package com.domi.service.util;

/**
 * 
 * @author chenhuanshuo
 *
 * @DATE 2017年4月8日
 */
public enum ExelOrderEnum {
	/**单例对象**/
	CHANNEL("Channel","通道:0@分期呗,1@海印"),          
	ORDERNUM("OrderNum","订单编号"),
	USERNAME("UserName"," 用户姓名"),
	USERPHONE("UserPhone","用户手机"), 
	SCHOOL("School","学历院校"), 
	TITLE("Title","商品名称"), 
	ITEMPRICE("ItemPrice","商品销售价格"), 
	ACTUALPRICE("ActualPrice","商品实际价格 "), 
	FIRSTPAY("FirstPay","首付金额"), 
	ITEMRATE("ItemRate","分期月利率"), 
	TERM ("Term","分期数"),
	INVITECODE("InviteCode","邀请码"), 
	EXPRESSCOMPANY("ExpressCompany","物流公司"), 
	EXPRESSNUM("ExpressNum","物流单号"),
	DEALMAN("DealMan","经办人"),
	PAYPURCHASETYPE("PayPurchaseType","打款方式"),
	PAYPURCHASEACCOUNT("PayPurchaseAccount","打款账号"),
	PAYPURCHASENAME("PayPurchaseName","账号名称"),
    SUPPLIER("Supplier","供应商"),
	RECEIVENAME("ReceiveName","收货人姓名"),
	RECEIVEPHONE("ReceivePhone","收货人电话"),
	RECEIVEADDRESS("ReceiveAddress","收货人地址"),
	ORDERTIME("OrderTime","下单时间"),
	PASSTIME("PassTime","发货时间/打款时间"),
	REPAYED("Repayed","已还金额"),
	SHOULDPAYTERMS("ShouldPayTerms","未还期数"),
	SHOULDPAYMONEY("ShouldPayMoney","未还金额"),
	FINANCEREMARK("FinanceRemark","备注信息"),
	
	MONEY("Money","白条金额"),
	CREDITRATE("CreditRate","白条利率"),
	FEE("Fee","手续费"),
	ALIPAY("Alipay","用户支付宝"),
	BANKCARDNAME("BankCardName","持卡人姓名"),
	BANKCARDID("BankCardID","银行卡账号"),
	BANKCARDBANK("BankCardBank","银行名称"),
	
	/**空字符串**/
	EMPTY_MSG("-1","全部"),
	/**错误信息**/
	FAIL_MSG("-2","信息获取失败:"),
	;
	/**常量信息**/
	private String msg;
	/**常量值**/
	private String code;
	
	private ExelOrderEnum(){}
	private ExelOrderEnum(String code,String msg){
		this.code = code;
		this.msg = msg;
	}
	
	public static String getMsgByCode(String code){
		try {
			ExelOrderEnum[] constansArr = ExelOrderEnum.values();
			for (ExelOrderEnum constans : constansArr) {
				if(constans.getCode().equalsIgnoreCase(code)){
					return constans.msg;
				}
			}
			return EMPTY_MSG.msg;
		} catch (Exception e) {
			e.printStackTrace();
			ExelOrderEnum.FAIL_MSG.msg = "信息获取失败:"+e.getMessage();
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
