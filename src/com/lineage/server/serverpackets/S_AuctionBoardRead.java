package com.lineage.server.serverpackets;

import com.lineage.server.datatables.lock.AuctionBoardReading;
import com.lineage.server.templates.L1AuctionBoardTmp;
import java.util.Calendar;

public class S_AuctionBoardRead extends ServerBasePacket {
    private byte[] _byte = null;

    public S_AuctionBoardRead(int objectId, String house_number) {
        buildPacket(objectId, house_number);
    }

    private void buildPacket(int objectId, String house_number) {
        L1AuctionBoardTmp board = AuctionBoardReading.get().getAuctionBoardTable(Integer.valueOf(house_number).intValue());
        writeC(OpcodesServer.S_OPCODE_SHOWHTML);
        writeD(objectId);
        writeS("agsel");
        writeS(house_number);
        writeH(9);
        writeS(board.getHouseName());
        writeS(String.valueOf(board.getLocation()) + "$1195");
        writeS(String.valueOf(board.getHouseArea()));
        writeS(board.getOldOwner());
        writeS(board.getBidder());
        writeS(String.valueOf(board.getPrice()));
        Calendar cal = board.getDeadline();
        int day = cal.get(5);
        int hour = cal.get(11);
        writeS(String.valueOf(cal.get(2) + 1));
        writeS(String.valueOf(day));
        writeS(String.valueOf(hour));
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
