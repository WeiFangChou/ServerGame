package com.lineage.server.serverpackets;

import com.lineage.server.datatables.lock.AuctionBoardReading;
import com.lineage.server.templates.L1AuctionBoardTmp;

public class S_ApplyAuction extends ServerBasePacket {
    private byte[] _byte = null;

    public S_ApplyAuction(int objectId, String houseNumber) {
        buildPacket(objectId, houseNumber);
    }

    private void buildPacket(int objectId, String houseNumber) {
        L1AuctionBoardTmp board = AuctionBoardReading.get().getAuctionBoardTable(Integer.valueOf(houseNumber).intValue());
        writeC(253);
        writeD(objectId);
        writeD(0);
        if (board.getBidderId() == 0) {
            writeD((int) board.getPrice());
            writeD((int) board.getPrice());
        } else {
            writeD(((int) board.getPrice()) + 1);
            writeD(((int) board.getPrice()) + 1);
        }
        writeD(2000000000);
        writeH(0);
        writeS("agapply");
        writeS("agapply " + houseNumber);
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
