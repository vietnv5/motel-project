package com.slook.controller;

/**
 * Created by dungvv8 on 2/24/2017.
 */

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import com.slook.object.Series;
import com.slook.persistence.MemberService;
import com.slook.util.Constant;
import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import java.util.*;

@ManagedBean
public class DashBoardController implements Constant, Serializable
{

    private static final Logger logger = Logger.getLogger(DashBoardController.class);

    @PostConstruct
    public void onStart()
    {

    }

    public void loadChartData()
    {
        //Số lượng hội viên theo từng địa điểm
        BarChartModel numOfMemModel = new BarChartModel();
        numOfMemModel.setStacked(true);
        ChartSeries memberNews = new ChartSeries();
        ChartSeries memberOlds = new ChartSeries();
        Long max = 0l;
        Long min = Long.MAX_VALUE;
        String sqlMemNew = "SELECT\n"
                + "  nvl(a.count, 0) count,\n"
                + "  c.BRANCH_ID,\n"
                + "  c.BRANCH_NAME\n"
                + "FROM (\n"
                + "       SELECT\n"
                + "         count(*) count,\n"
                + "         m.BRANCH_ID\n"
                + "       FROM MEMBER m\n"
                + "       WHERE m.JOIN_DATE BETWEEN trunc(sysdate - 10) AND sysdate\n"
                + "       GROUP BY m.BRANCH_ID\n"
                + "     ) a\n"
                + "  RIGHT JOIN CAT_BRANCH c ON a.BRANCH_ID = c.BRANCH_ID\n"
                + "ORDER BY c.BRANCH_ID";
        List<?> list = new MemberService().findListSQLAll(sqlMemNew);
        memberNews.setLabel("Số hội viên mới");
        for (Object o : list)
        {
            Object[] obj = (Object[]) o;
            Long nCount = Long.valueOf(obj[0].toString());
            memberNews.set(obj[2], nCount);
            if (nCount > max)
            {
                max = nCount;
            }
            if (nCount < min)
            {
                min = nCount;
            }
        }
//        memberNews.set("SITE1",2);
        String sqlMemOld = "SELECT\n"
                + "  nvl(a.count, 0) count,\n"
                + "  c.BRANCH_ID,\n"
                + "  c.BRANCH_NAME\n"
                + "FROM (\n"
                + "       SELECT\n"
                + "         count(*) count,\n"
                + "         m.BRANCH_ID\n"
                + "       FROM MEMBER m\n"
                + "       WHERE m.JOIN_DATE < trunc(sysdate - 10)\n"
                + "       GROUP BY m.BRANCH_ID\n"
                + "     ) a\n"
                + "  RIGHT JOIN CAT_BRANCH c ON a.BRANCH_ID = c.BRANCH_ID\n"
                + "ORDER BY c.BRANCH_ID";
        list = new MemberService().findListSQLAll(sqlMemOld);
        memberOlds.setLabel("Số hội viên cũ");
        for (Object o : list)
        {
            Object[] obj = (Object[]) o;
            Long nCount = Long.valueOf(obj[0].toString());
            memberOlds.set(obj[2], nCount);
            if (nCount > max)
            {
                max = nCount;
            }
            if (nCount < min)
            {
                min = nCount;
            }
        }
        numOfMemModel.addSeries(memberNews);
        numOfMemModel.addSeries(memberOlds);

        numOfMemModel.setTitle("Số lượng hội viên theo từng địa điểm");
        numOfMemModel.setLegendPosition("ne");

        Axis xAxis = numOfMemModel.getAxis(AxisType.X);
        xAxis.setLabel("Site");

        Axis yAxis = numOfMemModel.getAxis(AxisType.Y);
        yAxis.setLabel("Số lượng");
        yAxis.setMin(Math.max(0, min - 1));
        yAxis.setMax(max + 1);

        List<Series> series = new ArrayList<Series>();
        List<List<Object>> memberNewss = new ArrayList<>();
        List<List<Object>> memberOldss = new ArrayList<>();
        List<String> categoryList = new ArrayList<String>();

        for (Object o : memberNews.getData().keySet())
        {
            List<Object> tmps = new ArrayList<>();
            tmps.add(o);
            categoryList.add(o.toString());
            tmps.add(memberNews.getData().get(o));
            memberNewss.add(tmps);
        }
        for (Object o : memberOlds.getData().keySet())
        {
            List<Object> tmps = new ArrayList<>();
            tmps.add(o);
            tmps.add(memberOlds.getData().get(o));
            memberOldss.add(tmps);
        }
        series.add(new Series(memberNews.getLabel(), memberNewss));
        series.add(new Series(memberOlds.getLabel(), memberOldss));
        String chartData = (new Gson().toJson(series));
        String categories = (new Gson().toJson(categoryList));
        //System.out.println(categories);

        RequestContext reqCtx = RequestContext.getCurrentInstance();
        reqCtx.addCallbackParam("categories", categories);
        reqCtx.addCallbackParam("chartData", chartData);
        reqCtx.addCallbackParam("chartId", "container");
        reqCtx.addCallbackParam("chartType", "column");
        reqCtx.addCallbackParam("chartTitle", "Biểu đồ số lượng hội viên");
        reqCtx.addCallbackParam("chartSubTitle", "Số liệu theo địa điểm");
        reqCtx.addCallbackParam("yAxisTitle", "Số hội viên");
        reqCtx.addCallbackParam("xAxisTitle", "Địa điểm");
        //linear, logarithmic, datetime or category.
        reqCtx.addCallbackParam("xAxisType", "category");
    }

