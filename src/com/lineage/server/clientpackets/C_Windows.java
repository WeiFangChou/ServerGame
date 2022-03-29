package com.lineage.server.clientpackets;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_OwnCharStatus;
import com.lineage.server.serverpackets.S_PacketBoxLoc;
import com.lineage.server.serverpackets.S_PacketBoxMapTimer;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.world.World;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class C_Windows extends ClientBasePacket {
    private static final Log _log = LogFactory.getLog(C_Windows.class);

    public C_Windows() {
    }

    public void start(byte[] decrypt, ClientExecutor client) {
        try {
            this.read(decrypt);
            L1PcInstance pc = client.getActiveChar();
            int type = this.readC();
            switch(type) {
                case 0:
                    int objid = this.readD();
                    L1Object obj = World.get().findObject(objid);
                    if (obj instanceof L1PcInstance) {
                        L1PcInstance tgpc = (L1PcInstance)obj;
                        _log.warn("玩家:" + pc.getName() + " 申訴:(" + objid + ")" + tgpc.getName());
                    } else {
                        _log.warn("玩家:" + pc.getName() + " 申訴:NPC(" + objid + ")");
                    }
                    break;
                case 9:
                    pc.sendPackets(new S_PacketBoxMapTimer());
                    break;
                case 11:
                    String name = this.readS();
                    int mapid = this.readH();
                    int x = this.readH();
                    int y = this.readH();
                    int zone = this.readD();
                    L1PcInstance target = World.get().getPlayer(name);
                    if (target != null) {
                        target.sendPackets(new S_PacketBoxLoc(pc.getName(), mapid, x, y, zone));
                        pc.sendPackets(new S_ServerMessage(1783, name));
                    } else {
                        pc.sendPackets(new S_ServerMessage(1782));
                    }
                    break;
                case 44:
                    pc.setKillCount(0);
                    pc.sendPackets(new S_OwnCharStatus(pc));
            }
        } catch (Exception var16) {
            _log.error(var16.getLocalizedMessage(), var16);
        } finally {
            this.over();
        }

    }

    public String getType() {
        return this.getClass().getSimpleName();
    }
}
