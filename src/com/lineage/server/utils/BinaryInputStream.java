package com.lineage.server.utils;

import java.io.IOException;
import java.io.InputStream;

public class BinaryInputStream extends InputStream {
    InputStream _in;

    public BinaryInputStream(InputStream in) {
        this._in = in;
    }

    @Override // java.io.InputStream
    public long skip(long n) throws IOException {
        return this._in.skip(n);
    }

    @Override // java.io.InputStream
    public int available() throws IOException {
        return this._in.available();
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable, java.io.InputStream
    public void close() throws IOException {
        this._in.close();
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        return this._in.read();
    }

    public int readByte() throws IOException {
        return this._in.read();
    }

    public int readShort() throws IOException {
        return this._in.read() | ((this._in.read() << 8) & 65280);
    }

    public int readInt() throws IOException {
        return readShort() | ((readShort() << 16) & -65536);
    }
}
