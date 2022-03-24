package com.lineage.data.item_etcitem;

import com.lineage.config.ConfigRate;
import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_ServerMessage;
import java.util.Random;

public class Purification_Potion extends ItemExecutor {
    private Purification_Potion() {
    }

    public static ItemExecutor get() {
        return new Purification_Potion();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        L1ItemInstance item1 = pc.getInventory().getItem(data[0]);
        if (item1 != null) {
            Random random = new Random();
            int earingId = item1.getItem().getItemId();
            if (earingId < 40987 || 40989 < earingId) {
                pc.sendPackets(new S_ServerMessage(79));
                return;
            }
            if (random.nextInt(100) < ConfigRate.CREATE_CHANCE_RECOLLECTION) {
                CreateNewItem.createNewItem(pc, earingId + L1SkillId.BLOODLUST, 1);
            } else {
                pc.sendPackets(new S_ServerMessage((int) L1SkillId.NATURES_TOUCH, item1.getName()));
            }
            pc.getInventory().removeItem(item1, 1);
            pc.getInventory().removeItem(item, 1);
        }
    }
}
