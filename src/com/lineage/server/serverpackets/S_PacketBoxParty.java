package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Party;
import java.util.HashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class S_PacketBoxParty extends ServerBasePacket {
    public static final int MSG_LEADER = 106;
    public static final int MSG_PARTY1 = 104;
    public static final int MSG_PARTY2 = 105;
    public static final int MSG_PARTY3 = 110;
    private static final Log _log = LogFactory.getLog(S_PacketBoxParty.class);
    private byte[] _byte = null;

    public S_PacketBoxParty(L1PcInstance pc, L1Party party) {
        try {
            HashMap<Integer, L1PcInstance> map = new HashMap<>();
            map.putAll(party.partyUsers());
            writeC(40);
            writeC(104);
            writeC(map.size() - 1);
            for (Integer key : map.keySet()) {
                L1PcInstance tgpc = map.get(key);
                if (tgpc != null && !pc.equals(tgpc)) {
                    writeD(tgpc.getId());
                    writeS(tgpc.getName());
                    writeC((int) ((((double) tgpc.getCurrentHp()) / ((double) tgpc.getMaxHp())) * 100.0d));
                    writeD(tgpc.getMapId());
                    writeH(tgpc.getX());
                    writeH(tgpc.getY());
                }
            }
            map.clear();
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public S_PacketBoxParty(L1Party party, L1PcInstance pc) {
        try {
            HashMap<Integer, L1PcInstance> map = new HashMap<>();
            map.putAll(party.partyUsers());
            writeC(40);
            writeC(110);
            writeC(map.size() - 1);
            for (Integer key : map.keySet()) {
                L1PcInstance tgpc = map.get(key);
                if (tgpc != null && !pc.equals(tgpc)) {
                    writeD(tgpc.getId());
                    writeD(tgpc.getMapId());
                    writeH(tgpc.getX());
                    writeH(tgpc.getY());
                }
            }
            map.clear();
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public S_PacketBoxParty(L1PcInstance tgpc) {
        try {
            writeC(40);
            writeC(105);
            writeD(tgpc.getId());
            writeS(tgpc.getName());
            writeC((int) ((((double) tgpc.getCurrentHp()) / ((double) tgpc.getMaxHp())) * 100.0d));
            writeD(tgpc.getMapId());
            writeH(tgpc.getX());
            writeH(tgpc.getY());
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public S_PacketBoxParty(int objid, String name) {
        try {
            writeC(40);
            writeC(106);
            writeD(objid);
            writeS(name);
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
