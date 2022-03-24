package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import java.util.ArrayList;
import java.util.List;

public class S_FixWeaponList extends ServerBasePacket {
    private byte[] _byte = null;

    public S_FixWeaponList(List<L1ItemInstance> weaponList) {
        writeC(208);
        writeD(200);
        writeH(weaponList.size());
        for (L1ItemInstance weapon : weaponList) {
            writeD(weapon.getId());
            writeC(weapon.get_durability());
        }
    }

    public S_FixWeaponList(L1PcInstance pc) {
        buildPacket(pc);
    }

    private void buildPacket(L1PcInstance pc) {
        writeC(208);
        writeD(200);
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
        writeH(weaponList.size());
        for (L1ItemInstance weapon : weaponList) {
            writeD(weapon.getId());
            writeC(weapon.get_durability());
        }
    }

    public S_FixWeaponList(L1ItemInstance weapon) {
        writeC(208);
        writeD(200);
        writeH(1);
        writeD(weapon.getId());
        writeC(weapon.get_durability());
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
