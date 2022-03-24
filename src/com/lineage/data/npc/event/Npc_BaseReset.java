package com.lineage.data.npc.event;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.skill.L1SkillUse;
import com.lineage.server.serverpackets.S_CharReset;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.ServerBasePacket;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Npc_BaseReset extends NpcExecutor {
    private static final Log _log = LogFactory.getLog(Npc_BaseReset.class);

    public static NpcExecutor get() {
        return new Npc_BaseReset();
    }

    public int type() {
        return 3;
    }

    public void talk(L1PcInstance pc, L1NpcInstance npc) {
        try {
            pc.sendPackets((ServerBasePacket)new S_NPCTalkReturn(npc.getId(), "baseReset"));
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public void action(L1PcInstance pc, L1NpcInstance npc, String cmd, long amount) {
        try {
            if (cmd.equalsIgnoreCase("ent")) {
                if (!pc.getInventory().checkItem(49142)) {
                    pc.sendPackets((ServerBasePacket)new S_ServerMessage(1290));
                    return;
                }
                L1SkillUse l1skilluse = new L1SkillUse();
                l1skilluse.handleCommands(pc, 44, pc.getId(), pc.getX(), pc.getY(), 0, 1);
                pc.getInventory().takeoffEquip(945);
                L1Teleport.teleport(pc, 32737, 32789, (short)997, 4, false);
                int initStatusPoint = 75 + pc.getElixirStats();
                int pcStatusPoint = pc.getBaseStr() + pc.getBaseInt() + pc.getBaseWis() +
                        pc.getBaseDex() + pc.getBaseCon() + pc.getBaseCha();
                if (pc.getLevel() > 50)
                    pcStatusPoint += pc.getLevel() - 50 - pc.getBonusStats();
                int diff = pcStatusPoint - initStatusPoint;
                int maxLevel = 1;
                if (diff > 0) {
                    maxLevel = Math.min(50 + diff, 99);
                } else {
                    maxLevel = pc.getLevel();
                }
                pc.setTempMaxLevel(maxLevel);
                pc.setTempLevel(1);
                pc.setInCharReset(true);
                pc.sendPackets((ServerBasePacket)new S_CharReset(pc));
            }
            pc.sendPackets((ServerBasePacket)new S_CloseList(pc.getId()));
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
