package william;

import com.lineage.DatabaseFactory;
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

public class L1SystemMessageTable {
    private static L1SystemMessageTable _instance;
    private static final Log _log = LogFactory.getLog(L1SystemMessageTable.class);
    private final Map<Integer, L1SystemMessage> _ConfigIndex = new HashMap();

    public static L1SystemMessageTable get() {
        if (_instance == null) {
            _instance = new L1SystemMessageTable();
        }
        return _instance;
    }

    public void loadSystemMessage() {
        PerformanceTimer timer = new PerformanceTimer();
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("SELECT * FROM system_message");
            rs = pstm.executeQuery();
            fillSystemMessage(rs);
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
        _log.info("載入DB化系統設定檔資料數量: " + this._ConfigIndex.size() + "(" + timer.get() + "ms)");
    }

    private void fillSystemMessage(ResultSet rs) throws SQLException {
        while (rs.next()) {
            int Id = rs.getInt("id");
            String Message = rs.getString("message");
            Calendar cal = null;
            if (rs.getTimestamp("resettime") != null) {
                cal = timestampToCalendar(rs.getTimestamp("resettime"));
            }
            L1SystemMessage System_Message = new L1SystemMessage();
            System_Message.set_id(Id);
            System_Message.set_message(Message);
            System_Message.set_resettime(cal);
            this._ConfigIndex.put(Integer.valueOf(Id), System_Message);
        }
    }

    public L1SystemMessage getTemplate(int Id) {
        return this._ConfigIndex.get(Integer.valueOf(Id));
    }

    private Calendar timestampToCalendar(Timestamp ts) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(ts.getTime());
        return cal;
    }

    public void updateResetTime(int id, Calendar reset_cal) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("UPDATE `system_message` SET `resettime`=? WHERE `id`=?");
            int i = 0 + 1;
            pstm.setString(i, new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(reset_cal.getTime()));
            pstm.setInt(i + 1, id);
            pstm.execute();
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }
}
