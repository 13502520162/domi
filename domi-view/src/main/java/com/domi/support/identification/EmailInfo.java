package com.domi.support.identification;

import java.util.ArrayList;
import java.util.List;

/**
 * email info,include 'from,to,subject,content'
 * 
 * @author tgf(Mar 6, 2010)
 * 
 */
public class EmailInfo {
	/** receiver emails */
    private List<String> toEmails;
    /** email subject */
    private String subject;
    /** email content */
    private String message;
    /** 附件 */
    private List<String> attachFilepaths;
    /** blind carbon copy */
    private List<String> bcc;
    /** carbon copy */
    private List<String> cc;

    public EmailInfo() {

    }

    public EmailInfo(String subject, String message) {
        this.subject = subject;
        this.message = message;
    }

    /**
     * 增加收件人email地址
     * 
     * @param email
     */
    public void addTo(String email) {
        if(toEmails == null){
            toEmails = new ArrayList<String>();
        }
        toEmails.add(email);
    }

    /**
     * 增加附件
     * 
     * @param pathname
     */
    public void addAttach(String pathname) {
        if(attachFilepaths == null){
            attachFilepaths = new ArrayList<String>();
        }
        attachFilepaths.add(pathname);
    }

    /**
     * 增加bcc
     * 
     * @param email
     */
    public void addBcc(String email) {
        if(bcc == null){
            bcc = new ArrayList<String>();
        }
        bcc.add(email);
    }

    /**
     * 增加cc
     * 
     * @param email
     */
    public void addCc(String email) {
        if(cc == null){
            cc = new ArrayList<String>();
        }
        cc.add(email);
    }

    public List<String> getToEmails() {
        return toEmails;
    }

    public void setToEmails(List<String> toEmails) {
        this.toEmails = toEmails;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getAttachFilepaths() {
        return attachFilepaths;
    }

    public void setAttachFilepaths(List<String> attachFilepaths) {
        this.attachFilepaths = attachFilepaths;
    }

    public List<String> getBcc() {
        return bcc;
    }

    public List<String> getCc() {
        return cc;
    }
}
