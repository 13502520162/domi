package com.domi.support.upload;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;


//
public class TaskUploadUtil
{
	/**
	 * 
	 * 
	 * @param request
	 * @param businessId
	 * @param taskId
	 * @param tag
	 *           
	 * @return 
	 */
	@SuppressWarnings("deprecation")
	public static String createDir(HttpServletRequest request, int businessId,
			int taskId, String tag)
	{
		String realPath = request.getRealPath("/upload");
		String filePath = realPath + "/" + tag + "/" + businessId + "/"
				+ taskId;


		File file = new File(filePath);

		boolean b = file.mkdirs();

		return filePath;

	}
	
	/**
	 * 生成上传文件的存放路径（用户模块）
	 * @param request
	 * @param QQOpenID
	 * @param taskId
	 * @param tag
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static String createDir(HttpServletRequest request, String QQOpenID,
			int taskId, String tag)
	{
		String realPath = request.getRealPath("/upload");

		String filePath = realPath + "/" + tag + "/" + taskId + "/"
				+ QQOpenID;

		System.out.println("上传文件的存放路径" + filePath);

		File file = new File(filePath);

		if(!file.exists()){
			boolean b = file.mkdirs();
			System.out.println("新建的上传文件路径：" + b);
		}
		
		return filePath;

	}
	/**
	 * 
	 * 
	 * @param b
	 *           
	 * @param filePath
	 *            
	 * @return true
	 */
	public static boolean uploadFile(byte[] b, String filePath)
	{

		boolean result=false;

		FileOutputStream fos = null;

			try
			{
				fos = new FileOutputStream(new File(filePath));

				fos.write(b);

				result = true;
			}
			catch (Exception e)
			{
				result = false;

				e.printStackTrace();
			}
			finally
			{
				try
				{
					fos.close();
				}
				catch (IOException e)
				{
				}

			}
		
		return result;
	}
}
