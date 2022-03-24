package com.lineage.server.serverpackets;
import com.lineage.server.serverpackets.OpcodesServer.*;

public class S_DoActionGFX extends ServerBasePacket {
    public static int ACTION_MAGIC = 22;
    private byte[] _byte = null;

    public S_DoActionGFX(int objectId, int actionId) {
        writeC(OpcodesServer.S_OPCODE_DOACTIONGFX);
        writeD(objectId);
        writeC(actionId);
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
