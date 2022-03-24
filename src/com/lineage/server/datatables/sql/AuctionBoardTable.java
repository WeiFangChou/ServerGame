package com.lineage.server.datatables.sql;

import com.lineage.DatabaseFactory;
import com.lineage.config.Config;
import com.lineage.server.datatables.storage.AuctionBoardStorage;
import com.lineage.server.templates.L1AuctionBoardTmp;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AuctionBoardTable implements AuctionBoardStorage {
    private static final Map<Integer, L1AuctionBoardTmp> _boards = new HashMap();
    private static final Log _log = LogFactory.getLog(AuctionBoardTable.class);

    private Calendar timestampToCalendar(Timestamp ts) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(ts.getTime());
        return cal;
    }

    @Override // com.lineage.server.datatables.storage.AuctionBoardStorage
    public void load() {
        PerformanceTimer timer = new PerformanceTimer();
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("SELECT * FROM `server_board_auction`");
            rs = pstm.executeQuery();
            while (rs.next()) {
                L1AuctionBoardTmp board = new L1AuctionBoardTmp();
                int houseId = rs.getInt("house_id");
                String house_name = rs.getString("house_name");
                int house_area = rs.getInt("house_area");
                Calendar deadline = timestampToCalendar((Timestamp) rs.getObject("deadline"));
                String location = rs.getString("location");
                long price = rs.getLong("price");
                String old_owner = rs.getString("old_owner");
                int old_owner_id = rs.getInt("old_owner_id");
                String bidder = rs.getString("bidder");
                int bidder_id = rs.getInt("bidder_id");
                if (!location.startsWith("$")) {
                    String townName = "";
                    if (houseId >= 262145 && houseId <= 262189) {
                        townName = "$1242 ";
                    }
                    if (houseId >= 327681 && houseId <= 327691) {
                        townName = "$1513 ";
                    }
                    if (houseId >= 458753 && houseId <= 458819) {
                        townName = "$2129 ";
                    }
                    if (houseId >= 524289 && houseId <= 524294) {
                        townName = "$381 ";
                    }
                    location = String.valueOf(townName) + " " + location;
                }
                if (house_name.equals("null")) {
                    house_name = String.valueOf(location) + "$1195";
                }
                board.setHouseId(houseId);
                board.setHouseName(house_name);
                board.setHouseArea(house_area);
                board.setDeadline(deadline);
                board.setPrice(price);
                board.setLocation(location);
                board.setOldOwner(old_owner);
                board.setOldOwnerId(old_owner_id);
                board.setBidder(bidder);
                board.setBidderId(bidder_id);
                _boards.put(Integer.valueOf(board.getHouseId()), board);
            }
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
        _log.info("載入盟屋拍賣公告欄資料數量: " + _boards.size() + "(" + timer.get() + "ms)");
    }

    @Override // com.lineage.server.datatables.storage.AuctionBoardStorage
    public Map<Integer, L1AuctionBoardTmp> getAuctionBoardTableList() {
        return _boards;
    }

    @Override // com.lineage.server.datatables.storage.AuctionBoardStorage
    public L1AuctionBoardTmp getAuctionBoardTable(int houseId) {
        return _boards.get(Integer.valueOf(houseId));
    }

    @Override // com.lineage.server.datatables.storage.AuctionBoardStorage
    public void insertAuctionBoard(L1AuctionBoardTmp board) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("INSERT INTO `server_board_auction` SET `house_id`=?,`house_name`=?,`house_area`=?,`deadline`=?,`price`=?,`location`=?,`old_owner`=?,`old_owner_id`=?,`bidder`=?,`bidder_id`=?");
            pstm.setInt(1, board.getHouseId());
            pstm.setString(2, board.getHouseName());
            pstm.setInt(3, board.getHouseArea());
            pstm.setString(4, new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance(TimeZone.getTimeZone(Config.TIME_ZONE)).getTime()));
            pstm.setLong(5, board.getPrice());
            pstm.setString(6, board.getLocation());
            pstm.setString(7, board.getOldOwner());
            pstm.setInt(8, board.getOldOwnerId());
            pstm.setString(9, board.getBidder());
            pstm.setInt(10, board.getBidderId());
            pstm.execute();
            _boards.put(Integer.valueOf(board.getHouseId()), board);
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    @Override // com.lineage.server.datatables.storage.AuctionBoardStorage
    public void updateAuctionBoard(L1AuctionBoardTmp board) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("UPDATE `server_board_auction` SET `house_name`=?,`house_area`=?,`deadline`=?,`price`=?,`location`=?,`old_owner`=?,`old_owner_id`=?,`bidder`=?,`bidder_id`=? WHERE `house_id`=?");
            pstm.setString(1, board.getHouseName());
            pstm.setInt(2, board.getHouseArea());
            pstm.setString(3, new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(board.getDeadline().getTime()));
            pstm.setLong(4, board.getPrice());
            pstm.setString(5, board.getLocation());
            pstm.setString(6, board.getOldOwner());
            pstm.setInt(7, board.getOldOwnerId());
            pstm.setString(8, board.getBidder());
            pstm.setInt(9, board.getBidderId());
            pstm.setInt(10, board.getHouseId());
            pstm.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    @Override // com.lineage.server.datatables.storage.AuctionBoardStorage
    public void deleteAuctionBoard(int houseId) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("DELETE FROM `server_board_auction` WHERE `house_id`=?");
            pstm.setInt(1, houseId);
            pstm.execute();
            _boards.remove(Integer.valueOf(houseId));
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }
}
