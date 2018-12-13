package com.domi.service;

public class StateConstant {


	public class ManagerType{
		/**超级管理员*/
		public final static int superManager = 0;
	}
	
	public class BannerType{
		/**首页**/
		public static final int homePage = 0;
		/**限时特惠**/
		public static final int salePage = 1;
	}	
	
	
	
	
	public class VisitHistoryHasRegiste{
		/**访问**/
		public static final int visit = 0;
		/**注册**/
		public static final int registe = 1;
	} 

		
	public class QiNiuUrlPrefix{
		
		/**人脸识别图片前缀*/
		public final static String faceImgUrl = "http://7xtd3p.com2.z0.glb.clouddn.com/";
		
	}
	
	
	public class UserLoanMoney{
		/**3千以下**/
		public static final int blew3 = 1;
		/**3千-1万**/
		public static final int between3to10 = 2;
		/**1万-5万**/
		public static final int between10to50 = 3;
		/**5万-10万**/
		public static final int between50to100 = 4;
		/**10万以上**/
		public static final int than100 = 5;
	} 
	
	public class AccountBookRepaymentType{
		/**一次性付款**/
		public static final int onePay = 0;
		/**分期付款**/
		public static final int payForTerm = 1;
	} 
	
	
	public class VersionType{		
		
		/**安卓*/		
		public final static int andriod = 2;
		/**IOS*/		
		public final static int ios = 3;
		
	}
	

	
	
	
}
