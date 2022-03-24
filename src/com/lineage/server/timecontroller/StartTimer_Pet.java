package com.lineage.server.timecontroller;

import com.lineage.server.timecontroller.pet.DollAidTimer;
import com.lineage.server.timecontroller.pet.DollGetTimer;
import com.lineage.server.timecontroller.pet.DollHprTimer;
import com.lineage.server.timecontroller.pet.DollMprTimer;
import com.lineage.server.timecontroller.pet.DollTimer;
import com.lineage.server.timecontroller.pet.PetHprTimer;
import com.lineage.server.timecontroller.pet.PetMprTimer;
import com.lineage.server.timecontroller.pet.SummonHprTimer;
import com.lineage.server.timecontroller.pet.SummonMprTimer;
import com.lineage.server.timecontroller.pet.SummonTimer;

public class StartTimer_Pet {
    public void start() throws InterruptedException {
        new PetHprTimer().start();
        Thread.sleep(50);
        new PetMprTimer().start();
        Thread.sleep(50);
        new SummonHprTimer().start();
        Thread.sleep(50);
        new SummonMprTimer().start();
        Thread.sleep(50);
        new SummonTimer().start();
        Thread.sleep(50);
        new DollTimer().start();
        new DollHprTimer().start();
        new DollMprTimer().start();
        new DollGetTimer().start();
        new DollAidTimer().start();
    }
}
