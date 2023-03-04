/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.slook.controller;

import com.slook.util.StringUtil;
import org.primefaces.context.RequestContext;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;

import static org.primefaces.component.contextmenu.ContextMenu.PropertyKeys.event;

/**
 * @author vietnv14
 */
@ViewScoped
@ManagedBean
public class CommonUtilsController
{

    private static final long serialVersionUID = 4870520554535423726L;
    String logDetail;
    String titleDetail;

    public String getLogDetail()
    {
        return logDetail;
    }

    public void setLogDetail(String logDetail)
    {
        this.logDetail = logDetail;
    }

    public String getTitleDetail()
    {
        return titleDetail;
    }

    public void setTitleDetail(String titleDetail)
    {
        this.titleDetail = titleDetail;
    }

    public void onClickDetail(String title, String log)
    {
        this.logDetail = log;
        this.titleDetail = title;
        RequestContext.getCurrentInstance().update("dlgdetailLogCommonUtilsId");
        RequestContext.getCurrentInstance().execute("PF('showDetailCommonUtilsDlg').show();");

    }

    public void postProcessXLS(Object document)
    {
        HSSFWorkbook wb = (HSSFWorkbook) document;
        HSSFSheet sheet = wb.getSheetAt(0);
        HSSFRow header = sheet.getRow(0);
        HSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setFillForegroundColor(HSSFColor.GREEN.index);
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        //BORDER_THICK rat dam, BORDER_MEDIUM net dam, BORDER_HAIR cham cham nho,BORDER_DOTTED cham cham to,BORDER_DASHED cham cham to day 

        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);

        HSSFFont hSSFFont = wb.createFont();
        hSSFFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        cellStyle.setFont(hSSFFont);

        for (int i = 0; i < header.getPhysicalNumberOfCells(); i++)
        {
            HSSFCell cell = header.getCell(i);

            cell.setCellStyle(cellStyle);
        }
        //set vien cho noi dung
//        int total row=sheet.getLastRowNum();
    }

    public void postProcessXLSHeader(Object document)
    {
        postProcessXLS(document);
        HSSFWorkbook wb = (HSSFWorkbook) document;
        HSSFSheet sheet = wb.getSheetAt(0);
        sheet.shiftRows(0, sheet.getLastRowNum(), 1);
        HSSFCell cell = sheet.createRow(0).createCell(0);
        if (StringUtil.isNotNullAndNullStr(data))
        {
            cell.setCellValue(data.toUpperCase());
        }
        else
        {
            cell.setCellValue("");
        }

        HSSFFont font = wb.createFont();
//        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setFontHeightInPoints((short) 24);

        HSSFCellStyle style = wb.createCellStyle();
        style.setFont(font);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
//set vien
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);

        cell.setCellStyle(style);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, sheet.getRow(1).getLastCellNum() - 1));
    }

    String data;

    public void attributeListener(ActionEvent event)
    {
        data = (String) event.getComponent().getAttributes().get("headerExport");
    }
}
