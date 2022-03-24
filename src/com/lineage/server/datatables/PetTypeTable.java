package com.lineage.server.datatables;

import com.lineage.DatabaseFactory;
import com.lineage.server.templates.L1PetType;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.RangeInt;
import com.lineage.server.utils.SQLUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PetTypeTable {
    private static final Set<String> _defaultNames = new HashSet();
    private static PetTypeTable _instance;
    private static final Log _log = LogFactory.getLog(PetTypeTable.class);
    private static final Map<Integer, L1PetType> _types = new HashMap();

    public static void load() {
        _instance = new PetTypeTable();
    }

    public static PetTypeTable getInstance() {
        return _instance;
    }

    private PetTypeTable() {
        PerformanceTimer timer = new PerformanceTimer();
        loadTypes();
        _log.info("載入寵物種族資料數量: " + _types.size() + "(" + timer.get() + "ms)");
    }

    private void loadTypes() {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("SELECT * FROM pettypes");
            rs = pstm.executeQuery();
            while (rs.next()) {
                int Level = rs.getInt("level");
                int baseNpcId = rs.getInt("BaseNpcId");
                String name = rs.getString("Name");
                int itemIdForTaming = rs.getInt("ItemIdForTaming");
                int hpUpMin = rs.getInt("HpUpMin");
                int hpUpMax = rs.getInt("HpUpMax");
                int mpUpMin = rs.getInt("MpUpMin");
                int mpUpMax = rs.getInt("MpUpMax");
                int evolvItemId = rs.getInt("EvolvItemId");
                int npcIdForEvolving = rs.getInt("NpcIdForEvolving");
                int[] msgIds = new int[5];
                for (int i = 0; i < 5; i++) {
                    msgIds[i] = rs.getInt("MessageId" + (i + 1));
                }
                int defyMsgId = rs.getInt("DefyMessageId");
                int board = rs.getInt("Board");
                _types.put(Integer.valueOf(baseNpcId), new L1PetType(Level, baseNpcId, name, itemIdForTaming, new RangeInt(hpUpMin, hpUpMax), new RangeInt(mpUpMin, mpUpMax), evolvItemId, npcIdForEvolving, msgIds, defyMsgId, board));
                _defaultNames.add(name.toLowerCase());
            }
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    public L1PetType get(int baseNpcId) {
        return _types.get(Integer.valueOf(baseNpcId));
    }

    public boolean isNameDefault(String name) {
        return _defaultNames.contains(name.toLowerCase());
    }
}
