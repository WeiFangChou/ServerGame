package com.lineage.server.datatables;

import com.lineage.DatabaseFactory;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.utils.SQLUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InnKeyTable {
    private static Logger _log = Logger.getLogger(InnKeyTable.class.getName());

    public static void StoreKey(L1ItemInstance item) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("INSERT INTO inn_key SET item_obj_id=?, key_id=?, npc_id=?, hall=?, due_time=?");
            pstm.setInt(1, item.getId());
            pstm.setInt(2, item.getKeyId());
            pstm.setInt(3, item.getInnNpcId());
            pstm.setBoolean(4, item.checkRoomOrHall());
            pstm.setTimestamp(5, item.getDueTime());
            pstm.execute();
        } catch (SQLException e) {
            _log.log(Level.SEVERE, e.getLocalizedMessage(), (Throwable) e);
        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    public static void DeleteKey(L1ItemInstance item) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("DELETE FROM inn_key WHERE item_obj_id=?");
            pstm.setInt(1, item.getId());
            pstm.execute();
        } catch (SQLException e) {
            _log.log(Level.SEVERE, e.getLocalizedMessage(), (Throwable) e);
        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    public static boolean checkey(L1ItemInstance item) {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("SELECT * FROM inn_key WHERE item_obj_id=?");
            pstm.setInt(1, item.getId());
            rs = pstm.executeQuery();
            while (rs.next()) {
                if (item.getId() == rs.getInt("item_obj_id")) {
                    item.setKeyId(rs.getInt("key_id"));
                    item.setInnNpcId(rs.getInt("npc_id"));
                    item.setHall(rs.getBoolean("hall"));
                    item.setDueTime(rs.getTimestamp("due_time"));
                    return true;
                }
            }
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        } catch (SQLException e) {
            _log.log(Level.SEVERE, e.getLocalizedMessage(), (Throwable) e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
        return false;
    }
}
