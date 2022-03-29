package com.lineage.data.npc.quest;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.WizardLv30_1;
import com.lineage.server.datatables.QuestMapTable;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1PolyMorph;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.ServerBasePacket;
import com.lineage.server.templates.L1QuestUser;
import com.lineage.server.utils.L1SpawnUtil;
import com.lineage.server.world.WorldQuest;
import java.util.ArrayList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Npc_Dilong extends NpcExecutor {
    private static final Log _log = LogFactory.getLog(Npc_Dilong.class);

    public static NpcExecutor get() {
        return new Npc_Dilong();
    }

    public int type() {
        return 3;
    }

    public void talk(L1PcInstance pc, L1NpcInstance npc) {
        try {
            if (pc.isCrown()) {
                pc.sendPackets((ServerBasePacket)new S_NPCTalkReturn(npc.getId(), "dilong2"));
            } else if (pc.isKnight()) {
                pc.sendPackets((ServerBasePacket)new S_NPCTalkReturn(npc.getId(), "dilong2"));
            } else if (pc.isElf()) {
                pc.sendPackets((ServerBasePacket)new S_NPCTalkReturn(npc.getId(), "dilong2"));
            } else if (pc.isWizard()) {
                if (pc.getQuest().isEnd(WizardLv30_1.QUEST.get_id())) {
                    pc.sendPackets((ServerBasePacket)new S_NPCTalkReturn(npc.getId(), "dilong3"));
                    return;
                }
                if (pc.getLevel() >= WizardLv30_1.QUEST.get_questlevel()) {
                    if (!pc.getQuest().isStart(WizardLv30_1.QUEST.get_id())) {
                        pc.sendPackets((ServerBasePacket)new S_NPCTalkReturn(npc.getId(), "dilong3"));
                    } else {
                        pc.sendPackets((ServerBasePacket)new S_NPCTalkReturn(npc.getId(), "dilong1"));
                    }
                } else {
                    pc.sendPackets((ServerBasePacket)new S_NPCTalkReturn(npc.getId(), "dilong2"));
                }
            } else if (pc.isDarkelf()) {
                pc.sendPackets((ServerBasePacket)new S_NPCTalkReturn(npc.getId(), "dilong2"));
            } else if (pc.isDragonKnight()) {
                pc.sendPackets((ServerBasePacket)new S_NPCTalkReturn(npc.getId(), "dilong2"));
            } else if (pc.isIllusionist()) {
                pc.sendPackets((ServerBasePacket)new S_NPCTalkReturn(npc.getId(), "dilong2"));
            } else {
                pc.sendPackets((ServerBasePacket)new S_NPCTalkReturn(npc.getId(), "dilong2"));
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public void action(L1PcInstance pc, L1NpcInstance npc, String cmd, long amount) {
        boolean isCloseList = false;
        if (pc.isWizard()) {
            if (pc.getQuest().isEnd(WizardLv30_1.QUEST.get_id()))
                return;
            if (!pc.getQuest().isStart(WizardLv30_1.QUEST.get_id()))
                return;
            if (cmd.equalsIgnoreCase("teleportURL")) {
                if (!pc.getInventory().checkItem(40581)) {
                    pc.sendPackets((ServerBasePacket)new S_NPCTalkReturn(npc.getId(), "dilongn"));
                    return;
                }
                if (pc.getInventory().checkItem(40579)) {
                    pc.sendPackets((ServerBasePacket)new S_NPCTalkReturn(npc.getId(), "dilongn"));
                } else {
                    pc.sendPackets((ServerBasePacket)new S_NPCTalkReturn(npc.getId(), "dilongs"));
                }
            } else if (cmd.equalsIgnoreCase("teleport mage-quest-dungen")) {
                staraQuest(pc);
            }
        }
        if (isCloseList)
            pc.sendPackets((ServerBasePacket)new S_CloseList(pc.getId()));
    }

    public static void staraQuest(L1PcInstance pc) {
        try {
            int questid = WizardLv30_1.QUEST.get_id();
            int mapid = 201;
            int showId = WorldQuest.get().nextId();
            L1QuestUser quest = WorldQuest.get().put(showId, 201, questid, pc);
            if (quest == null) {
                _log.error("副本設置過程發生異常!!");
                        pc.sendPackets((ServerBasePacket)new S_CloseList(pc.getId()));
                return;
            }
            Integer time = QuestMapTable.get().getTime(201);
            if (time != null)
                quest.set_time(time.intValue());
            ArrayList<L1NpcInstance> npcs = quest.npcList(81109);
            if (npcs != null)
                for (L1NpcInstance npc : npcs)
                    L1PolyMorph.doPoly((L1Character)npc, 1128, 1800, 1);
            L1SpawnUtil.spawnDoor(quest, 10000, 89, 32809, 32795, 201, 1);
            L1SpawnUtil.spawnDoor(quest, 10001, 88, 32812, 32909, 201, 0);
            L1SpawnUtil.spawnDoor(quest, 10002, 89, 32825, 32920, 201, 1);
            L1SpawnUtil.spawnDoor(quest, 10003, 90, 32868, 32919, 201, 0);
            pc.getInventory().takeoffEquip(945);
            L1Teleport.teleport(pc, 32791, 32788, 201, 5, true);
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
