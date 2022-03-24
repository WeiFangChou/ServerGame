package com.lineage.server.templates;

public class L1BookConfig {
    private byte[] _data = null;
    private int _objid = 0;

    public int getObjid() {
        return this._objid;
    }

    public void setObjid(int objid) {
        this._objid = objid;
    }

    public byte[] getData() {
        return this._data;
    }

    public void setData(byte[] data) {
        this._data = data;
    }
}
