package com.lineage.server.templates;

public class L1WeaponTypeSystem {
    private double _dmg;
    private int _gfxid;
    private int _probability;
    private int _type;

    public int getType() {
        return this._type;
    }

    public void setType(int i) {
        this._type = i;
    }

    public final int getProbability() {
        return this._probability;
    }

    public void setProbability(int Probability) {
        this._probability = Probability;
    }

    public final double getDmg() {
        return this._dmg;
    }

    public void setDmg(double dmg) {
        this._dmg = dmg;
    }

    public final int getgfxid() {
        return this._gfxid;
    }

    public void setgfxid(int gfxid) {
        this._gfxid = gfxid;
    }
}
