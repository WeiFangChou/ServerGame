package com.lineage.server.clientpackets;

import com.lineage.echo.ClientExecutor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class C_HireSoldier extends ClientBasePacket {
    private static final Log _log = LogFactory.getLog(C_HireSoldier.class);

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public void start(byte[] decrypt, ClientExecutor client) {
        try {
            read(decrypt);
            readH();
            readH();
            readD();
            readD();
            readH();
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
