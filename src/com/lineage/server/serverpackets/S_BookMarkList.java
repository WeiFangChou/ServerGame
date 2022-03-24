package com.lineage.server.serverpackets;

import com.lineage.server.datatables.lock.CharBookReading;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.templates.L1BookConfig;
import com.lineage.server.templates.L1BookMark;
import java.util.ArrayList;
import java.util.Iterator;

public class S_BookMarkList extends ServerBasePacket {
    private byte[] _byte = null;

    public S_BookMarkList(L1PcInstance pc, L1BookConfig config) {
        byte[] data = config.getData();
        ArrayList<L1BookMark> bookList = CharBookReading.get().getBookMarks(pc);
        writeC(33);
        writeC(42);
        writeH(128);
        writeC(2);
        writeByte(data);
        writeH(60);
        if (bookList == null || bookList.size() <= 0) {
            writeH(0);
            return;
        }
        writeH(bookList.size());
        Iterator<L1BookMark> it = bookList.iterator();
        while (it.hasNext()) {
            L1BookMark book = it.next();
            writeD(book.getId());
            writeS(book.getName());
            writeH(book.getMapId());
            writeH(book.getLocX());
            writeH(book.getLocY());
        }
    }

    public S_BookMarkList(ArrayList<L1BookMark> bookList) {
        writeC(33);
        writeC(42);
        writeH(128);
        writeC(2);
        for (int i = 1; i < 128; i++) {
            writeC(255);
        }
        writeH(60);
        if (bookList.size() > 0) {
            writeH(bookList.size());
            Iterator<L1BookMark> it = bookList.iterator();
            while (it.hasNext()) {
                L1BookMark book = it.next();
                writeD(book.getId());
                writeS(book.getName());
                writeH(book.getMapId());
                writeH(book.getLocX());
                writeH(book.getLocY());
            }
            return;
        }
        writeH(0);
    }

    public S_BookMarkList() {
        writeC(33);
        writeC(42);
        writeH(128);
        writeC(2);
        for (int i = 1; i < 128; i++) {
            writeC(255);
        }
        writeH(60);
        writeH(0);
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
