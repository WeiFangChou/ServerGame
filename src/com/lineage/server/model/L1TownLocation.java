package com.lineage.server.model;

import com.lineage.server.datatables.TownSetTable;
import com.lineage.server.datatables.lock.TownReading;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.types.Point;
import java.util.Random;

public class L1TownLocation {
    private static final Point[] GETBACK_LOC_ADEN = {new Point(33915, 33114), new Point(34061, 33115), new Point(34090, 33168), new Point(34011, 33136), new Point(34093, 33117), new Point(33959, 33156), new Point(33992, 33120), new Point(34047, 33156)};
    private static final Point[] GETBACK_LOC_ELVEN_FOREST = {new Point(33065, 32358), new Point(33052, 32313), new Point(33030, 32342), new Point(33068, 32320), new Point(33071, 32314), new Point(33030, 32370), new Point(33076, 32324), new Point(33068, 32336)};
    private static final Point[] GETBACK_LOC_GIRAN = {new Point(33435, 32803), new Point(33439, 32817), new Point(33440, 32809), new Point(33419, 32810), new Point(33426, 32823), new Point(33418, 32818), new Point(33432, 32824)};
    private static final Point[] GETBACK_LOC_GLUDIO = {new Point(32601, 32757), new Point(32625, 32809), new Point(32611, 32727), new Point(32612, 32781), new Point(32605, 32761), new Point(32614, 32739), new Point(32626, 32775)};
    private static final Point[] GETBACK_LOC_HEINE = {new Point(33593, 33242), new Point(33593, 33248), new Point(33604, 33236), new Point(33599, 33236), new Point(33610, 33247), new Point(33610, 33241), new Point(33599, 33252), new Point(33605, 33252)};
    private static final Point[] GETBACK_LOC_KENT = {new Point(33048, 32750), new Point(33059, 32768), new Point(33047, 32761), new Point(33059, 32759), new Point(33051, 32775), new Point(33048, 32778), new Point(33064, 32773), new Point(33057, 32748)};
    private static final Point[] GETBACK_LOC_ORCISH_FOREST = {new Point(32750, 32435), new Point(32745, 32447), new Point(32738, 32452), new Point(32741, 32436), new Point(32749, 32446)};
    private static final Point[] GETBACK_LOC_OREN = {new Point(34086, 32280), new Point(34037, 32230), new Point(34022, 32254), new Point(34021, 32269), new Point(34044, 32290), new Point(34049, 32316), new Point(34081, 32249), new Point(34074, 32313), new Point(34064, 32230)};
    private static final Point[] GETBACK_LOC_OUM_DUNGEON = {new Point(32818, 32805), new Point(32800, 32798), new Point(32815, 32819), new Point(32823, 32811), new Point(32817, 32828)};
    private static final Point[] GETBACK_LOC_PIRATE_ISLAND = {new Point(32431, 33058), new Point(32407, 33054)};
    private static final Point[] GETBACK_LOC_RECLUSE_VILLAGE = {new Point(32599, 32916), new Point(32599, 32923), new Point(32603, 32908), new Point(32595, 32908), new Point(32591, 32918)};
    private static final Point[] GETBACK_LOC_RESISTANCE = {new Point(32570, 32667), new Point(32559, 32678), new Point(32564, 32683), new Point(32574, 32661), new Point(32576, 32669), new Point(32572, 32662)};
    private static final Point[] GETBACK_LOC_SILENT_CAVERN = {new Point(32856, 32898), new Point(32860, 32916), new Point(32868, 32893), new Point(32875, 32903), new Point(32855, 32898)};
    private static final Point[] GETBACK_LOC_SILVER_KNIGHT_TOWN = {new Point(33071, 33402), new Point(33091, 33396), new Point(33085, 33402), new Point(33097, 33366), new Point(33110, 33365), new Point(33072, 33392)};
    private static final Point[] GETBACK_LOC_TALKING_ISLAND = {new Point(32600, 32942), new Point(32574, 32944), new Point(32580, 32923), new Point(32557, 32975), new Point(32597, 32914), new Point(32580, 32974)};
    private static final Point[] GETBACK_LOC_WERLDAN = {new Point(33702, 32492), new Point(33747, 32508), new Point(33696, 32498), new Point(33723, 32512), new Point(33710, 32521), new Point(33724, 32488), new Point(33693, 32513)};
    private static final Point[] GETBACK_LOC_WINDAWOOD = {new Point(32608, 33178), new Point(32626, 33185), new Point(32630, 33179), new Point(32625, 33207), new Point(32638, 33203), new Point(32621, 33179)};
    private static final int GETBACK_MAP_ADEN = 4;
    private static final int GETBACK_MAP_ELVEN_FOREST = 4;
    private static final int GETBACK_MAP_GIRAN = 4;
    private static final int GETBACK_MAP_GLUDIO = 4;
    private static final int GETBACK_MAP_HEINE = 4;
    private static final int GETBACK_MAP_KENT = 4;
    private static final int GETBACK_MAP_ORCISH_FOREST = 4;
    private static final int GETBACK_MAP_OREN = 4;
    private static final int GETBACK_MAP_OUM_DUNGEON = 310;
    private static final int GETBACK_MAP_PIRATE_ISLAND = 440;
    private static final int GETBACK_MAP_RECLUSE_VILLAGE = 400;
    private static final int GETBACK_MAP_RESISTANCE = 400;
    private static final int GETBACK_MAP_SILENT_CAVERN = 304;
    private static final int GETBACK_MAP_SILVER_KNIGHT_TOWN = 4;
    private static final int GETBACK_MAP_TALKING_ISLAND = 0;
    private static final int GETBACK_MAP_WERLDAN = 4;
    private static final int GETBACK_MAP_WINDAWOOD = 4;
    public static final int TOWNID_ADEN = 12;
    public static final int TOWNID_ELVEN_FOREST = 11;
    public static final int TOWNID_GIRAN = 7;
    public static final int TOWNID_GLUDIO = 3;
    public static final int TOWNID_HEINE = 8;
    public static final int TOWNID_KENT = 6;
    public static final int TOWNID_ORCISH_FOREST = 4;
    public static final int TOWNID_OREN = 10;
    public static final int TOWNID_OUM_DUNGEON = 14;
    public static final int TOWNID_PIRATE_ISLAND = 16;
    public static final int TOWNID_RECLUSE_VILLAGE = 17;
    public static final int TOWNID_RESISTANCE = 15;
    public static final int TOWNID_SILENT_CAVERN = 13;
    public static final int TOWNID_SILVER_KNIGHT_TOWN = 2;
    public static final int TOWNID_TALKING_ISLAND = 1;
    public static final int TOWNID_WERLDAN = 9;
    public static final int TOWNID_WINDAWOOD = 5;

