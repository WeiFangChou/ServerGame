package com.lineage.server.clientpackets;

import com.lineage.config.ConfigAlt;
import com.lineage.echo.ClientExecutor;
import com.lineage.server.datatables.CharObjidTable;
import com.lineage.server.datatables.sql.CharacterTable;
import com.lineage.server.model.L1Clan;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_DeleteCharOK;
import com.lineage.server.world.WorldClan;
import java.sql.Timestamp;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class C_DeleteChar extends ClientBasePacket {
    private static final Log _log = LogFactory.getLog(C_DeleteChar.class);

    public C_DeleteChar() {
    }

    public void start(byte[] decrypt, ClientExecutor client) {
        try {
            this.read(decrypt);
            String name = this.readS();
            if (name.isEmpty()) {
                return;
            }

            try {
                L1PcInstance pc = CharacterTable.get().restoreCharacter(name);
                if (pc != null && pc.getLevel() >= ConfigAlt.DELETE_CHARACTER_AFTER_LV && ConfigAlt.DELETE_CHARACTER_AFTER_7DAYS) {
                    if (pc.getType() < 32) {
                        if (pc.isCrown()) {
                            pc.setType(32);
                        } else if (pc.isKnight()) {
                            pc.setType(33);
                        } else if (pc.isElf()) {
                            pc.setType(34);
                        } else if (pc.isWizard()) {
                            pc.setType(35);
                        } else if (pc.isDarkelf()) {
                            pc.setType(36);
                        } else if (pc.isDragonKnight()) {
                            pc.setType(37);
                        } else if (pc.isIllusionist()) {
                            pc.setType(38);
                        }

                        Timestamp deleteTime = new Timestamp(System.currentTimeMillis() + 604800000L);
                        pc.setDeleteTime(deleteTime);
                        pc.save();
                    } else {
                        if (pc.isCrown()) {
                            pc.setType(0);
                        } else if (pc.isKnight()) {
                            pc.setType(1);
                        } else if (pc.isElf()) {
                            pc.setType(2);
                        } else if (pc.isWizard()) {
                            pc.setType(3);
                        } else if (pc.isDarkelf()) {
                            pc.setType(4);
                        } else if (pc.isDragonKnight()) {
                            pc.setType(5);
                        } else if (pc.isIllusionist()) {
                            pc.setType(6);
                        }

                        pc.setDeleteTime((Timestamp)null);
                        pc.save();
                    }

                    client.out().encrypt(new S_DeleteCharOK(81));
                    return;
                }

                if (pc != null) {
                    L1Clan clan = WorldClan.get().getClan(pc.getClanname());
                    if (clan != null) {
                        clan.delMemberName(name);
                    }
                }

                int countCharacters = client.getAccount().get_countCharacters();
                client.getAccount().set_countCharacters(countCharacters - 1);
                CharObjidTable.get().charRemove(name);
                CharacterTable.get().deleteCharacter(client.getAccountName(), name);
            } catch (Exception var10) {
                client.close();
                return;
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }

            client.out().encrypt(new S_DeleteCharOK(5));
        } catch (Exception var11) {
        } finally {
            this.over();
        }

    }

    public String getType() {
        return this.getClass().getSimpleName();
    }
}
