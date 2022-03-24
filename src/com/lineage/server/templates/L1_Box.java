package com.lineage.server.templates;

public class L1_Box {
    private int _box_item_id;
    private int _get_item_id;
    private int _max_count;
    private int _min_count;
    private int _out;
    private int _random;
    private int _remain_time;

    public int get_box_item_id() {
        return this._box_item_id;
    }

    public void set_box_item_id(int box_item_id) {
        this._box_item_id = box_item_id;
    }

    public int get_item_id() {
        return this._get_item_id;
    }

    public void set_get_item_id(int get_item_id) {
        this._get_item_id = get_item_id;
    }

    public int get_random() {
        return this._random;
    }

    public void set_random(int random) {
        this._random = random;
    }

    public int get_min_count() {
        return this._min_count;
    }

    public void set_min_count(int min_count) {
        this._min_count = min_count;
    }

    public int get_max_count() {
        return this._max_count;
    }

    public void set_max_count(int max_count) {
        this._max_count = max_count;
    }

    public int is_out() {
        return this._out;
    }

    public void set_out(int out) {
        this._out = out;
    }

    public int get_remain_time() {
        return this._remain_time;
    }

    public void set_remain_time(int remain_time) {
        this._remain_time = remain_time;
    }
}
