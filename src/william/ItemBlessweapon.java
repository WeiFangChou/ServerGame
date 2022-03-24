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

public class ItemBlessweapon {
    private static ItemBlessweapon _instance;
    private static Logger _log = Logger.getLogger(ItemBlessweapon.class.getName());
    private final HashMap<Integer, L1William_Bless_weapon> _itemIdIndex = new HashMap<>();

    public static ItemBlessweapon getInstance() {
        if (_instance == null) {
            _instance = new ItemBlessweapon();
        }
        return _instance;
    }

    private ItemBlessweapon() {
        load_Bless_weapon_Item();
    }

    private void load_Bless_weapon_Item() {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("SELECT * FROM 武器升祝福系統");
            rs = pstm.executeQuery();
            fillBlessweaponItem(rs);
        } catch (SQLException e) {
            _log.log(Level.SEVERE, "error while creating 武器升祝福系統 table", (Throwable) e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    private void fillBlessweaponItem(ResultSet rs) throws SQLException {
        while (rs.next()) {
            int itemId = rs.getInt("武器編號");
            this._itemIdIndex.put(Integer.valueOf(itemId), new L1William_Bless_weapon(itemId, rs.getInt("祝福武器編號"), rs.getInt("公告")));
        }
    }

    public L1William_Bless_weapon getTemplate(int itemId) {
        return this._itemIdIndex.get(Integer.valueOf(itemId));
    }
}
