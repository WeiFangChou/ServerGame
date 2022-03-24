package com.lineage.server.clientpackets;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.datatables.lock.MailReading;
import com.lineage.server.datatables.sql.CharacterTable;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Clan;
import com.lineage.server.model.item.L1ItemId;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_Mail;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Mail;
import com.lineage.server.world.World;
import com.lineage.server.world.WorldClan;
import java.util.ArrayList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class C_Mail extends ClientBasePacket {
    private static final int TYPE_CLAN_MAIL = 1;
    private static final int TYPE_MAIL_BOX = 2;
    private static final int TYPE_NORMAL_MAIL = 0;
    private static final Log _log = LogFactory.getLog(C_Mail.class);

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public void start(byte[] decrypt, ClientExecutor client) {
        try {
            read(decrypt);
            int type = readC();
            L1PcInstance pc = client.getActiveChar();
            switch (type) {
                case 0:
                case 1:
                case 2:
                    if (pc != null) {
                        clientPackA(pc, type);
                        break;
                    }
                    break;
                case 16:
                case 17:
                case 18:
                    if (pc != null) {
                        clientPackB(pc, type, readD());
                        break;
                    }
                    break;
                case 32:
                    if (pc != null) {
                        readH();
                        clientPackD(pc, type, readS(), readByte());
                        break;
                    }
                    break;
                case 33:
                    if (pc != null) {
                        readH();
                        clientPackE(pc, type, readS(), readByte());
                        break;
                    }
                    break;
                case 48:
                case 49:
                case 50:
                    if (pc != null) {
                        clientPackF(pc, type, readD());
                        break;
                    }
                    break;
                case 64:
                    if (pc != null) {
                        clientPackG(pc, type, readD());
                        break;
                    }
                    break;
            }
        } catch (Exception ignored) {
        } finally {
            over();
        }
    }

    private void clientPackG(L1PcInstance pc, int type, int saveid) {
        try {
            L1Mail mail = MailReading.get().getMail(saveid);
            if (mail != null) {
                MailReading.get().setMailType(saveid, 2);
                pc.sendPackets(new S_Mail(mail, type));
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private void clientPackF(L1PcInstance pc, int type, int delid) {
        try {
            L1Mail mail = MailReading.get().getMail(delid);
            if (mail != null) {
                MailReading.get().deleteMail(delid);
                pc.sendPackets(new S_Mail(mail, type));
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private void clientPackE(L1PcInstance pc, int type, String clanName, byte[] text) {
        try {
            L1Clan clan = WorldClan.get().getClan(clanName);
            if (!pc.getInventory().consumeItem(L1ItemId.ADENA, 1000)) {
                pc.sendPackets(new S_ServerMessage((int) L1SkillId.SHOCK_SKIN));
            } else if (clan != null) {
                String[] allMembers = clan.getAllMembers();
                for (String name : allMembers) {
                    if (MailReading.get().getMailSizeByReceiver(name, 1) < 50) {
                        MailReading.get().writeMail(1, name, pc, text);
                        L1PcInstance clanPc = World.get().getPlayer(name);
                        if (clanPc != null) {
                            ArrayList<L1Mail> mails = MailReading.get().getMails(name);
                            if (!mails.isEmpty()) {
                                clanPc.sendPackets(new S_Mail(mails, 0));
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private void clientPackD(L1PcInstance pc, int type, String receiverName, byte[] textr) {
        try {
            L1PcInstance receiver = World.get().getPlayer(receiverName);
            if (!pc.getInventory().consumeItem(L1ItemId.ADENA, 50)) {
                pc.sendPackets(new S_ServerMessage((int) L1SkillId.SHOCK_SKIN));
            } else if (receiver == null) {
                try {
                    if (CharacterTable.get().restoreCharacter(receiverName) == null) {
                        pc.sendPackets(new S_ServerMessage(109, receiverName));
                    } else if (MailReading.get().getMailSizeByReceiver(receiverName, 0) >= 20) {
                        pc.sendPackets(new S_Mail(type));
                    } else {
                        MailReading.get().writeMail(0, receiverName, pc, textr);
                    }
                } catch (Exception e) {
                    _log.error(e.getLocalizedMessage(), e);
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            } else if (MailReading.get().getMailSizeByReceiver(receiverName, 0) >= 20) {
                pc.sendPackets(new S_Mail(type));
            } else {
                MailReading.get().writeMail(0, receiverName, pc, textr);
                if (receiver.getOnlineStatus() == 1) {
                    ArrayList<L1Mail> mails = MailReading.get().getMails(receiverName);
                    if (!mails.isEmpty()) {
                        receiver.sendPackets(new S_Mail(mails, 0));
                    }
                }
            }
        } catch (Exception e2) {
            _log.error(e2.getLocalizedMessage(), e2);
        }
    }

    private void clientPackB(L1PcInstance pc, int type, int id) {
        try {
            L1Mail mail = MailReading.get().getMail(id);
            if (mail != null) {
                if (mail.getReadStatus() == 0) {
                    MailReading.get().setReadStatus(id);
                }
                pc.sendPackets(new S_Mail(mail, type));
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private void clientPackA(L1PcInstance pc, int type) {
        try {
            ArrayList<L1Mail> mails = MailReading.get().getMails(pc.getName());
            if (mails != null && !mails.isEmpty()) {
                pc.sendPackets(new S_Mail(mails, type));
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public String getType() {
        return getClass().getSimpleName();
    }
}