    /**
     * " Đường biểu đồ số lượng hội viên hoạt động trong 2 tháng( kể từ ngày 1
     * của tháng trước đó) Tên biểu số: Biểu đồ xu thế hội viên trong 2 tháng
     * Đường 1: Biểu đồ xu thế hội viên của toàn công ty Đường còn lại: Biểu đồ
     * xu thế hội viên của từng địa điểm"
     */
    public void loadDashboardMemActive()
    {
        String sqlCountMemActive = "SELECT nvl(cc.count,0), c.BRANCH_ID,c.BRANCH_NAME,cc.CHECK_IN_DATE from CAT_BRANCH c LEFT JOIN (\n"
                + "  SELECT\n"
                + "    count(*) count,\n"
                + "    BRANCH_ID,\n"
                + "    BRANCH_NAME,\n"
                + "    CHECK_IN_DATE\n"
                + "  FROM (\n"
                + "    SELECT\n"
                + "      count(*)               count,\n"
                + "      m.MEMBER_ID,\n"
                + "      m.BRANCH_ID,\n"
                + "      b.BRANCH_NAME,\n"
                + "      trunc(c.CHECK_IN_DATE) CHECK_IN_DATE\n"
                + "    FROM MEMBER m\n"
                + "      JOIN MEMBER_CHECKIN c ON m.MEMBER_ID = c.MEMBER_ID\n"
                + "      JOIN CAT_BRANCH b ON m.BRANCH_ID = b.BRANCH_ID\n"
                + "    WHERE m.STATUS != -1 AND c.CHECK_IN_DATE BETWEEN ? AND ?\n"
                + "    GROUP BY m.MEMBER_ID, m.BRANCH_ID, b.BRANCH_NAME, trunc(c.CHECK_IN_DATE)\n"
                + "  )\n"
                + "  WHERE count > 0\n"
                + "  GROUP BY BRANCH_ID, BRANCH_NAME, CHECK_IN_DATE\n"
                + "  ) cc on c.BRANCH_ID = cc.BRANCH_ID\n"
                + "ORDER BY c.BRANCH_ID, cc.CHECK_IN_DATE";
        Calendar calendar = Calendar.getInstance();
        Date dateTo = calendar.getTime();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date dateFrom = calendar.getTime();
//        System.out.println(dateFrom);
//        System.out.println(dateTo);
        List<?> list = new MemberService().findListSQLAll(sqlCountMemActive, dateFrom, dateTo);
        List<ChartSeries> chartSeries = new ArrayList<>();
        Map<String, Map<Date, Number>> mapBranch = new HashMap<>();
        for (Object o : list)
        {
            Object[] obj = (Object[]) o;
            if (mapBranch.get(obj[2].toString()) == null)
            {
                Map<Date, Number> dateNumberMap = new HashMap<>();
                dateNumberMap.put((Date) obj[3], (Number) obj[0]);
                mapBranch.put(obj[2].toString(), dateNumberMap);
            }
            else
            {
                Map<Date, Number> dateNumberMap = mapBranch.get(obj[2]);
                dateNumberMap.put((Date) obj[3], (Number) obj[0]);
            }

        }
        List<String> categoryList = new ArrayList<>();
        for (String s : mapBranch.keySet())
        {
            ChartSeries chart = new ChartSeries();
            chart.setLabel(s);
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(dateFrom);

            while (calendar1.getTime().before(dateTo))
            {

                if (mapBranch.get(s).get(calendar1.getTime()) != null)
                {
                    chart.set(calendar1.getTime(), mapBranch.get(s).get(calendar1.getTime()));
                }
                else
                {
                    chart.set(calendar1.getTime(), 0);
                }
                calendar1.add(Calendar.DAY_OF_YEAR, 1);
            }
            chartSeries.add(chart);
        }
//        System.out.println(chartSeries.size());
        List<Series> series = new ArrayList<>();
        for (ChartSeries chartSery : chartSeries)
        {
            List<List<Object>> memberNewss = new ArrayList<>();
            for (Object o : chartSery.getData().keySet())
            {
                List<Object> tmps = new ArrayList<>();
                tmps.add(((Date) o).getTime());

                tmps.add(chartSery.getData().get(o));
                memberNewss.add(tmps);
            }
            series.add(new Series(chartSery.getLabel(), memberNewss));
        }
        String chartData = (new Gson().toJson(series));
        String categories = (new Gson().toJson(categoryList));
//        System.out.println(categories);

        RequestContext reqCtx = RequestContext.getCurrentInstance();
        reqCtx.addCallbackParam("categories", categories);
        reqCtx.addCallbackParam("chartData", chartData);
        reqCtx.addCallbackParam("chartId", "container2");
        reqCtx.addCallbackParam("chartType", "StackedArea");
        reqCtx.addCallbackParam("chartTitle", "Biểu đồ số lượng hội viên hoạt động");
        reqCtx.addCallbackParam("chartSubTitle", "Số liệu trong 2 tháng gần đây");
        reqCtx.addCallbackParam("yAxisTitle", "Số hội viên");
        reqCtx.addCallbackParam("xAxisTitle", "Địa điểm");
        //linear, logarithmic, datetime or category.
        reqCtx.addCallbackParam("xAxisType", "datetime");
    }

