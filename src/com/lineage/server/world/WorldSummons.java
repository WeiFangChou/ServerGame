package com.lineage.server.world;

import com.lineage.server.model.Instance.L1SummonInstance;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class WorldSummons {
    private static WorldSummons _instance;
    private static final Log _log = LogFactory.getLog(WorldSummons.class);
    private Collection<L1SummonInstance> _allSummonValues;
    private final ConcurrentHashMap<Integer, L1SummonInstance> _isSummons = new ConcurrentHashMap<>();

    public static WorldSummons get() {
        if (_instance == null) {
            _instance = new WorldSummons();
        }
        return _instance;
    }

    private WorldSummons() {
    }

    public Collection<L1SummonInstance> all() {
        try {
            Collection<L1SummonInstance> vs = this._allSummonValues;
            if (vs != null) {
                return vs;
            }
            Collection<L1SummonInstance> vs2 = Collections.unmodifiableCollection(this._isSummons.values());
            this._allSummonValues = vs2;
            return vs2;
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    public ConcurrentHashMap<Integer, L1SummonInstance> map() {
        return this._isSummons;
    }

    public void put(Integer key, L1SummonInstance value) {
        try {
            this._isSummons.put(key, value);
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public void remove(Integer key) {
        try {
            this._isSummons.remove(key);
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
