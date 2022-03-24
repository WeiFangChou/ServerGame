package com.lineage.server.clientpackets;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_Lock;
import com.lineage.server.serverpackets.S_Paralysis;
import com.lineage.server.utils.Teleportation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class C_UnLock extends ClientBasePacket {
    private static final Log _log = LogFactory.getLog(C_UnLock.class);

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public void start(byte[] decrypt, ClientExecutor client) {
        try {
            read(decrypt);
            int type = readC();
            L1PcInstance pc = client.getActiveChar();
            if (type == 127) {
                int oleLocx = pc.getX();
                int oleLocy = pc.getY();
                pc.setOleLocX(oleLocx);
                pc.setOleLocY(oleLocy);
                pc.sendPackets(new S_Lock());
            } else {
                pc.sendPackets(new S_Paralysis(7, false));
                pc.setTeleportX(pc.getOleLocX());
                pc.setTeleportY(pc.getOleLocY());
                pc.setTeleportMapId(pc.getMapId());
                pc.setTeleportHeading(pc.getHeading());
                Teleportation.teleportation(pc);
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
