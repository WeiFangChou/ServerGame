package com.lineage.server.clientpackets;

import com.lineage.echo.ClientExecutor;
import com.lineage.list.OnlineUser;
import com.lineage.server.templates.L1Account;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class C_ReturnToLogin extends ClientBasePacket {
    private static final Log _log = LogFactory.getLog(C_ReturnToLogin.class);

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public void start(byte[] decrypt, ClientExecutor client) {
        try {
            if (client.getActiveChar() != null) {
                client.quitGame();
            }
            L1Account account = client.getAccount();
            if (account != null) {
                OnlineUser.get().remove(account.get_login());
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
