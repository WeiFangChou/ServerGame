package com.lineage.server.templates;

import com.lineage.list.OnlineUser;

public class L1Bank {
    private String _account_name = null;
    private long _adena_count = 0;
    private String _pass = null;

    public String get_account_name() {
        return this._account_name;
    }

    public void set_account_name(String _account_name2) {
        this._account_name = _account_name2;
    }

    public long get_adena_count() {
        return this._adena_count;
    }

    public void set_adena_count(long _adena_count2) {
        this._adena_count = _adena_count2;
    }

    public String get_pass() {
        return this._pass;
    }

    public void set_pass(String _pass2) {
        this._pass = _pass2;
    }

    public boolean isLan() {
        return OnlineUser.get().isLan(this._account_name);
    }

    public boolean isEmpty() {
        return this._adena_count <= 0;
    }
}
