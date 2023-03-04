/*
 * Created on Sep 11, 2013
 *
 * Copyright (C) 2013 by Viettel Network Company. All rights reserved
 */
package com.slook.lazy;

import com.slook.persistence.GenericDaoServiceNewV2;
import org.apache.log4j.Logger;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;

import java.io.Serializable;
import java.util.*;

import static org.apache.log4j.Logger.getLogger;

/**
 * @param <T>
 * @param <PK>
 * @author Nguyễn Xuân Huy <huynx6@viettel.com.vn>
 * @version 1.0
 * @sin Jul 29, 2016
 */
public class LazyDataModelBase<T, PK extends Serializable> extends LazyDataModel<T>
{

    private static final Logger logger = getLogger(LazyDataModelBase.class);

    private static final long serialVersionUID = -8213459208378430543L;
    protected GenericDaoServiceNewV2<T, PK> daoService;
    protected Map<String, Object> filters;
    private LinkedHashMap<String, String> orders;
    private Map<String, Object> currFilters;
    private Map<String, Object> initFilters;

    protected Map<String, Object> sqlRes;

    public LazyDataModelBase(GenericDaoServiceNewV2<T, PK> daoService)
    {
        this.daoService = daoService;
    }

    public LazyDataModelBase(GenericDaoServiceNewV2<T, PK> daoService, Map<String, Object> filters, LinkedHashMap<String, String> orders)
    {

        this.daoService = daoService;
        initFilterOrder(filters, orders);
    }

    public LazyDataModelBase(GenericDaoServiceNewV2<T, PK> daoService, Object... filtersOrOrders)
    {

        if (daoService != null)
        {
            this.daoService = daoService;
        }
        initFilterOrder(filtersOrOrders);
    }

    @SuppressWarnings("unchecked")
    private void initFilterOrder(Object... filtersOrOrders)
    {
        // TODO Auto-generated method stub
        if (filtersOrOrders != null)
        {
            switch (filtersOrOrders.length)
            {
                case 1:
                    if (filtersOrOrders[0] instanceof Map<?, ?>)
                    {
                        filters = (Map<String, Object>) filtersOrOrders[0];
                    }
                    break;
                case 2:
                    if (filtersOrOrders[0] != null && filtersOrOrders[0] instanceof Map<?, ?>)
                    {
                        filters = (Map<String, Object>) filtersOrOrders[0];
                    }
                    if (filtersOrOrders[1] != null && filtersOrOrders[1] instanceof Map<?, ?>)
                    {
                        orders = (LinkedHashMap<String, String>) filtersOrOrders[1];
                    }
                    break;
                case 3:
                    if (filtersOrOrders[0] != null && filtersOrOrders[0] instanceof Map<?, ?>)
                    {
                        filters = (Map<String, Object>) filtersOrOrders[0];
                    }
                    if (filtersOrOrders[1] != null && filtersOrOrders[1] instanceof Map<?, ?>)
                    {
                        sqlRes = (Map<String, Object>) filtersOrOrders[1];
                    }
                    if (filtersOrOrders[2] != null && filtersOrOrders[2] instanceof Map<?, ?>)
                    {
                        orders = (LinkedHashMap<String, String>) filtersOrOrders[2];
                    }
                    break;
                default:
                    //No sort or filter
                    break;

            }
        }
    }

