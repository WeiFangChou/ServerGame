package com.lineage.server.model.Instance.npcai;

import java.util.HashSet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Square {
    private static final Log _log = LogFactory.getLog(Square.class);
    private final HashSet<Square> _adjacencies;
    private boolean _end;
    private final CheckPath _maze;
    private boolean _open = false;
    private Square _parent;
    private boolean _start;

    /* renamed from: _x */
    private final int f14_x;

    /* renamed from: _y */
    private final int f15_y;

    public Square(int x, int y, CheckPath maze) {
        this.f14_x = x;
        this.f15_y = y;
        this._maze = maze;
        this._adjacencies = new HashSet<>();
    }

    public int getX() {
        return this.f14_x;
    }

    public int getY() {
        return this.f15_y;
    }

    public boolean isStart() {
        return this._start;
    }

    public void setStart(boolean start) {
        this._start = start;
    }

    public boolean isEnd() {
        return this._end;
    }

    public void setEnd(boolean end) {
        this._end = end;
    }

    public HashSet<Square> getAdjacencies() {
        return this._adjacencies;
    }

    public Square getParent() {
        return this._parent;
    }

    public void setParent(Square parent) {
        this._parent = parent;
    }

    public void calculateAdjacencies() {
        try {
            int bottom = this.f14_x + 1;
            int right = this.f15_y + 1;
            if (bottom < this._maze.getRows() && this._maze.getSquare(bottom, this.f15_y).is_open()) {
                this._maze.getSquare(bottom, this.f15_y).addAdjacency(this);
                addAdjacency(this._maze.getSquare(bottom, this.f15_y));
            }
            if (right < this._maze.getColumns() && this._maze.getSquare(this.f14_x, right).is_open()) {
                this._maze.getSquare(this.f14_x, right).addAdjacency(this);
                addAdjacency(this._maze.getSquare(this.f14_x, right));
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public void addAdjacency(Square square) {
        this._adjacencies.add(square);
    }

    public void removeAdjacency(Square square) {
        this._adjacencies.remove(square);
    }

    public boolean is_open() {
        return this._open;
    }

    public void set_open(boolean open) {
        this._open = open;
    }
}
