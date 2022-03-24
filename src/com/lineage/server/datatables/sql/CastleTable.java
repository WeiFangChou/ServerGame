package com.lineage.server.datatables.sql;

import com.lineage.DatabaseFactory;
import com.lineage.server.datatables.storage.CastleStorage;
import com.lineage.server.templates.L1Castle;
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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CastleTable implements CastleStorage {
    private static final Map<Integer, L1Castle> _castles = new HashMap();
    private static final Log _log = LogFactory.getLog(CastleTable.class);

    private Calendar timestampToCalendar(Timestamp ts) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(ts.getTime());
        return cal;
    }

    @Override // com.lineage.server.datatables.storage.CastleStorage
    public void load() {
        PerformanceTimer timer = new PerformanceTimer();
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("SELECT * FROM `server_castle`");
            rs = pstm.executeQuery();
            while (rs.next()) {
                int castle_id = rs.getInt("castle_id");
                String name = rs.getString("name");
                Calendar war_time = timestampToCalendar(rs.getTimestamp("war_time"));
                int tax_rate = rs.getInt("tax_rate");
                long public_money = rs.getLong("public_money");
                L1Castle castle = new L1Castle(castle_id, name);
                castle.setWarTime(war_time);
                castle.setTaxRate(tax_rate);
                castle.setPublicMoney(public_money);
                _castles.put(Integer.valueOf(castle_id), castle);
            }
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
        _log.info("載入城堡資料數量: " + _castles.size() + "(" + timer.get() + "ms)");
    }

    @Override // com.lineage.server.datatables.storage.CastleStorage
    public Map<Integer, L1Castle> getCastleMap() {
        return _castles;
    }

    @Override // com.lineage.server.datatables.storage.CastleStorage
    public L1Castle[] getCastleTableList() {
        return (L1Castle[]) _castles.values().toArray(new L1Castle[_castles.size()]);
    }

    @Override // com.lineage.server.datatables.storage.CastleStorage
    public L1Castle getCastleTable(int id) {
        return _castles.get(Integer.valueOf(id));
    }

    @Override // com.lineage.server.datatables.storage.CastleStorage
    public void updateCastle(L1Castle castle) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("UPDATE `server_castle` SET `name`=?,`war_time`=?,`tax_rate`=?,`public_money`=? WHERE `castle_id`=?");
            int i = 0 + 1;
            pstm.setString(i, castle.getName());
            int i2 = i + 1;
            pstm.setString(i2, new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(castle.getWarTime().getTime()));
            int i3 = i2 + 1;
            pstm.setInt(i3, castle.getTaxRate());
            int i4 = i3 + 1;
            pstm.setLong(i4, castle.getPublicMoney());
            pstm.setInt(i4 + 1, castle.getId());
            pstm.execute();
            _castles.put(Integer.valueOf(castle.getId()), castle);
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }
}
