package com.lineage.server.utils;

import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class StreamUtil {
    private static final Log _log = LogFactory.getLog(StreamUtil.class);

    public static void close(Closeable... closeables) {
        for (Closeable c : closeables) {
            if (c != null) {
                try {
                    c.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

    public static void close(SelectionKey... keys) {
        for (SelectionKey key : keys) {
            if (key != null) {
                key.cancel();
            }
        }
    }

    public static void close(Selector... selectors) {
        for (Selector selector : selectors) {
            if (selector != null) {
                try {
                    selector.close();
                } catch (IOException e) {
                    _log.error("關閉Selector發生異常", e);
                }
            }
        }
    }

    public static void close(Socket csocket) {
        try {
            if (!csocket.isClosed()) {
                csocket.shutdownInput();
                csocket.shutdownOutput();
                csocket.close();
            }
        } catch (IOException e) {
            _log.error("關閉Socket發生異常", e);
        }
    }

    public static void close(ServerSocket server) {
        try {
            if (!server.isClosed()) {
                server.close();
            }
        } catch (IOException e) {
            _log.error("關閉ServerSocket發生異常", e);
        }
    }

    public static void interrupt(Thread thread) {
        try {
            if (thread.isAlive()) {
                thread.interrupt();
            }
        } catch (Exception e) {
            _log.error("關閉Thread發生異常", e);
        }
    }
}
