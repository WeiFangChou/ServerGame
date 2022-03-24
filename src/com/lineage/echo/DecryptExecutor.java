package com.lineage.echo;

import com.lineage.config.Config;
import com.lineage.echo.encryptions.Encryption;
import com.lineage.server.utils.StreamUtil;
import java.io.InputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DecryptExecutor {
    private static final Log _log = LogFactory.getLog(DecryptExecutor.class);
    private final ClientExecutor _client;
    private final InputStream _in;
    private Encryption _keys;

    public DecryptExecutor(ClientExecutor client, InputStream in) {
        this._client = client;
        this._keys = client.get_keys();
        this._in = in;
    }

    public ClientExecutor get_client() {
        return this._client;
    }

    public byte[] decrypt() throws Exception {
        try {
            int hiByte = this._in.read();
            int loByte = this._in.read();
            if (loByte < 0) {
                throw new DecryptErrorException();
            }
            if (Config.LOGINS_TO_AUTOENTICATION) {
                hiByte ^= this._client._xorByte;
                loByte ^= this._client._xorByte;
            }
            int dataLength = ((loByte << 8) + hiByte) - 2;
            byte[] data = new byte[dataLength];
            int readSize = 0;
            int i = 0;
            while (i != -1 && readSize < dataLength) {
                i = this._in.read(data, readSize, dataLength - readSize);
                readSize += i;
            }
            if (readSize != dataLength) {
                throw new RuntimeException();
            }
            if (Config.LOGINS_TO_AUTOENTICATION) {
                for (int i2 = 0; i2 < dataLength; i2++) {
                    data[i2] = (byte) (data[i2] ^ this._client._xorByte);
                }
            }
            return this._keys.decrypt(data);
        } catch (Exception e) {
            throw new DecryptErrorException();
        }
    }

    public void stop() {
        try {
            StreamUtil.close(this._in);
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
