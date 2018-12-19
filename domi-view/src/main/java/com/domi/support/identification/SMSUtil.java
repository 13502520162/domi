package com.domi.support.identification;

//import cn.bidaround.log.BzLogger;
import com.domi.support.identification.StringUtil;

/**
 * 鍙戦�鐭俊宸ュ叿绫� * 
 * @author ldr(2013-8-6)
 * 
 */
public class SMSUtil {
    private static BzLogger logger = new BzLogger(SMSUtil.class);

    /** sms鐩稿叧閰嶇疆淇℃伅 */
    private static SMSConfig config = new SMSConfig();

    /**
     * 鍒╃敤瀛愮嚎绋嬪彂閫佷俊鎭�     * 
     * @param info
     */
    public static void sendSMSInThread(final SMSInfo info) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                sendSMS(info);
            }
        });

        thread.start();
    }

    public static void sendSMS(SMSInfo info) {
        // 浣跨敤smsclient鍙戦�淇℃伅
        SMSClient sms = new SMSClient();
        // 瑁呰浇閰嶇疆淇℃伅
        boolean success = fillSMSConfig(sms, info);
        if(success){
            // 鍙戦�
            try{
                logger.info(sms.sendSMS());
            }catch (Exception e){
                // 璁板綍閿欒淇℃伅
                logSendSMSErrors(info, e);
            }
        }else{
            logger.error("fail to send sms");
        }
    }

    /**
     * 璁板綍閿欒淇℃伅
     * 
     * @param info
     * @param e
     */
    private static void logSendSMSErrors(SMSInfo info, Exception e) {
        logger.error("fail to send sms", e);
        logger.error("the sms config is: {}", config.toString());
    }

    /**
     * 缁勮sms淇℃伅
     * 
     * @param sms
     * @param info
     * @return
     */
    private static boolean fillSMSConfig(SMSClient sms, SMSInfo info) {
        if(StringUtil.isEmpty(info.getReceiver())){
            logger.error("no sms receiver");
            return false;
        }else{
            sms.setReceiver(info.getReceiver());
        }
        if(StringUtil.isEmpty(info.getMessage())){
            logger.error("no message to send");
            return false;
        }else{
            sms.setMessage(info.getMessage());
        }
        if(StringUtil.isEmpty(config.getServiceURL())){
            logger.error("setting service url fail");
            return false;
        }else{
            sms.setServiceURL(config.getServiceURL());
        }
        if(StringUtil.isEmpty(config.getUsername())){
            logger.error("setting username fail");
            return false;
        }else{
            sms.setUsername(config.getUsername());
        }
        if(StringUtil.isEmpty(config.getPassword())){
            logger.error("setting password fail");
            return false;
        }else{
            sms.setPassword(config.getPassword());
        }

        return true;
    }
}
