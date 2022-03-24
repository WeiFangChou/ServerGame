package com.lineage.data.item_etcitem.reel;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_ServerMessage;
import java.util.Random;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Destroys_Helmet_Reel extends ItemExecutor {
    private static final Log _log = LogFactory.getLog(Destroys_Helmet_Reel.class);
    private static final Random _random = new Random();

    private Destroys_Helmet_Reel() {
    }

    public static ItemExecutor get() {
        return new Destroys_Helmet_Reel();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) {
        try {
            L1ItemInstance tgItem = pc.getInventory().getItem(data[0]);
            if (tgItem.getBless() >= 128) {
                pc.sendPackets(new S_ServerMessage(79));
                return;
            }
            int getItemId_1 = 0;
            int getItemId_2 = 0;
            if (tgItem.getItem().getType2() == 2) {
                switch (tgItem.getItem().getItemId()) {
                    case 20137:
                    case 120137:
                        getItemId_1 = 26001;
                        getItemId_2 = 26003;
                        break;
                    case 20138:
                        getItemId_1 = 26007;
                        getItemId_2 = 26009;
                        break;
                    case 26000:
                        getItemId_1 = 26004;
                        getItemId_2 = 26006;
                        break;
                }
                pc.getInventory().removeItem(item, 1);
                if (getItemId_1 == 0) {
                    pc.sendPackets(new S_ServerMessage(154));
                    return;
                }
                int random = _random.nextInt(L1SkillId.STATUS_BRAVE);
                if (random >= 980 && random < 1000) {
                    pc.sendPackets(new S_ServerMessage(3101, tgItem.getLogName()));
                    pc.getInventory().removeItem(tgItem, tgItem.getCount());
                    CreateNewItem.createNewItem(pc, getItemId_1, 1);
                    CreateNewItem.createNewItem(pc, getItemId_2, 1);
                } else if (random <= 750 || random >= 980) {
                    pc.sendPackets(new S_ServerMessage(3100, tgItem.getLogName()));
                } else {
                    pc.sendPackets(new S_ServerMessage((int) L1SkillId.EXOTIC_VITALIZE));
                    pc.getInventory().removeItem(tgItem, tgItem.getCount());
                }
            } else {
                pc.sendPackets(new S_ServerMessage(79));
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public void execute2(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        int msg;
        L1ItemInstance item1 = pc.getInventory().getItem(data[0]);
        if (item1 != null) {
            if (item1.getBless() >= 128) {
                pc.sendPackets(new S_ServerMessage(79));
                return;
            }
            if (item1.getItem().getType2() == 2) {
                switch (item1.getItem().getType()) {
                    case 1:
                        msg = L1SkillId.ELEMENTAL_FIRE;
                        break;
                    case 2:
                        msg = L1SkillId.EXOTIC_VITALIZE;
                        break;
                    case 3:
                        msg = 170;
                        break;
                    case 4:
                        msg = L1SkillId.IRON_SKIN;
                        break;
                    case 5:
                        msg = L1SkillId.STORM_WALK;
                        break;
                    case 6:
                        msg = 173;
                        break;
                    case 7:
                        msg = 174;
                        break;
                    default:
                        msg = 167;
                        break;
                }
                pc.sendPackets(new S_ServerMessage(msg));
                pc.getInventory().removeItem(item1, 1);
            } else {
                pc.sendPackets(new S_ServerMessage(154));
            }
            pc.getInventory().removeItem(item, 1);
        }
    }
}
