package com.lineage.server.clientpackets;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1ExcludingList;
import com.lineage.server.serverpackets.S_PacketBox;
import com.lineage.server.serverpackets.S_ServerMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class C_Exclude extends ClientBasePacket {
    private static final Log _log = LogFactory.getLog(C_Exclude.class);

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public void start(byte[] decrypt, ClientExecutor client) {
        try {
            read(decrypt);
            String name = readS();
            if (!name.isEmpty()) {
                L1PcInstance pc = client.getActiveChar();
                L1ExcludingList exList = pc.getExcludingList();
                if (exList.isFull()) {
                    pc.sendPackets(new S_ServerMessage(472));
                    over();
                    return;
                }
                if (exList.contains(name)) {
                    pc.sendPackets(new S_PacketBox(19, exList.remove(name)));
                } else {
                    exList.add(name);
                    pc.sendPackets(new S_PacketBox(18, name));
                }
                over();
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
