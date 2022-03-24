package com.lineage.server.clientpackets;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.world.WorldNpc;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class C_BoardDelete extends ClientBasePacket {
    private static final Log _log = LogFactory.getLog(C_BoardDelete.class);

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public void start(byte[] decrypt, ClientExecutor client) {
        try {
            read(decrypt);
            int objId = readD();
            int topicId = readD();
            L1NpcInstance npc = WorldNpc.get().map().get(Integer.valueOf(objId));
            if (npc != null) {
                L1PcInstance pc = client.getActiveChar();
                if (pc == null) {
                    over();
                    return;
                }
                if (npc.ACTION != null) {
                    npc.ACTION.action(pc, npc, "d", (long) topicId);
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
