package com.lineage.data.item_etcitem.shop;

import com.lineage.config.ConfigOther;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class UserTitle extends ItemExecutor {
    private static final Log _log = LogFactory.getLog(UserTitle.class);

    private UserTitle() {
    }

    public static ItemExecutor get() {
        return new UserTitle();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) {
        if (item == null || pc == null || pc.isGhost() || pc.isDead() || pc.isTeleport()) {
            return;
        }
        if (!ConfigOther.CLANTITLE) {
            pc.sendPackets(new S_ServerMessage("\\fT尚未開放"));
        } else if (pc.isPrivateShop()) {
            pc.sendPackets(new S_ServerMessage("\\fT請先結束商店村模式!"));
        } else {
            try {
                pc.retitle(true);
                pc.sendPackets(new S_ServerMessage("\\fR輸入封號後直接按下ENTER"));
            } catch (Exception e) {
                _log.error(e.getLocalizedMessage(), e);
            }
        }
    }
}
