package com.lineage.data.item_armor.set;

import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1PcInventory;
import com.lineage.server.serverpackets.S_SkillSound;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ArmorSetImpl extends ArmorSet {
    private static final Log _log = LogFactory.getLog(ArmorSetImpl.class);
    private final ArrayList<ArmorSetEffect> _effects = new ArrayList<>();
    private final int[] _gfxids;
    private final int _id;
    private final int[] _ids;

    protected ArmorSetImpl(int id, int[] ids, int[] gfxids) {
        this._id = id;
        this._ids = ids;
        this._gfxids = gfxids;
    }

    public int get_id() {
        return this._id;
    }

    @Override // com.lineage.data.item_armor.set.ArmorSet
    public int[] get_ids() {
        return this._ids;
    }

    public void addEffect(ArmorSetEffect effect) {
        this._effects.add(effect);
    }

    public void removeEffect(ArmorSetEffect effect) {
        this._effects.remove(effect);
    }

    @Override // com.lineage.data.item_armor.set.ArmorSet
    public int[] get_mode() {
        int[] mode = new int[21];
        Iterator<ArmorSetEffect> it = this._effects.iterator();
        while (it.hasNext()) {
            ArmorSetEffect set = it.next();
            if (set instanceof EffectStat_Str) {
                mode[0] = set.get_mode();
            }
            if (set instanceof EffectStat_Dex) {
                mode[1] = set.get_mode();
            }
            if (set instanceof EffectStat_Con) {
                mode[2] = set.get_mode();
            }
            if (set instanceof EffectStat_Wis) {
                mode[3] = set.get_mode();
            }
            if (set instanceof EffectStat_Int) {
                mode[4] = set.get_mode();
            }
            if (set instanceof EffectStat_Cha) {
                mode[5] = set.get_mode();
            }
            if (set instanceof EffectHp) {
                mode[6] = set.get_mode();
            }
            if (set instanceof EffectMp) {
                mode[7] = set.get_mode();
            }
            mode[8] = 0;
            mode[9] = 0;
            if (set instanceof EffectMr) {
                mode[10] = set.get_mode();
            }
            if (set instanceof EffectDefenseFire) {
                mode[11] = set.get_mode();
            }
            if (set instanceof EffectDefenseWater) {
                mode[12] = set.get_mode();
            }
            if (set instanceof EffectDefenseWind) {
                mode[13] = set.get_mode();
            }
            if (set instanceof EffectDefenseEarth) {
                mode[14] = set.get_mode();
            }
            if (set instanceof EffectRegist_Freeze) {
                mode[15] = set.get_mode();
            }
            if (set instanceof EffectRegist_Stone) {
                mode[16] = set.get_mode();
            }
            if (set instanceof EffectRegist_Sleep) {
                mode[17] = set.get_mode();
            }
            if (set instanceof EffectRegist_Blind) {
                mode[18] = set.get_mode();
            }
            if (set instanceof EffectRegist_Stun) {
                mode[19] = set.get_mode();
            }
            if (set instanceof EffectRegist_Sustain) {
                mode[20] = set.get_mode();
            }
        }
        return mode;
    }

    @Override // com.lineage.data.item_armor.set.ArmorSet
    public void giveEffect(L1PcInstance pc) {
        try {
            Iterator<ArmorSetEffect> it = this._effects.iterator();
            while (it.hasNext()) {
                it.next().giveEffect(pc);
            }
            if (this._gfxids != null) {
                for (int gfx : this._gfxids) {
                    pc.sendPacketsX8(new S_SkillSound(pc.getId(), gfx));
                }
            }
        } catch (Exception ex) {
            _log.error(ex.getLocalizedMessage(), ex);
        }
    }

    @Override // com.lineage.data.item_armor.set.ArmorSet
    public void cancelEffect(L1PcInstance pc) {
        try {
            Iterator<ArmorSetEffect> it = this._effects.iterator();
            while (it.hasNext()) {
                it.next().cancelEffect(pc);
            }
        } catch (Exception ex) {
            _log.error(ex.getLocalizedMessage(), ex);
        }
    }

    @Override // com.lineage.data.item_armor.set.ArmorSet
    public final boolean isValid(L1PcInstance pc) {
        return pc.getInventory().checkEquipped(this._ids);
    }

    @Override // com.lineage.data.item_armor.set.ArmorSet
    public boolean isPartOfSet(int id) {
        for (int i : this._ids) {
            if (id == i) {
                return true;
            }
        }
        return false;
    }

    @Override // com.lineage.data.item_armor.set.ArmorSet
    public boolean isEquippedRingOfArmorSet(L1PcInstance pc) {
        L1PcInventory pcInventory = pc.getInventory();
        L1ItemInstance armor = null;
        boolean isSetContainRing = false;
        int[] iArr = this._ids;
        int length = iArr.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                break;
            }
            armor = pcInventory.findItemId(iArr[i]);
            if (armor.getItem().getUseType() == 23) {
                isSetContainRing = true;
                break;
            }
            i++;
        }
        if (armor != null && isSetContainRing) {
            int itemId = armor.getItem().getItemId();
            if (pcInventory.getTypeEquipped(2, 9) == 2) {
                L1ItemInstance[] l1ItemInstanceArr = new L1ItemInstance[2];
                L1ItemInstance[] ring = pcInventory.getRingEquipped();
                if (ring[0].getItem().getItemId() == itemId && ring[1].getItem().getItemId() == itemId) {
                    return true;
                }
            }
        }
        return false;
    }
}
