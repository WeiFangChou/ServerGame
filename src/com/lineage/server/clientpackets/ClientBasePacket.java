package com.lineage.server.clientpackets;

import com.lineage.config.Config;
import com.lineage.echo.ClientExecutor;
import com.lineage.server.serverpackets.S_EquipmentWindow;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class ClientBasePacket {
    private static final String CLIENT_LANGUAGE_CODE = Config.CLIENT_LANGUAGE_CODE;
    private static final Log _log = LogFactory.getLog(ClientBasePacket.class);
    private byte[] _decrypt = null;
    private int _off = 0;

    public abstract void start(byte[] bArr, ClientExecutor clientExecutor) throws Exception, Throwable;

    /* access modifiers changed from: protected */
    public void read(byte[] abyte0) {
        try {
            this._decrypt = abyte0;
            this._off = 1;
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /* access modifiers changed from: protected */
    public int readD() {
        try {
            if (this._decrypt == null) {
                return 0;
            }
            if (this._decrypt.length < this._off + 4) {
                return 0;
            }
            byte[] bArr = this._decrypt;
            int i = this._off;
            this._off = i + 1;
            int i2 = bArr[i] & 255;
            byte[] bArr2 = this._decrypt;
            int i3 = this._off;
            this._off = i3 + 1;
            int i4 = i2 | ((bArr2[i3] << 8) & 65280);
            byte[] bArr3 = this._decrypt;
            int i5 = this._off;
            this._off = i5 + 1;
            int i6 = i4 | ((bArr3[i5] << 16) & 16711680);
            byte[] bArr4 = this._decrypt;
            int i7 = this._off;
            this._off = i7 + 1;
            return i6 | ((bArr4[i7] << S_EquipmentWindow.EQUIPMENT_INDEX_RUNE3) & -16777216);
        } catch (Exception e) {
            return 0;
        }
    }

    /* access modifiers changed from: protected */
    public int readC() {
        try {
            if (this._decrypt == null || this._decrypt.length < this._off + 1) {
                return 0;
            }
            byte[] bArr = this._decrypt;
            int i = this._off;
            this._off = i + 1;
            return bArr[i] & 255;
        } catch (Exception e) {
            return 0;
        }
    }

    /* access modifiers changed from: protected */
    public int readH() {
        try {
            if (this._decrypt == null) {
                return 0;
            }
            if (this._decrypt.length < this._off + 2) {
                return 0;
            }
            byte[] bArr = this._decrypt;
            int i = this._off;
            this._off = i + 1;
            int i2 = bArr[i] & 255;
            byte[] bArr2 = this._decrypt;
            int i3 = this._off;
            this._off = i3 + 1;
            return i2 | ((bArr2[i3] << 8) & 65280);
        } catch (Exception e) {
            return 0;
        }
    }

    /* access modifiers changed from: protected */
    public int readCH() {
        try {
            if (this._decrypt == null) {
                return 0;
            }
            if (this._decrypt.length < this._off + 3) {
                return 0;
            }
            byte[] bArr = this._decrypt;
            int i = this._off;
            this._off = i + 1;
            int i2 = bArr[i] & 255;
            byte[] bArr2 = this._decrypt;
            int i3 = this._off;
            this._off = i3 + 1;
            int i4 = i2 | ((bArr2[i3] << 8) & 65280);
            byte[] bArr3 = this._decrypt;
            int i5 = this._off;
            this._off = i5 + 1;
            return i4 | ((bArr3[i5] << 16) & 16711680);
        } catch (Exception e) {
            return 0;
        }
    }

    /* access modifiers changed from: protected */
    public double readF() {
        try {
            if (this._decrypt == null || this._decrypt.length < this._off + 8) {
                return 0.0d;
            }
            byte[] bArr = this._decrypt;
            int i = this._off;
            this._off = i + 1;
            long l = (long) (bArr[i] & 255);
            byte[] bArr2 = this._decrypt;
            int i2 = this._off;
            this._off = i2 + 1;
            long l2 = l | ((long) ((bArr2[i2] << 8) & 65280));
            byte[] bArr3 = this._decrypt;
            int i3 = this._off;
            this._off = i3 + 1;
            long l3 = l2 | ((long) ((bArr3[i3] << 16) & 16711680));
            byte[] bArr4 = this._decrypt;
            int i4 = this._off;
            this._off = i4 + 1;
            long l4 = l3 | ((long) ((bArr4[i4] << S_EquipmentWindow.EQUIPMENT_INDEX_RUNE3) & -16777216));
            byte[] bArr5 = this._decrypt;
            int i5 = this._off;
            this._off = i5 + 1;
            long l5 = l4 | ((((long) bArr5[i5]) << 32) & 1095216660480L);
            byte[] bArr6 = this._decrypt;
            int i6 = this._off;
            this._off = i6 + 1;
            long l6 = l5 | ((((long) bArr6[i6]) << 40) & 280375465082880L);
            byte[] bArr7 = this._decrypt;
            int i7 = this._off;
            this._off = i7 + 1;
            long l7 = l6 | ((((long) bArr7[i7]) << 48) & 71776119061217280L);
            byte[] bArr8 = this._decrypt;
            int i8 = this._off;
            this._off = i8 + 1;
            return Double.longBitsToDouble(l7 | ((((long) bArr8[i8]) << 56) & -72057594037927936L));
        } catch (Exception e) {
            return 0.0d;
        }
    }

    /* access modifiers changed from: protected */
    public String readS() {
        String s = null;
        try {
            if (this._decrypt == null) {
                return null;
            }
            String s2 = new String(this._decrypt, this._off, this._decrypt.length - this._off, CLIENT_LANGUAGE_CODE);
            try {
                s = s2.substring(0, s2.indexOf(0));
                this._off += s.getBytes(CLIENT_LANGUAGE_CODE).length + 1;
            } catch (Exception e) {
                s = s2;
            }
            return s;
        } catch (Exception e2) {
            return null;
        }
    }

    /* access modifiers changed from: protected */
    public byte[] readByte() {
        byte[] result = new byte[(this._decrypt.length - this._off)];
        try {
            System.arraycopy(this._decrypt, this._off, result, 0, this._decrypt.length - this._off);
            this._off = this._decrypt.length;
        } catch (Exception ignored) {
        }
        return result;
    }

    public void over() {
        try {
            this._decrypt = null;
            this._off = 0;
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public String getType() {
        return getClass().getSimpleName();
    }
}
