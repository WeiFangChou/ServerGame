package com.lineage.server;

import com.lineage.config.Config;
import com.lineage.echo.ServerExecutor;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.thread.GeneralThreadPool;

import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class EchoServerTimer extends TimerTask {
    private static final Map<Integer, ServerExecutor> _echoList = new HashMap();
    private static EchoServerTimer _instance;
    private static final Log _log = LogFactory.getLog(EchoServerTimer.class);

    public static EchoServerTimer get() {
        if (_instance == null) {
            _instance = new EchoServerTimer();
        }
        return _instance;
    }

    public void start() {
        try {
            if (_echoList.isEmpty()) {
                startEcho();
            }
            if (Config.RESTART_LOGIN > 0) {
                _log.warn("監聽端口重置作業啟動 間隔時間:" + Config.RESTART_LOGIN + "分鐘。");
                int timeMillis = Config.RESTART_LOGIN * 60 * L1SkillId.STATUS_BRAVE;
                GeneralThreadPool.get().aiScheduleAtFixedRate(this, (long) timeMillis, (long) timeMillis);
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            if (Config.RESTART_LOGIN > 0) {
                _log.warn("監聽端口重置作業啟動 間隔時間:" + Config.RESTART_LOGIN + "分鐘。");
                int timeMillis2 = Config.RESTART_LOGIN * 60 * L1SkillId.STATUS_BRAVE;
                GeneralThreadPool.get().aiScheduleAtFixedRate(this, (long) timeMillis2, (long) timeMillis2);
            }
        } catch (Throwable th) {
            if (Config.RESTART_LOGIN > 0) {
                _log.warn("監聽端口重置作業啟動 間隔時間:" + Config.RESTART_LOGIN + "分鐘。");
                int timeMillis3 = Config.RESTART_LOGIN * 60 * L1SkillId.STATUS_BRAVE;
                GeneralThreadPool.get().aiScheduleAtFixedRate(this, (long) timeMillis3, (long) timeMillis3);
            }
            throw th;
        }
    }

    public void run() {
        try {
            _log.warn("監聽端口重置作業!");

            stopEcho();
            startEcho();

        } catch (Exception e) {
            _log.error("監聽端口重置作業失敗!!", e);
        } finally {
            _log.warn("監聽端口重置作業完成!!");
        }
    }

    public void reStart() {
        try {
            if (!Shutdown.SHUTDOWN) {
                if (!_echoList.isEmpty()) {
                    stopEcho();
                    Thread.sleep(2000);
                    startEcho();
                    return;
                }
                _log.error("監聽端口重置作業失敗(目前無任何監聽線程)!!");
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public void startEcho() {
        try {
            for (String ports : Config.GAME_SERVER_PORT.split("-")) {
                int key = Integer.parseInt(ports);
                ServerExecutor echoServer = new ServerExecutor(key);
                if (echoServer != null) {
                    _echoList.put(key, echoServer);
                    echoServer.stsrtEcho();
                }
                Thread.sleep(100);
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public void stopEcho() {
        try {
            if (!_echoList.isEmpty()) {
                for (Integer key : _echoList.keySet()) {
                    ServerExecutor echoServer = _echoList.get(key);
                    if (echoServer != null) {
                        echoServer.stopEcho();
                    }
                    Thread.sleep(100);
                }
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public boolean isPort(int key) {
        try {
            return _echoList.get(key) != null;
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            return false;
        }
    }

    public void stopPort(int key) {
        try {
            ServerExecutor echoServer = _echoList.get(key);
            if (echoServer != null) {
                echoServer.stopEcho();
                _echoList.remove(key);
                return;
            }
            _log.warn("關閉指定監聽端口 作業失敗:該端口未在作用中!");
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public void startPort(int key) {
        try {
            if (_echoList.get(key) == null) {
                ServerExecutor echoServer = new ServerExecutor(key);
                _echoList.put(key, echoServer);
                echoServer.stsrtEcho();
                return;
            }
            _log.warn("啟用指定監聽端口 作業失敗:該端口已在作用中!");
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
