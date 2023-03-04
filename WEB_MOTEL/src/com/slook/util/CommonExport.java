/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.slook.util;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import java.io.*;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.AbstractMap.SimpleEntry;

/**
 * @author tamdx
 */
public class CommonExport
{

    private CommonExport()
    {
        //Ham khoi tao
    }

    public static String getTemplateExport()
    {
        ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance()
                .getExternalContext().getContext();
        return ctx.getRealPath("/")
                + File.separator + "templates" + File.separator + "TEMPLATE_EXPORT.xlsx";
    }

    public static String getTemplateExportMultiSheet()
    {
        ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance()
                .getExternalContext().getContext();
        return ctx.getRealPath("/") + File.separator + "templates" + File.separator + "TEMPLATE_EXPORT_MULTI_SHEET.xlsx";
    }

    public static String getTemplateMultiExport(String fileName)
    {
        ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance()
                .getExternalContext().getContext();
        return ctx.getRealPath("/") + File.separator + "templates" + File.separator + fileName;

    }

    public static List<SimpleEntry<String, String>> buildExportHeader(String[] header, String[] align)
    {
        List<SimpleEntry<String, String>> headerAlign = new ArrayList<SimpleEntry<String, String>>();
        for (int i = 0; i < header.length; i++)
        {
            headerAlign.add(new SimpleEntry(header[i], align[i]));
        }
        return headerAlign;
    }

    public static String getPathSaveFileExport(String fileNameOut)
    {
        String pathOut = getFolderSave();
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        dateFormat.applyPattern("dd/MM/yyyy HH:mm:ss");
        String strCurTimeExp = dateFormat.format(new Date());
        strCurTimeExp = strCurTimeExp.replaceAll("/", "_");
        strCurTimeExp = strCurTimeExp.replaceAll(" ", "_");
        strCurTimeExp = strCurTimeExp.replaceAll(":", "_");
        pathOut = pathOut + fileNameOut + strCurTimeExp + Config.XLSX_FILE_EXTENTION;

        return pathOut;
    }

    public static String getFolderSave()
    {
        String pathOut;
        ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance()
                .getExternalContext().getContext();
        pathOut = ctx.getRealPath("/") + Config.PATH_OUT;
        File folderOut = new File(pathOut);
        if (!folderOut.exists())
        {
            folderOut.mkdirs();
        }
        return pathOut;
    }

