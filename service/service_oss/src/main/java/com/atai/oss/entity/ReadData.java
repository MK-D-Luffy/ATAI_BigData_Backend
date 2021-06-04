package com.atai.oss.entity;

import com.alibaba.excel.annotation.ExcelProperty;


public class ReadData {
    @ExcelProperty(index = 0)
    private int sid;
    //设置列对应的属性
    @ExcelProperty(index = 1)
    private String sname;
    public int getSid() {
        return sid;
    }
    public void setSid(int sid) {
        this.sid = sid;
    }
    public String getSname() {
        return sname;
    }
    public void setSname(String sname) {
        this.sname = sname;
    }
    @Override
    public String toString() {
        return "ReadData{" +
                "sid=" + sid +
                ", sname='" + sname + '\'' +
                '}';
    }
}