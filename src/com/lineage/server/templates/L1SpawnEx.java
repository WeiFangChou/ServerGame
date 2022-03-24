package com.lineage.server.templates;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Random;

public abstract class L1SpawnEx implements Serializable {
    protected static final Random _random = new Random();
    private static final long serialVersionUID = -755041639984207871L;
    protected int _existTime = 0;
    protected int _groupId;
    protected int _heading;
    protected int _id;
    protected int _locx1;
    protected int _locx2;
    protected int _locy1;
    protected int _locy2;
    protected short _mapid;
    protected int _maximumCount;
    protected Timestamp _nextSpawnTime = null;
    protected int _npcid;
    protected int _spawnInterval = 0;
    protected L1Npc _template;
    protected int _tmplocx;
    protected int _tmplocy;
    protected short _tmpmapid;

    public abstract void doSpawn(int i);

    public abstract int get_existTime();

    public abstract int get_groupId();

    public abstract int get_heading();

    public abstract int get_id();

    public abstract int get_locx1();

    public abstract int get_locx2();

    public abstract int get_locy1();

    public abstract int get_locy2();

    public abstract short get_mapid();

    public abstract int get_maximumCount();

    public abstract Timestamp get_nextSpawnTime();

    public abstract int get_npcid();

    public abstract long get_spawnInterval();

    public abstract L1Npc get_template();

    public abstract int get_tmplocx();

    public abstract int get_tmplocy();

    public abstract short get_tmpmapid();

    public abstract void set_existTime(int i);

    public abstract void set_groupId(int i);

    public abstract void set_heading(int i);

    public abstract void set_id(int i);

    public abstract void set_locx1(int i);

    public abstract void set_locx2(int i);

    public abstract void set_locy1(int i);

    public abstract void set_locy2(int i);

    public abstract void set_mapid(short s);

    public abstract void set_maximumCount(int i);

    public abstract void set_nextSpawnTime(Timestamp timestamp);

    public abstract void set_npcid(int i);

    public abstract void set_spawnInterval(int i);

    public abstract void set_tmplocx(int i);

    public abstract void set_tmplocy(int i);

    public abstract void set_tmpmapid(short s);
}
