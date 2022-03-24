package com.lineage.data.npc.mob;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1ItemDelay;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillSound;
import java.util.Random;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class E_Event1030 extends NpcExecutor {
    private static final Log _log = LogFactory.getLog(E_Event1030.class);
    private static Random _random = new Random();

    private E_Event1030() {
    }

    public static NpcExecutor get() {
        return new E_Event1030();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 4;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void attack(L1PcInstance pc, L1NpcInstance npc) {
        try {
            int i = _random.nextInt(100);
            if (i < 0 || i > 1) {
                if (i < 2 || i > 5) {
                    if (i >= 94 && i <= 99 && !npc.isremovearmor() && !pc.hasItemDelay(500)) {
                        L1ItemDelay.onItemUse(pc, 500, 2000);
                        pc.getInventory().takeoffEquip(945);
                        pc.sendPackets(new S_ServerMessage(1356));
                        pc.sendPackets(new S_ServerMessage(1027));
                        npc.set_removearmor(true);
                    }
                } else if (!npc.isremovearmor() && !pc.hasSkillEffect(L1SkillId.ADLV80_2_1) && !pc.hasSkillEffect(L1SkillId.ADLV80_2_2)) {
                    pc.sendPacketsX8(new S_SkillSound(pc.getId(), 7781));
                    pc.setSkillEffect(L1SkillId.ADLV80_2_1, 12000);
                    npc.set_removearmor(true);
                }
            } else if (!pc.hasSkillEffect(L1SkillId.ADLV80_2_1) && !pc.hasSkillEffect(L1SkillId.ADLV80_2_2)) {
                pc.sendPacketsX8(new S_SkillSound(pc.getId(), 7782));
                pc.setSkillEffect(L1SkillId.ADLV80_2_2, 12000);
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
