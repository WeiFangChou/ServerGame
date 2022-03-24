package com.lineage.server.utils;

import com.lineage.config.Config;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class BinaryOutputStream extends OutputStream {
    private static final String CLIENT_LANGUAGE_CODE = Config.CLIENT_LANGUAGE_CODE;
    private final ByteArrayOutputStream _bao = new ByteArrayOutputStream();

    @Override // java.io.OutputStream
    public void write(int b) throws IOException {
        this._bao.write(b);
    }

    public void writeD(int value) {
        this._bao.write(value & 255);
        this._bao.write((value >> 8) & 255);
        this._bao.write((value >> 16) & 255);
        this._bao.write((value >> 24) & 255);
    }

    public void writeH(int value) {
        this._bao.write(value & 255);
        this._bao.write((value >> 8) & 255);
    }

    public void writeC(int value) {
        this._bao.write(value & 255);
    }

    public void writeP(int value) {
        this._bao.write(value);
    }

    public void writeL(long value) {
        this._bao.write((int) (255 & value));
    }

    public void writeF(double org) {
        long value = Double.doubleToRawLongBits(org);
        this._bao.write((int) (value & 255));
        this._bao.write((int) ((value >> 8) & 255));
        this._bao.write((int) ((value >> 16) & 255));
        this._bao.write((int) ((value >> 24) & 255));
        this._bao.write((int) ((value >> 32) & 255));
        this._bao.write((int) ((value >> 40) & 255));
        this._bao.write((int) ((value >> 48) & 255));
        this._bao.write((int) ((value >> 56) & 255));
    }

    public void writeS(String text) {
        if (text != null) {
            try {
                this._bao.write(text.getBytes(CLIENT_LANGUAGE_CODE));
            } catch (Exception ignored) {
            }
        }
        this._bao.write(0);
    }

    public void writeByte(byte[] text) {
        if (text != null) {
            try {
                this._bao.write(text);
            } catch (Exception ignored) {
            }
        }
    }

    public int getLength() {
        return this._bao.size() + 2;
    }

    public byte[] getBytes() {
        return this._bao.toByteArray();
    }
}
