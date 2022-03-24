package william;

import com.lineage.DatabaseFactory;
import com.lineage.server.utils.SQLUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SystemMessage {
    private static SystemMessage _instance;
    private static Log _log = LogFactory.getLog(SystemMessage.class);
    private final HashMap<Integer, L1WilliamSystemMessage> _itemIdIndex = new HashMap<>();

    public static SystemMessage getInstance() {
        if (_instance == null) {
            _instance = new SystemMessage();
        }
        return _instance;
    }

    private SystemMessage() {
        loadSystemMessage();
    }

    private void loadSystemMessage() {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("SELECT * FROM 顯示訊息系統");
            rs = pstm.executeQuery();
            fillSystemMessage(rs);
        } catch (SQLException e) {
            _log.error("error while creating william_system_message table", e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    private void fillSystemMessage(ResultSet rs) throws SQLException {
        while (rs.next()) {
            int Id = rs.getInt("id");
            this._itemIdIndex.put(Integer.valueOf(Id), new L1WilliamSystemMessage(Id, rs.getString("顯示訊息")));
        }
    }

    public L1WilliamSystemMessage getTemplate(int Id) {
        return this._itemIdIndex.get(Integer.valueOf(Id));
    }
}
