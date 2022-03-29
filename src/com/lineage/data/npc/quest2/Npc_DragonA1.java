package com.lineage.data.npc.quest2;

import com.lineage.data.QuestClass;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.ADLv80_1;
import com.lineage.server.datatables.QuestMapTable;
import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Location;
import com.lineage.server.model.L1Party;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.templates.L1QuestUser;
import com.lineage.server.utils.L1SpawnUtil;
import com.lineage.server.world.WorldQuest;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Npc_DragonA1 extends NpcExecutor {
    private static final Log _log = LogFactory.getLog(Npc_DragonA1.class);
    private static final Map<Integer, String> _playList = new HashMap();

    private Npc_DragonA1() {
    }

    public static NpcExecutor get() {
        return new Npc_DragonA1();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 3;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void talk(L1PcInstance pc, L1NpcInstance npc) {
        try {
            if (!isError(pc, npc)) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "y_n_dragon1", new String[]{"安塔瑞斯棲息地"}));
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void action(L1PcInstance pc, L1NpcInstance npc, String cmd, long amount) {
        if (cmd.equalsIgnoreCase("0")) {
            if (!isError(pc, npc)) {
                staraQuestA(pc);
            } else {
                return;
            }
        }
        if (0 != 0) {
            pc.sendPackets(new S_CloseList(pc.getId()));
        }
    }

    private void staraQuestA(L1PcInstance pc) {
        try {
            int questid = ADLv80_1.QUEST.get_id();
            int showId = WorldQuest.get().nextId();
            int users = QuestMapTable.get().getTemplate(ADLv80_1.MAPID);
            if (users == -1) {
                users = 127;
            }
            L1Party party = pc.getParty();
            if (party != null) {
                int i = 0;
                for (L1PcInstance otherPc : party.partyUsers().values()) {
                    if (i <= users - 1 && otherPc.getId() != party.getLeaderID()) {
                        WorldQuest.get().put(showId, ADLv80_1.MAPID, questid, otherPc);
                        _playList.put(new Integer(otherPc.getId()), otherPc.getName());
                        L1Teleport.teleport(otherPc, 32595, 32747,  1005, 1, true);
                        QuestClass.get().startQuest(otherPc, ADLv80_1.QUEST.get_id());
                        QuestClass.get().endQuest(otherPc, ADLv80_1.QUEST.get_id());
                    }
                    i++;
                }
            }
            L1QuestUser quest = WorldQuest.get().put(showId, ADLv80_1.MAPID, questid, pc);
            if (quest == null) {
                _log.error("副本設置過程發生異常!!");
                pc.sendPackets(new S_CloseList(pc.getId()));
                return;
            }
            Integer time = QuestMapTable.get().getTime(ADLv80_1.MAPID);
            if (time != null) {
                quest.set_time(time.intValue());
            }
            L1SpawnUtil.spawnDoor(quest, 10005, 7556, 32759, 32799, 1005, 1);
            L1SpawnUtil.spawnDoor(quest, 10006, 7556, 32787, 32848, 1005, 1);
            L1SpawnUtil.spawnDoor(quest, 10007, 7556, 32781, 32919, 1005, 1);
            L1SpawnUtil.spawn(71014, new L1Location(32781, 32705, (int) ADLv80_1.MAPID), 5, showId);
            L1Teleport.teleport(pc, 32595, 32747, 1005, 1, true);
            QuestClass.get().startQuest(pc, ADLv80_1.QUEST.get_id());
            QuestClass.get().endQuest(pc, ADLv80_1.QUEST.get_id());
            _playList.put(new Integer(pc.getId()), pc.getName());
            for (L1NpcInstance npc : quest.npcList()) {
                if (npc instanceof L1MonsterInstance) {
                    L1MonsterInstance mob = (L1MonsterInstance) npc;
                    if (!(mob.getNpcId() == 71014 || mob.getNpcId() == 71015 || mob.getNpcId() == 71016)) {
                        mob.set_storeDroped(true);
                        mob.getInventory().clearItems();
                    }
                }
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private boolean isError(L1PcInstance pc, L1NpcInstance npc) {
        if (pc.isGm()) {
            return false;
        }
        L1Party party = pc.getParty();
        if (party == null) {
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "y_n_dragon2"));
            return true;
        } else if (!party.isLeader(pc)) {
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "y_n_dragon6"));
            return true;
        } else if (party.getNumOfMembers() < 3) {
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "y_n_dragon3"));
            return true;
        } else {
            StringBuilder list80 = null;
            StringBuilder list = null;
            for (L1PcInstance tgpc : party.partyUsers().values()) {
                if (tgpc.getLevel() < 80) {
                    if (list80 == null) {
                        list80 = new StringBuilder();
                    }
                    list80.append(String.valueOf(tgpc.getName()) + " ");
                }
                if (_playList.get(new Integer(tgpc.getId())) != null) {
                    if (list == null) {
                        list = new StringBuilder();
                    }
                    list.append(String.valueOf(tgpc.getName()) + " ");
                }
            }
            if (list80 != null) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "y_n_dragon4", new String[]{list80.toString()}));
                return true;
            } else if (list == null) {
                return false;
            } else {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "y_n_dragon5", new String[]{list.toString()}));
                return true;
            }
        }
    }
}
