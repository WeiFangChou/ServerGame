package com.lineage.server.timecontroller.event.ranking;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.world.World;
import java.util.Collection;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class RankingKillTimer extends TimerTask {
    private static String[] _deathName = {" ", " ", " ", " ", " ", " ", " ", " ", " ", " "};
    private static String[] _killName = {" ", " ", " ", " ", " ", " ", " ", " ", " ", " "};
    private static boolean _load = false;
    private static final Log _log = LogFactory.getLog(RankingKillTimer.class);
    private ScheduledFuture<?> _timer;

    public void start() {
        this._timer = GeneralThreadPool.get().scheduleAtFixedRate((TimerTask) this, 600000L, 600000L);
    }

    public static String[] killName() {
        if (!_load) {
            load();
        }
        return _killName;
    }

    public static String[] deathName() {
        if (!_load) {
            load();
        }
        return _deathName;
    }

    public void run() {
        try {
            load();
        } catch (Exception e) {
            _log.error("殺手風雲榜時間軸異常重啟", e);
            GeneralThreadPool.get().cancel(this._timer, false);
            new RankingKillTimer().start();
        }
    }

    private static void load() {
        try {
            Collection<L1PcInstance> allPc = World.get().getAllPlayers();
            if (!allPc.isEmpty()) {
                _load = true;
                restart();
                for (L1PcInstance tgpc : allPc) {
                    if (!(tgpc == null || tgpc.getOnlineStatus() == 0 || tgpc.getNetConnection() == null)) {
                        int killCount = tgpc.get_other().get_killCount();
                        if (killCount > 0) {
                            _killName = intTree(killCount, tgpc.getName(), _killName);
                        }
                        int deathCount = tgpc.get_other().get_deathCount();
                        if (deathCount > 0) {
                            _deathName = intTree(deathCount, tgpc.getName(), _deathName);
                        }
                        Thread.sleep(1);
                    }
                }
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private static void restart() {
        _killName = new String[]{" ", " ", " ", " ", " ", " ", " ", " ", " ", " "};
        _deathName = new String[]{" ", " ", " ", " ", " ", " ", " ", " ", " ", " "};
    }

    private static String[] intTree(int killCount, String name, String[] userName) {
        if (userName[0].equals(" ")) {
            userName[0] = String.valueOf(name) + "," + killCount;
        } else {
            String[] set = userName[0].split(",");
            if (Integer.parseInt(set[1]) < killCount && !set[0].equals(name)) {
                userName[9] = userName[8];
                userName[8] = userName[7];
                userName[7] = userName[6];
                userName[6] = userName[5];
                userName[5] = userName[4];
                userName[4] = userName[3];
                userName[3] = userName[2];
                userName[2] = userName[1];
                userName[1] = userName[0];
                userName[0] = String.valueOf(name) + "," + killCount;
            } else if (userName[1].equals(" ")) {
                userName[1] = String.valueOf(name) + "," + killCount;
            } else {
                String[] set2 = userName[1].split(",");
                if (Integer.parseInt(set2[1]) < killCount && !set2[0].equals(name)) {
                    userName[9] = userName[8];
                    userName[8] = userName[7];
                    userName[7] = userName[6];
                    userName[6] = userName[5];
                    userName[5] = userName[4];
                    userName[4] = userName[3];
                    userName[3] = userName[2];
                    userName[2] = userName[1];
                    userName[1] = String.valueOf(name) + "," + killCount;
                } else if (userName[2].equals(" ")) {
                    userName[2] = String.valueOf(name) + "," + killCount;
                } else {
                    String[] set3 = userName[2].split(",");
                    if (Integer.parseInt(set3[1]) < killCount && !set3[0].equals(name)) {
                        userName[9] = userName[8];
                        userName[8] = userName[7];
                        userName[7] = userName[6];
                        userName[6] = userName[5];
                        userName[5] = userName[4];
                        userName[4] = userName[3];
                        userName[3] = userName[2];
                        userName[2] = String.valueOf(name) + "," + killCount;
                    } else if (userName[3].equals(" ")) {
                        userName[3] = String.valueOf(name) + "," + killCount;
                    } else {
                        String[] set4 = userName[3].split(",");
                        if (Integer.parseInt(set4[1]) < killCount && !set4[0].equals(name)) {
                            userName[9] = userName[8];
                            userName[8] = userName[7];
                            userName[7] = userName[6];
                            userName[6] = userName[5];
                            userName[5] = userName[4];
                            userName[4] = userName[3];
                            userName[3] = String.valueOf(name) + "," + killCount;
                        } else if (userName[4].equals(" ")) {
                            userName[4] = String.valueOf(name) + "," + killCount;
                        } else {
                            String[] set5 = userName[4].split(",");
                            if (Integer.parseInt(set5[1]) < killCount && !set5[0].equals(name)) {
                                userName[9] = userName[8];
                                userName[8] = userName[7];
                                userName[7] = userName[6];
                                userName[6] = userName[5];
                                userName[5] = userName[4];
                                userName[4] = String.valueOf(name) + "," + killCount;
                            } else if (userName[5].equals(" ")) {
                                userName[5] = String.valueOf(name) + "," + killCount;
                            } else {
                                String[] set6 = userName[5].split(",");
                                if (Integer.parseInt(set6[1]) < killCount && !set6[0].equals(name)) {
                                    userName[9] = userName[8];
                                    userName[8] = userName[7];
                                    userName[7] = userName[6];
                                    userName[6] = userName[5];
                                    userName[5] = String.valueOf(name) + "," + killCount;
                                } else if (userName[6].equals(" ")) {
                                    userName[6] = String.valueOf(name) + "," + killCount;
                                } else {
                                    String[] set7 = userName[6].split(",");
                                    if (Integer.parseInt(set7[1]) < killCount && !set7[0].equals(name)) {
                                        userName[9] = userName[8];
                                        userName[8] = userName[7];
                                        userName[7] = userName[6];
                                        userName[6] = String.valueOf(name) + "," + killCount;
                                    } else if (userName[7].equals(" ")) {
                                        userName[7] = String.valueOf(name) + "," + killCount;
                                    } else {
                                        String[] set8 = userName[7].split(",");
                                        if (Integer.parseInt(set8[1]) < killCount && !set8[0].equals(name)) {
                                            userName[9] = userName[8];
                                            userName[8] = userName[7];
                                            userName[7] = String.valueOf(name) + "," + killCount;
                                        } else if (userName[8].equals(" ")) {
                                            userName[8] = String.valueOf(name) + "," + killCount;
                                        } else {
                                            String[] set9 = userName[8].split(",");
                                            if (Integer.parseInt(set9[1]) < killCount && !set9[0].equals(name)) {
                                                userName[9] = userName[8];
                                                userName[8] = String.valueOf(name) + "," + killCount;
                                            } else if (userName[9].equals(" ")) {
                                                userName[9] = String.valueOf(name) + "," + killCount;
                                            } else {
                                                String[] set10 = userName[9].split(",");
                                                if (Integer.parseInt(set10[1]) < killCount && !set10[0].equals(name)) {
                                                    userName[9] = String.valueOf(name) + "," + killCount;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return userName;
    }
}
