package com.lineage.server.serverpackets;

import java.util.concurrent.atomic.AtomicInteger;

public class S_Message_YN extends ServerBasePacket {
    private static AtomicInteger _sequentialNumber = new AtomicInteger(1);
    private byte[] _byte = null;

    public S_Message_YN(int type) {
        writeC(155);
        writeH(0);
        writeD(0);
        writeH(type);
    }

    public S_Message_YN(String name) {
        writeC(155);
        writeH(0);
        writeD(_sequentialNumber.incrementAndGet());
        writeH(OpcodesServer.S_OPCODE_PINKNAME);
        writeS(name);
    }

    public S_Message_YN(int type, String msg) {
        writeC(155);
        writeH(0);
        writeD(0);
        writeH(type);
        writeS(msg);
    }

    public S_Message_YN(int type, String msg1, String msg2) {
        writeC(155);
        writeH(0);
        writeD(0);
        writeH(type);
        writeS(msg1);
        writeS(msg2);
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
