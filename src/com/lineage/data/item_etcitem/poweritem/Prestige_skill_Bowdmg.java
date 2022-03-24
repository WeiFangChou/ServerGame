package com.lineage.data.item_etcitem.poweritem;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.datatables.SkillsTable;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.model.skill.L1SkillUse;

public class Prestige_skill_Bowdmg extends ItemExecutor {
    private int _time = 1800;

    private Prestige_skill_Bowdmg() {
    }

    public static ItemExecutor get() {
        return new Prestige_skill_Bowdmg();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        int time = this._time;
        int[] allBuffSkill = {2, 14, 32, 48, 55, 68, 79, 88, 89, 90, 98, L1SkillId.BURNING_SPIRIT, 104, 105, 106, 111, L1SkillId.GLOWING_AURA, 117, 129, 137, L1SkillId.ELEMENTAL_PROTECTION, L1SkillId.AQUA_PROTECTER, 166, L1SkillId.IRON_SKIN, L1SkillId.EXOTIC_VITALIZE, 170, L1SkillId.ADDITIONAL_FIRE};
        for (int i = 0; i < allBuffSkill.length; i++) {
            new L1SkillUse().handleCommands(pc, 42, pc.getId(), pc.getX(), pc.getY(), time, 4);
            new L1SkillUse().handleCommands(pc, 26, pc.getId(), pc.getX(), pc.getY(), time, 4);
            new L1SkillUse().handleCommands(pc, allBuffSkill[i], pc.getId(), pc.getX(), pc.getY(), SkillsTable.get().getTemplate(allBuffSkill[i]).getBuffDuration(), 4);
        }
        pc.getInventory().removeItem(item, 1);
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void set_set(String[] set) {
        try {
            this._time = Integer.parseInt(set[1]);
        } catch (Exception ignored) {
        }
    }
}
