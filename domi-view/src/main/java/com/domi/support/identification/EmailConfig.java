package com.domi.support.identification;

/**
* email配置信息 默认从classpath中的email.properties读取
* 
* @author tgf(Jan 26, 2011)
* 
*/

public class EmailConfig {
	/** smtp server,like smtp.163.com */
    private String smtpHost;
    /** email username,exclude @163.com */
    private String username;
    /** email password */
    private String password;
    /** who send the email */
    private String from;
    /** 是否使用ssl层 */
    private boolean isSSL;
    private boolean isTLS;
    private boolean isDebug;

    private String replyTosString;

    private String defaultConfigfile = "email.properties";

    public EmailConfig() {
        init(defaultConfigfile);
    }

    public EmailConfig(String smtpProperties) {
        init(smtpProperties);
    }

    private void init(String smtpProperties) {
        com.domi.support.identification.PropsConfigReader config = new com.domi.support.identification.PropsConfigReader(smtpProperties);
        this.smtpHost = config.getString("host");
        this.username = config.getString("username");
        this.password = config.getString("password");
        this.from = config.getString("from");
        this.isSSL = config.getBoolean("ssl");
        this.isTLS = config.getBoolean("tls");
        this.isDebug = config.getBoolean("debug");
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("smtpHost: " + this.smtpHost);
        sb.append("\nusername: " + this.username);
        sb.append("\npassword: " + this.password);
        sb.append("\nfrom: " + this.from);
        return sb.toString();
    }

    public String getSmtpHost() {
        return smtpHost;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFrom() {
        return from;
    }

    public boolean isSSL() {
        return isSSL;
    }

    public boolean isTLS() {
        return isTLS;
    }

    public boolean isDebug() {
        return isDebug;
    }

    public String getReplyTosString() {
        return replyTosString;
    }

    public String getDefaultConfigfile() {
        return defaultConfigfile;
    }
}
