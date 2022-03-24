package com.lineage.server.model;

import com.lineage.server.IdFactoryNpc;
import com.lineage.server.datatables.NpcTable;
import com.lineage.server.datatables.UBTable;
import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCPack;
import com.lineage.server.world.World;
import java.util.Iterator;

public class L1UbSpawn implements Comparable<L1UbSpawn> {
    private int _amount;
    private int _group;
    private int _id;
    private String _name;
    private int _npcTemplateId;
    private int _pattern;
    private int _sealCount;
    private int _spawnDelay;
    private int _ubId;

    public int getId() {
        return this._id;
    }

    public void setId(int id) {
        this._id = id;
    }

    public int getUbId() {
        return this._ubId;
    }

    public void setUbId(int ubId) {
        this._ubId = ubId;
    }

    public int getPattern() {
        return this._pattern;
    }

    public void setPattern(int pattern) {
        this._pattern = pattern;
    }

    public int getGroup() {
        return this._group;
    }

    public void setGroup(int group) {
        this._group = group;
    }

    public int getNpcTemplateId() {
        return this._npcTemplateId;
    }

    public void setNpcTemplateId(int npcTemplateId) {
        this._npcTemplateId = npcTemplateId;
    }

    public int getAmount() {
        return this._amount;
    }

    public void setAmount(int amount) {
        this._amount = amount;
    }

    public int getSpawnDelay() {
        return this._spawnDelay;
    }

    public void setSpawnDelay(int spawnDelay) {
        this._spawnDelay = spawnDelay;
    }

    public int getSealCount() {
        return this._sealCount;
    }

    public void setSealCount(int i) {
        this._sealCount = i;
    }

    public String getName() {
        return this._name;
    }

    public void setName(String name) {
        this._name = name;
    }

    public void spawnOne() {
        boolean z = false;
        L1UltimateBattle ub = UBTable.getInstance().getUb(this._ubId);
        L1Location loc = ub.getLocation().randomLocation((ub.getLocX2() - ub.getLocX1()) / 2, false);
        L1MonsterInstance mob = new L1MonsterInstance(NpcTable.get().getTemplate(getNpcTemplateId()));
        mob.setId(IdFactoryNpc.get().nextId());
        mob.setHeading(5);
        mob.setX(loc.getX());
        mob.setHomeX(loc.getX());
        mob.setY(loc.getY());
        mob.setHomeY(loc.getY());
        mob.setMap((short) loc.getMapId());
        if (3 >= getGroup()) {
            z = true;
        }
        mob.set_storeDroped(z);
        mob.setUbSealCount(getSealCount());
        mob.setUbId(getUbId());
        World.get().storeObject(mob);
        World.get().addVisibleObject(mob);
        S_NPCPack s_npcPack = new S_NPCPack(mob);
        Iterator<L1PcInstance> it = World.get().getRecognizePlayer(mob).iterator();
        while (it.hasNext()) {
            L1PcInstance pc = it.next();
            pc.addKnownObject(mob);
            mob.addKnownObject(pc);
            pc.sendPackets(s_npcPack);
        }
        mob.onNpcAI();
        mob.turnOnOffLight();
    }

    public void spawnAll() {
        for (int i = 0; i < getAmount(); i++) {
            spawnOne();
        }
    }

    public int compareTo(L1UbSpawn rhs) {
        if (getId() < rhs.getId()) {
            return -1;
        }
        if (getId() > rhs.getId()) {
            return 1;
        }
        return 0;
    }
}
