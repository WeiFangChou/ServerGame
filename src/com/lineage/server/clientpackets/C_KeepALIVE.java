//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.lineage.server.clientpackets;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.gametime.L1GameTimeClock;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.serverpackets.S_GameTime;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class C_KeepALIVE extends ClientBasePacket {
    private static final Log _log = LogFactory.getLog(C_KeepALIVE.class);

    public C_KeepALIVE() {
    }

    public void start(byte[] decrypt, ClientExecutor client) {
        try {
            L1PcInstance pc = client.getActiveChar();
            int serverTime = L1GameTimeClock.getInstance().currentTime().getSeconds();
            pc.sendPackets(new S_GameTime(serverTime));
            if (pc.isPrivateShop()) {
                int mapId = pc.getMapId();
                if (mapId != 340 && mapId != 350 && mapId != 360 && mapId != 370) {
                    pc.getSellList().clear();
                    pc.getBuyList().clear();
                    pc.setPrivateShop(false);
                    pc.sendPacketsAll(new S_DoActionGFX(pc.getId(), 3));
                }
            }
        } catch (Exception var9) {
        } finally {
            this.over();
        }

    }

    public String getType() {
        return this.getClass().getSimpleName();
    }
}
