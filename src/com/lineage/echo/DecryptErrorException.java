package com.lineage.echo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DecryptErrorException extends Exception {
    private static final Log _log = LogFactory.getLog(DecryptErrorException.class);
    private static final long serialVersionUID = 1;

    public DecryptErrorException() {
    }

    public DecryptErrorException(String string) {
        _log.error(string);
    }

    public DecryptErrorException(String string, Exception e) {
        _log.error(string, e);
    }
}
