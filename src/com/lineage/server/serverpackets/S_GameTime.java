package com.lineage.server.serverpackets;

import com.lineage.server.model.gametime.L1GameTimeClock;

public class S_GameTime extends ServerBasePacket {
    public S_GameTime(int time) {
        buildPacket(time);
    }

    public S_GameTime() {
        buildPacket(L1GameTimeClock.getInstance().currentTime().getSeconds());
    }

    private void buildPacket(int time) {
        writeC(194);
        writeD(time);
    }

    @Override // com.lineage.server.serverpackets.ServerBasePacket
    public byte[] getContent() {
        return getBytes();
    }
}
