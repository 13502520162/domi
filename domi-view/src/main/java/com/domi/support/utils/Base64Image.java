package com.domi.support.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Base64Image {
    public static void main(String[] args) {
        // 测试从Base64编码转换为图片文件
    	
        String strImg = "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";
//        String imagePath = "D:\\develop\\project\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\wtpwebapps\\Amer\\28.jpg";
        String imagePath ="d:\\wangyc.jpg";
System.out.println(imagePath);
        GenerateImage(strImg, imagePath);
        
        // 测试从图片文件转换为Base64编码
        System.out.println(GetImageStr("d:\\wangyc.jpg"));
    }

    public static String GetImageStr(String imgFilePath) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        byte[] data = null;
        
        // 读取图片字节数组
        try {
            InputStream in = new FileInputStream(imgFilePath);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);// 返回Base64编码过的字节数组字符串
    }

    public static boolean GenerateImage(String imgStr, String imgFilePath) {// 对字节数组字符串进行Base64解码并生成图片
        if (imgStr == null) // 图像数据为空
            return false;
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            // Base64解码
            byte[] bytes = decoder.decodeBuffer(imgStr);
            for (int i = 0; i < bytes.length; ++i) {
                if (bytes[i] < 0) {// 调整异常数据
                    bytes[i] += 256;
                }
            }
            // 生成jpeg图片
            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(bytes);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
