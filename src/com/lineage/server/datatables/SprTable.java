package com.lineage.server.datatables;

import com.lineage.DatabaseFactory;
import com.lineage.server.templates.L1Command;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SprTable {
    private static final Map<Integer, Spr> _dataMap = new HashMap();
    private static SprTable _instance;
    private static final Log _log = LogFactory.getLog(SprTable.class);

    /* access modifiers changed from: private */
    public static class Spr {
        private final Map<Integer, Integer> _attackSpeed;
        private int _dirSpellSpeed;
        private int _dirSpellSpeed30;
        private int _dmg;
        private final Map<Integer, int[]> _frame;
        private final Map<Integer, Integer> _moveSpeed;
        private int _nodirSpellSpeed;

        private Spr() {
            this._moveSpeed = new HashMap();
            this._attackSpeed = new HashMap();
            this._frame = new HashMap();
            this._nodirSpellSpeed = 0;
            this._dirSpellSpeed = 0;
            this._dirSpellSpeed30 = 0;
            this._dmg = 0;
        }

        /* synthetic */ Spr(Spr spr) {
            this();
        }
    }

    public static SprTable get() {
        if (_instance == null) {
            _instance = new SprTable();
        }
        return _instance;
    }

    public void load() throws Exception {

        PerformanceTimer timer = new PerformanceTimer();
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        Spr spr = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("SELECT * FROM `spr_action`");
            rs = pstm.executeQuery();
            while (rs.next()) {
                    int key = rs.getInt("spr_id");
                    if (!_dataMap.containsKey(key)) {
                        spr = new Spr(null);
                        _dataMap.put(key, spr);
                    } else {
                        spr = _dataMap.get(key);
                    }
                    int actid = rs.getInt("act_id");
                    int frameCount = rs.getInt("framecount");
                    if (frameCount < 0) {
                        frameCount = 0;
                    }
                    int frameRate = rs.getInt("framerate");
                    if (frameRate < 0) {
                        frameRate = 0;
                    }
                    int speed = calcActionSpeed(frameCount, frameRate);
                    int[] frame = {frameCount, frameRate};
                    switch (actid) {
                        case 0:
                        case 4:
                        case 11:
                        case 20:
                        case 24:
                        case 40:
                        case 46:
                        case 50:
                        case 54:
                        case 58:
                        case 62:
                        case 83:
                            spr._moveSpeed.put(actid, speed);
                            break;
                        case 1:
                        case 5:
                        case 12:
                        case 21:
                        case 25:
                        case 41:
                        case 47:
                        case 51:
                        case 55:
                        case 59:
                        case 63:
                        case 84:
                            spr._attackSpeed.put(actid, speed);
                            spr._frame.put(actid, frame);
                            break;
                        case 2:
                            spr._dmg = speed;
                            break;
                        case 18:
                            spr._dirSpellSpeed = speed;
                            break;
                        case 19:
                            spr._nodirSpellSpeed = speed;
                            break;
                        case 30:
                            spr._dirSpellSpeed30 = speed;
                            break;
                    }
            }

        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        }finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
        _log.info("載入圖形影格資料數量: " + _dataMap.size() + "(" + timer.get() + "ms)");
    }

    private int calcActionSpeed(int frameCount, int frameRate) {
        return (int) (((double) (frameCount * 40)) * (24.0d / ((double) frameRate)));
    }

    public final boolean containsTripleArrowSpr(int sprid) {
        if (_dataMap.containsKey(sprid)) {
            return _dataMap.get(sprid)._attackSpeed.containsKey(21);
        }
        return false;
    }

    public Collection<String> getspr() {
        ArrayList<String> list = new ArrayList<>();
        for (L1Command command : CommandsTable.get().getList()) {
            list.add(command.getName() + ": " + command.get_note());
        }
        return list;
    }

    public int getAttackSpeed(int sprid, int actid) {
        if (!_dataMap.containsKey(sprid)) {
            return 0;
        }
        if (_dataMap.get(sprid)._attackSpeed.containsKey(actid)) {
            return _dataMap.get(sprid)._attackSpeed.get(actid);
        }
        if (actid == 1) {
            return 0;
        }
        return _dataMap.get(sprid)._attackSpeed.get(1);
    }

    public int getMoveSpeed(int sprid, int actid) {
        if (!_dataMap.containsKey(sprid)) {
            return 0;
        }
        if (_dataMap.get(sprid)._moveSpeed.containsKey(actid)) {
            return _dataMap.get(sprid)._moveSpeed.get(actid);
        }
        if (actid == 0) {
            return 0;
        }
        return _dataMap.get(sprid)._moveSpeed.get(0);
    }

    public int getDirSpellSpeed(int sprid) {
        if (_dataMap.containsKey(sprid)) {
            return _dataMap.get(sprid)._dirSpellSpeed;
        }
        return 0;
    }

    public int getNodirSpellSpeed(int sprid) {
        if (_dataMap.containsKey(sprid)) {
            return _dataMap.get(sprid)._nodirSpellSpeed;
        }
        return 0;
    }

    public int getDirSpellSpeed30(int sprid) {
        if (_dataMap.containsKey(sprid)) {
            return _dataMap.get(sprid)._dirSpellSpeed30;
        }
        return 0;
    }

    public int getDmg(int sprid) {
        if (_dataMap.containsKey(sprid)) {
            return _dataMap.get(sprid)._dmg;
        }
        return 0;
    }

    public long spr_move_speed(int tempCharGfx) {
        return 200;
    }

    public long spr_attack_speed(int tempCharGfx) {
        return 200;
    }

    public long spr_skill_speed(int tempCharGfx) {
        return 200;
    }

    public final boolean containsChainswordSpr(int sprid) {
        if (_dataMap.containsKey(sprid)) {
            return _dataMap.get(sprid)._moveSpeed.containsKey(83);
        }
        return false;
    }

    public int[] getFrame(int sprid, int actid) {
        if (!_dataMap.containsKey(sprid)) {
            return null;
        }
        if (_dataMap.get(sprid)._attackSpeed.containsKey(actid)) {
            return (int[]) _dataMap.get(sprid)._frame.get(actid);
        }
        if (actid == 1) {
            return null;
        }
        return (int[]) _dataMap.get(sprid)._frame.get(1);
    }
}
