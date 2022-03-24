package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1PcInstance;
import java.text.SimpleDateFormat;

public class S_NewCharPacket extends ServerBasePacket {
    private byte[] _byte = null;

    public S_NewCharPacket(L1PcInstance pc) {
        buildPacket(pc);
    }

    private void buildPacket(L1PcInstance pc) {
        writeC(212);
        writeS(pc.getName());
        writeS("");
        writeC(pc.getType());
        writeC(pc.get_sex());
        writeH(pc.getLawful());
        writeH(pc.getMaxHp());
        writeH(pc.getMaxMp());
        writeC(pc.getAc());
        writeC(pc.getLevel());
        writeC(pc.getStr());
        writeC(pc.getDex());
        writeC(pc.getCon());
        writeC(pc.getWis());
        writeC(pc.getCha());
        writeC(pc.getInt());
        writeC(0);
        String times = Integer.toHexString(Integer.parseInt(new SimpleDateFormat("yyyy-MM-dd").format(Long.valueOf(System.currentTimeMillis())).replace("-", "")));
        if (times.length() < 8) {
            times = "0" + times;
        }
        writeC(Integer.decode("0x" + times.substring(6, 8)).intValue());
        writeC(Integer.decode("0x" + times.substring(4, 6)).intValue());
        writeC(Integer.decode("0x" + times.substring(2, 4)).intValue());
        writeC(Integer.decode("0x" + times.substring(0, 2)).intValue());
        writeC(((((((pc.getLevel() ^ pc.getStr()) ^ pc.getDex()) ^ pc.getCon()) ^ pc.getWis()) ^ pc.getCha()) ^ pc.getInt()) & 255);
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
