package com.lineage.server.serverpackets;

import com.lineage.server.datatables.lock.AuctionBoardReading;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.templates.L1AuctionBoardTmp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

public class S_AuctionBoard extends ServerBasePacket {
    private byte[] _byte = null;

    public S_AuctionBoard(L1NpcInstance board) {
        buildPacket(board);
    }

    private void buildPacket(L1NpcInstance board) {
        ArrayList<L1AuctionBoardTmp> houseListX = new ArrayList<>();
        for (L1AuctionBoardTmp boardX : AuctionBoardReading.get().getAuctionBoardTableList().values()) {
            int houseId = boardX.getHouseId();
            if (board.getX() == 33421 && board.getY() == 32823) {
                if (houseId >= 262145 && houseId <= 262189) {
                    houseListX.add(boardX);
                }
            } else if (board.getX() == 33585 && board.getY() == 33235) {
                if (houseId >= 327681 && houseId <= 327691) {
                    houseListX.add(boardX);
                }
            } else if (board.getX() == 33959 && board.getY() == 33253) {
                if (houseId >= 458753 && houseId <= 458819) {
                    houseListX.add(boardX);
                }
            } else if (board.getX() == 32611 && board.getY() == 32775 && houseId >= 524289 && houseId <= 524294) {
                houseListX.add(boardX);
            }
        }
        writeC(24);
        writeD(board.getId());
        writeH(houseListX.size());
        Iterator<L1AuctionBoardTmp> it = houseListX.iterator();
        while (it.hasNext()) {
            L1AuctionBoardTmp boardX2 = it.next();
            writeD(boardX2.getHouseId());
            writeS(boardX2.getHouseName());
            writeH(boardX2.getHouseArea());
            Calendar cal = boardX2.getDeadline();
            writeC(cal.get(2) + 1);
            writeC(cal.get(5));
            writeD((int) boardX2.getPrice());
        }
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
