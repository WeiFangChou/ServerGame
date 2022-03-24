package com.lineage.data.item_etcitem.xljnet;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.datatables.DropTable;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Object;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_SystemMessage;
import com.lineage.server.templates.L1Drop;
import com.lineage.server.templates.L1Item;
import com.lineage.server.world.World;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Npc_CarriedItems_Wand extends ItemExecutor {
    public static ItemExecutor get() {
        return new Npc_CarriedItems_Wand();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) {
        useNpcCarriedItemsWand(pc, data[0], item);
    }

    private void useNpcCarriedItemsWand(L1PcInstance pc, int targetId, L1ItemInstance item) {
        String blessed;
        L1Object target = World.get().findObject(targetId);
        if (target != null) {
            pc.sendPacketsAll(new S_DoActionGFX(pc.getId(), 17));
            if (target instanceof L1MonsterInstance) {
                L1NpcInstance tgnpc = (L1MonsterInstance) target;
                ArrayList<L1Drop> dropList = DropTable.get().getdroplist(tgnpc.getNpcId());
                if (dropList != null) {
                    String[] msgs = {"名稱：" + tgnpc.getName() + " 等級：" + tgnpc.getLevel() + "。", "血量：" + tgnpc.getMaxHp() + " 魔量：" + ((int) tgnpc.getMaxMp()) + "。", "防禦：" + tgnpc.getAc() + "。"};
                    Map<Integer, String> msgstable = new HashMap<>();
                    for (int i = 0; i < msgs.length; i++) {
                        msgstable.put(Integer.valueOf(i), msgs[i]);
                    }
                    String[] itemdatas = new String[dropList.size()];
                    for (int i2 = 0; i2 < dropList.size(); i2++) {
                        L1Drop drop = dropList.get(i2);
                        L1Item dropitem = ItemTable.get().getTemplate(drop.getItemid());
                        double dropchance = ((double) drop.getChance()) / 10000.0d;
                        if (dropitem.getBless() == 1) {
                            blessed = "";
                        } else if (dropitem.getBless() == 0) {
                            blessed = "祝福：";
                        } else {
                            blessed = "詛咒：";
                        }
                        NumberFormat nf = NumberFormat.getInstance();
                        nf.setMaximumFractionDigits(3);
                        itemdatas[i2] = String.valueOf(blessed) + dropitem.getName() + " 機率：" + nf.format(dropchance) + "%";
                        msgstable.put(Integer.valueOf(i2 + 3), itemdatas[i2]);
                    }
                    String[] msgs2 = new String[msgstable.size()];
                    for (int i3 = 0; i3 < msgstable.size(); i3++) {
                        msgs2[i3] = msgstable.get(Integer.valueOf(i3));
                    }
                    pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "mobdroplist", msgs2));
                    return;
                }
                pc.sendPackets(new S_SystemMessage("此怪物沒有掉寶資料。"));
                return;
            }
            pc.sendPackets(new S_SystemMessage("只能對怪物使用。"));
        }
    }
}
