package com.lineage.server.command.executor;

import com.lineage.config.Config;
import com.lineage.config.ConfigOther;
import com.lineage.server.clientpackets.AcceleratorChecker;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_SystemMessage;
import java.util.Calendar;
import java.util.StringTokenizer;
import java.util.TimeZone;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1SpeedCheck implements L1CommandExecutor {
    private static final Log _log = LogFactory.getLog(L1SpeedCheck.class);

    private L1SpeedCheck() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1SpeedCheck();
    }

    public Calendar getRealTime() {
        return Calendar.getInstance(TimeZone.getTimeZone(Config.TIME_ZONE));
    }

    @Override // com.lineage.server.command.executor.L1CommandExecutor
    public void execute(L1PcInstance pc, String cmdName, String arg) {
        try {
            StringTokenizer stringtokenizer = new StringTokenizer(arg);
            int move = Integer.parseInt(stringtokenizer.nextToken());
            int attack = Integer.parseInt(stringtokenizer.nextToken());
            int type = Integer.parseInt(stringtokenizer.nextToken());
            int time = Integer.parseInt(stringtokenizer.nextToken());
            ConfigOther.CHECK_MOVE_STRICTNESS = move;
            AcceleratorChecker.Setspeed();
            pc.sendPackets(new S_SystemMessage("\\aD目前移動防加速誤差值為 :" + ConfigOther.CHECK_MOVE_STRICTNESS));
            ConfigOther.CHECK_STRICTNESS = attack;
            AcceleratorChecker.Setspeed();
            pc.sendPackets(new S_SystemMessage("\\aE目前動作防加速誤差值為 :" + ConfigOther.CHECK_STRICTNESS));
            ConfigOther.PUNISHMENT_TYPE = type;
            pc.sendPackets(new S_SystemMessage("\\aG目前防加速的懲罰類行為 :" + ConfigOther.PUNISHMENT_TYPE));
            ConfigOther.PUNISHMENT_TIME = time;
            pc.sendPackets(new S_SystemMessage("\\aH目前防加速的懲罰時間為 :" + ConfigOther.PUNISHMENT_TIME));
        } catch (Exception e) {
            _log.error("錯誤的 GM 指令格式: " + getClass().getSimpleName() + " 執行 GM :" + pc.getName());
            pc.sendPackets(new S_SystemMessage("\\aG請輸入  移動/攻擊/類型/時間 參數。"));
        }
    }
}
