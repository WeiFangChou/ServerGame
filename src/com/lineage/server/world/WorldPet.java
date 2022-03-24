package com.lineage.server.world;

import com.lineage.server.model.Instance.L1PetInstance;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class WorldPet {
    private static WorldPet _instance;
    private static final Log _log = LogFactory.getLog(WorldPet.class);
    private Collection<L1PetInstance> _allPetValues;
    private final ConcurrentHashMap<Integer, L1PetInstance> _isPet = new ConcurrentHashMap<>();

    public static WorldPet get() {
        if (_instance == null) {
            _instance = new WorldPet();
        }
        return _instance;
    }

    private WorldPet() {
    }

    public Collection<L1PetInstance> all() {
        try {
            Collection<L1PetInstance> vs = this._allPetValues;
            if (vs != null) {
                return vs;
            }
            Collection<L1PetInstance> vs2 = Collections.unmodifiableCollection(this._isPet.values());
            this._allPetValues = vs2;
            return vs2;
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    public ConcurrentHashMap<Integer, L1PetInstance> map() {
        return this._isPet;
    }

    public L1PetInstance get(Integer key) {
        try {
            return this._isPet.get(key);
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    public void put(Integer key, L1PetInstance value) {
        try {
            this._isPet.put(key, value);
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public void remove(Integer key) {
        try {
            this._isPet.remove(key);
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
