package com.domi.service.util;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 导出exel通用方法
 * @author chenhuanshuo
 *
 * @Date 2017年5月22日
 */
public class ExcelUtil {
	/**
	 * 解析properties文件
	 * @param propertiesName
	 * @return
	 * @throws Exception
	 * @author chenhuanshuo
	 * @Date 2017年5月26日
	 */
	public static Map<String,Object> parseProperties(String propertiesName) throws Exception{
		Map<String,Object> map=new HashMap<String,Object>();
		 /**读取配置文件 */
		String key="";
		List<String> keyList = new ArrayList<String>();
		List<String> valueList = new ArrayList<String>();
		 Properties prop = new OrderedProperties(); 
		 InputStream in = new BufferedInputStream (new FileInputStream(propertiesName));
		 prop.load(in);     ///加载属性列表
		 Iterator<String> it=prop.stringPropertyNames().iterator();
         while(it.hasNext()){
        	   key=it.next();
               keyList.add(key);
               valueList.add(new String(prop.getProperty(key).getBytes("ISO-8859-1"),"utf-8"));
            }
		  in.close();
		  String exelName=valueList.get(0);//exel表名称
		  String sheetTitle=valueList.get(1);//工作表主标题
		  String[] titleFiles=new String[keyList.size()-2];//excel中文列标题
		  String[] titles = new String[valueList.size()-2];// 各个列标题对应的实体变量，如Name,Gender...(第一个字母大写: get方法获取值)
		  HashMap<Object, String> rulesMap = new HashMap<Object, String>();//转换字段
		 for (int i = 2; i < keyList.size(); i++) {
			 titleFiles[i-2]=keyList.get(i);
			 titles[i-2]=valueList.get(i);
			 if(titles[i-2].indexOf(":")!=-1){
				 rulesMap.put(i-2, titles[i-2].split(":")[1]);
				 titles[i-2] = titles[i-2].split(":")[0];
			 }
		}
		 map.put("exelName", exelName);
		 map.put("sheetTitle", sheetTitle);
		 map.put("titleFiles", titleFiles);
		 map.put("titles", titles);
		 map.put("rulesMap", rulesMap);
		return map;
	}
	
	/**
	 * 导出Excel(动态字段)
	 * @param objectList 要导出的实体集合
	 * @param rulesMap  转换字段
	 * @param outputStream 
	 * @param sheetTitle  工作表主标题
	 * @param  titleFiles 列对应字段(get 首字母大写)
	 * @param  titles  exel表列名称
	 * @throws Exception
	 * @author chenhuanshuo
	 * @Date 2017年5月25日
	 */
	public static void exportExcel(List<Object> objectList,Map<Object, String> rulesMap, OutputStream outputStream,String sheetTitle,String[] titleFiles,String[]titles) throws Exception {
		/**创建excel*/
		// 2.1 创建工作簿
		XSSFWorkbook workbook = new XSSFWorkbook();
		// 2.1.1 工作簿样式
		// 2.1.1.1 第一行头标题样式
		XSSFCellStyle style1 = createStyle(workbook, (short) 16);
		
		// 2.1.1.2第二行列标题样式
		XSSFCellStyle style2 = createStyle(workbook, (short) 13);
		
		// 2.2 创建工作表
		XSSFSheet sheet = workbook.createSheet(sheetTitle);
		sheet.setDefaultColumnWidth(20);// 设置默认列宽
		
		// 2.2.1 创建表头
		// 2.2.1.1 创建第一行表头，标题栏
		XSSFRow row1 = sheet.createRow(0);
		// 2.2.1.2 合并单元格，（起始行号，终止行号，起始列号，终止列号），行列号都是从0开始
		CellRangeAddress rangeAddress = new CellRangeAddress(0, 0, 0, (titleFiles.length-1));
		sheet.addMergedRegion(rangeAddress);
		XSSFCell cell1 = row1.createCell(0);// 创建单元格
		cell1.setCellStyle(style1);// 设置标题栏样式
		cell1.setCellValue(sheetTitle);// 设置主标题
		// 2.2.2.1 创建第二行标题，列标题
		XSSFRow row2 = sheet.createRow(1);
		
		for (int i = 0; i < titles.length; i++) {
			XSSFCell cell2 = row2.createCell(i);
			cell2.setCellStyle(style2);
			cell2.setCellValue(titles[i]);
		}
		// 2.3 创建数据行和单元格，写入数据
		int index = 2;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		XSSFRow row = null;
		Method method = null;
		XSSFCell cell11 = null;
		String fileValue = "";
		Object fileObject = null;
		for (Object obj : objectList) {
			row = sheet.createRow(index++);
			for (int i = 0; i < titleFiles.length; i++) {
				//注意这里是使用了getter方法返回值，如果是boolean类型的属性，它的没有get方法而是is方法，所以可以在实体类中对boolean类型的属性写个get方法即可。
				method = obj.getClass().getMethod("get" + titleFiles[i]);
				cell11 = row.createCell(i);
				fileObject = method.invoke(obj);// 通过反射getter方法获取值
				if (fileObject != null) {//判断属性值是否为null，是的话显示空值
					if (fileObject instanceof Date) {// 如果是日期类型的则格式化为yyyy-MM-dd
						fileValue = dateFormat.format((Date) fileObject);
					}else {// 其它类型的按照字符串处理
						fileValue = fileObject + "";
					}
					//对真值按显示规则进行处理
					if(rulesMap.size()>=1){
						if(rulesMap.containsKey(i)){//判断当前属性是否需要进行显示值按照规则处理
							String [] rules = rulesMap.get(i).split(",");//按照 , 将字符串分成数组
							for(String str: rules){
								if(str.contains(fileValue)){
									fileValue = str.split("@")[1];//按照 @ 将字符串分组，并将显示值赋值给excel中要显示的内容
									cell11.setCellValue(fileValue);
									break;
								}
							}
						}else{//数值类型
							 if(fileObject instanceof Integer||fileObject instanceof Float){
								    cell11.setCellValue(Double.parseDouble(fileValue.toString()));
								}else{
									cell11.setCellValue(fileValue);
								}
						}
					}else{//数值类型
						 if(fileObject instanceof Integer||fileObject instanceof Float){
							    cell11.setCellValue(Double.parseDouble(fileValue.toString()));
							}else{
								cell11.setCellValue(fileValue);
							}
					}	
				} else {fileValue = "";}
			}
		}
		workbook.write(outputStream);
		outputStream.close();
	}
	/**
	 * 创建特定字号，且水平、垂直居中、字体加粗的样式。
	 * @param workbook
	 * @param fontSize
	 * @return
	 * @author chenhuanshuo
	 * @Date 2017年5月22日
	 */
	public static XSSFCellStyle createStyle(XSSFWorkbook workbook, short fontSize) {
		XSSFCellStyle style1 = workbook.createCellStyle();
		style1.setAlignment(XSSFCellStyle.ALIGN_CENTER);// 水平居中
		style1.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);// 垂直居中
		XSSFFont font1 = workbook.createFont();
		font1.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);// 字体加粗
		font1.setFontHeightInPoints(fontSize);// 字体大小
		style1.setFont(font1);// 设置字体
		return style1;
	}
}
