package com.lineage.data.npc.quest;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.DarkElfLv50_1;
import com.lineage.server.datatables.QuestMapTable;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.ServerBasePacket;
import com.lineage.server.templates.L1QuestUser;
import com.lineage.server.world.WorldQuest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Npc_CorruptSoul extends NpcExecutor {
    private static final Log _log = LogFactory.getLog(Npc_CorruptSoul.class);

    public static NpcExecutor get() {
        return new Npc_CorruptSoul();
    }

    public int type() {
        return 3;
    }

    public void talk(L1PcInstance pc, L1NpcInstance npc) {
        try {
            if (!pc.getInventory().checkEquipped(20037))
                return;
            if (pc.isCrown()) {
                pc.sendPackets((ServerBasePacket)new S_NPCTalkReturn(npc.getId(), "csoulq2"));
            } else if (pc.isKnight()) {
                pc.sendPackets((ServerBasePacket)new S_NPCTalkReturn(npc.getId(), "csoulq2"));
            } else if (pc.isElf()) {
                pc.sendPackets((ServerBasePacket)new S_NPCTalkReturn(npc.getId(), "csoulq2"));
            } else if (pc.isWizard()) {
                pc.sendPackets((ServerBasePacket)new S_NPCTalkReturn(npc.getId(), "csoulq2"));
            } else if (pc.isDarkelf()) {
                if (pc.getQuest().isEnd(DarkElfLv50_1.QUEST.get_id())) {
                    pc.sendPackets((ServerBasePacket)new S_NPCTalkReturn(npc.getId(), "csoulq3"));
                    return;
                }
                switch (pc.getQuest().get_step(DarkElfLv50_1.QUEST.get_id())) {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                        pc.sendPackets((ServerBasePacket)new S_NPCTalkReturn(npc.getId(), "csoulqn"));
                        return;
                    case 4:
                        pc.sendPackets((ServerBasePacket)new S_NPCTalkReturn(npc.getId(), "csoulq1"));
                        return;
                }
                pc.sendPackets((ServerBasePacket)new S_NPCTalkReturn(npc.getId(), "csoulq3"));
            } else if (pc.isDragonKnight()) {
                pc.sendPackets((ServerBasePacket)new S_NPCTalkReturn(npc.getId(), "csoulq2"));
            } else if (pc.isIllusionist()) {
                pc.sendPackets((ServerBasePacket)new S_NPCTalkReturn(npc.getId(), "csoulq2"));
            } else {
                pc.sendPackets((ServerBasePacket)new S_NPCTalkReturn(npc.getId(), "csoulq2"));
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public void action(L1PcInstance pc, L1NpcInstance npc, String cmd, long amount) {
        boolean isCloseList = false;
        if (pc.isDarkelf()) {
            if (pc.getQuest().isEnd(DarkElfLv50_1.QUEST.get_id())) {
                isCloseList = true;
            } else if (pc.getInventory().checkItem(20037)) {
                if (pc.getQuest().get_step(DarkElfLv50_1.QUEST.get_id()) == 4) {
                    if (cmd.equalsIgnoreCase("teleportURL")) {
                        pc.sendPackets((ServerBasePacket)new S_NPCTalkReturn(npc.getId(), "csoulqs"));
                    } else if (cmd.equalsIgnoreCase("teleport evil-dungeon")) {
                        staraQuest(pc);
                        isCloseList = true;
                    }
                } else {
                    isCloseList = true;
                }
            } else {
                isCloseList = true;
            }
        } else {
            isCloseList = true;
        }
        if (isCloseList)
            pc.sendPackets((ServerBasePacket)new S_CloseList(pc.getId()));
    }

    private void staraQuest(L1PcInstance pc) {
        try {
            int questid = DarkElfLv50_1.QUEST.get_id();
            int mapid = 306;
            int showId = WorldQuest.get().nextId();
            L1QuestUser quest = WorldQuest.get().put(showId, 306, questid, pc);
            if (quest == null) {
                _log.error("副本設置過程發生異常!!");
                        pc.sendPackets((ServerBasePacket)new S_CloseList(pc.getId()));
                return;
            }
            quest.set_info(false);
            Integer time = QuestMapTable.get().getTime(306);
            if (time != null)
                quest.set_time(time.intValue());
            L1Teleport.teleport(pc, 32748, 32799, (short)306, 5, true);
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}

