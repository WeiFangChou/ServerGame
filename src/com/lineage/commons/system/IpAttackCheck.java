package com.lineage.commons.system;

import com.lineage.config.ConfigIpCheck;
import com.lineage.echo.ClientExecutor;
import com.lineage.server.datatables.lock.IpReading;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class IpAttackCheck {
    public static final Map<ClientExecutor, String> SOCKETLIST = new ConcurrentHashMap();
    private static IpAttackCheck _instance;
    private static final HashMap<String, IpTemp> _ipList = new HashMap<>();
    private static final Log _log = LogFactory.getLog(IpAttackCheck.class);

    /* access modifiers changed from: private */
    public class IpTemp {
        int _count;
        long _time;

        private IpTemp() {
        }

        /* synthetic */ IpTemp(IpAttackCheck ipAttackCheck, IpTemp ipTemp) {
            this();
        }
    }

    public static IpAttackCheck get() {
        if (_instance == null) {
            _instance = new IpAttackCheck();
        }
        return _instance;
    }

    private IpAttackCheck() {
        _ipList.clear();
    }

    public boolean check(String key) {
        try {
            long time = System.currentTimeMillis();
            IpTemp value = _ipList.get(key);
            if (value == null) {
                IpTemp value2 = new IpTemp(this, null);
                value2._time = time;
                value2._count = 1;
                _ipList.put(key, value2);
                return true;
            }
            if (time - value._time <= ((long) ConfigIpCheck.TIMEMILLIS)) {
                value._count++;
            }
            value._time = time;
            if (value._count < ConfigIpCheck.COUNT) {
                return true;
            }
            kick(key);
            if (!ConfigIpCheck.SETDB) {
                return true;
            }
            IpReading.get().add(key, "IP類似攻擊行為");
            return false;
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            return true;
        }
    }

    private void kick(String key) {
        try {
            for (ClientExecutor socket : SOCKETLIST.keySet()) {
                String ip = SOCKETLIST.get(socket);
                if (ip != null && ip.equals(key)) {
                    socket.close();
                }
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
