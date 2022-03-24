package com.lineage.server.world;

import com.lineage.server.model.Instance.L1PcInstance;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class WorldCrown {
    private static WorldCrown _instance;
    private static final Log _log = LogFactory.getLog(WorldCrown.class);
    private Collection<L1PcInstance> _allPlayer;
    private final ConcurrentHashMap<Integer, L1PcInstance> _isCrown = new ConcurrentHashMap<>();

    public static WorldCrown get() {
        if (_instance == null) {
            _instance = new WorldCrown();
        }
        return _instance;
    }

    private WorldCrown() {
    }

    public Collection<L1PcInstance> all() {
        try {
            Collection<L1PcInstance> vs = this._allPlayer;
            if (vs != null) {
                return vs;
            }
            Collection<L1PcInstance> vs2 = Collections.unmodifiableCollection(this._isCrown.values());
            this._allPlayer = vs2;
            return vs2;
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    public ConcurrentHashMap<Integer, L1PcInstance> map() {
        return this._isCrown;
    }

    public void put(Integer key, L1PcInstance value) {
        try {
            this._isCrown.put(key, value);
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public void remove(Integer key) {
        try {
            this._isCrown.remove(key);
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
