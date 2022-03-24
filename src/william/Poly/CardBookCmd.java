package william.Poly;

import com.lineage.DatabaseFactory;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.utils.SQLUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CardBookCmd {
    private static CardBookCmd _instance;
    private static final Log _log = LogFactory.getLog(CardBookCmd.class);

    public static CardBookCmd get() {
        if (_instance == null) {
            _instance = new CardBookCmd();
        }
        return _instance;
    }

    public static void CardSetTable(L1PcInstance pc) {
        Npc_PowerLog.get().effectBuff(pc, 1);
        HashSet<Integer> id = new HashSet<>();
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("SELECT `quest` FROM `道具兌換能力系統log_1` WHERE `account_name`=?");
            pstm.setString(1, pc.getAccountName());
            rs = pstm.executeQuery();
            while (rs.next()) {
                id.add(Integer.valueOf(rs.getInt(1)));
            }
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
        for (int i = 0; i <= NpcPower.get().NpcPowerSize(); i++) {
            NpcPower Np = NpcPower.get().getPower(i);
            if (Np != null) {
                Iterator<Integer> it = id.iterator();
                while (it.hasNext()) {
                    if (it.next().intValue() == Np.getQuest() && pc.getQuest().get_step(Np.getQuest()) == 0) {
                        Npc_PowerLog.get().storeOther(pc.getId(), Np);
                        pc.getQuest().set_end(Np.getQuest());
                    }
                }
            }
        }
    }
}
