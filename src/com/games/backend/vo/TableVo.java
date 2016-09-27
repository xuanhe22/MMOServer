package com.games.backend.vo;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;

import com.storm.lib.base.BaseVo;
import com.storm.lib.util.ExcelUtil;

public class TableVo extends BaseVo{
	public List<String> titles= new ArrayList<String>();
	public List<List<Object>> rows= new ArrayList<List<Object>>();
	public TableVo(String[] tit){
		for (String ti : tit) {
			titles.add(ti);
		}
	}
	
	public void addRow(Object[] objs){
		List<Object> row =new ArrayList<Object>();
		for (Object obj : objs) {
			row.add(obj);
		}
		rows.add(row);
	}

	public String toHtmlTable(int border, int widthPer) {
		StringBuffer html=new StringBuffer();
		html.append("<table border='").append(border).append("' style='width:").append(widthPer).append("%'><tbody><tr>");
		for (String title : titles) {
			html.append("<th>");
			html.append(title);
			html.append("</th>");
		}
		html.append("</tr></tbody>");

		for (List<Object> row : rows) {
			html.append("<tr>");
			for (Object object : row) {
				html.append("<td>");
				html.append(object);
				html.append("</td>");
			}
			html.append("</tr>");
		}
		html.append("</table>");
		return html.toString();
	}

	public void addToExcelSheet(HSSFSheet sheet) {
	    HSSFRow row=null;  
	    HSSFCell cell=null; 
	    int currentRow=0;
	    row=sheet.createRow(currentRow++);//新增一行 		
	    ExcelUtil.createRowCellData(row,titles.toArray(new String[titles.size()]));
		for (List<Object> obj : rows) {
		    row=sheet.createRow(currentRow++);//新增一行
		    ExcelUtil.createRowCellData(row,obj.toArray(new Object[obj.size()]));
		}
	}
	
}
