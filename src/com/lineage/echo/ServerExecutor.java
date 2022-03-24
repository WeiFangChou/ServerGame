package com.lineage.echo;

import com.lineage.commons.system.IpAttackCheck;
import com.lineage.commons.system.LanSecurityManager;
import com.lineage.config.Config;
import com.lineage.config.ConfigIpCheck;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.utils.StreamUtil;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.logging.SocketHandler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ServerExecutor extends Thread {
    private static final Log _log = LogFactory.getLog(ServerExecutor.class);
    private ServerSocket _server;
    private int _port = 0;
    private static final String _t1 = "\n\r--------------------------------------------------";
    private static final String _t2 = "\n\r--------------------------------------------------";

    public ServerExecutor(int port) {
        try {
            this._port = port;
            if (!"*".equals(Config.GAME_SERVER_HOST_NAME)) {
                InetAddress inetaddress = InetAddress.getByName(Config.GAME_SERVER_HOST_NAME);
                this._server = new ServerSocket(this._port, 50, inetaddress);
            } else {
                this._server = new ServerSocket(this._port);
            }
        } catch (SocketTimeoutException var7) {
            _log.fatal("連線超時:(" + this._port + ")", var7);
        } catch (IOException var8) {
            _log.fatal("IP位置加載錯誤 或 端口位置已被佔用:(" + this._port + ")", var8);
        } finally {
            _log.info("[D] " + this.getClass().getSimpleName() + " 開始監聽服務端口:(" + this._port + ")");
        }

    }

    public void stsrtEcho() throws IOException {
        GeneralThreadPool.get().execute(this);
    }

    public void run() {
        try {
            while (_server != null) {
                Socket socket= null;
                try {
                    socket = _server.accept();

                    if (socket != null) {
                        // 性能偏好
                        //connectionTime - 表达短连接时间的相对重要性的 int
                        //latency - 表达低延迟的相对重要性的 int
                        //bandwidth - 表达高带宽的相对重要性的 int
                        //socket.setPerformancePreferences(1, 0, 0);
                        //socket.setSoTimeout(120000);// 用戶端2分鐘無反應中斷

                        String ipaddr = socket.getInetAddress().getHostAddress();
                        // log4j
                        String info =
                                _t1
                                        + "\n       客戶端 連線遊戲伺服器 服務端口:(" + _port + ")"
                                        + "\n       " + ipaddr
                                        + _t2;

                        _log.info(info);
//                        // 計時器
//                        ClientExecutor client = new ClientExecutor(socket);
//                        GeneralThreadPool.get().execute(client);

                        try {
                            // 使用線程模式進行處理，防止1個accept產生錯誤后卡死
                            final SocketHandler sh = new SocketHandler(socket, this._port);
                            sh.startHandler();
                        } catch (final Exception e) {
                            // 拋出的異常不包括IO異常，給予放行
                        }
                    }

                } catch (final SecurityException e) {
                    _log.warn(e.getLocalizedMessage());
                }
            }

        } catch (IOException e) {
            _log.error("Server Exc : "+e.getLocalizedMessage(), e);

        } finally {
            final String lanInfo = "[D] " + getClass().getSimpleName() + " 伺服器核心關閉監聽端口(" + _port + ")";

            _log.warn(lanInfo);
        }
    }

    public void stopEcho() {
        try {
            if (this._server != null) {
                StreamUtil.close(this._server);
                StreamUtil.interrupt(this);
                this._server = null;
            }
        } catch (Exception var2) {
            _log.error(var2.getLocalizedMessage(), var2);
        }

    }

    private class SocketHandler implements Runnable {

        private Socket _socket = null;

        /**
         * 初始化
         * @param socket
         * @param port
         * */
        public SocketHandler(final Socket socket, final int port) {
            this._socket = socket;
        }

        /**
         * 開始處理線程<BR>
         * 運行過程中的IO錯誤會被全程捕捉<BR>
         * 拋出線程錯誤必須調用時捕獲處理<BR>
         * */
        public void startHandler() {
            GeneralThreadPool.get().execute(this);
        }

        @Override
        public void run() {
            try {
                final String ipAddr = this._socket.getInetAddress().getHostAddress(); // 獲取IP地址
                // LanSecurityManager.LOADMAP.put(ipaddr, 15); // 客戶端 連線遊戲伺服器 加入禁用位置
                String info = null; // 連線資訊初始化
                // 獲取MAC地址
                StringBuilder macStr = null;

                if (ipAddr != null && LanSecurityManager.BANIPMAP.containsKey(ipAddr)) {
                    _log.warn(new StringBuilder().append("禁止登入位置(IP封锁): host=").append(ipAddr).toString());
                    StreamUtil.close(this._socket);
//				} else if (macStr != null && LanSecurityManager.BANIPMAP.containsKey(macStr)) {
//					_log.warn(new StringBuilder().append("禁止登入位置(MAC封锁): mac=").append(ipAddr).toString());
//					StreamUtil.close(this._socket);
                } else {

                    this._socket.setSoTimeout(180000); // socket超時解除
                    final ClientExecutor client = new ClientExecutor(this._socket);
                    if (ConfigIpCheck.IPCHECK) {
                        IpAttackCheck.SOCKETLIST.put(client, ipAddr);
                    }
                    GeneralThreadPool.get().execute(client);
                }
            } catch (final IOException ioe) {
                StreamUtil.close(this._socket);
            }
        }
    }
}
