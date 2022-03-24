package com.lineage.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ConfigErrorException extends Exception {
    private static final Log _log = LogFactory.getLog(ConfigErrorException.class);
    private static final long serialVersionUID = 1;

    public ConfigErrorException() {
    }

    public ConfigErrorException(String string) {
        _log.error(string);
    }
}
