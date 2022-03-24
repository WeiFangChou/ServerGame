package com.lineage.server.clientpackets;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_PrivateShop;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class C_ShopList extends ClientBasePacket {
    private static final Log _log = LogFactory.getLog(C_ShopList.class);

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public void start(byte[] decrypt, ClientExecutor client) {
        try {
            read(decrypt);
            L1PcInstance pc = client.getActiveChar();
            if (!pc.isGhost()) {
                if (pc.isDead()) {
                    over();
                } else if (pc.isTeleport()) {
                    over();
                } else if (pc.isPrivateShop()) {
                    over();
                } else {
                    int mapId = pc.getMapId();
                    boolean isShopMap = false;
                    if (mapId == 340) {
                        isShopMap = true;
                    }
                    if (mapId == 350) {
                        isShopMap = true;
                    }
                    if (mapId == 360) {
                        isShopMap = true;
                    }
                    if (mapId == 370) {
                        isShopMap = true;
                    }
                    if (isShopMap) {
                        pc.sendPackets(new S_PrivateShop(pc, readD(), readC()));
                    }
                    over();
                }
            }
        } catch (Exception ignored) {
        } finally {
            over();
        }
    }

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public String getType() {
        return getClass().getSimpleName();
    }
}
