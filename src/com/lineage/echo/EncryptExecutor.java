package com.lineage.echo;

import com.lineage.config.Config;
import com.lineage.echo.encryptions.Encryption;
import com.lineage.server.serverpackets.OpcodesServer;
import com.lineage.server.serverpackets.ServerBasePacket;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.utils.StreamUtil;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class EncryptExecutor extends OpcodesServer {
    private static final Log _log = LogFactory.getLog(EncryptExecutor.class);
    private final ClientExecutor _client;
    private Encryption _keys;
    private final OutputStream _out;
    private final PacketSc _scPacket;

    public EncryptExecutor(ClientExecutor client, OutputStream out) {
        this._client = client;
        this._keys = client.get_keys();
        this._out = new BufferedOutputStream(out);
        this._scPacket = new PacketSc(client, this);
    }

    public void outStart() {
        try {
            synchronized (this) {
                if (this._out != null) {
                    if (Config.LOGINS_TO_AUTOENTICATION) {
                        this._out.write((int) (this._client._authdata & 255));
                        this._out.write((int) ((this._client._authdata >> 8) & 255));
                        this._out.write((int) ((this._client._authdata >> 16) & 255));
                        this._out.write((int) ((this._client._authdata >> 24) & 255));
                        this._out.flush();
                    }
                    this._out.write(OpcodesClient._firstPacket);
                    this._out.flush();
                    try {
                        this._keys.initKeys(994303243);
                    } catch (Exception e) {
                        stop();
                        throw new EncryptErrorException("設置加密公式發生異常: " + ((Object) this._client.getIp()), e);
                    }
                }
            }
        } catch (Exception ignored) {
            _log.error("EncryptEx Error : "+ignored.getMessage());
        }
    }

    public void encrypt(ServerBasePacket packet) {
        if (packet != null) {
            try {
                synchronized (this) {
                    this._scPacket.encrypt(packet);
                }
            } catch (Exception e) {
                _log.error(e.getLocalizedMessage(), e);
            }
        }
    }

    public OutputStream out() {
        return this._out;
    }

    public void satrt() {
        GeneralThreadPool.get().schedule(this._scPacket, 0);
    }

    public void stop() {
        try {
            this._scPacket.stop();
            StreamUtil.close(this._out);
        } catch (Exception ignored) {
        }
    }
}
