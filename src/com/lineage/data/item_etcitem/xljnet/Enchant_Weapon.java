package com.lineage.data.item_etcitem.xljnet;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ItemName;
import com.lineage.server.serverpackets.S_ItemStatus;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SystemMessage;
import com.lineage.server.world.World;

public class Enchant_Weapon extends ItemExecutor {
    private int _type = 0;

    public static ItemExecutor get() {
        return new Enchant_Weapon();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        L1ItemInstance tgItem = pc.getInventory().getItem(data[0]);
        if (tgItem == null) {
            return;
        }
        if (tgItem.getItem().get_safeenchant() <= -1) {
            pc.sendPackets(new S_ServerMessage("無法使用防武卷強化的道具。"));
        } else if (tgItem.getproctect() || tgItem.getproctect0() || tgItem.getproctect1()) {
            pc.sendPackets(new S_ServerMessage("+" + tgItem.getEnchantLevel() + " " + tgItem.getName() + " 已是保護狀態。"));
        } else if (tgItem.getItem().getType2() == 0 || tgItem.getItem().getType2() == 2) {
            pc.sendPackets(new S_ServerMessage("武器才能受到保護。"));
        } else if (tgItem.getEnchantLevel() < tgItem.getItem().get_safeenchant()) {
            pc.sendPackets(new S_ServerMessage("+" + tgItem.getEnchantLevel() + " " + tgItem.getName() + " 還在安定值內，不需要保護。"));
        } else {
            switch (this._type) {
                case 1:
                    tgItem.setproctect(true);
                    break;
                case 2:
                    tgItem.setproctect0(true);
                    break;
                case 3:
                    tgItem.setproctect1(true);
                    break;
            }
            pc.sendPackets(new S_ItemName(tgItem));
            pc.sendPackets(new S_ItemStatus(tgItem));
            pc.sendPackets(new S_ServerMessage(String.valueOf(tgItem.getLogName()) + " 已受到保護。"));
            World.get().broadcastPacketToAll(new S_SystemMessage("玩家：【" + pc.getName() + "】使用：【" + item.getName() + "】(" + tgItem.getLogName() + ")已受到保護。"));
            pc.getInventory().removeItem(item, 1);
        }
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void set_set(String[] set) {
        try {
            this._type = Integer.parseInt(set[1]);
        } catch (Exception ignored) {
        }
    }
}
