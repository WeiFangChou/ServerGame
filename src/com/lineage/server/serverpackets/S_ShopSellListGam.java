package com.lineage.server.serverpackets;

import com.lineage.data.event.GamblingSet;
import com.lineage.data.event.gambling.GamblingNpc;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.templates.L1Item;
import com.lineage.server.timecontroller.event.GamblingTime;
import java.util.Map;

public class S_ShopSellListGam extends ServerBasePacket {
    private byte[] _byte = null;

    public S_ShopSellListGam(L1PcInstance pc, L1NpcInstance npc) {
        writeC(OpcodesServer.S_OPCODE_SHOWSHOPBUYLIST);
        writeD(npc.getId());
        Map<Integer, GamblingNpc> list = GamblingTime.get_gambling().get_allNpc();
        if (list.size() <= 0) {
            writeH(0);
            return;
        }
        writeH(list.size());
        L1Item item = ItemTable.get().getTemplate(40309);
        int i = 0;
        for (GamblingNpc gamblingNpc : list.values()) {
            i++;
            pc.get_otherList().add_gamList(gamblingNpc, i);
            writeD(i);
            writeH(item.getGfxId());
            writeD(GamblingSet.GAMADENA);
            int no = GamblingTime.get_gamblingNo();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(gamblingNpc.get_npc().getNameId());
            stringBuilder.append(" [" + no + "-" + gamblingNpc.get_npc().getNpcId() + "]");
            writeS(stringBuilder.toString());
            writeC(0);
        }
        writeH(7);
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
