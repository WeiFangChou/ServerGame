package com.lineage.server.model.Instance;

public abstract class NpcMoveExecutor {
    protected static final byte[] HEADING_RD;
    protected static final byte[] HEADING_TABLE_X;
    protected static final byte[] HEADING_TABLE_XR;
    protected static final byte[] HEADING_TABLE_Y;
    protected static final byte[] HEADING_TABLE_YR;
    protected static final int[][] _ary1;
    protected static final int[] _heading2;
    protected static final int[] _heading3;

    public abstract int checkObject(int i);

    public abstract void clear();

    public abstract int moveDirection(int i, int i2);

    public abstract int openDoor(int i);

    public abstract void setDirectionMove(int i);

    public abstract int targetReverseDirection(int i, int i2);

    static {
        byte[] bArr = new byte[8];
        bArr[1] = 1;
        bArr[2] = 1;
        bArr[3] = 1;
        bArr[5] = -1;
        bArr[6] = -1;
        bArr[7] = -1;
        HEADING_TABLE_X = bArr;
        byte[] bArr2 = new byte[8];
        bArr2[0] = -1;
        bArr2[1] = -1;
        bArr2[3] = 1;
        bArr2[4] = 1;
        bArr2[5] = 1;
        bArr2[7] = -1;
        HEADING_TABLE_Y = bArr2;
        byte[] bArr3 = new byte[8];
        bArr3[1] = -1;
        bArr3[2] = -1;
        bArr3[3] = -1;
        bArr3[5] = 1;
        bArr3[6] = 1;
        bArr3[7] = 1;
        HEADING_TABLE_XR = bArr3;
        byte[] bArr4 = new byte[8];
        bArr4[0] = 1;
        bArr4[1] = 1;
        bArr4[3] = -1;
        bArr4[4] = -1;
        bArr4[5] = -1;
        bArr4[7] = 1;
        HEADING_TABLE_YR = bArr4;
        byte[] bArr5 = new byte[8];
        bArr5[0] = 4;
        bArr5[1] = 5;
        bArr5[2] = 6;
        bArr5[3] = 7;
        bArr5[5] = 1;
        bArr5[6] = 2;
        bArr5[7] = 3;
        HEADING_RD = bArr5;
        int[] iArr = new int[5];
        iArr[0] = 7;
        iArr[1] = 1;
        iArr[3] = 6;
        iArr[4] = 2;
        int[] iArr2 = new int[5];
        iArr2[0] = 7;
        iArr2[1] = 3;
        iArr2[2] = 1;
        iArr2[4] = 2;
        int[] iArr3 = new int[5];
        iArr3[0] = 3;
        iArr3[1] = 1;
        iArr3[3] = 4;
        iArr3[4] = 2;
        int[] iArr4 = new int[5];
        iArr4[0] = 7;
        iArr4[1] = 5;
        iArr4[3] = 6;
        iArr4[4] = 4;
        int[] iArr5 = new int[5];
        iArr5[0] = 7;
        iArr5[1] = 5;
        iArr5[2] = 1;
        iArr5[4] = 6;
        _ary1 = new int[][]{iArr, iArr2, iArr3, new int[]{5, 3, 1, 4, 2}, new int[]{5, 3, 6, 4, 2}, new int[]{7, 5, 3, 6, 4}, iArr4, iArr5};
        int[] iArr6 = new int[8];
        iArr6[0] = 7;
        iArr6[2] = 1;
        iArr6[3] = 2;
        iArr6[4] = 3;
        iArr6[5] = 4;
        iArr6[6] = 5;
        iArr6[7] = 6;
        _heading2 = iArr6;
        int[] iArr7 = new int[8];
        iArr7[0] = 1;
        iArr7[1] = 2;
        iArr7[2] = 3;
        iArr7[3] = 4;
        iArr7[4] = 5;
        iArr7[5] = 6;
        iArr7[6] = 7;
        _heading3 = iArr7;
    }
}
