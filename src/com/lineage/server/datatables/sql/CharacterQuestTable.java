package com.lineage.server.datatables.sql;

import com.lineage.DatabaseFactory;
import com.lineage.server.datatables.CharObjidTable;
import com.lineage.server.datatables.QuestTable;
import com.lineage.server.datatables.storage.CharacterQuestStorage;
import com.lineage.server.templates.L1Quest;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CharacterQuestTable implements CharacterQuestStorage {
    private static final Log _log = LogFactory.getLog(CharacterQuestTable.class);
    private static final Map<Integer, HashMap<Integer, Integer>> _questList = new HashMap();

    @Override // com.lineage.server.datatables.storage.CharacterQuestStorage
    public void load() {
        delete();
        PerformanceTimer timer = new PerformanceTimer();
        Connection co = null;
        PreparedStatement pm = null;
        ResultSet rs = null;
        try {
            co = DatabaseFactory.get().getConnection();
            pm = co.prepareStatement("SELECT * FROM `character_quests`");
            rs = pm.executeQuery();
            while (rs.next()) {
                int char_id = rs.getInt("char_id");
                if (CharObjidTable.get().isChar(char_id) != null) {
                    int key = rs.getInt("quest_id");
                    switch (key) {
                        case 110:
                        case 117:
                        case 137:
                        case 139:
                            break;
                        default:
                            int value = rs.getInt("quest_step");
                            HashMap<Integer, Integer> hsMap = _questList.get(new Integer(char_id));
                            if (hsMap != null) {
                                hsMap.put(new Integer(key), new Integer(value));
                                break;
                            } else {
                                HashMap<Integer, Integer> hsMap2 = new HashMap<>();
                                hsMap2.put(new Integer(key), new Integer(value));
                                _questList.put(new Integer(char_id), hsMap2);
                                continue;
                            }
                    }
                } else {
                    delete(char_id);
                }
            }
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pm);
            SQLUtil.close(co);
        }
        _log.info("載入人物任務紀錄資料數量: " + _questList.size() + "(" + timer.get() + "ms)");
    }

    private static void delete() {
        deleteData(110);
        deleteData(117);
        deleteData(137);
        deleteData(139);
    }

    public static void deleteData(int questid) {
        Connection co = null;
        PreparedStatement pm = null;
        try {
            co = DatabaseFactory.get().getConnection();
            pm = co.prepareStatement("DELETE FROM `character_quests` WHERE `quest_id`=?");
            pm.setInt(1, questid);
            pm.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(pm);
            SQLUtil.close(co);
        }
    }

    private static void delete(int objid) {
        _questList.remove(Integer.valueOf(objid));
        Connection co = null;
        PreparedStatement pm = null;
        try {
            co = DatabaseFactory.get().getConnection();
            pm = co.prepareStatement("DELETE FROM `character_quests` WHERE `char_id`=?");
            pm.setInt(1, objid);
            pm.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(pm);
            SQLUtil.close(co);
        }
    }

    @Override // com.lineage.server.datatables.storage.CharacterQuestStorage
    public Map<Integer, Integer> get(int char_id) {
        return _questList.get(new Integer(char_id));
    }

    @Override // com.lineage.server.datatables.storage.CharacterQuestStorage
    public void storeQuest(int char_id, int key, int value) {
        HashMap<Integer, Integer> hsMap = _questList.get(new Integer(char_id));
        if (hsMap == null) {
            HashMap<Integer, Integer> hsMap2 = new HashMap<>();
            hsMap2.put(new Integer(key), new Integer(value));
            _questList.put(new Integer(char_id), hsMap2);
        } else {
            hsMap.put(new Integer(key), new Integer(value));
        }
        L1Quest quest = null;
        if (value == 1) {
            quest = QuestTable.get().getTemplate(key);
        }
        Connection co = null;
        PreparedStatement pm = null;
        String add = "";
        if (quest != null) {
            add = ",`note`=?";
        }
        try {
            co = DatabaseFactory.get().getConnection();
            pm = co.prepareStatement("INSERT INTO `character_quests` SET `char_id`=?,`quest_id`=?,`quest_step`=?" + add);
            int i = 0 + 1;
            pm.setInt(i, char_id);
            int i2 = i + 1;
            pm.setInt(i2, key);
            int i3 = i2 + 1;
            pm.setInt(i3, value);
            if (quest != null) {
                pm.setString(i3 + 1, quest.get_note());
            }
            pm.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(pm);
            SQLUtil.close(co);
        }
    }

    @Override // com.lineage.server.datatables.storage.CharacterQuestStorage
    public void updateQuest(int char_id, int key, int value) {
        HashMap<Integer, Integer> hsMap = _questList.get(new Integer(char_id));
        if (hsMap == null) {
            HashMap<Integer, Integer> hsMap2 = new HashMap<>();
            hsMap2.put(new Integer(key), new Integer(value));
            _questList.put(new Integer(char_id), hsMap2);
        } else {
            hsMap.put(new Integer(key), new Integer(value));
        }
        Connection co = null;
        PreparedStatement pm = null;
        try {
            co = DatabaseFactory.get().getConnection();
            pm = co.prepareStatement("UPDATE `character_quests` SET `quest_step`=? WHERE `char_id`=? AND `quest_id`=?");
            int i = 0 + 1;
            pm.setInt(i, value);
            int i2 = i + 1;
            pm.setInt(i2, char_id);
            pm.setInt(i2 + 1, key);
            pm.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(pm);
            SQLUtil.close(co);
        }
    }

    @Override // com.lineage.server.datatables.storage.CharacterQuestStorage
    public void delQuest(int char_id, int key) {
        HashMap<Integer, Integer> hsMap = _questList.get(new Integer(char_id));
        if (hsMap != null) {
            hsMap.remove(Integer.valueOf(key));
            Connection co = null;
            PreparedStatement pm = null;
            try {
                co = DatabaseFactory.get().getConnection();
                pm = co.prepareStatement("DELETE FROM `character_quests` WHERE `char_id`=? AND `quest_id`=?");
                int i = 0 + 1;
                pm.setInt(i, char_id);
                pm.setInt(i + 1, key);
                pm.execute();
            } catch (SQLException e) {
                _log.error(e.getLocalizedMessage(), e);
            } finally {
                SQLUtil.close(pm);
                SQLUtil.close(co);
            }
        }
    }
}
