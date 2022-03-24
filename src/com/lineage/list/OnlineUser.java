package com.lineage.list;

import com.lineage.config.Config;
import com.lineage.echo.ClientExecutor;
import com.lineage.server.datatables.lock.AccountReading;
import com.lineage.server.templates.L1Account;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class OnlineUser {
    private static OnlineUser _instance;
    private static final Log _log = LogFactory.getLog(OnlineUser.class);
    private Collection<ClientExecutor> _allClient;
    private Collection<String> _allValues;
    private final Map<String, ClientExecutor> _clientList = new ConcurrentHashMap();

    public static OnlineUser get() {
        if (_instance == null) {
            _instance = new OnlineUser();
        }
        return _instance;
    }

    private OnlineUser() {
    }

    public boolean addClient(L1Account value, ClientExecutor client) {
        String accountName = value.get_login();
        ClientExecutor xclient = this._clientList.get(accountName);
        if (xclient == null) {
            client.setAccount(value);
            value.set_isLoad(true);
            value.set_server_no(Config.SERVERNO);
            AccountReading.get().updateLan(accountName, true);
            this._clientList.put(accountName, client);
            _log.info("帳號登入: " + value.get_login() + " 目前連線帳號: " + this._clientList.size());
            return true;
        }
        xclient.kick();
        client.kick();
        _log.error("連線列表中重複資料: " + value.get_login() + "\n");
        return false;
    }

    public boolean isLan(String accountName) {
        if (this._clientList.get(accountName) != null) {
            return true;
        }
        return false;
    }

    public void remove(String accountName) {
        ClientExecutor xclient = this._clientList.get(accountName);
        if (xclient != null) {
            L1Account value = xclient.getAccount();
            value.set_isLoad(false);
            value.set_server_no(0);
            AccountReading.get().updateLan(accountName, false);
            this._clientList.remove(accountName);
        }
    }

    public ClientExecutor get(String accountName) {
        return this._clientList.get(accountName);
    }

    public Map<String, ClientExecutor> map() {
        return this._clientList;
    }

    public Collection<ClientExecutor> all() {
        try {
            Collection<ClientExecutor> vs = this._allClient;
            if (vs != null) {
                return vs;
            }
            Collection<ClientExecutor> vs2 = Collections.unmodifiableCollection(this._clientList.values());
            this._allClient = vs2;
            return vs2;
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    public Collection<String> getObject() {
        Collection<String> vs = this._allValues;
        if (vs != null) {
            return vs;
        }
        Collection<String> vs2 = Collections.unmodifiableCollection(this._clientList.keySet());
        this._allValues = vs2;
        return vs2;
    }

    public boolean isMax() {
        if (this._clientList.size() >= Config.MAX_ONLINE_USERS) {
            return true;
        }
        return false;
    }

    public int size() {
        return this._clientList.size();
    }

    public void kickAll() {
        for (String acc : this._clientList.keySet()) {
            remove(acc);
        }
    }
}
