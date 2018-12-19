package com.domi.support.identification;

/**
 * sms info,include 'receiver,message'
 * 
 * @author ldr(2013-8-6)
 * 
 */
public class SMSInfo {
    /** receiver mobile */
    private String receiver;
    /** message */
    private String message;

    public SMSInfo() {

    }

    public SMSInfo(String receiver, String message) {
        this.receiver = receiver;
        this.message = message;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
