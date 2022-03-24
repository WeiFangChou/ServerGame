package com.lineage.data.npc.quest;

import com.lineage.data.QuestClass;
import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.Chapter00;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1PolyMorph;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.ServerBasePacket;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Npc_YouLiya extends NpcExecutor {
    private static final Log _log = LogFactory.getLog(Npc_YouLiya.class);

    public static NpcExecutor get() {
        return new Npc_YouLiya();
    }

    public int type() {
        return 3;
    }

    public void talk(L1PcInstance pc, L1NpcInstance npc) {
        try {
            if (pc.getInventory().checkItem(49312)) {
                pc.sendPackets((ServerBasePacket)new S_NPCTalkReturn(npc.getId(), "j_html00"));
            } else {
                pc.sendPackets((ServerBasePacket)new S_NPCTalkReturn(npc.getId(), "j_html01"));
            }
        } catch (Exception e) {
            pc.sendPackets((ServerBasePacket)new S_NPCTalkReturn(npc.getId(), "j_html05"));
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public void action(L1PcInstance pc, L1NpcInstance npc, String cmd, long amount) {
        try {
            boolean isCloseList = false;
            if (cmd.equalsIgnoreCase("a")) {
                int[] items = { 40308, 49313 };
                int[] counts = { 10000, 1 };
                if (CreateNewItem.checkNewItem(pc,
                        items,
                        counts) <
                        1L) {
                    pc.sendPackets((ServerBasePacket)new S_NPCTalkReturn(npc.getId(), "j_html02"));
                } else {
                    L1ItemInstance item1 = pc.getInventory().checkItemX(items[0], counts[0]);
                    boolean error = false;
                    if (item1 != null) {
                        pc.getInventory().removeItem(item1, counts[0]);
                    } else {
                        error = true;
                    }
                    L1ItemInstance item2 = pc.getInventory().checkItemX(items[1], counts[1]);
                    if (item2 != null && !error) {
                        long remove = counts[1];
                        if (item2.getCount() >= 4L)
                            remove = item2.getCount() - 2L;
                        pc.getInventory().removeItem(item2, remove);
                    } else {
                        error = true;
                    }
                    if (!error) {
                        L1PolyMorph.undoPoly((L1Character)pc);
                        L1Teleport.teleport(pc, 32747, 32861, (short)9100, 5, true);
                        QuestClass.get().endQuest(pc, Chapter00.QUEST.get_id());
                    }
                    isCloseList = true;
                }
            } else if (cmd.equalsIgnoreCase("d")) {
                int[] items = { 49301, 49302, 49303, 49304, 49305, 49306, 49307, 49308, 49309, 49310 };
                int[] counts = { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };
                int[] gitems = { 49311 };
                int[] gcounts = { 1 };
                if (CreateNewItem.checkNewItem(pc,
                        items,
                        counts) <
                        1L) {
                    pc.sendPackets((ServerBasePacket)new S_NPCTalkReturn(npc.getId(), "j_html06"));
                } else {
                    CreateNewItem.createNewItem(pc,
                            items, counts,
                            gitems, 1L, gcounts);
                    pc.sendPackets((ServerBasePacket)new S_NPCTalkReturn(npc.getId(), "j_html04"));
                }
            } else if (cmd.equalsIgnoreCase("c")) {
                if (!pc.getInventory().checkItem(49312)) {
                    CreateNewItem.createNewItem(pc, 49312, 1L);
                    QuestClass.get().startQuest(pc, Chapter00.QUEST.get_id());
                    isCloseList = true;
                } else {
                    pc.sendPackets((ServerBasePacket)new S_NPCTalkReturn(npc.getId(), "j_html03"));
                }
            }
            if (isCloseList)
                pc.sendPackets((ServerBasePacket)new S_CloseList(pc.getId()));
        } catch (Exception e) {
            pc.sendPackets((ServerBasePacket)new S_NPCTalkReturn(npc.getId(), "j_html05"));
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
