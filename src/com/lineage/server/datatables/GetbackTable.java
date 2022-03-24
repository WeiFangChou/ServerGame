package com.lineage.server.datatables;

import com.lineage.DatabaseFactory;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1TownLocation;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GetbackTable {
    private static Map<Integer, ArrayList<GetbackTable>> _getback = new HashMap();
    private static final Log _log = LogFactory.getLog(GetbackTable.class);
    private static Random _random = new Random();
    private int _areaMapId;
    private int _areaX1;
    private int _areaX2;
    private int _areaY1;
    private int _areaY2;
    private int _getbackMapId;
    private int _getbackTownId;
    private int _getbackTownIdForDarkelf;
    private int _getbackTownIdForElf;
    private int _getbackX1;
    private int _getbackX2;
    private int _getbackX3;
    private int _getbackY1;
    private int _getbackY2;
    private int _getbackY3;

    private GetbackTable() {
    }

    private boolean isSpecifyArea() {
        return (this._areaX1 == 0 || this._areaY1 == 0 || this._areaX2 == 0 || this._areaY2 == 0) ? false : true;
    }

    public static void loadGetBack() {
        _getback.clear();
        PerformanceTimer timer = new PerformanceTimer();
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("SELECT * FROM `getback` ORDER BY `area_mapid`,`area_x1` DESC ");
            rs = pstm.executeQuery();
            while (rs.next()) {
                GetbackTable getback = new GetbackTable();
                getback._areaX1 = rs.getInt("area_x1");
                getback._areaY1 = rs.getInt("area_y1");
                getback._areaX2 = rs.getInt("area_x2");
                getback._areaY2 = rs.getInt("area_y2");
                getback._areaMapId = rs.getInt("area_mapid");
                getback._getbackX1 = rs.getInt("getback_x1");
                getback._getbackY1 = rs.getInt("getback_y1");
                getback._getbackX2 = rs.getInt("getback_x2");
                getback._getbackY2 = rs.getInt("getback_y2");
                getback._getbackX3 = rs.getInt("getback_x3");
                getback._getbackY3 = rs.getInt("getback_y3");
                getback._getbackMapId = rs.getInt("getback_mapid");
                getback._getbackTownId = rs.getInt("getback_townid");
                getback._getbackTownIdForElf = rs.getInt("getback_townid_elf");
                getback._getbackTownIdForDarkelf = rs.getInt("getback_townid_darkelf");
                ArrayList<GetbackTable> getbackList = _getback.get(Integer.valueOf(getback._areaMapId));
                if (getbackList == null) {
                    getbackList = new ArrayList<>();
                    _getback.put(Integer.valueOf(getback._areaMapId), getbackList);
                }
                getbackList.add(getback);
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
        _log.info("載入回村座標設置數量: " + _getback.size() + "(" + timer.get() + "ms)");
    }

    public static int[] GetBack_Location(L1PcInstance pc, boolean bScroll_Escape) {
        int[] loc = new int[3];
        int nPosition = _random.nextInt(3);
        int pcLocX = pc.getX();
        int pcLocY = pc.getY();
        ArrayList<GetbackTable> getbackList = _getback.get(Integer.valueOf(pc.getMapId()));
        if (getbackList != null) {
            GetbackTable getback = null;
            Iterator<GetbackTable> it = getbackList.iterator();
            while (true) {
                if (it.hasNext()) {
                    GetbackTable gb = it.next();
                    if (gb.isSpecifyArea()) {
                        if (gb._areaX1 <= pcLocX && pcLocX <= gb._areaX2 && gb._areaY1 <= pcLocY && pcLocY <= gb._areaY2) {
                            getback = gb;
                            break;
                        }
                    } else {
                        getback = gb;
                        break;
                    }
                } else {
                    break;
                }
            }
            int[] loc2 = ReadGetbackInfo(getback, nPosition);
            if (pc.isElf() && getback._getbackTownIdForElf > 0) {
                return L1TownLocation.getGetBackLoc(getback._getbackTownIdForElf);
            }
            if (pc.isDarkelf() && getback._getbackTownIdForDarkelf > 0) {
                return L1TownLocation.getGetBackLoc(getback._getbackTownIdForDarkelf);
            }
            if (getback._getbackTownId > 0) {
                return L1TownLocation.getGetBackLoc(getback._getbackTownId);
            }
            return loc2;
        }
        loc[0] = 33089;
        loc[1] = 33397;
        loc[2] = 4;
        return loc;
    }

    private static int[] ReadGetbackInfo(GetbackTable getback, int nPosition) {
        int[] loc = new int[3];
        switch (nPosition) {
            case 0:
                loc[0] = getback._getbackX1;
                loc[1] = getback._getbackY1;
                break;
            case 1:
                loc[0] = getback._getbackX2;
                loc[1] = getback._getbackY2;
                break;
            case 2:
                loc[0] = getback._getbackX3;
                loc[1] = getback._getbackY3;
                break;
        }
        loc[2] = getback._getbackMapId;
        return loc;
    }
}
