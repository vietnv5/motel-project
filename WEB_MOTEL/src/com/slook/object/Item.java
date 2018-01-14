package com.slook.object;

/**
 * Created by dungvv8 on 12/28/2016.
 */
public class Item {
    private String label;
    private Object value;

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
