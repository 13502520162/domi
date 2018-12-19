package com.domi.support.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class ImageDownload {

	public static void download(String url,String destPath) throws ClientProtocolException, IOException{
		HttpClient client = new DefaultHttpClient();  
        HttpGet get = new HttpGet(url);  
        HttpResponse res=client.execute(get);  
        if(res.getStatusLine().getStatusCode()==HttpStatus.SC_OK){
        	HttpEntity entity=res.getEntity();
        	if(entity!=null){
        		File storeFile = new File(destPath);  
     	        FileOutputStream output = new FileOutputStream(storeFile); 
     	       output.write(EntityUtils.toByteArray(entity));  
     	       output.close();  
        	}
        }
	}
}
