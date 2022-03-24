package com.lineage.data.item_etcitem;

import com.lineage.config.ConfigRate;
import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_ServerMessage;
import java.util.Random;

public class Summoning_Center extends ItemExecutor {
    private Summoning_Center() {
    }

    public static ItemExecutor get() {
        return new Summoning_Center();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        int itemId = item.getItemId();
        L1ItemInstance item1 = pc.getInventory().getItem(data[0]);
        if (item1 != null) {
            Random _random = new Random();
            if (itemId == 41029) {
                int dantesId = item1.getItem().getItemId();
                if (dantesId < 41030 || 41034 < dantesId) {
                    pc.sendPackets(new S_ServerMessage(79));
                    return;
                }
                if (_random.nextInt(99) + 1 < ConfigRate.CREATE_CHANCE_DANTES) {
                    CreateNewItem.createNewItem(pc, dantesId + 1, 1);
                } else {
                    pc.sendPackets(new S_ServerMessage((int) L1SkillId.NATURES_TOUCH, item1.getName()));
                }
                pc.getInventory().removeItem(item1, 1);
                pc.getInventory().removeItem(item, 1);
            }
        }
    }
}
