package com.lineage.server.model;

import com.lineage.server.model.map.L1Map;
import com.lineage.server.model.map.L1WorldMap;
import com.lineage.server.types.Point;

import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1Location extends Point {
    private static final Log _log = LogFactory.getLog(L1Location.class);
    private static Random _random = new Random();
    protected L1Map _map;

    public L1Location() {
        this._map = L1Map.newNull();
    }

    public L1Location(L1Location loc) {
        this(loc.f19_x, loc.f20_y, loc._map);
    }

    public L1Location(int x, int y, int mapId) {
        super(x, y);
        this._map = L1Map.newNull();
        setMap(mapId);
    }

    public L1Location(int x, int y, L1Map map) {
        super(x, y);
        this._map = L1Map.newNull();
        this._map = map;
    }

    public L1Location(Point pt, int mapId) {
        super(pt);
        this._map = L1Map.newNull();
        setMap(mapId);
    }

    public L1Location(Point pt, L1Map map) {
        super(pt);
        this._map = L1Map.newNull();
        this._map = map;
    }

    public void set(L1Location loc) {
        this._map = loc._map;
        this.f19_x = loc.f19_x;
        this.f20_y = loc.f20_y;
    }

    public void set(int x, int y, int mapId) {
        set(x, y);
        setMap(mapId);
    }

    public void set(int x, int y, L1Map map) {
        set(x, y);
        this._map = map;
    }

    public void set(Point pt, int mapId) {
        set(pt);
        setMap(mapId);
    }

    public void set(Point pt, L1Map map) {
        set(pt);
        this._map = map;
    }

    public L1Map getMap() {
        return this._map;
    }

    public int getMapId() {
        return this._map.getId();
    }

    public void setMap(L1Map map) {
        this._map = map;
    }

    public void setMap(int mapId) {
        this._map = L1WorldMap.get().getMap( mapId);
    }

    @Override // com.lineage.server.types.Point
    public boolean equals(Object obj) {
        if (!(obj instanceof L1Location)) {
            return false;
        }
        L1Location loc = (L1Location) obj;
        if (getMap() == loc.getMap() && getX() == loc.getX() && getY() == loc.getY()) {
            return true;
        }
        return false;
    }

    @Override // com.lineage.server.types.Point
    public int hashCode() {
        return (this._map.getId() * 7) + hashCode();
    }

    @Override // com.lineage.server.types.Point
    public String toString() {
        return String.format("(%d, %d) on %d", Integer.valueOf(this.f19_x), Integer.valueOf(this.f20_y), Integer.valueOf(this._map.getId()));
    }

    public L1Location randomLocation(int max, boolean isRandomTeleport) {
        return randomLocation(0, max, isRandomTeleport);
    }

    public L1Location randomLocation(int min, int max, boolean isRandomTeleport) {
        return randomLocation(this, min, max, isRandomTeleport);
    }

    public static L1Location randomLocation(L1Location baseLocation, int min, int max, boolean isRandomTeleport) {
        L1Location newLocation = null;
        if (min > max) {
            try {
                throw new IllegalArgumentException("min > max 設定異常");
            } catch (Exception e) {
                Exception e1 = e;
                _log.error(e1.getLocalizedMessage(), e1);
                return newLocation;
            }
        } else if (max <= 0) {
            return new L1Location(baseLocation);
        } else {
            if (min < 0) {
                min = 0;
            }
            L1Location newLocation2 = new L1Location();
            try {
                int locX = baseLocation.getX();
                int locY = baseLocation.getY();
                int mapId =  baseLocation.getMapId();
                L1Map map = baseLocation.getMap();
                newLocation2.setMap(map);
                int locX1 = locX - max;
                int locX2 = locX + max;
                int locY1 = locY - max;
                int locY2 = locY + max;
                int mapX1 = map.getX();
                int mapX2 = mapX1 + map.getWidth();
                int mapY1 = map.getY();
                int mapY2 = mapY1 + map.getHeight();
                if (locX1 < mapX1) {
                    locX1 = mapX1;
                }
                if (locX2 > mapX2) {
                    locX2 = mapX2;
                }
                if (locY1 < mapY1) {
                    locY1 = mapY1;
                }
                if (locY2 > mapY2) {
                    locY2 = mapY2;
                }
                int diffX = locX2 - locX1;
                int diffY = locY2 - locY1;
                int trial = 0;
                int amax = (int) Math.pow(((max * 2) + 1), 2.0d);
                int trialLimit = (amax * 40) / (amax - (min == 0 ? 0 : (int) Math.pow((double) (((min - 1) * 2) + 1), 2.0d)));
                while (true) {
                    if (1 == 0) {
                        newLocation = newLocation2;
                        break;
                    } else if (trial >= trialLimit) {
                        newLocation2.set(locX, locY);
                        newLocation = newLocation2;
                        break;
                    } else {
                        trial++;
                        try {
                            int newX = locX1 + _random.nextInt(diffX + 1);
                            int newY = locY1 + _random.nextInt(diffY + 1);
                            newLocation2.set(newX, newY);
                            if (baseLocation.getTileLineDistance(newLocation2) >= min && ((!isRandomTeleport || (!L1CastleLocation.checkInAllWarArea(newX, newY, mapId) && !L1HouseLocation.isInHouse(newX, newY, mapId))) && map.isInMap(newX, newY) && map.isPassable(newX, newY, (L1Character) null))) {
                                newLocation = newLocation2;
                                break;
                            }
                        } catch (Exception e2) {
                            newLocation2.set(locX, locY);
                            newLocation = newLocation2;
                        }
                    }
                }
            } catch (Exception e) {

                newLocation = newLocation2;
                _log.error(e.getLocalizedMessage(), e);
                return newLocation;
            }

            return newLocation;
        }
    }
}
