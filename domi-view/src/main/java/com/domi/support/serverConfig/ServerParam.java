package com.domi.support.serverConfig;

import com.domi.support.identification.PropsConfigReader;

public class ServerParam {
	private String base_url;
	private String accessKey;
	private String secretKey;
	private String qiniuIdCardUrl;
	private int alipay_mode;
	private int new_version;
	private String downloadUrl;

	private static ServerParam m_instance = null;

	private ServerParam() {
		reLoad();
	}

	// 每次调用该工厂方法返回该实例
	public synchronized static ServerParam getInstance(boolean reload) {
		if (m_instance == null) {
			m_instance = new ServerParam();
		} else {
			if (reload) {
				m_instance.reLoad();
			}
		}
		return m_instance;
	}

	public synchronized void reLoad() {
		PropsConfigReader config;
		config = new PropsConfigReader("serverParam.properties");
		base_url = config.getString("base_url");
		accessKey = config.getString("accessKey");
		secretKey = config.getString("secretKey");
		qiniuIdCardUrl = config.getString("qiniuIdCardUrl");
		alipay_mode = config.getInt("alipay_mode");
		new_version = config.getInt("new_version");
		downloadUrl = config.getString("downloadUrl");
	}

	public String getBase_url() {
		return base_url;
	}

	public String getAccessKey() {
		return accessKey;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public String getQiniuIdCardUrl() {
		return qiniuIdCardUrl;
	}

	public int getAlipay_mode() {
		return alipay_mode;
	}

	public int getNew_version() {
		return new_version;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

}
