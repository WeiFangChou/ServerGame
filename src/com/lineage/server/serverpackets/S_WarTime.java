package com.lineage.server.serverpackets;

import com.lineage.config.Config;
import com.lineage.server.datatables.lock.CastleReading;
import java.util.Calendar;

public class S_WarTime extends ServerBasePacket {
    private byte[] _byte = null;

    public S_WarTime(Calendar cal) {
        Calendar base_cal = Calendar.getInstance();
        base_cal.set(1997, 0, 1, 17, 0);
        int time = (int) ((((cal.getTimeInMillis() - base_cal.getTimeInMillis()) - 72000000) / 60000) / 182);
        writeC(49);
        writeH(6);
        writeS(Config.TIME_ZONE);
        writeC(0);
        writeC(0);
        writeC(0);
        writeD(time);
        writeC(0);
        writeD(time - 1);
        writeC(0);
        writeD(time - 2);
        writeC(0);
        writeD(time - 3);
        writeC(0);
        writeD(time - 4);
        writeC(0);
        writeD(time - 5);
        writeC(0);
    }

    public S_WarTime(int op) {
        Calendar cal = CastleReading.get().getCastleTable(5).getWarTime();
        Calendar base_cal = Calendar.getInstance();
        base_cal.set(1997, 0, 1, 17, 0);
        int time = (int) ((((cal.getTimeInMillis() - base_cal.getTimeInMillis()) - 72000000) / 60000) / 182);
        writeC(op);
        writeH(6);
        writeS(Config.TIME_ZONE);
        writeC(0);
        writeC(0);
        writeC(0);
        writeD(time);
        writeC(0);
        writeD(time - 1);
        writeC(0);
        writeD(time - 2);
        writeC(0);
        writeD(time - 3);
        writeC(0);
        writeD(time - 4);
        writeC(0);
        writeD(time - 5);
        writeC(0);
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
