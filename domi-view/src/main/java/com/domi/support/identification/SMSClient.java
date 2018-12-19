package com.domi.support.identification;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Description:
 */
public class SMSClient {
    // WebService
    private String serviceURL;
    // 鐢ㄦ埛鍚�   
    private String username;
    // 瀵嗙爜
    private String password;
    // 鎺ユ敹浜�    
    private String receiver;
    // 淇℃伅鍐呭
    private String message;

    /**
     * 鍙戦�鐭俊
     * 
     * @return 鐭俊鍙戦�鐘舵�
     */
    public String sendSMS() {
        String result = this.gxmt(receiver, message);
        if(result.equals("0")){
            return "鍙戦�鐭俊閫氱煡鎴愬姛";
        }else{
            return "鍙戦�鐭俊閫氱煡澶辫触";
        }
    }

    /**
     * 鍙戦�鐭俊(鐩稿悓鍐呭锛屼笉鍚屾墜鏈哄彿锛屽鎵嬫満鍙风敤','闅斿紑)
     * 
     * @param receiver
     * @param message
     * @return 0涓哄彂閫佹垚鍔�     */
    private String mt(String receiver, String message) {
        // 璁剧疆杩斿洖鍊�       
    	String resultok = "";

        // 瀹氫箟璁块棶鍦板潃
        String soapAction = "http://tempuri.org/mt";
        String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";// xml 澶存爣绛�
        // SOAP锛氱畝鍗曞璞¤闂崗璁紝绠�崟瀵硅薄璁块棶鍗忚
        // 锛圫OAP锛夋槸涓�杞婚噺鐨勩�绠�崟鐨勩�鍩轰簬 XML 鐨勫崗璁紝
        // 瀹冭璁捐鎴愬湪 WEB 涓婁氦鎹㈢粨鏋勫寲鐨勫拰鍥哄寲鐨勪俊鎭�
        // SOAP 鍙互鍜岀幇瀛樼殑璁稿鍥犵壒缃戝崗璁拰鏍煎紡缁撳悎浣跨敤锛�        // 鍖呮嫭瓒呮枃鏈紶杈撳崗璁紙 HTTP锛夛紝绠�崟閭欢浼犺緭鍗忚锛圫MTP锛夛紝
        // 澶氱敤閫旂綉闄呴偖浠舵墿鍏呭崗璁紙MIME锛夈�
        // 瀹冭繕鏀寔浠庢秷鎭郴缁熷埌杩滅▼杩囩▼璋冪敤锛圧PC锛夌瓑澶ч噺鐨勫簲鐢ㄧ▼搴忋�

        xml += "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">";
        xml += "<soap:Body>";
        xml += "<mt xmlns=\"http://tempuri.org/\">";
        xml += "<Sn>" + username + "</Sn>"; // 鐢ㄦ埛鍚�        
        xml += "<Pwd>" + password + "</Pwd>";// 瀵嗙爜
        xml += "<mobile>" + receiver + "</mobile>";// 鎵嬫満鍙�       
        xml += "<content>" + message + "</content>";// 鍐呭
        xml += "</mt>";
        xml += "</soap:Body>";
        xml += "</soap:Envelope>";
        // 鐢虫槑URL
        URL url;
        try{
            // 鍙戦�鐨勬湇鍔″櫒鍦板潃
            url = new URL(serviceURL);
            // 鑾峰彇 connection瀵硅薄
            URLConnection connection = url.openConnection();

            // 璧癶ttp浼犺緭鍗忚 鏂规硶
            HttpURLConnection httpconnection = (HttpURLConnection)connection;
            // 鍦ㄧ綉缁滀紶杈撲腑鎴戜滑寰�線瑕佷紶杈撳緢澶氬彉閲忥紝鎴戜滑鍙互鍒╃敤ByteArrayOutputStream鎶婃墍鏈夌殑鍙橀噺鏀堕泦鍒颁竴璧凤紝鐒跺悗涓�鎬ф妸鏁版嵁鍙戦�鍑哄幓銆�            
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            // 鍐欏叆瀹氫箟濂界殑xml 鏂囦欢
            bout.write(xml.getBytes());
            byte[] b = bout.toByteArray();
            // key value 瀛樺叆鏁版嵁

            httpconnection.setRequestProperty("Content-Type",
                    "text/xml; charset=gb2312");
            httpconnection.setRequestProperty("Content-Length",
                    String.valueOf(b.length));
            httpconnection.setRequestProperty("soapAction", soapAction);
            // 璁剧疆鍙戦�鏂瑰紡
            httpconnection.setRequestMethod("POST");
            // setDoInput锛氳缃緭鍏ョ殑鍐呭setDoOutput锛氳缃緭鍑虹殑鍐呭
            httpconnection.setDoInput(true);
            httpconnection.setDoOutput(true);

            OutputStream out = httpconnection.getOutputStream();
            out.write(b);
            out.close();
            // InputStreamReader 鏄瓧鑺傛祦閫氬悜瀛楃娴佺殑妗ユ锛氬畠浣跨敤鎸囧畾鐨�charset 璇诲彇瀛楄妭骞跺皢鍏惰В鐮佷负瀛楃銆�            // 瀹冧娇鐢ㄧ殑瀛楃闆嗗彲浠ョ敱鍚嶇О鎸囧畾鎴栨樉寮忕粰瀹氾紝鍚﹀垯鍙兘鎺ュ彈骞冲彴榛樿鐨勫瓧绗﹂泦銆�           
            InputStreamReader inputsr = new InputStreamReader(
                    httpconnection.getInputStream());

            // 杩欎釜绫诲氨鏄竴涓寘瑁呯被锛屽畠鍙互鍖呰瀛楃娴侊紝灏嗗瓧绗︽祦鏀惧叆缂撳瓨閲岋紝鍏堟妸瀛楃璇诲埌缂撳瓨閲岋紝
            // 鍒扮紦瀛樻弧浜嗘垨鑰呬綘flush鐨勬椂鍊欙紝鍐嶈鍏ュ唴瀛橈紝灏辨槸涓轰簡鎻愪緵璇荤殑鏁堢巼鑰岃璁＄殑銆�            
             BufferedReader in = new BufferedReader(inputsr);
            String inputLine;
            while(null != (inputLine = in.readLine())){
                // 姝ｅ垯琛ㄨ揪寮�                
            	Pattern pattern = Pattern.compile("<mtResult>(.*)</mtResult>");
                Matcher matcher = pattern.matcher(inputLine);
                while(matcher.find()){
                    resultok = matcher.group(1);
                }
            }
            return resultok;
        }catch (Exception e){            
            return "";
        }
    }

