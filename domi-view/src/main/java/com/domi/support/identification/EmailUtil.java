package com.domi.support.identification;

import java.util.List;

import javax.mail.Authenticator;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

/**
 * 发送邮件工具类
 * 
 * @author tgf(Jan 26, 2011)
 * 
 */
public class EmailUtil {
	private static BzLogger logger = new BzLogger(EmailUtil.class);

    /** email相关的配置信息 */
    private static EmailConfig config = new EmailConfig();

    /**
     * 利用子线程发送邮件
     * 
     * @param info
     */
    public static void sendEmailInThread(final EmailInfo info) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                sendEmail(info);
            }
        });

        thread.start();
    }

    /**
     * 将emailInfo中的邮件信息发送
     * 
     * @param info
     */
    public static void sendEmail(EmailInfo info) {
        // 使用htmlemail发送邮件
        HtmlEmail email = new HtmlEmail();
        // 装载email配置信息
        boolean success = fillEmailConfig(email, info);
        if(success){
            // 发送
            try{
                email.send();
            }catch (EmailException e){
                // 记录错误信息
                logSendEmailErrors(info, e);
            }
        }else{
            logger.error("fail to send emails");
        }
    }

    /**
     * log send email errors
     * 
     * @param info
     * @param e
     */
    private static void logSendEmailErrors(EmailInfo info, EmailException e) {
        logger.error("fail to send email", e);
        logger.error("the email config is: {}", config.toString());
    }

    /**
     * 组装html email信息
     * 
     * @param email
     * @param info
     * @return
     */
    private static boolean fillEmailConfig(HtmlEmail email, EmailInfo info) {
        // 收件人
        List<String> tos = info.getToEmails();
        if(tos == null || tos.size() == 0){
            // 没有收件人
            logger.error("no email to send");
            return false;
        }

        try{
            for(String to : tos){
                email.addTo(to);
            }
        }catch (EmailException e){
            logger.error("add to email fail");
            return false;
        }

        try{
            // from
            email.setFrom(config.getFrom());
        }catch (EmailException e1){
            logger.error("setting {} from email fail", config.getFrom());
            return false;
        }

        // bcc
        List<String> bccs = info.getBcc();
        if(bccs != null){
            try{
                for(String bcc : bccs){
                    email.addBcc(bcc);
                }
            }catch (EmailException e){
                logger.error("add {} bcc fail");
                return false;
            }

        }

        // cc
        List<String> ccs = info.getCc();
        if(ccs != null){
            try{
                for(String cc : ccs){
                    email.addCc(cc);
                }
            }catch (EmailException e){
                logger.error("add {} cc fail");
                return false;
            }

        }

        // message
        try{
            email.setCharset("UTF-8");
            email.setHtmlMsg(info.getMessage());
        }catch (EmailException e){
            logger.error("set {} msg fail", info.getMessage());
            return false;
        }
        // subject
        email.setSubject(info.getSubject());

        // attachments
        List<String> attachments = info.getAttachFilepaths();
        if(attachments != null){
            try{
                for(String path : attachments){
                    EmailAttachment attach = new EmailAttachment();
                    attach.setPath(path);
                    email.attach(attach);
                }
            }catch (EmailException e){
                logger.error("attach {} file fail");
                return false;
            }

        }

        email.setHostName(config.getSmtpHost());
        Authenticator auth = new DefaultAuthenticator(config.getUsername(),
                config.getPassword());

        email.setAuthenticator(auth);
        // transport layer security
        email.setTLS(config.isTLS());
        email.setSSL(config.isSSL());
        email.setDebug(config.isDebug());

        return true;
    }
}
