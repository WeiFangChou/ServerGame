package com.lineage.server.serverpackets;

import com.lineage.server.templates.L1Mail;
import java.util.ArrayList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class S_Mail extends ServerBasePacket {
    private static final Log _log = LogFactory.getLog(S_Mail.class);
    private byte[] _byte = null;

    public S_Mail(ArrayList<L1Mail> mails, int type) {
        try {
            writeC(1);
            writeC(type);
            writeH(mails.size());
            for (int i = 0; i < mails.size(); i++) {
                L1Mail mail = mails.get(i);
                writeD(mail.getId());
                writeC(mail.getReadStatus());
                String[] st = mail.getDate().split("/");
                writeC(Integer.parseInt(st[0]));
                writeC(Integer.parseInt(st[1]));
                writeC(Integer.parseInt(st[2]));
                writeS(mail.getSenderName());
                writeByte(mail.getSubject());
                writeC(0);
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public S_Mail(int type) {
        writeC(1);
        writeC(type);
    }

    public S_Mail(L1Mail mail, int type) {
        switch (type) {
            case 48:
            case 49:
            case 50:
            case 64:
                buildPacket_1(mail, type);
                return;
            default:
                buildPacket_2(mail, type);
                return;
        }
    }

    private void buildPacket_1(L1Mail mail, int type) {
        writeC(1);
        writeC(type);
        writeD(mail.getId());
        writeC(1);
    }

    private void buildPacket_2(L1Mail mail, int type) {
        writeC(1);
        writeC(type);
        writeD(mail.getId());
        writeByte(mail.getContent());
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
