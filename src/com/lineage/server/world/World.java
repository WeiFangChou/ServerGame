package com.lineage.server.world;

import com.lineage.config.Config;
import com.lineage.server.datatables.MapsTable;
import com.lineage.server.model.Instance.L1BowInstance;
import com.lineage.server.model.Instance.L1DollInstance;
import com.lineage.server.model.Instance.L1DwarfInstance;
import com.lineage.server.model.Instance.L1EffectInstance;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1MerchantInstance;
import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.model.Instance.L1SummonInstance;
import com.lineage.server.model.Instance.L1TrapInstance;
import com.lineage.server.model.L1GroundInventory;
import com.lineage.server.model.L1Location;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.map.L1Map;
import com.lineage.server.serverpackets.S_GreenMessage;
import com.lineage.server.serverpackets.S_SystemMessage;
import com.lineage.server.serverpackets.ServerBasePacket;
import com.lineage.server.types.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class World {
    private static World _instance;
    private static final Log _log = LogFactory.getLog(World.class);
    private final ConcurrentHashMap<Integer, L1Object> _allObjects = new ConcurrentHashMap<>();
    private Collection<L1PcInstance> _allPlayerValues;
    private final ConcurrentHashMap<String, L1PcInstance> _allPlayers = new ConcurrentHashMap<>();
    private Collection<L1Object> _allValues;
    private boolean _processingContributionTotal = false;
    private final HashMap<Integer, ConcurrentHashMap<Integer, L1Object>> _visibleObjects = new HashMap<>();
    private int _weather = 4;
    private boolean _worldChatEnabled = true;

    public World() {
        for (Integer mapid : MapsTable.get().getMaps().keySet()) {
            ConcurrentHashMap<Integer, L1Object> map = new ConcurrentHashMap<>();
            this._visibleObjects.put(mapid, map);
        }
        _log.info("遊戲世界儲存中心建立完成!!!");
    }

    public static World get() {
        if (_instance == null) {
            _instance = new World();
        }
        return _instance;
    }

    public Object getRegion(Object object) {
        return null;
    }

    public void clear() {
        _instance = new World();
    }

    public void storeObject(L1Object object) {
        if (object == null) {
            try {
                throw new NullPointerException();
            } catch (Exception e) {
                _log.error(e.getLocalizedMessage(), e);
            }
        } else {
            this._allObjects.put(Integer.valueOf(object.getId()), object);
            if (object instanceof L1ItemInstance) {
                WorldItem.get().put(new Integer(object.getId()), (L1ItemInstance) object);
            }
            if (object instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) object;
                if (pc.isCrown()) {
                    WorldCrown.get().put(new Integer(pc.getId()), pc);
                } else if (pc.isKnight()) {
                    WorldKnight.get().put(new Integer(pc.getId()), pc);
                } else if (pc.isElf()) {
                    WorldElf.get().put(new Integer(pc.getId()), pc);
                } else if (pc.isWizard()) {
                    WorldWizard.get().put(new Integer(pc.getId()), pc);
                } else if (pc.isDarkelf()) {
                    WorldDarkelf.get().put(new Integer(pc.getId()), pc);
                } else if (pc.isDragonKnight()) {
                    WorldDragonKnight.get().put(new Integer(pc.getId()), pc);
                } else if (pc.isIllusionist()) {
                    WorldIllusionist.get().put(new Integer(pc.getId()), pc);
                }
                this._allPlayers.put(pc.getName(), pc);
            }
            if (object instanceof L1TrapInstance) {
                WorldTrap.get().put(new Integer(object.getId()), (L1TrapInstance) object);
            }
            if (object instanceof L1PetInstance) {
                WorldPet.get().put(new Integer(object.getId()), (L1PetInstance) object);
            }
            if (object instanceof L1SummonInstance) {
                WorldSummons.get().put(new Integer(object.getId()), (L1SummonInstance) object);
            }
            if (object instanceof L1DollInstance) {
                WorldDoll.get().put(new Integer(object.getId()), (L1DollInstance) object);
            }
            if (object instanceof L1EffectInstance) {
                WorldEffect.get().put(new Integer(object.getId()), (L1EffectInstance) object);
            }
            if (object instanceof L1MonsterInstance) {
                WorldMob.get().put(new Integer(object.getId()), (L1MonsterInstance) object);
            }
            if (object instanceof L1BowInstance) {
                WorldBow.get().put(new Integer(object.getId()), (L1BowInstance) object);
            }
            if (object instanceof L1NpcInstance) {
                WorldNpc.get().put(new Integer(object.getId()), (L1NpcInstance) object);
            }
        }
    }

    public void removeObject(L1Object object) {
        if (object == null) {
            try {
                throw new NullPointerException();
            } catch (Exception e) {
                _log.error(e.getLocalizedMessage(), e);
            }
        } else {
            this._allObjects.remove(Integer.valueOf(object.getId()));
            if (object instanceof L1ItemInstance) {
                WorldItem.get().remove(new Integer(object.getId()));
            }
            if (object instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) object;
                if (pc.isCrown()) {
                    WorldCrown.get().remove(new Integer(pc.getId()));
                } else if (pc.isKnight()) {
                    WorldKnight.get().remove(new Integer(pc.getId()));
                } else if (pc.isElf()) {
                    WorldElf.get().remove(new Integer(pc.getId()));
                } else if (pc.isWizard()) {
                    WorldWizard.get().remove(new Integer(pc.getId()));
                } else if (pc.isDarkelf()) {
                    WorldDarkelf.get().remove(new Integer(pc.getId()));
                } else if (pc.isDragonKnight()) {
                    WorldDragonKnight.get().remove(new Integer(pc.getId()));
                } else if (pc.isIllusionist()) {
                    WorldIllusionist.get().remove(new Integer(pc.getId()));
                }
                this._allPlayers.remove(pc.getName());
            }
            if (object instanceof L1TrapInstance) {
                WorldTrap.get().remove(new Integer(object.getId()));
            }
            if (object instanceof L1PetInstance) {
                WorldPet.get().remove(new Integer(object.getId()));
            }
            if (object instanceof L1SummonInstance) {
                WorldSummons.get().remove(new Integer(object.getId()));
            }
            if (object instanceof L1DollInstance) {
                WorldDoll.get().remove(new Integer(object.getId()));
            }
            if (object instanceof L1EffectInstance) {
                WorldEffect.get().remove(new Integer(object.getId()));
            }
            if (object instanceof L1MonsterInstance) {
                WorldMob.get().remove(new Integer(object.getId()));
            }
            if (object instanceof L1BowInstance) {
                WorldBow.get().remove(new Integer(object.getId()));
            }
            if (object instanceof L1NpcInstance) {
                WorldNpc.get().remove(new Integer(object.getId()));
            }
        }
    }

    public L1Object findObject(int oID) {
        if (oID == 0) {
            return null;
        }
        return this._allObjects.get(Integer.valueOf(oID));
    }

    public Collection<L1Object> getObject() {
        try {
            Collection<L1Object> vs = this._allValues;
            if (vs != null) {
                return vs;
            }
            Collection<L1Object> vs2 = Collections.unmodifiableCollection(this._allObjects.values());
            this._allValues = vs2;
            return vs2;
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    public L1GroundInventory getInventory(int x, int y, short map) {
        int inventoryKey = (((x - 30000) * 10000) + (y - 30000)) * -1;
        try {
            ConcurrentHashMap<Integer, L1Object> idmap = this._visibleObjects.get(new Integer(map));
            if (idmap != null) {
                Object object = idmap.get(Integer.valueOf(inventoryKey));
                if (object == null) {
                    return new L1GroundInventory(inventoryKey, x, y, map);
                }
                return (L1GroundInventory) object;
            }
            _log.error("遊戲世界儲存中心並未建立該地圖編號資料檔案: " + ((int) map));
            return null;
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return  null;
    }



    public L1GroundInventory getInventory(L1Location loc) {
        return getInventory(loc.getX(), loc.getY(), (short) loc.getMap().getId());
    }

    public void addVisibleObject(L1Object object) {
        try {
            ConcurrentHashMap<Integer, L1Object> map = this._visibleObjects.get(new Integer(object.getMapId()));
            if (map != null) {
                map.put(Integer.valueOf(object.getId()), object);
            } else {
                _log.error("遊戲世界儲存中心並未建立該地圖編號資料檔案: " + ((int) object.getMapId()));
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public void removeVisibleObject(L1Object object) {
        try {
            ConcurrentHashMap<Integer, L1Object> map = this._visibleObjects.get(new Integer(object.getMapId()));
            if (map != null) {
                map.remove(Integer.valueOf(object.getId()));
            } else {
                _log.error("遊戲世界儲存中心並未建立該地圖編號資料檔案: " + ((int) object.getMapId()));
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public void moveVisibleObject(L1Object object, int newMapId) {
        try {
            int srcMapId = object.getMapId();
            if (srcMapId != newMapId) {
                ConcurrentHashMap<Integer, L1Object> mapSrc = this._visibleObjects.get(new Integer(srcMapId));
                if (mapSrc != null) {
                    mapSrc.remove(Integer.valueOf(object.getId()));
                } else {
                    _log.error("遊戲世界儲存中心並未建立該地圖編號資料檔案: " + srcMapId);
                }
                ConcurrentHashMap<Integer, L1Object> map = this._visibleObjects.get(new Integer(newMapId));
                if (map != null) {
                    map.put(Integer.valueOf(object.getId()), object);
                } else {
                    _log.error("遊戲世界儲存中心並未建立該地圖編號資料檔案: " + newMapId);
                }
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private HashMap<Integer, Integer> createLineMap(Point src, Point target) {
        HashMap<Integer, Integer> lineMap = new HashMap<>();
        int x0 = src.getX();
        int y0 = src.getY();
        int x1 = target.getX();
        int y1 = target.getY();
        int sx = x1 > x0 ? 1 : -1;
        int dx = x1 > x0 ? x1 - x0 : x0 - x1;
        int sy = y1 > y0 ? 1 : -1;
        int dy = y1 > y0 ? y1 - y0 : y0 - y1;
        int x = x0;
        int y = y0;
        if (dx >= dy) {
            int E = -dx;
            for (int i = 0; i <= dx; i++) {
                int key = (x << 16) + y;
                lineMap.put(Integer.valueOf(key), Integer.valueOf(key));
                x += sx;
                E += dy << 1;
                if (E >= 0) {
                    y += sy;
                    E -= dx << 1;
                }
            }
        } else {
            int E2 = -dy;
            for (int i2 = 0; i2 <= dy; i2++) {
                int key2 = (x << 16) + y;
                lineMap.put(Integer.valueOf(key2), Integer.valueOf(key2));
                y += sy;
                E2 += dx << 1;
                if (E2 >= 0) {
                    x += sx;
                    E2 -= dy << 1;
                }
            }
        }
        return lineMap;
    }

    public ArrayList<L1Object> getVisibleLineObjects(L1Object src, L1Object target) {
        try {
            HashMap<Integer, Integer> lineMap = createLineMap(src.getLocation(), target.getLocation());
            int map = target.getMapId();
            ArrayList<L1Object> result = new ArrayList<>();
            ConcurrentHashMap<Integer, L1Object> mapSrc = this._visibleObjects.get(new Integer(map));
            if (mapSrc == null) {
                _log.error("遊戲世界儲存中心並未建立該地圖編號資料檔案: " + map);
                return result;
            }
            for (L1Object element : mapSrc.values()) {
                if (!element.equals(src) && src.get_showId() == element.get_showId() && lineMap.containsKey(Integer.valueOf((element.getX() << 16) + element.getY()))) {
                    result.add(element);
                }
            }
            lineMap.clear();
            return result;
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    public ArrayList<L1Object> getVisibleBoxObjects(L1Object object, int heading, int width, int height) {
        int x = object.getX();
        int y = object.getY();
        int map = object.getMapId();
        L1Location location = object.getLocation();
        ArrayList<L1Object> result = new ArrayList<>();
        int[] headingRotate = new int[8];
        headingRotate[0] = 6;
        headingRotate[1] = 7;
        headingRotate[3] = 1;
        headingRotate[4] = 2;
        headingRotate[5] = 3;
        headingRotate[6] = 4;
        headingRotate[7] = 5;
        double cosSita = Math.cos((((double) headingRotate[heading]) * 3.141592653589793d) / 4.0d);
        double sinSita = Math.sin((((double) headingRotate[heading]) * 3.141592653589793d) / 4.0d);
        ConcurrentHashMap<Integer, L1Object> mapSrc = this._visibleObjects.get(new Integer(map));
        if (mapSrc == null) {
            _log.error("遊戲世界儲存中心並未建立該地圖編號資料檔案: " + map);
        } else {
            for (L1Object element : mapSrc.values()) {
                if (!element.equals(object) && object.get_showId() == element.get_showId() && map == element.getMapId()) {
                    if (location.isSamePoint(element.getLocation())) {
                        result.add(element);
                    } else {
                        int distance = location.getTileLineDistance(element.getLocation());
                        if (distance <= height || distance <= width) {
                            int x1 = element.getX() - x;
                            int y1 = element.getY() - y;
                            int rotX = (int) Math.round((((double) x1) * cosSita) + (((double) y1) * sinSita));
                            int rotY = (int) Math.round((((double) (-x1)) * sinSita) + (((double) y1) * cosSita));
                            int ymin = -width;
                            if (rotX > 0 && distance <= height && rotY >= ymin && rotY <= width) {
                                result.add(element);
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    public ArrayList<L1Object> getVisibleObjects(L1Object object) {
        return getVisibleObjects(object, -1);
    }

    public boolean getVisibleObjects(L1Object src, L1Object tg) {
        if (src.get_showId() == tg.get_showId() && src.getMapId() == tg.getMapId() && src.getLocation().isInScreen(tg.getLocation())) {
            return true;
        }
        return false;
    }

    public ArrayList<L1Object> getVisibleObjects(L1Object object, int radius) {
        L1Map map = object.getMap();
        Point pt = object.getLocation();
        ArrayList<L1Object> result = new ArrayList<>();
        ConcurrentHashMap<Integer, L1Object> mapSrc = this._visibleObjects.get(new Integer(map.getId()));
        if (mapSrc == null) {
            _log.error("遊戲世界儲存中心並未建立該地圖編號資料檔案: " + map.getId());
        } else {
            for (L1Object element : mapSrc.values()) {
                if (!element.equals(object)) {
                    if (object.get_showId() != element.get_showId()) {
                        boolean is = false;
                        if (element instanceof L1MerchantInstance) {
                            is = true;
                        }
                        if (element instanceof L1DwarfInstance) {
                            is = true;
                        }
                        if (!is) {
                        }
                    }
                    if (map == element.getMap()) {
                        switch (radius) {
                            case -1:
                                if (pt.isInScreen(element.getLocation())) {
                                    result.add(element);
                                    break;
                                } else {
                                    continue;
                                }
                            case 0:
                                if (pt.isSamePoint(element.getLocation())) {
                                    result.add(element);
                                    break;
                                } else {
                                    continue;
                                }
                            default:
                                if (pt.getTileLineDistance(element.getLocation()) <= radius) {
                                    result.add(element);
                                    break;
                                } else {
                                    continue;
                                }
                        }
                    }
                }
            }
        }
        return result;
    }

    public ArrayList<L1Object> getVisiblePoint(L1Location loc, int radius, int showid) {
        ArrayList<L1Object> result = new ArrayList<>();
        int mapId = loc.getMapId();
        ConcurrentHashMap<Integer, L1Object> mapSrc = this._visibleObjects.get(new Integer(mapId));
        if (mapSrc == null) {
            _log.error("遊戲世界儲存中心並未建立該地圖編號資料檔案: " + mapId);
        } else {
            for (L1Object element : mapSrc.values()) {
                if (mapId == element.getMapId() && showid == element.get_showId() && loc.getTileLineDistance(element.getLocation()) <= radius) {
                    result.add(element);
                }
            }
        }
        return result;
    }

    public ArrayList<L1PcInstance> getVisiblePc(L1Location loc) {
        ArrayList<L1PcInstance> result = new ArrayList<>();
        int mapId = loc.getMapId();
        if (this._visibleObjects.get(new Integer(mapId)) == null) {
            _log.error("遊戲世界儲存中心並未建立該地圖編號資料檔案: " + mapId);
        } else {
            for (L1PcInstance element : this._allPlayers.values()) {
                if (mapId == element.getMapId() && loc.isInScreen(element.getLocation())) {
                    result.add(element);
                }
            }
        }
        return result;
    }

    public ArrayList<L1PcInstance> getVisiblePlayer(L1Object object) {
        return getVisiblePlayer(object, -1);
    }

    public ArrayList<L1PcInstance> getVisiblePlayer(L1Object object, int radius) {
        int map = object.getMapId();
        Point pt = object.getLocation();
        ArrayList<L1PcInstance> result = new ArrayList<>();
        for (L1PcInstance element : this._allPlayers.values()) {
            if (!element.equals(object) && map == element.getMapId() && object.get_showId() == element.get_showId()) {
                switch (radius) {
                    case -1:
                        if (pt.isInScreen(element.getLocation())) {
                            result.add(element);
                            break;
                        } else {
                            continue;
                        }
                    case 0:
                        if (pt.isSamePoint(element.getLocation())) {
                            result.add(element);
                            break;
                        } else {
                            continue;
                        }
                    default:
                        if (pt.getTileLineDistance(element.getLocation()) <= radius) {
                            result.add(element);
                            break;
                        } else {
                            continue;
                        }
                }
            }
        }
        return result;
    }

    public ArrayList<L1PcInstance> getVisiblePlayerExceptTargetSight(L1Object object, L1Object target) {
        int map = object.getMapId();
        Point objectPt = object.getLocation();
        Point targetPt = target.getLocation();
        ArrayList<L1PcInstance> result = new ArrayList<>();
        for (L1PcInstance element : this._allPlayers.values()) {
            if (!element.equals(object) && map == element.getMapId() && target.get_showId() == element.get_showId()) {
                if (Config.PC_RECOGNIZE_RANGE == -1) {
                    if (objectPt.isInScreen(element.getLocation()) && !targetPt.isInScreen(element.getLocation())) {
                        result.add(element);
                    }
                } else if (objectPt.getTileLineDistance(element.getLocation()) <= Config.PC_RECOGNIZE_RANGE && targetPt.getTileLineDistance(element.getLocation()) > Config.PC_RECOGNIZE_RANGE) {
                    result.add(element);
                }
            }
        }
        return result;
    }

    public ArrayList<L1PcInstance> getVisiblePlayerExceptTargetSight(L1Object object, L1Object target, int radius) {
        int map = object.getMapId();
        Point objectPt = object.getLocation();
        Point targetPt = target.getLocation();
        ArrayList<L1PcInstance> result = new ArrayList<>();
        for (L1PcInstance element : this._allPlayers.values()) {
            if (!element.equals(object) && map == element.getMapId() && target.get_showId() == element.get_showId() && objectPt.getTileLineDistance(element.getLocation()) <= radius && targetPt.getTileLineDistance(element.getLocation()) <= radius) {
                result.add(element);
            }
        }
        return result;
    }

    public ArrayList<L1PcInstance> getRecognizePlayer(L1Object object) {
        return getVisiblePlayer(object, Config.PC_RECOGNIZE_RANGE);
    }

    public Collection<L1PcInstance> getAllPlayers() {
        try {
            Collection<L1PcInstance> vs = this._allPlayerValues;
            if (vs != null) {
                return vs;
            }
            Collection<L1PcInstance> vs2 = Collections.unmodifiableCollection(this._allPlayers.values());
            this._allPlayerValues = vs2;
            return vs2;
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    public L1PcInstance getPlayer(String name) {
        if (this._allPlayers.contains(name)) {
            return this._allPlayers.get(name);
        }
        for (L1PcInstance each : getAllPlayers()) {
            if (each.getName().equalsIgnoreCase(name)) {
                return each;
            }
        }
        return null;
    }

    public boolean get_pc(L1PcInstance pc, String name) {
        L1PcInstance tg = this._allPlayers.get(name);
        if (tg == null || !pc.getLocation().isInScreen(tg.getLocation())) {
            return false;
        }
        return true;
    }

    public final Map<Integer, L1Object> getAllVisibleObjects() {
        return this._allObjects;
    }

    public final HashMap<Integer, ConcurrentHashMap<Integer, L1Object>> getVisibleObjects() {
        return this._visibleObjects;
    }

    public final ConcurrentHashMap<Integer, L1Object> getVisibleObjects(int mapId) {
        return this._visibleObjects.get(new Integer(mapId));
    }

    public void setWeather(int weather) {
        this._weather = weather;
    }

    public int getWeather() {
        return this._weather;
    }

    public void set_worldChatElabled(boolean flag) {
        this._worldChatEnabled = flag;
    }

    public boolean isWorldChatElabled() {
        return this._worldChatEnabled;
    }

    public void setProcessingContributionTotal(boolean flag) {
        this._processingContributionTotal = flag;
    }

    public boolean isProcessingContributionTotal() {
        return this._processingContributionTotal;
    }

    public void broadcastPacketToAll(ServerBasePacket packet) {
        for (L1PcInstance pc : getAllPlayers()) {
            pc.sendPackets(packet);
        }
    }

    public void broadcastServerMessage(String message) {
        broadcastPacketToAll(new S_SystemMessage(message));
    }

    public void broadcastPacketToAll_sing(S_GreenMessage s_GreenMessage, L1PcInstance objpc) {
        for (L1PcInstance pc : getAllPlayers()) {
            pc.sendPackets(s_GreenMessage);
        }
    }
}
