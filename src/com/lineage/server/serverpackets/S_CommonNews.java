package com.lineage.server.serverpackets;

import com.lineage.list.Announcements;
import java.util.ArrayList;
import java.util.Iterator;

public class S_CommonNews extends ServerBasePacket {
    private byte[] _byte = null;

    public S_CommonNews() {
        ArrayList<String> info = Announcements.get().list();
        writeC(30);
        StringBuilder messagePack = new StringBuilder();
        Iterator<String> it = info.iterator();
        while (it.hasNext()) {
            messagePack.append(it.next() + "\n");
        }
        writeS(messagePack.toString());
    }

    public S_CommonNews(String s) {
        writeC(30);
        writeS(s);
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
