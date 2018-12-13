package com.domi.support.utils;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * �����ļ����ع�����
 * @author jishaobin 
 *
 */
public class TaskDownLoadUtil {
	
	/*
	public static void main(String[] args) throws Exception {
		String path = "http://192.168.1.101:8080/Test/YoudaoDict.3566063651.exe";
		int threadsize = 3;
		String saveDir="C:/ab";
		new TaskDownLoadUtil().download(path, threadsize,saveDir);
	}*/

	public void download(String path, int threadsize,String saveDir) throws Exception {
		URL downpath = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) downpath.openConnection();
		conn.setConnectTimeout(5000);
		conn.setRequestMethod("GET");
		
		if(conn.getResponseCode() == 200){
			int length = conn.getContentLength();
			File file = new File(saveDir);
			if(!file.exists()) file.mkdirs();
			File fileSaveDir=new File(saveDir+"/"+getFileName(path));
			RandomAccessFile accessFile = new RandomAccessFile(fileSaveDir, "rwd");
			accessFile.setLength(length);
			accessFile.close();
			
			
			int block = length % threadsize == 0 ? length / threadsize : length / threadsize +1;
			for(int threadid = 0 ; threadid < threadsize ; threadid++){
				new DownloadThread(threadid, downpath, block, fileSaveDir).start();
			}
		}
}


	
	private final class DownloadThread extends Thread{
		private int threadid;
		private URL downpath;
		private int block;
		private File file;
		
		public DownloadThread(int threadid, URL downpath, int block, File file) {
			this.threadid = threadid;
			this.downpath = downpath;
			this.block = block;
			this.file = file;
		}
		public void run() {
			int startposition = threadid * block;
			int endposition = (threadid+1) * block - 1;
			
			//Range:bytes=startposition-endposition
			try{
				RandomAccessFile accessFile = new RandomAccessFile(file, "rwd");
				accessFile.seek(startposition);
				HttpURLConnection conn = (HttpURLConnection) downpath.openConnection();
				conn.setConnectTimeout(5000);
				conn.setRequestMethod("GET");
				conn.setRequestProperty("Range", "bytes="+ startposition+ "-"+ endposition);
				InputStream inStream = conn.getInputStream();
				byte[] buffer = new byte[1024];
				int len = 0;
				while( (len = inStream.read(buffer)) != -1 ){
					accessFile.write(buffer, 0, len);  
				}
				accessFile.close();
				inStream.close();
				System.out.println("��"+ (threadid+1)+ "�߳��������");
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 *
	 * @param path ����·��
	 * @return
	 */
	private static String getFileName(String path) {
		return path.substring(path.lastIndexOf("/")+ 1);
	}
}
