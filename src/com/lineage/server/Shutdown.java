package com.lineage.server;

import com.lineage.echo.ClientExecutor;
import com.lineage.echo.QuitGame;
import com.lineage.list.OnlineUser;
import com.lineage.server.datatables.lock.AccountReading;
import com.lineage.server.datatables.lock.ServerReading;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.OpcodesServer;
import com.lineage.server.serverpackets.S_Disconnect;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Account;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.world.World;
import java.util.Collection;
import java.util.Iterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Shutdown extends Thread {
    public static final int ABORT = 3;
    public static final int GM_RESTART = 2;
    public static final int GM_SHUTDOWN = 1;
    public static boolean SHUTDOWN = false;
    public static final int SIGTERM = 0;
    private static Shutdown _counterInstance = null;
    private static Shutdown _instance;
    private static boolean _isMsg = false;
    private static final Log _log = LogFactory.getLog(Shutdown.class);
    private int _overTime;
    private int _secondsShut;
    private int _shutdownMode;

    public Shutdown() {
        this._overTime = 5;
        this._secondsShut = -1;
        this._shutdownMode = 0;
    }

    public Shutdown(int seconds, boolean restart) {
        this._overTime = 5;
        this._secondsShut = seconds < 0 ? 0 : seconds;
        if (restart) {
            this._shutdownMode = 2;
        } else {
            this._shutdownMode = 1;
        }
    }

    public static Shutdown getInstance() {
        if (_instance == null) {
            _instance = new Shutdown();
        }
        return _instance;
    }

    public void run() {
        if (this != _instance) {
            countdown();
            if (this._shutdownMode != 3) {
                switch (this._shutdownMode) {
                    case 1:
                        _instance.setMode(1);
                        System.exit(0);
                        return;
                    case 2:
                        _instance.setMode(2);
                        System.exit(1);
                        return;
                    default:
                        return;
                }
            }
        } else if (this._shutdownMode != 3) {
            try {
                saveData();
                Thread.sleep(3000);
                while (this._overTime > 0) {
                    _log.warn("距離核心完全關閉 : " + this._overTime + "秒!");
                    this._overTime--;
                    Thread.sleep(1000);
                }
                int list = OnlineUser.get().size();
                _log.warn("核心關閉殘餘連線帳號數量: " + list);
                Thread.sleep(1000);
                if (list > 0) {
                    Iterator<String> it = OnlineUser.get().map().keySet().iterator();
                    while (it.hasNext()) {
                        _log.warn("核心關閉殘留帳號: " + it.next());
                    }
                }
            } catch (InterruptedException e) {
                _log.error(e.getLocalizedMessage(), e);
            }
        }
    }

    public void startShutdown(L1PcInstance activeChar, int seconds, boolean restart) {
        if (_counterInstance == null) {
            if (activeChar != null) {
                _log.warn(activeChar.getName() + " 啟動關機計時: " + seconds + " 秒!");
            }
            if (_counterInstance != null) {
                _counterInstance._abort();
            }
            _counterInstance = new Shutdown(seconds, restart);
            GeneralThreadPool.get().execute(_counterInstance);
        }
    }

    public void abort(L1PcInstance activeChar) {
        if (_counterInstance != null) {
            _counterInstance._abort();
        }
    }

    private void setMode(int mode) {
        this._shutdownMode = mode;
    }

    private void _abort() {
        this._shutdownMode = 3;
    }

    private void countdown() {
        try {
            SHUTDOWN = true;
            World.get().broadcastPacketToAll(new S_ServerMessage(72, String.valueOf(this._secondsShut)));
            while (this._secondsShut > 0) {
                switch (this._secondsShut) {
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 10:
                    case 30:
                    case 60:
                    case OpcodesServer.S_OPCODE_STRUP /*{ENCODED_INT: 120}*/:
                    case 150:
                        _log.warn("關機倒數: " + this._secondsShut + " 秒!");
                        World.get().broadcastPacketToAll(new S_ServerMessage(72, String.valueOf(this._secondsShut)));
                        break;
                    case OpcodesServer.S_OPCODE_INVLIST /*{ENCODED_INT: 180}*/:
                    case 240:
                    case 300:
                        EchoServerTimer.get().stopEcho();
                        break;
                }
                this._secondsShut--;
                Thread.sleep(1000);
                if (this._shutdownMode == 3 && this._secondsShut > 5) {
                    SHUTDOWN = false;
                    EchoServerTimer.get().reStart();
                    World.get().broadcastPacketToAll(new S_ServerMessage(166, "取消關機倒數!!遊戲將會正常執行!!"));
                    if (this._shutdownMode != 3) {
                        saveData();
                        return;
                    }
                    this._secondsShut = -1;
                    this._shutdownMode = 0;
                    _counterInstance = null;
                    return;
                }
            }
            if (this._shutdownMode != 3) {
                saveData();
                return;
            }
            this._secondsShut = -1;
            this._shutdownMode = 0;
            _counterInstance = null;
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            if (this._shutdownMode != 3) {
                saveData();
                return;
            }
            this._secondsShut = -1;
            this._shutdownMode = 0;
            _counterInstance = null;
        } catch (Throwable th) {
            if (this._shutdownMode != 3) {
                saveData();
            } else {
                this._secondsShut = -1;
                this._shutdownMode = 0;
                _counterInstance = null;
            }
            throw th;
        }
    }

    private synchronized void saveData() {
        try {
            World.get().broadcastPacketToAll(new S_Disconnect());
            Collection<ClientExecutor> list = OnlineUser.get().all();
            if (!_isMsg) {
                _isMsg = true;
                _log.info("人物/物品 資料的存檔 - 關閉核心前連線帳號數量: " + list.size());
            }
            for (ClientExecutor client : list) {
                L1PcInstance tgpc = client.getActiveChar();
                if (tgpc != null) {
                    QuitGame.quitGame(tgpc);
                    client.setActiveChar(null);
                    client.kick();
                    client.close();
                }
                L1Account value = client.getAccount();
                if (value != null) {
                    if (value.is_isLoad()) {
                        OnlineUser.get().remove(client.getAccountName());
                    }

                    value.set_isLoad(false);
                    AccountReading.get().updateLan(client.getAccountName(), false);
                }
            }
            OnlineUser.get().kickAll();
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            AccountReading.get().updateLan();
            ServerReading.get().isStop();
        }
    }
}