    /**
     * Đường biểu đồ số lượng hội viên mới đăng ký trong 30 ngày gần đây
     */
    public void loadDashboardNewMember()
    {
        String sqlCountNewMem = "SELECT nvl(cc.count,0), c.BRANCH_ID,c.BRANCH_NAME,cc.JOIN_DATE from CAT_BRANCH c LEFT JOIN (\n"
                + "  SELECT\n"
                + "    count(*)           count,\n"
                + "    m.BRANCH_ID,\n"
                + "    b.BRANCH_NAME,\n"
                + "    trunc(m.JOIN_DATE) JOIN_DATE\n"
                + "  FROM MEMBER m\n"
                + "    JOIN CAT_BRANCH b ON m.BRANCH_ID = b.BRANCH_ID\n"
                + "  WHERE m.JOIN_DATE BETWEEN ? AND ?\n"
                + "  GROUP BY m.BRANCH_ID, b.BRANCH_NAME, trunc(m.JOIN_DATE)\n"
                + "  )cc ON c.BRANCH_ID = cc.BRANCH_ID\n"
                + "ORDER BY c.BRANCH_ID, cc.JOIN_DATE";
        Calendar calendar = Calendar.getInstance();
        Date dateTo = calendar.getTime();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.DAY_OF_YEAR, -30);
        Date dateFrom = calendar.getTime();
        List<?> list = new MemberService().findListSQLAll(sqlCountNewMem, dateFrom, dateTo);
        List<ChartSeries> chartSeries = new ArrayList<>();
        Map<String, Map<Date, Number>> mapBranch = new HashMap<>();
        for (Object o : list)
        {
            Object[] obj = (Object[]) o;
            if (mapBranch.get(obj[2].toString()) == null)
            {
                Map<Date, Number> dateNumberMap = new HashMap<>();
                dateNumberMap.put((Date) obj[3], (Number) obj[0]);
                mapBranch.put(obj[2].toString(), dateNumberMap);
            }
            else
            {
                Map<Date, Number> dateNumberMap = mapBranch.get(obj[2]);
                dateNumberMap.put((Date) obj[3], (Number) obj[0]);
            }

        }
        List<String> categoryList = new ArrayList<String>();
        for (String s : mapBranch.keySet())
        {
            ChartSeries chart = new ChartSeries();
            chart.setLabel(s);
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(dateFrom);

            while (calendar1.getTime().before(dateTo))
            {

                if (mapBranch.get(s).get(calendar1.getTime()) != null)
                {
                    chart.set(calendar1.getTime(), mapBranch.get(s).get(calendar1.getTime()));
                }
                else
                {
                    chart.set(calendar1.getTime(), 0);
                }
                calendar1.add(Calendar.DAY_OF_YEAR, 1);
            }
            chartSeries.add(chart);
        }
//        System.out.println(chartSeries.size());
        List<Series> series = new ArrayList<>();
        for (ChartSeries chartSery : chartSeries)
        {
            List<List<Object>> memberNewss = new ArrayList<>();
            for (Object o : chartSery.getData().keySet())
            {
                List<Object> tmps = new ArrayList<>();
                tmps.add(((Date) o).getTime());

                tmps.add(chartSery.getData().get(o));
                memberNewss.add(tmps);
            }
            series.add(new Series(chartSery.getLabel(), memberNewss));
        }
        String chartData = (new Gson().toJson(series));
        String categories = (new Gson().toJson(categoryList));
//        System.out.println(categories);

