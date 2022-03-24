package com.lineage.server.clientpackets;

import com.lineage.echo.ClientExecutor;
import com.lineage.echo.encryptions.PacketPrint;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class C_Unkonwn extends ClientBasePacket {
    private static final Log _log = LogFactory.getLog(C_Unkonwn.class);

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public void start(byte[] decrypt, ClientExecutor client) {
        try {
            _log.info("未處理封包: " + (decrypt[0] & 255) + " (" + getNow_YMDHMS() + " 核心管理者紀錄用!)");
            _log.info(PacketPrint.get().printData(decrypt, decrypt.length));
        } catch (Exception ignored) {
        } finally {
            over();
        }
    }

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public String getType() {
        return getClass().getSimpleName();
    }

    private final String getNow_YMDHMS() {
        return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());
    }
}
