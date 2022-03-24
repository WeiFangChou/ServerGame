package com.lineage.server.types;

public class Point {
    private static final int[] HEADING_TABLE_X;
    private static final int[] HEADING_TABLE_Y;

    /* renamed from: _x */
    protected int f19_x = 0;

    /* renamed from: _y */
    protected int f20_y = 0;

    public Point() {
    }

    public Point(int x, int y) {
        this.f19_x = x;
        this.f20_y = y;
    }

    public Point(Point pt) {
        this.f19_x = pt.f19_x;
        this.f20_y = pt.f20_y;
    }

    public int getX() {
        return this.f19_x;
    }

    public void setX(int x) {
        this.f19_x = x;
    }

    public int getY() {
        return this.f20_y;
    }

    public void setY(int y) {
        this.f20_y = y;
    }

    public void set(Point pt) {
        this.f19_x = pt.f19_x;
        this.f20_y = pt.f20_y;
    }

    public void set(int x, int y) {
        this.f19_x = x;
        this.f20_y = y;
    }

    static {
        int[] iArr = new int[8];
        iArr[1] = 1;
        iArr[2] = 1;
        iArr[3] = 1;
        iArr[5] = -1;
        iArr[6] = -1;
        iArr[7] = -1;
        HEADING_TABLE_X = iArr;
        int[] iArr2 = new int[8];
        iArr2[0] = -1;
        iArr2[1] = -1;
        iArr2[3] = 1;
        iArr2[4] = 1;
        iArr2[5] = 1;
        iArr2[7] = -1;
        HEADING_TABLE_Y = iArr2;
    }

    public void forward(int heading) {
        this.f19_x += HEADING_TABLE_X[heading];
        this.f20_y += HEADING_TABLE_Y[heading];
    }

    public void backward(int heading) {
        this.f19_x -= HEADING_TABLE_X[heading];
        this.f20_y -= HEADING_TABLE_Y[heading];
    }

    public double getLineDistance(Point pt) {
        long diffX = (long) (pt.getX() - getX());
        long diffY = (long) (pt.getY() - getY());
        return Math.sqrt((double) ((diffX * diffX) + (diffY * diffY)));
    }

    public double getLineDistance(int x, int y) {
        long diffX = (long) (x - getX());
        long diffY = (long) (y - getY());
        return Math.sqrt((double) ((diffX * diffX) + (diffY * diffY)));
    }

    public int getTileLineDistance(Point pt) {
        return Math.max(Math.abs(pt.getX() - getX()), Math.abs(pt.getY() - getY()));
    }

    public int getTileDistance(Point pt) {
        return Math.abs(pt.getX() - getX()) + Math.abs(pt.getY() - getY());
    }

    public boolean isInScreen(Point pt) {
        int dist = getTileDistance(pt);
        if (dist > 19) {
            return false;
        }
        if (dist <= 18) {
            return true;
        }
        int dist2 = Math.abs(pt.getX() - (getX() - 18)) + Math.abs(pt.getY() - (getY() - 18));
        return 19 <= dist2 && dist2 <= 52;
    }

    public boolean isSamePoint(Point pt) {
        return pt.getX() == getX() && pt.getY() == getY();
    }

    public int hashCode() {
        return (getX() * 7) + getY();
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Point)) {
            return false;
        }
        Point pt = (Point) obj;
        if (getX() == pt.getX() && getY() == pt.getY()) {
            return true;
        }
        return false;
    }

    public String toString() {
        return String.format("(%d, %d)", Integer.valueOf(this.f19_x), Integer.valueOf(this.f20_y));
    }
}
