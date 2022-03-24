package com.lineage.data.item_etcitem.hp;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1BuffUtil;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_SkillSound;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/* renamed from: com.lineage.data.item_etcitem.hp.UserAddHp */
public class UserAddHp extends ItemExecutor {
    private static final Log _log = LogFactory.getLog(UserAddHp.class);
    private int _gfxid = 0;
    private int _max_addhp = 0;
    private int _min_hp = 1;
    private boolean _notConsume;

    private UserAddHp() {
    }

    public static ItemExecutor get() {
        return new UserAddHp();
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
                    int addhp = this._min_hp;
                    if (this._max_addhp > 0) {
                        addhp += (int) (Math.random() * ((double) this._max_addhp));
                    }
                    if (pc.get_up_hp_potion() > 0) {
                        addhp += (pc.get_up_hp_potion() * addhp) / 100;
                    }
                    if (pc.hasSkillEffect(173)) {
                        addhp >>= 1;
                    }
                    if (pc.hasSkillEffect(L1SkillId.ADLV80_2_2)) {
                        addhp >>= 1;
                    }
                    if (pc.hasSkillEffect(L1SkillId.ADLV80_2_1)) {
                        addhp *= -1;
                    }
                    pc.setCurrentHp(pc.getCurrentHp() + addhp);
                }
            } catch (Exception e) {
                _log.error(e.getLocalizedMessage(), e);
            }
        }
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void set_set(String[] set) {
        try {
            this._min_hp = Integer.parseInt(set[1]);
            if (this._min_hp <= 0) {
                _log.error("UserHpr 設置錯誤:最小恢復質小於等於0! 使用預設1");
                this._min_hp = 1;
            }
        } catch (Exception ignored) {
        }
        try {
            int max_hp = Integer.parseInt(set[2]);
            if (max_hp >= this._min_hp) {
                this._max_addhp = (max_hp - this._min_hp) + 1;
            } else {
                _log.error("UserHpr 設置錯誤:最大恢復質小於最小恢復質!(" + this._min_hp + " " + max_hp + ")");
                this._max_addhp = 0;
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
