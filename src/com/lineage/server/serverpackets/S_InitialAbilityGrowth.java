package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1PcInstance;

public class S_InitialAbilityGrowth extends ServerBasePacket {
    private byte[] _byte = null;

    public S_InitialAbilityGrowth(L1PcInstance pc) {
        int Str = pc.getOriginalStr();
        int Dex = pc.getOriginalDex();
        int Con = pc.getOriginalCon();
        int Wis = pc.getOriginalWis();
        int Cha = pc.getOriginalCha();
        int Int = pc.getOriginalInt();
        int[] growth = new int[6];
        if (pc.isCrown()) {
            int[] Initial = {13, 10, 10, 11, 13, 10};
            growth[0] = Str - Initial[0];
            growth[1] = Dex - Initial[1];
            growth[2] = Con - Initial[2];
            growth[3] = Wis - Initial[3];
            growth[4] = Cha - Initial[4];
            growth[5] = Int - Initial[5];
        }
        if (pc.isWizard()) {
            int[] Initial2 = {8, 7, 12, 12, 8, 12};
            growth[0] = Str - Initial2[0];
            growth[1] = Dex - Initial2[1];
            growth[2] = Con - Initial2[2];
            growth[3] = Wis - Initial2[3];
            growth[4] = Cha - Initial2[4];
            growth[5] = Int - Initial2[5];
        }
        if (pc.isKnight()) {
            int[] Initial3 = {16, 12, 14, 9, 12, 8};
            growth[0] = Str - Initial3[0];
            growth[1] = Dex - Initial3[1];
            growth[2] = Con - Initial3[2];
            growth[3] = Wis - Initial3[3];
            growth[4] = Cha - Initial3[4];
            growth[5] = Int - Initial3[5];
        }
        if (pc.isElf()) {
            int[] Initial4 = {11, 12, 12, 12, 9, 12};
            growth[0] = Str - Initial4[0];
            growth[1] = Dex - Initial4[1];
            growth[2] = Con - Initial4[2];
            growth[3] = Wis - Initial4[3];
            growth[4] = Cha - Initial4[4];
            growth[5] = Int - Initial4[5];
        }
        if (pc.isDarkelf()) {
            int[] Initial5 = {12, 15, 8, 10, 9, 11};
            growth[0] = Str - Initial5[0];
            growth[1] = Dex - Initial5[1];
            growth[2] = Con - Initial5[2];
            growth[3] = Wis - Initial5[3];
            growth[4] = Cha - Initial5[4];
            growth[5] = Int - Initial5[5];
        }
        if (pc.isDragonKnight()) {
            int[] Initial6 = {13, 11, 14, 12, 8, 11};
            growth[0] = Str - Initial6[0];
            growth[1] = Dex - Initial6[1];
            growth[2] = Con - Initial6[2];
            growth[3] = Wis - Initial6[3];
            growth[4] = Cha - Initial6[4];
            growth[5] = Int - Initial6[5];
        }
        if (pc.isIllusionist()) {
            int[] Initial7 = {11, 10, 12, 12, 8, 12};
            growth[0] = Str - Initial7[0];
            growth[1] = Dex - Initial7[1];
            growth[2] = Con - Initial7[2];
            growth[3] = Wis - Initial7[3];
            growth[4] = Cha - Initial7[4];
            growth[5] = Int - Initial7[5];
        }
        buildPacket(pc, growth[0], growth[1], growth[2], growth[3], growth[4], growth[5]);
    }

    private void buildPacket(L1PcInstance pc, int Str, int Dex, int Con, int Wis, int Cha, int Int) {
        writeC(33);
        writeC(4);
        writeC((Int * 16) + Str);
        writeC((Dex * 16) + Wis);
        writeC((Cha * 16) + Con);
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
