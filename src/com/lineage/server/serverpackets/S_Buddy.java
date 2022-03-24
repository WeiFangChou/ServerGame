package com.lineage.server.serverpackets;

import com.lineage.server.datatables.lock.BuddyReading;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.templates.L1BuddyTmp;
import com.lineage.server.world.World;
import java.util.Iterator;

public class S_Buddy extends ServerBasePacket {
    private static final String _buddy = "buddy";
    private byte[] _byte = null;

    public S_Buddy(int objId) {
        buildPacket(objId);
    }

    private void buildPacket(int objId) {
        writeC(OpcodesServer.S_OPCODE_SHOWHTML);
        writeD(objId);
        writeS(_buddy);
        writeH(2);
        writeH(2);
        String result = new String("");
        String onlineBuddy = new String("");
        if (BuddyReading.get().userBuddy(objId) != null) {
            Iterator<L1BuddyTmp> it = BuddyReading.get().userBuddy(objId).iterator();
            while (it.hasNext()) {
                String buddy_name = it.next().get_buddy_name();
                result = String.valueOf(result) + buddy_name + " ";
                L1PcInstance find = World.get().getPlayer(buddy_name);
                if (find != null) {
                    onlineBuddy = String.valueOf(onlineBuddy) + find.getName() + " ";
                }
            }
        }
        writeS(result);
        writeS(onlineBuddy);
    }

    @Override // com.lineage.server.serverpackets.ServerBasePacket
    public byte[] getContent() {
        if (this._byte == null) {
            this._byte = getBytes();
        }
        return this._byte;
    }

    @Override // com.lineage.server.serverpackets.ServerBasePacket
    public String getType() {
        return getClass().getSimpleName();
    }
}
