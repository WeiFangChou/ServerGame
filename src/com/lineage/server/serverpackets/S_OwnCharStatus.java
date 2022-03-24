package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.gametime.L1GameTimeClock;

public class S_OwnCharStatus extends ServerBasePacket {
    private byte[] _byte = null;

    public S_OwnCharStatus(L1PcInstance pc) {
        int time = L1GameTimeClock.getInstance().currentTime().getSeconds();
        writeC(145);
        writeD(pc.getId());
        writeC(pc.getLevel());
        writeExp(pc.getExp());
        writeC(pc.getStr());
        writeC(pc.getInt());
        writeC(pc.getWis());
        writeC(pc.getDex());
        writeC(pc.getCon());
        writeC(pc.getCha());
        writeH(pc.getCurrentHp());
        writeH(pc.getMaxHp());
        writeH(pc.getCurrentMp());
        writeH(pc.getMaxMp());
        writeC(pc.getAc());
        writeD(time - (time % 300));
        writeC(pc.get_food());
        writeC(pc.getInventory().getWeight240());
        writeH(pc.getLawful());
        writeC(pc.getFire());
        writeC(pc.getWater());
        writeC(pc.getWind());
        writeC(pc.getEarth());
        writeD(pc.getKillCount());
    }

    public S_OwnCharStatus(L1PcInstance pc, int str) {
        int time = L1GameTimeClock.getInstance().currentTime().getSeconds();
        writeC(145);
        writeD(pc.getId());
        writeC(pc.getLevel());
        writeExp(pc.getExp());
        writeC(str);
        writeC(pc.getInt());
        writeC(pc.getWis());
        writeC(pc.getDex());
        writeC(pc.getCon());
        writeC(pc.getCha());
        writeH(pc.getCurrentHp());
        writeH(pc.getMaxHp());
        writeH(pc.getCurrentMp());
        writeH(pc.getMaxMp());
        writeC(pc.getAc());
        writeD(time - (time % 300));
        writeC(pc.get_food());
        writeC(pc.getInventory().getWeight240());
        writeH(pc.getLawful());
        writeC(pc.getFire());
        writeC(pc.getWater());
        writeC(pc.getWind());
        writeC(pc.getEarth());
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
