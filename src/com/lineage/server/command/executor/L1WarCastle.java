package com.lineage.server.command.executor;

import com.lineage.config.Config;
import com.lineage.config.ConfigAlt;
import com.lineage.server.datatables.lock.CastleReading;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1War;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Castle;
import com.lineage.server.timecontroller.server.ServerWarExecutor;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.world.WorldWar;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1WarCastle implements L1CommandExecutor {
    private static final Log _log = LogFactory.getLog(L1WarCastle.class);

    private L1WarCastle() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1WarCastle();
    }

    public Calendar getRealTime() {
        return Calendar.getInstance(TimeZone.getTimeZone(Config.TIME_ZONE));
    }

    @Override // com.lineage.server.command.executor.L1CommandExecutor
    public void execute(L1PcInstance pc, String cmdName, String arg) {
        int castleId = 0;
        String name = null;
        boolean start = false;
        try {
            Calendar calendar = PerformanceTimer.getRealTime();
            if (arg.equalsIgnoreCase("stopall")) {
                List<L1War> alllist = WorldWar.get().getWarList();
                if (!alllist.isEmpty()) {
                    for (L1War war : alllist) {
                        if (war.get_castleId() == 0) {
                            war.ceaseWar();
                        } else {
                            L1Castle castle = CastleReading.get().getCastleTable(war.get_castleId());
                            calendar.add(ConfigAlt.ALT_WAR_TIME_UNIT, -(ConfigAlt.ALT_WAR_TIME * 2));
                            castle.setWarTime(calendar);
                            ServerWarExecutor.get().setWarTime(war.get_castleId(), calendar);
                            ServerWarExecutor.get().setEndWarTime(war.get_castleId(), calendar);
                        }
                    }
                }
            } else if (arg.equalsIgnoreCase("stop1")) {
                castleId = 1;
                calendar.add(11, -4);
                name = "肯特城";
            } else if (arg.equalsIgnoreCase("stop2")) {
                castleId = 2;
                calendar.add(11, -4);
                name = "妖魔城";
            } else if (arg.equalsIgnoreCase("stop3")) {
                castleId = 3;
                calendar.add(11, -4);
                name = "風木城";
            } else if (arg.equalsIgnoreCase("stop4")) {
                castleId = 4;
                calendar.add(11, -4);
                name = "奇岩城";
            } else if (arg.equalsIgnoreCase("stop5")) {
                castleId = 5;
                calendar.add(11, -4);
                name = "海音城";
            } else if (arg.equalsIgnoreCase("stop6")) {
                castleId = 6;
                calendar.add(11, -4);
                name = "侏儒城";
            } else if (arg.equalsIgnoreCase("stop7")) {
                castleId = 7;
                calendar.add(11, -4);
                name = "亞丁城";
            } else if (arg.equalsIgnoreCase("start1")) {
                castleId = 1;
                start = true;
                name = "肯特城";
            } else if (arg.equalsIgnoreCase("start2")) {
                castleId = 2;
                start = true;
                name = "妖魔城";
            } else if (arg.equalsIgnoreCase("start3")) {
                castleId = 3;
                start = true;
                name = "風木城";
            } else if (arg.equalsIgnoreCase("start4")) {
                castleId = 4;
                start = true;
                name = "奇岩城";
            } else if (arg.equalsIgnoreCase("start5")) {
                castleId = 5;
                start = true;
                name = "海音城";
            } else if (arg.equalsIgnoreCase("start6")) {
                castleId = 6;
                start = true;
                name = "侏儒城";
            } else if (arg.equalsIgnoreCase("start7")) {
                castleId = 7;
                start = true;
                name = "亞丁城";
            }
            if (castleId != 0) {
                CastleReading.get().getCastleTable(castleId).setWarTime(calendar);
                ServerWarExecutor.get().setWarTime(castleId, calendar);
                ServerWarExecutor.get().setEndWarTime(castleId, calendar);
                if (start) {
                    if (pc != null) {
                        pc.sendPackets(new S_ServerMessage(166, "準備啟動 " + name + " 攻城戰"));
                    }
                    _log.warn("準備啟動 " + name + " 攻城戰");
                    return;
                }
                if (pc != null) {
                    pc.sendPackets(new S_ServerMessage(166, "準備停止 " + name + " 攻城戰"));
                }
                _log.warn("準備停止 " + name + " 攻城戰");
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            if (pc == null) {
                _log.error("錯誤的命令格式: " + getClass().getSimpleName());
                return;
            }
            _log.error("錯誤的GM指令格式: " + getClass().getSimpleName() + " 執行的GM:" + pc.getName());
            pc.sendPackets(new S_ServerMessage(261));
        }
    }
}
