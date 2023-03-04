package com.slook.controller;


import com.google.gson.Gson;

import com.slook.object.Series;
import org.primefaces.context.RequestContext;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Chart Controller
 *
 * @author Babji Prashanth, Chetty
 */
@ManagedBean
@ViewScoped
public class ChartController
{
    private String chartData;
    private String categories;
    private List<String> categoryList = new ArrayList<String>();
    private List<List<Object>> heapSizeList = new ArrayList<>();
    private List<List<Object>> usedHeapSizeList = new ArrayList<>();
    SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");//dd/MM/yyyy
    private static final long MB = 1024 * 1024;
    int index = 0;
    private Long[] longs;

    /**
     * Load Chart Data
     */
    public void loadChartData()
    {
        if (heapSizeList.size() > 10)
        {
            heapSizeList.remove(0);
            usedHeapSizeList.remove(0);
            categoryList.remove(0);
        }
        List<Series> series = new ArrayList<Series>();
        for (int i = 0; i < 500; i++)
        {
            malloc();
            long heapSize = Runtime.getRuntime().maxMemory();
            long heap = heapSize / MB;
            List<Object> objHeap = new ArrayList<>();
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR, i);
            calendar.add(Calendar.MINUTE, i);
            objHeap.add(calendar.getTime().getTime());
            objHeap.add(heap);
            heapSizeList.add(objHeap);
            long l = (heapSize - Runtime.getRuntime().freeMemory()) / MB;
            List<Object> oUl = new ArrayList<>();
            oUl.add(calendar.getTime().getTime());
            oUl.add(l);
            usedHeapSizeList.add(oUl);
//            categoryList.add(sdfDate.format(calendar.getTime()));
            categoryList.add(calendar.getTime().getTime() + "");
        }
        series.add(new Series("Heap Size", heapSizeList));
        series.add(new Series("Used Heap", usedHeapSizeList));
        setChartData(new Gson().toJson(series));
        setCategories(new Gson().toJson(categoryList));

        RequestContext reqCtx = RequestContext.getCurrentInstance();
        reqCtx.addCallbackParam("categories", getCategories());
        reqCtx.addCallbackParam("chartData", getChartData());
        reqCtx.addCallbackParam("chartId", "container");
        reqCtx.addCallbackParam("chartType", "column");
        reqCtx.addCallbackParam("chartTitle", "Sample chart");
        reqCtx.addCallbackParam("chartSubTitle", "Sub Sample chart");
        reqCtx.addCallbackParam("yAxisTitle", "yAxisTitle");
        reqCtx.addCallbackParam("xAxisTitle", "xAxisTitle");
        //linear, logarithmic, datetime or category.
        reqCtx.addCallbackParam("xAxisType", "datetime");

    }

    /**
     * @return the chartData
     */
    public String getChartData()
    {
        return chartData;
    }

    /**
     * @param chartData the chartData to set
     */
    public void setChartData(String chartData)
    {
        this.chartData = chartData;
    }

    /**
     * @return the categories
     */
    public String getCategories()
    {
        return categories;
    }

    /**
     * @param categories the categories to set
     */
    public void setCategories(String categories)
    {
        this.categories = categories;
    }

    private void malloc()
    {
        if (index % 2 == 0)
        {
            longs = new Long[100000];
            for (int i = 0; i < 1000; i++)
            {
                longs[i] = Long.valueOf(i);
            }
        }
        else
        {
            longs = null;
        }
        index++;
    }
}
