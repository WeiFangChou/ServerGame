package com.lineage.server.model;

import com.lineage.config.Config;
import org.apache.commons.logging.Log;

public class ModelError {
    private static boolean _debug = Config.DEBUG;

    public static void isError(Log log, String string, Exception e) {
        if (_debug) {
            log.error(string, e);
        }
    }
}
