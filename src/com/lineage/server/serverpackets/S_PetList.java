package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import java.util.ArrayList;
import java.util.List;

public class S_PetList extends ServerBasePacket {
    private byte[] _byte = null;

    public S_PetList(int npcObjId, L1PcInstance pc) {
        buildPacket(npcObjId, pc);
    }

    private void buildPacket(int npcObjId, L1PcInstance pc) {
        List<L1ItemInstance> amuletList = new ArrayList<>();
        for (L1ItemInstance item : pc.getInventory().getItems()) {
            switch (item.getItem().getItemId()) {
                case 40314:
                case 40316:
                    if (!isWithdraw(pc, item)) {
                        amuletList.add(item);
                        break;
                    } else {
                        break;
                    }
            }
        }
        if (amuletList.size() != 0) {
            writeC(208);
            writeD(70);
            writeH(amuletList.size());
            for (L1ItemInstance item2 : amuletList) {
                writeD(item2.getId());
                writeC((int) Math.min(item2.getCount(), 2000000000L));
            }
        }
    }

    private boolean isWithdraw(L1PcInstance pc, L1ItemInstance item) {
        Object[] petlist = pc.getPetList().values().toArray();
        for (Object petObject : petlist) {
            if ((petObject instanceof L1PetInstance) && item.getId() == ((L1PetInstance) petObject).getItemObjId()) {
                return true;
            }
        }
        return false;
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
