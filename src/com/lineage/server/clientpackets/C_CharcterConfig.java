package com.lineage.server.clientpackets;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.datatables.lock.CharacterConfigReading;
import com.lineage.server.model.Instance.L1PcInstance;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class C_CharcterConfig extends ClientBasePacket {
    private static final Log _log = LogFactory.getLog(C_CharcterConfig.class);

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public void start(byte[] decrypt, ClientExecutor client) {
        try {
            read(decrypt);
            L1PcInstance pc = client.getActiveChar();
            if (pc != null) {
                int objid = pc.getId();
                int length = readD() - 3;
                byte[] data = readByte();
                if (CharacterConfigReading.get().get(objid) == null) {
                    CharacterConfigReading.get().storeCharacterConfig(objid, length, data);
                } else {
                    CharacterConfigReading.get().updateCharacterConfig(objid, length, data);
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
