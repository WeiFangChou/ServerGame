package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1FollowerInstance;
import com.lineage.server.model.Instance.L1PcInstance;

public class S_FollowerPack extends ServerBasePacket {
    private static final int STATUS_POISON = 1;
    private byte[] _byte = null;

    public S_FollowerPack(L1FollowerInstance follower, L1PcInstance pc) {
        writeC(3);
        writeH(follower.getX());
        writeH(follower.getY());
        writeD(follower.getId());
        writeH(follower.getGfxId());
        writeC(follower.getStatus());
        writeC(follower.getHeading());
        writeC(follower.getChaLightSize());
        writeC(follower.getMoveSpeed());
        writeD(0);
        writeH(0);
        writeS(follower.getNameId());
        writeS(follower.getTitle());
        int status = 0;
        if (follower.getPoison() != null && follower.getPoison().getEffectId() == 1) {
            status = 1;
        }
        writeC(status);
        writeD(0);
        writeS(null);
        writeS(null);
        writeC(0);
        writeC(255);
        writeC(0);
        writeC(0);
        writeC(0);
        writeC(255);
        writeC(255);
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
