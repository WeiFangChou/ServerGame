package com.lineage.server.world;

import com.lineage.server.model.Instance.L1BowInstance;
import java.util.HashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class WorldBow {
    private static WorldBow _instance;
    private static final Log _log = LogFactory.getLog(WorldBow.class);
    private final HashMap<Integer, L1BowInstance> _isBow = new HashMap<>();

    public static WorldBow get() {
        if (_instance == null) {
            _instance = new WorldBow();
        }
        return _instance;
    }

    private WorldBow() {
    }

    public HashMap<Integer, L1BowInstance> map() {
        return this._isBow;
    }

    public void put(Integer key, L1BowInstance value) {
        try {
            this._isBow.put(key, value);
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public void remove(Integer key) {
        try {
            this._isBow.remove(key);
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
