package com.lineage.server.serverpackets;

import com.lineage.server.clientpackets.C_CreateChar;
import com.lineage.server.model.Instance.L1PcInstance;

public class S_CharResetInfo extends ServerBasePacket {
    private byte[] _byte = null;

    public S_CharResetInfo(L1PcInstance pc) {
        int baseStr = C_CreateChar.ORIGINAL_STR[pc.getType()];
        int baseDex = C_CreateChar.ORIGINAL_DEX[pc.getType()];
        int baseCon = C_CreateChar.ORIGINAL_CON[pc.getType()];
        int baseWis = C_CreateChar.ORIGINAL_WIS[pc.getType()];
        int baseCha = C_CreateChar.ORIGINAL_CHA[pc.getType()];
        int baseInt = C_CreateChar.ORIGINAL_INT[pc.getType()];
        int originalStr = pc.getOriginalStr();
        int originalDex = pc.getOriginalDex();
        int originalCon = pc.getOriginalCon();
        int originalWis = pc.getOriginalWis();
        int originalCha = pc.getOriginalCha();
        writeC(33);
        writeC(4);
        writeC(((pc.getOriginalInt() - baseInt) << 4) + (originalStr - baseStr));
        writeC(((originalDex - baseDex) << 4) + (originalWis - baseWis));
        writeC(((originalCha - baseCha) << 4) + (originalCon - baseCon));
        writeC(0);
        writeH(0);
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