        RequestContext reqCtx = RequestContext.getCurrentInstance();
        reqCtx.addCallbackParam("categories", categories);
        reqCtx.addCallbackParam("chartData", chartData);
        reqCtx.addCallbackParam("chartId", "container3");
        reqCtx.addCallbackParam("chartType", "StackedArea");
        reqCtx.addCallbackParam("chartTitle", "Biểu đồ số lượng hội viên mới đăng ký");
        reqCtx.addCallbackParam("chartSubTitle", "Số liệu trong 30 ngày gần đây");
        reqCtx.addCallbackParam("yAxisTitle", "Số hội viên");
        reqCtx.addCallbackParam("xAxisTitle", "Địa điểm");
        //linear, logarithmic, datetime or category.
        reqCtx.addCallbackParam("xAxisType", "datetime");
    }

    public void loadDashboardMemberFinish()
    {
        String sqlCountNewMem = "SELECT nvl(cc.count,0), c.BRANCH_ID,c.BRANCH_NAME,cc.END_DATE from CAT_BRANCH c LEFT JOIN (\n"
                + "  SELECT\n"
                + "    count(*)           count,\n"
                + "    m.BRANCH_ID,\n"
                + "    b.BRANCH_NAME,\n"
                + "    trunc(m.END_DATE) END_DATE\n"
                + "  FROM MEMBER m\n"
                + "    JOIN CAT_BRANCH b ON m.BRANCH_ID = b.BRANCH_ID\n"
                + "  WHERE m.END_DATE BETWEEN ? AND ?\n"
                + "  GROUP BY m.BRANCH_ID, b.BRANCH_NAME, trunc(m.END_DATE)\n"
                + "  )cc ON c.BRANCH_ID = cc.BRANCH_ID\n"
                + "ORDER BY c.BRANCH_ID, cc.END_DATE";
        Calendar calendar = Calendar.getInstance();
        Date dateTo = calendar.getTime();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.DAY_OF_YEAR, -30);
        Date dateFrom = calendar.getTime();
        List<?> list = new MemberService().findListSQLAll(sqlCountNewMem, dateFrom, dateTo);
        List<ChartSeries> chartSeries = new ArrayList<>();
        Map<String, Map<Date, Number>> mapBranch = new HashMap<>();
        for (Object o : list)
        {
            Object[] obj = (Object[]) o;
            if (mapBranch.get(obj[2].toString()) == null)
            {
                Map<Date, Number> dateNumberMap = new HashMap<>();
                dateNumberMap.put((Date) obj[3], (Number) obj[0]);
                mapBranch.put(obj[2].toString(), dateNumberMap);
            }
            else
            {
                Map<Date, Number> dateNumberMap = mapBranch.get(obj[2]);
                dateNumberMap.put((Date) obj[3], (Number) obj[0]);
            }

        }
        List<String> categoryList = new ArrayList<String>();
        for (String s : mapBranch.keySet())
        {
            ChartSeries chart = new ChartSeries();
            chart.setLabel(s);
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(dateFrom);

            while (calendar1.getTime().before(dateTo))
            {

                if (mapBranch.get(s).get(calendar1.getTime()) != null)
                {
                    chart.set(calendar1.getTime(), mapBranch.get(s).get(calendar1.getTime()));
                }
                else
                {
                    chart.set(calendar1.getTime(), 0);
                }
                calendar1.add(Calendar.DAY_OF_YEAR, 1);
            }
            chartSeries.add(chart);
        }
