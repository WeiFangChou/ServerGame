package com.lineage.server.templates;

import java.util.Random;

public class L1Weapon extends L1Item {
    private static Random _random = new Random();
    private static final long serialVersionUID = 1;
    private int _add_dmg_max = 0;
    private int _add_dmg_min = 0;
    private int _canbedmg = 0;
    private int _dmgModifier = 0;
    private int _doubleDmgChance;
    private int _hitModifier = 0;
    private int _magicDmgModifier = 0;
    private int _range = 0;

    public void set_add_dmg(int add_dmg_min, int add_dmg_max) {
        this._add_dmg_min = add_dmg_min;
        this._add_dmg_max = add_dmg_max;
    }

    @Override // com.lineage.server.templates.L1Item
    public int get_add_dmg() {
        if (this._add_dmg_min == 0 || this._add_dmg_max == 0) {
            return 0;
        }
        return this._add_dmg_min + _random.nextInt(this._add_dmg_max - this._add_dmg_min);
    }

    @Override // com.lineage.server.templates.L1Item
    public int getRange() {
        return this._range;
    }

    public void setRange(int i) {
        this._range = i;
    }

    @Override // com.lineage.server.templates.L1Item
    public int getHitModifier() {
        return this._hitModifier;
    }

    public void setHitModifier(int i) {
        this._hitModifier = i;
    }

    @Override // com.lineage.server.templates.L1Item
    public int getDmgModifier() {
        return this._dmgModifier;
    }

    public void setDmgModifier(int i) {
        this._dmgModifier = i;
    }

    @Override // com.lineage.server.templates.L1Item
    public int getDoubleDmgChance() {
        return this._doubleDmgChance;
    }

    public void setDoubleDmgChance(int i) {
        this._doubleDmgChance = i;
    }

    @Override // com.lineage.server.templates.L1Item
    public int getMagicDmgModifier() {
        return this._magicDmgModifier;
    }

    public void setMagicDmgModifier(int i) {
        this._magicDmgModifier = i;
    }

    @Override // com.lineage.server.templates.L1Item
    public int get_canbedmg() {
        return this._canbedmg;
    }

    public void set_canbedmg(int i) {
        this._canbedmg = i;
    }

    @Override // com.lineage.server.templates.L1Item
    public boolean isTwohandedWeapon() {
        int weapon_type = getType();
        return weapon_type == 3 || weapon_type == 4 || weapon_type == 5 || weapon_type == 11 || weapon_type == 12 || weapon_type == 15 || weapon_type == 16 || weapon_type == 18;
    }
}
