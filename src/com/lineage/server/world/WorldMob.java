package com.lineage.server.world;

import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.map.L1Map;
import com.lineage.server.types.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class WorldMob {
    private static WorldMob _instance;
    private static final Log _log = LogFactory.getLog(WorldMob.class);
    private Collection<L1MonsterInstance> _allMobValues;
    private final ConcurrentHashMap<Integer, L1MonsterInstance> _isMob = new ConcurrentHashMap<>();

    public static WorldMob get() {
        if (_instance == null) {
            _instance = new WorldMob();
        }
        return _instance;
    }

    private WorldMob() {
    }

    public Collection<L1MonsterInstance> all() {
        try {
            Collection<L1MonsterInstance> vs = this._allMobValues;
            if (vs != null) {
                return vs;
            }
            Collection<L1MonsterInstance> vs2 = Collections.unmodifiableCollection(this._isMob.values());
            this._allMobValues = vs2;
            return vs2;
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    public ConcurrentHashMap<Integer, L1MonsterInstance> map() {
        return this._isMob;
    }

    public void put(Integer key, L1MonsterInstance value) {
        try {
            this._isMob.put(key, value);
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public void remove(Integer key) {
        try {
            this._isMob.remove(key);
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public ArrayList<L1MonsterInstance> getVisibleMob(L1MonsterInstance src) {
        L1Map map = src.getMap();
        Point pt = src.getLocation();
        ArrayList<L1MonsterInstance> result = new ArrayList<>();
        for (L1MonsterInstance element : all()) {
            if (!element.equals(src) && map == element.getMap() && pt.isInScreen(element.getLocation())) {
                result.add(element);
            }
        }
        return result;
    }

    public int getVisibleCount(L1MonsterInstance src) {
        L1Map map = src.getMap();
        Point pt = src.getLocation();
        int count = 0;
        for (L1MonsterInstance element : all()) {
            if (!element.equals(src) && map == element.getMap() && pt.isInScreen(element.getLocation()) && src.getNpcId() == element.getNpcId()) {
                count++;
            }
        }
        return count;
    }
}
