package com.lineage.data.item_etcitem;

import com.lineage.config.ConfigRate;
import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_ServerMessage;
import java.util.Random;

public class Glue extends ItemExecutor {
    private Glue() {
    }

    public static ItemExecutor get() {
        return new Glue();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        int itemId = item.getItemId();
        L1ItemInstance item1 = pc.getInventory().getItem(data[0]);
        if (item1 != null) {
            int diaryId = item1.getItem().getItemId();
            Random random = new Random();
            if (itemId != 41036) {
                return;
            }
            if (diaryId < 41038 || 41047 < diaryId) {
                pc.sendPackets(new S_ServerMessage(79));
                return;
            }
            pc.getInventory().removeItem(item1, 1);
            pc.getInventory().removeItem(item, 1);
            if (random.nextInt(99) + 1 <= ConfigRate.CREATE_CHANCE_DIARY) {
                CreateNewItem.createNewItem(pc, diaryId + 10, 1);
            } else {
                pc.sendPackets(new S_ServerMessage((int) L1SkillId.NATURES_TOUCH, item1.getName()));
            }
        }
    }
}
