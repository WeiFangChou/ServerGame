package com.lineage.data.npc.event;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_NpcChat;
import com.lineage.server.serverpackets.S_SkillSound;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Npc_Mazu extends NpcExecutor {
    private static final Log _log = LogFactory.getLog(Npc_Mazu.class);

    private Npc_Mazu() {
    }

    public static NpcExecutor get() {
        return new Npc_Mazu();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 3;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void talk(L1PcInstance pc, L1NpcInstance npc) {
        try {
            if (pc.getQuest().get_step(900001) < 1) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "y_e_g_01"));
                return;
            }
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "y_e_g_02"));
            pc.sendPackets(new S_NpcChat(npc, "媽祖的祝福剩餘：" + pc.getSkillEffectTimeSec(L1SkillId.MAZU_SKILL) + " 秒。"));
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void action(L1PcInstance pc, L1NpcInstance npc, String cmd, long amount) {
        try {
            if (cmd.equalsIgnoreCase("0") && pc.getQuest().get_step(900001) < 1) {
                pc.sendPacketsX8(new S_SkillSound(pc.getId(), 7321));
                pc.setSkillEffect(L1SkillId.MAZU_SKILL, 2400000);
                pc.getQuest().set_step(900001, pc.getQuest().get_step(900001) + 1);
            }
            pc.sendPackets(new S_CloseList(pc.getId()));
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
