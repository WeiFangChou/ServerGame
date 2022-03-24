package com.lineage.data.item_etcitem.shop;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_Message_YN;
import com.lineage.server.serverpackets.S_ServerMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class UserName extends ItemExecutor {
    private static final Log _log = LogFactory.getLog(UserName.class);

    private UserName() {
    }

    public static ItemExecutor get() {
        return new UserName();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) {
        if (item == null || pc == null || pc.isGhost() || pc.isDead() || pc.isTeleport()) {
            return;
        }
        if (pc.isPrivateShop()) {
            pc.sendPackets(new S_ServerMessage("\\fT請先結束商店村模式!"));
        } else if (pc.getPetList().values().toArray().length > 0) {
            pc.sendPackets(new S_ServerMessage("\\fT請先回收寵物!"));
        } else if (!pc.getDolls().isEmpty()) {
            pc.sendPackets(new S_ServerMessage("\\fT請先回收魔法娃娃!"));
        } else if (pc.getParty() != null) {
            pc.sendPackets(new S_ServerMessage("\\fT請先退出隊伍!"));
        } else if (pc.getClanid() != 0) {
            pc.sendPackets(new S_ServerMessage("\\fT請先退出血盟!"));
        } else {
            try {
                pc.sendPackets(new S_Message_YN(325));
                pc.rename(true);
            } catch (Exception e) {
                _log.error(e.getLocalizedMessage(), e);
            }
        }
    }
}
