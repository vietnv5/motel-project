package com.slook.object;

import java.util.List;

/**
 *
 * @author xuanhuy
 */
public class Series {
    private String name;
//    private String type="area";

    private List<List<Object>> data;

    public Series() {}

    public Series(String name, List<List<Object>> data) {
        this.name = name;
        this.data = data;
    }
}
