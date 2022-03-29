package com.lineage.server.clientpackets;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.datatables.GetbackTable;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CharVisualUpdate;
import com.lineage.server.serverpackets.S_MapID;
import com.lineage.server.serverpackets.S_OtherCharPacks;
import com.lineage.server.serverpackets.S_OwnCharPack;
import com.lineage.server.serverpackets.S_PacketBoxIcon1;
import com.lineage.server.serverpackets.S_RemoveObject;
import com.lineage.server.serverpackets.S_Weather;
import com.lineage.server.world.World;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class C_Restart extends ClientBasePacket {
    private static final Log _log = LogFactory.getLog(C_Restart.class);

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public void start(byte[] decrypt, ClientExecutor client) {
        try {
            L1PcInstance pc = client.getActiveChar();
            if (pc != null) {
                int[] loc = pc.getHellTime() > 0 ? new int[]{32701, 32777, 666} : GetbackTable.GetBack_Location(pc, true);
                pc.stopPcDeleteTimer();
                pc.removeAllKnownObjects();
                pc.broadcastPacketAll(new S_RemoveObject(pc));
                pc.setCurrentHp(pc.getLevel());
                pc.set_food(40);
                pc.setStatus(0);
                World.get().moveVisibleObject(pc, loc[2]);
                pc.setX(loc[0]);
                pc.setY(loc[1]);
                pc.setMap( loc[2]);
                pc.set_showId(-1);
                pc.sendPackets(new S_MapID(pc, pc.getMapId(), pc.getMap().isUnderwater()));
                pc.broadcastPacketAll(new S_OtherCharPacks(pc));
                pc.sendPackets(new S_OwnCharPack(pc));
                pc.sendPackets(new S_CharVisualUpdate(pc));
                pc.startHpRegeneration();
                pc.startMpRegeneration();
                pc.sendPackets(new S_Weather(World.get().getWeather()));
                pc.sendPackets(new S_PacketBoxIcon1(true, pc.get_dodge()));
                if (pc.getHellTime() > 0) {
                    pc.beginHell(false);
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
