package com.lineage.data.item_etcitem;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_PacketBox;
import com.lineage.server.serverpackets.S_ServerMessage;

public class Food1 extends ItemExecutor {
    private Food1() {
    }

    public static ItemExecutor get() {
        return new Food1();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        int itemId = item.getItemId();
        pc.getInventory().removeItem(item, 1);
        int foodvolume1 =  (item.getItem().getFoodVolume() / 10);
        if (foodvolume1 <= 0) {
            foodvolume1 = 5;
        }
        if (pc.get_food() < 225) {
            int foodvolume2 =  (pc.get_food() + foodvolume1);
            if (foodvolume2 > 255) {
                foodvolume2 = 255;
            }
            pc.set_food(foodvolume2);
            pc.sendPackets(new S_PacketBox(11,  pc.get_food()));
        }
        if (itemId == 40057) {
            pc.setSkillEffect(L1SkillId.STATUS_FLOATING_EYE, 0);
        }
        pc.sendPackets(new S_ServerMessage(76, item.getItem().getName()));
    }
}
