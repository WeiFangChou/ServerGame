package com.lineage.server.serverpackets;

import com.lineage.config.Config;
import com.lineage.config.ConfigBad;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class ServerBasePacket {

    private static Logger _log = Logger.getLogger(ServerBasePacket.class.getName());

    ByteArrayOutputStream _bao = new ByteArrayOutputStream();

    protected ServerBasePacket() {
    }

    protected void writeD(int value) {
        _bao.write(value & 0xff);
        _bao.write(value >> 8 & 0xff);
        _bao.write(value >> 16 & 0xff);
        _bao.write(value >> 24 & 0xff);
    }

    protected void writeH(int value) {
        _bao.write(value & 0xff);
        _bao.write(value >> 8 & 0xff);
    }

    protected void writeC(int value) {
        _bao.write(value & 0xff);
    }

    protected void writeP(int value) {
        _bao.write(value);
    }

    protected void writeL(long value) {
        _bao.write((int) (value & 0xff));
    }

    protected void writeF(double org) {
        long value = Double.doubleToRawLongBits(org);
        _bao.write((int) (value & 0xff));
        _bao.write((int) (value >> 8 & 0xff));
        _bao.write((int) (value >> 16 & 0xff));
        _bao.write((int) (value >> 24 & 0xff));
        _bao.write((int) (value >> 32 & 0xff));
        _bao.write((int) (value >> 40 & 0xff));
        _bao.write((int) (value >> 48 & 0xff));
        _bao.write((int) (value >> 56 & 0xff));
    }

    protected void writeExp(long value) {
        this._bao.write((int) (value & 0xFFL));
        this._bao.write((int) (value >> 8 & 0xFFL));
        this._bao.write((int) (value >> 16 & 0xFFL));
        this._bao.write((int) (value >> 24 & 0xFFL));
    }

    protected void writeS(String text) {
        try {
            if (text != null) {
                _bao.write(text.getBytes(Config.CLIENT_LANGUAGE_CODE));
            }
        } catch (Exception e) {
            _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
        }

        _bao.write(0);
    }

    protected void writeByte(byte[] text) {
        try {
            if (text != null) {
                _bao.write(text);
            }
        } catch (Exception e) {
            _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
    }

    public int getLength() {
        return _bao.size() + 2;
    }

    public byte[] getBytes() {
        int padding = _bao.size() % 4;

        if (padding != 0) {
            for (int i = padding; i < 4; i++) {
                writeC(0x00);
            }
        }

        return _bao.toByteArray();
    }

    public abstract byte[] getContent() throws IOException;

    public String getType() {
        return "[S] " + this.getClass().getSimpleName();
    }
}