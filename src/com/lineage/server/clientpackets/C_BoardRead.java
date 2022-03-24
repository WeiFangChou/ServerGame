package com.lineage.server.clientpackets;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.world.WorldNpc;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class C_BoardRead extends ClientBasePacket {
    private static final Log _log = LogFactory.getLog(C_BoardRead.class);

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public void start(byte[] decrypt, ClientExecutor client) throws Exception {
        try {
            read(decrypt);
            int objId = readD();
            int topicNumber = readD();
            L1NpcInstance npc = WorldNpc.get().map().get(Integer.valueOf(objId));
            if (npc != null) {
                L1PcInstance pc = client.getActiveChar();
                if (pc == null) {
                    over();
                    return;
                }
                if (npc.ACTION != null) {
                    npc.ACTION.action(pc, npc, "r", (long) topicNumber);
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
