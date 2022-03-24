package com.lineage.server.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLUtil {
    public static SQLException close(Connection cn) {
        if (cn != null) {
            try {
                cn.close();
            } catch (SQLException e) {
                return e;
            }
        }
        return null;
    }

    public static SQLException close(Statement ps) {
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                return e;
            }
        }
        return null;
    }

    public static SQLException close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                return e;
            }
        }
        return null;
    }

    public static void close(ResultSet rs, Statement ps, Connection cn) {
        close(rs);
        close(ps);
        close(cn);
    }
}
