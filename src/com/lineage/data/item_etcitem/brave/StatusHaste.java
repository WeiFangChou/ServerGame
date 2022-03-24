package com.lineage.data.item_etcitem.brave;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1BuffUtil;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillHaste;
import com.lineage.server.serverpackets.S_SkillSound;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class StatusHaste extends ItemExecutor {
    private static final int _int1 = 1;
    private static final int _int2 = 2;
    private static final int _int3 = 4;
    private static final int _int4 = 8;
    private static final int _int5 = 16;
    private static final int _int6 = 32;
    private static final int _int7 = 64;
    private static final Log _log = LogFactory.getLog(StatusHaste.class);
    private int _gfxid = 0;
    private boolean _isCrown;
    private boolean _isDarkelf;
    private boolean _isDragonKnight;
    private boolean _isElf;
    private boolean _isIllusionist;
    private boolean _isKnight;
    private boolean _isWizard;
    private boolean _notConsume;
    private int _time = 300;

    private StatusHaste() {
    }

    public static ItemExecutor get() {
        return new StatusHaste();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        if (item != null && pc != null && L1BuffUtil.stopPotion(pc)) {
            if (check(pc)) {
                if (this._gfxid > 0) {
                    pc.sendPacketsX8(new S_SkillSound(pc.getId(), this._gfxid));
                }
                if (pc.getHasteItemEquipped() <= 0) {
                    if (!this._notConsume) {
                        pc.getInventory().removeItem(item, 1);
                    }
                    pc.setDrink(false);
                    L1BuffUtil.hasteStart(pc);
                    L1BuffUtil.cancelAbsoluteBarrier(pc);
                    if (pc.hasSkillEffect(29)) {
                        pc.killSkillEffectTimer(29);
                        pc.sendPacketsAll(new S_SkillHaste(pc.getId(), 0, 0));
                    } else if (pc.hasSkillEffect(76)) {
                        pc.killSkillEffectTimer(76);
                        pc.sendPacketsAll(new S_SkillHaste(pc.getId(), 0, 0));
                    } else if (pc.hasSkillEffect(L1SkillId.ENTANGLE)) {
                        pc.killSkillEffectTimer(L1SkillId.ENTANGLE);
                        pc.sendPacketsAll(new S_SkillHaste(pc.getId(), 0, 0));
                    } else {
                        pc.sendPackets(new S_SkillHaste(pc.getId(), 1, this._time));
                        pc.broadcastPacketAll(new S_SkillHaste(pc.getId(), 1, 0));
                        pc.setMoveSpeed(1);
                        pc.setSkillEffect(L1SkillId.STATUS_HASTE, this._time * L1SkillId.STATUS_BRAVE);
                    }
                }
            } else {
                pc.sendPackets(new S_ServerMessage(79));
            }
        }
    }

    private boolean check(L1PcInstance pc) {
        try {
            if (pc.isCrown() && this._isCrown) {
                return true;
            }
            if (pc.isKnight() && this._isKnight) {
                return true;
            }
            if (pc.isElf() && this._isElf) {
                return true;
            }
            if (pc.isWizard() && this._isWizard) {
                return true;
            }
            if (pc.isDarkelf() && this._isDarkelf) {
                return true;
            }
            if (pc.isDragonKnight() && this._isDragonKnight) {
                return true;
            }
            if (!pc.isIllusionist() || !this._isIllusionist) {
                return false;
            }
            return true;
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            return false;
        }
    }

    private void set_use_type(int use_type) {
        if (use_type >= 64) {
            use_type -= 64;
            try {
                this._isIllusionist = true;
            } catch (Exception e) {
                _log.error(e.getLocalizedMessage(), e);
                return;
            }
        }
        if (use_type >= 32) {
            use_type -= 32;
            this._isDragonKnight = true;
        }
        if (use_type >= 16) {
            use_type -= 16;
            this._isDarkelf = true;
        }
        if (use_type >= 8) {
            use_type -= 8;
            this._isWizard = true;
        }
        if (use_type >= 4) {
            use_type -= 4;
            this._isElf = true;
        }
        if (use_type >= 2) {
            use_type -= 2;
            this._isKnight = true;
        }
        if (use_type >= 1) {
            use_type--;
            this._isCrown = true;
        }
        if (use_type > 0) {
            _log.error("StatusHaste 可執行職業設定錯誤:餘數大於0");
        }
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void set_set(String[] set) {
        try {
            this._time = Integer.parseInt(set[1]);
            if (this._time <= 0) {
                _log.error("StatusHaste 設置錯誤:技能效果時間小於等於0! 使用預設300秒");
                this._time = 300;
            }
        } catch (Exception ignored) {
        }
        try {
            this._gfxid = Integer.parseInt(set[2]);
        } catch (Exception ignored) {
        }
        try {
            set_use_type(Integer.parseInt(set[3]));
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
