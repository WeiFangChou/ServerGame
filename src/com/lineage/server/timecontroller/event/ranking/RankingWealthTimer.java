package com.lineage.server.timecontroller.event.ranking;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.item.L1ItemId;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.world.World;
import java.util.Collection;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class RankingWealthTimer extends TimerTask {
    private static boolean _load = false;
    private static final Log _log = LogFactory.getLog(RankingWealthTimer.class);
    private static String[] _userName = {" ", " ", " ", " ", " ", " ", " ", " ", " ", " "};
    private ScheduledFuture<?> _timer;

    public void start() {
        this._timer = GeneralThreadPool.get().scheduleAtFixedRate((TimerTask) this, 600000L, 600000L);
    }

    public static String[] userName() {
        if (!_load) {
            load();
        }
        String[] newUserName = new String[10];
        for (int i = 0; i < 10; i++) {
            newUserName[i] = _userName[i].split(",")[0];
        }
        return newUserName;
    }

    public void run() {
        try {
            load();
        } catch (Exception e) {
            _log.error("財富風雲榜時間軸異常重啟", e);
            GeneralThreadPool.get().cancel(this._timer, false);
            new RankingWealthTimer().start();
        }
    }

    private static void load() {
        try {
            Collection<L1PcInstance> allPc = World.get().getAllPlayers();
            if (!allPc.isEmpty()) {
                _load = true;
                restart();
                for (L1PcInstance tgpc : allPc) {
                    if (!(tgpc == null || tgpc.getOnlineStatus() == 0 || tgpc.getNetConnection() == null || tgpc.getAccessLevel() > 0)) {
                        long count = tgpc.getInventory().countItems(L1ItemId.ADENA);
                        if (count > 0) {
                            _userName = intTree(count, tgpc.getName(), _userName);
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
        _userName = new String[]{" ", " ", " ", " ", " ", " ", " ", " ", " ", " "};
    }

    private static String[] intTree(long count, String name, String[] userName) {
        if (userName[0].equals(" ")) {
            userName[0] = String.valueOf(name) + "," + count;
        } else {
            String[] set = userName[0].split(",");
            if (Long.parseLong(set[1]) < count && !set[0].equals(name)) {
                userName[9] = userName[8];
                userName[8] = userName[7];
                userName[7] = userName[6];
                userName[6] = userName[5];
                userName[5] = userName[4];
                userName[4] = userName[3];
                userName[3] = userName[2];
                userName[2] = userName[1];
                userName[1] = userName[0];
                userName[0] = String.valueOf(name) + "," + count;
            } else if (userName[1].equals(" ")) {
                userName[1] = String.valueOf(name) + "," + count;
            } else {
                String[] set2 = userName[1].split(",");
                if (Long.parseLong(set2[1]) < count && !set2[0].equals(name)) {
                    userName[9] = userName[8];
                    userName[8] = userName[7];
                    userName[7] = userName[6];
                    userName[6] = userName[5];
                    userName[5] = userName[4];
                    userName[4] = userName[3];
                    userName[3] = userName[2];
                    userName[2] = userName[1];
                    userName[1] = String.valueOf(name) + "," + count;
                } else if (userName[2].equals(" ")) {
                    userName[2] = String.valueOf(name) + "," + count;
                } else {
                    String[] set3 = userName[2].split(",");
                    if (Long.parseLong(set3[1]) < count && !set3[0].equals(name)) {
                        userName[9] = userName[8];
                        userName[8] = userName[7];
                        userName[7] = userName[6];
                        userName[6] = userName[5];
                        userName[5] = userName[4];
                        userName[4] = userName[3];
                        userName[3] = userName[2];
                        userName[2] = String.valueOf(name) + "," + count;
                    } else if (userName[3].equals(" ")) {
                        userName[3] = String.valueOf(name) + "," + count;
                    } else {
                        String[] set4 = userName[3].split(",");
                        if (Long.parseLong(set4[1]) < count && !set4[0].equals(name)) {
                            userName[9] = userName[8];
                            userName[8] = userName[7];
                            userName[7] = userName[6];
                            userName[6] = userName[5];
                            userName[5] = userName[4];
                            userName[4] = userName[3];
                            userName[3] = String.valueOf(name) + "," + count;
                        } else if (userName[4].equals(" ")) {
                            userName[4] = String.valueOf(name) + "," + count;
                        } else {
                            String[] set5 = userName[4].split(",");
                            if (Long.parseLong(set5[1]) < count && !set5[0].equals(name)) {
                                userName[9] = userName[8];
                                userName[8] = userName[7];
                                userName[7] = userName[6];
                                userName[6] = userName[5];
                                userName[5] = userName[4];
                                userName[4] = String.valueOf(name) + "," + count;
                            } else if (userName[5].equals(" ")) {
                                userName[5] = String.valueOf(name) + "," + count;
                            } else {
                                String[] set6 = userName[5].split(",");
                                if (Long.parseLong(set6[1]) < count && !set6[0].equals(name)) {
                                    userName[9] = userName[8];
                                    userName[8] = userName[7];
                                    userName[7] = userName[6];
                                    userName[6] = userName[5];
                                    userName[5] = String.valueOf(name) + "," + count;
                                } else if (userName[6].equals(" ")) {
                                    userName[6] = String.valueOf(name) + "," + count;
                                } else {
                                    String[] set7 = userName[6].split(",");
                                    if (Long.parseLong(set7[1]) < count && !set7[0].equals(name)) {
                                        userName[9] = userName[8];
                                        userName[8] = userName[7];
                                        userName[7] = userName[6];
                                        userName[6] = String.valueOf(name) + "," + count;
                                    } else if (userName[7].equals(" ")) {
                                        userName[7] = String.valueOf(name) + "," + count;
                                    } else {
                                        String[] set8 = userName[7].split(",");
                                        if (Long.parseLong(set8[1]) < count && !set8[0].equals(name)) {
                                            userName[9] = userName[8];
                                            userName[8] = userName[7];
                                            userName[7] = String.valueOf(name) + "," + count;
                                        } else if (userName[8].equals(" ")) {
                                            userName[8] = String.valueOf(name) + "," + count;
                                        } else {
                                            String[] set9 = userName[8].split(",");
                                            if (Long.parseLong(set9[1]) < count && !set9[0].equals(name)) {
                                                userName[9] = userName[8];
                                                userName[8] = String.valueOf(name) + "," + count;
                                            } else if (userName[9].equals(" ")) {
                                                userName[9] = String.valueOf(name) + "," + count;
                                            } else {
                                                String[] set10 = userName[9].split(",");
                                                if (Long.parseLong(set10[1]) < count && !set10[0].equals(name)) {
                                                    userName[9] = String.valueOf(name) + "," + count;
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
