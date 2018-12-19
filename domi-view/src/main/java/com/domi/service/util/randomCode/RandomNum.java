package com.domi.service.util.randomCode;
import java.util.Random;
import com.domi.support.utils.MD5;
public class RandomNum {
	 private static int codeCount = 4;  
     private static char[] codeSequence = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',  
             'K', 'L', 'M', 'N','O',  'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',  
             'X', 'Y', 'Z','a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',  
             'k', 'l', 'm', 'n','o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',  
             'x', 'y', 'z','0','1','2','3','4','5','6','7','8','9'};
   public static String getRandomNum(){
    	 StringBuffer randomCode = new StringBuffer();
    	 Random random = new Random();  
    	 for (int i = 0; i < codeCount; i++) {  
             String strRand = String.valueOf(codeSequence[random.nextInt(codeSequence.length)]);  
             randomCode.append(strRand);  
         }  
    	 return randomCode.toString();
     }
   public static void main(String[] args) throws Exception {
	   String signKey="cxxYH159"+getRandomNum();
	   String md5Encrypt = MD5.MD5Encrypt(signKey);
	   System.out.println(getRandomNum());
	   System.out.println(md5Encrypt);
  }
   
}
