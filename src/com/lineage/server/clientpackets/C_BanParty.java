package com.lineage.server.clientpackets;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class C_BanParty extends ClientBasePacket {
    private static final Log _log = LogFactory.getLog(C_BanParty.class);

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public void start(byte[] decrypt, ClientExecutor client) {
        try {
            read(decrypt);
            L1PcInstance pc = client.getActiveChar();
            if (!pc.getParty().isLeader(pc)) {
                pc.sendPackets(new S_ServerMessage(427));
                return;
            }
            String userName = readS();
            ConcurrentHashMap<Integer, L1PcInstance> pcs = pc.getParty().partyUsers();
            if (pcs.isEmpty()) {
                over();
            } else if (pcs.size() <= 0) {
                over();
            } else {
                for (L1PcInstance member : pcs.values()) {
                    if (member.getName().toLowerCase().equals(userName.toLowerCase())) {
                        pc.getParty().kickMember(member);
                        over();
                        return;
                    }
                }
                pc.sendPackets(new S_ServerMessage(426, userName));
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
