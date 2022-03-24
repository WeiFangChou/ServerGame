package com.lineage.server.model.skill;

import com.lineage.server.model.L1Character;

public class TargetStatus {
    private boolean _isAction = false;
    private boolean _isCalc = true;
    private boolean _isSendStatus = false;
    private L1Character _target = null;

    public TargetStatus(L1Character cha) {
        this._target = cha;
    }

    public TargetStatus(L1Character cha, boolean flg) {
        this._target = cha;
        this._isCalc = flg;
    }

    public L1Character getTarget() {
        return this._target;
    }

    public boolean isCalc() {
        return this._isCalc;
    }

    public void isCalc(boolean flg) {
        this._isCalc = flg;
    }

    public void isAction(boolean flg) {
        this._isAction = flg;
    }

    public boolean isAction() {
        return this._isAction;
    }

    public void isSendStatus(boolean flg) {
        this._isSendStatus = flg;
    }

    public boolean isSendStatus() {
        return this._isSendStatus;
    }
}
