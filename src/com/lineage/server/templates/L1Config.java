package com.lineage.server.templates;

public class L1Config {
    private byte[] data = null;
    private int length = 0;
    private int objid = 0;

    public int getObjid() {
        return this.objid;
    }

    public void setObjid(int objid2) {
        this.objid = objid2;
    }

    public int getLength() {
        return this.length;
    }

    public void setLength(int length2) {
        this.length = length2;
    }

    public byte[] getData() {
        return this.data;
    }

    public void setData(byte[] data2) {
        this.data = data2;
    }
}
