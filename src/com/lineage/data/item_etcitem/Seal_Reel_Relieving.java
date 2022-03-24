package com.lineage.data.item_etcitem;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_ServerMessage;

public class Seal_Reel_Relieving extends ItemExecutor {
    private Seal_Reel_Relieving() {
    }

    public static ItemExecutor get() {
        return new Seal_Reel_Relieving();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        L1ItemInstance item1 = pc.getInventory().getItem(data[0]);
        if (item1 != null) {
            int lockItemId = item1.getItem().getItemId();
            if ((item1 == null || item1.getItem().getType2() != 1) && item1.getItem().getType2() != 2 && (item1.getItem().getType2() != 0 || (lockItemId != 40314 && lockItemId != 40316 && lockItemId != 41248 && lockItemId != 41249 && lockItemId != 41250 && lockItemId != 49037 && lockItemId != 49038 && lockItemId != 49039))) {
                pc.sendPackets(new S_ServerMessage(79));
            } else if (item1.getBless() == 128 || item1.getBless() == 129 || item1.getBless() == 130 || item1.getBless() == 131) {
                int bless = 1;
                switch (item1.getBless()) {
                    case 128:
                        bless = 0;
                        break;
                    case 129:
                        bless = 1;
                        break;
                    case L1SkillId.BODY_TO_MIND /*{ENCODED_INT: 130}*/:
                        bless = 2;
                        break;
                    case 131:
                        bless = 3;
                        break;
                }
                item1.setBless(bless);
                pc.getInventory().updateItem(item1, 512);
                pc.getInventory().saveItem(item1, 512);
                pc.getInventory().removeItem(item, 1);
            } else {
                pc.sendPackets(new S_ServerMessage(79));
            }
        }
    }
}
