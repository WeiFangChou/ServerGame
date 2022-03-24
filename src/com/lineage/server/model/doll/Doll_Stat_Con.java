package com.lineage.server.model.doll;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_OwnCharStatus2;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Doll_Stat_Con extends L1DollExecutor {
    private static final Log _log = LogFactory.getLog(Doll_Stat_Con.class);
    private int _int1;
    private String _note;

    public static L1DollExecutor get() {
        return new Doll_Stat_Con();
    }

    @Override // com.lineage.server.model.doll.L1DollExecutor
    public void set_power(int int1, int int2, int int3) {
        try {
            this._int1 = int1;
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.server.model.doll.L1DollExecutor
    public void set_note(String note) {
        this._note = note;
    }

    @Override // com.lineage.server.model.doll.L1DollExecutor
    public String get_note() {
        return this._note;
    }

    @Override // com.lineage.server.model.doll.L1DollExecutor
    public void setDoll(L1PcInstance pc) {
        try {
            pc.addCon(this._int1);
            pc.sendPackets(new S_OwnCharStatus2(pc));
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.server.model.doll.L1DollExecutor
    public void removeDoll(L1PcInstance pc) {
        try {
            pc.addCon(-this._int1);
            pc.sendPackets(new S_OwnCharStatus2(pc));
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.server.model.doll.L1DollExecutor
    public boolean is_reset() {
        return false;
    }
}
