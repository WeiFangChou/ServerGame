package com.lineage.server.templates;

public class L1Armor extends L1Item {
    private static final long serialVersionUID = 1;
    private int _ac = 0;
    private int _bowDmgModifierByArmor = 0;
    private int _bowHitModifierByArmor = 0;
    private int _damageReduction = 0;
    private int _defense_earth = 0;
    private int _defense_fire = 0;
    private int _defense_water = 0;
    private int _defense_wind = 0;
    private int _dmgModifierByArmor = 0;
    private int _greater = 3;
    private int _hitModifierByArmor = 0;
    private int _regist_blind = 0;
    private int _regist_freeze = 0;
    private int _regist_sleep = 0;
    private int _regist_stone = 0;
    private int _regist_stun = 0;
    private int _regist_sustain = 0;
    private int _weightReduction = 0;

    @Override // com.lineage.server.templates.L1Item
    public int get_ac() {
        return this._ac;
    }

    public void set_ac(int i) {
        this._ac = i;
    }

    @Override // com.lineage.server.templates.L1Item
    public int getDamageReduction() {
        return this._damageReduction;
    }

    public void setDamageReduction(int i) {
        this._damageReduction = i;
    }

    @Override // com.lineage.server.templates.L1Item
    public int getWeightReduction() {
        return this._weightReduction;
    }

    public void setWeightReduction(int i) {
        this._weightReduction = i;
    }

    @Override // com.lineage.server.templates.L1Item
    public int getHitModifierByArmor() {
        return this._hitModifierByArmor;
    }

    public void setHitModifierByArmor(int i) {
        this._hitModifierByArmor = i;
    }

    @Override // com.lineage.server.templates.L1Item
    public int getDmgModifierByArmor() {
        return this._dmgModifierByArmor;
    }

    public void setDmgModifierByArmor(int i) {
        this._dmgModifierByArmor = i;
    }

    @Override // com.lineage.server.templates.L1Item
    public int getBowHitModifierByArmor() {
        return this._bowHitModifierByArmor;
    }

    public void setBowHitModifierByArmor(int i) {
        this._bowHitModifierByArmor = i;
    }

    @Override // com.lineage.server.templates.L1Item
    public int getBowDmgModifierByArmor() {
        return this._bowDmgModifierByArmor;
    }

    public void setBowDmgModifierByArmor(int i) {
        this._bowDmgModifierByArmor = i;
    }

    public void set_defense_water(int i) {
        this._defense_water = i;
    }

    @Override // com.lineage.server.templates.L1Item
    public int get_defense_water() {
        return this._defense_water;
    }

    public void set_defense_wind(int i) {
        this._defense_wind = i;
    }

    @Override // com.lineage.server.templates.L1Item
    public int get_defense_wind() {
        return this._defense_wind;
    }

    public void set_defense_fire(int i) {
        this._defense_fire = i;
    }

    @Override // com.lineage.server.templates.L1Item
    public int get_defense_fire() {
        return this._defense_fire;
    }

    public void set_defense_earth(int i) {
        this._defense_earth = i;
    }

    @Override // com.lineage.server.templates.L1Item
    public int get_defense_earth() {
        return this._defense_earth;
    }

    public void set_regist_stun(int i) {
        this._regist_stun = i;
    }

    @Override // com.lineage.server.templates.L1Item
    public int get_regist_stun() {
        return this._regist_stun;
    }

    public void set_regist_stone(int i) {
        this._regist_stone = i;
    }

    @Override // com.lineage.server.templates.L1Item
    public int get_regist_stone() {
        return this._regist_stone;
    }

    public void set_regist_sleep(int i) {
        this._regist_sleep = i;
    }

    @Override // com.lineage.server.templates.L1Item
    public int get_regist_sleep() {
        return this._regist_sleep;
    }

    public void set_regist_freeze(int i) {
        this._regist_freeze = i;
    }

    @Override // com.lineage.server.templates.L1Item
    public int get_regist_freeze() {
        return this._regist_freeze;
    }

    public void set_regist_sustain(int i) {
        this._regist_sustain = i;
    }

    @Override // com.lineage.server.templates.L1Item
    public int get_regist_sustain() {
        return this._regist_sustain;
    }

    public void set_regist_blind(int i) {
        this._regist_blind = i;
    }

    @Override // com.lineage.server.templates.L1Item
    public int get_regist_blind() {
        return this._regist_blind;
    }

    public void set_greater(int greater) {
        this._greater = greater;
    }

    @Override // com.lineage.server.templates.L1Item
    public int get_greater() {
        return this._greater;
    }
}
