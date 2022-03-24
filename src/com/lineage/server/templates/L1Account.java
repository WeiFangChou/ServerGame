package com.lineage.server.templates;

import java.sql.Timestamp;

public class L1Account {
    private int _access_level;
    private int _character_slot;
    private int _countCharacters;
    private String _ip;
    private boolean _isLoad;
    private Timestamp _lastactive;
    private String _login;
    private String _mac;
    private String _password;
    private int _pay_first;
    private int _points;
    private int _server_no;
    private String _spw;
    private int _warehouse = -256;

    public String get_login() {
        return this._login;
    }

    public void set_login(String login) {
        this._login = login;
    }

    public String get_password() {
        return this._password;
    }

    public void set_password(String password) {
        this._password = password;
    }

    public Timestamp get_lastactive() {
        return this._lastactive;
    }

    public void set_lastactive(Timestamp lastactive) {
        this._lastactive = lastactive;
    }

    public int get_access_level() {
        return this._access_level;
    }

    public void set_access_level(int access_level) {
        this._access_level = access_level;
    }

    public String get_ip() {
        return this._ip;
    }

    public void set_ip(String ip) {
        this._ip = ip;
    }

    public String get_mac() {
        return this._mac;
    }

    public void set_mac(String mac) {
        this._mac = mac;
    }

    public int get_character_slot() {
        return this._character_slot;
    }

    public void set_character_slot(int character_slot) {
        this._character_slot = character_slot;
    }

    public String get_spw() {
        return this._spw;
    }

    public void set_spw(String spw) {
        this._spw = spw;
    }

    public int get_warehouse() {
        return this._warehouse;
    }

    public void set_warehouse(int warehouse) {
        this._warehouse = warehouse;
    }

    public int get_countCharacters() {
        return this._countCharacters;
    }

    public void set_countCharacters(int characters) {
        this._countCharacters = characters;
    }

    public boolean is_isLoad() {
        return this._isLoad;
    }

    public void set_isLoad(boolean load) {
        this._isLoad = load;
    }

    public void set_server_no(int server_no) {
        this._server_no = server_no;
    }

    public int get_server_no() {
        return this._server_no;
    }

    public void set_point(int points) {
        this._points = points;
    }

    public int get_point() {
        return this._points;
    }

    public int get_pay_first() {
        return this._pay_first;
    }

    public void set_pay_first(int pay_first) {
        this._pay_first = pay_first;
    }
}