    public static File exportFile(List lstDTO, List<SimpleEntry<String, String>> headerAlign,
                                  String headerPrefix, String pathTemplate, String fileNameOut, int startRow,
                                  String subTitle, int cellTitleIndex, String... title) throws Exception
    {
        String pathOut = getPathSaveFileExport(fileNameOut);

        try
        {
            InputStream fileTemplate = new FileInputStream(pathTemplate);
            Workbook workbook = WorkbookFactory.create(fileTemplate);
//            if (pathTemplate.endsWith(".xls") || pathTemplate.endsWith(".XLS")) {
//                workbook = new HSSFWorkbook(fileTemplate);
//            } else if (pathTemplate.endsWith(".xlsx") || pathTemplate.endsWith(".XLSX")) {
//                workbook = WorkbookFactory.create(fileTemplate);
//            }
            Sheet worksheet = workbook.getSheetAt(0);

            CellStyle cellStyleTitle = setStyleForWorkbook(workbook);
            initDataToSheet(workbook, worksheet, cellStyleTitle, lstDTO,
                    headerAlign, headerPrefix,
                    startRow, subTitle, cellTitleIndex, title);
            try
            {

                FileOutputStream fileOut = new FileOutputStream(pathOut);
                workbook.write(fileOut);
                fileOut.flush();
                fileOut.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return new File(pathOut);
    }

    public static File exportFileMultiSheet(ExcelDTO excel, String pathTemplate, String fileNameOut) throws Exception
    {
        String pathOut = getPathSaveFileExport(fileNameOut);
        try
        {
            InputStream fileTemplate = new FileInputStream(pathTemplate);
            Workbook workbook = null;
            if (pathTemplate.endsWith(".xls") || pathTemplate.endsWith(".XLS"))
            {
                workbook = new HSSFWorkbook(fileTemplate);
            }
            else if (pathTemplate.endsWith(".xlsx") || pathTemplate.endsWith(".XLSX"))
            {
                workbook = new XSSFWorkbook(fileTemplate);
            }
            CellStyle cellStyleTitle = setStyleForWorkbook(workbook);

            List<SheetDTO> lstAllSheet = excel.getLstDataSheel();
            int i = 0;
            for (SheetDTO sheet : lstAllSheet)
            {
                Sheet worksheet = workbook.getSheetAt(i);
                initDataToSheet(workbook, worksheet, cellStyleTitle, sheet.getDataSheet(),
                        sheet.getHeaderAlign(), sheet.getHeaderPrefix(),
                        sheet.getStartRow(),
                        sheet.getSubTitle(),
                        sheet.getCellTitleIndex(),
                        sheet.getTitle());
                i++;
            }
            try
            {

                FileOutputStream fileOut = new FileOutputStream(pathOut);
                workbook.write(fileOut);
                fileOut.flush();
                fileOut.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return new File(pathOut);

    }

    //Quytv7_export_file_date_start
    public static File exportFileDate(int sheetIndex, List lstDTO, String startDate, String endDate, List<SimpleEntry<String, String>> headerAlign,
                                      String headerPrefix, String pathTemplate, String fileNameOut, int startRow,
                                      String subTitle, int cellTitleIndex, String... title) throws Exception
    {
        String pathOut = getPathSaveFileExport(fileNameOut);
        try
        {
            InputStream fileTemplate = new FileInputStream(pathTemplate);
            Workbook workbook = null;
            if (pathTemplate.endsWith(".xls") || pathTemplate.endsWith(".XLS"))
            {
                workbook = new HSSFWorkbook(fileTemplate);
            }
            else if (pathTemplate.endsWith(".xlsx") || pathTemplate.endsWith(".XLSX"))
            {
                workbook = new XSSFWorkbook(fileTemplate);
            }
            Sheet worksheet = workbook.getSheetAt(sheetIndex);

            CellStyle cellStyleTitle = setStyleForWorkbook(workbook);
            initDataToSheetDate(workbook, worksheet, cellStyleTitle, lstDTO, startDate, endDate,
                    headerAlign, headerPrefix,
                    startRow, subTitle, cellTitleIndex, title);
            try
            {

                FileOutputStream fileOut = new FileOutputStream(pathOut);
                workbook.write(fileOut);
                fileOut.flush();
                fileOut.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return new File(pathOut);
    }

    private static void initDataToSheetDate(Workbook workbook,
                                            Sheet worksheet, CellStyle cellStyleTitle,
                                            List lstDTO, String startDate, String endDate, List<SimpleEntry<String, String>> headerAlign,
                                            String headerPrefix,
                                            int startRow, String subTitle, int cellTitleIndex, String... title)
            throws Exception
    {

        if (title != null && title.length > 0)
        {
            Row rowMainTitle = worksheet.createRow(startRow - 4);
            Cell mainCellTitle = rowMainTitle.createCell(cellTitleIndex - 2);
            mainCellTitle.setCellValue(title[0]);
            mainCellTitle.setCellStyle(cellStyleTitle);
            worksheet.addMergedRegion(new CellRangeAddress(startRow - 4, startRow - 4, cellTitleIndex - 2, cellTitleIndex + 2));
        }

        CellStyle cellStyleDate = workbook.createCellStyle();
        cellStyleDate.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyleDate.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        cellStyleDate.setWrapText(false);
        Font hSSFFontDate = workbook.createFont();
        hSSFFontDate.setFontName(HSSFFont.FONT_ARIAL);
        hSSFFontDate.setFontHeightInPoints((short) 10);
        hSSFFontDate.setColor(HSSFColor.BLACK.index);
        hSSFFontDate.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        cellStyleDate.setFont(hSSFFontDate);
        if (startDate != null && endDate != null)
        {
            Row rowMainTitle = worksheet.createRow(startRow - 3);
            Cell mainCellTitle = rowMainTitle.createCell(cellTitleIndex - 2);
            mainCellTitle.setCellValue(MessageUtil.getResourceBundleMessage("report.startTime")
                    + startDate + "  " + MessageUtil.getResourceBundleMessage("report.endTime") + "  " + endDate);
            mainCellTitle.setCellStyle(cellStyleDate);
            worksheet.addMergedRegion(new CellRangeAddress(startRow - 3, startRow - 3, cellTitleIndex - 2, cellTitleIndex + 2));
        }

        Row rowsubTitle = worksheet.createRow(startRow - 2);
        Cell cellsubTitle = rowsubTitle.createCell(cellTitleIndex);
        cellsubTitle.setCellValue(subTitle);

        //header
        Row rowHeader = worksheet.createRow(startRow);
        rowHeader.setHeight((short) 500);

        CellStyle cellStyleHeader = workbook.createCellStyle();
        cellStyleHeader.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyleHeader.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        cellStyleHeader.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyleHeader.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyleHeader.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyleHeader.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellStyleHeader.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
        cellStyleHeader.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cellStyleHeader.setWrapText(false);
        Font hSSFFontHeader = workbook.createFont();
        hSSFFontHeader.setFontName(HSSFFont.FONT_ARIAL);
        hSSFFontHeader.setFontHeightInPoints((short) 10);
        hSSFFontHeader.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        hSSFFontHeader.setColor(HSSFColor.BLUE.index);
        cellStyleHeader.setFont(hSSFFontHeader);

        for (int i = -1; i < headerAlign.size(); i++)
        {
            Cell cellHeader = rowHeader.createCell(i + 1);
            if (i == -1)
            {
                cellHeader.setCellValue(MessageUtil.getResourceBundleMessage("datatable.header.stt"));
            }
            else
            {
                SimpleEntry<String, String> entry = headerAlign.get(i);
                cellHeader.setCellValue(MessageUtil.getResourceBundleMessage(entry.getKey()));

            }
            cellHeader.setCellStyle(cellStyleHeader);
        }

        //trai
        CellStyle cellStyleLeft = workbook.createCellStyle();
        cellStyleLeft.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        cellStyleLeft.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        cellStyleLeft.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyleLeft.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyleLeft.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyleLeft.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellStyleLeft.setWrapText(false);
        //phai
        CellStyle cellStyleRight = workbook.createCellStyle();
        cellStyleRight.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        cellStyleRight.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        cellStyleRight.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyleRight.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyleRight.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyleRight.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellStyleRight.setWrapText(false);
        //giua
        CellStyle cellStyleCenter = workbook.createCellStyle();
        cellStyleCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyleCenter.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        cellStyleCenter.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyleCenter.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyleCenter.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyleCenter.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellStyleCenter.setWrapText(false);

        //Data
        if (lstDTO != null && !lstDTO.isEmpty())
        {
            //init mapColumn
            Object firstRow = lstDTO.get(0);
            Map<String, Field> mapField = new HashMap<String, Field>();
            for (int j = 0; j < headerAlign.size(); j++)
            {
                SimpleEntry<String, String> entryHeader = headerAlign.get(j);
                String header = entryHeader.getKey();
                for (Field f : firstRow.getClass().getDeclaredFields())
                {
                    f.setAccessible(true);
                    if (f.getName().equals(header))
                    {
                        mapField.put(header, f);
                    }
                }
            }

            //fillData
            for (int i = 0; i < lstDTO.size(); i++)
            {
                Row row = worksheet.createRow(i + startRow + 1);
                for (int j = -1; j < headerAlign.size(); j++)
                {
                    Cell cell = row.createCell(j + 1);
                    if (j == -1)
                    {
                        cell.setCellValue(i + 1);
                        cell.setCellStyle(cellStyleCenter);
                    }
                    else
                    {
                        SimpleEntry<String, String> entryHeader = headerAlign.get(j);
                        String header = entryHeader.getKey();
                        String align = entryHeader.getValue();
                        Object obj = lstDTO.get(i);
                        Field f = mapField.get(header);
//                            f.setAccessible(true);
                        if (f.getName().equals(header))
                        {
                            Object value = f.get(obj);
                            cell.setCellValue(value == null ? "" : value.toString());
                            if ("CENTER".equals(align))
                            {
                                cell.setCellStyle(cellStyleCenter);
                            }
                            if ("LEFT".equals(align))
                            {
                                cell.setCellStyle(cellStyleLeft);
                            }
                            if ("RIGHT".equals(align))
                            {
                                cell.setCellStyle(cellStyleRight);
                            }
                        }

                    }
                }

            }
        }

        //Set Width
        for (int i = 0; i <= headerAlign.size(); i++)
        {
            worksheet.autoSizeColumn(i);
            if (worksheet.getColumnWidth(i) > 20000)
            {
                worksheet.setColumnWidth(i, 20000);
            }
        }
    }
    //Quytv7_export_file_date_start

    private static CellStyle setStyleForWorkbook(Workbook workbook)
    {
        CellStyle cellStyle;

        CellStyle cellStyleFormatNumber = workbook.createCellStyle();
        cellStyleFormatNumber.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
        cellStyleFormatNumber.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyleFormatNumber.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyleFormatNumber.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyleFormatNumber.setBorderTop(HSSFCellStyle.BORDER_THIN);

        cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellStyle.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cellStyle.setWrapText(false);

        //Title of report
        CellStyle cellStyleTitle = workbook.createCellStyle();
        cellStyleTitle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyleTitle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        Font hSSFFont = workbook.createFont();
        hSSFFont.setFontName(HSSFFont.FONT_ARIAL);
        hSSFFont.setFontHeightInPoints((short) 20);
        hSSFFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        hSSFFont.setColor(HSSFColor.BLACK.index);
        cellStyleTitle.setFont(hSSFFont);
        return cellStyleTitle;
    }

    private static void initDataToSheet(Workbook workbook,
                                        Sheet worksheet, CellStyle cellStyleTitle,
                                        List lstDTO, List<SimpleEntry<String, String>> headerAlign,
                                        String headerPrefix,
                                        int startRow, String subTitle, int cellTitleIndex, String... title)
            throws Exception
    {

        if (title != null && title.length > 0)
        {
            Row rowMainTitle = worksheet.createRow(startRow - 4);
            Cell mainCellTitle = rowMainTitle.createCell(cellTitleIndex - 2);
            mainCellTitle.setCellValue(title[0]);
            mainCellTitle.setCellStyle(cellStyleTitle);
            worksheet.addMergedRegion(new CellRangeAddress(startRow - 4, startRow - 4, cellTitleIndex - 2, cellTitleIndex + 2));
        }

        Row rowsubTitle = worksheet.createRow(startRow - 2);
        Cell cellsubTitle = rowsubTitle.createCell(cellTitleIndex);
        cellsubTitle.setCellValue(subTitle);

        //header
        Row rowHeader = worksheet.createRow(startRow);
        rowHeader.setHeight((short) 500);

        CellStyle cellStyleHeader = workbook.createCellStyle();
        cellStyleHeader.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyleHeader.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        cellStyleHeader.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyleHeader.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyleHeader.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyleHeader.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellStyleHeader.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
        cellStyleHeader.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cellStyleHeader.setWrapText(false);
        Font hSSFFontHeader = workbook.createFont();
        hSSFFontHeader.setFontName(HSSFFont.FONT_ARIAL);
        hSSFFontHeader.setFontHeightInPoints((short) 10);
        hSSFFontHeader.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        hSSFFontHeader.setColor(HSSFColor.BLUE.index);
        cellStyleHeader.setFont(hSSFFontHeader);

        for (int i = -1; i < headerAlign.size(); i++)
        {
            Cell cellHeader = rowHeader.createCell(i + 1);
            if (i == -1)
            {
                cellHeader.setCellValue(MessageUtil.getResourceBundleMessage("datatable.header.stt"));
            }
            else
            {
                SimpleEntry<String, String> entry = headerAlign.get(i);
                String[] header = entry.getKey().split("=");
                String hd = header.length > 1 ? header[1] : header[0];
                cellHeader.setCellValue(MessageUtil.getResourceBundleMessage(hd));

            }
            cellHeader.setCellStyle(cellStyleHeader);
        }

        //trai
        CellStyle cellStyleLeft = workbook.createCellStyle();
        cellStyleLeft.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        cellStyleLeft.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        cellStyleLeft.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyleLeft.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyleLeft.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyleLeft.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellStyleLeft.setWrapText(false);
        //phai
        CellStyle cellStyleRight = workbook.createCellStyle();
        cellStyleRight.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        cellStyleRight.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        cellStyleRight.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyleRight.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyleRight.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyleRight.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellStyleRight.setWrapText(false);
        //giua
        CellStyle cellStyleCenter = workbook.createCellStyle();
        cellStyleCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyleCenter.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        cellStyleCenter.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyleCenter.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyleCenter.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyleCenter.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellStyleCenter.setWrapText(false);

        //Data
        if (lstDTO != null && !lstDTO.isEmpty())
        {
            //init mapColumn
//            Object firstRow = lstDTO.get(0);
//            Map<String, DatapathObject> mapField = new HashMap<String, DatapathObject>();
//            for (int j = 0; j < headerAlign.size(); j++) {
//                SimpleEntry<String, String> entryHeader = headerAlign.get(j);
//                String[] header = entryHeader.getKey().split("=");
//                
//                DatapathObject dObj = resolveDatapath(header[0], firstRow);
//                mapField.put(header[0], dObj);
//            }

            //fillData
            for (int i = 0; i < lstDTO.size(); i++)
            {
                Row row = worksheet.createRow(i + startRow + 1);
                for (int j = -1; j < headerAlign.size(); j++)
                {
                    Cell cell = row.createCell(j + 1);
                    if (j == -1)
                    {
                        cell.setCellValue(i + 1);
                        cell.setCellStyle(cellStyleCenter);
                    }
                    else
                    {
                        SimpleEntry<String, String> entryHeader = headerAlign.get(j);
                        String[] header = entryHeader.getKey().split("=");
                        String align = entryHeader.getValue();
                        Object obj = lstDTO.get(i);

                        DatapathObject dObj = resolveDatapath(header[0], obj);
                        try
                        {
                            dObj.getField().setAccessible(true);
                        }
                        catch (Exception ex)
                        {
                            System.out.println(header[0] + "-" + header[1]);
                        }
                        Object value = dObj.getField().get(dObj.getParent());
                        cell.setCellValue(value == null ? "" : value.toString());
                        if ("CENTER".equals(align))
                        {
                            cell.setCellStyle(cellStyleCenter);
                        }
                        if ("LEFT".equals(align))
                        {
                            cell.setCellStyle(cellStyleLeft);
                        }
                        if ("RIGHT".equals(align))
                        {
                            cell.setCellStyle(cellStyleRight);
                        }
                    }
                }

            }
        }

        //Set Width
        for (int i = 0; i <= headerAlign.size(); i++)
        {
            worksheet.autoSizeColumn(i);
            if (worksheet.getColumnWidth(i) > 20000)
            {
                worksheet.setColumnWidth(i, 20000);
            }
        }
    }

    public static String getPathSaveFile(String fileNameOut, String extension)
    {
        String pathOut;
        pathOut = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath()
                + Config.PATH_OUT;
        File folderOut = new File(pathOut);
        if (!folderOut.exists())
        {
            folderOut.mkdir();
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        dateFormat.applyPattern("dd/MM/yyyy HH:mm:ss");
        String strCurTimeExp = dateFormat.format(new Date());
        strCurTimeExp = strCurTimeExp.replaceAll("/", "_");
        strCurTimeExp = strCurTimeExp.replaceAll(" ", "_");
        strCurTimeExp = strCurTimeExp.replaceAll(":", "_");
        pathOut = pathOut + fileNameOut + strCurTimeExp + extension;

        return pathOut;
    }

    private static DatapathObject resolveDatapath(String path, Object parent) throws
            IllegalArgumentException, IllegalAccessException
    {
        String subString = path;
        if (!subString.contains("."))
        {
            //We haven reached the end of the path
            Field field = getField(subString, parent.getClass());
            return new DatapathObject(parent, field);
        }

        //We haven't reached the end of the 
        subString = path.substring(0, path.indexOf("."));
        Field field = getField(subString, parent.getClass());
        field.setAccessible(true);
        return resolveDatapath(path.substring(path.indexOf(".") + 1),
                field.get(parent));
    }

    private static Field getField(String name, Class<?> parent)
    {
        Field[] fields = parent.getDeclaredFields();

        for (Field f : fields)
        {
            String current = f.getName();
            if (current.equalsIgnoreCase(name))
            {
                try
                {
                    return parent.getDeclaredField(current);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    static class DatapathObject
    {

        private final Object parent;
        private final Field field;

        public DatapathObject(Object parent, Field field)
        {
            this.parent = parent;
            this.field = field;
        }

        public Object getParent()
        {
            return parent;
        }

        public Field getField()
        {
            return field;
        }
    }

    //tuanpv
    public static File saveFileResultFromInputFile(int sheetIndex, int startRow, int startColumn,
                                                   InputStream fileTemplate, String fileNameOut, List<String> lstData,
                                                   String header1, int headerRow1, String header2, int headerRow2)
    {
        String pathOut = getPathSaveFileExport(fileNameOut);
        try
        {
//            InputStream fileTemplate = new FileInputStream(pathTemplate);
            Workbook workbook = null;
            if (fileNameOut.endsWith(".xls") || fileNameOut.endsWith(".XLS"))
            {
                workbook = new HSSFWorkbook(fileTemplate);
            }
            else if (fileNameOut.endsWith(".xlsx") || fileNameOut.endsWith(".XLSX"))
            {
                workbook = new XSSFWorkbook(fileTemplate);
            }
            Sheet worksheet = workbook.getSheetAt(sheetIndex);

//            CellStyle cellStyleTitle = setStyleForWorkbook(workbook);
            //header
            Row rowHeaderTemp1 = worksheet.getRow(headerRow1);
            Cell cellHeaderTemp1 = rowHeaderTemp1.getCell(0);
            CellStyle cellStyleHeaderTemp1 = cellHeaderTemp1.getCellStyle();
            Cell cellHeader1 = rowHeaderTemp1.createCell(startColumn);
            cellHeader1.setCellValue(header1);
            cellHeader1.setCellStyle(cellStyleHeaderTemp1);

            Row rowHeaderTemp2 = worksheet.getRow(headerRow2);
            Cell cellHeaderTemp2 = rowHeaderTemp2.getCell(0);
            CellStyle cellStyleHeaderTemp2 = cellHeaderTemp2.getCellStyle();
            Cell cellHeader2 = rowHeaderTemp2.createCell(startColumn);
            cellHeader2.setCellValue(header2);
            cellHeader2.setCellStyle(cellStyleHeaderTemp2);

            Row rowValue = worksheet.getRow(startRow);
            Cell cellValue = rowValue.getCell(0);
            CellStyle cellStyle = cellValue.getCellStyle();

            for (int i = 0; i < lstData.size(); i++)
            {
                Row row = worksheet.getRow(i + startRow);
                Cell cell = row.createCell(startColumn);
                cell.setCellValue(lstData.get(i));
                cell.setCellStyle(cellStyle);

            }

            worksheet.autoSizeColumn(startColumn);

            try
            {

                FileOutputStream fileOut = new FileOutputStream(pathOut);
                workbook.write(fileOut);
                fileOut.flush();
                fileOut.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return new File(pathOut);
    }
}
