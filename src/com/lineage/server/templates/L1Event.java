package com.lineage.server.templates;

public class L1Event {
    private String _eventclass;
    private int _eventid;
    private String _eventname;
    private String _eventother;
    private boolean _eventstart;

    public int get_eventid() {
        return this._eventid;
    }

    public void set_eventid(int eventid) {
        this._eventid = eventid;
    }

    public String get_eventname() {
        return this._eventname;
    }

    public void set_eventname(String eventname) {
        this._eventname = eventname;
    }

    public String get_eventclass() {
        return this._eventclass;
    }

    public void set_eventclass(String eventclass) {
        this._eventclass = eventclass;
    }

    public boolean is_eventstart() {
        return this._eventstart;
    }

    public void set_eventstart(boolean eventstart) {
        this._eventstart = eventstart;
    }

    public String get_eventother() {
        return this._eventother;
    }

    public void set_eventother(String eventother) {
        this._eventother = eventother;
    }
}
