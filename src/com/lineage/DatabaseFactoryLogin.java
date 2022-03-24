package com.lineage;

import com.lineage.config.ConfigSQL;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DatabaseFactoryLogin {
    private static String _driver;
    private static DatabaseFactoryLogin _instance;
    private static final Log _log = LogFactory.getLog(DatabaseFactoryLogin.class);
    private static String _password;
    private static String _url;
    private static String _user;
    private ComboPooledDataSource _source;

    public static void setDatabaseSettings() {
        _driver = ConfigSQL.DB_DRIVER;
        _url = String.valueOf(ConfigSQL.DB_URL1_LOGIN) + ConfigSQL.DB_URL2_LOGIN + ConfigSQL.DB_URL3_LOGIN;
        _user = ConfigSQL.DB_LOGIN_LOGIN;
        _password = ConfigSQL.DB_PASSWORD_LOGIN;
    }

    public DatabaseFactoryLogin() throws SQLException {
        try {
            this._source = new ComboPooledDataSource();
            this._source.setDriverClass(_driver);
            this._source.setJdbcUrl(_url);
            this._source.setUser(_user);
            this._source.setPassword(_password);
            this._source.getConnection().close();
        } catch (SQLException e) {
            _log.fatal("資料庫讀取錯誤!"+_driver+" "+_url+" "+ _user+ " "+ _password, e);
        } catch (Exception e2) {
            _log.fatal("資料庫讀取錯誤!"+_driver+" "+_url+" "+ _user+ " "+ _password, e2);
        }
    }

    public void shutdown() {
        try {
            this._source.close();
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        try {
            this._source = null;
        } catch (Exception e2) {
            _log.error(e2.getLocalizedMessage(), e2);
        }
    }

    public static DatabaseFactoryLogin get() throws SQLException {
        if (_instance == null) {
            _instance = new DatabaseFactoryLogin();
        }
        return _instance;
    }

    public Connection getConnection() {
        Connection con = null;
        while (con == null) {
            try {
                con = this._source.getConnection();
            } catch (SQLException e) {
                _log.error(e.getLocalizedMessage()+con.getClass(), e);
            }
        }
        return con;
    }
}
