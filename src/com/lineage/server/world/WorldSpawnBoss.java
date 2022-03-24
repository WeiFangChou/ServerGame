package com.lineage.server.world;

import com.lineage.server.templates.L1SpawnBoss;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class WorldSpawnBoss {
    private static WorldSpawnBoss _instance;
    private static final Log _log = LogFactory.getLog(WorldSpawnBoss.class);
    private Collection<L1SpawnBoss> _allBossValues;
    private final HashMap<Integer, L1SpawnBoss> _bossSpawn = new HashMap<>();

    public static WorldSpawnBoss get() {
        if (_instance == null) {
            _instance = new WorldSpawnBoss();
        }
        return _instance;
    }

    private WorldSpawnBoss() {
    }

    public Collection<L1SpawnBoss> all() {
        try {
            Collection<L1SpawnBoss> vs = this._allBossValues;
            if (vs != null) {
                return vs;
            }
            Collection<L1SpawnBoss> vs2 = Collections.unmodifiableCollection(this._bossSpawn.values());
            this._allBossValues = vs2;
            return vs2;
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    public HashMap<Integer, L1SpawnBoss> map() {
        return this._bossSpawn;
    }

    public L1SpawnBoss get(Integer key) {
        return this._bossSpawn.get(key);
    }

    public void put(Integer key, L1SpawnBoss value) {
        try {
            this._bossSpawn.put(key, value);
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public void remove(Integer key) {
        try {
            this._bossSpawn.remove(key);
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
