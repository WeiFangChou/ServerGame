package com.lineage.server.command.executor;

import com.lineage.server.datatables.SkillsTable;
import com.lineage.server.datatables.lock.CharSkillReading;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_AddSkill;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillSound;
import com.lineage.server.templates.L1Skills;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1AddSkill implements L1CommandExecutor {
    private static final Log _log = LogFactory.getLog(L1AddSkill.class);

    private L1AddSkill() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1AddSkill();
    }

    @Override // com.lineage.server.command.executor.L1CommandExecutor
    public void execute(L1PcInstance pc, String cmdName, String arg) {
        try {
            int object_id = pc.getId();
            pc.sendPacketsX8(new S_SkillSound(object_id, 227));
            if (pc.isCrown()) {
                pc.sendPackets(new S_AddSkill(pc, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
                for (int cnt = 1; cnt <= 16; cnt++) {
                    L1Skills l1skills = SkillsTable.get().getTemplate(cnt);
                    String skill_name = l1skills.getName();
                    CharSkillReading.get().spellMastery(object_id, l1skills.getSkillId(), skill_name, 0, 0);
                }
                for (int cnt2 = 113; cnt2 <= 120; cnt2++) {
                    L1Skills l1skills2 = SkillsTable.get().getTemplate(cnt2);
                    String skill_name2 = l1skills2.getName();
                    CharSkillReading.get().spellMastery(object_id, l1skills2.getSkillId(), skill_name2, 0, 0);
                }
            } else if (pc.isKnight()) {
                pc.sendPackets(new S_AddSkill(pc, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0, 192, 7, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
                for (int cnt3 = 1; cnt3 <= 8; cnt3++) {
                    L1Skills l1skills3 = SkillsTable.get().getTemplate(cnt3);
                    String skill_name3 = l1skills3.getName();
                    CharSkillReading.get().spellMastery(object_id, l1skills3.getSkillId(), skill_name3, 0, 0);
                }
                for (int cnt4 = 87; cnt4 <= 91; cnt4++) {
                    L1Skills l1skills4 = SkillsTable.get().getTemplate(cnt4);
                    String skill_name4 = l1skills4.getName();
                    CharSkillReading.get().spellMastery(object_id, l1skills4.getSkillId(), skill_name4, 0, 0);
                }
            } else if (pc.isElf()) {
                pc.sendPackets(new S_AddSkill(pc, 255, 255, 127, 255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 127, 3, 255, 255, 255, 255, 0, 0, 0, 0, 0, 0));
                for (int cnt5 = 1; cnt5 <= 48; cnt5++) {
                    L1Skills l1skills5 = SkillsTable.get().getTemplate(cnt5);
                    String skill_name5 = l1skills5.getName();
                    CharSkillReading.get().spellMastery(object_id, l1skills5.getSkillId(), skill_name5, 0, 0);
                }
                for (int cnt6 = 129; cnt6 <= 176; cnt6++) {
                    L1Skills l1skills6 = SkillsTable.get().getTemplate(cnt6);
                    String skill_name6 = l1skills6.getName();
                    CharSkillReading.get().spellMastery(object_id, l1skills6.getSkillId(), skill_name6, 0, 0);
                }
            } else if (pc.isWizard()) {
                pc.sendPackets(new S_AddSkill(pc, 255, 255, 127, 255, 255, 255, 255, 255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
                for (int cnt7 = 1; cnt7 <= 80; cnt7++) {
                    L1Skills l1skills7 = SkillsTable.get().getTemplate(cnt7);
                    String skill_name7 = l1skills7.getName();
                    CharSkillReading.get().spellMastery(object_id, l1skills7.getSkillId(), skill_name7, 0, 0);
                }
            } else if (pc.isDarkelf()) {
                pc.sendPackets(new S_AddSkill(pc, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
                for (int cnt8 = 1; cnt8 <= 16; cnt8++) {
                    L1Skills l1skills8 = SkillsTable.get().getTemplate(cnt8);
                    String skill_name8 = l1skills8.getName();
                    CharSkillReading.get().spellMastery(object_id, l1skills8.getSkillId(), skill_name8, 0, 0);
                }
                for (int cnt9 = 97; cnt9 <= 112; cnt9++) {
                    L1Skills l1skills9 = SkillsTable.get().getTemplate(cnt9);
                    String skill_name9 = l1skills9.getName();
                    CharSkillReading.get().spellMastery(object_id, l1skills9.getSkillId(), skill_name9, 0, 0);
                }
            } else if (pc.isDragonKnight()) {
                pc.sendPackets(new S_AddSkill(pc, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 240, 255, 7, 0, 0, 0));
                for (int cnt10 = L1SkillId.DRAGON_SKIN; cnt10 <= 195; cnt10++) {
                    L1Skills l1skills10 = SkillsTable.get().getTemplate(cnt10);
                    String skill_name10 = l1skills10.getName();
                    CharSkillReading.get().spellMastery(object_id, l1skills10.getSkillId(), skill_name10, 0, 0);
                }
            } else if (pc.isIllusionist()) {
                pc.sendPackets(new S_AddSkill(pc, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 255, 255, 15));
                for (int cnt11 = 201; cnt11 <= 220; cnt11++) {
                    L1Skills l1skills11 = SkillsTable.get().getTemplate(cnt11);
                    String skill_name11 = l1skills11.getName();
                    CharSkillReading.get().spellMastery(object_id, l1skills11.getSkillId(), skill_name11, 0, 0);
                }
            }
        } catch (Exception e) {
            _log.error("錯誤的GM指令格式: " + getClass().getSimpleName() + " 執行的GM:" + pc.getName());
            pc.sendPackets(new S_ServerMessage(261));
        }
    }
}
