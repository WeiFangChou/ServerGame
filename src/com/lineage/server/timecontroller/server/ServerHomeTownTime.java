package com.lineage.server.timecontroller.server;

import com.lineage.server.datatables.lock.TownReading;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.gametime.L1GameTime;
import com.lineage.server.model.gametime.L1GameTimeAdapter;
import com.lineage.server.model.gametime.L1GameTimeClock;
import com.lineage.server.serverpackets.S_PacketBox;
import com.lineage.server.world.World;
import java.util.Collection;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ServerHomeTownTime {
    private static ServerHomeTownTime _instance;
    private static L1TownFixedProcListener _listener;
    private static final Log _log = LogFactory.getLog(ServerHomeTownTime.class);

    public static ServerHomeTownTime getInstance() {
        if (_instance == null) {
            _instance = new ServerHomeTownTime();
        }
        return _instance;
    }

    private ServerHomeTownTime() {
        startListener();
    }

    private void startListener() {
        if (_listener == null) {
            _listener = new L1TownFixedProcListener(this, null);
            L1GameTimeClock.getInstance().addListener(_listener);
        }
    }

    /* access modifiers changed from: private */
    public class L1TownFixedProcListener extends L1GameTimeAdapter {
        private L1TownFixedProcListener() {
        }

        /* synthetic */ L1TownFixedProcListener(ServerHomeTownTime serverHomeTownTime, L1TownFixedProcListener l1TownFixedProcListener) {
            this();
        }

        @Override // com.lineage.server.model.gametime.L1GameTimeAdapter, com.lineage.server.model.gametime.L1GameTimeListener
        public void onDayChanged(L1GameTime time) {
            ServerHomeTownTime.this.fixedProc(time);
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void fixedProc(L1GameTime time) {
        if (time.getCalendar().get(5) == 25) {
            monthlyProc();
        } else {
            dailyProc();
        }
    }

    public void dailyProc() {
        _log.info("村莊系統：日處理啟動");
        TownReading.get().updateTaxRate();
        TownReading.get().updateSalesMoneyYesterday();
        TownReading.get().load();
    }

    public void monthlyProc() {
        _log.info("村莊系統：月處理啟動");
        World.get().setProcessingContributionTotal(true);
        Collection<L1PcInstance> players = World.get().getAllPlayers();
        for (L1PcInstance pc : players) {
            try {
                pc.save();
            } catch (Exception e) {
                _log.error(e.getLocalizedMessage(), e);
            }
        }
        for (int townId = 1; townId <= 10; townId++) {
            String leaderName = TownReading.get().totalContribution(townId);
            if (leaderName != null) {
                S_PacketBox packet = new S_PacketBox(23, leaderName);
                for (L1PcInstance pc2 : players) {
                    if (pc2.getHomeTownId() == townId) {
                        pc2.setContribution(0);
                        pc2.sendPackets(packet);
                    }
                }
            }
        }
        TownReading.get().load();
        for (L1PcInstance pc3 : players) {
            if (pc3.getHomeTownId() == -1) {
                pc3.setHomeTownId(0);
            }
            pc3.setContribution(0);
            try {
                pc3.save();
            } catch (Exception e2) {
                _log.error(e2.getLocalizedMessage(), e2);
            }
        }
        TownReading.get().clearHomeTownID();
        World.get().setProcessingContributionTotal(false);
    }
}
