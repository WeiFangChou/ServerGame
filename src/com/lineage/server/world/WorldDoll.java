package com.lineage.server.world;

import com.lineage.server.model.Instance.L1DollInstance;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class WorldDoll {
    private static WorldDoll _instance;
    private static final Log _log = LogFactory.getLog(WorldDoll.class);
    private Collection<L1DollInstance> _allDollValues;
    private final ConcurrentHashMap<Integer, L1DollInstance> _isDoll = new ConcurrentHashMap<>();

    public static WorldDoll get() {
        if (_instance == null) {
            _instance = new WorldDoll();
        }
        return _instance;
    }

    private WorldDoll() {
    }

    public Collection<L1DollInstance> all() {
        try {
            Collection<L1DollInstance> vs = this._allDollValues;
            if (vs != null) {
                return vs;
            }
            Collection<L1DollInstance> vs2 = Collections.unmodifiableCollection(this._isDoll.values());
            this._allDollValues = vs2;
            return vs2;
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    public ConcurrentHashMap<Integer, L1DollInstance> map() {
        return this._isDoll;
    }

    public L1DollInstance get(Integer key) {
        try {
            return this._isDoll.get(key);
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    public void put(Integer key, L1DollInstance value) {
        try {
            this._isDoll.put(key, value);
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public void remove(Integer key) {
        try {
            this._isDoll.remove(key);
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