    private L1TownLocation() {
    }

    public static final boolean isGambling(L1PcInstance pc) {
        return isGambling(pc.getX(), pc.getY(), pc.getMapId());
    }

    public static final boolean isGambling(int x, int y, int map) {
        if (map == 4) {
            if (x > 33469 && x < 33528 && y > 32837 && y < 32848) {
                return true;
            }
            if (x > 33466 && x < 33532 && y > 32858 && y < 32870) {
                return true;
            }
            if (x <= 33528 || x >= 33427 || y <= 32848 || y >= 32871) {
                return false;
            }
            return true;
        }
        return false;
    }

    public static int[] getGetBackLoc(int town_id) {
        Random random = new Random();
        int[] loc = new int[3];
        switch (town_id) {
            case 1:
                int rnd = random.nextInt(GETBACK_LOC_TALKING_ISLAND.length);
                loc[0] = GETBACK_LOC_TALKING_ISLAND[rnd].getX();
                loc[1] = GETBACK_LOC_TALKING_ISLAND[rnd].getY();
                loc[2] = 0;
                break;
            case 2:
                int rnd2 = random.nextInt(GETBACK_LOC_SILVER_KNIGHT_TOWN.length);
                loc[0] = GETBACK_LOC_SILVER_KNIGHT_TOWN[rnd2].getX();
                loc[1] = GETBACK_LOC_SILVER_KNIGHT_TOWN[rnd2].getY();
                loc[2] = 4;
                break;
            case 3:
                int rnd3 = random.nextInt(GETBACK_LOC_GLUDIO.length);
                loc[0] = GETBACK_LOC_GLUDIO[rnd3].getX();
                loc[1] = GETBACK_LOC_GLUDIO[rnd3].getY();
                loc[2] = 4;
                break;
            case 4:
                int rnd4 = random.nextInt(GETBACK_LOC_ORCISH_FOREST.length);
                loc[0] = GETBACK_LOC_ORCISH_FOREST[rnd4].getX();
                loc[1] = GETBACK_LOC_ORCISH_FOREST[rnd4].getY();
                loc[2] = 4;
                break;
            case 5:
                int rnd5 = random.nextInt(GETBACK_LOC_WINDAWOOD.length);
                loc[0] = GETBACK_LOC_WINDAWOOD[rnd5].getX();
                loc[1] = GETBACK_LOC_WINDAWOOD[rnd5].getY();
                loc[2] = 4;
                break;
            case 6:
                int rnd6 = random.nextInt(GETBACK_LOC_KENT.length);
                loc[0] = GETBACK_LOC_KENT[rnd6].getX();
                loc[1] = GETBACK_LOC_KENT[rnd6].getY();
                loc[2] = 4;
                break;
            case 7:
                int rnd7 = random.nextInt(GETBACK_LOC_GIRAN.length);
                loc[0] = GETBACK_LOC_GIRAN[rnd7].getX();
                loc[1] = GETBACK_LOC_GIRAN[rnd7].getY();
                loc[2] = 4;
                break;
            case 8:
                int rnd8 = random.nextInt(GETBACK_LOC_HEINE.length);
                loc[0] = GETBACK_LOC_HEINE[rnd8].getX();
                loc[1] = GETBACK_LOC_HEINE[rnd8].getY();
                loc[2] = 4;
                break;
            case 9:
                int rnd9 = random.nextInt(GETBACK_LOC_WERLDAN.length);
                loc[0] = GETBACK_LOC_WERLDAN[rnd9].getX();
                loc[1] = GETBACK_LOC_WERLDAN[rnd9].getY();
                loc[2] = 4;
                break;
            case 10:
                int rnd10 = random.nextInt(GETBACK_LOC_OREN.length);
                loc[0] = GETBACK_LOC_OREN[rnd10].getX();
                loc[1] = GETBACK_LOC_OREN[rnd10].getY();
                loc[2] = 4;
                break;
            case 11:
                int rnd11 = random.nextInt(GETBACK_LOC_ELVEN_FOREST.length);
                loc[0] = GETBACK_LOC_ELVEN_FOREST[rnd11].getX();
                loc[1] = GETBACK_LOC_ELVEN_FOREST[rnd11].getY();
                loc[2] = 4;
                break;
            case 12:
                int rnd12 = random.nextInt(GETBACK_LOC_ADEN.length);
                loc[0] = GETBACK_LOC_ADEN[rnd12].getX();
                loc[1] = GETBACK_LOC_ADEN[rnd12].getY();
                loc[2] = 4;
                break;
            case 13:
                int rnd13 = random.nextInt(GETBACK_LOC_SILENT_CAVERN.length);
                loc[0] = GETBACK_LOC_SILENT_CAVERN[rnd13].getX();
                loc[1] = GETBACK_LOC_SILENT_CAVERN[rnd13].getY();
                loc[2] = 304;
                break;
            case 14:
                int rnd14 = random.nextInt(GETBACK_LOC_OUM_DUNGEON.length);
                loc[0] = GETBACK_LOC_OUM_DUNGEON[rnd14].getX();
                loc[1] = GETBACK_LOC_OUM_DUNGEON[rnd14].getY();
                loc[2] = 310;
                break;
            case 15:
                int rnd15 = random.nextInt(GETBACK_LOC_RESISTANCE.length);
                loc[0] = GETBACK_LOC_RESISTANCE[rnd15].getX();
                loc[1] = GETBACK_LOC_RESISTANCE[rnd15].getY();
                loc[2] = 400;
                break;
            case 16:
                int rnd16 = random.nextInt(GETBACK_LOC_PIRATE_ISLAND.length);
                loc[0] = GETBACK_LOC_PIRATE_ISLAND[rnd16].getX();
                loc[1] = GETBACK_LOC_PIRATE_ISLAND[rnd16].getY();
                loc[2] = 440;
                break;
            case 17:
                int rnd17 = random.nextInt(GETBACK_LOC_RECLUSE_VILLAGE.length);
                loc[0] = GETBACK_LOC_RECLUSE_VILLAGE[rnd17].getX();
                loc[1] = GETBACK_LOC_RECLUSE_VILLAGE[rnd17].getY();
                loc[2] = 400;
                break;
            default:
                int rnd18 = random.nextInt(GETBACK_LOC_SILVER_KNIGHT_TOWN.length);
                loc[0] = GETBACK_LOC_SILVER_KNIGHT_TOWN[rnd18].getX();
                loc[1] = GETBACK_LOC_SILVER_KNIGHT_TOWN[rnd18].getY();
                loc[2] = 4;
                break;
        }
        return loc;
    }

    public static int getTownTaxRateByNpcid(int npcid) {
        int town_id = getTownIdByNpcid(npcid);
        if (town_id < 1 || town_id > 10) {
            return 0;
        }
        return TownReading.get().getTownTable(town_id).get_tax_rate() + 2;
    }

    public static int getTownIdByNpcid(int npcid) {
        return TownSetTable.get().getTownid(npcid);
    }
}
