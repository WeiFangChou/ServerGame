package com.lineage.commons.system;

import com.lineage.config.ConfigIpCheck;
import com.lineage.server.thread.GeneralThreadPool;
import java.io.IOException;
import java.security.Permission;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LanSecurityManager extends SecurityManager {
    public static final Map<String, Integer> BANIPMAP = new HashMap();
    public static final Map<String, Integer> BANIPPACK = new ConcurrentHashMap();
    public static final Map<String, Integer> BANNAMEMAP = new HashMap();
    public static final Map<String, Integer> ONEIPMAP = new ConcurrentHashMap();
    public static final Map<String, Long> ONETIMEMILLISMAP = new ConcurrentHashMap();
    private static final Log _log = LogFactory.getLog(LanSecurityManager.class);

    public void checkAccept(String host, int port) {
        if (ConfigIpCheck.IPCHECKPACK) {
            if (BANIPMAP.containsKey(host)) {
                throw new SecurityException();
            } else if (BANIPPACK.containsKey(host)) {
                throw new SecurityException();
            }
        } else if (BANIPMAP.containsKey(host)) {
            throw new SecurityException();
        } else if (ONEIPMAP.containsKey(host)) {
            throw new SecurityException();
        } else if (ONETIMEMILLISMAP.containsKey(host)) {
            throw new SecurityException();
        } else {
            if (ConfigIpCheck.ONETIMEMILLIS != 0) {
                ONETIMEMILLISMAP.put(host, Long.valueOf(System.currentTimeMillis()));
            }
            if (ConfigIpCheck.ISONEIP) {
                ONEIPMAP.put(host, Integer.valueOf(port));
            }
            if (ConfigIpCheck.IPCHECK && !IpAttackCheck.get().check(host)) {
                throw new SecurityException();
            }
        }
    }

    @Override // java.lang.SecurityManager
    public void checkAccess(Thread t) {
    }

    public void checkPermission(Permission perm) {
    }

    public void stsrt_cmd() throws IOException {
        GeneralThreadPool.get().execute(new RemoveIp(ConfigIpCheck.ONETIMEMILLIS));
    }

    public void stsrt_cmd_tmp() throws IOException {
        GeneralThreadPool.get().execute(new RemoveTmpIp(this, null));
    }

    private class RemoveTmpIp implements Runnable {
        private RemoveTmpIp() {
        }

        /* synthetic */ RemoveTmpIp(LanSecurityManager lanSecurityManager, RemoveTmpIp removeTmpIp) {
            this();
        }

        public void run() {
            while (true) {
                try {
                    Thread.sleep(1000);
                    if (!LanSecurityManager.BANIPPACK.isEmpty()) {
                        for (String ip : LanSecurityManager.BANIPPACK.keySet()) {
                            int time = LanSecurityManager.BANIPPACK.get(ip).intValue() - 1;
                            if (time <= 0) {
                                LanSecurityManager.BANIPPACK.remove(ip);
                            } else {
                                LanSecurityManager.BANIPPACK.put(ip, Integer.valueOf(time));
                            }
                        }
                    }
                } catch (Exception e) {
                    LanSecurityManager._log.error(e.getLocalizedMessage(), e);
                    return;
                }
            }
        }
    }

    private class RemoveIp implements Runnable {
        public int _time = 60000;

        public RemoveIp(int oNETIMEMILLIS) {
            this._time = oNETIMEMILLIS;
        }

        public void run() {
            while (true) {
                try {
                    Thread.sleep(10000);
                    if (!LanSecurityManager.ONETIMEMILLISMAP.isEmpty()) {
                        for (String ip : LanSecurityManager.ONETIMEMILLISMAP.keySet()) {
                            if (System.currentTimeMillis() - LanSecurityManager.ONETIMEMILLISMAP.get(ip).longValue() >= ((long) this._time)) {
                                LanSecurityManager.ONETIMEMILLISMAP.remove(ip);
                            }
                        }
                    }
                } catch (Exception e) {
                    LanSecurityManager._log.error(e.getLocalizedMessage(), e);
                    return;
                }
            }
        }
    }
}
