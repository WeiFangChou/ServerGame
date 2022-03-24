package com.lineage.server.world;

import com.lineage.server.model.L1War;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class WorldWar {
    private static WorldWar _instance;
    private static final Log _log = LogFactory.getLog(WorldWar.class);
    private List<L1War> _allWarList;
    private final CopyOnWriteArrayList<L1War> _allWars = new CopyOnWriteArrayList<>();

    public static WorldWar get() {
        if (_instance == null) {
            _instance = new WorldWar();
        }
        return _instance;
    }

    private WorldWar() {
    }

    public void addWar(L1War war) {
        try {
            if (!this._allWars.contains(war)) {
                this._allWars.add(war);
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public void removeWar(L1War war) {
        try {
            if (this._allWars.contains(war)) {
                this._allWars.remove(war);
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public List<L1War> getWarList() {
        try {
            List<L1War> vs = this._allWarList;
            if (vs != null) {
                return vs;
            }
            List<L1War> vs2 = Collections.unmodifiableList(this._allWars);
            this._allWarList = vs2;
            return vs2;
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    public boolean isWar(String clanname, String tgclanname) {
        try {
            Iterator<L1War> it = this._allWars.iterator();
            while (it.hasNext()) {
                L1War war = it.next();
                if (war.checkClanInWar(clanname) && war.checkClanInWar(tgclanname)) {
                    return true;
                }
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return false;
    }
}
