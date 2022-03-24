package com.lineage.data.item_armor.set;

import com.lineage.server.datatables.ArmorSetTable;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.templates.L1ArmorSets;
import java.util.HashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class ArmorSet {
    private static final HashMap<Integer, ArmorSet> _allSet = new HashMap<>();
    private static final Log _log = LogFactory.getLog(ArmorSet.class);

    public abstract void cancelEffect(L1PcInstance l1PcInstance);

    public abstract int[] get_ids();

    public abstract int[] get_mode();

    public abstract void giveEffect(L1PcInstance l1PcInstance);

    public abstract boolean isEquippedRingOfArmorSet(L1PcInstance l1PcInstance);

    public abstract boolean isPartOfSet(int i);

    public abstract boolean isValid(L1PcInstance l1PcInstance);

    public static HashMap<Integer, ArmorSet> getAllSet() {
        return _allSet;
    }

    public static void load() {
        try {
            L1ArmorSets[] allList = ArmorSetTable.get().getAllList();
            for (L1ArmorSets armorSets : allList) {
                int id = armorSets.getId();
                int[] gfxs = null;
                if (armorSets.get_gfxs() != null) {
                    gfxs = armorSets.get_gfxs();
                }
                ArmorSetImpl value = new ArmorSetImpl(id, getArray(id, armorSets.getSets()), gfxs);
                if (armorSets.getPolyId() != -1) {
                    value.addEffect(new EffectPolymorph(armorSets.getPolyId()));
                }
                if (armorSets.getAc() != 0) {
                    value.addEffect(new EffectAc(armorSets.getAc()));
                }
                if (armorSets.getMr() != 0) {
                    value.addEffect(new EffectMr(armorSets.getMr()));
                }
                if (armorSets.getHp() != 0) {
                    value.addEffect(new EffectHp(armorSets.getHp()));
                }
                if (armorSets.getHpr() != 0) {
                    value.addEffect(new EffectHpR(armorSets.getHpr()));
                }
                if (armorSets.getMp() != 0) {
                    value.addEffect(new EffectMp(armorSets.getMp()));
                }
                if (armorSets.getMpr() != 0) {
                    value.addEffect(new EffectMpR(armorSets.getMpr()));
                }
                if (armorSets.getDefenseWater() != 0) {
                    value.addEffect(new EffectDefenseWater(armorSets.getDefenseWater()));
                }
                if (armorSets.getDefenseWind() != 0) {
                    value.addEffect(new EffectDefenseWind(armorSets.getDefenseWind()));
                }
                if (armorSets.getDefenseFire() != 0) {
                    value.addEffect(new EffectDefenseFire(armorSets.getDefenseFire()));
                }
                if (armorSets.getDefenseEarth() != 0) {
                    value.addEffect(new EffectDefenseEarth(armorSets.getDefenseEarth()));
                }
                if (armorSets.get_regist_stun() != 0) {
                    value.addEffect(new EffectRegist_Stun(armorSets.get_regist_stun()));
                }
                if (armorSets.get_regist_stone() != 0) {
                    value.addEffect(new EffectRegist_Stone(armorSets.get_regist_stone()));
                }
                if (armorSets.get_regist_sleep() != 0) {
                    value.addEffect(new EffectRegist_Sleep(armorSets.get_regist_sleep()));
                }
                if (armorSets.get_regist_freeze() != 0) {
                    value.addEffect(new EffectRegist_Freeze(armorSets.get_regist_freeze()));
                }
                if (armorSets.get_regist_sustain() != 0) {
                    value.addEffect(new EffectRegist_Sustain(armorSets.get_regist_sustain()));
                }
                if (armorSets.get_regist_blind() != 0) {
                    value.addEffect(new EffectRegist_Blind(armorSets.get_regist_blind()));
                }
                if (armorSets.getStr() != 0) {
                    value.addEffect(new EffectStat_Str(armorSets.getStr()));
                }
                if (armorSets.getDex() != 0) {
                    value.addEffect(new EffectStat_Dex(armorSets.getDex()));
                }
                if (armorSets.getCon() != 0) {
                    value.addEffect(new EffectStat_Con(armorSets.getCon()));
                }
                if (armorSets.getWis() != 0) {
                    value.addEffect(new EffectStat_Wis(armorSets.getWis()));
                }
                if (armorSets.getCha() != 0) {
                    value.addEffect(new EffectStat_Cha(armorSets.getCha()));
                }
                if (armorSets.getIntl() != 0) {
                    value.addEffect(new EffectStat_Int(armorSets.getIntl()));
                }
                if (armorSets.get_modifier_dmg() != 0) {
                    value.addEffect(new Effect_Modifier_dmg(armorSets.get_modifier_dmg()));
                }
                if (armorSets.get_reduction_dmg() != 0) {
                    value.addEffect(new Effect_Reduction_dmg(armorSets.get_reduction_dmg()));
                }
                if (armorSets.get_magic_modifier_dmg() != 0) {
                    value.addEffect(new Effect_Magic_modifier_dmg(armorSets.get_magic_modifier_dmg()));
                }
                if (armorSets.get_magic_reduction_dmg() != 0) {
                    value.addEffect(new Effect_Magic_reduction_dmg(armorSets.get_magic_reduction_dmg()));
                }
                if (armorSets.get_bow_modifier_dmg() != 0) {
                    value.addEffect(new Effect_Bow_modifier_dmg(armorSets.get_bow_modifier_dmg()));
                }
                _allSet.put(Integer.valueOf(armorSets.getId()), value);
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            ItemTable.get().se_mode();
        }
    }

    private static int[] getArray(int id, String s) {
        String[] clientStrAry = s.split(",");
        int[] array = new int[clientStrAry.length];
        for (int i = 0; i < clientStrAry.length; i++) {
            try {
                array[i] = Integer.parseInt(clientStrAry[i]);
            } catch (Exception e) {
                _log.error("編號:" + id + " 套件設置錯誤!!檢查資料庫!!", e);
            }
        }
        return array;
    }
}
