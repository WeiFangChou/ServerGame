package com.lineage.server.datatables.storage;

import com.lineage.server.templates.L1AuctionBoardTmp;
import java.util.Map;

public interface AuctionBoardStorage {
    void deleteAuctionBoard(int i);

    L1AuctionBoardTmp getAuctionBoardTable(int i);

    Map<Integer, L1AuctionBoardTmp> getAuctionBoardTableList();

    void insertAuctionBoard(L1AuctionBoardTmp l1AuctionBoardTmp);

    void load();

    void updateAuctionBoard(L1AuctionBoardTmp l1AuctionBoardTmp);
}
