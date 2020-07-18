package com.e.soilab.model;

public class AvaliableModel
{
    String txt_start,txt_End,txt_Bar;

    public AvaliableModel(String txt_start,  String txt_Bar) {
        this.txt_start = txt_start;
        this.txt_Bar = txt_Bar;
    }

    public String getTxt_start() {
        return txt_start;
    }

    public String getTxt_Bar() {
        return txt_Bar;
    }

    public void setTxt_start(String txt_start) {
        this.txt_start = txt_start;
    }
    public void setTxt_Bar(String txt_Bar) {
        this.txt_Bar = txt_Bar;
    }
}
