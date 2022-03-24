package com.lineage.server.templates;

public class L1Day_Signature {
    private int _day;
    private int _id;
    private String _item;
    private String _itemcount;
    private String _itemenchant;
    private int _makeup;
    private int _makeupcount;
    private String _msg;

    public int getId() {
        return this._id;
    }

    public int getDay() {
        return this._day;
    }

    public void setDay(int day) {
        this._day = day;
    }

    public String getMsg() {
        return this._msg;
    }

    public void setMsg(String s) {
        this._msg = s;
    }

    public String getItem() {
        return this._item;
    }

    public void setItem(String s) {
        this._item = s;
    }

    public String getEnchant() {
        return this._itemenchant;
    }

    public void setEnchant(String s) {
        this._itemenchant = s;
    }

    public String getCount() {
        return this._itemcount;
    }

    public void setCount(String s) {
        this._itemcount = s;
    }

    public int getMakeUp() {
        return this._makeup;
    }

    public void setMakeUp(int i) {
        this._makeup = i;
    }

    public int getMakeUpC() {
        return this._makeupcount;
    }

    public void setMakeUpC(int i) {
        this._makeupcount = i;
    }
}
