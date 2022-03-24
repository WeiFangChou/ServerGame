package com.lineage.server.datatables.storage;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.templates.L1Mail;
import java.util.Map;

public interface MailStorage {
    void deleteMail(int i);

    Map<Integer, L1Mail> getAllMail();

    L1Mail getMail(int i);

    void load();

    void setMailType(int i, int i2);

    void setReadStatus(int i);

    void writeMail(int i, String str, L1PcInstance l1PcInstance, byte[] bArr);
}
