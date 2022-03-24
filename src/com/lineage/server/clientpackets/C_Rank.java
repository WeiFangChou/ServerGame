package com.lineage.server.clientpackets;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.datatables.sql.CharacterTable;
import com.lineage.server.model.L1Clan;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_PacketBox;
import com.lineage.server.serverpackets.S_PacketBoxMapTimer;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillSound;
import com.lineage.server.world.World;
import com.lineage.server.world.WorldClan;
import java.util.Calendar;
import java.util.Random;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class C_Rank extends ClientBasePacket {
    private static final Log _log = LogFactory.getLog(C_Rank.class);

    public C_Rank() {
    }

    public void start(byte[] decrypt, ClientExecutor client) {
        try {
            try {
                this.read(decrypt);
                String name = "";

                int data;
                int rank;
                try {
                    data = this.readC();
                    rank = this.readC();
                    name = this.readS();
                } catch (Exception var20) {
                    return;
                }

                L1PcInstance pc = client.getActiveChar();
                if (pc == null) {
                    return;
                }

                switch(data) {
                    case 1:
                        this.rank(pc, rank, name);
                        break;
                    case 2:
                    case 3:
                    case 4:
                    case 7:
                    case 8:
                    default:
                        return;
                    case 5:
                        if (pc.get_food() < 225) {
                            return;
                        }

                        if (pc.getWeapon() == null) {
                            pc.sendPackets(new S_ServerMessage(1973));
                            return;
                        }

                        Random random = new Random();
                        long time = pc.get_h_time();
                        Calendar cal = Calendar.getInstance();
                        long h_time = cal.getTimeInMillis() / 1000L;
                        int n = (int)((h_time - time) / 60L);
                        int addhp = 0;
                        if (pc.getWeapon().getEnchantLevel() > -1) {
                            if (n <= 0) {
                                pc.sendPackets(new S_ServerMessage(1974));
                            } else if (n >= 1 && n <= 29) {
                                addhp = (int)((double)pc.getMaxHp() * ((double)n / 100.0D));
                            } else if (n >= 30) {
                                int lv = pc.getWeapon().getEnchantLevel();
                                switch(lv) {
                                    case 0:
                                    case 1:
                                    case 2:
                                    case 3:
                                    case 4:
                                    case 5:
                                    case 6:
                                        pc.sendPacketsAll(new S_SkillSound(pc.getId(), 8907));
                                        pc.sendPacketsAll(new S_SkillSound(pc.getId(), 8684));
                                        addhp = (int)((double)pc.getMaxHp() * ((double)(random.nextInt(20) + 20) / 100.0D));
                                        break;
                                    case 7:
                                    case 8:
                                        pc.sendPacketsAll(new S_SkillSound(pc.getId(), 8909));
                                        pc.sendPacketsAll(new S_SkillSound(pc.getId(), 8685));
                                        addhp = (int)((double)pc.getMaxHp() * ((double)(random.nextInt(20) + 30) / 100.0D));
                                        break;
                                    case 9:
                                    case 10:
                                        pc.sendPacketsAll(new S_SkillSound(pc.getId(), 8910));
                                        pc.sendPacketsAll(new S_SkillSound(pc.getId(), 8773));
                                        addhp = (int)((double)pc.getMaxHp() * ((double)(random.nextInt(10) + 50) / 100.0D));
                                        break;
                                    case 11:
                                    default:
                                        pc.sendPacketsAll(new S_SkillSound(pc.getId(), 8908));
                                        pc.sendPacketsAll(new S_SkillSound(pc.getId(), 8686));
                                        addhp = (int)((double)pc.getMaxHp() * 0.7D);
                                }
                            }

                            if (addhp != 0) {
                                pc.set_food(0);
                                pc.sendPackets(new S_PacketBox(11, 0));
                                pc.setCurrentHp(pc.getCurrentHp() + addhp);
                            }

                            return;
                        }

                        pc.sendPackets(new S_ServerMessage(1974));
                        return;
                    case 6:
                        if (pc.getWeapon() == null) {
                            pc.sendPackets(new S_ServerMessage(1973));
                            return;
                        }

                        if (pc.get_food() < 225) {
                            pc.sendPackets(new S_ServerMessage(1974));
                            return;
                        }

                        if (pc.getWeapon().getEnchantLevel() <= -1) {
                            pc.sendPackets(new S_ServerMessage(1974));
                            return;
                        }

                        int lv = pc.getWeapon().getEnchantLevel();
                        short gfx;
                        switch(lv) {
                            case 0:
                            case 1:
                            case 2:
                            case 3:
                            case 4:
                            case 5:
                            case 6:
                                gfx = 8684;
                                break;
                            case 7:
                            case 8:
                                gfx = 8685;
                                break;
                            case 9:
                            case 10:
                                gfx = 8773;
                                break;
                            case 11:
                            default:
                                gfx = 8686;
                        }

                        pc.sendPacketsAll(new S_SkillSound(pc.getId(), gfx));
                        return;
                    case 9:
                        pc.sendPackets(new S_PacketBoxMapTimer());
                }
            } catch (Exception var21) {
            }

        } finally {
            this.over();
        }
    }

    private void rank(L1PcInstance pc, int rank, String name) {
        L1PcInstance targetPc = World.get().getPlayer(name);
        L1Clan clan = WorldClan.get().getClan(pc.getClanname());
        if (clan != null) {
            boolean isOK = false;
            if (rank >= 2 && rank <= 10) {
                isOK = true;
            }

            if (!isOK) {
                pc.sendPackets(new S_ServerMessage(2149));
            } else if (pc.isCrown()) {
                if (pc.getId() != clan.getLeaderId()) {
                    pc.sendPackets(new S_ServerMessage(785));
                } else {
                    if (targetPc != null) {
                        try {
                            if (pc.getClanid() != targetPc.getClanid()) {
                                pc.sendPackets(new S_ServerMessage(201, name));
                                return;
                            }

                            targetPc.setClanRank(rank);
                            targetPc.save();
                            targetPc.sendPackets(new S_PacketBox(27, rank));
                        } catch (Exception var8) {
                            _log.error(var8.getLocalizedMessage(), var8);
                        }
                    } else {
                        try {
                            L1PcInstance restorePc = CharacterTable.get().restoreCharacter(name);
                            if (restorePc == null || restorePc.getClanid() != pc.getClanid()) {
                                pc.sendPackets(new S_ServerMessage(109, name));
                                return;
                            }

                            restorePc.setClanRank(rank);
                            restorePc.save();
                        } catch (Exception var9) {
                            _log.error(var9.getLocalizedMessage(), var9);
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    }

                }
            } else {
                pc.sendPackets(new S_ServerMessage(518));
            }
        }
    }

    public String getType() {
        return this.getClass().getSimpleName();
    }
}
