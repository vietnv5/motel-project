package com.slook.util;

import com.google.gson.GsonBuilder;
import com.slook.exception.AppException;
import org.apache.commons.beanutils.BasicDynaBean;
import org.apache.commons.beanutils.BasicDynaClass;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.primefaces.event.FileUploadEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

import static org.apache.log4j.Logger.getLogger;

@Service(value = "importer")
@Scope("session")
public abstract class Importer<T extends Serializable> implements Constant
{

    private static final Logger logger = getLogger(Importer.class);
    private Class<T> domainClass;
    private Map<Integer, String> indexMapFieldClass = getIndexMapFieldClass();
    private String dateFormat = "MM/dd/yyyy";
    private Workbook workbook;
    private Sheet sheet;
    private File fileResult;

    //    @Autowired
    ExcelWriterUtils excelUtil;

    /**
     * @return Map<Integer, String>
     * <br>Start column = 0
     * @author huynx6
     */
    protected abstract Map<Integer, String> getIndexMapFieldClass();

    protected abstract String getDateFormat();

    private Map<Integer, String> mapHeader = getMapHeader();

    @SuppressWarnings("unchecked")
    public Importer()
    {
        super();
        java.lang.reflect.Type genericSuperclass = getClass().getGenericSuperclass();
        if (genericSuperclass != null && genericSuperclass instanceof ParameterizedType)
        {
            this.domainClass = (Class<T>) ((ParameterizedType) genericSuperclass).getActualTypeArguments()[0];
            if (domainClass.getName().equals("java.io.Serializable"))
            {
                this.domainClass = (Class<T>) BasicDynaClass.class;
            }
        }
        else
        {
            this.domainClass = (Class<T>) BasicDynaClass.class;
        }
        if (getDateFormat() != null)
        {
            dateFormat = getDateFormat();
        }
    }

