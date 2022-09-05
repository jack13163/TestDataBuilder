package com.testDataBuilder.ui.util;
/**
 * 列的信息.
 * <p>Title：ColumnTable.java</p>
 * <p>Description：TestDataBuilder</p>
 * <p>Copyright：Copyright (c)2007 TestDataBuilder,Inc</p>
 * <p>Company：TestDataBuilder,Inc</p> 
 * @author LiuXiaojie 2007-8-4
 * @version 1.0
 */
public class ColumnInfo{
    
    private String name;
    
    private int width;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public ColumnInfo(String name, int width) {
        super();
        this.name = name;
        this.width = width;
    }   
}