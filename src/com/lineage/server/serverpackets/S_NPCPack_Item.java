package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1ItemInstance;

public class S_NPCPack_Item extends ServerBasePacket {
    private byte[] _byte = null;

    public S_NPCPack_Item(L1ItemInstance item) {
        buildPacket(item);
    }

    private void buildPacket(L1ItemInstance item) {
        int i;
        writeC(3);
        writeH(item.getX());
        writeH(item.getY());
        writeD(item.getId());
        writeH(item.getItem().getGroundGfxId());
        writeC(0);
        writeC(0);
        if (item.isNowLighting()) {
            i = item.getItem().getLightRange();
        } else {
            i = 0;
        }
        writeC(i);
        writeC(0);
        writeD((int) Math.min(item.getCount(), 2000000000L));
        writeH(0);
        String name = "";
        if (item.getCount() <= 1) {
            switch (item.getItemId()) {
                case 20383:
                case 41235:
                case 41236:
                    name = String.valueOf(item.getItem().getName()) + " [" + item.getChargeCount() + "]";
                    break;
                case 40006:
                case 40007:
                case 40008:
                case 40009:
                case 140006:
                case 140008:
                    if (item.isIdentified()) {
                        name = String.valueOf(item.getItem().getName()) + " (" + item.getChargeCount() + ")";
                        break;
                    }
                    break;
                default:
                    if (item.getItem().getLightRange() != 0 && item.isNowLighting()) {
                        name = String.valueOf(item.getItem().getName()) + " ($10)";
                        break;
                    } else {
                        name = item.getItem().getName();
                        break;
                    }

            }
        } else {
            name = String.valueOf(item.getItem().getName()) + " (" + item.getCount() + ")";
        }
        writeS(name);
        writeS(null);
        writeC(0);
        writeD(0);
        writeS(null);
        writeS(null);
        writeC(0);
        writeC(255);
        writeC(0);
        writeC(0);
        writeC(0);
        writeC(255);
        writeC(255);
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
