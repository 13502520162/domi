package com.domi.support.utils;

import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

import com.domi.model.loanmarket.PlatformInfo;

public class ExcelUtils {
	public static HSSFCellStyle getTableHeaderStyle(HSSFWorkbook workbook) {
		// 生成一个样式
		HSSFCellStyle style = workbook.createCellStyle();
		// 设置这些样式
		style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

		HSSFFont font = workbook.createFont();
		font.setColor(HSSFColor.BLACK.index);
		font.setFontHeightInPoints((short) 12);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		// 把字体应用到当前的样式
		style.setFont(font);
		return style;
	}

	public static HSSFCellStyle getTableBodyStyle(HSSFWorkbook workbook) {
		// 生成并设置另一个样式
		HSSFCellStyle style2 = workbook.createCellStyle();
		style2.setFillForegroundColor(HSSFColor.WHITE.index);
		style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		// 生成另一个字体
		HSSFFont font2 = workbook.createFont();
		font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		// 把字体应用到当前的样式
		style2.setFont(font2);
		return style2;
	}

	public static HSSFCellStyle getTagStyle(HSSFWorkbook workbook) {
		HSSFCellStyle style3 = workbook.createCellStyle();
		// 设置这些样式
		style3.setFillForegroundColor(HSSFColor.WHITE.index);
		style3.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style3.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		// 生成一个字体
		HSSFFont font3 = workbook.createFont();
		font3.setColor(HSSFColor.BLACK.index);
		font3.setFontHeightInPoints((short) 10);
		font3.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		// 把字体应用到当前的样式
		style3.setFont(font3);
		return style3;
	}


	
	
	
	public static void exportLoanPlatformInfo(HSSFWorkbook workbook, List<PlatformInfo> list, String title) {
		HSSFSheet sheet = workbook.createSheet(title);
		sheet.setDefaultColumnWidth(25);

		HSSFCellStyle table_header_style = getTableHeaderStyle(workbook);
		HSSFCellStyle table_body_style = getTableBodyStyle(workbook);
		String[] headers = { "上线情况", "发票类型", "发票内容", "平台名称", "公司名称", "税号/信用代码", "注册地址", "公司电话", "开户行", "开户账号", "寄送地址",
				"对接/收件人", "对接收件人联系方式"};
		int cell_length = headers.length;
		HSSFRow row = sheet.createRow(0);
		row.setHeightInPoints(20);
		for (int i = 0; i < cell_length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellStyle(table_header_style);
			HSSFRichTextString text = new HSSFRichTextString(headers[i]);
			cell.setCellValue(text);
		}
		
		int index = 1;
		for (PlatformInfo item : list) {
			row = sheet.createRow(index);
			row.setHeightInPoints(20);
			for (int i = 0; i < cell_length; i++) {
				HSSFCell cell = row.createCell(i);
				cell.setCellStyle(table_body_style);
				String content = "";
				switch (i) {
				case 0:
					int state = item.getState();
					content = state == 0 ? "待上线" : (state == 1 ? "已上线" : "已下线");
					break;
				case 1:
					content = item.getReceiptType();
					break;
				case 2:
					content = item.getReceiptContent();
					break;
				case 3:
					content = item.getName();
					break;
				case 4:
					content = item.getCompanyName();
					break;
				case 5:
					content = item.getTaxNumber();
					break;
				case 6:
					content = item.getCompanyAddr();
					break;
				case 7:
					content = item.getCompanyPhone();
					break;
				case 8:
					content = item.getOpenbank();
					break;
				case 9:
					content = item.getBankAccount();
					break;
				case 10:
					content = item.getSendAddr();
					break;
				case 11:
					content = item.getReceiveName();
					break;
				case 12:
					content = item.getRecervePhone();
					break;
				default:
					break;
				}
				cell.setCellValue(content);
			}
			index++;
		}
	}


	private static String getStringCellValue(HSSFCell cell) {
		String strCell = "";
		
		switch (cell.getCellType()) {
		case HSSFCell.CELL_TYPE_STRING:
			strCell = cell.getStringCellValue();
			break;
		case HSSFCell.CELL_TYPE_NUMERIC:
			strCell = String.valueOf(cell.getNumericCellValue());
			break;
		case HSSFCell.CELL_TYPE_BOOLEAN:
			strCell = String.valueOf(cell.getBooleanCellValue());
			break;
		case HSSFCell.CELL_TYPE_BLANK:
			strCell = "";
			break;
		default:
			strCell = "";
			break;
		}
		if (strCell.equals("") || strCell == null) {
			return "";
		}
		return strCell;
	}


}
