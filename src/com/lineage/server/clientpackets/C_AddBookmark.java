package com.lineage.server.clientpackets;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.datatables.lock.CharBookReading;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1CastleLocation;
import com.lineage.server.model.L1HouseLocation;
import com.lineage.server.model.L1TownLocation;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_ServerMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class C_AddBookmark extends ClientBasePacket {
    private static final Log _log = LogFactory.getLog(C_AddBookmark.class);

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
                    String locName = readS();
                    if (!pc.getMap().isMarkable() && !pc.isGm()) {
                        pc.sendPackets(new S_ServerMessage((int) L1SkillId.ILLUSION_DIA_GOLEM));
                    } else if (L1CastleLocation.checkInAllWarArea(pc.getX(), pc.getY(), pc.getMapId()) || L1HouseLocation.isInHouse(pc.getX(), pc.getY(), pc.getMapId()) || ((pc.getX() >= 33514 && pc.getX() <= 33809 && pc.getY() >= 32216 && pc.getY() <= 32457 && pc.getMapId() == 4) || ((pc.getX() >= 34211 && pc.getX() <= 34287 && pc.getY() >= 33103 && pc.getY() <= 33492 && pc.getMapId() == 4) || (pc.getX() >= 33461 && pc.getX() <= 33537 && pc.getY() >= 32829 && pc.getY() <= 32877 && pc.getMapId() == 4 && !pc.isGm())))) {
                        pc.sendPackets(new S_ServerMessage((int) L1SkillId.ILLUSION_DIA_GOLEM));
                    } else if (L1TownLocation.isGambling(pc)) {
                        pc.sendPackets(new S_ServerMessage((int) L1SkillId.ILLUSION_DIA_GOLEM));
                        over();
                        return;
                    } else {
                        CharBookReading.get().addBookmark(pc, locName);
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
