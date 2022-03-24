package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1SummonInstance;

public class S_NPCPack_Summon extends ServerBasePacket {
    private static final int STATUS_POISON = 1;
    private byte[] _byte = null;

    public S_NPCPack_Summon(L1SummonInstance pet, L1PcInstance pc) {
        buildPacket(pet, pc, true);
    }

    public S_NPCPack_Summon(L1SummonInstance pet, L1PcInstance pc, boolean isCheckMaster) {
        buildPacket(pet, pc, isCheckMaster);
    }

    private void buildPacket(L1SummonInstance pet, L1PcInstance pc, boolean isCheckMaster) {
        L1PcInstance master;
        writeC(3);
        writeH(pet.getX());
        writeH(pet.getY());
        writeD(pet.getId());
        writeH(pet.getGfxId());
        writeC(pet.getStatus());
        writeC(pet.getHeading());
        writeC(pet.getChaLightSize());
        writeC(pet.getMoveSpeed());
        writeD(0);
        writeH(0);
        writeS(pet.getNameId());
        writeS(pet.getTitle());
        int status = 0;
        if (pet.getPoison() != null && pet.getPoison().getEffectId() == 1) {
            status = 1;
        }
        writeC(status);
        writeD(0);
        writeS(null);
        StringBuilder stringBuilder = new StringBuilder();
        if (!isCheckMaster || !pet.isExsistMaster()) {
            stringBuilder.append("");
        } else if ((pet.getMaster() instanceof L1PcInstance) && (master = (L1PcInstance) pet.getMaster()) != null) {
            if (master.get_other().get_color() != 0) {
                stringBuilder.append(master.get_other().color());
            }
            stringBuilder.append(master.getName());
        }
        if (pet.getMaster() instanceof L1NpcInstance) {
            stringBuilder.append(((L1NpcInstance) pet.getMaster()).getNameId());
        }
        writeS(stringBuilder.toString());
        writeC(0);
        if (pet.getMaster() instanceof L1NpcInstance) {
            writeC(255);
        } else if (pet.getMaster() == null || pet.getMaster().getId() != pc.getId()) {
            writeC(255);
        } else {
            writeC(pet.getMaxHp() != 0 ? (pet.getCurrentHp() * 100) / pet.getMaxHp() : 100);
        }
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
