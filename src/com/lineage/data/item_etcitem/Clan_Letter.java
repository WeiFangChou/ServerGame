package com.lineage.data.item_etcitem;

import com.lineage.config.Config;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.datatables.CharObjidTable;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.datatables.lock.CharItemsReading;
import com.lineage.server.datatables.sql.CharacterTable;
import com.lineage.server.datatables.sql.LetterTable;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Clan;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillSound;
import com.lineage.server.world.World;
import com.lineage.server.world.WorldClan;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.TimeZone;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Clan_Letter extends ItemExecutor {
    private static final Log _log = LogFactory.getLog(Clan_Letter.class);

    private Clan_Letter() {
    }

    public static ItemExecutor get() {
        return new Clan_Letter();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        if (writeClanLetter(item.getItemId(), pc, data[0], pc.getText(), pc.getTextByte())) {
            pc.getInventory().removeItem(item, 1);
        }
    }

    private boolean writeClanLetter(int itemId, L1PcInstance pc, int letterCode, String letterReceiver, byte[] letterText) {
        L1Clan targetClan = null;
        Iterator<L1Clan> iter = WorldClan.get().getAllClans().iterator();
        while (true) {
            if (!iter.hasNext()) {
                break;
            }
            L1Clan clan = iter.next();
            if (clan.getClanName().toLowerCase().equals(letterReceiver.toLowerCase())) {
                targetClan = clan;
                break;
            }
        }
        if (targetClan == null) {
            pc.sendPackets(new S_ServerMessage(434));
            return false;
        }
        String[] memberName = targetClan.getAllMembers();
        for (int i = 0; i < memberName.length; i++) {
            L1ItemInstance item = ItemTable.get().createItem(49016);
            if (item == null) {
                return false;
            }
            item.setCount(1);
            if (sendLetter(pc, memberName[i], item, false)) {
                saveLetter(item.getId(), letterCode, pc.getName(), memberName[i], letterText);
            }
        }
        return true;
    }

    private boolean sendLetter(L1PcInstance pc, String name, L1ItemInstance item, boolean isFailureMessage) {
        L1PcInstance target = World.get().getPlayer(name);
        if (target != null) {
            if (target.getInventory().checkAddItem(item, 1) == 0) {
                target.getInventory().storeItem(item);
                target.sendPackets(new S_SkillSound(target.getId(), 1091));
                target.sendPackets(new S_ServerMessage(428));
            } else if (!isFailureMessage) {
                return false;
            } else {
                pc.sendPackets(new S_ServerMessage(942));
                return false;
            }
        } else if (CharacterTable.doesCharNameExist(name)) {
            try {
                int objid = CharObjidTable.get().charObjid(name);
                if (CharItemsReading.get().loadItems(Integer.valueOf(objid)).size() < 180) {
                    CharItemsReading.get().storeItem(objid, item);
                } else if (!isFailureMessage) {
                    return false;
                } else {
                    pc.sendPackets(new S_ServerMessage(942));
                    return false;
                }
            } catch (Exception e) {
                _log.error(e.getLocalizedMessage(), e);
            }
        } else if (!isFailureMessage) {
            return false;
        } else {
            pc.sendPackets(new S_ServerMessage(109, name));
            return false;
        }
        return true;
    }

    private void saveLetter(int itemObjectId, int code, String sender, String receiver, byte[] text) {
        String date = new SimpleDateFormat("yy/MM/dd").format(Calendar.getInstance(TimeZone.getTimeZone(Config.TIME_ZONE)).getTime());
        int spacePosition1 = 0;
        int spacePosition2 = 0;
        int i = 0;
        while (true) {
            if (i >= text.length) {
                break;
            }
            if (text[i] == 0 && text[i + 1] == 0) {
                if (spacePosition1 != 0) {
                    if (spacePosition1 != 0 && 0 == 0) {
                        spacePosition2 = i;
                        break;
                    }
                } else {
                    spacePosition1 = i;
                }
            }
            i += 2;
        }
        int subjectLength = spacePosition1 + 2;
        int contentLength = spacePosition2 - spacePosition1;
        if (contentLength <= 0) {
            contentLength = 1;
        }
        byte[] subject = new byte[subjectLength];
        byte[] content = new byte[contentLength];
        System.arraycopy(text, 0, subject, 0, subjectLength);
        System.arraycopy(text, subjectLength, content, 0, contentLength);
        LetterTable.getInstance().writeLetter(itemObjectId, code, sender, receiver, date, 0, subject, content);
    }
}
