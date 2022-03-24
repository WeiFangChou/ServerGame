package com.lineage.server.model.doll;

import com.lineage.server.datatables.SkillsTable;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillUse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Doll_Add_Skill extends L1DollExecutor {
    private static final Log _log = LogFactory.getLog(Doll_Add_Skill.class);
    private int _int1;
    private String _note;

    public static L1DollExecutor get() {
        return new Doll_Add_Skill();
    }

    @Override // com.lineage.server.model.doll.L1DollExecutor
    public void set_power(int int1, int int2, int int3) {
        try {
            this._int1 = int1;
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.server.model.doll.L1DollExecutor
    public void set_note(String note) {
        this._note = note;
    }

    @Override // com.lineage.server.model.doll.L1DollExecutor
    public String get_note() {
        return this._note;
    }

    @Override // com.lineage.server.model.doll.L1DollExecutor
    public void setDoll(L1PcInstance pc) {
        L1ItemInstance tgItem = null;
        try {
            switch (this._int1) {
                case 8:
                case 12:
                case 48:
                case 107:
                    tgItem = pc.getWeapon();
                    if (tgItem == null) {
                        return;
                    }
                    break;
                case 21:
                    tgItem = pc.getInventory().getItemEquipped(2, 2);
                    if (tgItem == null) {
                        return;
                    }
                    break;
            }
            boolean is = false;
            if (tgItem != null) {
                if (!tgItem.isRunning()) {
                    is = true;
                }
            } else if (!pc.hasSkillEffect(this._int1)) {
                is = true;
            }
            if (is) {
                new L1SkillUse().handleCommands(pc, this._int1, pc.getId(), pc.getX(), pc.getY(), SkillsTable.get().getTemplate(this._int1).getBuffDuration(), 4);
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.server.model.doll.L1DollExecutor
    public void removeDoll(L1PcInstance pc) {
    }

    @Override // com.lineage.server.model.doll.L1DollExecutor
    public boolean is_reset() {
        return true;
    }
}
