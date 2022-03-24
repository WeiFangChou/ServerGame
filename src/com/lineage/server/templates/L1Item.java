package com.lineage.server.templates;

import java.io.Serializable;

public abstract class L1Item implements Serializable {
    private static final long serialVersionUID = 1;
    private byte _addcha = 0;
    private byte _addcon = 0;
    private byte _adddex = 0;
    private int _addhp = 0;
    private int _addhpr = 0;
    private byte _addint = 0;
    private int _addmp = 0;
    private int _addmpr = 0;
    private int _addsp = 0;
    private byte _addstr = 0;
    private byte _addwis = 0;
    private int _bless;
    private boolean _cantDelete;
    private String _classname;
    private int _delay_effect;
    private int _dmgLarge = 0;
    private int _dmgSmall = 0;
    private int _expPoint;
    private int _foodVolume;
    private int _gfxId;
    private int _groundGfxId;
    private String _identifiedNameId;
    private boolean _isHasteItem = false;
    private int _itemDescId;
    private int _itemId;
    private int _material;
    private int _maxLevel;
    private int _maxUseTime = 0;
    private int _mdef = 0;
    private int _minLevel;
    private int[] _mode = null;
    private String _name;
    private int _safeEnchant = 0;
    private boolean _save_at_once;
    private boolean _tradable;
    private int _type;
    private int _type1;
    private int _type2;
    private String _unidentifiedNameId;
    private boolean _useDarkelf = false;
    private boolean _useDragonknight = false;
    private boolean _useElf = false;
    private boolean _useHighPet = false;
    private boolean _useIllusionist = false;
    private boolean _useKnight = false;
    private boolean _useMage = false;
    private boolean _useRoyal = false;
    private int _useType;
    private int _weight;
    private L1SuperCard card;

    public int getType2() {
        return this._type2;
    }

    public void setType2(int type) {
        this._type2 = type;
    }

    public int getItemId() {
        return this._itemId;
    }

    public void setItemId(int itemId) {
        this._itemId = itemId;
    }

    public String getName() {
        return this._name;
    }

    public void setName(String name) {
        this._name = name;
    }

    public String getclassname() {
        return this._classname;
    }

    public void setClassname(String classname) {
        this._classname = classname;
    }

    public String getUnidentifiedNameId() {
        return this._unidentifiedNameId;
    }

    public void setUnidentifiedNameId(String unidentifiedNameId) {
        this._unidentifiedNameId = unidentifiedNameId;
    }

    public String getIdentifiedNameId() {
        return this._identifiedNameId;
    }

    public void setIdentifiedNameId(String identifiedNameId) {
        this._identifiedNameId = identifiedNameId;
    }

    public int getType() {
        return this._type;
    }

    public void setType(int type) {
        this._type = type;
    }

    public int getType1() {
        return this._type1;
    }

    public void setType1(int type1) {
        this._type1 = type1;
    }

    public int getMaterial() {
        return this._material;
    }

    public void setMaterial(int material) {
        this._material = material;
    }

    public int getWeight() {
        return this._weight;
    }

    public void setWeight(int weight) {
        this._weight = weight;
    }

    public int getGfxId() {
        return this._gfxId;
    }

    public void setGfxId(int gfxId) {
        this._gfxId = gfxId;
    }

    public int getGroundGfxId() {
        return this._groundGfxId;
    }

    public void setGroundGfxId(int groundGfxId) {
        this._groundGfxId = groundGfxId;
    }

    public int getItemDescId() {
        return this._itemDescId;
    }

    public void setItemDescId(int descId) {
        this._itemDescId = descId;
    }

    public int getMinLevel() {
        return this._minLevel;
    }

    public void setMinLevel(int level) {
        this._minLevel = level;
    }

    public int getMaxLevel() {
        return this._maxLevel;
    }

    public void setMaxLevel(int maxlvl) {
        this._maxLevel = maxlvl;
    }

    public int getBless() {
        return this._bless;
    }

    public void setBless(int i) {
        this._bless = i;
    }

    public boolean isTradable() {
        return this._tradable;
    }

    public void setTradable(boolean flag) {
        this._tradable = flag;
    }

    public boolean isCantDelete() {
        return this._cantDelete;
    }

    public void setCantDelete(boolean flag) {
        this._cantDelete = flag;
    }

    public boolean isToBeSavedAtOnce() {
        return this._save_at_once;
    }

    public void setToBeSavedAtOnce(boolean flag) {
        this._save_at_once = flag;
    }

    public int getMaxUseTime() {
        return this._maxUseTime;
    }

    public void setMaxUseTime(int i) {
        this._maxUseTime = i;
    }

    public int getFoodVolume() {
        return this._foodVolume;
    }

    public void setFoodVolume(int volume) {
        this._foodVolume = volume;
    }

    public int getLightRange() {
        switch (this._itemId) {
            case 40001:
                return 11;
            case 40002:
                return 14;
            case 40003:
            default:
                return 0;
            case 40004:
                return 22;
            case 40005:
                return 8;
        }
    }

    public int getLightFuel() {
        switch (this._itemId) {
            case 40001:
                return 6000;
            case 40002:
                return 12000;
            case 40003:
                return 12000;
            case 40004:
                return 0;
            case 40005:
                return 600;
            default:
                return 0;
        }
    }

    public int getUseType() {
        return this._useType;
    }

    public void setUseType(int useType) {
        this._useType = useType;
    }

    public int getDmgSmall() {
        return this._dmgSmall;
    }

    public void setDmgSmall(int dmgSmall) {
        this._dmgSmall = dmgSmall;
    }

    public int getDmgLarge() {
        return this._dmgLarge;
    }