    @Override
    public List<T> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters)
    {
//        try {
        // TODO Auto-generated method stub
        List<SortMeta> multiSortMeta = new ArrayList<SortMeta>();
        if (sortField != null)
        {
            multiSortMeta.add(new SortMeta(null, sortField, sortOrder, null));
        }
        return load(first, pageSize, multiSortMeta, filters);
//        } catch (Exception ex) {
//            logger.error(ex.getMessage(), ex);
//        }
//        return null;
    }

    //dungvv8
    boolean firstLoad = true;

    @SuppressWarnings("unchecked")
    @Override
    public List<T> load(int first, int pageSize, List<SortMeta> multiSortMeta, Map<String, Object> filters)
    {

        if (filters == null)
        {
            filters = new HashMap<String, Object>();
        }
        List<T> data = new ArrayList<T>();
        int dataSize = 0;
        try
        {
            if (initFilters != null)
            {
                filters.putAll(initFilters);
                initFilters.clear();
            }

            //add filter mac dinh this.filters
            if (this.filters != null)
            {
//				filters.putAll(this.filters);
                //dungvv8
                for (Map.Entry<String, Object> entry : this.filters.entrySet())
                {
                    String field = entry.getKey();
                    //Lan dau load se lay tat ca filter ke ca filter key default
                    if (firstLoad)
                    {
                        filters.put(field, entry.getValue());
                    }
                    else
                    {
                        //Lan load sau hoac sang trang se khong lay filter co key default
                        if (!field.contains("DEFAULT") || first > 0)
                        {
                            filters.put(field, entry.getValue());
                        }
                    }
                }
                firstLoad = false;
                //dungvv8
            }
            //dungvv8
            //trim space filter
            for (Map.Entry<String, Object> entry : filters.entrySet())
            {
                if (entry.getValue() != null && entry.getValue() instanceof String)
                {
                    filters.put(entry.getKey(), String.valueOf(entry.getValue()).trim());
                }
            }
            //add sort mac dinh
            LinkedHashMap<String, String> sorter = null;
            if (this.orders != null)
            {
                sorter = new LinkedHashMap<String, String>();
                for (Iterator<String> iterator = this.orders.keySet().iterator(); iterator.hasNext(); )
                {
                    String field = iterator.next();
                    String value = this.orders.get(field);
                    sorter.put(field, value);
                }
            }
            if (multiSortMeta != null)
            {
                for (SortMeta sortMeta : multiSortMeta)
                {
                    String sortField = sortMeta.getSortField();
                    SortOrder sortOrder = sortMeta.getSortOrder();
                    if (sortField != null)
                    {
                        if (sorter == null)
                        {
                            sorter = new LinkedHashMap<String, String>();
                        }
                        switch (sortOrder)
                        {
                            case ASCENDING:
                                sorter.put(sortField, "ASC");
                                break;
                            case DESCENDING:
                                sorter.put(sortField, "DESC");
                                break;
                            case UNSORTED:
                            default:
                                sorter = null;
                                break;
                        }
                    }
                }
            }

            if (this.filters != null && this.filters.get("OR_1") != null)
            {
                Set<T> data2 = new HashSet<>();
                Set<T> data3 = new HashSet<>();
                for (Iterator<String> iterator = this.filters.keySet().iterator(); iterator.hasNext(); )
                {
                    Map<String, Object> filterOr = new HashMap<String, Object>(filters);
                    String field = iterator.next();
                    Map<String, String> value = (Map<String, String>) this.filters.get(field);
                    filterOr.putAll(value);
                    data2.addAll(daoService.findList(first, pageSize, filterOr, sorter));
                    data3.addAll(daoService.findList(-1, -1, filterOr, null));
                }
                data = new ArrayList<>(data2);
                dataSize = data3.size();
            }
            else
            {
                this.currFilters = filters;
                if (filters.get("search") != null)
                {

                }
                else
                {
                    if (this.sqlRes == null || this.sqlRes.isEmpty())
                    {

                        data = daoService.findList(first, pageSize, filters, sorter);
                        dataSize = daoService.count2(filters);
                    }
                    else
                    {
                        data = daoService.findList(first, pageSize, filters, sqlRes, sorter);
                        dataSize = daoService.count2(filters, sqlRes);

                    }
                }
            }
        }
        catch (Exception ex)
        {
            logger.error(ex.getMessage(), ex);
        }

        this.setRowCount(dataSize);
        return data;
    }

    @Override
    public T getRowData(String rowKey)
    {
        T object = null;
        try
        {
            object = daoService.findById((PK) rowKey);
        }
        catch (Exception ex)
        {
            logger.error(ex.getMessage(), ex);
        }

        return (T) object;
    }

    public Map<String, Object> getFilters()
    {
        return filters;
    }

    public void setFilters(Map<String, Object> filters)
    {
        this.filters = filters;
    }

    public static void main(String[] args)
    {
        //		filter.put("deptReportId", "6892");
//		filtersOrOrders.put("OR_1", filter );
//		filter = new HashMap<>();
//		filter.put("id-EXAC", "1");
//		filtersOrOrders.put("OR_2", filter);
//		LazyDataModel<Dispatch> test = new LazyDataModelBase<>(DAO.dispatchService());
//		filter.put("DispatchHasDepts_FILTER", "Phòng");
//		List<Dispatch> objs = test.load(0, 25, null, null, filter);
//		System.err.println(objs.size());
        //LazyDataModel<TaskForLazy> test = new LazyDataModelBase<>(DAO.taskForLazyService());
        //filter.put("TaskHasDepts_FILTER", "Phòng");
//		List<TaskForLazy> objs = test.load(0, 25, null, null, filter);
        //System.err.println(objs.size());

    }

    public Map<String, Object> getCurrFilters()
    {
        return currFilters;
    }

    public void setCurrFilters(Map<String, Object> currFilters)
    {
        this.currFilters = currFilters;
    }

    public Map<String, Object> getInitFilters()
    {
        return initFilters;
    }

    public void setInitFilters(Map<String, Object> initFilters)
    {
        this.initFilters = initFilters;
    }

    //        vietnv14 add start
    public LazyDataModelBase(GenericDaoServiceNewV2<T, PK> daoService, Map<String, Object> filters, Map<String, Object> sqlRes, LinkedHashMap<String, String> orders)
    {

        this.daoService = daoService;
        initFilterOrder(filters, sqlRes, orders);
    }
//        vietnv14 add end
}
