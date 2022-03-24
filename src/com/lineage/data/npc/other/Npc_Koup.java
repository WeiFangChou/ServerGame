package com.lineage.data.npc.other;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.item.L1ItemId;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Npc_Koup extends NpcExecutor {
    private static final Log _log = LogFactory.getLog(Npc_Koup.class);

    private Npc_Koup() {
    }

    public static NpcExecutor get() {
        return new Npc_Koup();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 3;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void talk(L1PcInstance pc, L1NpcInstance npc) {
        try {
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "koup1"));
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void action(L1PcInstance pc, L1NpcInstance npc, String cmd, long amount) {
        boolean isCloseList = false;
        if (cmd.equalsIgnoreCase("request dark dualblade")) {
            isCloseList = CreateNewItem.getItem(pc, npc, cmd, new int[]{40321, 40408, 40406}, new int[]{100, 10, 20}, new int[]{75}, new int[]{1}, 1);
        } else if (cmd.equalsIgnoreCase("request dark claw")) {
            isCloseList = CreateNewItem.getItem(pc, npc, cmd, new int[]{40321, 40322, 40408, 40406}, new int[]{100, 5, 10, 10}, new int[]{158}, new int[]{1}, 1);
        } else if (cmd.equalsIgnoreCase("request dark crossbow")) {
            isCloseList = CreateNewItem.getItem(pc, npc, cmd, new int[]{40321, 40322, 40408, 40406}, new int[]{100, 10, 10, 30}, new int[]{168}, new int[]{1}, 1);
        } else if (cmd.equalsIgnoreCase("request blind dualblade")) {
            isCloseList = CreateNewItem.getItem(pc, npc, cmd, new int[]{75, 40408, 40406, 40322, 40323}, new int[]{1, 10, 20, 100, 5}, new int[]{81}, new int[]{1}, 1);
        } else if (cmd.equalsIgnoreCase("request blind claw")) {
            isCloseList = CreateNewItem.getItem(pc, npc, cmd, new int[]{L1SkillId.NATURES_TOUCH, 40408, 40406, 40322, 40323}, new int[]{1, 10, 10, 100, 10}, new int[]{162}, new int[]{1}, 1);
        } else if (cmd.equalsIgnoreCase("request blind crossbow")) {
            isCloseList = CreateNewItem.getItem(pc, npc, cmd, new int[]{L1SkillId.IRON_SKIN, 40408, 40406, 40322, 40323}, new int[]{1, 10, 30, 100, 20}, new int[]{177}, new int[]{1}, 1);
        } else if (cmd.equalsIgnoreCase("request silver dualblade")) {
            isCloseList = CreateNewItem.getItem(pc, npc, cmd, new int[]{75, 40408, 40406, 40321, 40323, 40044, 40467}, new int[]{1, 10, 20, 50, 1, 1, 20}, new int[]{74}, new int[]{1}, 1);
        } else if (cmd.equalsIgnoreCase("request silver claw")) {
            isCloseList = CreateNewItem.getItem(pc, npc, cmd, new int[]{L1SkillId.NATURES_TOUCH, 40408, 40406, 40321, 40323, 40044, 40467}, new int[]{1, 10, 10, 40, 1, 1, 30}, new int[]{157}, new int[]{1}, 1);
        } else if (cmd.equalsIgnoreCase("request black mithril")) {
            isCloseList = CreateNewItem.getItem(pc, npc, cmd, new int[]{40486, 40490, 40442, 40444, L1ItemId.ADENA}, new int[]{1, 1, 1, 5, L1SkillId.SEXP11}, new int[]{40443}, new int[]{1}, amount);
        } else if (cmd.equalsIgnoreCase("request black mithril arrow")) {
            isCloseList = CreateNewItem.getItem(pc, npc, cmd, new int[]{40507, 40443, 40440, L1ItemId.ADENA}, new int[]{10, 1, 1, L1SkillId.STATUS_BRAVE}, new int[]{40747}, new int[]{5000}, amount);
        } else if (cmd.equalsIgnoreCase("request lump of steel")) {
            isCloseList = CreateNewItem.getItem(pc, npc, cmd, new int[]{40899, 40408, L1ItemId.ADENA}, new int[]{5, 5, 500}, new int[]{40779}, new int[]{1}, amount);
        } else if (cmd.equalsIgnoreCase("request silver bar")) {
            isCloseList = CreateNewItem.getItem(pc, npc, cmd, new int[]{40468, L1ItemId.ADENA}, new int[]{10, 500}, new int[]{40467}, new int[]{1}, amount);
        } else if (cmd.equalsIgnoreCase("request gold bar")) {
            isCloseList = CreateNewItem.getItem(pc, npc, cmd, new int[]{40489, L1ItemId.ADENA}, new int[]{10, L1SkillId.STATUS_BRAVE}, new int[]{40488}, new int[]{1}, amount);
        } else if (cmd.equalsIgnoreCase("request platinum bar")) {
            isCloseList = CreateNewItem.getItem(pc, npc, cmd, new int[]{40441, L1ItemId.ADENA}, new int[]{10, L1SkillId.SEXP11}, new int[]{40440}, new int[]{1}, amount);
        } else if (cmd.equalsIgnoreCase("request silver plate")) {
            isCloseList = CreateNewItem.getItem(pc, npc, cmd, new int[]{40467, 40779, L1ItemId.ADENA}, new int[]{5, 3, L1SkillId.STATUS_BRAVE}, new int[]{40469}, new int[]{1}, amount);
        } else if (cmd.equalsIgnoreCase("request gold plate")) {
            isCloseList = CreateNewItem.getItem(pc, npc, cmd, new int[]{40488, 40779, L1ItemId.ADENA}, new int[]{5, 3, 3000}, new int[]{40487}, new int[]{1}, amount);
        } else if (cmd.equalsIgnoreCase("request platinum plate")) {
            isCloseList = CreateNewItem.getItem(pc, npc, cmd, new int[]{40440, 40779, L1ItemId.ADENA}, new int[]{5, 3, 10000}, new int[]{40439}, new int[]{1}, amount);
        } else if (cmd.equalsIgnoreCase("request black mithril plate")) {
            isCloseList = CreateNewItem.getItem(pc, npc, cmd, new int[]{40443, 40497, 40509, 40469, 40487, 40439, L1ItemId.ADENA}, new int[]{10, 1, 1, 1, 1, 1, 10000}, new int[]{40445}, new int[]{1}, amount);
        } else if (cmd.equalsIgnoreCase("request ancient blue dragon armor")) {
            isCloseList = CreateNewItem.getItem(pc, npc, cmd, new int[]{20127, 40445, 40051, 40413, L1ItemId.ADENA}, new int[]{1, 3, 30, 5, 50000}, new int[]{20153}, new int[]{1}, 1);
        } else if (cmd.equalsIgnoreCase("request ancient green dragon armor")) {
            isCloseList = CreateNewItem.getItem(pc, npc, cmd, new int[]{20146, 40445, 40048, 40162, L1ItemId.ADENA}, new int[]{1, 3, 30, 5, 50000}, new int[]{20130}, new int[]{1}, 1);
        } else if (cmd.equalsIgnoreCase("request ancient red dragon armor")) {
            isCloseList = CreateNewItem.getItem(pc, npc, cmd, new int[]{20159, 40445, 40049, 40409, L1ItemId.ADENA}, new int[]{1, 3, 30, 5, 50000}, new int[]{20119}, new int[]{1}, 1);
        } else if (cmd.equalsIgnoreCase("request ancient azure dragon armor")) {
            isCloseList = CreateNewItem.getItem(pc, npc, cmd, new int[]{20156, 40445, 40050, 40169, L1ItemId.ADENA}, new int[]{1, 3, 30, 5, 50000}, new int[]{20108}, new int[]{1}, 1);
        }
        if (isCloseList) {
            pc.sendPackets(new S_CloseList(pc.getId()));
        }
    }
}
