package com.lineage.server.timecontroller.server;

import com.lineage.config.Config;
import com.lineage.config.ConfigAlt;
import com.lineage.server.datatables.lock.AuctionBoardReading;
import com.lineage.server.datatables.lock.ClanReading;
import com.lineage.server.datatables.lock.HouseReading;
import com.lineage.server.model.L1Clan;
import com.lineage.server.templates.L1AuctionBoardTmp;
import com.lineage.server.templates.L1House;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.world.WorldClan;
import java.util.Calendar;
import java.util.Collection;
import java.util.TimeZone;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ServerHouseTaxTimer extends TimerTask {
    private static final Log _log = LogFactory.getLog(ServerHouseTaxTimer.class);
    private ScheduledFuture<?> _timer;

    public void start() {
        this._timer = GeneralThreadPool.get().scheduleAtFixedRate((TimerTask) this, 600000L, 600000L);
    }

    public void run() {
        try {
            checkTaxDeadline();
        } catch (Exception e) {
            _log.error("血盟小屋稅收時間軸異常重啟", e);
            GeneralThreadPool.get().cancel(this._timer, false);
            new ServerHouseTaxTimer().start();
        }
    }

    private static Calendar getRealTime() {
        return Calendar.getInstance(TimeZone.getTimeZone(Config.TIME_ZONE));
    }

    private static void checkTaxDeadline() {
        try {
            Collection<L1House> houseList = HouseReading.get().getHouseTableList().values();
            if (!houseList.isEmpty()) {
                for (L1House house : houseList) {
                    if (!house.isOnSale() && house.getTaxDeadline().before(getRealTime())) {
                        sellHouse(house);
                    }
                }
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private static void sellHouse(L1House house) {
        L1AuctionBoardTmp board = new L1AuctionBoardTmp();
        if (board != null) {
            int houseId = house.getHouseId();
            board.setHouseId(houseId);
            board.setHouseName(house.getHouseName());
            board.setHouseArea(house.getHouseArea());
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(Config.TIME_ZONE));
            cal.add(5, 5);
            cal.set(12, 0);
            cal.set(13, 0);
            board.setDeadline(cal);
            board.setPrice(100000);
            board.setLocation(house.getLocation());
            board.setOldOwner("");
            board.setOldOwnerId(0);
            board.setBidder("");
            board.setBidderId(0);
            AuctionBoardReading.get().insertAuctionBoard(board);
            house.setOnSale(true);
            house.setPurchaseBasement(true);
            cal.add(5, ConfigAlt.HOUSE_TAX_INTERVAL);
            house.setTaxDeadline(cal);
            HouseReading.get().updateHouse(house);
            for (L1Clan clan : WorldClan.get().getAllClans()) {
                if (clan.getHouseId() == houseId) {
                    clan.setHouseId(0);
                    ClanReading.get().updateClan(clan);
                }
            }
        }
    }
}
