package com.lineage.data.item_etcitem;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1BuffUtil;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillHaste;
import com.lineage.server.serverpackets.S_SkillSound;

public class Food2 extends ItemExecutor {
    private Food2() {
    }

    public static ItemExecutor get() {
        return new Food2();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        useGreenPotion(pc, item.getItemId());
        pc.getInventory().removeItem(item, 1);
    }

    private void useGreenPotion(L1PcInstance pc, int itemId) {
        if (pc.hasSkillEffect(71)) {
            pc.sendPackets(new S_ServerMessage(698));
            return;
        }
        L1BuffUtil.cancelAbsoluteBarrier(pc);
        pc.sendPacketsX8(new S_SkillSound(pc.getId(), L1SkillId.MORTAL_BODY));
        if (pc.getHasteItemEquipped() <= 0) {
            pc.setDrink(false);
            if (pc.hasSkillEffect(43)) {
                pc.killSkillEffectTimer(43);
                pc.sendPacketsAll(new S_SkillHaste(pc.getId(), 0, 0));
                pc.setMoveSpeed(0);
            } else if (pc.hasSkillEffect(54)) {
                pc.killSkillEffectTimer(54);
                pc.sendPacketsAll(new S_SkillHaste(pc.getId(), 0, 0));
                pc.setMoveSpeed(0);
            } else if (pc.hasSkillEffect(L1SkillId.STATUS_HASTE)) {
                pc.killSkillEffectTimer(L1SkillId.STATUS_HASTE);
                pc.sendPacketsAll(new S_SkillHaste(pc.getId(), 0, 0));
                pc.setMoveSpeed(0);
            }
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
                pc.sendPackets(new S_SkillHaste(pc.getId(), 1, 30));
                pc.broadcastPacketAll(new S_SkillHaste(pc.getId(), 1, 0));
                pc.setMoveSpeed(1);
                pc.setSkillEffect(L1SkillId.STATUS_HASTE, 30000);
            }
        }
    }
}
