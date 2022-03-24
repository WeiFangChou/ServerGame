package com.lineage.server.clientpackets;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_FixWeaponList;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class C_FixWeaponList extends ClientBasePacket {
    private static final Log _log = LogFactory.getLog(C_FixWeaponList.class);

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public void start(byte[] decrypt, ClientExecutor client) {
        try {
            L1PcInstance pc = client.getActiveChar();
            List<L1ItemInstance> weaponList = new ArrayList<>();
            for (L1ItemInstance item : pc.getInventory().getItems()) {
                switch (item.getItem().getType2()) {
                    case 1:
                        if (item.get_durability() > 0) {
                            weaponList.add(item);
                            break;
                        } else {
                            break;
                        }
                }
            }
            pc.sendPackets(new S_FixWeaponList(weaponList));
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