    public void setDmgLarge(int dmgLarge) {
        this._dmgLarge = dmgLarge;
    }

    public int[] get_mode() {
        return this._mode;
    }

    public void set_mode(int[] mode) {
        this._mode = mode;
    }

    public int get_safeenchant() {
        return this._safeEnchant;
    }

    public void set_safeenchant(int safeenchant) {
        this._safeEnchant = safeenchant;
    }

    public boolean isUseRoyal() {
        return this._useRoyal;
    }

    public void setUseRoyal(boolean flag) {
        this._useRoyal = flag;
    }

    public boolean isUseKnight() {
        return this._useKnight;
    }

    public void setUseKnight(boolean flag) {
        this._useKnight = flag;
    }

    public boolean isUseElf() {
        return this._useElf;
    }

    public void setUseElf(boolean flag) {
        this._useElf = flag;
    }

    public boolean isUseMage() {
        return this._useMage;
    }

    public void setUseMage(boolean flag) {
        this._useMage = flag;
    }

    public boolean isUseDarkelf() {
        return this._useDarkelf;
    }

    public void setUseDarkelf(boolean flag) {
        this._useDarkelf = flag;
    }

    public boolean isUseDragonknight() {
        return this._useDragonknight;
    }

    public void setUseDragonknight(boolean flag) {
        this._useDragonknight = flag;
    }

    public boolean isUseIllusionist() {
        return this._useIllusionist;
    }

    public void setUseIllusionist(boolean flag) {
        this._useIllusionist = flag;
    }

    public byte get_addstr() {
        return this._addstr;
    }

    public void set_addstr(byte addstr) {
        this._addstr = addstr;
    }

    public byte get_adddex() {
        return this._adddex;
    }

    public void set_adddex(byte adddex) {
        this._adddex = adddex;
    }

    public byte get_addcon() {
        return this._addcon;
    }

    public void set_addcon(byte addcon) {
        this._addcon = addcon;
    }

    public byte get_addint() {
        return this._addint;
    }

    public void set_addint(byte addint) {
        this._addint = addint;
    }

    public byte get_addwis() {
        return this._addwis;
    }

    public void set_addwis(byte addwis) {
        this._addwis = addwis;
    }

    public byte get_addcha() {
        return this._addcha;
    }

    public void set_addcha(byte addcha) {
        this._addcha = addcha;
    }

    public int get_addhp() {
        return this._addhp;
    }

    public void set_addhp(int addhp) {
        this._addhp = addhp;
    }

    public int get_addmp() {
        return this._addmp;
    }

    public void set_addmp(int addmp) {
        this._addmp = addmp;
    }

    public int get_addhpr() {
        return this._addhpr;
    }

    public void set_addhpr(int addhpr) {
        this._addhpr = addhpr;
    }

    public int get_addmpr() {
        return this._addmpr;
    }

    public void set_addmpr(int addmpr) {
        this._addmpr = addmpr;
    }

    public int get_addsp() {
        return this._addsp;
    }

    public void set_addsp(int addsp) {
        this._addsp = addsp;
    }

    public int get_mdef() {
        return this._mdef;
    }

    public void set_mdef(int i) {
        this._mdef = i;
    }

    public boolean isHasteItem() {
        return this._isHasteItem;
    }

    public void setHasteItem(boolean flag) {
        this._isHasteItem = flag;
    }

    public boolean isStackable() {
        return false;
    }

    public int get_delayid() {
        return 0;
    }

    public int get_delaytime() {
        return 0;
    }

    public int getMaxChargeCount() {
        return 0;
    }

    public void set_delayEffect(int delay_effect) {
        this._delay_effect = delay_effect;
    }

    public int get_delayEffect() {
        return this._delay_effect;
    }

    public int get_add_dmg() {
        return 0;
    }

    public int getRange() {
        return 0;
    }

    public int getHitModifier() {
        return 0;
    }

    public int getDmgModifier() {
        return 0;
    }

    public int getDoubleDmgChance() {
        return 0;
    }

    public int getMagicDmgModifier() {
        return 0;
    }

    public int get_canbedmg() {
        return 0;
    }

    public boolean isTwohandedWeapon() {
        return false;
    }

    public int get_ac() {
        return 0;
    }

    public int getDamageReduction() {
        return 0;
    }

    public int getWeightReduction() {
        return 0;
    }

    public int getHitModifierByArmor() {
        return 0;
    }

    public int getDmgModifierByArmor() {
        return 0;
    }

    public int getBowHitModifierByArmor() {
        return 0;
    }

    public int getBowDmgModifierByArmor() {
        return 0;
    }

    public int get_defense_water() {
        return 0;
    }

    public int get_defense_fire() {
        return 0;
    }

    public int get_defense_earth() {
        return 0;
    }

    public int get_defense_wind() {
        return 0;
    }

    public int get_regist_stun() {
        return 0;
    }

    public int get_regist_stone() {
        return 0;
    }

    public int get_regist_sleep() {
        return 0;
    }

    public int get_regist_freeze() {
        return 0;
    }

    public int get_regist_sustain() {
        return 0;
    }

    public int get_regist_blind() {
        return 0;
    }

    public int get_greater() {
        return 3;
    }

    public L1SuperCard getCard() {
        return this.card;
    }

    public void setCard(L1SuperCard card2) {
        this.card = card2;
    }

    public int getExpPoint() {
        return this._expPoint;
    }

    public void setExpPoint(int i) {
        this._expPoint = i;
    }

    public boolean isUseHighPet() {
        return this._useHighPet;
    }

    public void setUseHighPet(boolean flag) {
        this._useHighPet = flag;
    }

    public boolean isActivity() {
        return false;
    }
}
