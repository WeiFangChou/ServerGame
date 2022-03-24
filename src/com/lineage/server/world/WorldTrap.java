package com.lineage.server.world;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1TrapInstance;
import com.lineage.server.model.L1Location;
import java.util.HashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class WorldTrap {
    private static WorldTrap _instance;
    private static final Log _log = LogFactory.getLog(WorldTrap.class);
    private final HashMap<Integer, L1TrapInstance> _isTrap = new HashMap<>();

    public static WorldTrap get() {
        if (_instance == null) {
            _instance = new WorldTrap();
        }
        return _instance;
    }

    private WorldTrap() {
    }

    public HashMap<Integer, L1TrapInstance> map() {
        return this._isTrap;
    }

    public void put(Integer key, L1TrapInstance value) {
        try {
            this._isTrap.put(key, value);
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public void remove(Integer key) {
        try {
            this._isTrap.remove(key);
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public void resetAllTraps() {
        for (Object iter : this._isTrap.values().toArray()) {
            L1TrapInstance trap = (L1TrapInstance) iter;
            trap.resetLocation();
            trap.enableTrap();
        }
    }

    public void onPlayerMoved(L1PcInstance pc) {
        L1Location loc = pc.getLocation();
        for (Object iter : this._isTrap.values().toArray()) {
            L1TrapInstance trap = (L1TrapInstance) iter;
            if (trap.isEnable() && loc.equals(trap.getLocation())) {
                trap.onTrod(pc);
                trap.disableTrap();
            }
        }
    }

    public void onDetection(L1PcInstance pc) {
        L1Location loc = pc.getLocation();
        for (Object iter : this._isTrap.values().toArray()) {
            L1TrapInstance trap = (L1TrapInstance) iter;
            if (trap.isEnable() && loc.isInScreen(trap.getLocation())) {
                trap.onDetection(pc);
                trap.disableTrap();
            }
        }
    }
}
