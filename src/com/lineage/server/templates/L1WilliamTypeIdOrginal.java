package com.lineage.server.templates;

public class L1WilliamTypeIdOrginal {
    private final double _dmg;
    private final int _gfxId;

    public L1WilliamTypeIdOrginal(int gfxId, double dmg) {
        this._gfxId = gfxId;
        this._dmg = dmg;
    }

    public int getGfxId() {
        return this._gfxId;
    }

    public final double getDmg() {
        return this._dmg;
    }
}
