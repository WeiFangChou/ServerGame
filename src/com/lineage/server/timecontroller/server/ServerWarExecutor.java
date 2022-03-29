package com.lineage.server.timecontroller.server;

import com.lineage.config.Config;
import com.lineage.config.ConfigAlt;
import com.lineage.server.datatables.CastleWarGiftTable;
import com.lineage.server.datatables.DoorSpawnTable;
import com.lineage.server.datatables.lock.CastleReading;
import com.lineage.server.datatables.lock.CharOtherReading;
import com.lineage.server.model.Instance.L1CrownInstance;
import com.lineage.server.model.Instance.L1DoorInstance;
import com.lineage.server.model.Instance.L1FieldObjectInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1TowerInstance;
import com.lineage.server.model.L1CastleLocation;
import com.lineage.server.model.L1Clan;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.L1War;
import com.lineage.server.model.L1WarSpawn;
import com.lineage.server.serverpackets.S_PacketBoxWar;
import com.lineage.server.templates.L1Castle;
import com.lineage.server.world.World;
import com.lineage.server.world.WorldClan;
import com.lineage.server.world.WorldWar;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ServerWarExecutor {
    private static ServerWarExecutor _instance;
    private static final Log _log = LogFactory.getLog(ServerWarExecutor.class);
    private String[] _castleName = {"肯特", "妖魔", "風木", "奇岩", "海音", "侏儒", "亞丁", "狄亞得要塞"};
    private boolean[] _is_now_war = new boolean[8];
    private L1Castle[] _l1castle = new L1Castle[8];
    private Calendar[] _war_end_time = new Calendar[8];
    private Calendar[] _war_start_time = new Calendar[8];

    private ServerWarExecutor() {
        for (int i = 0; i < this._l1castle.length; i++) {
            this._l1castle[i] = CastleReading.get().getCastleTable(i + 1);
            this._war_start_time[i] = this._l1castle[i].getWarTime();
            this._war_end_time[i] = (Calendar) this._l1castle[i].getWarTime().clone();
            this._war_end_time[i].add(ConfigAlt.ALT_WAR_TIME_UNIT, ConfigAlt.ALT_WAR_TIME);
        }
    }

    public static ServerWarExecutor get() {
        if (_instance == null) {
            _instance = new ServerWarExecutor();
        }
        return _instance;
    }

    public Calendar getRealTime() {
        return Calendar.getInstance(TimeZone.getTimeZone(Config.TIME_ZONE));
    }

    public boolean isNowWar(int castle_id) {
        return this._is_now_war[castle_id - 1];
    }

    public void setWarTime(int castle_id, Calendar calendar) {
        this._war_start_time[castle_id - 1] = (Calendar) calendar.clone();
    }

    public void setEndWarTime(int castle_id, Calendar calendar) {
        this._war_end_time[castle_id - 1] = (Calendar) calendar.clone();
        this._war_end_time[castle_id - 1].add(ConfigAlt.ALT_WAR_TIME_UNIT, ConfigAlt.ALT_WAR_TIME);
    }

    public void checkCastleWar(L1PcInstance player) {
        for (int i = 0; i < 8; i++) {
            if (this._is_now_war[i]) {
                player.sendPackets(new S_PacketBoxWar(2, i + 1));
            }
        }
    }

    public int checkCastleWar() {
        int x = 0;
        for (int i = 0; i < 8; i++) {
            if (this._is_now_war[i]) {
                x++;
            }
        }
        return x;
    }

    /* access modifiers changed from: protected */
    public void checkWarTime() {
        for (int i = 0; i < 8; i++) {
            try {
                Calendar now = getRealTime();
                if (!this._war_start_time[i].before(now) || !this._war_end_time[i].after(now)) {
                    if (this._war_end_time[i].before(now) && this._is_now_war[i]) {
                        this._is_now_war[i] = false;
                        World.get().broadcastPacketToAll(new S_PacketBoxWar(1, i + 1));
                        this._war_start_time[i].add(ConfigAlt.ALT_WAR_INTERVAL_UNIT, ConfigAlt.ALT_WAR_INTERVAL);
                        this._war_end_time[i].add(ConfigAlt.ALT_WAR_INTERVAL_UNIT, ConfigAlt.ALT_WAR_INTERVAL);
                        this._l1castle[i].setTaxRate(10);
                        this._l1castle[i].setPublicMoney(0);
                        CastleReading.get().updateCastle(this._l1castle[i]);
                        int castle_id = i + 1;
                        for (L1War war : WorldWar.get().getWarList()) {
                            if (war.get_castleId() == castle_id) {
                                war.ceaseCastleWar();
                            }
                        }
                        for (L1Object l1object : World.get().getObject()) {
                            if (l1object instanceof L1FieldObjectInstance) {
                                L1FieldObjectInstance flag = (L1FieldObjectInstance) l1object;
                                if (L1CastleLocation.checkInWarArea(castle_id, flag)) {
                                    flag.deleteMe();
                                }
                            }
                            if (l1object instanceof L1CrownInstance) {
                                L1CrownInstance crown = (L1CrownInstance) l1object;
                                if (L1CastleLocation.checkInWarArea(castle_id, crown)) {
                                    crown.deleteMe();
                                }
                            }
                            if (l1object instanceof L1TowerInstance) {
                                L1TowerInstance tower = (L1TowerInstance) l1object;
                                if (L1CastleLocation.checkInWarArea(castle_id, tower)) {
                                    tower.deleteMe();
                                }
                            }
                        }
                        new L1WarSpawn().spawnTower(castle_id);
                        L1DoorInstance[] doorList = DoorSpawnTable.get().getDoorList();
                        for (L1DoorInstance door : doorList) {
                            if (L1CastleLocation.checkInWarArea(castle_id, door)) {
                                door.repairGate();
                            }
                        }
                        World.get().broadcastPacketToAll(new S_PacketBoxWar());
                        _log.info(String.valueOf(this._castleName[i]) + " 的攻城戰結束。時間: " + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(now.getTime()));
                        CastleWarGiftTable.get().get_gift(castle_id);
                    }
                } else if (!this._is_now_war[i]) {
                    this._is_now_war[i] = true;
                    CharOtherReading.get().tam();
                    new L1WarSpawn().SpawnFlag(i + 1);
                    L1DoorInstance[] doorList2 = DoorSpawnTable.get().getDoorList();
                    for (L1DoorInstance door2 : doorList2) {
                        if (L1CastleLocation.checkInWarArea(i + 1, door2)) {
                            door2.repairGate();
                        }
                    }
                    _log.info(String.valueOf(this._castleName[i]) + " 的攻城戰開始。時間: " + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(this._war_start_time[i].getTime()));
                    World.get().broadcastPacketToAll(new S_PacketBoxWar(0, i + 1));
                    int[] iArr = new int[3];
                    for (L1PcInstance pc : World.get().getAllPlayers()) {
                        int castleId = i + 1;
                        if (L1CastleLocation.checkInWarArea(castleId, pc) && !pc.isGm()) {
                            L1Clan clan = WorldClan.get().getClan(pc.getClanname());
                            if (clan == null || clan.getCastleId() != castleId) {
                                int[] loc = L1CastleLocation.getGetBackLoc(castleId);
                                L1Teleport.teleport(pc, loc[0], loc[1],  loc[2], 5, true);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                _log.error(e.getLocalizedMessage(), e);
                return;
            }
        }
    }
}
