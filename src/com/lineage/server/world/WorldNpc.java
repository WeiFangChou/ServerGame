package com.lineage.server.world;

import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.map.L1Map;
import com.lineage.server.types.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class WorldNpc {
    private static WorldNpc _instance;
    private static final Log _log = LogFactory.getLog(WorldNpc.class);
    private Collection<L1NpcInstance> _allMobValues;
    private final ConcurrentHashMap<Integer, L1NpcInstance> _isMob = new ConcurrentHashMap<>();

    public static WorldNpc get() {
        if (_instance == null) {
            _instance = new WorldNpc();
        }
        return _instance;
    }

    private WorldNpc() {
    }

    public Collection<L1NpcInstance> all() {
        try {
            Collection<L1NpcInstance> vs = this._allMobValues;
            if (vs != null) {
                return vs;
            }
            Collection<L1NpcInstance> vs2 = Collections.unmodifiableCollection(this._isMob.values());
            this._allMobValues = vs2;
            return vs2;
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    public ConcurrentHashMap<Integer, L1NpcInstance> map() {
        return this._isMob;
    }

    public void put(Integer key, L1NpcInstance value) {
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

    public ArrayList<L1NpcInstance> getVisibleMob(L1NpcInstance src) {
        L1Map map = src.getMap();
        Point pt = src.getLocation();
        ArrayList<L1NpcInstance> result = new ArrayList<>();
        for (L1NpcInstance element : all()) {
            if (!element.equals(src) && map == element.getMap() && pt.isInScreen(element.getLocation())) {
                result.add(element);
            }
        }
        return result;
    }

    public int getVisibleCount(L1NpcInstance src) {
        L1Map map = src.getMap();
        Point pt = src.getLocation();
        int count = 0;
        for (L1NpcInstance element : all()) {
            if (!element.equals(src) && map == element.getMap() && pt.isInScreen(element.getLocation()) && src.getNpcId() == element.getNpcId()) {
                count++;
            }
        }
        return count;
    }
}
