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

public class ItemBlessarmor {
    private static ItemBlessarmor _instance;
    private static Logger _log = Logger.getLogger(ItemBlessarmor.class.getName());
    private final HashMap<Integer, L1William_Bless_armor> _itemIdIndex = new HashMap<>();

    public static ItemBlessarmor getInstance() {
        if (_instance == null) {
            _instance = new ItemBlessarmor();
        }
        return _instance;
    }

    private ItemBlessarmor() {
        load_Bless_armor_Item();
    }

    private void load_Bless_armor_Item() {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("SELECT * FROM 裝備升祝福系統");
            rs = pstm.executeQuery();
            fillBlessarmorItem(rs);
        } catch (SQLException e) {
            _log.log(Level.SEVERE, "error while creating 裝備升祝福系統 table", (Throwable) e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    private void fillBlessarmorItem(ResultSet rs) throws SQLException {
        while (rs.next()) {
            int itemId = rs.getInt("裝備編號");
            this._itemIdIndex.put(Integer.valueOf(itemId), new L1William_Bless_armor(itemId, rs.getInt("祝福裝備編號"), rs.getInt("公告")));
        }
    }

    public L1William_Bless_armor getTemplate(int itemId) {
        return this._itemIdIndex.get(Integer.valueOf(itemId));
    }
}
