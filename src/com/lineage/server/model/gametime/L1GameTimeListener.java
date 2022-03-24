package com.lineage.server.model.gametime;

public interface L1GameTimeListener {
    void onDayChanged(L1GameTime l1GameTime);

    void onHourChanged(L1GameTime l1GameTime);

    void onMinuteChanged(L1GameTime l1GameTime);

    void onMonthChanged(L1GameTime l1GameTime);
}
