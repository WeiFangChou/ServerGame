package com.lineage.echo;

import com.lineage.config.Config;
import com.lineage.echo.encryptions.Encryption;
import com.lineage.echo.encryptions.PacketPrint;
import com.lineage.server.serverpackets.ServerBasePacket;
import com.lineage.server.types.UByte8;
import com.lineage.server.types.UChar8;
import java.io.IOException;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PacketSc implements Runnable {
    private static final Log _log = LogFactory.getLog(PacketSc.class);
    private static boolean _debug = Config.DEBUG;
    private final Queue<byte[]> _queue;
    private final ClientExecutor _client;
    private final EncryptExecutor _executor;
    private Encryption _keys;


    public PacketSc(ClientExecutor client, EncryptExecutor executor) {
        this._client = client;
        this._keys = client.get_keys();
        this._executor = executor;
        this._queue = new ConcurrentLinkedQueue();
    }

    private void requestWork(byte[] data) {
        this._queue.offer(data);
    }

    public void encrypt(ServerBasePacket packet) throws Exception {
        byte[] encrypt = packet.getContent();
        if (encrypt.length > 0 && this._executor.out() != null) {
            if (Config.DEBUG) {
                _log.info("服務端: " + packet.getType() + "\nOP ID: " + (encrypt[0] & 255) + "\nInfo:\n" + PacketPrint.get().printData(encrypt, encrypt.length));
            }

            char[] ac;
            ac = UChar8.fromArray(encrypt);
            ac = this._keys.encrypt(ac);
            if (ac == null) {
                return;
            }

            encrypt = UByte8.fromArray(ac);
            this.requestWork(encrypt);
        }

    }

    public void run() {
        try {
            while (_client.get_socket() != null) {
                for (final Iterator<byte[]> iter = _queue.iterator(); iter.hasNext();) {
                    final byte[] decrypt = iter.next();// 返回迭代的下一个元素。
                    // 从迭代器指向的 collection 中移除迭代器返回的最后一个元素
                    iter.remove();
                    outPacket(decrypt);
                    Thread.sleep(1);
                }
                // 队列为空 休眠
                Thread.sleep(10);
            }
            // finalize();

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);

            /*
             * } catch (Throwable e) { _log.error(e.getLocalizedMessage(), e);//
             */

        } finally {
            // 移除此 collection 中的所有元素
            _queue.clear();
        }
    }

    private void outPacket(byte[] decrypt) {
        try {
            int outLength = decrypt.length + 2;
            this._executor.out().write(outLength & 255);
            this._executor.out().write(outLength >> 8 & 255);
            this._executor.out().write(decrypt);
            this._executor.out().flush();
        } catch (IOException var3) {
            this._executor.stop();
        }

    }

    public void stop() {
        this._queue.clear();
    }
}
