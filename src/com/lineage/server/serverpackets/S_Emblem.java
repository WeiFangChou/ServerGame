package com.lineage.server.serverpackets;

import com.lineage.server.datatables.lock.ClanEmblemReading;
import com.lineage.server.templates.L1EmblemIcon;

public class S_Emblem extends ServerBasePacket {
    private byte[] _byte = null;

    public S_Emblem(int clanid) {
        byte[] icon;
        L1EmblemIcon emblemIcon = ClanEmblemReading.get().get(clanid);
        if (emblemIcon != null) {
            writeC(50);
            writeD(clanid);
            for (byte b : emblemIcon.get_clanIcon()) {
                writeP(b);
            }
        }
    }

    public S_Emblem(L1EmblemIcon emblemIcon) {
        writeC(50);
        writeD(emblemIcon.get_clanid());
        writeByte(emblemIcon.get_clanIcon());
    }

    public S_Emblem(int clanid, byte[] clanIcon) {
        writeC(50);
        writeD(clanid);
        writeByte(clanIcon);
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
