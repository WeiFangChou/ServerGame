package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1ItemInstance;
import java.util.List;

public class S_InvList extends ServerBasePacket {
    private byte[] _byte = null;

    public S_InvList(List<L1ItemInstance> items) {
        writeC(OpcodesServer.S_OPCODE_INVLIST);
        writeC(items.size());
        for (L1ItemInstance item : items) {
            writeD(item.getId());
            switch (item.getItem().getItemId()) {
                case 40318:
                    writeH(166);
                    break;
                case 40319:
                    writeH(569);
                    break;
                case 40321:
                    writeH(837);
                    break;
                case 49156:
                    writeH(3606);
                    break;
                case 49157:
                    writeH(3605);
                    break;
                case 49158:
                    writeH(3674);
                    break;
                default:
                    writeH(0);
                    break;
            }
            int type = item.getItem().getUseType();
            if (type < 0) {
                type = 0;
            } else if (type == 96 || type >= 98) {
                type = 26;
            } else if (type == 97) {
                type = 27;
            }
            writeC(type);
            if (item.getChargeCount() > 0) {
                writeC(item.getChargeCount());
            } else {
                writeC(0);
            }
            writeH(item.get_gfxid());
            writeC(item.getBless());
            writeD((int) Math.min(item.getCount(), 2000000000L));
            int statusX = 0;
            if (item.getBless() < 128) {
                statusX = item.isIdentified() ? 1 : statusX;
                statusX = !item.getItem().isTradable() ? statusX | 2 : statusX;
                statusX = item.getItem().isCantDelete() ? statusX | 4 : statusX;
                if (item.getItem().get_safeenchant() < 0 || item.getItem().getUseType() == -3 || item.getItem().getUseType() == -2) {
                    statusX |= 8;
                }
            } else if (item.isIdentified()) {
                statusX = 32 | 1 | 2 | 4 | 8;
            } else {
                statusX = 32 | 2 | 4 | 8;
            }
            writeC(statusX);
            writeS(item.getViewName());
            if (!item.isIdentified()) {
                writeC(0);
            } else {
                byte[] status = item.getStatusBytes();
                writeC(status.length);
                for (byte b : status) {
                    writeC(b);
                }
            }
            writeC(10);
            writeH(0);
            writeD(0);
            writeD(0);
        }
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
