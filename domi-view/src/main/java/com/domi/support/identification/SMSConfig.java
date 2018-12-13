package com.domi.support.identification;

public class SMSConfig {
	/** WebService */
    private String serviceURL;

    private String username;

    private String password;

    private String defaultConfigfile = "sms.properties";

    public SMSConfig() {
        init(defaultConfigfile);
    }

    public SMSConfig(String smsProperties) {
        init(smsProperties);
    }

    private void init(String smsProperties) {
        PropsConfigReader config = new PropsConfigReader(smsProperties);
        this.serviceURL = config.getString("serviceURL");
        this.username = config.getString("username");
        this.password = config.getString("password");
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("serviceURL: " + this.serviceURL);
        sb.append("\nusername: " + this.username);
        sb.append("\npassword: " + this.password);
        return sb.toString();
    }

    public String getServiceURL() {
        return serviceURL;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getDefaultConfigfile() {
        return defaultConfigfile;
    }
}
