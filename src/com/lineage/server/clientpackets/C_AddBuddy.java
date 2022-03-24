package com.lineage.server.clientpackets;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.datatables.CharObjidTable;
import com.lineage.server.datatables.lock.BuddyReading;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1BuddyTmp;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class C_AddBuddy extends ClientBasePacket {
    private static final Log _log = LogFactory.getLog(C_AddBuddy.class);

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public void start(byte[] decrypt, ClientExecutor client) {
        try {
            read(decrypt);
            String charName = readS().toLowerCase();
            L1PcInstance pc = client.getActiveChar();
            ArrayList<L1BuddyTmp> list = BuddyReading.get().userBuddy(pc.getId());
            if (list != null) {
                if (!charName.equalsIgnoreCase(pc.getName())) {
                    Iterator<L1BuddyTmp> it = list.iterator();
                    while (it.hasNext()) {
                        if (charName.equalsIgnoreCase(it.next().get_buddy_name())) {
                            over();
                            return;
                        }
                    }
                } else {
                    return;
                }
            }
            int objid = CharObjidTable.get().charObjid(charName);
            if (objid != 0) {
                BuddyReading.get().addBuddy(pc.getId(), objid, CharObjidTable.get().isChar(objid));
                over();
                return;
            }
            pc.sendPackets(new S_ServerMessage(109, charName));
            over();
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
