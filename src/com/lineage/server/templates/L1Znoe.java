package com.lineage.server.templates;

public class L1Znoe {
    private final int _endX;
    private final int _endY;
    private final int _mapId;
    private final String _mapName;
    private final int _startX;
    private final int _startY;

    public L1Znoe(int mapId, String mapName, int startX, int startY, int endX, int endY) {
        this._mapId = mapId;
        this._mapName = mapName;
        this._startX = startX;
        this._startY = startY;
        this._endX = endX;
        this._endY = endY;
    }

    public String getMapName() {
        return this._mapName;
    }

    public int getStartX() {
        return this._startX;
    }

    public int getStartY() {
        return this._startY;
    }

    public int getEndX() {
        return this._endX;
    }

    public int getEndY() {
        return this._endY;
    }

    public int getMapId() {
        return this._mapId;
    }
}
