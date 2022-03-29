package com.lineage.server.model;

import com.lineage.server.datatables.lock.CastleReading;
import com.lineage.server.model.gametime.L1GameTime;
import com.lineage.server.model.gametime.L1GameTimeAdapter;
import com.lineage.server.model.gametime.L1GameTimeClock;
import com.lineage.server.templates.L1Castle;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1CastleLocation {
    public static final int ADEN_CASTLE_ID = 7;
    private static final int ADEN_INNER_CASTLE_MAP = 300;
    private static final int ADEN_MAP = 4;
    private static final int ADEN_SUB_TOWER1_X = 34057;
    private static final int ADEN_SUB_TOWER1_Y = 33291;
    private static final int ADEN_SUB_TOWER2_X = 34123;
    private static final int ADEN_SUB_TOWER2_Y = 33291;
    private static final int ADEN_SUB_TOWER3_X = 34057;
    private static final int ADEN_SUB_TOWER3_Y = 33230;
    private static final int ADEN_SUB_TOWER4_X = 34123;
    private static final int ADEN_SUB_TOWER4_Y = 33230;
    private static final int ADEN_TOWER_MAP = 4;
    private static final int ADEN_TOWER_X = 34090;
    private static final int ADEN_TOWER_Y = 33260;
    private static final int ADEN_X1 = 34007;
    private static final int ADEN_X2 = 34162;
    private static final int ADEN_Y1 = 33172;
    private static final int ADEN_Y2 = 33332;
    public static final int DIAD_CASTLE_ID = 8;
    private static final int DIAD_INNER_CASTLE_MAP = 330;
    private static final int DIAD_MAP = 320;
    private static final int DIAD_TOWER_MAP = 320;
    private static final int DIAD_TOWER_X = 33033;
    private static final int DIAD_TOWER_Y = 32895;
    private static final int DIAD_X1 = 32888;
    private static final int DIAD_X2 = 33070;
    private static final int DIAD_Y1 = 32839;
    private static final int DIAD_Y2 = 32953;
    public static final int DOWA_CASTLE_ID = 6;
    private static final int DOWA_MAP = 66;
    private static final int DOWA_TOWER_MAP = 66;
    private static final int DOWA_TOWER_X = 32828;
    private static final int DOWA_TOWER_Y = 32818;
    private static final int DOWA_X1 = 32755;
    private static final int DOWA_X2 = 32870;
    private static final int DOWA_Y1 = 32790;
    private static final int DOWA_Y2 = 32920;
    public static final int GIRAN_CASTLE_ID = 4;
    private static final int GIRAN_INNER_CASTLE_MAP = 52;
    private static final int GIRAN_MAP = 4;
    private static final int GIRAN_TOWER_MAP = 4;
    private static final int GIRAN_TOWER_X = 33631;
    private static final int GIRAN_TOWER_Y = 32678;
    private static final int GIRAN_X1 = 33559;
    private static final int GIRAN_X2 = 33686;
    private static final int GIRAN_Y1 = 32615;
    private static final int GIRAN_Y2 = 32755;
    public static final int HEINE_CASTLE_ID = 5;
    private static final int HEINE_INNER_CASTLE_MAP = 64;
    private static final int HEINE_MAP = 4;
    private static final int HEINE_TOWER_MAP = 4;
    private static final int HEINE_TOWER_X = 33524;
    private static final int HEINE_TOWER_Y = 33396;
    private static final int HEINE_X1 = 33458;
    private static final int HEINE_X2 = 33583;
    private static final int HEINE_Y1 = 33315;
    private static final int HEINE_Y2 = 33490;
    public static final int KENT_CASTLE_ID = 1;
    private static final int KENT_INNER_CASTLE_MAP = 15;
    private static final int KENT_MAP = 4;
    private static final int KENT_TOWER_MAP = 4;
    private static final int KENT_TOWER_X = 33139;
    private static final int KENT_TOWER_Y = 32768;
    private static final int KENT_X1 = 33089;
    private static final int KENT_X2 = 33219;
    private static final int KENT_Y1 = 32717;
    private static final int KENT_Y2 = 32827;
    public static final int OT_CASTLE_ID = 2;
    private static final int OT_MAP = 4;
    private static final int OT_TOWER_MAP = 4;
    private static final int OT_TOWER_X = 32798;
    private static final int OT_TOWER_Y = 32291;
    private static final int OT_X1 = 32750;
    private static final int OT_X2 = 32850;
    private static final int OT_Y1 = 32250;
    private static final int OT_Y2 = 32350;
    public static final int WW_CASTLE_ID = 3;
    private static final int WW_INNER_CASTLE_MAP = 29;
    private static final int WW_MAP = 4;
    private static final int WW_TOWER_MAP = 4;
    private static final int WW_TOWER_X = 32623;
    private static final int WW_TOWER_Y = 33379;
    private static final int WW_X1 = 32571;
    private static final int WW_X2 = 32721;
    private static final int WW_Y1 = 33350;
    private static final int WW_Y2 = 33460;
    private static final Map<Integer, L1MapArea> _areas = new HashMap();
    private static Map<Integer, Integer> _castleTaxRate = new HashMap();
    private static final Map<Integer, Integer> _innerTowerMaps = new HashMap();
    private static final Map<Integer, L1Clan> _isCastle = new HashMap();
    private static L1CastleTaxRateListener _listener;
    private static final Log _log = LogFactory.getLog(L1CastleLocation.class);
    private static final Map<Integer, L1Location> _subTowers = new HashMap();
    private static final Map<Integer, L1Location> _towers = new HashMap();

    static {
        _towers.put(1, new L1Location((int) KENT_TOWER_X, (int) KENT_TOWER_Y, 4));
        _towers.put(2, new L1Location((int) OT_TOWER_X, (int) OT_TOWER_Y, 4));
        _towers.put(3, new L1Location((int) WW_TOWER_X, (int) WW_TOWER_Y, 4));
        _towers.put(4, new L1Location((int) GIRAN_TOWER_X, (int) GIRAN_TOWER_Y, 4));
        _towers.put(5, new L1Location((int) HEINE_TOWER_X, (int) HEINE_TOWER_Y, 4));
        _towers.put(6, new L1Location((int) DOWA_TOWER_X, (int) DOWA_TOWER_Y, 66));
        _towers.put(7, new L1Location((int) ADEN_TOWER_X, (int) ADEN_TOWER_Y, 4));
        _towers.put(8, new L1Location((int) DIAD_TOWER_X, (int) DIAD_TOWER_Y, 320));
        _areas.put(1, new L1MapArea(KENT_X1, KENT_Y1, KENT_X2, KENT_Y2, 4));
        _areas.put(2, new L1MapArea(OT_X1, OT_Y1, OT_X2, OT_Y2, 4));
        _areas.put(3, new L1MapArea(WW_X1, WW_Y1, WW_X2, WW_Y2, 4));
        _areas.put(4, new L1MapArea(GIRAN_X1, GIRAN_Y1, GIRAN_X2, 32755, 4));
        _areas.put(5, new L1MapArea(HEINE_X1, HEINE_Y1, HEINE_X2, HEINE_Y2, 4));
        _areas.put(6, new L1MapArea(32755, DOWA_Y1, DOWA_X2, DOWA_Y2, 66));
        _areas.put(7, new L1MapArea(ADEN_X1, ADEN_Y1, ADEN_X2, ADEN_Y2, 4));
        _areas.put(8, new L1MapArea(DIAD_X1, DIAD_Y1, DIAD_X2, DIAD_Y2, 320));
        _innerTowerMaps.put(1, 15);
        _innerTowerMaps.put(3, 29);
        _innerTowerMaps.put(4, 52);
        _innerTowerMaps.put(5, 64);
        _innerTowerMaps.put(7, 300);
        _innerTowerMaps.put(8, 330);
        _subTowers.put(1, new L1Location(34057, 33291, 4));
        _subTowers.put(2, new L1Location(34123, 33291, 4));
        _subTowers.put(3, new L1Location(34057, 33230, 4));
        _subTowers.put(4, new L1Location(34123, 33230, 4));
    }

    private L1CastleLocation() {
    }

    public static Map<Integer, L1Clan> mapCastle() {
        return _isCastle;
    }

    public static L1Clan castleClan(Integer key) {
        return _isCastle.get(key);
    }

    public static void putCastle(Integer key, L1Clan value) {
        try {
            _isCastle.put(key, value);
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public static void removeCastle(Integer key) {
        try {
            _isCastle.remove(key);
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public static int getCastleId(L1Location loc) {
        for (Map.Entry<Integer, L1Location> entry : _towers.entrySet()) {
            if (entry.getValue().equals(loc)) {
                return entry.getKey().intValue();
            }
        }
        return 0;
    }

    public static int getCastleId(int locx, int locy, int mapid) {
        return getCastleId(new L1Location(locx, locy, mapid));
    }

    public static int getCastleIdByArea(L1Location loc) {
        for (Map.Entry<Integer, L1MapArea> entry : _areas.entrySet()) {
            if (entry.getValue().contains(loc)) {
                return entry.getKey().intValue();
            }
        }
        for (Map.Entry<Integer, Integer> entry2 : _innerTowerMaps.entrySet()) {
            if (entry2.getValue().intValue() == loc.getMapId()) {
                return entry2.getKey().intValue();
            }
        }
        return 0;
    }

    public static int getCastleIdByArea(L1Character cha) {
        return getCastleIdByArea(cha.getLocation());
    }

    public static boolean checkInWarArea(int castleId, L1Location loc) {
        return castleId == getCastleIdByArea(loc);
    }

    public static boolean checkInWarArea(int castleId, L1Character cha) {
        return checkInWarArea(castleId, cha.getLocation());
    }

    public static boolean checkInAllWarArea(L1Location loc) {
        return getCastleIdByArea(loc) != 0;
    }

    public static boolean checkInAllWarArea(int locx, int locy, int mapid) {
        return checkInAllWarArea(new L1Location(locx, locy, mapid));
    }

    public static int[] getTowerLoc(int castleId) {
        int[] result = new int[3];
        L1Location loc = _towers.get(Integer.valueOf(castleId));
        if (loc != null) {
            result[0] = loc.getX();
            result[1] = loc.getY();
            result[2] = loc.getMapId();
        }
        return result;
    }

    public static int[] getWarArea(int castleId) {
        int[] loc = new int[5];
        switch (castleId) {
            case 1:
                loc[0] = KENT_X1;
                loc[1] = KENT_X2;
                loc[2] = KENT_Y1;
                loc[3] = KENT_Y2;
                loc[4] = 4;
                break;
            case 2:
                loc[0] = OT_X1;
                loc[1] = OT_X2;
                loc[2] = OT_Y1;
                loc[3] = OT_Y2;
                loc[4] = 4;
                break;
            case 3:
                loc[0] = WW_X1;
                loc[1] = WW_X2;
                loc[2] = WW_Y1;
                loc[3] = WW_Y2;
                loc[4] = 4;
                break;
            case 4:
                loc[0] = GIRAN_X1;
                loc[1] = GIRAN_X2;
                loc[2] = GIRAN_Y1;
                loc[3] = 32755;
                loc[4] = 4;
                break;
            case 5:
                loc[0] = HEINE_X1;
                loc[1] = HEINE_X2;
                loc[2] = HEINE_Y1;
                loc[3] = HEINE_Y2;
                loc[4] = 4;
                break;
            case 6:
                loc[0] = 32755;
                loc[1] = DOWA_X2;
                loc[2] = DOWA_Y1;
                loc[3] = DOWA_Y2;
                loc[4] = 66;
                break;
            case 7:
                loc[0] = ADEN_X1;
                loc[1] = ADEN_X2;
                loc[2] = ADEN_Y1;
                loc[3] = ADEN_Y2;
                loc[4] = 4;
                break;
            case 8:
                loc[0] = DIAD_X1;
                loc[1] = DIAD_X2;
                loc[2] = DIAD_Y1;
                loc[3] = DIAD_Y2;
                loc[4] = 320;
                break;
        }
        return loc;
    }

    public static int[] getCastleLoc(int castle_id) {
        int[] loc = new int[3];
        switch (castle_id) {
            case 1:
                loc[0] = 32731;
                loc[1] = 32810;
                loc[2] = 15;
                break;
            case 2:
                loc[0] = 32800;
                loc[1] = 32277;
                loc[2] = 4;
                break;
            case 3:
                loc[0] = 32730;
                loc[1] = 32814;
                loc[2] = 29;
                break;
            case 4:
                loc[0] = 32724;
                loc[1] = KENT_Y2;
                loc[2] = 52;
                break;
            case 5:
                loc[0] = 32568;
                loc[1] = 32855;
                loc[2] = 64;
                break;
            case 6:
                loc[0] = 32853;
                loc[1] = 32810;
                loc[2] = 66;
                break;
            case 7:
                loc[0] = 32892;
                loc[1] = 32572;
                loc[2] = 300;
                break;
            case 8:
                loc[0] = 32733;
                loc[1] = 32985;
                loc[2] = 330;
                break;
        }
        return loc;
    }

    public static int[] getGetBackLoc(int castle_id) {
        switch (castle_id) {
            case 1:
                return L1TownLocation.getGetBackLoc(6);
            case 2:
                return L1TownLocation.getGetBackLoc(4);
            case 3:
                return L1TownLocation.getGetBackLoc(5);
            case 4:
                return L1TownLocation.getGetBackLoc(7);
            case 5:
                return L1TownLocation.getGetBackLoc(8);
            case 6:
                return L1TownLocation.getGetBackLoc(9);
            case 7:
                return L1TownLocation.getGetBackLoc(12);
            case 8:
                return new int[][]{new int[]{32792, 32807, 310}, new int[]{32816, 32820, 310}, new int[]{32823, 32797, 310}}[new Random().nextInt(3)];
            default:
                return L1TownLocation.getGetBackLoc(2);
        }
    }

    public static int getCastleIdByNpcid(int npcid) {
        switch (L1TownLocation.getTownIdByNpcid(npcid)) {
            case 1:
            case 7:
                return 4;
            case 2:
            case 5:
                return 3;
            case 3:
            case 6:
                return 1;
            case 4:
                return 2;
            case 8:
                return 5;
            case 9:
            case 10:
                return 6;
            case 11:
            case 13:
            default:
                return 0;
            case 12:
                return 7;
            case 14:
                return 8;
        }
    }

    public static int getCastleTaxRateByNpcId(int npcId) {
        int castleId = getCastleIdByNpcid(npcId);
        if (castleId != 0) {
            return _castleTaxRate.get(Integer.valueOf(castleId)).intValue();
        }
        return 0;
    }

    public static void setCastleTaxRate() {
        try {
            L1Castle[] castleTableList = CastleReading.get().getCastleTableList();
            for (L1Castle castle : castleTableList) {
                _castleTaxRate.put(Integer.valueOf(castle.getId()), Integer.valueOf(castle.getTaxRate()));
            }
            if (_listener == null) {
                _listener = new L1CastleTaxRateListener(null);
                L1GameTimeClock.getInstance().addListener(_listener);
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /* access modifiers changed from: private */
    public static class L1CastleTaxRateListener extends L1GameTimeAdapter {
        private L1CastleTaxRateListener() {
        }

        /* synthetic */ L1CastleTaxRateListener(L1CastleTaxRateListener l1CastleTaxRateListener) {
            this();
        }

        @Override // com.lineage.server.model.gametime.L1GameTimeAdapter, com.lineage.server.model.gametime.L1GameTimeListener
        public void onDayChanged(L1GameTime time) {
            try {
                L1CastleLocation.setCastleTaxRate();
            } catch (Exception e) {
                L1CastleLocation._log.error(e.getLocalizedMessage(), e);
            }
        }
    }

    public static int[] getSubTowerLoc(int no) {
        int[] result = new int[3];
        L1Location loc = _subTowers.get(Integer.valueOf(no));
        if (loc != null) {
            result[0] = loc.getX();
            result[1] = loc.getY();
            result[2] = loc.getMapId();
        }
        return result;
    }
}
