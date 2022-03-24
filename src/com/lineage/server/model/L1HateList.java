package com.lineage.server.model;

import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class L1HateList {
    private final Map<L1Character, Integer> _hateMap;

    private L1HateList(Map<L1Character, Integer> hateMap) {
        this._hateMap = hateMap;
    }

    public L1HateList() {
        this._hateMap = new HashMap();
    }

    public synchronized void add(L1Character cha, int hate) {
        if (cha != null) {
            Integer h = this._hateMap.get(cha);
            if (h != null) {
                this._hateMap.put(cha, Integer.valueOf(h.intValue() + hate));
            } else {
                this._hateMap.put(cha, Integer.valueOf(hate));
            }
        }
    }

    public synchronized boolean isHate(L1Character cha) {
        boolean z;
        if (this._hateMap.get(cha) != null) {
            z = true;
        } else {
            z = false;
        }
        return z;
    }

    public synchronized int get(L1Character cha) {
        return this._hateMap.get(cha).intValue();
    }

    public synchronized boolean containsKey(L1Character cha) {
        return this._hateMap.containsKey(cha);
    }

    public synchronized void remove(L1Character cha) {
        this._hateMap.remove(cha);
    }

    public synchronized void clear() {
        this._hateMap.clear();
    }

    public synchronized boolean isEmpty() {
        return this._hateMap.isEmpty();
    }

    public synchronized L1Character getMaxHateCharacter() {
        L1Character cha;
        cha = null;
        int hate = Integer.MIN_VALUE;
        for (Map.Entry<L1Character, Integer> e : this._hateMap.entrySet()) {
            if (hate < e.getValue().intValue()) {
                cha = e.getKey();
                hate = e.getValue().intValue();
            }
        }
        return cha;
    }

    public synchronized void removeInvalidCharacter(L1NpcInstance npc) {
        ArrayList<L1Character> invalidChars = new ArrayList<>();
        for (L1Character cha : this._hateMap.keySet()) {
            if (cha == null || cha.isDead() || !npc.knownsObject(cha)) {
                invalidChars.add(cha);
            }
        }
        Iterator<L1Character> it = invalidChars.iterator();
        while (it.hasNext()) {
            this._hateMap.remove(it.next());
        }
    }

    public synchronized int getTotalHate() {
        int totalHate;
        totalHate = 0;
        for (Integer num : this._hateMap.values()) {
            totalHate += num.intValue();
        }
        return totalHate;
    }

    public synchronized int getTotalLawfulHate() {
        int totalHate;
        totalHate = 0;
        for (Map.Entry<L1Character, Integer> e : this._hateMap.entrySet()) {
            if (e.getKey() instanceof L1PcInstance) {
                totalHate += e.getValue().intValue();
            }
        }
        return totalHate;
    }

    public synchronized int getPartyHate(L1Party party) {
        int partyHate;
        partyHate = 0;
        for (Map.Entry<L1Character, Integer> e : this._hateMap.entrySet()) {
            L1PcInstance pc = null;
            if (e.getKey() instanceof L1PcInstance) {
                pc = (L1PcInstance) e.getKey();
            }
            if (e.getKey() instanceof L1NpcInstance) {
                L1Character cha = ((L1NpcInstance) e.getKey()).getMaster();
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance) cha;
                }
            }
            if (pc != null && party.isMember(pc)) {
                partyHate += e.getValue().intValue();
            }
        }
        return partyHate;
    }

    public synchronized int getPartyLawfulHate(L1Party party) {
        int partyHate;
        partyHate = 0;
        for (Map.Entry<L1Character, Integer> e : this._hateMap.entrySet()) {
            L1PcInstance pc = null;
            if (e.getKey() instanceof L1PcInstance) {
                pc = (L1PcInstance) e.getKey();
            }
            if (pc != null && party.isMember(pc)) {
                partyHate += e.getValue().intValue();
            }
        }
        return partyHate;
    }

    public synchronized L1HateList copy() {
        return new L1HateList(new HashMap(this._hateMap));
    }

    public synchronized Set<Map.Entry<L1Character, Integer>> entrySet() {
        return this._hateMap.entrySet();
    }

    public synchronized ArrayList<L1Character> toTargetArrayList() {
        return new ArrayList<>(this._hateMap.keySet());
    }

    public synchronized ArrayList<Integer> toHateArrayList() {
        return new ArrayList<>(this._hateMap.values());
    }
}
