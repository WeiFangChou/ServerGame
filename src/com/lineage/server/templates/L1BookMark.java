package com.lineage.server.templates;

public class L1BookMark {
    private int _charId;
    private int _id;
    private int _locX;
    private int _locY;
    private int _mapId;
    private String _name;

    public int getId() {
        return this._id;
    }

    public void setId(int i) {
        this._id = i;
    }

    public int getCharId() {
        return this._charId;
    }

    public void setCharId(int i) {
        this._charId = i;
    }

    public String getName() {
        return this._name;
    }

    public void setName(String s) {
        this._name = s;
    }

    public int getLocX() {
        return this._locX;
    }

    public void setLocX(int i) {
        this._locX = i;
    }

    public int getLocY() {
        return this._locY;
    }

    public void setLocY(int i) {
        this._locY = i;
    }

    public int getMapId() {
        return this._mapId;
    }

    public void setMapId(int i) {
        this._mapId = i;
    }
}
