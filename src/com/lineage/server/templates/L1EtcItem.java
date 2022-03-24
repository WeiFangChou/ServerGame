package com.lineage.server.templates;

public class L1EtcItem extends L1Item {
    private static final long serialVersionUID = 1;
    private int _delay_effect;
    private int _delay_id;
    private int _delay_time;
    private int _maxChargeCount;
    private boolean _stackable;

    @Override // com.lineage.server.templates.L1Item
    public boolean isStackable() {
        return this._stackable;
    }

    public void set_stackable(boolean stackable) {
        this._stackable = stackable;
    }

    public void set_delayid(int delay_id) {
        this._delay_id = delay_id;
    }

    @Override // com.lineage.server.templates.L1Item
    public int get_delayid() {
        return this._delay_id;
    }

    public void set_delaytime(int delay_time) {
        this._delay_time = delay_time;
    }

    @Override // com.lineage.server.templates.L1Item
    public int get_delaytime() {
        return this._delay_time;
    }

    @Override // com.lineage.server.templates.L1Item
    public void set_delayEffect(int delay_effect) {
        this._delay_effect = delay_effect;
    }

    @Override // com.lineage.server.templates.L1Item
    public int get_delayEffect() {
        return this._delay_effect;
    }

    public void setMaxChargeCount(int i) {
        this._maxChargeCount = i;
    }

    @Override // com.lineage.server.templates.L1Item
    public int getMaxChargeCount() {
        return this._maxChargeCount;
    }
}