    /**
     * 鍙戦�鐭俊(涓嶅悓鍐呭锛屼笉鍚屾墜鏈哄彿銆傚鎵嬫満鍙风敤','闅斿紑锛屽鍐呭鐢�*'闅斿紑)
     * 
     * @param receiver
     * @param message
     * @return 0涓哄彂閫佹垚鍔�     */
    public String gxmt(String receiver, String message) {
        String resultok = "";
        String soapAction = "http://tempuri.org/gxmt";
        String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";// xml 澶存爣绛�
        xml += "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">";
        xml += "<soap:Body>";
        xml += "<gxmt xmlns=\"http://tempuri.org/\">";
        xml += "<Sn>" + username + "</Sn>"; // 鐢ㄦ埛鍚�        xml += "<Pwd>" + password + "</Pwd>";// 瀵嗙爜
        xml += "<mobile>" + receiver + "</mobile>";// 鎵嬫満鍙�        xml += "<content>" + message + "</content>";// 鍐呭
        xml += "</gxmt>";
        xml += "</soap:Body>";
        xml += "</soap:Envelope>";
        URL url;
        try{
            url = new URL(serviceURL);
            URLConnection connection = url.openConnection();
            HttpURLConnection httpconnection = (HttpURLConnection)connection;
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            bout.write(xml.getBytes());
            byte[] b = bout.toByteArray();
            httpconnection.setRequestProperty("Content-Type",
                    "text/xml; charset=gb2312");
            httpconnection.setRequestProperty("Content-Length",
                    String.valueOf(b.length));
            httpconnection.setRequestProperty("soapAction", soapAction);
            httpconnection.setRequestMethod("POST");
            httpconnection.setDoInput(true);
            httpconnection.setDoOutput(true);
            OutputStream out = httpconnection.getOutputStream();
            out.write(b);
            out.close();
            InputStreamReader inputsr = new InputStreamReader(
                    httpconnection.getInputStream());
            BufferedReader in = new BufferedReader(inputsr);
            String inputLine;
            while(null != (inputLine = in.readLine())){
                Pattern pattern = Pattern
                        .compile("<gxmtResult>(.*)</gxmtResult>");
                Matcher matcher = pattern.matcher(inputLine);
                while(matcher.find()){
                    resultok = matcher.group(1);
                }
            }
            return resultok;
        }catch (Exception e){           
            return "";
        }
    }

    /**
     * 鑾峰彇浣欓
     * 
     * @return 浣欓锛坕nt锛�     */
    public String balance() {
        String result = "";
        String soapAction = "http://tempuri.org/balance";
        String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
        xml += "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">";
        xml += "<soap:Body>";
        xml += "<balance xmlns=\"http://tempuri.org/\">";
        xml += "<Sn>" + username + "</Sn>";
        xml += "<Pwd>" + password + "</Pwd>";
        xml += "</balance>";
        xml += "</soap:Body>";
        xml += "</soap:Envelope>";

        URL url;
        try{
            url = new URL(serviceURL);

            URLConnection connection = url.openConnection();
            HttpURLConnection httpconn = (HttpURLConnection)connection;
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            bout.write(xml.getBytes());
            byte[] b = bout.toByteArray();
            httpconn.setRequestProperty("Content-Length",
                    String.valueOf(b.length));
            httpconn.setRequestProperty("Content-Type",
                    "text/xml; charset=utf-8");
            httpconn.setRequestProperty("SOAPAction", soapAction);
            httpconn.setRequestMethod("POST");
            httpconn.setDoInput(true);
            httpconn.setDoOutput(true);

            OutputStream out = httpconn.getOutputStream();
            out.write(b);
            out.close();

            InputStreamReader isr = new InputStreamReader(
                    httpconn.getInputStream());
            BufferedReader in = new BufferedReader(isr);
            String inputLine;
            while(null != (inputLine = in.readLine())){
                Pattern pattern = Pattern
                        .compile("<balanceResult>(.*)</balanceResult>");
                Matcher matcher = pattern.matcher(inputLine);
                while(matcher.find()){
                    result = matcher.group(1);
                }
            }
            in.close();
            return new String(result.getBytes());
        }catch (Exception e){            
            return "";
        }
    }

    public void setServiceURL(String serviceURL) {
        this.serviceURL = serviceURL;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
}
