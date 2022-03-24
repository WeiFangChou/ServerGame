package com.lineage.server.model;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.map.L1Map;
import com.lineage.server.model.map.L1WorldMap;
import java.io.Serializable;

public class L1Object implements Serializable {
    private static final long serialVersionUID = 1;
    private int _id = 0;
    private final L1Location _loc = new L1Location();
    private int _showId = -1;

    public short getMapId() {
        return (short) this._loc.getMap().getId();
    }

    public void setMap(short mapId) {
        this._loc.setMap(L1WorldMap.get().getMap(mapId));
    }

    public L1Map getMap() {
        return this._loc.getMap();
    }

    public void setMap(L1Map map) {
        if (map == null) {
            throw new NullPointerException();
        }
        this._loc.setMap(map);
    }

    public int getId() {
        return this._id;
    }

    public void setId(int id) {
        this._id = id;
    }

    public int getX() {
        return this._loc.getX();
    }

    public void setX(int x) {
        this._loc.setX(x);
    }

    public int getY() {
        return this._loc.getY();
    }

    public void setY(int y) {
        this._loc.setY(y);
    }

    public L1Location getLocation() {
        return this._loc;
    }

    public void setLocation(L1Location loc) {
        this._loc.setX(loc.getX());
        this._loc.setY(loc.getY());
        this._loc.setMap(loc.getMapId());
    }

    public void setLocation(int x, int y, int mapid) {
        this._loc.setX(x);
        this._loc.setY(y);
        this._loc.setMap(mapid);
    }

    public double getLineDistance(L1Object obj) {
        return getLocation().getLineDistance(obj.getLocation());
    }

    public int getTileLineDistance(L1Object obj) {
        return getLocation().getTileLineDistance(obj.getLocation());
    }

    public int getTileDistance(L1Object obj) {
        return getLocation().getTileDistance(obj.getLocation());
    }

    public void onPerceive(L1PcInstance perceivedFrom) {
    }

    public void onAction(L1PcInstance actionFrom) throws Exception {
    }

    public void onTalkAction(L1PcInstance talkFrom) throws Exception {
    }

    public int get_showId() {
        return this._showId;
    }

    public void set_showId(int showId) {
        this._showId = showId;
    }
}
