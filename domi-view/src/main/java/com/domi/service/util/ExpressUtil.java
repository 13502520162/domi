package com.domi.service.util;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
public class ExpressUtil {
	
	/**
     * 获取快递信息
     * @param expressCompany
     * @param expressNum
     * @return
     * @author chenhuanshuo
     * @throws Exception 
     * @Date 2017年7月27日
     */
	public static String getExpressInfo(String expressCompany,String expressNum) throws Exception {
		    String host = "https://ali-deliver.showapi.com";
		    String path = "/showapi_expInfo";
		    String method = "GET";
		    String appcode = "49e3dffe06f84305a893d5a5c6ba45c2";
		    Map<String, String> headers = new HashMap<String, String>();
		    //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
		    headers.put("Authorization", "APPCODE " + appcode);
		    Map<String, String> querys = new HashMap<String, String>();
		    querys.put("com", expressCompany);
		    querys.put("nu",expressNum);
	    	HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
	   return  EntityUtils.toString(response.getEntity()); //获取response的body
	}
	
	/**
	 * 根据订单号获取公司名称
	 * @param expressNum
	 * @return
	 * @throws Exception
	 * @author chenhuanshuo
	 * @Date 2017年7月28日
	 */
	public static String getCompanyByNum(String expressNum) throws Exception {
		String host = "https://ali-deliver.showapi.com";
	    String path = "/fetchCom";
	    String method = "GET";
	    String appcode = "49e3dffe06f84305a893d5a5c6ba45c2";
	    Map<String, String> headers = new HashMap<String, String>();
	    //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
	    headers.put("Authorization", "APPCODE " + appcode);
	    Map<String, String> querys = new HashMap<String, String>();
	    querys.put("nu", expressNum);
	    HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
	  return EntityUtils.toString(response.getEntity());//获取response的body
	}
}
