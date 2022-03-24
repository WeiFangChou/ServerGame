package com.lineage.data.item_etcitem.mp;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1BuffUtil;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillSound;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/* renamed from: com.lineage.data.item_etcitem.mp.UserAddMp */
public class UserAddMp extends ItemExecutor {
    private static final Log _log = LogFactory.getLog(UserAddMp.class);
    private int _gfxid = 0;
    private int _max_addmp = 0;
    private int _min_mp = 1;
    private boolean _notConsume;

    private UserAddMp() {
    }

    public static ItemExecutor get() {
        return new UserAddMp();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) {
        if (item != null && pc != null) {
            try {
                if (L1BuffUtil.stopPotion(pc)) {
                    if (!this._notConsume) {
                        pc.getInventory().removeItem(item, 1);
                    }
                    L1BuffUtil.cancelAbsoluteBarrier(pc);
                    if (this._gfxid > 0) {
                        pc.sendPacketsX8(new S_SkillSound(pc.getId(), this._gfxid));
                    }
                    int addmp = this._min_mp;
                    if (this._max_addmp > 0) {
                        addmp += (int) (Math.random() * ((double) this._max_addmp));
                    }
                    if (addmp > 0) {
                        pc.sendPackets(new S_ServerMessage(338, "$1084"));
                    }
                    pc.setCurrentMp(pc.getCurrentMp() + addmp);
                }
            } catch (Exception e) {
                _log.error(e.getLocalizedMessage(), e);
            }
        }
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void set_set(String[] set) {
        try {
            this._min_mp = Integer.parseInt(set[1]);
            if (this._min_mp <= 0) {
                _log.error("UserMpr 設置錯誤:最小恢復質小於等於0! 使用預設1");
                this._min_mp = 1;
            }
        } catch (Exception ignored) {
        }
        try {
            int max_hp = Integer.parseInt(set[2]);
            if (max_hp >= this._min_mp) {
                this._max_addmp = (max_hp - this._min_mp) + 1;
            } else {
                _log.error("UserMpr 設置錯誤:最大恢復質小於最小恢復質!(" + this._min_mp + " " + max_hp + ")");
                this._max_addmp = 0;
            }
        } catch (Exception ignored) {
        }
        try {
            this._gfxid = Integer.parseInt(set[3]);
        } catch (Exception ignored) {
        }
        try {
            if (set.length > 4) {
                this._notConsume = Boolean.parseBoolean(set[4]);
            }
        } catch (Exception ignored) {
        }
    }
}
