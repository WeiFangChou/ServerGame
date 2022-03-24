package com.lineage.server.timecontroller.event.ranking;

import com.lineage.DatabaseFactory;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.utils.SQLUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class RankingHeroTimer extends TimerTask {
    private static RankingHeroTimer _instance;
    private static boolean _load;
    private static final Log _log = LogFactory.getLog(RankingHeroTimer.class);
    private static String[] _userNameAll;
    private static String[] _userNameC;
    private static String[] _userNameD;
    private static String[] _userNameE;
    private static String[] _userNameG;
    private static String[] _userNameI;
    private static String[] _userNameK;
    private static String[] _userNameW;
    private ScheduledFuture<?> _timer;

    public static RankingHeroTimer get() {
        if (_instance == null) {
            _instance = new RankingHeroTimer();
        }
        return _instance;
    }

    public void start() {
        restart();
        this._timer = GeneralThreadPool.get().scheduleAtFixedRate((TimerTask) this, 600000L, 600000L);
    }

    public static String[] userNameAll() {
        if (!_load) {
            load();
        }
        return _userNameAll;
    }

    public static String[] userNameC() {
        if (!_load) {
            load();
        }
        return _userNameC;
    }

    public static String[] userNameK() {
        if (!_load) {
            load();
        }
        return _userNameK;
    }

    public static String[] userNameE() {
        if (!_load) {
            load();
        }
        return _userNameE;
    }

    public static String[] userNameW() {
        if (!_load) {
            load();
        }
        return _userNameW;
    }

    public static String[] userNameD() {
        if (!_load) {
            load();
        }
        return _userNameD;
    }

    public static String[] userNameG() {
        if (!_load) {
            load();
        }
        return _userNameG;
    }

    public static String[] userNameI() {
        if (!_load) {
            load();
        }
        return _userNameI;
    }

    public void run() {
        try {
            load();
        } catch (Exception e) {
            _log.error("英雄風雲榜時間軸異常重啟", e);
            GeneralThreadPool.get().cancel(this._timer, false);
            new RankingHeroTimer().start();
        }
    }

    public static void load() {
        try {
            _load = true;
            restart();
            Connection con = null;
            PreparedStatement pstm = null;
            ResultSet rs = null;
            try {
                con = DatabaseFactory.get().getConnection();
                pstm = con.prepareStatement("SELECT * FROM `characters` WHERE `level` > 0 and `AccessLevel` <= 0 ORDER BY `level` DESC,`Exp` DESC");
                rs = pstm.executeQuery();
                while (rs.next()) {
                    String char_name = rs.getString("char_name");
                    int level = rs.getInt("level");
                    int type = rs.getInt("Type");
                    if (type == 0) {
                        int i = 0;
                        while (true) {
                            if (i >= 10) {
                                break;
                            } else if (_userNameC[i].equals(" ")) {
                                StringBuffer sbr = new StringBuffer().append(char_name);
                                sbr.append(" (Lv.").append(level).append(")");
                                _userNameC[i] = sbr.toString();
                                break;
                            } else {
                                i++;
                            }
                        }
                    } else if (type == 1) {
                        int i2 = 0;
                        while (true) {
                            if (i2 >= 10) {
                                break;
                            } else if (_userNameK[i2].equals(" ")) {
                                StringBuffer sbr2 = new StringBuffer().append(char_name);
                                sbr2.append(" (Lv.").append(level).append(")");
                                _userNameK[i2] = sbr2.toString();
                                break;
                            } else {
                                i2++;
                            }
                        }
                    } else if (type == 2) {
                        int i3 = 0;
                        while (true) {
                            if (i3 >= 10) {
                                break;
                            } else if (_userNameE[i3].equals(" ")) {
                                StringBuffer sbr3 = new StringBuffer().append(char_name);
                                sbr3.append(" (Lv.").append(level).append(")");
                                _userNameE[i3] = sbr3.toString();
                                break;
                            } else {
                                i3++;
                            }
                        }
                    } else if (type == 3) {
                        int i4 = 0;
                        while (true) {
                            if (i4 >= 10) {
                                break;
                            } else if (_userNameW[i4].equals(" ")) {
                                StringBuffer sbr4 = new StringBuffer().append(char_name);
                                sbr4.append(" (Lv.").append(level).append(")");
                                _userNameW[i4] = sbr4.toString();
                                break;
                            } else {
                                i4++;
                            }
                        }
                    } else if (type == 4) {
                        int i5 = 0;
                        while (true) {
                            if (i5 >= 10) {
                                break;
                            } else if (_userNameD[i5].equals(" ")) {
                                StringBuffer sbr5 = new StringBuffer().append(char_name);
                                sbr5.append(" (Lv.").append(level).append(")");
                                _userNameD[i5] = sbr5.toString();
                                break;
                            } else {
                                i5++;
                            }
                        }
                    } else if (type == 5) {
                        int i6 = 0;
                        while (true) {
                            if (i6 >= 10) {
                                break;
                            } else if (_userNameG[i6].equals(" ")) {
                                StringBuffer sbr6 = new StringBuffer().append(char_name);
                                sbr6.append(" (Lv.").append(level).append(")");
                                _userNameG[i6] = sbr6.toString();
                                break;
                            } else {
                                i6++;
                            }
                        }
                    } else if (type == 6) {
                        int i7 = 0;
                        while (true) {
                            if (i7 >= 10) {
                                break;
                            } else if (_userNameI[i7].equals(" ")) {
                                StringBuffer sbr7 = new StringBuffer().append(char_name);
                                sbr7.append(" (Lv.").append(level).append(")");
                                _userNameI[i7] = sbr7.toString();
                                break;
                            } else {
                                i7++;
                            }
                        }
                    }
                    int i8 = 0;
                    while (true) {
                        if (i8 >= 10) {
                            break;
                        } else if (_userNameAll[i8].equals(" ")) {
                            StringBuffer sbr8 = new StringBuffer().append(char_name);
                            sbr8.append(" (Lv.").append(level).append(")");
                            _userNameAll[i8] = sbr8.toString();
                            break;
                        } else {
                            i8++;
                        }
                    }
                    Thread.sleep(1);
                }
            } catch (SQLException e) {
                _log.error(e.getLocalizedMessage(), e);
            } finally {
                SQLUtil.close(rs);
                SQLUtil.close(pstm);
                SQLUtil.close(con);
            }
        } catch (Exception e2) {
            _log.error(e2.getLocalizedMessage(), e2);
        }
    }

    private static void restart() {
        _userNameAll = new String[]{" ", " ", " ", " ", " ", " ", " ", " ", " ", " "};
        _userNameC = new String[]{" ", " ", " ", " ", " ", " ", " ", " ", " ", " "};
        _userNameK = new String[]{" ", " ", " ", " ", " ", " ", " ", " ", " ", " "};
        _userNameE = new String[]{" ", " ", " ", " ", " ", " ", " ", " ", " ", " "};
        _userNameW = new String[]{" ", " ", " ", " ", " ", " ", " ", " ", " ", " "};
        _userNameD = new String[]{" ", " ", " ", " ", " ", " ", " ", " ", " ", " "};
        _userNameG = new String[]{" ", " ", " ", " ", " ", " ", " ", " ", " ", " "};
        _userNameI = new String[]{" ", " ", " ", " ", " ", " ", " ", " ", " ", " "};
    }
}
