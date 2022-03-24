package com.lineage.server.templates;

public class L1Skills {
    public static final int ATTR_EARTH = 1;
    public static final int ATTR_FIRE = 2;
    public static final int ATTR_NONE = 0;
    public static final int ATTR_RAY = 16;
    public static final int ATTR_WATER = 4;
    public static final int ATTR_WIND = 8;
    public static final int TARGET_TO_CLAN = 4;
    public static final int TARGET_TO_ME = 0;
    public static final int TARGET_TO_NPC = 2;
    public static final int TARGET_TO_PARTY = 8;
    public static final int TARGET_TO_PC = 1;
    public static final int TARGET_TO_PET = 16;
    public static final int TARGET_TO_PLACE = 32;
    public static final int TYPE_ATTACK = 64;
    public static final int TYPE_CHANGE = 2;
    public static final int TYPE_CURSE = 4;
    public static final int TYPE_DEATH = 8;
    public static final int TYPE_HEAL = 16;
    public static final int TYPE_OTHER = 128;
    public static final int TYPE_PROBABILITY = 1;
    public static final int TYPE_RESTORE = 32;
    private int _actionId;
    private int _area;
    private int _attr;
    private int _buffDuration;
    private int _castGfx;
    private int _castGfx2;
    private int _damageDice;
    private int _damageDiceCount;
    private int _damageValue;
    private int _hpConsume;
    private int _id;
    boolean _isThrough;
    private int _itmeConsumeCount;
    private int _itmeConsumeId;
    private int _lawful;
    private int _mpConsume;
    private String _name;
    private String _nameId;
    private int _probabilityDice;
    private int _probabilityValue;
    private int _ranged;
    private int _reuseDelay;
    private int _skillId;
    private int _skillLevel;
    private int _skillNumber;
    private int _sysmsgIdFail;
    private int _sysmsgIdHappen;
    private int _sysmsgIdStop;
    private String _target;
    private int _targetTo;
    private int _type;

    public int getSkillId() {
        return this._skillId;
    }

    public void setSkillId(int i) {
        this._skillId = i;
    }

    public String getName() {
        return this._name;
    }

    public void setName(String s) {
        this._name = s;
    }

    public int getSkillLevel() {
        return this._skillLevel;
    }

    public void setSkillLevel(int i) {
        this._skillLevel = i;
    }

    public int getSkillNumber() {
        return this._skillNumber;
    }

    public void setSkillNumber(int i) {
        this._skillNumber = i;
    }

    public int getMpConsume() {
        return this._mpConsume;
    }

    public void setMpConsume(int i) {
        this._mpConsume = i;
    }

    public int getHpConsume() {
        return this._hpConsume;
    }

    public void setHpConsume(int i) {
        this._hpConsume = i;
    }

    public int getItemConsumeId() {
        return this._itmeConsumeId;
    }

    public void setItemConsumeId(int i) {
        this._itmeConsumeId = i;
    }

    public int getItemConsumeCount() {
        return this._itmeConsumeCount;
    }

    public void setItemConsumeCount(int i) {
        this._itmeConsumeCount = i;
    }

    public int getReuseDelay() {
        return this._reuseDelay;
    }

    public void setReuseDelay(int i) {
        this._reuseDelay = i;
    }

    public int getBuffDuration() {
        return this._buffDuration;
    }

    public void setBuffDuration(int i) {
        this._buffDuration = i;
    }

    public String getTarget() {
        return this._target;
    }

    public void setTarget(String s) {
        this._target = s;
    }

    public int getTargetTo() {
        return this._targetTo;
    }

    public void setTargetTo(int i) {
        this._targetTo = i;
    }

    public int getDamageValue() {
        return this._damageValue;
    }

    public void setDamageValue(int i) {
        this._damageValue = i;
    }

    public int getDamageDice() {
        return this._damageDice;
    }

    public void setDamageDice(int i) {
        this._damageDice = i;
    }

    public int getDamageDiceCount() {
        return this._damageDiceCount;
    }

    public void setDamageDiceCount(int i) {
        this._damageDiceCount = i;
    }

    public int getProbabilityValue() {
        return this._probabilityValue;
    }

    public void setProbabilityValue(int i) {
        this._probabilityValue = i;
    }

    public int getProbabilityDice() {
        return this._probabilityDice;
    }

    public void setProbabilityDice(int i) {
        this._probabilityDice = i;
    }

    public int getAttr() {
        return this._attr;
    }

    public void setAttr(int i) {
        this._attr = i;
    }

    public int getType() {
        return this._type;
    }

    public void setType(int i) {
        this._type = i;
    }

    public int getLawful() {
        return this._lawful;
    }

    public void setLawful(int i) {
        this._lawful = i;
    }

    public int getRanged() {
        return this._ranged;
    }

    public void setRanged(int i) {
        this._ranged = i;
    }

    public int getArea() {
        return this._area;
    }

    public void setArea(int i) {
        this._area = i;
    }

    public boolean isThrough() {
        return this._isThrough;
    }

    public void setThrough(boolean flag) {
        this._isThrough = flag;
    }

    public int getId() {
        return this._id;
    }

    public void setId(int i) {
        this._id = i;
    }

    public String getNameId() {
        return this._nameId;
    }

    public void setNameId(String s) {
        this._nameId = s;
    }

    public int getActionId() {
        return this._actionId;
    }

    public void setActionId(int i) {
        this._actionId = i;
    }

    public int getCastGfx() {
        return this._castGfx;
    }

    public void setCastGfx(int i) {
        this._castGfx = i;
    }

    public int getCastGfx2() {
        return this._castGfx2;
    }

    public void setCastGfx2(int i) {
        this._castGfx2 = i;
    }

    public int getSysmsgIdHappen() {
        return this._sysmsgIdHappen;
    }

    public void setSysmsgIdHappen(int i) {
        this._sysmsgIdHappen = i;
    }

    public int getSysmsgIdStop() {
        return this._sysmsgIdStop;
    }

    public void setSysmsgIdStop(int i) {
        this._sysmsgIdStop = i;
    }

    public int getSysmsgIdFail() {
        return this._sysmsgIdFail;
    }

    public void setSysmsgIdFail(int i) {
        this._sysmsgIdFail = i;
    }
}
