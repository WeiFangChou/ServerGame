package com.lineage.server.clientpackets;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.serverpackets.S_ServerVersion;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class C_ServerVersion extends ClientBasePacket {
    private static final Log _log = LogFactory.getLog(C_ServerVersion.class);

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public void start(byte[] decrypt, ClientExecutor client) {
        try {
            read(decrypt);
            client.out().encrypt(new S_ServerVersion());
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
