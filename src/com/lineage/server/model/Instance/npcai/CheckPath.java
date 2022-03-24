package com.lineage.server.model.Instance.npcai;

import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.map.L1Map;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CheckPath {
    private static final Log _log = LogFactory.getLog(CheckPath.class);
    private final ArrayList<int[]> _bestList = new ArrayList<>();
    private final HashSet<Square> _closed = new HashSet<>();
    private final int _columns;
    private final int _cx;
    private final int _cy;
    private final Square[][] _elements;
    private Square _goal;
    private final ArrayList<Square> _opened = new ArrayList<>();
    private final int _rows;
    private Square _start;

    public CheckPath(int tx, int ty, int hc, L1NpcInstance npc) {
        int x = npc.getX();
        int y = npc.getY();
        int x1 = x - hc;
        int y1 = y - hc;
        int x2 = x + hc;
        int y2 = y + hc;
        this._rows = x2 - x1;
        this._columns = y2 - y1;
        this._cx = x1;
        this._cy = y1;
        int mx = x2 - this._rows;
        int my = y2 - this._columns;
        this._elements = (Square[][]) Array.newInstance(Square.class, this._rows, this._columns);
        createSquares(npc);
        setStartAndGoal(x - mx, y - my, tx - mx, ty - my);
        init();
    }

    public int[] cxy() {
        return new int[]{this._cx, this._cy};
    }

    /* access modifiers changed from: protected */
    public int getRows() {
        return this._rows;
    }

    /* access modifiers changed from: protected */
    public int getColumns() {
        return this._columns;
    }

    /* access modifiers changed from: protected */
    public Square getSquare(int x, int y) {
        return this._elements[x][y];
    }

    private void init() {
        generateAdjacenies();
    }

    public void draw() {
        for (int i = 0; i < this._rows; i++) {
            for (int j = 0; j < this._columns; j++) {
                drawLeft(this._elements[i][j]);
            }
            System.out.println();
        }
    }

    private void drawLeft(Square square) {
        String out = null;
        Iterator<int[]> it = this._bestList.iterator();
        while (it.hasNext()) {
            int[] i = it.next();
            if (square.getX() == i[0] && square.getY() == i[1]) {
                if (square.isEnd()) {
                    out = "PC";
                } else {
                    out = "^^";
                }
            }
        }
        if (out == null) {
            if (square.isStart()) {
                out = "NP";
            } else if (square.isEnd()) {
                out = "PC";
            } else if (square.is_open()) {
                out = "  ";
            } else {
                out = "##";
            }
        }
        System.out.print(out);
    }

    private void setStartAndGoal(int x, int y, int tx, int ty) {
        try {
            this._start = this._elements[x][y];
            this._start.setStart(true);
            this._goal = this._elements[tx][ty];
            this._goal.setEnd(true);
        } catch (Exception ignored) {
        }
    }

    private void generateAdjacenies() {
        for (int i = 0; i < this._rows; i++) {
            try {
                for (int j = 0; j < this._columns; j++) {
                    this._elements[i][j].calculateAdjacencies();
                }
            } catch (Exception e) {
                _log.error(e.getLocalizedMessage(), e);
                return;
            }
        }
    }

    private void createSquares(L1NpcInstance npc) {
        try {
            L1Map map = npc.getMap();
            for (int i = 0; i < this._rows; i++) {
                for (int j = 0; j < this._columns; j++) {
                    Square[] squareArr = this._elements[i];
                    Square square = new Square(i, j, this);
                    squareArr[j] = square;
                    if (map.isPassableDna(this._cx + i, this._cy + j, 0)) {
                        square.set_open(true);
                    }
                }
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public ArrayList<int[]> findBestPath() {
        try {
            Iterator<Square> iter = this._start.getAdjacencies().iterator();
            while (iter.hasNext()) {
                Square adjacency = iter.next();
                adjacency.setParent(this._start);
                if (!adjacency.isStart()) {
                    this._opened.add(adjacency);
                }
            }
            while (this._opened.size() > 0) {
                Square best = findBestPassThrough();
                this._opened.remove(best);
                this._closed.add(best);
                if (best.isEnd()) {
                    populateBestList(this._goal);
                    this._opened.clear();
                    this._closed.clear();
                    return this._bestList;
                }
                Iterator<Square> iter2 = best.getAdjacencies().iterator();
                while (iter2.hasNext()) {
                    Square neighbor = iter2.next();
                    if (this._opened.contains(neighbor)) {
                        Square tmpSquare = new Square(neighbor.getX(), neighbor.getY(), this);
                        tmpSquare.setParent(best);
                        if (!tmpSquare.is_open()) {
                            this._opened.remove(tmpSquare);
                        }
                    }
                    if (this._closed.contains(neighbor)) {
                        Square tmpSquare2 = new Square(neighbor.getX(), neighbor.getY(), this);
                        tmpSquare2.setParent(best);
                        if (!tmpSquare2.is_open()) {
                            this._closed.remove(tmpSquare2);
                        }
                    }
                    neighbor.setParent(best);
                    this._opened.remove(neighbor);
                    this._closed.remove(neighbor);
                    this._opened.add(0, neighbor);
                }
            }
            this._opened.clear();
            this._closed.clear();
            this._bestList.clear();
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return this._bestList;
    }

    private void populateBestList(Square square) throws Exception {
        if (square != null) {
            try {
                this._bestList.add(0, new int[]{square.getX(), square.getY()});
                if (!square.getParent().isStart()) {
                    populateBestList(square.getParent());
                }
            } catch (Exception e) {
                _log.error(e.getLocalizedMessage(), e);
            }
        }
    }

    private Square findBestPassThrough() {
        Square best = null;
        try {
            Iterator<Square> iter = this._opened.iterator();
            while (iter.hasNext()) {
                Square square = iter.next();
                if (best == null || square.is_open()) {
                    best = square;
                }
            }
            return best;
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            return null;
        }
    }
}
