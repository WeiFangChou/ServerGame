package com.lineage.server.templates;

public class L1EmblemIcon {
    private byte[] _clanIcon;
    private int _clanid;
    private int _update;

    public int get_clanid() {
        return this._clanid;
    }

    public void set_clanid(int clanid) {
        this._clanid = clanid;
    }

    public byte[] get_clanIcon() {
        return this._clanIcon;
    }

    public void set_clanIcon(byte[] icon) {
        this._clanIcon = icon;
    }

    public int get_update() {
        return this._update;
    }

    public void set_update(int update) {
        this._update = update;
    }
}
