package com.lineage.server.clientpackets;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.datatables.lock.BuddyReading;
import com.lineage.server.model.Instance.L1PcInstance;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class C_DelBuddy extends ClientBasePacket {
    private static final Log _log = LogFactory.getLog(C_DelBuddy.class);

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public void start(byte[] decrypt, ClientExecutor client) {
        try {
            read(decrypt);
            L1PcInstance pc = client.getActiveChar();
            String charName = readS().toLowerCase();
            if (!charName.isEmpty()) {
                BuddyReading.get().removeBuddy(pc.getId(), charName);
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
