package com.lineage.data.item_etcitem;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_OwnCharStatus;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillSound;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Reactivating_Potion extends ItemExecutor {
    private static final Log _log = LogFactory.getLog(Reactivating_Potion.class);

    private Reactivating_Potion() {
    }

    public static ItemExecutor get() {
        return new Reactivating_Potion();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        int pcObjid = pc.getId();
        pc.setExp(1);
        pc.resetLevel();
        pc.setBonusStats(0);
        pc.resetBaseAc();
        pc.resetBaseMr();
        pc.resetBaseHitup();
        pc.resetBaseDmgup();
        int randomHp = pc.getMaxHp() - ((pc.getMaxHp() * 10) / 100);
        int randomMp = pc.getMaxMp() - ((pc.getMaxMp() * 10) / 100);
        pc.addBaseMaxHp( (-randomHp));
        pc.addBaseMaxMp( (-randomMp));
        pc.setCurrentHp(pc.getMaxHp());
        pc.setCurrentMp(pc.getMaxMp());
        pc.sendPacketsX8(new S_SkillSound(pcObjid, L1SkillId.MORTAL_BODY));
        pc.sendPackets(new S_OwnCharStatus(pc));
        pc.getInventory().removeItem(item, 1);
        pc.sendPackets(new S_ServerMessage(822));
        try {
            pc.save();
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
