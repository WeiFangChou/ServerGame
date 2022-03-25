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
/**
 * 连线用户管理
 *
 * @author dexc
 *
 */
public class OnlineUser {
    private static OnlineUser _instance;
    private static final Log _log = LogFactory.getLog(OnlineUser.class);
    private Collection<ClientExecutor> _allClient;
    private Collection<String> _allValues;
    private Map<String, ClientExecutor> _clientList;

    public static OnlineUser get() {
        if (_instance == null) {
            _instance = new OnlineUser();
        }
        return _instance;
    }

    private OnlineUser() {
        _clientList = new ConcurrentHashMap<String, ClientExecutor>();
    }
    /**
     * 增加连线用户资料
     *
     * @param value
     *            帐号
     * @param client
     *            连线线程
     * @return
     */
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
        }else {
            xclient.kick();
            client.kick();
            _log.error("連線列表中重複資料: " + value.get_login() + "\n");
            return false;
        }
    }
    /**
     * 用户连线中
     *
     * @param accountName
     * @return
     */
    public boolean isLan(String accountName) {
        if (this._clientList.get(accountName) != null) {
            return true;
        }
        return false;
    }
    /**
     * 移除连线用户资料
     *
     * @param accountName
     */
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
    /**
     * 取回连线用户 ClientThread 资料
     *
     * @param accountName
     * @return 该帐户未连线 传回NULL
     */
    public ClientExecutor get(String accountName) {
        return this._clientList.get(accountName);
    }
    /**
     * 全部连线用户(Map)
     *
     * @return
     */
    public Map<String, ClientExecutor> map() {
        return this._clientList;
    }
    /**
     * 全部连线用户(Collection)
     *
     * @return
     */
    public Collection<ClientExecutor> all() {
        try {
            Collection<ClientExecutor> vs = _allClient;
            return (vs != null) ? vs :  Collections.unmodifiableCollection(_clientList.values());
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);

        }
        return null;
    }
    /**
     * 传回全部连线中帐户
     *
     * @return
     */
    public Collection<String> getObject() {
        Collection<String> vs = this._allValues;
        return  (vs != null) ? vs : Collections.unmodifiableCollection(this._clientList.keySet());
    }
    /**
     * 是否已达最大连线数量
     *
     * @return
     */
    public boolean isMax() {
        if (this._clientList.size() >= Config.MAX_ONLINE_USERS) {
            return true;
        }
        return false;
    }
    /**
     * 连线数量
     *
     * @return
     */
    public int size() {
        return this._clientList.size();
    }
    /**
     * 中断全部用户
     *
     * @return
     */
    public void kickAll() {
        for (String acc : this._clientList.keySet()) {
            remove(acc);
        }
    }
}
