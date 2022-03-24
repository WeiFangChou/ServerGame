package com.lineage.server.clientpackets;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Party;
import com.lineage.server.serverpackets.S_Party;
import com.lineage.server.serverpackets.S_ServerMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class C_Party extends ClientBasePacket {
    private static final Log _log = LogFactory.getLog(C_Party.class);

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public void start(byte[] decrypt, ClientExecutor client) {
        try {
            L1PcInstance pc = client.getActiveChar();
            if (!pc.isGhost()) {
                L1Party party = pc.getParty();
                if (pc.isInParty()) {
                    pc.sendPackets(new S_Party("party", pc.getId(), party.getLeader().getName(), party.getMembersNameList()));
                } else {
                    pc.sendPackets(new S_ServerMessage(425));
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