//        System.out.println(chartSeries.size());
        List<Series> series = new ArrayList<>();
        for (ChartSeries chartSery : chartSeries)
        {
            List<List<Object>> memberNewss = new ArrayList<>();
            for (Object o : chartSery.getData().keySet())
            {
                List<Object> tmps = new ArrayList<>();
                tmps.add(((Date) o).getTime());

                tmps.add(chartSery.getData().get(o));
                memberNewss.add(tmps);
            }
            series.add(new Series(chartSery.getLabel(), memberNewss));
        }
        String chartData = (new Gson().toJson(series));
        String categories = (new Gson().toJson(categoryList));
//        System.out.println(categories);

        RequestContext reqCtx = RequestContext.getCurrentInstance();
        reqCtx.addCallbackParam("categories", categories);
        reqCtx.addCallbackParam("chartData", chartData);
        reqCtx.addCallbackParam("chartId", "container4");
        reqCtx.addCallbackParam("chartType", "StackedArea");
        reqCtx.addCallbackParam("chartTitle", "Biểu đồ số lượng hội viên hết hạn");
        reqCtx.addCallbackParam("chartSubTitle", "Số liệu trong 30 ngày gần đây");
        reqCtx.addCallbackParam("yAxisTitle", "Số hội viên");
        reqCtx.addCallbackParam("xAxisTitle", "Địa điểm");
        //linear, logarithmic, datetime or category.
        reqCtx.addCallbackParam("xAxisType", "datetime");
    }

    /**
     * Biều đồ lịch sử hoạt động của hội viên
     *
     * @param memberId
     */
    public void loadChartLineDataMemberCheckin(Long memberId)
    {

        String sqlCountMemActive = "select member_id,trunc(CHECK_IN_DATE) CHECK_IN_DATE, 1 as value from MEMBER_CHECKIN\n";
        sqlCountMemActive += "where CHECK_IN_DATE BETWEEN ? AND ?\n";
        if (memberId != null)
        {
            sqlCountMemActive += "and member_id=?\n";
        }
        sqlCountMemActive += "group by member_id, trunc(CHECK_IN_DATE)\n";
        sqlCountMemActive += "order by member_id,trunc(CHECK_IN_DATE)";
        Calendar calendar = Calendar.getInstance();
        Date dateTo = calendar.getTime();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date dateFrom = calendar.getTime();
//        System.out.println(dateFrom);
//        System.out.println(dateTo);
        List list;
        if (memberId != null)
        {
            list = new MemberService().findListSQLAll(sqlCountMemActive, dateFrom, dateTo, memberId);
        }
        else
        {
            list = new MemberService().findListSQLAll(sqlCountMemActive, dateFrom, dateTo);
        }
        List<ChartSeries> chartSeries = new ArrayList<>();
        Map<String, Map<Date, Number>> mapMemberCheckin = new HashMap<>();
        for (Object o : list)
        {
            Object[] obj = (Object[]) o;
            if (mapMemberCheckin.get(obj[0].toString()) == null)
            {
                Map<Date, Number> dateNumberMap = new HashMap<>();
                dateNumberMap.put((Date) obj[1], (Number) obj[2]);
                mapMemberCheckin.put(obj[0].toString(), dateNumberMap);
            }
            else
            {
                Map<Date, Number> dateNumberMap = mapMemberCheckin.get(obj[0].toString());
                dateNumberMap.put((Date) obj[1], (Number) obj[2]);
            }

        }
        List<String> categoryList = new ArrayList<>();
        for (String s : mapMemberCheckin.keySet())
        {
            ChartSeries chart = new ChartSeries();
//            chart.setLabel(s);
            chart.setLabel("Mã hội viên: " + s);
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(dateFrom);

            while (calendar1.getTime().before(dateTo))
            {

                if (mapMemberCheckin.get(s).get(calendar1.getTime()) != null)
                {
                    chart.set(calendar1.getTime(), mapMemberCheckin.get(s).get(calendar1.getTime()));
                }
                else
                {
                    chart.set(calendar1.getTime(), 0);
                }
                calendar1.add(Calendar.DAY_OF_YEAR, 1);
            }
            chartSeries.add(chart);
        }
//        System.out.println(chartSeries.size());
        List<Series> series = new ArrayList<>();
        for (ChartSeries chartSery : chartSeries)
        {
            List<List<Object>> memberNewss = new ArrayList<>();
            for (Object o : chartSery.getData().keySet())
            {
                List<Object> tmps = new ArrayList<>();
                tmps.add(((Date) o).getTime());

                tmps.add(chartSery.getData().get(o));
                memberNewss.add(tmps);
            }
            series.add(new Series(chartSery.getLabel(), memberNewss));
        }
        String chartData = (new Gson().toJson(series));
        String categories = (new Gson().toJson(categoryList));
//        System.out.println(categories);

        RequestContext reqCtx = RequestContext.getCurrentInstance();
        reqCtx.addCallbackParam("categories", categories);
        reqCtx.addCallbackParam("chartData", chartData);
        reqCtx.addCallbackParam("chartId", "containerMemberCheckin");
        reqCtx.addCallbackParam("chartType", "StackedArea");
        reqCtx.addCallbackParam("chartTitle", "Biểu đồ hoạt động của hội viên");
        reqCtx.addCallbackParam("chartSubTitle", "Số liệu trong 1 tháng gần đây");
        reqCtx.addCallbackParam("yAxisTitle", "Hoạt động");
        reqCtx.addCallbackParam("xAxisTitle", "");
        //linear, logarithmic, datetime or category.
        reqCtx.addCallbackParam("xAxisType", "datetime");
    }

    public static void main(String[] args)
    {
        new DashBoardController().loadDashboardMemActive();
    }

}
