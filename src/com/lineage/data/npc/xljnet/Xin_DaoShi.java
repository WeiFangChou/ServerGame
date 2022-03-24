package com.lineage.data.npc.xljnet;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1BuffUtil;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillHaste;
import com.lineage.server.serverpackets.S_SkillSound;
import com.lineage.server.serverpackets.S_SystemMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Xin_DaoShi extends NpcExecutor {
    private static final Log _log = LogFactory.getLog(Xin_DaoShi.class);

    public static NpcExecutor get() {
        return new Xin_DaoShi();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 3;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void talk(L1PcInstance pc, L1NpcInstance npc) {
        try {
            if (pc.getLevel() < 15) {
                if (pc.getLevel() < 5) {
                    pc.addExp(750);
                }
                pc.sendPackets(new S_ServerMessage((int) L1SkillId.GUARD_BRAKE));
                pc.sendPacketsAll(new S_SkillHaste(pc.getId(), 1, 1600));
                pc.sendPacketsAll(new S_SkillSound(pc.getId(), 755));
                pc.setMoveSpeed(1);
                L1BuffUtil.brave(pc, 900000);
                pc.setSkillEffect(L1SkillId.STATUS_HASTE, 1600000);
                pc.setCurrentHp(pc.getMaxHp());
                pc.setCurrentMp(pc.getMaxMp());
                pc.sendPackets(new S_ServerMessage(77));
                pc.sendPackets(new S_SkillSound(pc.getId(), 830));
                return;
            }
            pc.sendPackets(new S_SystemMessage("你已經不再需要我的幫助."));
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
