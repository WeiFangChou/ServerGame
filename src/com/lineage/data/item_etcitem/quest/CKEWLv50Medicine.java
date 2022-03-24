package com.lineage.data.item_etcitem.quest;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillSound;

public class CKEWLv50Medicine extends ItemExecutor {
    private CKEWLv50Medicine() {
    }

    public static ItemExecutor get() {
        return new CKEWLv50Medicine();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        if (pc.getMapId() != 2000) {
            pc.sendPackets(new S_ServerMessage(79));
            return;
        }
        if (pc.hasSkillEffect(L1SkillId.DE_LV30)) {
            pc.removeSkillEffect(L1SkillId.DE_LV30);
        }
        if (pc.hasSkillEffect(L1SkillId.STATUS_CURSE_YAHEE)) {
            pc.removeSkillEffect(L1SkillId.STATUS_CURSE_YAHEE);
        }
        if (pc.hasSkillEffect(L1SkillId.STATUS_CURSE_BARLOG)) {
            pc.removeSkillEffect(L1SkillId.STATUS_CURSE_BARLOG);
        }
        if (pc.hasSkillEffect(L1SkillId.CKEW_LV50)) {
            pc.removeSkillEffect(L1SkillId.CKEW_LV50);
        }
        if (pc.getWeapon() != null && !pc.isCrown()) {
            pc.getInventory().setEquipped(pc.getWeapon(), false, false, false);
            pc.sendPackets(new S_ServerMessage(1027));
        }
        pc.setSkillEffect(L1SkillId.CKEW_LV50, 1500000);
        pc.sendPacketsX8(new S_SkillSound(pc.getId(), 7248));
        pc.sendPackets(new S_ServerMessage(1300));
        pc.getInventory().removeItem(item, 1);
    }
}
