package com.lineage.data.event.gambling;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Gambling extends GamblingConfig {
    public int WIN;
    private long _adena;
    private final Map<Integer, GamblingNpc> _npcidMap = new HashMap();
    private GamblingNpc _onenpc;
    private Random _random = new Random();
    private long _upadena;

    public void clear() {
        this._npcidMap.clear();
        this._onenpc = null;
        this._adena = 0;
    }

    public long get_allAdena() {
        return this._adena;
    }

    public void set_gmaNpc(long previous) {
        if (GamblingConfig.ISGFX) {
            GamblingConfig.ISGFX = false;
        } else {
            GamblingConfig.ISGFX = true;
        }
        this.WIN = this._random.nextInt(5);
        int i = 0;
        while (this._npcidMap.size() < 5) {
            int npcid = NPCID[this._random.nextInt(NPCID.length)];
            if (this._npcidMap.get(new Integer(npcid)) == null) {
                GamblingNpc gamnpc = new GamblingNpc(this);
                gamnpc.showNpc(npcid, i);
                this._npcidMap.put(new Integer(npcid), gamnpc);
                i++;
            }
        }
        this._upadena = previous;
    }

    public long get_allRate() {
        long adena = 0;
        for (GamblingNpc gamblingNpc : this._npcidMap.values()) {
            adena += gamblingNpc.get_adena();
        }
        return this._upadena + adena;
    }

    public void set_allRate() {
        long adena = this._upadena;
        for (GamblingNpc gamblingNpc : this._npcidMap.values()) {
            adena += gamblingNpc.get_adena();
        }
        this._adena = adena;
        for (GamblingNpc gamblingNpc2 : this._npcidMap.values()) {
            set_npcRate(gamblingNpc2, adena);
        }
    }

    private void set_npcRate(GamblingNpc npc, long adena) {
        double chip = (double) npc.get_adena();
        if (adena != 0 && chip != 0.0d) {
            double rate = ((double) (1 * adena)) / chip;
            if (rate > 100.0d) {
                rate = 100.0d;
            }
            if (rate < 2.0d) {
                rate = 2.0d;
            }
            npc.set_rate(rate);
        }
    }

    public Map<Integer, GamblingNpc> get_allNpc() {
        return this._npcidMap;
    }

    public GamblingNpc get_oneNpc() {
        return this._onenpc;
    }

    public void set_oneNpc(GamblingNpc onenpc) {
        this._onenpc = onenpc;
    }

    public void startGam() {
        for (GamblingNpc gamblingNpc : this._npcidMap.values()) {
            gamblingNpc.getStart();
        }
    }

    public void delAllNpc() {
        for (GamblingNpc gamblingNpc : this._npcidMap.values()) {
            gamblingNpc.delNpc();
        }
    }
}
