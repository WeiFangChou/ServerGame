package com.lineage.server.timecontroller;

import com.lineage.config.Config;
import com.lineage.config.ConfigAlt;
import com.lineage.server.timecontroller.p002pc.ExpTimer;
import com.lineage.server.timecontroller.p002pc.HprTimerCrown;
import com.lineage.server.timecontroller.p002pc.HprTimerDarkElf;
import com.lineage.server.timecontroller.p002pc.HprTimerDragonKnight;
import com.lineage.server.timecontroller.p002pc.HprTimerElf;
import com.lineage.server.timecontroller.p002pc.HprTimerIllusionist;
import com.lineage.server.timecontroller.p002pc.HprTimerKnight;
import com.lineage.server.timecontroller.p002pc.HprTimerWizard;
import com.lineage.server.timecontroller.p002pc.LawfulTimer;
import com.lineage.server.timecontroller.p002pc.MprTimerCrown;
import com.lineage.server.timecontroller.p002pc.MprTimerDarkElf;
import com.lineage.server.timecontroller.p002pc.MprTimerDragonKnight;
import com.lineage.server.timecontroller.p002pc.MprTimerElf;
import com.lineage.server.timecontroller.p002pc.MprTimerIllusionist;
import com.lineage.server.timecontroller.p002pc.MprTimerKnight;
import com.lineage.server.timecontroller.p002pc.MprTimerWizard;
import com.lineage.server.timecontroller.p002pc.PartyTimer;
import com.lineage.server.timecontroller.p002pc.PcAutoSaveInventoryTimer;
import com.lineage.server.timecontroller.p002pc.PcAutoSaveTimer;
import com.lineage.server.timecontroller.p002pc.PcDeleteTimer;
import com.lineage.server.timecontroller.p002pc.PcFishingTimer;
import com.lineage.server.timecontroller.p002pc.PcGhostTimer;
import com.lineage.server.timecontroller.p002pc.PcHellTimer;
import com.lineage.server.timecontroller.p002pc.UnfreezingTimer;
import com.lineage.server.timecontroller.p002pc.UpdateObjectCTimer;
import com.lineage.server.timecontroller.p002pc.UpdateObjectDKTimer;
import com.lineage.server.timecontroller.p002pc.UpdateObjectDTimer;
import com.lineage.server.timecontroller.p002pc.UpdateObjectETimer;
import com.lineage.server.timecontroller.p002pc.UpdateObjectITimer;
import com.lineage.server.timecontroller.p002pc.UpdateObjectKTimer;
import com.lineage.server.timecontroller.p002pc.UpdateObjectWTimer;

public class StartTimer_Pc {
    public void start() throws InterruptedException {
        if (Config.AUTOSAVE_INTERVAL > 0) {
            new PcAutoSaveTimer().start();
        }
        if (Config.AUTOSAVE_INTERVAL_INVENTORY > 0) {
            new PcAutoSaveInventoryTimer().start();
        }
        new PcFishingTimer().start();
        Thread.sleep(50);
        new UpdateObjectCTimer().start();
        new UpdateObjectDKTimer().start();
        new UpdateObjectDTimer().start();
        new UpdateObjectETimer().start();
        new UpdateObjectITimer().start();
        new UpdateObjectKTimer().start();
        new UpdateObjectWTimer().start();
        Thread.sleep(50);
        new HprTimerCrown().start();
        new HprTimerDarkElf().start();
        new HprTimerDragonKnight().start();
        new HprTimerElf().start();
        new HprTimerIllusionist().start();
        new HprTimerKnight().start();
        new HprTimerWizard().start();
        Thread.sleep(50);
        new MprTimerCrown().start();
        new MprTimerDarkElf().start();
        new MprTimerDragonKnight().start();
        new MprTimerElf().start();
        new MprTimerIllusionist().start();
        new MprTimerKnight().start();
        new MprTimerWizard().start();
        Thread.sleep(50);
        new ExpTimer().start();
        new LawfulTimer().start();
        new PcDeleteTimer().start();
        new PcGhostTimer().start();
        new UnfreezingTimer().start();
        new PartyTimer().start();
        Thread.sleep(50);
        if (ConfigAlt.ALT_PUNISHMENT) {
            new PcHellTimer().start();
        }
    }
}
