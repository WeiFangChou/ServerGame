package com.lineage.server.world;

import com.lineage.server.model.L1CastleLocation;
import com.lineage.server.model.L1Clan;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class WorldClan {
    private static WorldClan _instance;
    private static final Log _log = LogFactory.getLog(WorldClan.class);
    private Collection<L1Clan> _allClanValues;
    private final ConcurrentHashMap<String, L1Clan> _isClan = new ConcurrentHashMap<>();

    public static WorldClan get() {
        if (_instance == null) {
            _instance = new WorldClan();
        }
        return _instance;
    }

    private WorldClan() {
    }

    public L1Clan getClan(int clan_id) {
        for (L1Clan c : getAllClans()) {
            if (c.getClanId() == clan_id) {
                return c;
            }
        }
        return null;
    }

    public Collection<L1Clan> getAllClans() {
        try {
            Collection<L1Clan> vs = this._allClanValues;
            if (vs != null) {
                return vs;
            }
            Collection<L1Clan> vs2 = Collections.unmodifiableCollection(this._isClan.values());
            this._allClanValues = vs2;
            return vs2;
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    public void storeClan(L1Clan clan) {
        if (getClan(clan.getClanName()) == null) {
            this._isClan.put(clan.getClanName(), clan);
            int castle_id = clan.getCastleId();
            if (castle_id != 0 && L1CastleLocation.mapCastle().get(new Integer(castle_id)) == null) {
                L1CastleLocation.putCastle(new Integer(castle_id), clan);
            }
        }
    }

    public void removeClan(L1Clan clan) {
        if (getClan(clan.getClanName()) != null) {
            this._isClan.remove(clan.getClanName());
        }
    }

    public L1Clan getClan(String clan_name) {
        return this._isClan.get(clan_name);
    }

    public ConcurrentHashMap<String, L1Clan> map() {
        return this._isClan;
    }

    public HashMap<Integer, String> castleClanMap() {
        HashMap<Integer, String> isClan = new HashMap<>();
        for (L1Clan clan : getAllClans()) {
            if (clan.getCastleId() != 0) {
                isClan.put(Integer.valueOf(clan.getCastleId()), clan.getClanName());
            }
        }
        return isClan;
    }

    public void put(String key, L1Clan value) {
        try {
            this._isClan.put(key, value);
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public void remove(String key) {
        try {
            this._isClan.remove(key);
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
