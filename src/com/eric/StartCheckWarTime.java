package com.eric;

import com.lineage.DatabaseFactory;
import com.lineage.server.utils.SQLUtil;
import com.lineage.server.utils.collections.Maps;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class StartCheckWarTime {
    private static StartCheckWarTime _instance;
    private static Logger _log = Logger.getLogger(StartCheckWarTime.class.getName());
    private final Map<Integer, Data> _check = Maps.newHashMap();

    /* access modifiers changed from: private */
    public class Data {
        public boolean _isActive;

        private Data() {
            this._isActive = true;
        }

        /* synthetic */ Data(StartCheckWarTime startCheckWarTime, Data data) {
            this();
        }
    }

    private StartCheckWarTime() {
        loadStartCheckWarTimeFromDatabase();
    }

    private void loadStartCheckWarTimeFromDatabase() {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("SELECT * FROM eric_startcheckwartime");
            rs = pstm.executeQuery();
            while (rs.next()) {
                Data data = new Data(this, null);
                int id = rs.getInt("castle_id");
                rs.getString("name");
                data._isActive = rs.getBoolean("isActive");
                this._check.put(new Integer(id), data);
            }
            _log.config("StartCheckWarTime " + this._check.size());
        } catch (SQLException e) {
            e.printStackTrace();
            _log.log(Level.SEVERE, e.getLocalizedMessage(), (Throwable) e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    public static StartCheckWarTime getInstance() {
        if (_instance == null) {
            _instance = new StartCheckWarTime();
        }
        return _instance;
    }

    public boolean isActive(int castleId) {
        if (this._check.get(Integer.valueOf(castleId)) == null) {
            return true;
        }
        return this._check.get(Integer.valueOf(castleId))._isActive;
    }
}
