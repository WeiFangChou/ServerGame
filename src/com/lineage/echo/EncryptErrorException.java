package com.lineage.echo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class EncryptErrorException extends Exception {
    private static final Log _log = LogFactory.getLog(EncryptErrorException.class);
    private static final long serialVersionUID = 1;

    public EncryptErrorException() {
    }

    public EncryptErrorException(String string) {
        _log.error(string);
    }

    public EncryptErrorException(String string, Exception e) {
        _log.error(string, e);
    }
}