    public List<T> getDatas(FileUploadEvent event, Integer sheetNumber, String sline)
    {
        try
        {
            return getDatas(event.getFile().getInputstream(), sheetNumber, sline, null);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public List<T> getDatas(FileUploadEvent event, String sheetName, String sline)
    {
        try
        {
            return getDatas(event.getFile().getInputstream(), null, sline, sheetName);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public List<T> getDatas(InputStream inputStream, Integer sheetNumber, String sline)
    {
        return getDatas(inputStream, sheetNumber, sline, null);
    }

    public List<T> getDatas(InputStream inputStream, Integer sheetNumber, Integer startLine)
    {
        return getDatas(inputStream, sheetNumber, startLine, null);
    }

    public List<T> getDatas(InputStream inputStream, String sline, String sheetName)
    {
        return getDatas(inputStream, null, sline, sheetName);
    }

    private List<T> getDatas(InputStream inputStream, Integer sheetNumber, String sline, String sheetName)
    {

        try
        {
            //Get the workbook instance for XLS/xlsx file
            Workbook workbook = null;
            try
            {
                workbook = WorkbookFactory.create(inputStream);
                if (workbook == null)
                {
                    throw new NullPointerException();
                }
            }
            catch (InvalidFormatException e2)
            {
                e2.printStackTrace();
                throw new AppException("File import phải là Excel 97-2012 (xls, xlsx)!");
            }
            return getDatas(workbook, sheetNumber, sline, sheetName);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<T> getDatas(Workbook workbook, Integer sheetNumber, String sline)
    {
        return getDatas(workbook, sheetNumber, sline, null);
    }

    public List<T> getDatas(Workbook workbook, String sheetName, String sline)
    {
        return getDatas(workbook, null, sline, sheetName);
    }

    @SuppressWarnings("unchecked")
    //sline format
    //list: 1,2,3,4
    //from-to: 2-5
    private List<T> getDatas(Workbook workbook, Integer sheetNumber, String sline, String sheetName)
    {
        try
        {
            if (workbook == null)
            {
                throw new NullPointerException();
            }

            String[] mulLine = sline.split(",", -1);

            List<T> list = new LinkedList<T>();
            Sheet sheet = null;
            if (sheetNumber != null)
            {
                sheet = workbook.getSheetAt(sheetNumber);
            }
            else if (sheetName != null)
            {
                sheet = workbook.getSheet(sheetName);
            }
            if (sheet == null)
            {
                return list;
            }
            List<Field> fields = new ArrayList<>();
            try
            {
                Class<?> cls = domainClass;
                fields = Arrays.asList(cls.getDeclaredFields());
            }
            catch (SecurityException e)
            {
                e.printStackTrace();
            }
            int line = 1;
            String sLine = "";
            boolean isFindHeader = false;
            for (Iterator<Row> rowIterator = sheet.iterator(); rowIterator.hasNext(); )
            {
                Row row = (Row) rowIterator.next();
                boolean cont = false;
                sLine = "";
                try
                {
                    if (Pattern.compile("\"" + line + "\"").matcher(new GsonBuilder().create().toJson(mulLine)).find())
                    {
                        cont = true;
                    }
                    else
                    {
                        for (String _sline : mulLine)
                        {
                            String[] strip = _sline.split("-", -1);
                            if (strip.length == 1)
                            {
                                if (line == Integer.parseInt(strip[0]))
                                {
                                    cont = true;
                                    break;
                                }
                            }
                            else if (strip.length == 2)
                            {
                                if (strip[1].length() == 0)
                                {
                                    if (line >= Integer.parseInt(strip[0]))
                                    {
                                        cont = true;
                                        break;
                                    }
                                }
                                else if (line >= Integer.parseInt(strip[0]) && line <= Integer.parseInt(strip[1]))
                                {
                                    cont = true;
                                    break;
                                }

                            }

                        }
                    }
                }
                catch (Exception e1)
                {
                    e1.printStackTrace();
                }
                line++;
                if (!cont)
                {
                    continue;
                }
                if (mapHeader != null)
                {

                    if (!isFindHeader)
                    {
                        boolean isCont = false;
                        for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++)
                        {
                            if (row.getCell(i).getCellType() == Cell.CELL_TYPE_STRING && mapHeader.get(1).equalsIgnoreCase(row.getCell(i).getStringCellValue()))
                            {
                                isFindHeader = true;
                                isCont = true;
                                if (i > 0)
                                {
                                    Map<Integer, String> _indexMapFieldClass = new HashMap<Integer, String>();
                                    for (Iterator<Integer> iterator = indexMapFieldClass.keySet().iterator(); iterator.hasNext(); )
                                    {
                                        Integer col = (Integer) iterator.next();
                                        _indexMapFieldClass.put(i + col, indexMapFieldClass.get(col));
                                    }
                                    indexMapFieldClass.clear();
                                    indexMapFieldClass.putAll(_indexMapFieldClass);
                                }
                                break;
                            }
                        }
                        if (isCont)
                        {
                            continue;
                        }
                    }
                    if (!isFindHeader)
                    {
                        continue;
                    }

                }
                {//Read data
                    Class<?> obj = domainClass;

                    Object objInstance = obj.newInstance();
                    if (objInstance instanceof BasicDynaClass)
                    {
                        List<DynaProperty> dynaProperties = new ArrayList<DynaProperty>();
                        for (Integer idx : indexMapFieldClass.keySet())
                        {
                            dynaProperties.add(new DynaProperty(indexMapFieldClass.get(idx), String.class));
                        }
                        objInstance = new BasicDynaClass(sheetName, null, dynaProperties.toArray(new DynaProperty[dynaProperties.size()]));
                        objInstance = ((BasicDynaClass) objInstance).newInstance();
                    }
                    for (Iterator<Integer> columnIndex = indexMapFieldClass.keySet().iterator(); columnIndex.hasNext(); )
                    {
                        Integer nColumn = columnIndex.next();
                        Cell cell;
                        if (row.getCell(nColumn) == null)
                        {
                            cell = row.createCell(nColumn);
                        }
                        else
                        {
                            cell = row.getCell(nColumn);
                        }
                        Object cellValue = null;
                        switch (cell.getCellType())
                        {
                            case Cell.CELL_TYPE_BLANK:
                                cellValue = new String();
                                break;
                            case Cell.CELL_TYPE_BOOLEAN:
                                cellValue = cell.getBooleanCellValue();
                                break;
                            case Cell.CELL_TYPE_NUMERIC:
                                cell.setCellType(Cell.CELL_TYPE_STRING);
                            case Cell.CELL_TYPE_STRING:
                            default:
                                cellValue = cell.getStringCellValue();
                                break;
                        }
                        sLine += cellValue.toString();
                        if (fields.toString().contains(domainClass.getName() + "." + indexMapFieldClass.get(nColumn)))
                        {
                            try
                            {
                                //Field field = fields.get(index);
                                for (Field field : fields)
                                {
                                    if (field.getName().equalsIgnoreCase(indexMapFieldClass.get(nColumn)))
                                    {
                                        if (field.getType().getName().contains("java.lang.Long"))
                                        {
                                            PropertyUtils.setSimpleProperty(objInstance, field.getName(), Long.parseLong(cellValue.toString()));
                                        }
                                        else if (field.getType().getName().contains("java.lang.Integer"))
                                        {
                                            PropertyUtils.setSimpleProperty(objInstance, field.getName(), Integer.parseInt(cellValue.toString()));
                                        }
                                        else if (field.getType().getName().contains("java.lang.Double"))
                                        {
                                            PropertyUtils.setSimpleProperty(objInstance, field.getName(), Double.parseDouble(cellValue.toString()));
                                        }
                                        else if (field.getType().getName().contains("java.lang.String"))
                                        {
                                            PropertyUtils.setSimpleProperty(objInstance, field.getName(), cellValue);
                                        }
                                        else if (field.getType().getName().contains("Date"))
                                        {
                                            DateFormat formatter = new SimpleDateFormat(dateFormat);
                                            Date date = null;
                                            try
                                            {
                                                date = formatter.parse(cellValue.toString());
                                            }
                                            catch (Exception ex)
                                            {
                                                System.out.println(cellValue.toString());
                                                System.out.println(row);
                                                ex.printStackTrace();
                                            }
                                            PropertyUtils.setSimpleProperty(objInstance, field.getName(), date);
                                        }
                                        else if (field.getType().getName().contains("java.lang.Object"))
                                        {
                                            PropertyUtils.setSimpleProperty(objInstance, field.getName(), cellValue);
                                        }
                                        break;
                                    }
                                }

                            }
                            catch (IllegalArgumentException e)
                            {
//				    			e.printStackTrace();
                            }
                        }
                        else
                        {
                            if (objInstance instanceof BasicDynaBean)
                            {
                                PropertyUtils.setSimpleProperty(objInstance, normalizeParamCode(indexMapFieldClass.get(nColumn)), cellValue);
                            }
                        }
                    }
                    if (sLine.trim().length() == 0)
                    {
                        break;
                    }
                    list.add((T) objInstance);
                }
            }
            //dungvv8
            setWorkbook(workbook);
            return list;
        }
        catch (Exception e)
        {
            System.err.println(e.getMessage());
            return null;
        }

    }

    //dungvv8
    private List<T> getDatas(InputStream inputStream, Integer sheetNumber, Integer startRow, String sheetName)
    {

        try
        {
            //Get the workbook instance for XLS/xlsx file
            try
            {
                workbook = WorkbookFactory.create(inputStream);
            }
            catch (InvalidFormatException e2)
            {
                e2.printStackTrace();
                throw new AppException("File import phải là Excel 97-2012 (xls, xlsx)!");
            }
            return getDatas(workbook, sheetNumber, startRow, sheetName);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private List<T> getDatas(Workbook workbook, Integer sheetNumber, Integer startRow, String sheetName)
    {
        try
        {
            if (workbook == null)
            {
                throw new NullPointerException();
            }

            List<T> list = new LinkedList<>();
            if (sheetNumber != null)
            {
                sheet = workbook.getSheetAt(sheetNumber);
            }
            else if (sheetName != null)
            {
                sheet = workbook.getSheet(sheetName);
            }
            if (sheet == null)
            {
                return list;
            }
            List<Field> fields = new ArrayList<>();
            try
            {
                Class<?> cls = domainClass;
                fields = Arrays.asList(cls.getDeclaredFields());
            }
            catch (SecurityException e)
            {
                e.printStackTrace();
            }
            boolean isFindHeader = false;
            for (Row row : sheet)
            {
//                startRow++;
                if (row.getRowNum() < startRow)
                {
                    continue;
                }
                if (mapHeader != null)
                {
                    if (!isFindHeader)
                    {
                        boolean isCont = false;
                        for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++)
                        {
                            if (row.getCell(i).getCellType() == Cell.CELL_TYPE_STRING
                                    && mapHeader.get(1).equalsIgnoreCase(row.getCell(i).getStringCellValue()))
                            {
                                isFindHeader = true;
                                isCont = true;
                                if (i > 0)
                                {
                                    Map<Integer, String> _indexMapFieldClass = new HashMap<>();
                                    for (Integer col : indexMapFieldClass.keySet())
                                    {
                                        _indexMapFieldClass.put(i + col, indexMapFieldClass.get(col));
                                    }
                                    indexMapFieldClass.clear();
                                    indexMapFieldClass.putAll(_indexMapFieldClass);
                                }
                                break;
                            }
                        }
                        if (isCont)
                        {
                            continue;
                        }
                    }
                    if (!isFindHeader)
                    {
                        continue;
                    }
                }
                {//Read data
                    Class<?> obj = domainClass;

                    Object objInstance = obj.newInstance();
                    if (objInstance instanceof BasicDynaClass)
                    {
                        List<DynaProperty> dynaProperties = new ArrayList<>();
                        for (Integer idx : indexMapFieldClass.keySet())
                        {
                            dynaProperties.add(new DynaProperty(indexMapFieldClass.get(idx), String.class));
                        }
                        objInstance = new BasicDynaClass(sheetName, null, dynaProperties.toArray(new DynaProperty[dynaProperties.size()]));
                        objInstance = ((BasicDynaClass) objInstance).newInstance();
                    }
                    for (Integer nColumn : indexMapFieldClass.keySet())
                    {
                        Cell cell;
                        if (row.getCell(nColumn) == null)
                        {
                            cell = row.createCell(nColumn);
                        }
                        else
                        {
                            cell = row.getCell(nColumn);
                        }
                        Object cellValue = null;
                        switch (cell.getCellType())
                        {
                            case Cell.CELL_TYPE_BLANK:
                                cellValue = "";
                                break;
                            case Cell.CELL_TYPE_BOOLEAN:
                                cellValue = cell.getBooleanCellValue();
                                break;
                            case Cell.CELL_TYPE_NUMERIC:
                                if (DateUtil.isCellDateFormatted(cell))
                                {
                                    cellValue = cell.getDateCellValue();
                                    break;
                                }
                                else
                                {
                                    cell.setCellType(Cell.CELL_TYPE_STRING);
                                }
                            case Cell.CELL_TYPE_STRING:
                            default:
                                cellValue = cell.getStringCellValue();
                                break;
                        }
                        if (fields.toString().contains(domainClass.getName() + "." + indexMapFieldClass.get(nColumn)))
                        {
                            try
                            {
                                //Field field = fields.get(index);
                                for (Field field : fields)
                                {
                                    if (field.getName().equalsIgnoreCase(indexMapFieldClass.get(nColumn)))
                                    {
                                        if (field.getType().getName().contains("java.lang.Long"))
                                        {
                                            PropertyUtils.setSimpleProperty(objInstance, field.getName(), Long.parseLong(cellValue.toString()));
                                        }
                                        else if (field.getType().getName().contains("java.lang.Integer"))
                                        {
                                            PropertyUtils.setSimpleProperty(objInstance, field.getName(), Integer.parseInt(cellValue.toString()));
                                        }
                                        else if (field.getType().getName().contains("java.lang.Double"))
                                        {
                                            PropertyUtils.setSimpleProperty(objInstance, field.getName(), Double.parseDouble(cellValue.toString()));
                                        }
                                        else if (field.getType().getName().contains("java.lang.String"))
                                        {
                                            PropertyUtils.setSimpleProperty(objInstance, field.getName(), cellValue);
                                        }
                                        else if (field.getType().getName().contains("Date"))
                                        {
                                            if (cellValue instanceof Date)
                                            {
                                                PropertyUtils.setSimpleProperty(objInstance, field.getName(), cellValue);
                                            }
                                            else
                                            {
                                                DateFormat formatter = new SimpleDateFormat(dateFormat);
                                                Date date = null;
                                                try
                                                {
                                                    date = formatter.parse(cellValue.toString());
                                                }
                                                catch (Exception ex)
                                                {
                                                    System.out.println(cellValue.toString());
                                                    System.out.println(row);
                                                    ex.printStackTrace();
                                                }
                                                PropertyUtils.setSimpleProperty(objInstance, field.getName(), date);
                                            }
                                        }
                                        else if (field.getType().getName().contains("java.lang.Object"))
                                        {
                                            PropertyUtils.setSimpleProperty(objInstance, field.getName(), cellValue);
                                        }
                                        break;
                                    }
                                }

                            }
                            catch (IllegalArgumentException e)
                            {
//				    			e.printStackTrace();
                            }
                        }
                        else
                        {
                            if (objInstance instanceof BasicDynaBean)
                            {
                                PropertyUtils.setSimpleProperty(objInstance, normalizeParamCode(indexMapFieldClass.get(nColumn)), cellValue);
                            }
                        }
                    }
                    list.add((T) objInstance);
                }
            }
            return list;
        }
        catch (Exception e)
        {
            System.err.println(e.getMessage());
            return null;
        }

    }

    public void setResultImport(Integer startRow, List<String> results, int resultColumn)
    {
        try
        {

            for (Row row : sheet)
            {
                int rowNum = row.getRowNum();
                Cell cellResult = row.createCell(resultColumn);
                if (rowNum == startRow - 1)
                {
                    String header = MessageUtil.getKey("import.result");

                    cellResult.setCellValue(header);
                    cellResult.setCellStyle(excelUtil.getCsColHeader());
                }
                if (rowNum >= startRow)
                {
                    String result = results.get(rowNum - startRow);
                    cellResult.setCellValue(result);
                    if (MessageUtil.getKey("msg.insert.succ").equals(result)
                            || MessageUtil.getKey("msg.update.succ").equals(result))
                    {
                        cellResult.setCellStyle(excelUtil.getCsImportSucc());
                    }
                    else
                    {
                        cellResult.setCellStyle(excelUtil.getCsImportFail());
                    }
                }
            }
        }
        catch (Exception ex)
        {
            logger.error(ex.getMessage(), ex);
        }
    }

    public void writeResultImport(Integer startRow, List<String> results, int resultColumn, String fileName)
    {
        try
        {
            excelUtil = new ExcelWriterUtils();
            excelUtil.setWorkbook(workbook);
            String pathDirOut = excelUtil.getFolderOut();
            String pathFileOut = pathDirOut + excelUtil.getReportName(fileName);

            OutputStream fileOut = new FileOutputStream(pathFileOut);

            this.setResultImport(startRow, results, resultColumn);
            this.getWorkbook().write(fileOut);

            fileResult = new File(pathFileOut);
        }
        catch (Exception ex)
        {
            logger.error(ex.getMessage(), ex);
        }
    }

    public void setIndexMapFieldClass(Map<Integer, String> indexMapFieldClass)
    {
        this.indexMapFieldClass = indexMapFieldClass;
    }

    public Map<Integer, String> getMapHeader()
    {
        return mapHeader;
    }

    public void setMapHeader(Map<Integer, String> mapHeader)
    {
        this.mapHeader = mapHeader;
    }

    public void setDomainClass(Class<T> domainClass)
    {
        this.domainClass = domainClass;
    }

    public static String normalizeParamCode(String paramCode)
    {
        if (paramCode != null)
        {
            return paramCode.replace("(", "").replace(")", "").replace("[", "").replace("]", "");
        }
        return null;
    }

    public Workbook getWorkbook()
    {
        return workbook;
    }

    public void setWorkbook(Workbook workbook)
    {
        this.workbook = workbook;
    }

    public Sheet getSheet()
    {
        return sheet;
    }

    public void setSheet(Sheet sheet)
    {
        this.sheet = sheet;
    }

    public File getFileResult()
    {
        return fileResult;
    }

    public void setFileResult(File fileResult)
    {
        this.fileResult = fileResult;
    }

    public ExcelWriterUtils getExcelUtils()
    {
        return excelUtil;
    }

    public void setExcelUtils(ExcelWriterUtils excelUtil)
    {
        this.excelUtil = excelUtil;
    }
}
