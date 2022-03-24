package com.lineage.data.npc.other;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.DarkElfLv50_2;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.item.L1ItemId;
import com.lineage.server.serverpackets.OpcodesServer;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_ItemCount;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Npc_Karif extends NpcExecutor {
    private static final Log _log = LogFactory.getLog(Npc_Karif.class);

    private Npc_Karif() {
    }

    public static NpcExecutor get() {
        return new Npc_Karif();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 3;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void talk(L1PcInstance pc, L1NpcInstance npc) {
        try {
            if (pc.isCrown()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "karif9"));
            } else if (pc.isKnight()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "karif9"));
            } else if (pc.isElf()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "karif9"));
            } else if (pc.isWizard()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "karif9"));
            } else if (pc.isDarkelf()) {
                if (pc.getQuest().isEnd(DarkElfLv50_2.QUEST.get_id())) {
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "karif9"));
                } else if (pc.getLevel() >= DarkElfLv50_2.QUEST.get_questlevel()) {
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "karif3a"));
                } else {
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "karif9"));
                }
            } else if (pc.isDragonKnight()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "karif9"));
            } else if (pc.isIllusionist()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "karif9"));
            } else {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "karif9"));
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void action(L1PcInstance pc, L1NpcInstance npc, String cmd, long amount) {
        boolean isCloseList = false;
        if (pc.isDarkelf()) {
            if (cmd.equalsIgnoreCase("quest 32 karif4")) {
                isCloseList = getItem1(pc);
            } else if (cmd.equalsIgnoreCase("quest 32 karif5")) {
                isCloseList = getItem2(pc);
            } else if (cmd.equalsIgnoreCase("quest 32 karif6")) {
                isCloseList = getItem3(pc);
            } else if (cmd.equalsIgnoreCase("request darkness dualblade")) {
                isCloseList = getItem1(pc);
            } else if (cmd.equalsIgnoreCase("request darkness claw")) {
                isCloseList = getItem2(pc);
            } else if (cmd.equalsIgnoreCase("request darkness crossbow")) {
                isCloseList = getItem3(pc);
            }
        }
        if (isCloseList) {
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "karif9"));
            return;
        }
        if (cmd.equalsIgnoreCase("request karif bag1")) {
            isCloseList = requestKarifBag(pc, npc, new int[]{40044}, new int[]{1}, new int[]{49005}, new int[]{1}, "a1");
        } else if (cmd.equalsIgnoreCase("a1")) {
            isCloseList = getKarifBag(pc, new int[]{40044}, new int[]{1}, new int[]{49005}, new int[]{1}, amount);
        } else if (cmd.equalsIgnoreCase("request karif bag2")) {
            isCloseList = requestKarifBag(pc, npc, new int[]{40047}, new int[]{1}, new int[]{49008}, new int[]{1}, "a2");
        } else if (cmd.equalsIgnoreCase("a2")) {
            isCloseList = getKarifBag(pc, new int[]{40047}, new int[]{1}, new int[]{49008}, new int[]{1}, amount);
        } else if (cmd.equalsIgnoreCase("request karif bag3")) {
            isCloseList = requestKarifBag(pc, npc, new int[]{40045}, new int[]{1}, new int[]{49006}, new int[]{1}, "a3");
        } else if (cmd.equalsIgnoreCase("a3")) {
            isCloseList = getKarifBag(pc, new int[]{40045}, new int[]{1}, new int[]{49006}, new int[]{1}, amount);
        } else if (cmd.equalsIgnoreCase("request karif bag4")) {
            isCloseList = requestKarifBag(pc, npc, new int[]{40046}, new int[]{1}, new int[]{49007}, new int[]{1}, "a4");
        } else if (cmd.equalsIgnoreCase("a4")) {
            isCloseList = getKarifBag(pc, new int[]{40046}, new int[]{1}, new int[]{49007}, new int[]{1}, amount);
        } else if (cmd.equalsIgnoreCase("request karif bag5")) {
            isCloseList = requestKarifBag(pc, npc, new int[]{40048}, new int[]{1}, new int[]{49009}, new int[]{1}, "a5");
        } else if (cmd.equalsIgnoreCase("a5")) {
            isCloseList = getKarifBag(pc, new int[]{40048}, new int[]{1}, new int[]{49009}, new int[]{1}, amount);
        } else if (cmd.equalsIgnoreCase("request karif bag6")) {
            isCloseList = requestKarifBag(pc, npc, new int[]{40051}, new int[]{1}, new int[]{49010}, new int[]{1}, "a6");
        } else if (cmd.equalsIgnoreCase("a6")) {
            isCloseList = getKarifBag(pc, new int[]{40051}, new int[]{1}, new int[]{49010}, new int[]{1}, amount);
        } else if (cmd.equalsIgnoreCase("request karif bag7")) {
            isCloseList = requestKarifBag(pc, npc, new int[]{40049}, new int[]{1}, new int[]{49011}, new int[]{1}, "a7");
        } else if (cmd.equalsIgnoreCase("a7")) {
            isCloseList = getKarifBag(pc, new int[]{40049}, new int[]{1}, new int[]{49011}, new int[]{1}, amount);
        } else if (cmd.equalsIgnoreCase("request karif bag8")) {
            isCloseList = requestKarifBag(pc, npc, new int[]{40050}, new int[]{1}, new int[]{49012}, new int[]{1}, "a8");
        } else if (cmd.equalsIgnoreCase("a8")) {
            isCloseList = getKarifBag(pc, new int[]{40050}, new int[]{1}, new int[]{49012}, new int[]{1}, amount);
        }
        if (isCloseList) {
            pc.sendPackets(new S_CloseList(pc.getId()));
        }
    }

    private boolean getItem3(L1PcInstance pc) {
        return getItem(pc, new int[]{40466, 40054, L1ItemId.ADENA, 40403, 40413, 40525, OpcodesServer.S_OPCODE_SELECTTARGET}, new int[]{1, 3, 100000, 10, 9, 3, 1}, new int[]{189}, new int[]{1}, 1);
    }

    private boolean getItem2(L1PcInstance pc) {
        return getItem(pc, new int[]{40466, 40055, L1ItemId.ADENA, 40404, 40413, 40525, 162}, new int[]{1, 3, 100000, 10, 9, 3, 1}, new int[]{164}, new int[]{1}, 1);
    }

    private boolean getItem1(L1PcInstance pc) {
        return getItem(pc, new int[]{40466, 40053, L1ItemId.ADENA, 40402, 40413, 40525, 81}, new int[]{1, 3, 100000, 10, 9, 3, 1}, new int[]{84}, new int[]{1}, 1);
    }

    private boolean getItem(L1PcInstance pc, int[] items, int[] counts, int[] gitems, int[] gcounts, long amount) {
        if (CreateNewItem.checkNewItem(pc, items, counts) >= 1) {
            CreateNewItem.createNewItem(pc, items, counts, gitems, amount, gcounts);
        }
        return true;
    }

    private boolean getKarifBag(L1PcInstance pc, int[] items, int[] counts, int[] gitems, int[] gcounts, long amount) {
        if (CreateNewItem.checkNewItem(pc, items, counts) < amount) {
            return true;
        }
        CreateNewItem.createNewItem(pc, items, counts, gitems, amount, gcounts);
        return true;
    }

    private boolean requestKarifBag(L1PcInstance pc, L1NpcInstance npc, int[] items, int[] counts, int[] gitems, int[] gcounts, String string) {
        long xcount = CreateNewItem.checkNewItem(pc, items, counts);
        if (xcount == 1) {
            CreateNewItem.createNewItem(pc, items, counts, gitems, 1, gcounts);
            return true;
        } else if (xcount > 1) {
            pc.sendPackets(new S_ItemCount(npc.getId(), (int) xcount, string));
            return false;
        } else if (xcount < 1) {
            return true;
        } else {
            return false;
        }
    }
}
