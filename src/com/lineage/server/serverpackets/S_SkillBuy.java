package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1PcInstance;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class S_SkillBuy extends ServerBasePacket {
    private static final Log _log = LogFactory.getLog(S_SkillBuy.class);
    private byte[] _byte = null;

    public S_SkillBuy(L1PcInstance pc, ArrayList<Integer> newSkillList) {
        try {
            if (newSkillList.size() <= 0) {
                writeC(222);
                writeH(0);
                return;
            }
            writeC(222);
            writeD(100);
            writeH(newSkillList.size());
            Iterator<Integer> it = newSkillList.iterator();
            while (it.hasNext()) {
                writeD(it.next().intValue());
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.server.serverpackets.ServerBasePacket
    public byte[] getContent() {
        if (this._byte == null) {
            this._byte = getBytes();
        }
        return this._byte;
    }

    @Override // com.lineage.server.serverpackets.ServerBasePacket
    public String getType() {
        return getClass().getSimpleName();
    }
}
