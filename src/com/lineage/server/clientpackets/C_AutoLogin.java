package com.lineage.server.clientpackets;

import com.lineage.echo.ClientExecutor;

public class C_AutoLogin extends ClientBasePacket {
    private static final int AUTO_LOGIN = 6;

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public void start(byte[] decrypt, ClientExecutor client) {
        try {
            this.read(decrypt);
            if (readC() == 6) {
                new C_AuthLogin().checkLogin(client, readS().toLowerCase(), readS(), true);
            } else if (client.getActiveChar() == null) {
                over();
            } else {
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
