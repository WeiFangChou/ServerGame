package com.lineage.server;

import java.util.concurrent.atomic.AtomicInteger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class IdFactoryNpc {
    private static IdFactoryNpc _instance;
    private static final Log _log = LogFactory.getLog(IdFactoryNpc.class);
    private Object _monitor;
    private AtomicInteger _nextId;

    public static IdFactoryNpc get() {
        if (_instance == null) {
            _instance = new IdFactoryNpc();
        }
        return _instance;
    }

    public IdFactoryNpc() {
        try {
            this._monitor = new Object();
            this._nextId = new AtomicInteger(2000000000);
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public int nextId() {
        int andIncrement;
        synchronized (this._monitor) {
            andIncrement = this._nextId.getAndIncrement();
        }
        return andIncrement;
    }

    public int maxId() {
        int i;
        synchronized (this._monitor) {
            i = this._nextId.get();
        }
        return i;
    }
}
