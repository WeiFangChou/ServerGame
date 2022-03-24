package com.lineage.server.templates;

public class L1Exp {
    private long _exp;
    private double _expPenalty;
    private int _level;

    public int get_level() {
        return this._level;
    }

    public void set_level(int level) {
        this._level = level;
    }

    public long get_exp() {
        return this._exp;
    }

    public void set_exp(long exp) {
        this._exp = exp;
    }

    public double get_expPenalty() {
        return this._expPenalty;
    }

    public void set_expPenalty(double penalty) {
        this._expPenalty = penalty;
    }
}
