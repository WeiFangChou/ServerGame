package com.lineage.server.timecontroller.server;

import com.lineage.config.Config;
import com.lineage.config.ConfigAlt;
import com.lineage.server.datatables.lock.AuctionBoardReading;
import com.lineage.server.datatables.lock.CharItemsReading;
import com.lineage.server.datatables.lock.ClanReading;
import com.lineage.server.datatables.lock.HouseReading;
import com.lineage.server.datatables.sql.AuctionBoardTable;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Clan;
import com.lineage.server.model.item.L1ItemId;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1AuctionBoardTmp;
import com.lineage.server.templates.L1House;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.utils.ListMapUtil;
import com.lineage.server.world.World;
import com.lineage.server.world.WorldClan;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Queue;
import java.util.TimeZone;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ScheduledFuture;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ServerAuctionTimer extends TimerTask {
    private static final Log _log = LogFactory.getLog(ServerAuctionTimer.class);
    private static final Queue<Integer> _removeList = new ConcurrentLinkedQueue();
    private ScheduledFuture<?> _timer;

    public void start() {
        this._timer = GeneralThreadPool.get().scheduleAtFixedRate((TimerTask) this, 300000L, 300000L);
    }

    public void run() {
        try {
            checkAuctionDeadline();
        } catch (Exception e) {
            _log.error("小屋拍賣公告欄時間軸異常重啟", e);
            GeneralThreadPool.get().cancel(this._timer, false);
            new ServerAuctionTimer().start();
        } finally {
            ListMapUtil.clear(_removeList);
        }
    }

    private static Calendar getRealTime() {
        return Calendar.getInstance(TimeZone.getTimeZone(Config.TIME_ZONE));
    }

    private static void checkAuctionDeadline() {
        try {
            for (L1AuctionBoardTmp board : AuctionBoardReading.get().getAuctionBoardTableList().values()) {
                if (board.getDeadline().before(getRealTime())) {
                    endAuction(board);
                }
            }
            if (!_removeList.isEmpty()) {
                remove();
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private static void endAuction(L1AuctionBoardTmp board) {
        try {
            int houseId = board.getHouseId();
            long price = board.getPrice();
            int oldOwnerId = board.getOldOwnerId();
            String bidder = board.getBidder();
            int bidderId = board.getBidderId();
            if (oldOwnerId != 0 && bidderId != 0) {
                L1PcInstance oldOwnerPc = (L1PcInstance) World.get().findObject(oldOwnerId);
                long payPrice = (long) ((int) (((double) price) * 0.9d));
                if (oldOwnerPc != null) {
                    oldOwnerPc.getInventory().storeItem(L1ItemId.ADENA, payPrice);
                    oldOwnerPc.sendPackets(new S_ServerMessage(527, String.valueOf(payPrice)));
                } else {
                    try {
                        CharItemsReading.get().getAdenaCount(oldOwnerId, price);
                    } catch (Exception e) {
                        _log.error(e.getLocalizedMessage(), e);
                    }
                }
                L1PcInstance bidderPc = (L1PcInstance) World.get().findObject(bidderId);
                if (bidderPc != null) {
                    bidderPc.sendPackets(new S_ServerMessage(524, String.valueOf(price), bidder));
                }
                deleteHouseInfo(houseId);
                setHouseInfo(houseId, bidderId);
                _removeList.add(Integer.valueOf(houseId));
            } else if (oldOwnerId == 0 && bidderId != 0) {
                L1PcInstance bidderPc2 = (L1PcInstance) World.get().findObject(bidderId);
                if (bidderPc2 != null) {
                    bidderPc2.sendPackets(new S_ServerMessage(524, String.valueOf(price), bidder));
                }
                setHouseInfo(houseId, bidderId);
                _removeList.add(Integer.valueOf(houseId));
            } else if (oldOwnerId != 0 && bidderId == 0) {
                L1PcInstance oldOwnerPc2 = (L1PcInstance) World.get().findObject(oldOwnerId);
                if (oldOwnerPc2 != null) {
                    oldOwnerPc2.sendPackets(new S_ServerMessage(528));
                }
                _removeList.add(Integer.valueOf(houseId));
            } else if (oldOwnerId == 0 && bidderId == 0) {
                Calendar cal = getRealTime();
                cal.add(5, 5);
                cal.set(12, 0);
                cal.set(13, 0);
                board.setDeadline(cal);
                new AuctionBoardTable().updateAuctionBoard(board);
            }
        } catch (Exception e2) {
            _log.error(e2.getLocalizedMessage(), e2);
        }
    }

    private static void deleteHouseInfo(int houseId) {
        try {
            for (L1Clan clan : WorldClan.get().getAllClans()) {
                if (clan.getHouseId() == houseId) {
                    clan.setHouseId(0);
                    ClanReading.get().updateClan(clan);
                }
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private static void setHouseInfo(int houseId, int bidderId) {
        try {
            for (L1Clan clan : WorldClan.get().getAllClans()) {
                if (clan.getLeaderId() == bidderId) {
                    clan.setHouseId(houseId);
                    ClanReading.get().updateClan(clan);
                    return;
                }
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private static void remove() {
        try {
            Iterator<Integer> iter = _removeList.iterator();
            while (iter.hasNext()) {
                iter.remove();
                deleteNote(iter.next().intValue());
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private static void deleteNote(int houseId) {
        try {
            L1House house = HouseReading.get().getHouseTable(houseId);
            house.setOnSale(false);
            Calendar cal = getRealTime();
            cal.add(5, ConfigAlt.HOUSE_TAX_INTERVAL);
            cal.set(12, 0);
            cal.set(13, 0);
            house.setTaxDeadline(cal);
            HouseReading.get().updateHouse(house);
            AuctionBoardReading.get().deleteAuctionBoard(houseId);
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
