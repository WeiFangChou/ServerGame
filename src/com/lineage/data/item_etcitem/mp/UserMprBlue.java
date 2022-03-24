package com.lineage.data.item_etcitem.mp;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1BuffUtil;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_PacketBox;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillSound;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/* renamed from: com.lineage.data.item_etcitem.mp.UserMprBlue */
public class UserMprBlue extends ItemExecutor {
    private static final Log _log = LogFactory.getLog(UserMprBlue.class);
    private int _gfxid = 0;
    private int _time = 600;

    private UserMprBlue() {
    }

    public static ItemExecutor get() {
        return new UserMprBlue();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) {
        if (item != null && pc != null) {
            try {
                if (L1BuffUtil.stopPotion(pc)) {
                    if (pc.hasSkillEffect(L1SkillId.STATUS_BLUE_POTION)) {
                        pc.killSkillEffectTimer(L1SkillId.STATUS_BLUE_POTION);
                    }
                    pc.getInventory().removeItem(item, 1);
                    L1BuffUtil.cancelAbsoluteBarrier(pc);
                    if (this._gfxid > 0) {
                        pc.sendPacketsX8(new S_SkillSound(pc.getId(), this._gfxid));
                    }
                    pc.sendPackets(new S_PacketBox(34, this._time));
                    pc.setSkillEffect(L1SkillId.STATUS_BLUE_POTION, this._time * L1SkillId.STATUS_BRAVE);
                    pc.sendPackets(new S_ServerMessage((int) L1SkillId.STATUS_POISON_SILENCE));
                }
            } catch (Exception e) {
                _log.error(e.getLocalizedMessage(), e);
            }
        }
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void set_set(String[] set) {
        try {
            this._time = Integer.parseInt(set[1]);
            if (this._time <= 0) {
                _log.error("UserMpr 設置錯誤:時效小於等於0! 使用預設600");
                this._time = 600;
            }
        } catch (Exception ignored) {
        }
        try {
            this._gfxid = Integer.parseInt(set[2]);
        } catch (Exception ignored) {
        }
    }
}
