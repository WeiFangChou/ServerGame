package com.lineage.server.templates;

import com.lineage.data.executor.QuestMobExecutor;
import com.lineage.server.datatables.NpcTable;
import com.lineage.server.datatables.QuesttSpawnTable;
import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Location;
import com.lineage.server.model.L1MobGroupSpawn;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.map.L1Map;
import com.lineage.server.model.map.L1WorldMap;
import com.lineage.server.serverpackets.S_HelpMessage;
import com.lineage.server.serverpackets.ServerBasePacket;
import com.lineage.server.utils.L1SpawnUtil;
import com.lineage.server.utils.ListMapUtil;
import com.lineage.server.utils.PerformanceTimer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1QuestUser {
    private static final Log _log = LogFactory.getLog(L1QuestUser.class);
    private final int _id;
    private boolean _info = true;
    private final int _mapid;
    private QuestMobExecutor _mobNull = null;
    private final ArrayList<L1NpcInstance> _npcList;
    private boolean _outStop = false;
    private final int _questid;
    private Random _random = new Random();
    private int _time = -1;
    private final ArrayList<L1PcInstance> _userList;

    public L1QuestUser(int id, int mapid, int questid) {
        this._id = id;
        this._mapid =  mapid;
        this._questid = questid;
        this._userList = new ArrayList<>();
        this._npcList = new ArrayList<>();
    }

    public int get_id() {
        return this._id;
    }

    public int get_questid() {
        return this._questid;
    }

    public int get_mapid() {
        return this._mapid;
    }

    public void add(L1PcInstance pc) {
        try {
            if (!this._userList.contains(pc)) {
                this._userList.add(pc);
                pc.set_showId(this._id);
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            _log.info("加入副本執行成員(" + this._questid + "-" + this._id + "):" + pc.getName());
        }
    }

    public void remove(L1PcInstance pc) {
        try {
            if (this._userList.remove(pc)) {
                pc.set_showId(-1);
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            _log.info("移出副本執行成員(" + this._questid + "-" + this._id + "):" + pc.getName());
        }
    }

    public void set_time(int time) {
        this._time = time;
    }

    public int get_time() {
        return this._time;
    }

    public boolean is_time() {
        return this._time != -1;
    }

    public ArrayList<L1PcInstance> pcList() {
        return this._userList;
    }

    public int size() {
        return this._userList.size();
    }

    public List<L1NpcInstance> npcList() {
        return this._npcList;
    }

    public void addNpc(L1NpcInstance npc) {
        this._npcList.add(npc);
    }

    public ArrayList<L1NpcInstance> npcList(int npcid) {
        ArrayList<L1NpcInstance> npcList = new ArrayList<>();
        Iterator<L1NpcInstance> it = this._npcList.iterator();
        while (it.hasNext()) {
            L1NpcInstance npc = it.next();
            if (npc.getNpcId() == npcid && !npc.isDead()) {
                npcList.add(npc);
            }
        }
        if (npcList.size() <= 0) {
            return null;
        }
        return npcList;
    }

    public int npcSize() {
        return this._npcList.size();
    }

    public int mobSize() {
        int i = 0;
        Iterator<L1NpcInstance> it = this._npcList.iterator();
        while (it.hasNext()) {
            if (it.next() instanceof L1MonsterInstance) {
                i++;
            }
        }
        return i;
    }

    public void spawnQuestMob() {
        int count;
        PerformanceTimer timer = new PerformanceTimer();
        try {
            ArrayList<L1QuestMobSpawn> spawnList = QuesttSpawnTable.get().getMobSpawn(this._questid);
            if (spawnList.size() > 0) {
                Iterator<L1QuestMobSpawn> it = spawnList.iterator();
                while (it.hasNext()) {
                    L1QuestMobSpawn mobSpawn = it.next();
                    if (mobSpawn.get_mapid() == this._mapid && (count = mobSpawn.get_count()) > 0) {
                        for (int i = 0; i < count; i++) {
                            spawn(mobSpawn);
                        }
                    }
                }
            }
            spawnList.clear();
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            _log.info("副本任務啟動(" + this._questid + "-" + this._id + ") NPC完成召喚 數量:" + this._npcList.size() + "(" + timer.get() + "ms)");
        }
    }

    private void spawn(L1QuestMobSpawn mobSpawn) {
        try {
            int npcid = mobSpawn.get_npc_templateid();
            int group_id = mobSpawn.get_group_id();
            int locx1 = mobSpawn.get_locx1();
            int locy1 = mobSpawn.get_locy1();
            int locx2 = mobSpawn.get_locx2();
            int locy2 = mobSpawn.get_locy2();
            int heading = mobSpawn.get_heading();
            int mapid = mobSpawn.get_mapid();
            if (NpcTable.get().getTemplate(npcid) == null) {
                _log.error("召喚NPC編號: " + npcid + " 不存在資料庫中!");
            } else if (locx1 == 0 || locy1 == 0 || locx2 == 0 || locy2 == 0) {
                L1NpcInstance mob = L1SpawnUtil.spawn(npcid, new L1Location(locx1, locy1, mapid), heading, this._id);
                this._npcList.add(mob);
                groupSpawn(group_id, mob);
            } else {
                L1Map map = L1WorldMap.get().getMap( mapid);
                for (int tryCount = 0; tryCount <= 50; tryCount++) {
                    int x = this._random.nextInt(locx2 - locx1) + locx1;
                    int y = this._random.nextInt(locy2 - locy1) + locy1;
                    if (map.isInMap(x, y) && map.isPassable(x, y, (L1Character) null)) {
                        L1NpcInstance mob2 = L1SpawnUtil.spawn(npcid, new L1Location(x, y, mapid), heading, this._id);
                        this._npcList.add(mob2);
                        groupSpawn(group_id, mob2);
                        return;
                    }
                }
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private void groupSpawn(int group_id, L1NpcInstance mob) {
        if (group_id != 0) {
            L1MobGroupSpawn.getInstance().doSpawn(mob, group_id, true, true);
        }
        if (mob.getMobGroupInfo() != null) {
            for (L1NpcInstance mobx : mob.getMobGroupInfo().getList()) {
                if (!mobx.equals(mob)) {
                    this._npcList.add(mobx);
                }
            }
        }
    }

    public void removeMob(L1NpcInstance mob) {
        try {
            if (this._npcList.remove(mob) && is_info()) {
                sendPackets(new S_HelpMessage("\\fY剩餘怪物：" + mobSize()));
            }
            if (mobSize() <= 0 && this._mobNull != null) {
                this._mobNull.stopQuest(this);
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public void removeMob() {
        try {
            ArrayList<L1NpcInstance> allList = new ArrayList<>();
            allList.addAll(this._npcList);
            Iterator<L1NpcInstance> it = allList.iterator();
            while (it.hasNext()) {
                it.next().deleteMe();
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            ListMapUtil.clear((ArrayList<?>) this._npcList);
            _log.info("副本任務結束(" + this._questid + "-" + this._id + ")");
        }
    }

    public void endQuest() {
        try {
            Iterator<L1PcInstance> it = this._userList.iterator();
            while (it.hasNext()) {
                L1PcInstance pc = it.next();
                if (pc.getMapId() == this._mapid) {
                    L1Teleport.teleport(pc, 33430, 32814,  4, 4, true);
                }
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            ListMapUtil.clear((ArrayList<?>) this._userList);
        }
    }

    public void sendPackets(ServerBasePacket basePacket) {
        try {
            Iterator<L1PcInstance> it = this._userList.iterator();
            while (it.hasNext()) {
                it.next().sendPackets(basePacket);
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public void set_info(boolean _info2) {
        this._info = _info2;
    }

    public boolean is_info() {
        return this._info;
    }

    public void set_outStop(boolean _outStop2) {
        this._outStop = _outStop2;
    }

    public boolean is_outStop() {
        return this._outStop;
    }

    public void set_object(QuestMobExecutor mobNull) {
        this._mobNull = mobNull;
    }

    public QuestMobExecutor get_object() {
        return this._mobNull;
    }
}
