package com.lineage.server.clientpackets;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.serverpackets.S_OwnCharPack;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class C_Ship extends ClientBasePacket {
    private static final Log _log = LogFactory.getLog(C_Ship.class);

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public void start(byte[] decrypt, ClientExecutor client) {
        try {
            read(decrypt);
            L1PcInstance pc = client.getActiveChar();
            int item_id = 0;
            switch (pc.getMapId()) {
                case 5:
                    item_id = 40299;
                    break;
                case 6:
                    item_id = 40298;
                    break;
                case 83:
                    item_id = 40300;
                    break;
                case 84:
                    item_id = 40301;
                    break;
                case 446:
                    item_id = 40303;
                    break;
                case 447:
                    item_id = 40302;
                    break;
            }
            if (item_id != 0) {
                pc.getInventory().consumeItem(item_id, 1);
                int shipMapId = readH();
                int locX = readH();
                int locY = readH();
                pc.sendPackets(new S_OwnCharPack(pc));
                L1Teleport.teleport(pc, locX, locY, (short) shipMapId, 0, false);
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
