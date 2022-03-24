package com.lineage.server.clientpackets;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.world.WorldNpc;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class C_BoardWrite extends ClientBasePacket {
    private static final Log _log = LogFactory.getLog(C_BoardWrite.class);

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public void start(byte[] decrypt, ClientExecutor client) {
        try {
            read(decrypt);
            L1PcInstance pc = client.getActiveChar();
            if (!pc.isGhost()) {
                if (pc.isDead()) {
                    over();
                } else if (pc.isTeleport()) {
                    over();
                } else {
                    L1NpcInstance npc = WorldNpc.get().map().get(Integer.valueOf(readD()));
                    if (npc == null) {
                        over();
                        return;
                    }
                    String title = readS();
                    if (title.length() > 16) {
                        pc.sendPackets(new S_ServerMessage(166, "標題過長"));
                        over();
                        return;
                    }
                    String content = readS();
                    if (title.length() > 1000) {
                        pc.sendPackets(new S_ServerMessage(166, "內容過長"));
                        over();
                        return;
                    }
                    if (npc.ACTION != null) {
                        pc.set_board_title(title);
                        pc.set_board_content(content);
                        npc.ACTION.action(pc, npc, "w", 0);
                    }
                    over();
                }
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
