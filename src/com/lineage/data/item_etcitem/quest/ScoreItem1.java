package com.lineage.data.item_etcitem.quest;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillSound;
import java.util.Random;

public class ScoreItem1 extends ItemExecutor {
    private final Random _random = new Random();

    private ScoreItem1() {
    }

    public static ItemExecutor get() {
        return new ScoreItem1();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        L1ItemInstance itemX;
        boolean isError = true;
        if (pc.getMapId() == 4) {
            int x = pc.getX();
            int y = pc.getY();
            if (x > 34026 && x < 34080 && y > 32235 && y < 32314 && (itemX = pc.getInventory().checkItemX(44049, 100)) != null) {
                pc.sendPacketsAll(new S_SkillSound(pc.getId(), 2944));
                pc.sendPacketsAll(new S_DoActionGFX(pc.getId(), 19));
                pc.getInventory().removeItem(itemX, 100);
                if (this._random.nextInt(100) < 95) {
                    CreateNewItem.createNewItem(pc, 41223, 1);
                } else {
                    pc.sendPackets(new S_ServerMessage(280));
                }
                isError = false;
            }
        }
        if (isError) {
            pc.sendPackets(new S_ServerMessage(79));
        }
    }
}
