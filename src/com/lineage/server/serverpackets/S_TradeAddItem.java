package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1ItemInstance;

public class S_TradeAddItem extends ServerBasePacket {
    public S_TradeAddItem(L1ItemInstance item, long count, int type) {
        writeC(86);
        writeC(type);
        writeH(item.getItem().getGfxId());
        writeS(item.getNumberedViewName(count));
        writeC(item.getBless());
    }

    public S_TradeAddItem() {
        writeC(86);
        writeC(1);
        writeH(714);
        writeS("測試物品(55)");
        writeC(0);
    }

    @Override // com.lineage.server.serverpackets.ServerBasePacket
    public byte[] getContent() {
        return getBytes();
    }

    @Override // com.lineage.server.serverpackets.ServerBasePacket
    public String getType() {
        return getClass().getSimpleName();
    }
}
