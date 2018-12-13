package com.domi.support.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import cn.bestsign.sdk.BestSignSDK;
import cn.bestsign.sdk.domain.vo.params.ReceiveUser;
import cn.bestsign.sdk.domain.vo.params.SendUser;
import cn.bestsign.sdk.integration.Constants.CA_TYPE;
import cn.bestsign.sdk.integration.Constants.CONTRACT_NEEDVIDEO;
import cn.bestsign.sdk.integration.Constants.USER_TYPE;
import cn.bestsign.sdk.integration.Logger;
import cn.bestsign.sdk.integration.Logger.DEBUG_LEVEL;
import cn.bestsign.sdk.integration.utils.Utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class BestSign {
	private static final String mid = "fd2c3235a7ef45739a1ccf6dda63d823";
	private static final String pem = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAOjWW+w9sv4lNZQIc+2YwrhtjUqFT4up/PD8ybUcUVGzNv8GN0h7wtHpBayEmCkeVvYbdjnkVctvca9vwGjUzXKfIn8gMTfiXC0gbumzgLF5qVS0PV3XnR8OkPMV2zenPtG8+5Ns47r+KYqJYKx6i6ZGU7rsNSnBgLrq7OCxMOD5AgMBAAECgYEA4FT/1fqQRWtGVu1Z/JbNLXJnyl/tByxtxyZXlUbGVsgr+cRcaVrMlwBDdjd+f0d7E73bx+VmgA5M43HxWU+G7M06yCod4VzY4+ZSQ68IXjDA+guNLQ+gVKsV6OcQzzbdsfgmx907cErr3H7xKwoEKtmGNoL9AQrmZL2Sgw+/BT0CQQD4oE6dSKoS4sooFIfBBwDZEwFOQLbm78YPLs+hXKCSakBTUnrUKa+6mUXJrTOC4hz/0PRfN0St58sxsrG4MnSbAkEA774s17dCjDdutNk1eGzAdn2n/0IpEUKHMMMjLfFi4CdAekKCpQOZTfM0JKysboylWkZ2vcI8X7gPAKWI0ZX3+wJAC+iJu2jWlI7+Rcst8WXneX46isf4urjzUJjaYw0vt7OVHyiNL4pLm3Fp8U31cKhp4Srd+Y2JIZc/mULI1ly8TwJAIl8tpq/Lhrbb1Gnf1Kgi39ocH+ioYqUbJM6VWJznBCQchmrKuEXP0PExZKCtCBx6CHtZwtL6PO7yl7Ej8IiDyQJBAKS849t7GTtYFo8AR3rZk8Cb8TCTlc/Z7pDM6MxgfuFhVU1i7v5oCMj1Zy54OrRkDYPuv8Y2SxcEw4zG/XdQIEg=";
	private static String host = "https://www.bestsign.cn";
	//private static String host = "http://localhost:8080";
	
	private static BestSignSDK sdk = null;
	
	private static JSONObject lastContinfoList = null;
	
	private static boolean isOutputInfo = false;
	
	static {
		sdk = BestSignSDK.getInstance(mid, pem, host);
		sdk.setDebugLevel(DEBUG_LEVEL.INFO);
	}
    
    public static void setHost(String value) {
		host = value;
	}
	
	public static void setDebugLevel(DEBUG_LEVEL level) {
		sdk.setDebugLevel(level);
	}
	
	public static void setLogDir(String path) {
		sdk.setLogDir(path);
	}
	
	//CFCA证书申请
	public static HashMap<String, Object> certificateApply(String userName, String linkMobile, String identNo, String province, String city) throws Exception {
		HashMap<String, Object> result = new HashMap<String, Object>();
		CA_TYPE caType = CA_TYPE.CFCA;
		String password = "123456";
		String email = "";
		JSONObject resultJson = sdk.certificateApply(caType, userName, password, linkMobile, email, province, province, city, identNo);
		result.put("isResult", resultJson.get("isResult"));
		return result;
	}
	
	//根据模版创建合同
	public static HashMap<String, Object> uploadcontractly(int orderType, int orderId, String cellPhone, String userNamne, String pdfPath) throws Exception {
		HashMap<String, Object> result= new HashMap<String, Object>();
		String emailTitleString= cellPhone + "-" + orderType + "-" + orderId;
		byte[] fileData = getResource(pdfPath);
		ReceiveUser[] userlist = {new ReceiveUser("", userNamne, cellPhone, USER_TYPE.PERSONAL, CONTRACT_NEEDVIDEO.NONE, false)};
		SendUser senduser = new SendUser("925311689@qq.com", "分期呗", "13660184841", 99, true, USER_TYPE.ENTERPRISE, false, emailTitleString, "");
		JSONObject resultJson = sdk.sjdsendcontractdocUpload(userlist, senduser, fileData);
		JSONObject responseObject = resultJson.getJSONObject("response");
		if(responseObject.getJSONObject("info").getString("code").equals("100000")){
			JSONObject contentObject = responseObject.getJSONObject("content");
			JSONArray contlistArray = contentObject.getJSONArray("contlist");
			JSONObject contInfoObject = contlistArray.getJSONObject(0);
			JSONObject realContInfoObject = contInfoObject.getJSONObject("continfo");
			String signid = realContInfoObject.getString("signid");
			String docid = realContInfoObject.getString("docid");
			
			result.put("isResult", "true");
			result.put("signid", signid);
			result.put("docid", docid);
			
			return result;
		}
		else{
			result.put("isResult", "false");
			return result;
		}
	}
	
	
	/**
	 * //自动签合同
	 * (每页最左上角坐标为(0,0))
	 * @param userEmail
	 * @param userCellphone
	 * @param signid 合同编号
	 * @param signx  盖章的横坐标
	 * @param signy  盖章的纵坐标
	 * @param pagenum 盖章pdf所在页码
	 * @return
	 * @throws Exception
	 * @author chenhuanshuo
	 * @Date 2017年6月8日
	 */
	public static HashMap<String, Object> AutoSignbyCA(String userEmail, String userCellphone, String signid, float signx, float signy, int pagenum) throws Exception {

		HashMap<String, Object> result= new HashMap<String, Object>();
		//自动签这份合同
		String email = (userEmail.length()==0 ? userCellphone : userEmail);
		boolean openflag = true;		
		JSONObject resultJson = sdk.AutoSignbyCA(signid, email, pagenum, signx, signy, openflag, "");
		JSONObject responseObject = resultJson.getJSONObject("response");
		if(responseObject.getJSONObject("info").getString("code").equals("100000")){
			JSONObject contentObject = responseObject.getJSONObject("content");
			String code = contentObject.getString("code");
			
			if(code.equals("100000")){
				String fsdId = contentObject.getString("fsdId");
				String fmid = contentObject.getString("fmid");
				String returnurl = contentObject.getString("returnurl");
				
				result.put("isResult", "true");
				result.put("fsdId", fsdId);
				result.put("fmid", fmid);
				result.put("returnurl", returnurl);
			}
			else{
				result.put("isResult", "false");
			}
		}
		else{
			result.put("isResult", "false");
		}
		return result;
	}
	
//	// 查询合同列表
//	public static void contractList() throws Exception {
//		outputInfo();
//		
//		addToLog("");
//		addToLog("测试查询合同列表...");
//		
//		String email = "1234567@qq.com";
//		String status = "3";
//		String starttime = "";
//		String endtime = "";
//		JSONObject result = sdk.contractList(status, email, starttime, endtime);
//		addToLog("测试查询合同列表:");
//		dump(result);
//	}

//	// 合同详细信息
//	public static void contractInfo() throws Exception {
//		outputInfo();
//
//		addToLog("");
//		addToLog("测试合同详细信息...");
//		
//		if (lastContinfoList == null) {
//			addToLog("测试合同详细信息, 先创建一份合同...");
//			createContract();
//		}
//		String signid = getLastContractId();
//		
//		JSONObject result = sdk.contractInfo(signid);
//		addToLog("测试合同详细信息:");
//		dump(result);
//	}
//	
	//合同查看
	public static String viewContract(String signid, String fileid) throws Exception {
		String result = sdk.ViewContract(signid, fileid);
		return result;
	}
//	
//	//合同zip下载
//	public static void contractDownload() throws Exception {
//		downloadContract("ZIP");
//	}
//	
//	//合同pdf下载
//	public static void contractDownloadMobile() throws Exception {
//		downloadContract("PDF");
//	}
//	
//	public static void templateCreate() throws Exception {
//		outputInfo();
//
//		addToLog("");
//		addToLog("测试合同模版创建...");
//		
//		String uid = "a";
//		String result = sdk.templateCreate(uid);
//		addToLog("测试合同查看信息:");
//		dump(result);
//	}
	
	//随机ReceiveUser
	private static ReceiveUser randomReceiveUser() {
		String mobile = "1300" + Utils.rand(1000000, 9999999);
		String email = mobile + "@qq.com";
		String name = "Test" + mobile;
		ReceiveUser user = new ReceiveUser(email, name, mobile, USER_TYPE.PERSONAL, CONTRACT_NEEDVIDEO.NONE, false);
		return user;
	}
	
	//随机身份证
	private static String randomIDCard() {
		String a = Integer.toString(Utils.rand(110000, 650999));
		
		String yy = Integer.toString(Utils.rand(1950, 2000));
		String mm = Integer.toString(Utils.rand(3, 12));
		String dd = Integer.toString(Utils.rand(1, 30));
		if (mm.length() != 2) {
			mm = "0" + mm;
		}
		if (dd.length() != 2) {
			dd = "0" + dd;
		}
		
		String b = Integer.toString(Utils.rand(0, 999));
		if (b.length() == 1) {
			b = "00" + b;
		}
		else if (b.length() == 2) {
			b = "0" + b;
		}
		
		String s = a + yy + mm + dd + b;
		//System.out.println(s);
		
		int[] nums = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
		int m = 0;
		for (int i = 0; i < s.length(); i++) {
			String c = s.substring(i, i + 1);
			int n = Integer.parseInt(c);
			int n2 = nums[i];
			m += n * n2;
		}
		m = m % 11;
		
		String[] codes = {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};
		String code = codes[m];
		s += code;
		return s;
	}
	
	//合同上传
	private static JSONObject sjdsendcontractdocUpload() throws Exception {
		byte[] fileData = getResource("/demo.pdf");
		ReceiveUser[] userlist = {new ReceiveUser("1234567@qq.com", "Test1", "13812345678", USER_TYPE.PERSONAL, CONTRACT_NEEDVIDEO.NONE, false)};
		SendUser senduser = new SendUser("22345678@163.com", "Test2", "13912345678", 3, false, USER_TYPE.PERSONAL, false, "title", "");
		lastContinfoList = sdk.sjdsendcontractdocUpload(userlist, senduser, fileData);
		return lastContinfoList;
	}
	
//	//合同下载
//	private static void downloadContract(String type) throws Exception {
//		outputInfo();
//
//		addToLog("");
//		addToLog("测试合同" + type + "下载...");
//		
//		if (lastContinfoList == null) {
//			addToLog("测试合同" + type + "下载, 先创建一份合同...");
//			createContract();
//		}
//		String signid = getLastContractId();
//		
//		byte[] result;
//		if (type.equalsIgnoreCase("PDF")) {
//			result = sdk.contractDownloadMobile(signid);
//		}
//		else {
//			result = sdk.contractDownload(signid);
//		}
//		
//		addToLog("测试合同" + type + "下载信息:");
//		
//		//太大了，显示头若干个字节
//		int max = 256;
//		if (result.length > max) {
//			byte[] tmp = new byte[max];
//			System.arraycopy(result, 0, tmp, 0, tmp.length);
//			result = tmp;
//		}
//		for (int i = 0; i < result.length; i++) {
//			byte b = result[i];
//			int n = ((int) b) & 0xff;
//			String hex = Integer.toHexString(n);
//			if (hex.length() < 2) {
//				hex = "0" + hex;
//			}
//			System.out.print(hex);
//			if ((i + 1) % 16 == 0) {
//				System.out.println("");
//			}
//			else {
//				System.out.print(" ");
//			}
//		}
//	}
//	
	//提取lastContinfoList的signid
	private static String getLastContractId() {
		if (lastContinfoList == null) {
			return "";
		}
		JSONObject response = lastContinfoList.getJSONObject("response");
		JSONObject content = response.getJSONObject("content");
		JSONArray continfoList = content.getJSONArray("contlist");
		JSONObject contractInfo = continfoList.getJSONObject(0);
		contractInfo = contractInfo.getJSONObject("continfo");
		String signid = contractInfo.getString("signid");
		return signid;
	}
	
	//提取lastContinfoList的docid
	private static String getLastFileId() {
		if (lastContinfoList == null) {
			return "";
		}
		JSONObject response = lastContinfoList.getJSONObject("response");
		JSONObject content = response.getJSONObject("content");
		JSONArray continfoList = content.getJSONArray("contlist");
		JSONObject contractInfo = continfoList.getJSONObject(0);
		contractInfo = contractInfo.getJSONObject("continfo");
		String docid = contractInfo.getString("docid");
		return docid;
	}
	
	private static byte[] getResource(String path) throws IOException {
		File file = new File(path);
		InputStream s = new FileInputStream(file);
		
		ArrayList<byte[]> bufferList = new ArrayList<byte[]> ();
		byte[] buffer = new byte[4096];
		int read = 0;
		int total = 0;
		while ((read = s.read(buffer)) > 0) {
			byte[] b = new byte[read];
			System.arraycopy(buffer, 0, b, 0, read);
			bufferList.add(b);
			total += read;
		}
		s.close();
		
		byte[] result = new byte[total];
		int pos = 0;
		for (int i = 0; i < bufferList.size(); i++) {
			byte[] b = bufferList.get(i);
			System.arraycopy(b, 0, result, pos, b.length);
			pos += b.length;
		}
		
		return result;
	}
	
	private static void dump(Object value) {
		String output = JSONObject.toJSONString(value);
		Logger.addToLog(output);
		System.out.println(output);
	}
	
	private static void addToLog(Object message) {
		Logger.addToLog(message);
		System.out.println(message);
	}
	
	private static void outputInfo() {
		if (isOutputInfo) {
			return;
		}
		isOutputInfo = true;
		String output = "Test mid: " + mid + "; host: " + host;
		addToLog(output);
	}
}
