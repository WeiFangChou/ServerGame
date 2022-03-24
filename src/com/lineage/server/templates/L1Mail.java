package com.lineage.server.templates;

public class L1Mail {
    private byte[] _content = null;
    private String _date = null;
    private int _id;
    private int _readStatus = 0;
    private String _receiverName;
    private String _senderName;
    private byte[] _subject = null;
    private int _type;

    public int getId() {
        return this._id;
    }

    public void setId(int i) {
        this._id = i;
    }

    public int getType() {
        return this._type;
    }

    public void setType(int i) {
        this._type = i;
    }

    public String getSenderName() {
        return this._senderName;
    }

    public void setSenderName(String s) {
        this._senderName = s;
    }

    public String getReceiverName() {
        return this._receiverName;
    }

    public void setReceiverName(String s) {
        this._receiverName = s;
    }

    public String getDate() {
        return this._date;
    }

    public void setDate(String s) {
        this._date = s;
    }

    public int getReadStatus() {
        return this._readStatus;
    }

    public void setReadStatus(int i) {
        this._readStatus = i;
    }

    public byte[] getSubject() {
        return this._subject;
    }

    public void setSubject(byte[] arg) {
        byte[] newarg = new byte[(arg.length - 2)];
        System.arraycopy(arg, 0, newarg, 0, newarg.length);
        this._subject = newarg;
    }

    public byte[] getContent() {
        return this._content;
    }

    public void setContent(byte[] arg) {
        this._content = arg;
    }
}
