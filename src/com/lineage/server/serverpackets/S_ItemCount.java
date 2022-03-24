package com.lineage.server.serverpackets;

public class S_ItemCount extends ServerBasePacket {
    private byte[] _byte = null;

    public S_ItemCount(int objId, int max, String cmd) {
        writeC(253);
        writeD(objId);
        writeD(0);
        writeD(0);
        writeD(0);
        writeD(max);
        writeH(0);
        writeS("request");
        writeS(cmd);
    }

    public S_ItemCount(int objId, int max, String html, String cmd) {
        writeC(253);
        writeD(objId);
        writeD(0);
        writeD(0);
        writeD(0);
        writeD(max);
        writeH(0);
        writeS(html);
        writeS(cmd);
    }

    public S_ItemCount(int objId, int min, int max, String html, String cmd, String[] data) {
        writeC(253);
        writeD(objId);
        writeD(0);
        writeD(min);
        writeD(min);
        writeD(max);
        writeH(0);
        writeS(html);
        writeS(cmd);
        if (data != null && 1 <= data.length) {
            writeH(data.length);
            for (String datum : data) {
                writeS(datum);
            }
        }
    }

    public S_ItemCount(int objId, int min, int max, String html, String cmd) {
        writeC(253);
        writeD(objId);
        writeD(0);
        writeD(min);
        writeD(min);
        writeD(max);
        writeH(0);
        writeS(html);
        writeS(cmd);
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
