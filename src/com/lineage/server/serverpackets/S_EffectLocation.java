package com.lineage.server.serverpackets;

import com.lineage.server.model.L1Location;
import com.lineage.server.types.Point;

public class S_EffectLocation extends ServerBasePacket {
    private byte[] _byte;

    public S_EffectLocation(Point pt, int gfxId) {
        this(pt.getX(), pt.getY(), gfxId);
    }

    public S_EffectLocation(L1Location loc, int gfxId) {
        this(loc.getX(), loc.getY(), gfxId);
    }

    public S_EffectLocation(int x, int y, int gfxId) {
        this._byte = null;
        writeC(112);
        writeH(x);
        writeH(y);
        writeH(gfxId);
    }

    public S_EffectLocation(int opid, L1Location loc) {
        this._byte = null;
        writeC(opid);
        writeH(loc.getX());
        writeH(loc.getY());
        writeH(4842);
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
