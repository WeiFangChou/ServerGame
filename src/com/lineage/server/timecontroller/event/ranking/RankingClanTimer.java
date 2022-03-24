package com.lineage.server.timecontroller.event.ranking;

import com.lineage.server.model.L1Clan;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.world.WorldClan;
import java.util.Collection;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class RankingClanTimer extends TimerTask {
    private static String[] _clanName = {" ", " ", " ", " ", " ", " ", " ", " ", " ", " "};
    private static boolean _load = false;
    private static final Log _log = LogFactory.getLog(RankingClanTimer.class);
    private ScheduledFuture<?> _timer;

    public void start() {
        this._timer = GeneralThreadPool.get().scheduleAtFixedRate((TimerTask) this, 600000L, 600000L);
    }

    public static String[] userName() {
        if (!_load) {
            load();
        }
        return _clanName;
    }

    public void run() {
        try {
            load();
        } catch (Exception e) {
            _log.error("血盟風雲榜時間軸異常重啟", e);
            GeneralThreadPool.get().cancel(this._timer, false);
            new RankingClanTimer().start();
        }
    }

    private static void load() {
        try {
            Collection<L1Clan> allClan = WorldClan.get().getAllClans();
            if (!allClan.isEmpty()) {
                _load = true;
                restart();
                for (L1Clan clan : allClan) {
                    _clanName = intTree(clan.getOnlineClanMemberSize(), clan.getClanName(), _clanName);
                    Thread.sleep(1);
                }
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private static void restart() {
        _clanName = new String[]{" ", " ", " ", " ", " ", " ", " ", " ", " ", " "};
    }

    private static String[] intTree(int count, String clanName, String[] userName) {
        if (userName[0].equals(" ")) {
            userName[0] = String.valueOf(clanName) + "," + count;
        } else {
            String[] set = userName[0].split(",");
            if (Integer.parseInt(set[1]) < count && !set[0].equals(clanName)) {
                userName[9] = userName[8];
                userName[8] = userName[7];
                userName[7] = userName[6];
                userName[6] = userName[5];
                userName[5] = userName[4];
                userName[4] = userName[3];
                userName[3] = userName[2];
                userName[2] = userName[1];
                userName[1] = userName[0];
                userName[0] = String.valueOf(clanName) + "," + count;
            } else if (userName[1].equals(" ")) {
                userName[1] = String.valueOf(clanName) + "," + count;
            } else {
                String[] set2 = userName[1].split(",");
                if (Integer.parseInt(set2[1]) < count && !set2[0].equals(clanName)) {
                    userName[9] = userName[8];
                    userName[8] = userName[7];
                    userName[7] = userName[6];
                    userName[6] = userName[5];
                    userName[5] = userName[4];
                    userName[4] = userName[3];
                    userName[3] = userName[2];
                    userName[2] = userName[1];
                    userName[1] = String.valueOf(clanName) + "," + count;
                } else if (userName[2].equals(" ")) {
                    userName[2] = String.valueOf(clanName) + "," + count;
                } else {
                    String[] set3 = userName[2].split(",");
                    if (Integer.parseInt(set3[1]) < count && !set3[0].equals(clanName)) {
                        userName[9] = userName[8];
                        userName[8] = userName[7];
                        userName[7] = userName[6];
                        userName[6] = userName[5];
                        userName[5] = userName[4];
                        userName[4] = userName[3];
                        userName[3] = userName[2];
                        userName[2] = String.valueOf(clanName) + "," + count;
                    } else if (userName[3].equals(" ")) {
                        userName[3] = String.valueOf(clanName) + "," + count;
                    } else {
                        String[] set4 = userName[3].split(",");
                        if (Integer.parseInt(set4[1]) < count && !set4[0].equals(clanName)) {
                            userName[9] = userName[8];
                            userName[8] = userName[7];
                            userName[7] = userName[6];
                            userName[6] = userName[5];
                            userName[5] = userName[4];
                            userName[4] = userName[3];
                            userName[3] = String.valueOf(clanName) + "," + count;
                        } else if (userName[4].equals(" ")) {
                            userName[4] = String.valueOf(clanName) + "," + count;
                        } else {
                            String[] set5 = userName[4].split(",");
                            if (Integer.parseInt(set5[1]) < count && !set5[0].equals(clanName)) {
                                userName[9] = userName[8];
                                userName[8] = userName[7];
                                userName[7] = userName[6];
                                userName[6] = userName[5];
                                userName[5] = userName[4];
                                userName[4] = String.valueOf(clanName) + "," + count;
                            } else if (userName[5].equals(" ")) {
                                userName[5] = String.valueOf(clanName) + "," + count;
                            } else {
                                String[] set6 = userName[5].split(",");
                                if (Integer.parseInt(set6[1]) < count && !set6[0].equals(clanName)) {
                                    userName[9] = userName[8];
                                    userName[8] = userName[7];
                                    userName[7] = userName[6];
                                    userName[6] = userName[5];
                                    userName[5] = String.valueOf(clanName) + "," + count;
                                } else if (userName[6].equals(" ")) {
                                    userName[6] = String.valueOf(clanName) + "," + count;
                                } else {
                                    String[] set7 = userName[6].split(",");
                                    if (Integer.parseInt(set7[1]) < count && !set7[0].equals(clanName)) {
                                        userName[9] = userName[8];
                                        userName[8] = userName[7];
                                        userName[7] = userName[6];
                                        userName[6] = String.valueOf(clanName) + "," + count;
                                    } else if (userName[7].equals(" ")) {
                                        userName[7] = String.valueOf(clanName) + "," + count;
                                    } else {
                                        String[] set8 = userName[7].split(",");
                                        if (Integer.parseInt(set8[1]) < count && !set8[0].equals(clanName)) {
                                            userName[9] = userName[8];
                                            userName[8] = userName[7];
                                            userName[7] = String.valueOf(clanName) + "," + count;
                                        } else if (userName[8].equals(" ")) {
                                            userName[8] = String.valueOf(clanName) + "," + count;
                                        } else {
                                            String[] set9 = userName[8].split(",");
                                            if (Integer.parseInt(set9[1]) < count && !set9[0].equals(clanName)) {
                                                userName[9] = userName[8];
                                                userName[8] = String.valueOf(clanName) + "," + count;
                                            } else if (userName[9].equals(" ")) {
                                                userName[9] = String.valueOf(clanName) + "," + count;
                                            } else {
                                                String[] set10 = userName[9].split(",");
                                                if (Integer.parseInt(set10[1]) < count && !set10[0].equals(clanName)) {
                                                    userName[9] = String.valueOf(clanName) + "," + count;
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
