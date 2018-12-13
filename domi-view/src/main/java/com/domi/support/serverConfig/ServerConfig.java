package com.domi.support.serverConfig;

import com.domi.support.identification.PropsConfigReader;

public class ServerConfig {

	private String serverHost = "";
	private String servicePhone;
	private String serviceQQ;
	private String serviceWx;
	
	private int alipay_mode;
	private int check_version;
	
	private String bannerImgUrl;
	private String bannerGotoUrl;

	private String bank1ImgUrl;
	private String bank1GotoUrl;
	private String bank2ImgUrl;
	private String bank2GotoUrl;
	
	private String moreBankUrl;
	
	
	/** 平安保险参数 startPingang-isMobile 8个 其中2个测试环境 **/
	private int startPingang;// 是否开启平安保险 0:不开启 1:启动
	private String adid;
	private String channelId;
	private String key;
	private String isMobile;
	private String pabxUrl;
	private String testAdid;
	private String testPabxUrl;



	private static ServerConfig m_instance = null;

	private ServerConfig() {
		PropsConfigReader config = new PropsConfigReader("server.properties");
		serverHost = config.getString("server_host");

		servicePhone = config.getString("servicePhone");
		serviceQQ = config.getString("serviceQQ");
		serviceWx = config.getString("serviceWx");
		
		bannerImgUrl = config.getString("bannerImgUrl");
		bannerGotoUrl = config.getString("bannerGotoUrl");
		
		bank1ImgUrl = config.getString("bank1ImgUrl");
		bank1GotoUrl = config.getString("bank1GotoUrl");
		bank2ImgUrl = config.getString("bank2ImgUrl");
		bank2GotoUrl = config.getString("bank2GotoUrl");
		moreBankUrl = config.getString("moreBankUrl");
				
		alipay_mode = config.getInt("alipay_mode");
		startPingang = config.getInt("startPingang");
		adid = config.getString("adid");
		channelId = config.getString("channelId");
		key = config.getString("key");
		isMobile = config.getString("isMobile");
		pabxUrl = config.getString("pabxUrl");
		testAdid = config.getString("testAdid");
		testPabxUrl = config.getString("testPabxUrl");
		
		check_version = config.getInt("check_version");
		
	}

	// 每次调用该工厂方法返回该实例
	public synchronized static ServerConfig getInstance() {
		if (m_instance == null) {
			m_instance = new ServerConfig();
		}
		return m_instance;
	}

	public synchronized void reLoad() {
		PropsConfigReader config = new PropsConfigReader("server.properties");
		serverHost = config.getString("server_host");

		servicePhone = config.getString("servicePhone");
		serviceQQ = config.getString("serviceQQ");
		serviceWx = config.getString("serviceWx");
		alipay_mode = config.getInt("alipay_mode");
		check_version = config.getInt("check_version");
		bannerImgUrl = config.getString("bannerImgUrl");
		bannerGotoUrl = config.getString("bannerGotoUrl");
		bank1ImgUrl = config.getString("bank1ImgUrl");
		bank1GotoUrl = config.getString("bank1GotoUrl");
		bank2ImgUrl = config.getString("bank2ImgUrl");
		bank2GotoUrl = config.getString("bank2GotoUrl");
		moreBankUrl = config.getString("moreBankUrl");

		startPingang = config.getInt("startPingang");
		adid = config.getString("adid");
		channelId = config.getString("channelId");
		key = config.getString("key");
		isMobile = config.getString("isMobile");
		pabxUrl = config.getString("pabxUrl");
		testAdid = config.getString("testAdid");
		testPabxUrl = config.getString("testPabxUrl");

	}

	public String getServerHost() {
		return serverHost;
	}

	public int getCheck_version() {
		return check_version;
	}

	public int getAlipay_mode() {
		return alipay_mode;
	}


	public int getStartPingang() {
		return startPingang;
	}

	public String getAdid() {
		return adid;
	}

	public String getChannelId() {
		return channelId;
	}

	public String getKey() {
		return key;
	}

	public String getIsMobile() {
		return isMobile;
	}

	public String getPabxUrl() {
		return pabxUrl;
	}

	public String getTestAdid() {
		return testAdid;
	}

	public String getTestPabxUrl() {
		return testPabxUrl;
	}

	public String getServicePhone() {
		return servicePhone;
	}
	public String getServiceQQ() {
		return serviceQQ;
	}
	public String getServiceWx() {
		return serviceWx;
	}
	public String getBannerGotoUrl() {
		return bannerGotoUrl;
	}
	public String getBannerImgUrl() {
		return bannerImgUrl;
	}
	public String getBank1GotoUrl() {
		return bank1GotoUrl;
	}
	public String getBank1ImgUrl() {
		return bank1ImgUrl;
	}
	public String getBank2GotoUrl() {
		return bank2GotoUrl;
	}
	public String getBank2ImgUrl() {
		return bank2ImgUrl;
	}
	public String getMoreBankUrl() {
		return moreBankUrl;
	}
	
	
}
