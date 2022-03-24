package com.lineage.server.world;

import com.lineage.server.model.Instance.L1ItemInstance;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class WorldItem {
    private static WorldItem _instance;
    private static final Log _log = LogFactory.getLog(WorldItem.class);
    private Collection<L1ItemInstance> _allItemValues;
    private final ConcurrentHashMap<Integer, L1ItemInstance> _isItem = new ConcurrentHashMap<>();

    public static WorldItem get() {
        if (_instance == null) {
            _instance = new WorldItem();
        }
        return _instance;
    }

    private WorldItem() {
    }

    public Collection<L1ItemInstance> all() {
        try {
            Collection<L1ItemInstance> vs = this._allItemValues;
            if (vs != null) {
                return vs;
            }
            Collection<L1ItemInstance> vs2 = Collections.unmodifiableCollection(this._isItem.values());
            this._allItemValues = vs2;
            return vs2;
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    public ConcurrentHashMap<Integer, L1ItemInstance> map() {
        return this._isItem;
    }

    public void put(Integer key, L1ItemInstance value) {
        try {
            this._isItem.put(key, value);
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public void remove(Integer key) {
        try {
            this._isItem.remove(key);
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public L1ItemInstance getItem(Integer key) {
        return this._isItem.get(key);
    }
}
