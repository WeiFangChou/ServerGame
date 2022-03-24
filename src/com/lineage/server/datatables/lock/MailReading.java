package com.lineage.server.datatables.lock;

import com.lineage.server.datatables.sql.MailTable;
import com.lineage.server.datatables.storage.MailStorage;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.templates.L1Mail;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MailReading {
    private static MailReading _instance;
    private final Lock _lock = new ReentrantLock(true);
    private final MailStorage _storage = new MailTable();

    private MailReading() {
    }

    public static MailReading get() {
        if (_instance == null) {
            _instance = new MailReading();
        }
        return _instance;
    }

    public void load() {
        this._lock.lock();
        try {
            this._storage.load();
        } finally {
            this._lock.unlock();
        }
    }

    public void setReadStatus(int mailId) {
        this._lock.lock();
        try {
            this._storage.setReadStatus(mailId);
        } finally {
            this._lock.unlock();
        }
    }

    public void setMailType(int mailId, int type) {
        this._lock.lock();
        try {
            this._storage.setMailType(mailId, type);
        } finally {
            this._lock.unlock();
        }
    }

    public void deleteMail(int mailId) {
        this._lock.lock();
        try {
            this._storage.deleteMail(mailId);
        } finally {
            this._lock.unlock();
        }
    }

    public void writeMail(int type, String receiver, L1PcInstance writer, byte[] text) {
        this._lock.lock();
        try {
            this._storage.writeMail(type, receiver, writer, text);
        } finally {
            this._lock.unlock();
        }
    }

    public Map<Integer, L1Mail> getAllMail() {
        this._lock.lock();
        try {
            return this._storage.getAllMail();
        } finally {
            this._lock.unlock();
        }
    }

    public L1Mail getMail(int mailId) {
        this._lock.lock();
        try {
            return this._storage.getMail(mailId);
        } finally {
            this._lock.unlock();
        }
    }

    public ArrayList<L1Mail> getMails(String receiverName) {
        ArrayList<L1Mail> mailList = new ArrayList<>();
        for (L1Mail mail : getAllMail().values()) {
            if (mail.getReceiverName().equalsIgnoreCase(receiverName)) {
                mailList.add(mail);
            }
        }
        if (mailList.size() > 0) {
            return mailList;
        }
        return null;
    }

    public int getMailSizeByReceiver(String receiverName, int type) {
        ArrayList<L1Mail> mailList = new ArrayList<>();
        for (L1Mail mail : getAllMail().values()) {
            if (mail.getReceiverName().equalsIgnoreCase(receiverName) && mail.getType() == type) {
                mailList.add(mail);
            }
        }
        if (mailList.size() > 0) {
            return mailList.size();
        }
        return 0;
    }
}
