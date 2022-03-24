package com.lineage.data.item_armor.set;

import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1PolyMorph;
import com.lineage.server.serverpackets.S_ServerMessage;

public class EffectPolymorph implements ArmorSetEffect {
    private int _gfxId;

    public EffectPolymorph(int gfxId) {
        this._gfxId = gfxId;
    }

    @Override // com.lineage.data.item_armor.set.ArmorSetEffect
    public void giveEffect(L1PcInstance pc) {
        int awakeSkillId = pc.getAwakeSkillId();
        if (awakeSkillId == 185 || awakeSkillId == 190 || awakeSkillId == 195) {
            pc.sendPackets(new S_ServerMessage(1384));
            return;
        }
        if (this._gfxId == 6080 || this._gfxId == 6094) {
            if (pc.get_sex() == 0) {
                this._gfxId = 6094;
            } else {
                this._gfxId = 6080;
            }
            if (!isRemainderOfCharge(pc)) {
                return;
            }
        }
        L1PolyMorph.doPoly(pc, this._gfxId, 0, 1);
    }

    @Override // com.lineage.data.item_armor.set.ArmorSetEffect
    public void cancelEffect(L1PcInstance pc) {
        int awakeSkillId = pc.getAwakeSkillId();
        if (awakeSkillId == 185 || awakeSkillId == 190 || awakeSkillId == 195) {
            pc.sendPackets(new S_ServerMessage(1384));
            return;
        }
        if (this._gfxId == 6080 || this._gfxId == 6094) {
            if (pc.get_sex() == 0) {
                this._gfxId = 6094;
            } else {
                this._gfxId = 6080;
            }
        }
        if (pc.getTempCharGfx() == this._gfxId) {
            L1PolyMorph.undoPoly(pc);
        }
    }

    private boolean isRemainderOfCharge(L1PcInstance pc) {
        L1ItemInstance item;
        if (!pc.getInventory().checkItem(20383, 1) || (item = pc.getInventory().findItemId(20383)) == null || item.getChargeCount() == 0) {
            return false;
        }
        return true;
    }

    @Override // com.lineage.data.item_armor.set.ArmorSetEffect
    public int get_mode() {
        return this._gfxId;
    }
}
