package com.lineage.server.serverpackets;

import com.lineage.server.datatables.lock.BoardReading;
import com.lineage.server.templates.L1Board;

public class S_BoardRead extends ServerBasePacket {
    private byte[] _byte = null;

    public S_BoardRead(int number) {
        buildPacket(number);
    }

    private void buildPacket(int number) {
        L1Board board = BoardReading.get().getBoardTable(number);
        writeC(56);
        writeD(board.get_id());
        writeS(board.get_name());
        writeS(board.get_date());
        writeS(board.get_title());
        writeS(board.get_content());
    }

    public S_BoardRead() {
        writeC(56);
        writeD(10);
        writeS("測試NAME");
        writeS("2010-02-02");
        writeS("測試TITLE");
        writeS("測試內容");
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
