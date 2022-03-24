package com.lineage.server.world;

import com.lineage.server.model.Instance.L1EffectInstance;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Location;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.map.L1Map;
import com.lineage.server.types.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class WorldEffect {
    private static WorldEffect _instance;
    private static final Log _log = LogFactory.getLog(WorldEffect.class);
    private Collection<L1EffectInstance> _allEffValues;
    private final ConcurrentHashMap<Integer, L1EffectInstance> _isEff = new ConcurrentHashMap<>();

    public static WorldEffect get() {
        if (_instance == null) {
            _instance = new WorldEffect();
        }
        return _instance;
    }

    private WorldEffect() {
    }

    public Collection<L1EffectInstance> all() {
        try {
            Collection<L1EffectInstance> vs = this._allEffValues;
            if (vs != null) {
                return vs;
            }
            Collection<L1EffectInstance> vs2 = Collections.unmodifiableCollection(this._isEff.values());
            this._allEffValues = vs2;
            return vs2;
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    public ConcurrentHashMap<Integer, L1EffectInstance> map() {
        return this._isEff;
    }

    public void put(Integer key, L1EffectInstance value) {
        try {
            this._isEff.put(key, value);
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public void remove(Integer key) {
        try {
            this._isEff.remove(key);
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public boolean isEffect(L1Location loc, int npcid) {
        for (L1EffectInstance element : all()) {
            if (loc.getMapId() == element.getMap().getId() && npcid == element.getNpcId() && loc.isSamePoint(element.getLocation())) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<L1EffectInstance> getVisibleEffect(L1Location loc) {
        ArrayList<L1EffectInstance> result = new ArrayList<>();
        for (L1EffectInstance element : all()) {
            if (loc.getMapId() == element.getMap().getId() && loc.isSamePoint(element.getLocation())) {
                result.add(element);
            }
        }
        return result;
    }

    public ArrayList<L1EffectInstance> getVisibleEffect(L1EffectInstance src) {
        L1Map map = src.getMap();
        Point pt = src.getLocation();
        ArrayList<L1EffectInstance> result = new ArrayList<>();
        for (L1EffectInstance element : all()) {
            if (!element.equals(src) && map.getId() == element.getMap().getId() && src.getNpcId() == element.getNpcId() && pt.isSamePoint(element.getLocation())) {
                result.add(element);
            }
        }
        return result;
    }

    public int getVisibleCount(L1EffectInstance src) {
        L1Map map = src.getMap();
        Point pt = src.getLocation();
        int count = 0;
        for (L1EffectInstance element : all()) {
            if (!element.equals(src) && map == element.getMap() && pt.isInScreen(element.getLocation()) && src.getNpcId() == element.getNpcId()) {
                count++;
            }
        }
        return count;
    }

    public ArrayList<L1Character> getFirewall(L1EffectInstance firewall) {
        L1Map map = firewall.getMap();
        Point pt = firewall.getLocation();
        ArrayList<L1Character> result = new ArrayList<>();
        ArrayList<L1Object> mapSrc = World.get().getVisibleObjects(firewall, 2);
        if (mapSrc == null) {
            _log.error("遊戲世界儲存中心並未建立該地圖編號資料檔案: " + map.getId());
        } else if (!(mapSrc.isEmpty() || World.get().findObject(firewall.getId()) == null || World.get().findObject(firewall.getMaster().getId()) == null || firewall.destroyed() || firewall.getMaster() == null)) {
            Iterator<L1Object> it = mapSrc.iterator();
            while (it.hasNext()) {
                L1Object element = it.next();
                if ((element instanceof L1Character) && !firewall.getMaster().equals(element) && !((L1Character) element).isDead() && pt.getTileLineDistance(element.getLocation()) <= 1) {
                    result.add((L1Character) element);
                }
            }
        }
        return result;
    }
}
