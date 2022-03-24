package com.lineage.server.serverpackets;

import com.lineage.server.datatables.lock.BoardReading;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.templates.L1Board;
import java.util.ArrayList;

public class S_Board extends ServerBasePacket {
    private byte[] _byte = null;

    public S_Board(L1NpcInstance npc) {
        buildPacket(npc, 0);
    }

    public S_Board(L1NpcInstance npc, int number) {
        buildPacket(npc, number);
    }

    private void buildPacket(L1NpcInstance npc, int number) {
        int count = 0;
        ArrayList<L1Board> showList = new ArrayList<>();
        int maxid = BoardReading.get().getMaxId();
        while (count < 8 && maxid > 0) {
            maxid--;
            L1Board boardInfo = BoardReading.get().getBoardTable(maxid);
            if (boardInfo != null && (boardInfo.get_id() <= number || number == 0)) {
                showList.add(count, boardInfo);
                count++;
            }
        }
        writeC(64);
        writeC(0);
        writeD(npc.getId());
        writeC(255);
        writeC(255);
        writeC(255);
        writeC(127);
        writeH(showList.size());
        writeH(300);
        for (int i = 0; i < showList.size(); i++) {
            L1Board boardInfo2 = showList.get(i);
            if (boardInfo2 != null) {
                writeD(boardInfo2.get_id());
                writeS(boardInfo2.get_name());
                writeS(boardInfo2.get_date());
                writeS(boardInfo2.get_title());
            }
        }
    }

    public S_Board(int objectid) {
        writeC(64);
        writeC(0);
        writeD(objectid);
        writeC(255);
        writeC(255);
        writeC(255);
        writeC(127);
        writeH(2);
        writeH(300);
        writeD(2);
        writeS("佈告欄列表");
        writeS("2010-2-2");
        writeS("佈告欄列表TITLE");
        writeD(1);
        writeS("佈告欄列表2");
        writeS("2010-2-3");
        writeS("佈告欄列表TITLE2");
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
