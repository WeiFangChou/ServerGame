package com.lineage.server.timecontroller;

import com.lineage.server.timecontroller.npc.NpcBowTimer;
import com.lineage.server.timecontroller.npc.NpcChatTimer;
import com.lineage.server.timecontroller.npc.NpcDeadTimer;
import com.lineage.server.timecontroller.npc.NpcDeleteTimer;
import com.lineage.server.timecontroller.npc.NpcDigestItemTimer;
import com.lineage.server.timecontroller.npc.NpcHprTimer;
import com.lineage.server.timecontroller.npc.NpcMprTimer;
import com.lineage.server.timecontroller.npc.NpcRestTimer;
import com.lineage.server.timecontroller.npc.NpcSpawnBossTimer;
import com.lineage.server.timecontroller.npc.NpcWorkTimer;

public class StartTimer_Npc {
    public void start() throws InterruptedException {
        new NpcChatTimer().start();
        Thread.sleep(50);
        new NpcHprTimer().start();
        Thread.sleep(50);
        new NpcMprTimer().start();
        Thread.sleep(50);
        new NpcDeleteTimer().start();
        Thread.sleep(50);
        new NpcDeadTimer().start();
        Thread.sleep(50);
        new NpcDigestItemTimer().start();
        Thread.sleep(50);
        new NpcSpawnBossTimer().start();
        Thread.sleep(50);
        new NpcRestTimer().start();
        Thread.sleep(50);
        new NpcWorkTimer().start();
        Thread.sleep(50);
        new NpcBowTimer().start();
    }
}
