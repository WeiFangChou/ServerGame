package com.lineage.server.clientpackets;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ChangeName;
import com.lineage.server.serverpackets.S_PacketBoxIcon1;
import com.lineage.server.serverpackets.S_PacketBoxSelect;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class C_NewCharSelect extends ClientBasePacket {
    private static final Log _log = LogFactory.getLog(C_NewCharSelect.class);

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public void start(byte[] decrypt, ClientExecutor client) {
        try {
            L1PcInstance pc = client.getActiveChar();
            if (pc != null) {
                if (pc.isDead()) {
                    over();
                    return;
                }
                pc.sendPackets(new S_ChangeName(pc, false));
                pc.sendPackets(new S_PacketBoxIcon1(true, pc.get_dodge()));
                Thread.sleep(250);
                client.quitGame();
                client.out().encrypt(new S_PacketBoxSelect());
                _log.info("角色切換: " + pc.getName());
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
