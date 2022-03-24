package com.lineage.server.datatables;

import com.lineage.DatabaseFactory;
import com.lineage.server.templates.L1NpcChat;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class NpcChatTable {
    private static NpcChatTable _instance;
    private static final Log _log = LogFactory.getLog(NpcChatTable.class);
    private static final Map<Integer, L1NpcChat> _npcChatAppearance = new HashMap();
    private static final Map<Integer, L1NpcChat> _npcChatDead = new HashMap();
    private static final Map<Integer, L1NpcChat> _npcChatGameTime = new HashMap();
    private static final Map<Integer, L1NpcChat> _npcChatHide = new HashMap();
    private Collection<L1NpcChat> _allTimeValues;

    public static NpcChatTable get() {
        if (_instance == null) {
            _instance = new NpcChatTable();
        }
        return _instance;
    }

    public void load() {
        PerformanceTimer timer = new PerformanceTimer();
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("SELECT * FROM `npcchat`");
            rs = pstm.executeQuery();
            while (rs.next()) {
                L1NpcChat npcChat = new L1NpcChat();
                npcChat.setNpcId(rs.getInt("npc_id"));
                npcChat.setChatTiming(rs.getInt("chat_timing"));
                npcChat.setStartDelayTime(rs.getInt("start_delay_time"));
                npcChat.setChatId1(rs.getString("chat_id1"));
                npcChat.setChatId2(rs.getString("chat_id2"));
                npcChat.setChatId3(rs.getString("chat_id3"));
                npcChat.setChatId4(rs.getString("chat_id4"));
                npcChat.setChatId5(rs.getString("chat_id5"));
                npcChat.setChatInterval(rs.getInt("chat_interval"));
                npcChat.setShout(rs.getBoolean("is_shout"));
                npcChat.setWorldChat(rs.getBoolean("is_world_chat"));
                npcChat.setRepeat(rs.getBoolean("is_repeat"));
                npcChat.setRepeatInterval(rs.getInt("repeat_interval"));
                npcChat.setGameTime(rs.getInt("game_time"));
                if (npcChat.getChatTiming() == 0) {
                    _npcChatAppearance.put(new Integer(npcChat.getNpcId()), npcChat);
                } else if (npcChat.getChatTiming() == 1) {
                    _npcChatDead.put(new Integer(npcChat.getNpcId()), npcChat);
                } else if (npcChat.getChatTiming() == 2) {
                    _npcChatHide.put(new Integer(npcChat.getNpcId()), npcChat);
                } else if (npcChat.getChatTiming() == 3) {
                    _npcChatGameTime.put(new Integer(npcChat.getNpcId()), npcChat);
                }
            }
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
        _log.info("載入NPC會話資料數量: " + (_npcChatAppearance.size() + _npcChatDead.size() + _npcChatHide.size() + _npcChatGameTime.size()) + "(" + timer.get() + "ms)");
    }

    public L1NpcChat getTemplateAppearance(int i) {
        return _npcChatAppearance.get(new Integer(i));
    }

    public L1NpcChat getTemplateDead(int i) {
        return _npcChatDead.get(new Integer(i));
    }

    public L1NpcChat getTemplateHide(int i) {
        return _npcChatHide.get(new Integer(i));
    }

    public L1NpcChat getTemplateGameTime(int i) {
        return _npcChatGameTime.get(new Integer(i));
    }

    public Collection<L1NpcChat> all() {
        try {
            Collection<L1NpcChat> vs = this._allTimeValues;
            if (vs != null) {
                return vs;
            }
            Collection<L1NpcChat> vs2 = Collections.unmodifiableCollection(_npcChatGameTime.values());
            this._allTimeValues = vs2;
            return vs2;
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    public L1NpcChat[] getAllGameTime() {
        return (L1NpcChat[]) _npcChatGameTime.values().toArray(new L1NpcChat[_npcChatGameTime.size()]);
    }
}
