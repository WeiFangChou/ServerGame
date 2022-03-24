package com.lineage.server.model;

import com.lineage.data.ItemClass;
import com.lineage.data.item_armor.set.ArmorSet;
import com.lineage.server.datatables.BlessDystem;
import com.lineage.server.datatables.BlessSystem;
import com.lineage.server.datatables.ItemTimeTable;
import com.lineage.server.datatables.NewEnchantDystem;
import com.lineage.server.datatables.NewEnchantSystem;
import com.lineage.server.datatables.lock.CharItemsTimeReading;
import com.lineage.server.datatables.lock.CharSkillReading;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_Ability;
import com.lineage.server.serverpackets.S_AddSkill;
import com.lineage.server.serverpackets.S_DelSkill;
import com.lineage.server.serverpackets.S_Invis;
import com.lineage.server.serverpackets.S_ItemName;
import com.lineage.server.serverpackets.S_RemoveObject;
import com.lineage.server.serverpackets.S_SPMR;
import com.lineage.server.serverpackets.S_SkillBrave;
import com.lineage.server.serverpackets.S_SkillHaste;
import com.lineage.server.templates.L1BlessDystem;
import com.lineage.server.templates.L1BlessSystem;
import com.lineage.server.templates.L1Item;
import com.lineage.server.templates.L1ItemTime;
import com.lineage.server.templates.L1NewEnchantDystem;
import com.lineage.server.templates.L1NewEnchantSystem;
import java.sql.Timestamp;
import java.util.ArrayList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1EquipmentSlot {
    public static final Log _log = LogFactory.getLog(L1EquipmentSlot.class);
    private ArrayList<L1ItemInstance> _armors = new ArrayList<>();
    private ArrayList<ArmorSet> _currentArmorSet = new ArrayList<>();
    private L1PcInstance _owner;
    private L1ItemInstance _weapon;

    public L1EquipmentSlot(L1PcInstance owner) {
        this._owner = owner;
    }

    public L1ItemInstance getWeapon() {
        return this._weapon;
    }

    public ArrayList<L1ItemInstance> getArmors() {
        return this._armors;
    }

    private void setWeapon(L1ItemInstance weapon) {
        this._owner.setWeapon(weapon);
        this._owner.setCurrentWeapon(weapon.getItem().getType1());
        weapon.startEquipmentTimer(this._owner);
        this._weapon = weapon;
    }

    private void removeWeapon(L1ItemInstance weapon) {
        this._owner.setWeapon(null);
        this._owner.setCurrentWeapon(0);
        weapon.stopEquipmentTimer(this._owner);
        this._weapon = null;
        if (this._owner.hasSkillEffect(91)) {
            this._owner.removeSkillEffect(91);
        }
    }

    private void setArmor(L1ItemInstance armor) {
        L1Item item = armor.getItem();
        int itemId = armor.getItem().getItemId();
        switch (armor.getItem().getUseType()) {
            case 2:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 25:
                this._owner.addAc((item.get_ac() - armor.getEnchantLevel()) - armor.getAcByMagic());
                break;
            case 23:
            case 24:
            case 37:
            case 40:
                if (item.get_ac() != 0) {
                    this._owner.addAc(item.get_ac());
                }
                if (armor.getItem().get_greater() != 3) {
                    armor.greater(this._owner, true);
                    break;
                }
                break;
            case 43:
            case 44:
            case 45:
            case 47:
            case 48:
                if (item.get_ac() != 0) {
                    this._owner.addAc(item.get_ac());
                    break;
                }
                break;
        }
        set_time_item(armor);
        this._owner.addDamageReductionByArmor(item.getDamageReduction());
        this._owner.addWeightReduction(item.getWeightReduction());
        int hit = item.getHitModifierByArmor();
        int dmg = item.getDmgModifierByArmor();
        this._owner.addHitModifierByArmor(hit);
        this._owner.addDmgModifierByArmor(dmg);
        this._owner.addBowHitModifierByArmor(item.getBowHitModifierByArmor());
        this._owner.addBowDmgModifierByArmor(item.getBowDmgModifierByArmor());
        this._owner.addFire(item.get_defense_fire());
        this._owner.addWater(item.get_defense_water());
        this._owner.addWind(item.get_defense_wind());
        this._owner.addEarth(item.get_defense_earth());
        this._owner.add_regist_freeze(item.get_regist_freeze());
        this._owner.addRegistStone(item.get_regist_stone());
        this._owner.addRegistSleep(item.get_regist_sleep());
        this._owner.addRegistBlind(item.get_regist_blind());
        this._owner.addRegistStun(item.get_regist_stun());
        this._owner.addRegistSustain(item.get_regist_sustain());
        this._armors.add(armor);
        for (Integer key : ArmorSet.getAllSet().keySet()) {
            ArmorSet armorSet = ArmorSet.getAllSet().get(key);
            if (armorSet.isPartOfSet(itemId) && armorSet.isValid(this._owner)) {
                if (armor.getItem().getUseType() != 23) {
                    armorSet.giveEffect(this._owner);
                    this._currentArmorSet.add(armorSet);
                    this._owner.getInventory().setPartMode(armorSet, true);
                } else if (!armorSet.isEquippedRingOfArmorSet(this._owner)) {
                    armorSet.giveEffect(this._owner);
                    this._currentArmorSet.add(armorSet);
                    this._owner.getInventory().setPartMode(armorSet, true);
                }
            }
        }
        armor.startEquipmentTimer(this._owner);
    }

    private void set_time_item(L1ItemInstance item) {
        L1ItemTime itemTime;
        if (item.get_time() == null && (itemTime = ItemTimeTable.TIME.get(Integer.valueOf(item.getItemId()))) != null) {
            Timestamp ts = new Timestamp(System.currentTimeMillis() + ((long) (itemTime.get_remain_time() * 60 * L1SkillId.STATUS_BRAVE)));
            item.set_time(ts);
            CharItemsTimeReading.get().addTime(item.getId(), ts);
            this._owner.sendPackets(new S_ItemName(item));
        }
    }

    private void removeArmor(L1ItemInstance armor) {
        L1Item item = armor.getItem();
        int itemId = armor.getItem().getItemId();
        switch (armor.getItem().getUseType()) {
            case 2:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 25:
                this._owner.addAc(-((item.get_ac() - armor.getEnchantLevel()) - armor.getAcByMagic()));
                break;
            case 23:
            case 24:
            case 37:
            case 40:
                if (item.get_ac() != 0) {
                    this._owner.addAc(-item.get_ac());
                }
                if (armor.getItem().get_greater() != 3) {
                    armor.greater(this._owner, false);
                    break;
                }
                break;
            case 43:
            case 44:
            case 45:
            case 47:
            case 48:
                if (item.get_ac() != 0) {
                    this._owner.addAc(-item.get_ac());
                    break;
                }
                break;
        }
        this._owner.addDamageReductionByArmor(-item.getDamageReduction());
        this._owner.addWeightReduction(-item.getWeightReduction());
        int hit = item.getHitModifierByArmor();
        int dmg = item.getDmgModifierByArmor();
        this._owner.addHitModifierByArmor(-hit);
        this._owner.addDmgModifierByArmor(-dmg);
        this._owner.addBowHitModifierByArmor(-item.getBowHitModifierByArmor());
        this._owner.addBowDmgModifierByArmor(-item.getBowDmgModifierByArmor());
        this._owner.addFire(-item.get_defense_fire());
        this._owner.addWater(-item.get_defense_water());
        this._owner.addWind(-item.get_defense_wind());
        this._owner.addEarth(-item.get_defense_earth());
        this._owner.add_regist_freeze(-item.get_regist_freeze());
        this._owner.addRegistStone(-item.get_regist_stone());
        this._owner.addRegistSleep(-item.get_regist_sleep());
        this._owner.addRegistBlind(-item.get_regist_blind());
        this._owner.addRegistStun(-item.get_regist_stun());
        this._owner.addRegistSustain(-item.get_regist_sustain());
        for (Integer key : ArmorSet.getAllSet().keySet()) {
            ArmorSet armorSet = ArmorSet.getAllSet().get(key);
            if (armorSet.isPartOfSet(itemId) && this._currentArmorSet.contains(armorSet) && !armorSet.isValid(this._owner)) {
                armorSet.cancelEffect(this._owner);
                this._currentArmorSet.remove(armorSet);
                this._owner.getInventory().setPartMode(armorSet, false);
            }
        }
        armor.stopEquipmentTimer(this._owner);
        this._armors.remove(armor);
    }

    public void set(L1ItemInstance eq) {
        int extraMr;
        L1Item item = eq.getItem();
        if (item.getType2() != 0 || item.getCard() != null) {
            int polyId = item.getCard() == null ? 0 : item.getCard().getExtraPoly();
            if (polyId != 0) {
                L1PolyMorph.doPoly(this._owner, polyId, 0, 1);
            }
            int hit = item.getHitModifierByArmor() + (item.getCard() == null ? 0 : item.getCard().getExtraHit() + item.getCard().getWakeHit());
            int dmg = item.getDmgModifierByArmor() + (item.getCard() == null ? 0 : item.getCard().getExtraDmg() + item.getCard().getWakeDmg());
            int Bowhit = item.getBowHitModifierByArmor() + (item.getCard() == null ? 0 : item.getCard().getExtraBowhit() + item.getCard().getWakeBowhit());
            int Bowdmg = item.getBowDmgModifierByArmor() + (item.getCard() == null ? 0 : item.getCard().getExtraBowdmg() + item.getCard().getWakeBowdmg());
            int addhp = item.get_addhp() + (item.getCard() == null ? 0 : item.getCard().getExtraHp() + item.getCard().getWakeHp());
            int addmp = item.get_addmp() + (item.getCard() == null ? 0 : item.getCard().getExtraMp() + item.getCard().getWakeMp());
            int get_addstr = item.get_addstr() + (item.getCard() == null ? 0 : item.getCard().getExtraStr() + item.getCard().getWakeStr());
            int get_adddex = item.get_adddex() + (item.getCard() == null ? 0 : item.getCard().getExtraDex() + item.getCard().getWakeDex());
            int get_addcon = item.get_addcon() + (item.getCard() == null ? 0 : item.getCard().getExtraCon() + item.getCard().getWakeCon());
            int get_addwis = item.get_addwis() + (item.getCard() == null ? 0 : item.getCard().getExtraWis() + item.getCard().getWakeWis());
            int get_addint = item.get_addint() + (item.getCard() == null ? 0 : item.getCard().getExtraInt() + item.getCard().getWakeInt());
            int get_addcha = item.get_addcha() + (item.getCard() == null ? 0 : item.getCard().getExtraCha() + item.getCard().getWakeCha());
            int exp = item.getExpPoint() + (item.getCard() == null ? 0 : item.getCard().getExtraExp() + item.getCard().getWakeExp());
            int weight = item.getCard() == null ? 0 : item.getCard().getExtraWeight() + item.getCard().getWakeWeight();
            int earth = item.getCard() == null ? 0 : item.getCard().getExtraEarth() + item.getCard().getWakeEarth();
            int wind = item.getCard() == null ? 0 : item.getCard().getExtraWind() + item.getCard().getWakeWind();
            int water = item.getCard() == null ? 0 : item.getCard().getExtraWater() + item.getCard().getWakeWater();
            int fire = item.getCard() == null ? 0 : item.getCard().getExtraFire() + item.getCard().getWakeFire();
            int stun = item.getCard() == null ? 0 : item.getCard().getExtraStun() + item.getCard().getWakeStun();
            int stone = item.getCard() == null ? 0 : item.getCard().getExtraStone() + item.getCard().getWakeStone();
            int sleep = item.getCard() == null ? 0 : item.getCard().getExtraSleep() + item.getCard().getWakeSleep();
            int freeze = item.getCard() == null ? 0 : item.getCard().getExtrafreeze() + item.getCard().getWakefreeze();
            int sustain = item.getCard() == null ? 0 : item.getCard().getExtraSustain() + item.getCard().getWakeSustain();
            int blind = item.getCard() == null ? 0 : item.getCard().getExtraBlind() + item.getCard().getWakeBlind();
            int bamage = item.getCard() == null ? 0 : item.getCard().getExtraBamage() + item.getCard().getWakeBamage();
            int addSp = item.get_addsp() + (item.getCard() == null ? 0 : item.getCard().getExtraSp() + item.getCard().getWakeSp());
            int mr = eq.getMr();
            if (item.getCard() == null) {
                extraMr = 0;
            } else {
                extraMr = item.getCard().getExtraMr() + item.getCard().getWakeMr();
            }
            int addMr = mr + extraMr;
            L1BlessSystem Bless = BlessSystem.get(item.getItemId());
            if (Bless != null) {
                if (Bless.getHp() != 0) {
                    addhp += Bless.getHp();
                }
                if (Bless.getMp() != 0) {
                    addmp += Bless.getMp();
                }
                if (Bless.getStr() != 0) {
                    get_addstr += Bless.getStr();
                }
                if (Bless.getDex() != 0) {
                    get_adddex += Bless.getDex();
                }
                if (Bless.getCon() != 0) {
                    get_addcon += Bless.getCon();
                }
                if (Bless.getWis() != 0) {
                    get_addwis += Bless.getWis();
                }
                if (Bless.getInt() != 0) {
                    get_addint += Bless.getInt();
                }
                if (Bless.getCha() != 0) {
                    get_addcha += Bless.getCha();
                }
                if (Bless.getMr() != 0) {
                    addMr += Bless.getMr();
                }
                if (Bless.getSp() != 0) {
                    addSp += Bless.getSp();
                }
                if (Bless.getFire() != 0) {
                    fire += Bless.getFire();
                }
                if (Bless.getWater() != 0) {
                    water += Bless.getWater();
                }
                if (Bless.getWind() != 0) {
                    wind += Bless.getWind();
                }
                if (Bless.getEarth() != 0) {
                    earth += Bless.getEarth();
                }
                if (Bless.getFreeze() != 0) {
                    freeze += Bless.getFreeze();
                }
                if (Bless.getStone() != 0) {
                    stone += Bless.getStone();
                }
                if (Bless.getSleep() != 0) {
                    sleep += Bless.getSleep();
                }
                if (Bless.getBlind() != 0) {
                    blind += Bless.getBlind();
                }
                if (Bless.getStun() != 0) {
                    stun += Bless.getStun();
                }
                if (Bless.getSustain() != 0) {
                    sustain += Bless.getSustain();
                }
                if (Bless.getExp() != 0) {
                    exp += Bless.getExp();
                }
                if (Bless.getBamage() != 0) {
                    bamage += Bless.getBamage();
                }
                if (Bless.getHit() != 0) {
                    hit += Bless.getHit();
                }
                if (Bless.getDmg() != 0) {
                    dmg += Bless.getDmg();
                }
                if (Bless.getBowHit() != 0) {
                    Bowhit += Bless.getBowHit();
                }
                if (Bless.getBowDmg() != 0) {
                    Bowdmg += Bless.getBowDmg();
                }
            }
            L1BlessDystem Bl_ess = BlessDystem.get(item.getItemId());
            if (Bl_ess != null) {
                if (Bl_ess.getHp() != 0) {
                    addhp += Bl_ess.getHp();
                }
                if (Bl_ess.getMp() != 0) {
                    addmp += Bl_ess.getMp();
                }
                if (Bl_ess.getStr() != 0) {
                    get_addstr += Bl_ess.getStr();
                }
                if (Bl_ess.getDex() != 0) {
                    get_adddex += Bl_ess.getDex();
                }
                if (Bl_ess.getCon() != 0) {
                    get_addcon += Bl_ess.getCon();
                }
                if (Bl_ess.getWis() != 0) {
                    get_addwis += Bl_ess.getWis();
                }
                if (Bl_ess.getInt() != 0) {
                    get_addint += Bl_ess.getInt();
                }
                if (Bl_ess.getCha() != 0) {
                    get_addcha += Bl_ess.getCha();
                }
                if (Bl_ess.getMr() != 0) {
                    addMr += Bl_ess.getMr();
                }
                if (Bl_ess.getSp() != 0) {
                    addSp += Bl_ess.getSp();
                }
                if (Bl_ess.getFire() != 0) {
                    fire += Bl_ess.getFire();
                }
                if (Bl_ess.getWater() != 0) {
                    water += Bl_ess.getWater();
                }
                if (Bl_ess.getWind() != 0) {
                    wind += Bl_ess.getWind();
                }
                if (Bl_ess.getEarth() != 0) {
                    earth += Bl_ess.getEarth();
                }
                if (Bl_ess.getFreeze() != 0) {
                    freeze += Bl_ess.getFreeze();
                }
                if (Bl_ess.getStone() != 0) {
                    stone += Bl_ess.getStone();
                }
                if (Bl_ess.getSleep() != 0) {
                    sleep += Bl_ess.getSleep();
                }
                if (Bl_ess.getBlind() != 0) {
                    blind += Bl_ess.getBlind();
                }
                if (Bl_ess.getStun() != 0) {
                    stun += Bl_ess.getStun();
                }
                if (Bl_ess.getSustain() != 0) {
                    sustain += Bl_ess.getSustain();
                }
                if (Bl_ess.getExp() != 0) {
                    exp += Bl_ess.getExp();
                }
                if (Bl_ess.getBamage() != 0) {
                    bamage += Bl_ess.getBamage();
                }
                if (Bl_ess.getHit() != 0) {
                    hit += Bl_ess.getHit();
                }
                if (Bl_ess.getDmg() != 0) {
                    dmg += Bl_ess.getDmg();
                }
                if (Bl_ess.getBowHit() != 0) {
                    Bowhit += Bl_ess.getBowHit();
                }
                if (Bl_ess.getBowDmg() != 0) {
                    Bowdmg += Bl_ess.getBowDmg();
                }
            }
            int enchant = eq.getEnchantLevel() - eq.getItem().get_safeenchant();
            L1NewEnchantSystem Enchant = NewEnchantSystem.get(item.getItemId(), enchant);
            if (Enchant != null) {
                if (Enchant.getHp() != 0) {
                    addhp += Enchant.getHp();
                }
                if (Enchant.getMp() != 0) {
                    addmp += Enchant.getMp();
                }
                if (Enchant.getStr() != 0) {
                    get_addstr += Enchant.getStr();
                }
                if (Enchant.getDex() != 0) {
                    get_adddex += Enchant.getDex();
                }
                if (Enchant.getCon() != 0) {
                    get_addcon += Enchant.getCon();
                }
                if (Enchant.getWis() != 0) {
                    get_addwis += Enchant.getWis();
                }
                if (Enchant.getInt() != 0) {
                    get_addint += Enchant.getInt();
                }
                if (Enchant.getCha() != 0) {
                    get_addcha += Enchant.getCha();
                }
                if (Enchant.getMr() != 0) {
                    addMr += Enchant.getMr();
                }
                if (Enchant.getSp() != 0) {
                    addSp += Enchant.getSp();
                }
                if (Enchant.getFire() != 0) {
                    fire += Enchant.getFire();
                }
                if (Enchant.getWater() != 0) {
                    water += Enchant.getWater();
                }
                if (Enchant.getWind() != 0) {
                    wind += Enchant.getWind();
                }
                if (Enchant.getEarth() != 0) {
                    earth += Enchant.getEarth();
                }
                if (Enchant.getFreeze() != 0) {
                    freeze += Enchant.getFreeze();
                }
                if (Enchant.getStone() != 0) {
                    stone += Enchant.getStone();
                }
                if (Enchant.getSleep() != 0) {
                    sleep += Enchant.getSleep();
                }
                if (Enchant.getBlind() != 0) {
                    blind += Enchant.getBlind();
                }
                if (Enchant.getStun() != 0) {
                    stun += Enchant.getStun();
                }
                if (Enchant.getSustain() != 0) {
                    sustain += Enchant.getSustain();
                }
                if (Enchant.getExp() != 0) {
                    exp += Enchant.getExp();
                }
                if (Enchant.getBamage() != 0) {
                    bamage += Enchant.getBamage();
                }
                if (Enchant.getHit() != 0) {
                    hit += Enchant.getHit();
                }
                if (Enchant.getDmg() != 0) {
                    dmg += Enchant.getDmg();
                }
                if (Enchant.getBowHit() != 0) {
                    Bowhit += Enchant.getBowHit();
                }
                if (Enchant.getBowDmg() != 0) {
                    Bowdmg += Enchant.getBowDmg();
                }
            }
            L1NewEnchantDystem Encdant = NewEnchantDystem.get(item.getItemId(), enchant);
            if (Encdant != null) {
                if (Encdant.getHp() != 0) {
                    addhp += Encdant.getHp();
                }
                if (Encdant.getMp() != 0) {
                    addmp += Encdant.getMp();
                }
                if (Encdant.getStr() != 0) {
                    get_addstr += Encdant.getStr();
                }
                if (Encdant.getDex() != 0) {
                    get_adddex += Encdant.getDex();
                }
                if (Encdant.getCon() != 0) {
                    get_addcon += Encdant.getCon();
                }
                if (Encdant.getWis() != 0) {
                    get_addwis += Encdant.getWis();
                }
                if (Encdant.getInt() != 0) {
                    get_addint += Encdant.getInt();
                }
                if (Encdant.getCha() != 0) {
                    get_addcha += Encdant.getCha();
                }
                if (Encdant.getMr() != 0) {
                    addMr += Encdant.getMr();
                }
                if (Encdant.getSp() != 0) {
                    addSp += Encdant.getSp();
                }
                if (Encdant.getFire() != 0) {
                    fire += Encdant.getFire();
                }
                if (Encdant.getWater() != 0) {
                    water += Encdant.getWater();
                }
                if (Encdant.getWind() != 0) {
                    wind += Encdant.getWind();
                }
                if (Encdant.getEarth() != 0) {
                    earth += Encdant.getEarth();
                }
                if (Encdant.getFreeze() != 0) {
                    freeze += Encdant.getFreeze();
                }
                if (Encdant.getStone() != 0) {
                    stone += Encdant.getStone();
                }
                if (Encdant.getSleep() != 0) {
                    sleep += Encdant.getSleep();
                }
                if (Encdant.getBlind() != 0) {
                    blind += Encdant.getBlind();
                }
                if (Encdant.getStun() != 0) {
                    stun += Encdant.getStun();
                }
                if (Encdant.getSustain() != 0) {
                    sustain += Encdant.getSustain();
                }
                if (Encdant.getExp() != 0) {
                    exp += Encdant.getExp();
                }
                if (Encdant.getBamage() != 0) {
                    bamage += Encdant.getBamage();
                }
                if (Encdant.getHit() != 0) {
                    hit += Encdant.getHit();
                }
                if (Encdant.getDmg() != 0) {
                    dmg += Encdant.getDmg();
                }
                if (Encdant.getBowHit() != 0) {
                    Bowhit += Encdant.getBowHit();
                }
                if (Encdant.getBowDmg() != 0) {
                    Bowdmg += Encdant.getBowDmg();
                }
            }
            this._owner.addHitModifierByArmor(hit);
            this._owner.addDmgModifierByArmor(dmg);
            this._owner.addBowHitModifierByArmor(Bowhit);
            this._owner.addBowDmgModifierByArmor(Bowdmg);
            if (addhp != 0) {
                this._owner.addMaxHp(addhp);
            }
            if (addmp != 0) {
                this._owner.addMaxMp(addmp);
            }
            this._owner.addStr(get_addstr);
            this._owner.addDex(get_adddex);
            this._owner.addCon(get_addcon);
            this._owner.addWis(get_addwis);
            if (get_addwis != 0) {
                this._owner.resetBaseMr();
            }
            this._owner.addInt(get_addint);
            this._owner.addCha(get_addcha);
            this._owner.addWeightReduction(weight);
            this._owner.addEarth(earth);
            this._owner.addWind(wind);
            this._owner.addWater(water);
            this._owner.addFire(fire);
            this._owner.addRegistStun(stun);
            this._owner.addRegistStone(stone);
            this._owner.addRegistSleep(sleep);
            this._owner.add_regist_freeze(freeze);
            this._owner.addRegistSustain(sustain);
            this._owner.addRegistBlind(blind);
            this._owner.addDamageReductionByArmor(bamage);
            this._owner.setExpPoint(this._owner.getExpPoint() + exp);
            if (item.getItemId() == 20236 && this._owner.isElf()) {
                addMr += 5;
            }
            if (addMr != 0) {
                this._owner.addMr(addMr);
                this._owner.sendPackets(new S_SPMR(this._owner));
            }
            if (addSp != 0) {
                this._owner.addSp(addSp);
                this._owner.sendPackets(new S_SPMR(this._owner));
            }
            if (item.isHasteItem()) {
                this._owner.addHasteItemEquipped(1);
                this._owner.removeHasteSkillEffect();
                if (this._owner.getMoveSpeed() != 1) {
                    this._owner.setMoveSpeed(1);
                    this._owner.sendPackets(new S_SkillHaste(this._owner.getId(), 1, -1));
                    this._owner.broadcastPacketAll(new S_SkillHaste(this._owner.getId(), 1, 0));
                }
            }
            switch (item.getType2()) {
                case 1:
                    setWeapon(eq);
                    ItemClass.get().item_weapon(true, this._owner, eq);
                    return;
                case 2:
                    setArmor(eq);
                    setMagic(eq);
                    ItemClass.get().item_armor(true, this._owner, eq);
                    return;
                default:
                    return;
            }
        }
    }

    public void remove(L1ItemInstance eq) {
        int extraMr;
        L1Item item = eq.getItem();
        if (item.getType2() != 0 || item.getCard() != null) {
            if (this._owner.getTempCharGfx() == (item.getCard() == null ? 0 : item.getCard().getExtraPoly())) {
                L1PolyMorph.undoPoly(this._owner);
            }
            int hit = item.getHitModifierByArmor() + (item.getCard() == null ? 0 : item.getCard().getExtraHit() + item.getCard().getWakeHit());
            int dmg = item.getDmgModifierByArmor() + (item.getCard() == null ? 0 : item.getCard().getExtraDmg() + item.getCard().getWakeDmg());
            int Bowhit = item.getBowHitModifierByArmor() + (item.getCard() == null ? 0 : item.getCard().getExtraBowhit() + item.getCard().getWakeBowhit());
            int Bowdmg = item.getBowDmgModifierByArmor() + (item.getCard() == null ? 0 : item.getCard().getExtraBowdmg() + item.getCard().getWakeBowdmg());
            int addhp = item.get_addhp() + (item.getCard() == null ? 0 : item.getCard().getExtraHp() + item.getCard().getWakeHp());
            int addmp = item.get_addmp() + (item.getCard() == null ? 0 : item.getCard().getExtraMp() + item.getCard().getWakeMp());
            int get_addstr = item.get_addstr() + (item.getCard() == null ? 0 : item.getCard().getExtraStr() + item.getCard().getWakeStr());
            int get_adddex = item.get_adddex() + (item.getCard() == null ? 0 : item.getCard().getExtraDex() + item.getCard().getWakeDex());
            int get_addcon = item.get_addcon() + (item.getCard() == null ? 0 : item.getCard().getExtraCon() + item.getCard().getWakeCon());
            int get_addwis = item.get_addwis() + (item.getCard() == null ? 0 : item.getCard().getExtraWis() + item.getCard().getWakeWis());
            int get_addint = item.get_addint() + (item.getCard() == null ? 0 : item.getCard().getExtraInt() + item.getCard().getWakeInt());
            int get_addcha = item.get_addcha() + (item.getCard() == null ? 0 : item.getCard().getExtraCha() + item.getCard().getWakeCha());
            int exp = item.getExpPoint() + (item.getCard() == null ? 0 : item.getCard().getExtraExp() + item.getCard().getWakeExp());
            int weight = item.getCard() == null ? 0 : item.getCard().getExtraWeight() + item.getCard().getWakeWeight();
            int earth = item.getCard() == null ? 0 : item.getCard().getExtraEarth() + item.getCard().getWakeEarth();
            int wind = item.getCard() == null ? 0 : item.getCard().getExtraWind() + item.getCard().getWakeWind();
            int water = item.getCard() == null ? 0 : item.getCard().getExtraWater() + item.getCard().getWakeWater();
            int fire = item.getCard() == null ? 0 : item.getCard().getExtraFire() + item.getCard().getWakeFire();
            int stun = item.getCard() == null ? 0 : item.getCard().getExtraStun() + item.getCard().getWakeStun();
            int stone = item.getCard() == null ? 0 : item.getCard().getExtraStone() + item.getCard().getWakeStone();
            int sleep = item.getCard() == null ? 0 : item.getCard().getExtraSleep() + item.getCard().getWakeSleep();
            int freeze = item.getCard() == null ? 0 : item.getCard().getExtrafreeze() + item.getCard().getWakefreeze();
            int sustain = item.getCard() == null ? 0 : item.getCard().getExtraSustain() + item.getCard().getWakeSustain();
            int blind = item.getCard() == null ? 0 : item.getCard().getExtraBlind() + item.getCard().getWakeBlind();
            int bamage = item.getCard() == null ? 0 : item.getCard().getExtraBamage() + item.getCard().getWakeBamage();
            int addSp = item.get_addsp() + (item.getCard() == null ? 0 : item.getCard().getExtraSp() + item.getCard().getWakeSp());
            int mr = eq.getMr();
            if (item.getCard() == null) {
                extraMr = 0;
            } else {
                extraMr = item.getCard().getExtraMr() + item.getCard().getWakeMr();
            }
            int addMr = mr + extraMr;
            L1BlessSystem Bless = BlessSystem.get(item.getItemId());
            if (Bless != null) {
                if (Bless.getHp() != 0) {
                    addhp += Bless.getHp();
                }
                if (Bless.getMp() != 0) {
                    addmp += Bless.getMp();
                }
                if (Bless.getStr() != 0) {
                    get_addstr += Bless.getStr();
                }
                if (Bless.getDex() != 0) {
                    get_adddex += Bless.getDex();
                }
                if (Bless.getCon() != 0) {
                    get_addcon += Bless.getCon();
                }
                if (Bless.getWis() != 0) {
                    get_addwis += Bless.getWis();
                }
                if (Bless.getInt() != 0) {
                    get_addint += Bless.getInt();
                }
                if (Bless.getCha() != 0) {
                    get_addcha += Bless.getCha();
                }
                if (Bless.getMr() != 0) {
                    addMr += Bless.getMr();
                }
                if (Bless.getSp() != 0) {
                    addSp += Bless.getSp();
                }
                if (Bless.getFire() != 0) {
                    fire += Bless.getFire();
                }
                if (Bless.getWater() != 0) {
                    water += Bless.getWater();
                }
                if (Bless.getWind() != 0) {
                    wind += Bless.getWind();
                }
                if (Bless.getEarth() != 0) {
                    earth += Bless.getEarth();
                }
                if (Bless.getFreeze() != 0) {
                    freeze += Bless.getFreeze();
                }
                if (Bless.getStone() != 0) {
                    stone += Bless.getStone();
                }
                if (Bless.getSleep() != 0) {
                    sleep += Bless.getSleep();
                }
                if (Bless.getBlind() != 0) {
                    blind += Bless.getBlind();
                }
                if (Bless.getStun() != 0) {
                    stun += Bless.getStun();
                }
                if (Bless.getSustain() != 0) {
                    sustain += Bless.getSustain();
                }
                if (Bless.getExp() != 0) {
                    exp += Bless.getExp();
                }
                if (Bless.getBamage() != 0) {
                    bamage += Bless.getBamage();
                }
                if (Bless.getHit() != 0) {
                    hit += Bless.getHit();
                }
                if (Bless.getDmg() != 0) {
                    dmg += Bless.getDmg();
                }
                if (Bless.getBowHit() != 0) {
                    Bowhit += Bless.getBowHit();
                }
                if (Bless.getBowDmg() != 0) {
                    Bowdmg += Bless.getBowDmg();
                }
            }
            L1BlessDystem Bl_ess = BlessDystem.get(item.getItemId());
            if (Bl_ess != null) {
                if (Bl_ess.getHp() != 0) {
                    addhp += Bl_ess.getHp();
                }
                if (Bl_ess.getMp() != 0) {
                    addmp += Bl_ess.getMp();
                }
                if (Bl_ess.getStr() != 0) {
                    get_addstr += Bl_ess.getStr();
                }
                if (Bl_ess.getDex() != 0) {
                    get_adddex += Bl_ess.getDex();
                }
                if (Bl_ess.getCon() != 0) {
                    get_addcon += Bl_ess.getCon();
                }
                if (Bl_ess.getWis() != 0) {
                    get_addwis += Bl_ess.getWis();
                }
                if (Bl_ess.getInt() != 0) {
                    get_addint += Bl_ess.getInt();
                }
                if (Bl_ess.getCha() != 0) {
                    get_addcha += Bl_ess.getCha();
                }
                if (Bl_ess.getMr() != 0) {
                    addMr += Bl_ess.getMr();
                }
                if (Bl_ess.getSp() != 0) {
                    addSp += Bl_ess.getSp();
                }
                if (Bl_ess.getFire() != 0) {
                    fire += Bl_ess.getFire();
                }
                if (Bl_ess.getWater() != 0) {
                    water += Bl_ess.getWater();
                }
                if (Bl_ess.getWind() != 0) {
                    wind += Bl_ess.getWind();
                }
                if (Bl_ess.getEarth() != 0) {
                    earth += Bl_ess.getEarth();
                }
                if (Bl_ess.getFreeze() != 0) {
                    freeze += Bl_ess.getFreeze();
                }
                if (Bl_ess.getStone() != 0) {
                    stone += Bl_ess.getStone();
                }
                if (Bl_ess.getSleep() != 0) {
                    sleep += Bl_ess.getSleep();
                }
                if (Bl_ess.getBlind() != 0) {
                    blind += Bl_ess.getBlind();
                }
                if (Bl_ess.getStun() != 0) {
                    stun += Bl_ess.getStun();
                }
                if (Bl_ess.getSustain() != 0) {
                    sustain += Bl_ess.getSustain();
                }
                if (Bl_ess.getExp() != 0) {
                    exp += Bl_ess.getExp();
                }
                if (Bl_ess.getBamage() != 0) {
                    bamage += Bl_ess.getBamage();
                }
                if (Bl_ess.getHit() != 0) {
                    hit += Bl_ess.getHit();
                }
                if (Bl_ess.getDmg() != 0) {
                    dmg += Bl_ess.getDmg();
                }
                if (Bl_ess.getBowHit() != 0) {
                    Bowhit += Bl_ess.getBowHit();
                }
                if (Bl_ess.getBowDmg() != 0) {
                    Bowdmg += Bl_ess.getBowDmg();
                }
            }
            int enchant = eq.getEnchantLevel() - eq.getItem().get_safeenchant();
            L1NewEnchantSystem Enchant = NewEnchantSystem.get(item.getItemId(), enchant);
            if (Enchant != null) {
                if (Enchant.getHp() != 0) {
                    addhp += Enchant.getHp();
                }
                if (Enchant.getMp() != 0) {
                    addmp += Enchant.getMp();
                }
                if (Enchant.getStr() != 0) {
                    get_addstr += Enchant.getStr();
                }
                if (Enchant.getDex() != 0) {
                    get_adddex += Enchant.getDex();
                }
                if (Enchant.getCon() != 0) {
                    get_addcon += Enchant.getCon();
                }
                if (Enchant.getWis() != 0) {
                    get_addwis += Enchant.getWis();
                }
                if (Enchant.getInt() != 0) {
                    get_addint += Enchant.getInt();
                }
                if (Enchant.getCha() != 0) {
                    get_addcha += Enchant.getCha();
                }
                if (Enchant.getMr() != 0) {
                    addMr += Enchant.getMr();
                }
                if (Enchant.getSp() != 0) {
                    addSp += Enchant.getSp();
                }
                if (Enchant.getFire() != 0) {
                    fire += Enchant.getFire();
                }
                if (Enchant.getWater() != 0) {
                    water += Enchant.getWater();
                }
                if (Enchant.getWind() != 0) {
                    wind += Enchant.getWind();
                }
                if (Enchant.getEarth() != 0) {
                    earth += Enchant.getEarth();
                }
                if (Enchant.getFreeze() != 0) {
                    freeze += Enchant.getFreeze();
                }
                if (Enchant.getStone() != 0) {
                    stone += Enchant.getStone();
                }
                if (Enchant.getSleep() != 0) {
                    sleep += Enchant.getSleep();
                }
                if (Enchant.getBlind() != 0) {
                    blind += Enchant.getBlind();
                }
                if (Enchant.getStun() != 0) {
                    stun += Enchant.getStun();
                }
                if (Enchant.getSustain() != 0) {
                    sustain += Enchant.getSustain();
                }
                if (Enchant.getExp() != 0) {
                    exp += Enchant.getExp();
                }
                if (Enchant.getBamage() != 0) {
                    bamage += Enchant.getBamage();
                }
                if (Enchant.getHit() != 0) {
                    hit += Enchant.getHit();
                }
                if (Enchant.getDmg() != 0) {
                    dmg += Enchant.getDmg();
                }
                if (Enchant.getBowHit() != 0) {
                    Bowhit += Enchant.getBowHit();
                }
                if (Enchant.getBowDmg() != 0) {
                    Bowdmg += Enchant.getBowDmg();
                }
            }
            L1NewEnchantDystem Encdant = NewEnchantDystem.get(item.getItemId(), enchant);
            if (Encdant != null) {
                if (Encdant.getHp() != 0) {
                    addhp += Encdant.getHp();
                }
                if (Encdant.getMp() != 0) {
                    addmp += Encdant.getMp();
                }
                if (Encdant.getStr() != 0) {
                    get_addstr += Encdant.getStr();
                }
                if (Encdant.getDex() != 0) {
                    get_adddex += Encdant.getDex();
                }
                if (Encdant.getCon() != 0) {
                    get_addcon += Encdant.getCon();
                }
                if (Encdant.getWis() != 0) {
                    get_addwis += Encdant.getWis();
                }
                if (Encdant.getInt() != 0) {
                    get_addint += Encdant.getInt();
                }
                if (Encdant.getCha() != 0) {
                    get_addcha += Encdant.getCha();
                }
                if (Encdant.getMr() != 0) {
                    addMr += Encdant.getMr();
                }
                if (Encdant.getSp() != 0) {
                    addSp += Encdant.getSp();
                }
                if (Encdant.getFire() != 0) {
                    fire += Encdant.getFire();
                }
                if (Encdant.getWater() != 0) {
                    water += Encdant.getWater();
                }
                if (Encdant.getWind() != 0) {
                    wind += Encdant.getWind();
                }
                if (Encdant.getEarth() != 0) {
                    earth += Encdant.getEarth();
                }
                if (Encdant.getFreeze() != 0) {
                    freeze += Encdant.getFreeze();
                }
                if (Encdant.getStone() != 0) {
                    stone += Encdant.getStone();
                }
                if (Encdant.getSleep() != 0) {
                    sleep += Encdant.getSleep();
                }
                if (Encdant.getBlind() != 0) {
                    blind += Encdant.getBlind();
                }
                if (Encdant.getStun() != 0) {
                    stun += Encdant.getStun();
                }
                if (Encdant.getSustain() != 0) {
                    sustain += Encdant.getSustain();
                }
                if (Encdant.getExp() != 0) {
                    exp += Encdant.getExp();
                }
                if (Encdant.getBamage() != 0) {
                    bamage += Encdant.getBamage();
                }
                if (Encdant.getHit() != 0) {
                    hit += Encdant.getHit();
                }
                if (Encdant.getDmg() != 0) {
                    dmg += Encdant.getDmg();
                }
                if (Encdant.getBowHit() != 0) {
                    Bowhit += Encdant.getBowHit();
                }
                if (Encdant.getBowDmg() != 0) {
                    Bowdmg += Encdant.getBowDmg();
                }
            }
            this._owner.addHitModifierByArmor(-hit);
            this._owner.addDmgModifierByArmor(-dmg);
            this._owner.addBowHitModifierByArmor(-Bowhit);
            this._owner.addBowDmgModifierByArmor(-Bowdmg);
            if (addmp != 0) {
                this._owner.addMaxMp(-addmp);
            }
            if (addhp != 0) {
                this._owner.addMaxHp(-addhp);
            }
            this._owner.addStr((byte) (-get_addstr));
            this._owner.addDex((byte) (-get_adddex));
            this._owner.addCon((byte) (-get_addcon));
            this._owner.addWis((byte) (-get_addwis));
            if (get_addwis != 0) {
                this._owner.resetBaseMr();
            }
            this._owner.addInt((byte) (-get_addint));
            this._owner.addCha((byte) (-get_addcha));
            this._owner.addWeightReduction(-weight);
            this._owner.addEarth(-earth);
            this._owner.addWind(-wind);
            this._owner.addWater(-water);
            this._owner.addFire(-fire);
            this._owner.addRegistStun(-stun);
            this._owner.addRegistStone(-stone);
            this._owner.addRegistSleep(-sleep);
            this._owner.add_regist_freeze(-freeze);
            this._owner.addRegistSustain(-sustain);
            this._owner.addRegistBlind(-blind);
            this._owner.addDamageReductionByArmor(-bamage);
            this._owner.setExpPoint(this._owner.getExpPoint() - exp);
            if (item.getItemId() == 20236 && this._owner.isElf()) {
                addMr += 5;
            }
            if (addMr != 0) {
                this._owner.addMr(-addMr);
                this._owner.sendPackets(new S_SPMR(this._owner));
            }
            if (addSp != 0) {
                this._owner.addSp(-addSp);
                this._owner.sendPackets(new S_SPMR(this._owner));
            }
            if (item.isHasteItem()) {
                this._owner.addHasteItemEquipped(-1);
                if (this._owner.getHasteItemEquipped() == 0) {
                    this._owner.setMoveSpeed(0);
                    this._owner.sendPacketsAll(new S_SkillHaste(this._owner.getId(), 0, 0));
                }
            }
            switch (item.getType2()) {
                case 1:
                    removeWeapon(eq);
                    ItemClass.get().item_weapon(false, this._owner, eq);
                    return;
                case 2:
                    removeMagic(this._owner.getId(), eq);
                    removeArmor(eq);
                    ItemClass.get().item_armor(false, this._owner, eq);
                    return;
                default:
                    return;
            }
        }
    }

    private void setMagic(L1ItemInstance item) {
        switch (item.getItemId()) {
            case 20008:
                if (!this._owner.isSkillMastery(43)) {
                    this._owner.sendPackets(new S_AddSkill(this._owner, 43));
                    return;
                }
                return;
            case 20013:
                if (!this._owner.isSkillMastery(26)) {
                    this._owner.sendPackets(new S_AddSkill(this._owner, 26));
                }
                if (!this._owner.isSkillMastery(43)) {
                    this._owner.sendPackets(new S_AddSkill(this._owner, 43));
                    return;
                }
                return;
            case 20014:
                if (!this._owner.isSkillMastery(1)) {
                    this._owner.sendPackets(new S_AddSkill(this._owner, 1));
                }
                if (!this._owner.isSkillMastery(19)) {
                    this._owner.sendPackets(new S_AddSkill(this._owner, 19));
                    return;
                }
                return;
            case 20015:
                if (!this._owner.isSkillMastery(12)) {
                    this._owner.sendPackets(new S_AddSkill(this._owner, 12));
                }
                if (!this._owner.isSkillMastery(13)) {
                    this._owner.sendPackets(new S_AddSkill(this._owner, 13));
                }
                if (!this._owner.isSkillMastery(42)) {
                    this._owner.sendPackets(new S_AddSkill(this._owner, 42));
                    return;
                }
                return;
            case 20023:
                if (!this._owner.isSkillMastery(54)) {
                    this._owner.sendPackets(new S_AddSkill(this._owner, 54));
                    return;
                }
                return;
            case 20062:
            case 20077:
            case 120077:
                if (!this._owner.hasSkillEffect(60)) {
                    this._owner.killSkillEffectTimer(97);
                    this._owner.setSkillEffect(60, 0);
                    this._owner.sendPackets(new S_Invis(this._owner.getId(), 1));
                    this._owner.broadcastPacketAll(new S_RemoveObject(this._owner));
                    return;
                }
                return;
            case 20281:
                this._owner.sendPackets(new S_Ability(2, true));
                return;
            case 20284:
                this._owner.sendPackets(new S_Ability(5, true));
                return;
            case 20288:
                this._owner.sendPackets(new S_Ability(1, true));
                return;
            case 20383:
                if (item.getChargeCount() != 0) {
                    item.setChargeCount(item.getChargeCount() - 1);
                    this._owner.getInventory().updateItem(item, 128);
                }
                if (this._owner.hasSkillEffect(L1SkillId.STATUS_BRAVE)) {
                    this._owner.killSkillEffectTimer(L1SkillId.STATUS_BRAVE);
                    this._owner.sendPacketsAll(new S_SkillBrave(this._owner.getId(), 0, 0));
                    this._owner.setBraveSpeed(0);
                    return;
                }
                return;
            default:
                return;
        }
    }

    private void removeMagic(int objectId, L1ItemInstance item) {
        switch (item.getItemId()) {
            case 20008:
                if (!CharSkillReading.get().spellCheck(objectId, 43)) {
                    this._owner.sendPackets(new S_DelSkill(this._owner, 43));
                    return;
                }
                return;
            case 20013:
                if (!CharSkillReading.get().spellCheck(objectId, 26)) {
                    this._owner.sendPackets(new S_DelSkill(this._owner, 26));
                }
                if (!CharSkillReading.get().spellCheck(objectId, 43)) {
                    this._owner.sendPackets(new S_DelSkill(this._owner, 43));
                    return;
                }
                return;
            case 20014:
                if (!CharSkillReading.get().spellCheck(objectId, 1)) {
                    this._owner.sendPackets(new S_DelSkill(this._owner, 1));
                }
                if (!CharSkillReading.get().spellCheck(objectId, 19)) {
                    this._owner.sendPackets(new S_DelSkill(this._owner, 19));
                    return;
                }
                return;
            case 20015:
                if (!CharSkillReading.get().spellCheck(objectId, 12)) {
                    this._owner.sendPackets(new S_DelSkill(this._owner, 12));
                }
                if (!CharSkillReading.get().spellCheck(objectId, 13)) {
                    this._owner.sendPackets(new S_DelSkill(this._owner, 13));
                }
                if (!CharSkillReading.get().spellCheck(objectId, 42)) {
                    this._owner.sendPackets(new S_DelSkill(this._owner, 42));
                    return;
                }
                return;
            case 20023:
                if (!CharSkillReading.get().spellCheck(objectId, 54)) {
                    this._owner.sendPackets(new S_DelSkill(this._owner, 54));
                    return;
                }
                return;
            case 20062:
            case 20077:
            case 120077:
                this._owner.delInvis();
                return;
            case 20281:
            case 20383:
            default:
                return;
            case 20284:
                this._owner.sendPackets(new S_Ability(5, false));
                return;
            case 20288:
                this._owner.sendPackets(new S_Ability(1, false));
                return;
        }
    }
}
