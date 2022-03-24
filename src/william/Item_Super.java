package william;

import com.lineage.DatabaseFactory;
import com.lineage.server.utils.SQLUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Item_Super {
    private static Item_Super _instance;
    private static Logger _log = Logger.getLogger(Item_Super.class.getName());
    private final HashMap<Integer, L1William_Super> _itemIdIndex = new HashMap<>();

    public static Item_Super getInstance() {
        if (_instance == null) {
            _instance = new Item_Super();
        }
        return _instance;
    }

    private Item_Super() {
        load_Item_Super();
    }

    private void load_Item_Super() {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("SELECT * FROM 卡片覺醒系統");
            rs = pstm.executeQuery();
            fillItem_Super(rs);
        } catch (SQLException e) {
            _log.log(Level.SEVERE, "error while creating 卡片覺醒系統 table", (Throwable) e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    private void fillItem_Super(ResultSet rs) throws SQLException {
        while (rs.next()) {
            int itemId = rs.getInt("卡片道具編號");
            this._itemIdIndex.put(Integer.valueOf(itemId), new L1William_Super(itemId, rs.getInt("覺醒道具編號"), rs.getInt("失敗退回道具編號"), rs.getInt("失敗退回數量"), rs.getInt("公告")));
        }
    }

    public L1William_Super getTemplate(int itemId) {
        return this._itemIdIndex.get(Integer.valueOf(itemId));
    }
}
