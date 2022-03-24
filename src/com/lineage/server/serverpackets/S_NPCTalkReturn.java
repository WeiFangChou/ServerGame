package com.lineage.server.serverpackets;

import com.lineage.server.datatables.ItemTable;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1NpcTalkData;
import com.lineage.server.model.npc.L1NpcHtml;
import com.lineage.server.templates.L1Item;
import java.util.List;

public class S_NPCTalkReturn extends ServerBasePacket {
    private byte[] _byte;

    public S_NPCTalkReturn(int objid, String htmlid, L1PcInstance pc, List<Integer> list) {
        this._byte = null;
        writeC(OpcodesServer.S_OPCODE_SHOWHTML);
        writeD(objid);
        writeS(htmlid);
        writeH(1);
        writeH(11);
        int t = 0;
        for (Integer v : list) {
            t++;
            L1Item datum = ItemTable.get().getTemplate(v.intValue());
            pc.get_otherList().add_sitemList2(Integer.valueOf(t), v);
            writeS(datum.getName());
        }
        if (list.size() < 11) {
            int x = 11 - list.size();
            for (int i = 0; i < x; i++) {
                writeS(" ");
            }
        }
    }

    public S_NPCTalkReturn(int objid, String htmlid, List<L1ItemInstance> list, L1PcInstance pc) {
        this._byte = null;
        writeC(OpcodesServer.S_OPCODE_SHOWHTML);
        writeD(objid);
        writeS(htmlid);
        writeH(1);
        writeH(11);
        int t = 0;
        for (L1ItemInstance datum : list) {
            t++;
            pc.get_otherList().add_sitemList(Integer.valueOf(t), datum);
            writeS(datum.getViewName());
        }
        if (list.size() < 11) {
            int x = 11 - list.size();
            for (int i = 0; i < x; i++) {
                writeS(" ");
            }
        }
    }

    public S_NPCTalkReturn(L1NpcTalkData npc, int objid, int action, String[] data) {
        String htmlid;
        this._byte = null;
        if (action == 1) {
            htmlid = npc.getNormalAction();
        } else if (action == 2) {
            htmlid = npc.getCaoticAction();
        } else {
            throw new IllegalArgumentException();
        }
        buildPacket(objid, htmlid, data);
    }

    public S_NPCTalkReturn(L1NpcTalkData npc, int objid, int action) {
        this(npc, objid, action, (String[]) null);
    }

    public S_NPCTalkReturn(int objid, String htmlid, String[] data) {
        this._byte = null;
        buildPacket(objid, htmlid, data);
    }

    public S_NPCTalkReturn(int objid, String htmlid) {
        this._byte = null;
        buildPacket(objid, htmlid, null);
    }

    public S_NPCTalkReturn(int objid, L1NpcHtml html) {
        this._byte = null;
        buildPacket(objid, html.getName(), html.getArgs());
    }

    private void buildPacket(int objid, String htmlid, String[] data) {
        writeC(OpcodesServer.S_OPCODE_SHOWHTML);
        writeD(objid);
        writeS(htmlid);
        if (data == null || 1 > data.length) {
            writeH(0);
            writeH(0);
            return;
        }
        writeH(1);
        writeH(data.length);
        for (String datum : data) {
            writeS(datum);
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
