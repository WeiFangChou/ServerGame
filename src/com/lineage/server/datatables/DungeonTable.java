package com.lineage.server.datatables;

import com.lineage.DatabaseFactory;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.gametime.L1GameTimeClock;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_Teleport2;
import com.lineage.server.templates.L1Inn;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DungeonTable {
    private static final Log _log = LogFactory.getLog(DungeonTable.class);
    private static DungeonTable _instance = null;
    private static Map<String, DungeonTable.NewDungeon> _dungeonMap = new HashMap();
    private static AtomicInteger _nextId = new AtomicInteger(50000);

    public DungeonTable() {
    }

    public static DungeonTable get() {
        if (_instance == null) {
            _instance = new DungeonTable();
        }

        return _instance;
    }

    public void load() {
        PerformanceTimer timer = new PerformanceTimer();
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("SELECT * FROM `dungeon`");

            String key;
            DungeonTable.NewDungeon newDungeon;
            for(rs = ps.executeQuery(); rs.next(); _dungeonMap.put(key, newDungeon)) {
                int srcMapId = rs.getInt("src_mapid");
                int srcX = rs.getInt("src_x");
                int srcY = rs.getInt("src_y");
                key = "" + srcMapId + srcX + srcY;
                int newX = rs.getInt("new_x");
                int newY = rs.getInt("new_y");
                int newMapId = rs.getInt("new_mapid");
                int heading = rs.getInt("new_heading");
                int no = rs.getInt("是否開放");
                DungeonTable.DungeonType dungeonType = DungeonTable.DungeonType.NONE;
                if ((srcX == 33423 || srcX == 33424 || srcX == 33425 || srcX == 33426) && srcY == 33502 && srcMapId == 4 || (srcX == 32733 || srcX == 32734 || srcX == 32735 || srcX == 32736) && srcY == 32794 && srcMapId == 83) {
                    dungeonType = DungeonTable.DungeonType.SHIP_FOR_FI;
                } else if ((srcX != 32935 && srcX != 32936 && srcX != 32937 || srcY != 33058 || srcMapId != 70) && (srcX != 32732 && srcX != 32733 && srcX != 32734 && srcX != 32735 || srcY != 32796 || srcMapId != 84)) {
                    if ((srcX != 32750 && srcX != 32751 && srcX != 32752 || srcY != 32874 || srcMapId != 445) && (srcX != 32731 && srcX != 32732 && srcX != 32733 || srcY != 32796 || srcMapId != 447)) {
                        if ((srcX != 32296 && srcX != 32297 && srcX != 32298 || srcY != 33086 || srcMapId != 440) && (srcX != 32735 && srcX != 32736 && srcX != 32737 || srcY != 32795 || srcMapId != 446)) {
                            if ((srcX == 32630 || srcX == 32631 || srcX == 32632) && srcY == 32983 && srcMapId == 0 || (srcX == 32733 || srcX == 32734 || srcX == 32735) && srcY == 32796 && srcMapId == 5) {
                                dungeonType = DungeonTable.DungeonType.SHIP_FOR_GLUDIN;
                            } else if ((srcX != 32540 && srcX != 32542 && srcX != 32543 && srcX != 32544 && srcX != 32545 || srcY != 32728 || srcMapId != 4) && (srcX != 32734 && srcX != 32735 && srcX != 32736 && srcX != 32737 || srcY != 32794 || srcMapId != 6)) {
                                if (srcX == 32600 && srcY == 32931 && srcMapId == 0) {
                                    dungeonType = DungeonTable.DungeonType.TALKING_ISLAND_HOTEL;
                                } else if (srcX == 32632 && srcY == 32761 && srcMapId == 4) {
                                    dungeonType = DungeonTable.DungeonType.GLUDIO_HOTEL;
                                } else if (srcX == 33116 && srcY == 33379 && srcMapId == 4) {
                                    dungeonType = DungeonTable.DungeonType.SILVER_KNIGHT_HOTEL;
                                } else if (srcX == 32628 && srcY == 33167 && srcMapId == 4) {
                                    dungeonType = DungeonTable.DungeonType.WINDAWOOD_HOTEL;
                                } else if (srcX == 33605 && srcY == 33275 && srcMapId == 4) {
                                    dungeonType = DungeonTable.DungeonType.HEINE_HOTEL;
                                } else if (srcX == 33437 && srcY == 32789 && srcMapId == 4) {
                                    dungeonType = DungeonTable.DungeonType.GIRAN_HOTEL;
                                } else if (srcX == 34068 && srcY == 32254 && srcMapId == 4) {
                                    dungeonType = DungeonTable.DungeonType.OREN_HOTEL;
                                } else if (srcX == 33985 && srcY == 33312 && srcMapId == 4) {
                                    dungeonType = DungeonTable.DungeonType.ADEN_HOTEL;
                                } else if (srcX == 32450 && srcY == 33047 && srcMapId == 440) {
                                    dungeonType = DungeonTable.DungeonType.PIRATE_HOTEL;
                                }
                            } else {
                                dungeonType = DungeonTable.DungeonType.SHIP_FOR_TI;
                            }
                        } else {
                            dungeonType = DungeonTable.DungeonType.SHIP_FOR_HIDDENDOCK;
                        }
                    } else {
                        dungeonType = DungeonTable.DungeonType.SHIP_FOR_PI;
                    }
                } else {
                    dungeonType = DungeonTable.DungeonType.SHIP_FOR_HEINE;
                }

                newDungeon = new DungeonTable.NewDungeon(newX, newY, (short)newMapId, heading, no, dungeonType, (DungeonTable.NewDungeon)null);
                if (_dungeonMap.containsKey(key)) {
                    _log.error("相同SRC傳送座標(" + key + ")");
                }
            }
        } catch (SQLException var19) {
            _log.error(var19.getLocalizedMessage(), var19);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }

        _log.info("載入地圖切換點設置數量: " + _dungeonMap.size() + "(" + timer.get() + "ms)");
    }

    public boolean dg(int locX, int locY, int mapId, L1PcInstance pc) throws Exception {
        int servertime = L1GameTimeClock.getInstance().currentTime().getSeconds();
        int nowtime = servertime % 86400;
        String key = "" + mapId + locX + locY;
        if (_dungeonMap.containsKey(key)) {
            DungeonTable.NewDungeon newDungeon = (DungeonTable.NewDungeon)_dungeonMap.get(key);
            DungeonTable.DungeonType dungeonType = newDungeon._dungeonType;
            boolean teleportable = false;
            int npcid;
            int type;
            if (dungeonType == DungeonTable.DungeonType.NONE) {
                teleportable = true;
            } else if (dungeonType != DungeonTable.DungeonType.TALKING_ISLAND_HOTEL && dungeonType != DungeonTable.DungeonType.GLUDIO_HOTEL && dungeonType != DungeonTable.DungeonType.WINDAWOOD_HOTEL && dungeonType != DungeonTable.DungeonType.SILVER_KNIGHT_HOTEL && dungeonType != DungeonTable.DungeonType.HEINE_HOTEL && dungeonType != DungeonTable.DungeonType.GIRAN_HOTEL && dungeonType != DungeonTable.DungeonType.OREN_HOTEL && dungeonType != DungeonTable.DungeonType.ADEN_HOTEL && dungeonType != DungeonTable.DungeonType.PIRATE_HOTEL) {
                if (nowtime >= 5400 && nowtime < 9000 || nowtime >= 16200 && nowtime < 19800 || nowtime >= 27000 && nowtime < 30600 || nowtime >= 37800 && nowtime < 41400 || nowtime >= 48600 && nowtime < 52200 || nowtime >= 59400 && nowtime < 63000 || nowtime >= 70200 && nowtime < 73800 || nowtime >= 81000 && nowtime < 84600) {
                    if (pc.getInventory().checkItem(40299, 1L) && dungeonType == DungeonTable.DungeonType.SHIP_FOR_GLUDIN || pc.getInventory().checkItem(40301, 1L) && dungeonType == DungeonTable.DungeonType.SHIP_FOR_HEINE || pc.getInventory().checkItem(40302, 1L) && dungeonType == DungeonTable.DungeonType.SHIP_FOR_PI) {
                        teleportable = true;
                    }
                } else if ((nowtime >= 0 && nowtime < 3600 || nowtime >= 10800 && nowtime < 14400 || nowtime >= 21600 && nowtime < 25200 || nowtime >= 32400 && nowtime < 36000 || nowtime >= 43200 && nowtime < 46800 || nowtime >= 54000 && nowtime < 57600 || nowtime >= 64800 && nowtime < 68400 || nowtime >= 75600 && nowtime < 79200) && (pc.getInventory().checkItem(40298, 1L) && dungeonType == DungeonTable.DungeonType.SHIP_FOR_TI || pc.getInventory().checkItem(40300, 1L) && dungeonType == DungeonTable.DungeonType.SHIP_FOR_FI || pc.getInventory().checkItem(40303, 1L) && dungeonType == DungeonTable.DungeonType.SHIP_FOR_HIDDENDOCK)) {
                    teleportable = true;
                }
            } else {
                npcid = 0;
                int[] data = null;
                if (dungeonType == DungeonTable.DungeonType.TALKING_ISLAND_HOTEL) {
                    npcid = 70012;
                    data = new int[]{32745, 32803, 16384, 32743, 32808, 16896};
                } else if (dungeonType == DungeonTable.DungeonType.GLUDIO_HOTEL) {
                    npcid = 70019;
                    data = new int[]{32743, 32803, 17408, 32744, 32807, 17920};
                } else if (dungeonType == DungeonTable.DungeonType.GIRAN_HOTEL) {
                    npcid = 70031;
                    data = new int[]{32744, 32803, 18432, 32744, 32807, 18944};
                } else if (dungeonType == DungeonTable.DungeonType.OREN_HOTEL) {
                    npcid = 70065;
                    data = new int[]{32744, 32803, 19456, 32744, 32807, 19968};
                } else if (dungeonType == DungeonTable.DungeonType.WINDAWOOD_HOTEL) {
                    npcid = 70070;
                    data = new int[]{32744, 32803, 20480, 32744, 32807, 20992};
                } else if (dungeonType == DungeonTable.DungeonType.SILVER_KNIGHT_HOTEL) {
                    npcid = 70075;
                    data = new int[]{32744, 32803, 21504, 32744, 32807, 22016};
                } else if (dungeonType == DungeonTable.DungeonType.HEINE_HOTEL) {
                    npcid = 70084;
                    data = new int[]{32744, 32803, 22528, 32744, 32807, 23040};
                } else if (dungeonType == DungeonTable.DungeonType.ADEN_HOTEL) {
                    npcid = 70054;
                    data = new int[]{32744, 32803, 23552, 32744, 32807, 24064};
                } else if (dungeonType == DungeonTable.DungeonType.PIRATE_HOTEL) {
                    npcid = 70096;
                    data = new int[]{32744, 32803, 24576, 32744, 32807, 25088};
                }

                type = this.checkInnKey(pc, npcid);
                if (type == 1) {
                    locX = data[0];
                    locY = data[1];
                    mapId = data[2];
                    teleportable = true;
                } else if (type == 2) {
                    locX = data[3];
                    locY = data[4];
                    mapId = data[5];
                    teleportable = true;
                }
            }

            if (teleportable) {
                npcid = newDungeon._id;
                short newMap = newDungeon._newMapId;
                type = newDungeon._newX;
                int newY = newDungeon._newY;
                int heading = newDungeon._heading;
                int No = newDungeon._no;
                pc.setSkillEffect(78, 2000);
                pc.stopHpRegeneration();
                pc.stopMpRegeneration();
                if (No == 0) {
                    pc.sendPackets(new S_ServerMessage("地圖未開放。"));
                    return false;
                }

                this.teleport(pc, npcid, type, newY, newMap, heading, false);
                return true;
            }
        }

        return false;
    }

    private int checkInnKey(L1PcInstance pc, int npcid) throws Exception {
        Iterator var4 = pc.getInventory().getItems().iterator();

        while(true) {
            L1ItemInstance item;
            do {
                if (!var4.hasNext()) {
                    return 0;
                }

                item = (L1ItemInstance)var4.next();
            } while(item.getInnNpcId() != npcid);

            for(int i = 0; i < 16; ++i) {
                L1Inn inn = InnTable.getInstance().getTemplate(npcid, i);
                if (inn.getKeyId() == item.getKeyId()) {
                    Timestamp dueTime = item.getDueTime();
                    if (dueTime != null) {
                        Calendar cal = Calendar.getInstance();
                        if ((cal.getTimeInMillis() - dueTime.getTime()) / 1000L < 0L) {
                            pc.setInnKeyId(item.getKeyId());
                            return item.checkRoomOrHall() ? 2 : 1;
                        }
                    }
                }
            }
        }
    }

    private void teleport(L1PcInstance pc, int id, int newX, int newY, short newMap, int heading, boolean b) {
        pc.setTeleportX(newX);
        pc.setTeleportY(newY);
        pc.setTeleportMapId(newMap);
        pc.setTeleportHeading(heading);
        pc.sendPackets(new S_Teleport2(newMap, id));
    }

    private static enum DungeonType {
        NONE,
        SHIP_FOR_FI,
        SHIP_FOR_HEINE,
        SHIP_FOR_PI,
        SHIP_FOR_HIDDENDOCK,
        SHIP_FOR_GLUDIN,
        SHIP_FOR_TI,
        TALKING_ISLAND_HOTEL,
        GLUDIO_HOTEL,
        SILVER_KNIGHT_HOTEL,
        WINDAWOOD_HOTEL,
        HEINE_HOTEL,
        GIRAN_HOTEL,
        OREN_HOTEL,
        ADEN_HOTEL,
        PIRATE_HOTEL;

        private DungeonType() {
        }
    }

    private static class NewDungeon {
        int _id;
        int _newX;
        int _newY;
        short _newMapId;
        int _heading;
        int _no;
        DungeonTable.DungeonType _dungeonType;

        private NewDungeon(int newX, int newY, short newMapId, int heading, int no, DungeonType dungeonType, NewDungeon newDungeon) {
            this._id = DungeonTable._nextId.incrementAndGet();
            this._newX = newX;
            this._newY = newY;
            this._newMapId = newMapId;
            this._heading = heading;
            this._no = no;
            this._dungeonType = dungeonType;
        }
    }
}
