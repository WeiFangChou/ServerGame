package com.lineage.server.clientpackets;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.datatables.lock.CharBookReading;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class C_DeleteBookmark extends ClientBasePacket {
    private static final Log _log = LogFactory.getLog(C_DeleteBookmark.class);

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public void start(byte[] decrypt, ClientExecutor client) {
        try {
            read(decrypt);
            String bookmarkname = readS();
            if (!bookmarkname.isEmpty()) {
                CharBookReading.get().deleteBookmark(client.getActiveChar(), bookmarkname);
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
