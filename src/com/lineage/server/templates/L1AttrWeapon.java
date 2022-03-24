package com.lineage.server.templates;

public class L1AttrWeapon {
    private final int _gfxid;
    private final String _name;
    private final int _probability;
    private final int _stage;
    private final double _type_bind;
    private final double _type_dmgup;
    private final int _type_drain_hp;
    private final int _type_drain_mp;
    private final int _type_range;
    private final int _type_range_dmg;

    public L1AttrWeapon(String name, int stage, int probability, double type_dmgup, double type_bind, int type_drain_hp, int type_drain_mp, int type_range, int type_range_dmg, int gfxid) {
        this._name = name;
        this._stage = stage;
        this._probability = probability;
        this._type_bind = type_bind;
        this._type_dmgup = type_dmgup;
        this._type_drain_hp = type_drain_hp;
        this._type_drain_mp = type_drain_mp;
        this._type_range = type_range;
        this._type_range_dmg = type_range_dmg;
        this._gfxid = gfxid;
    }

    public final String getName() {
        return this._name;
    }

    public final int getStage() {
        return this._stage;
    }

    public final int getProbability() {
        return this._probability;
    }

    public final double getTypeBind() {
        return this._type_bind;
    }

    public final double getTypeDmgup() {
        return this._type_dmgup;
    }

    public final int getTypeDrainHp() {
        return this._type_drain_hp;
    }

    public final int getTypeDrainMp() {
        return this._type_drain_mp;
    }

    public final int getTypeRange() {
        return this._type_range;
    }

    public final int getTypeRangeDmg() {
        return this._type_range_dmg;
    }

    public final int gfxid() {
        return this._gfxid;
    }
}
