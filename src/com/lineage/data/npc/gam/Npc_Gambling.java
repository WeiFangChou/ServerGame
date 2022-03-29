package com.lineage.data.npc.gam;

import com.lineage.data.event.gambling.Gambling;
import com.lineage.data.event.gambling.GamblingNpc;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.datatables.lock.GamblingReading;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_NoSell;
import com.lineage.server.serverpackets.S_ShopBuyListGam;
import com.lineage.server.serverpackets.S_ShopSellListGam;
import com.lineage.server.serverpackets.ServerBasePacket;
import com.lineage.server.templates.L1Gambling;
import com.lineage.server.timecontroller.event.GamblingTime;
import com.lineage.server.utils.RangeLong;
import java.util.HashMap;
import java.util.Map;

public class Npc_Gambling extends NpcExecutor {
    public static NpcExecutor get() {
        return new Npc_Gambling();
    }

    public int type() {
        return 3;
    }

    public void talk(L1PcInstance pc, L1NpcInstance npc) {
        Gambling gambling = GamblingTime.get_gambling();
        String[] info = null;
        if (gambling == null) {
            pc.sendPackets((ServerBasePacket)new S_NPCTalkReturn(npc.getId(), "y_g_03", info));
            return;
        }
        if (npc.getNpcId() == 91172) {
            L1Teleport.teleport(pc, 33529, 32856, 4, 5, true);
            return;
        }
        String no = String.valueOf(String.valueOf(GamblingTime.get_gamblingNo())) + "~" + RangeLong.scount(gambling.get_allRate()) + "~";
        for (Integer key : GamblingTime.get_gambling().get_allNpc().keySet()) {
            GamblingNpc o = (GamblingNpc)GamblingTime.get_gambling().get_allNpc().get(key);
            String name = o.get_npc().getNameId();
            long adena = o.get_adena();
            no = String.valueOf(no) + name + " [" + RangeLong.scount(adena) + "]~";
        }
        if (GamblingTime.isStart()) {
            pc.sendPackets((ServerBasePacket)new S_NPCTalkReturn(npc.getId(), "y_g_E", no.split("~")));
            return;
        }
        pc.sendPackets((ServerBasePacket)new S_NPCTalkReturn(npc.getId(), "y_g_01", no.split("~")));
    }

    public void action(L1PcInstance pc, L1NpcInstance npc, String cmd, long amount) {
        if (GamblingTime.isStart()) {
            pc.sendPackets((ServerBasePacket)new S_CloseList(pc.getId()));
            return;
        }
        if (cmd.equals("a")) {
            sell(pc, npc);
        } else if (cmd.equals("b")) {
            pc.sendPackets((ServerBasePacket)new S_ShopSellListGam(pc, npc));
        } else if (cmd.equals("c")) {
            reading(pc, npc);
        }
    }

    private void reading(L1PcInstance pc, L1NpcInstance npc) {
        StringBuilder stringBuilder = new StringBuilder();
        Gambling gambling = GamblingTime.get_gambling();
        Map<Integer, GamblingNpc> npclist = gambling.get_allNpc();
        for (GamblingNpc gamblingNpc : npclist.values()) {
            stringBuilder.append(String.valueOf(gamblingNpc.get_npc().getNameId()) + "~");
            int npcid = gamblingNpc.get_npc().getNpcId();
            int[] x = GamblingReading.get().winCount(npcid);
            int all = x[0];
            if (all == 0)
                all = 1;
            int win = x[1];
            if (win == 0)
                win = all;
            double rate = (all / win);
            stringBuilder.append(String.valueOf(rate) + "~");
            StringBuilder adena = RangeLong.scount(gamblingNpc.get_adena());
            stringBuilder.append(adena + "~");
        }
        String[] clientStrAry = stringBuilder.toString().split("~");
        pc.sendPackets((ServerBasePacket)new S_NPCTalkReturn(npc.getId(), "y_g_02", clientStrAry));
    }

    private void sell(L1PcInstance pc, L1NpcInstance npc) {
        Map<Integer, L1Gambling> sellList = new HashMap<>();
        L1ItemInstance[] items = pc.getInventory().findItemsId(40309);
        if (items.length <= 0) {
            pc.sendPackets((ServerBasePacket)new S_NoSell(npc));
            sellList.clear();
            return;
        }
        byte b;
        int i;
        L1ItemInstance[] arrayOfL1ItemInstance1;
        for (i = (arrayOfL1ItemInstance1 = items).length, b = 0; b < i; ) {
            L1ItemInstance item = arrayOfL1ItemInstance1[b];
            L1Gambling gam = GamblingReading.get().getGambling(item.getGamNo());
            if (gam != null) {
                int objid = item.getId();
                sellList.put(new Integer(objid), gam);
            }
            b++;
        }
        if (sellList.size() > 0) {
            pc.sendPackets((ServerBasePacket)new S_ShopBuyListGam(pc, npc, sellList));
            pc.get_otherList().set_gamSellList(sellList);
        } else {
            pc.sendPackets((ServerBasePacket)new S_NoSell(npc));
        }
        sellList.clear();
    }
}
