package com.lineage.server.timecontroller;

import com.lineage.config.ConfigAlt;
import com.lineage.server.timecontroller.server.ServerAuctionTimer;
import com.lineage.server.timecontroller.server.ServerElementalStoneTimer;
import com.lineage.server.timecontroller.server.ServerHomeTownTime;
import com.lineage.server.timecontroller.server.ServerHouseTaxTimer;
import com.lineage.server.timecontroller.server.ServerItemUserTimer;
import com.lineage.server.timecontroller.server.ServerLightTimer;
import com.lineage.server.timecontroller.server.ServerResetDailyItemTimer;
import com.lineage.server.timecontroller.server.ServerRestartTimer;
import com.lineage.server.timecontroller.server.ServerShipTimer;
import com.lineage.server.timecontroller.server.ServerTrapTimer;
import com.lineage.server.timecontroller.server.ServerUseMapTimer;
import com.lineage.server.timecontroller.server.ServerWarTimer;

public class StartTimer_Server {
    public void start() throws InterruptedException {
        new ServerTrapTimer().start();
        Thread.sleep(50);
        ServerHomeTownTime.getInstance();
        Thread.sleep(50);
        new ServerWarTimer().start();
        Thread.sleep(50);
        new ServerAuctionTimer().start();
        Thread.sleep(50);
        new ServerHouseTaxTimer().start();
        Thread.sleep(50);
        new ServerLightTimer().start();
        Thread.sleep(50);
        if (ConfigAlt.ELEMENTAL_STONE_AMOUNT > 0) {
            new ServerElementalStoneTimer().start();
            Thread.sleep(50);
        }
        new ServerRestartTimer().start();
        Thread.sleep(50);
        new ServerUseMapTimer().start();
        Thread.sleep(50);
        new ServerItemUserTimer().start();
        new ServerShipTimer().start();
        new ServerResetDailyItemTimer().start();
        Thread.sleep(50);
    }
}
