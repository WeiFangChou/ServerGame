package com.lineage.server.command;

import com.lineage.server.datatables.SkillsTable;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Location;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.OpcodesServer;
import com.lineage.server.serverpackets.S_AddSkill;
import com.lineage.server.serverpackets.S_BlueMessage;
import com.lineage.server.serverpackets.S_Board;
import com.lineage.server.serverpackets.S_BoardRead;
import com.lineage.server.serverpackets.S_Bonusstats;
import com.lineage.server.serverpackets.S_CastleMaster;
import com.lineage.server.serverpackets.S_ChangeHeading;
import com.lineage.server.serverpackets.S_ChangeName;
import com.lineage.server.serverpackets.S_ChangeShape;
import com.lineage.server.serverpackets.S_CharReset;
import com.lineage.server.serverpackets.S_CharTitle;
import com.lineage.server.serverpackets.S_CharVisualUpdate;
import com.lineage.server.serverpackets.S_Chat;
import com.lineage.server.serverpackets.S_ChatGlobal;
import com.lineage.server.serverpackets.S_ChatOut;
import com.lineage.server.serverpackets.S_ChatWhisperFrom;
import com.lineage.server.serverpackets.S_CurseBlind;
import com.lineage.server.serverpackets.S_DelSkill;
import com.lineage.server.serverpackets.S_Deposit;
import com.lineage.server.serverpackets.S_Dexup;
import com.lineage.server.serverpackets.S_Disconnect;
import com.lineage.server.serverpackets.S_DoActionShop;
import com.lineage.server.serverpackets.S_Drawal;
import com.lineage.server.serverpackets.S_EffectLocation;
import com.lineage.server.serverpackets.S_Exp;
import com.lineage.server.serverpackets.S_FixWeaponList;
import com.lineage.server.serverpackets.S_HPMeter;
import com.lineage.server.serverpackets.S_HPUpdate;
import com.lineage.server.serverpackets.S_HireSoldier;
import com.lineage.server.serverpackets.S_IdentifyDesc;
import com.lineage.server.serverpackets.S_Invis;
import com.lineage.server.serverpackets.S_ItemCount;
import com.lineage.server.serverpackets.S_ItemError;
import com.lineage.server.serverpackets.S_Lawful;
import com.lineage.server.serverpackets.S_Light;
import com.lineage.server.serverpackets.S_Liquor;
import com.lineage.server.serverpackets.S_MPUpdate;
import com.lineage.server.serverpackets.S_Message_YN;
import com.lineage.server.serverpackets.S_NewMaster;
import com.lineage.server.serverpackets.S_NpcChat;
import com.lineage.server.serverpackets.S_OwnCharAttrDef;
import com.lineage.server.serverpackets.S_OwnCharStatus;
import com.lineage.server.serverpackets.S_OwnCharStatus2;
import com.lineage.server.serverpackets.S_PacketBox;
import com.lineage.server.serverpackets.S_Paralysis;
import com.lineage.server.serverpackets.S_PinkName;
import com.lineage.server.serverpackets.S_Poison;
import com.lineage.server.serverpackets.S_RemoveObject;
import com.lineage.server.serverpackets.S_Resurrection;
import com.lineage.server.serverpackets.S_SPMR;
import com.lineage.server.serverpackets.S_SelectTarget;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillBrave;
import com.lineage.server.serverpackets.S_SkillIconBlessOfEva;
import com.lineage.server.serverpackets.S_SkillIconShield;
import com.lineage.server.serverpackets.S_SkillSound;
import com.lineage.server.serverpackets.S_Strup;
import com.lineage.server.serverpackets.S_TaxRate;
import com.lineage.server.serverpackets.S_Trade;
import com.lineage.server.serverpackets.S_TradeAddItem;
import com.lineage.server.serverpackets.S_TradeStatus;
import com.lineage.server.serverpackets.S_TrueTarget;
import com.lineage.server.serverpackets.S_War;
import com.lineage.server.serverpackets.S_WarTime;
import com.lineage.server.serverpackets.S_Weather;
import com.lineage.server.templates.L1Skills;
import com.lineage.server.world.World;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SOpTest extends OpcodesServer {
    private static final Log _log = LogFactory.getLog(SOpTest.class);

    public static void testOpA(L1PcInstance pc, int opid) throws Exception {
        try {
            World.get().broadcastPacketToAll(new S_CastleMaster(opid, pc.getId()));
            System.out.println(String.valueOf(opid) + "/ " + pc.getId());
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public static void testOp(L1PcInstance pc, int opid) throws Exception {
        if (opid != 161 && opid != 151 && opid != 51 && opid != 131 && opid != 30) {
            if (opid == 40) {
                pc.sendPackets(new S_PacketBox(56, 1800));
            } else if (opid == 95) {
                pc.sendPackets(new S_Disconnect());
            } else if (opid == 10) {
                pc.sendPackets(new S_ChatGlobal(pc, "廣播頻道測試"));
            } else if (opid == 76) {
                pc.sendPackets(new S_Chat(pc, "一般頻道測試"));
            } else if (opid == 255) {
                pc.sendPackets(new S_ChatWhisperFrom(pc, "密語交談(接收)頻道測試"));
            } else if (opid == 133) {
                for (L1Object obj : pc.getKnownObjects()) {
                    if (obj instanceof L1NpcInstance) {
                        L1NpcInstance npc = (L1NpcInstance) obj;
                        npc.broadcastPacketX8(new S_NpcChat(npc, String.valueOf(pc.getName()) + " 打3小~~"));
                    }
                }
            } else if (opid == 13) {
                for (L1Object obj2 : pc.getKnownObjects()) {
                    if (obj2 instanceof L1NpcInstance) {
                        L1NpcInstance npc2 = (L1NpcInstance) obj2;
                        npc2.broadcastPacketX8(new S_NewMaster(pc.getName(), npc2));
                    }
                }
            } else if (opid != 126 && opid != 184 && opid != 153 && opid != 212 && opid != 5) {
                if (opid == 145) {
                    pc.sendPackets(new S_OwnCharStatus(pc, 101));
                } else if (opid == 216) {
                    pc.sendPackets(new S_OwnCharStatus2(pc, 103));
                } else if (opid != 50) {
                    if (opid == 202) {
                        StringBuilder title = new StringBuilder();
                        title.append("\\f=測試角色封號");
                        pc.sendPackets(new S_CharTitle(pc.getId(), title));
                    } else if (opid == 252) {
                        for (L1Object obj3 : pc.getKnownObjects()) {
                            if (obj3 instanceof L1NpcInstance) {
                                L1NpcInstance npc3 = (L1NpcInstance) obj3;
                                npc3.broadcastPacketX8(new S_PinkName(npc3.getId(), L1SkillId.STATUS_BRAVE));
                            }
                        }
                        pc.sendPacketsAll(new S_PinkName(pc.getId(), L1SkillId.STATUS_BRAVE));
                    } else if (opid == 66) {
                        pc.sendPackets(new S_CastleMaster(5, pc.getId()));
                    } else if (opid == 33) {
                        pc.sendPackets(new S_CharReset(pc));
                    } else if (opid != 3) {
                        if (opid == 185) {
                            for (L1Object obj4 : pc.getKnownObjects()) {
                                if (obj4 instanceof L1NpcInstance) {
                                    pc.sendPackets(new S_RemoveObject((L1NpcInstance) obj4));
                                }
                            }
                        } else if (opid == 128) {
                            for (L1Object obj5 : pc.getKnownObjects()) {
                                if (obj5 instanceof L1NpcInstance) {
                                    L1NpcInstance npc4 = (L1NpcInstance) obj5;
                                    pc.sendPackets(new S_HPMeter(npc4.getId(), (npc4.getCurrentHp() * 100) / npc4.getMaxHp()));
                                }
                            }
                        } else if (opid != 35 && opid != 63 && opid != 148 && opid != 144) {
                            if (opid == 227) {
                                pc.sendPacketsX8(new S_SkillSound(pc.getId(), 230));
                                pc.resurrect(pc.getMaxHp());
                                pc.setCurrentHp(pc.getMaxHp());
                                pc.startHpRegeneration();
                                pc.startMpRegeneration();
                                pc.stopPcDeleteTimer();
                                pc.sendPacketsAll(new S_Resurrection(pc, (L1Character) pc, 0));
                                pc.sendPacketsAll(new S_CharVisualUpdate(pc));
                                if (pc.getExpRes() == 1 && pc.isGres() && pc.isGresValid()) {
                                    pc.resExp();
                                    pc.setExpRes(0);
                                    pc.setGres(false);
                                }
                            } else if (opid != 122 && opid != 142 && opid != 113) {
                                if (opid == 218) {
                                    pc.sendPackets(new S_DoActionShop(pc.getId(), "物件動作種類(短時間)"));
                                } else if (opid == 232) {
                                    pc.sendPackets(new S_SkillSound(pc.getId(), 3819));
                                    for (int i = 7013; i < 7100; i++) {
                                        System.out.println("i: " + i);
                                        pc.sendPackets(new S_SkillSound(pc.getId(), i, 150));
                                        Thread.sleep(500);
                                    }
                                } else if (opid == 112) {
                                    int x = pc.getX();
                                    int y = pc.getY();
                                    int mapId = pc.getMapId();
                                    pc.sendPackets(new S_EffectLocation(new L1Location(x - 2, y - 2, mapId), 4842));
                                    pc.sendPackets(new S_EffectLocation(new L1Location(x + 2, y - 2, mapId), 4842));
                                    pc.sendPackets(new S_EffectLocation(new L1Location(x + 2, y + 2, mapId), 4842));
                                    pc.sendPackets(new S_EffectLocation(new L1Location(x - 2, y + 2, mapId), 4842));
                                } else if (opid != 16 && opid != 135 && opid != 1) {
                                    if (opid == 123) {
                                        pc.sendPackets(new S_War(7, "測試0血盟", "測試1血盟"));
                                    } else if (opid == 119) {
                                        pc.sendPackets(new S_Bonusstats(pc.getId()));
                                    } else if (opid == 253) {
                                        pc.sendPackets(new S_ItemCount(pc.getId(), L1SkillId.STATUS_BRAVE, "XXXX"));
                                    } else if (opid == 14) {
                                        pc.sendPackets(new S_ServerMessage(96, "測試"));
                                    } else if (opid == 155) {
                                        pc.sendPackets(new S_Message_YN(748));
                                    } else if (opid == 43) {
                                        pc.sendPackets(new S_IdentifyDesc());
                                    } else if (opid == 59) {
                                        pc.sendPackets(new S_BlueMessage(552, "100", "50"));
                                    } else if (opid != 195 && opid != 127) {
                                        if (opid == 53) {
                                            pc.sendPackets(new S_Light(pc.getId(), 20));
                                        } else if (opid == 193) {
                                            pc.sendPackets(new S_Weather(19));
                                        } else if (opid == 199) {
                                            try {
                                                pc.setHeading(0);
                                                pc.sendPackets(new S_ChangeHeading(pc));
                                                Thread.sleep(500);
                                                pc.setHeading(1);
                                                pc.sendPackets(new S_ChangeHeading(pc));
                                                Thread.sleep(500);
                                                pc.setHeading(2);
                                                pc.sendPackets(new S_ChangeHeading(pc));
                                                Thread.sleep(500);
                                                pc.setHeading(3);
                                                pc.sendPackets(new S_ChangeHeading(pc));
                                                Thread.sleep(500);
                                                pc.setHeading(4);
                                                pc.sendPackets(new S_ChangeHeading(pc));
                                                Thread.sleep(500);
                                                pc.setHeading(5);
                                                pc.sendPackets(new S_ChangeHeading(pc));
                                                Thread.sleep(500);
                                                pc.setHeading(6);
                                                pc.sendPackets(new S_ChangeHeading(pc));
                                                Thread.sleep(500);
                                                pc.setHeading(7);
                                                pc.sendPackets(new S_ChangeHeading(pc));
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        } else if (opid != 164) {
                                            if (opid == 81) {
                                                pc.sendPackets(new S_ChangeName(pc.getId(), "更新物件名稱"));
                                            } else if (opid == 42) {
                                                pc.sendPackets(new S_HPUpdate(32767, 32767));
                                            } else if (opid == 73) {
                                                pc.sendPackets(new S_MPUpdate(32767, 32767));
                                            } else if (opid != 150 && opid != 194) {
                                                if (opid == 121) {
                                                    pc.sendPackets(new S_Exp());
                                                } else if (opid == 140) {
                                                    pc.sendPackets(new S_Lawful(pc.getId()));
                                                } else if (opid == 174) {
                                                    pc.sendPackets(new S_SPMR());
                                                } else if (opid == 15) {
                                                    pc.sendPackets(new S_OwnCharAttrDef());
                                                } else if (opid == 64) {
                                                    pc.sendPackets(new S_Board(pc.getId()));
                                                } else if (opid == 56) {
                                                    pc.sendPackets(new S_BoardRead());
                                                } else if (opid != 24 && opid != 44) {
                                                    if (opid == 93) {
                                                        pc.sendPackets(new S_Poison(pc.getId(), 1));
                                                        Thread.sleep(1000);
                                                        pc.sendPackets(new S_Poison(pc.getId(), 2));
                                                        Thread.sleep(1000);
                                                        pc.sendPackets(new S_Poison(pc.getId(), 0));
                                                    } else if (opid == 200) {
                                                        pc.sendPackets(new S_SkillBrave(pc.getId(), 3));
                                                        Thread.sleep(1000);
                                                        pc.sendPackets(new S_SkillBrave(pc.getId(), 5));
                                                        Thread.sleep(1000);
                                                        pc.sendPackets(new S_SkillBrave(pc.getId(), 0));
                                                    } else if (opid == 69) {
                                                        pc.sendPackets(new S_SkillIconShield(5, 3600));
                                                    } else if (opid != 149) {
                                                        if (opid == 110) {
                                                            for (L1Object obj6 : pc.getKnownObjects()) {
                                                                if (obj6 instanceof L1NpcInstance) {
                                                                    L1NpcInstance npc5 = (L1NpcInstance) obj6;
                                                                    npc5.broadcastPacketX8(new S_TrueTarget(npc5.getId(), pc.getId(), "魔法效果 精準目標"));
                                                                }
                                                            }
                                                        } else if (opid == 12) {
                                                            pc.sendPackets(new S_SkillIconBlessOfEva(pc.getId(), 300));
                                                        } else if (opid == 57) {
                                                            pc.sendPackets(new S_Invis(pc.getId(), 1));
                                                            Thread.sleep(1000);
                                                            pc.sendPackets(new S_Invis(pc.getId(), 0));
                                                        } else if (opid == 31) {
                                                            pc.sendPackets(new S_Liquor(pc.getId(), 1));
                                                            Thread.sleep(1000);
                                                            pc.sendPackets(new S_Liquor(pc.getId(), 2));
                                                            Thread.sleep(1000);
                                                            pc.sendPackets(new S_Liquor(pc.getId(), 3));
                                                            Thread.sleep(1000);
                                                            pc.sendPackets(new S_Liquor(pc.getId(), 0));
                                                        } else if (opid == 165) {
                                                            pc.sendPackets(new S_Paralysis(2, true));
                                                        } else if (opid == 28) {
                                                            pc.sendPackets(new S_Dexup(pc, 5, 1800));
                                                        } else if (opid == 120) {
                                                            pc.sendPackets(new S_Strup(pc, 5, 1800));
                                                        } else if (opid == 238) {
                                                            pc.sendPackets(new S_CurseBlind(1));
                                                            Thread.sleep(1000);
                                                            pc.sendPackets(new S_CurseBlind(2));
                                                            Thread.sleep(1000);
                                                            pc.sendPackets(new S_CurseBlind(0));
                                                        } else if (opid != 180 && opid != 222 && opid != 171) {
                                                            if (opid == 48) {
                                                                for (int i2 = 0; i2 < 300; i2++) {
                                                                    L1Skills skill = SkillsTable.get().getTemplate(i2);
                                                                    if (skill != null && skill.getSkillLevel() > 0) {
                                                                        pc.sendPackets(new S_DelSkill(pc, i2));
                                                                    }
                                                                }
                                                                Thread.sleep(2000);
                                                                System.out.println("TEST START!!");
                                                                int[] level = new int[28];
                                                                int dx = 1;
                                                                for (int mode = 0; mode < 28; mode++) {
                                                                    while (dx < 255) {
                                                                        level[mode] = level[mode] + dx;
                                                                        pc.sendPackets(new S_AddSkill(pc, mode, level[mode]));
                                                                        System.out.println("dx: " + dx + " " + level[mode]);
                                                                        dx <<= 1;
                                                                        Thread.sleep(100);
                                                                    }
                                                                    if (dx >= 255) {
                                                                        dx = 1;
                                                                    }
                                                                }
                                                            } else if (opid == 18) {
                                                                for (int i3 = 0; i3 < 300; i3++) {
                                                                    L1Skills skill2 = SkillsTable.get().getTemplate(i3);
                                                                    if (skill2 != null && skill2.getSkillLevel() > 0) {
                                                                        pc.sendPackets(new S_DelSkill(pc, i3));
                                                                    }
                                                                    Thread.sleep(10);
                                                                }
                                                            } else if (opid != 250 && opid != 11) {
                                                                if (opid == 208) {
                                                                    List<L1ItemInstance> weaponList = new ArrayList<>();
                                                                    for (L1ItemInstance item : pc.getInventory().getItems()) {
                                                                        switch (item.getItem().getType2()) {
                                                                            case 1:
                                                                                if (item.get_durability() > 0) {
                                                                                    weaponList.add(item);
                                                                                    break;
                                                                                } else {
                                                                                    break;
                                                                                }
                                                                        }
                                                                    }
                                                                    pc.sendPackets(new S_FixWeaponList(weaponList));
                                                                } else if (opid != 170 && opid != 254) {
                                                                    if (opid == 77) {
                                                                        pc.sendPackets(new S_Trade("測試交易封包"));
                                                                    } else if (opid == 239) {
                                                                        pc.sendPackets(new S_TradeStatus(1));
                                                                    } else if (opid == 86) {
                                                                        pc.sendPackets(new S_TradeAddItem());
                                                                    } else if (opid != 190 && opid != 116 && opid != 84 && opid != 4 && opid != 46) {
                                                                        if (opid == 177) {
                                                                            pc.sendPackets(new S_SelectTarget(pc.getId()));
                                                                        } else if (opid == 203) {
                                                                            pc.sendPackets(new S_Deposit(pc.getId()));
                                                                        } else if (opid == 224) {
                                                                            pc.sendPackets(new S_Drawal(pc.getId(), 1234567));
                                                                        } else if (opid == 126) {
                                                                            pc.sendPackets(new S_HireSoldier(pc));
                                                                        } else if (opid == 72) {
                                                                            pc.sendPackets(new S_TaxRate(pc.getId()));
                                                                        } else if (opid == 49) {
                                                                            pc.sendPackets(new S_WarTime(49));
                                                                        } else if (opid != 90) {
                                                                            if (opid == 204) {
                                                                                for (L1Object obj7 : pc.getKnownObjects()) {
                                                                                    if (obj7 instanceof L1NpcInstance) {
                                                                                        L1NpcInstance npc6 = (L1NpcInstance) obj7;
                                                                                        npc6.broadcastPacketX8(new S_ChangeShape(pc, npc6, 1080));
                                                                                    }
                                                                                }
                                                                            } else if (opid != 88) {
                                                                                if (opid == 17) {
                                                                                    pc.sendPackets(new S_ChatOut(pc));
                                                                                } else if (opid != 13 && opid != 0 && opid != -71 && opid == 89) {
                                                                                    for (int i4 = 0; i4 < 10; i4++) {
                                                                                        pc.sendPackets(new S_ItemError(i4));
                                                                                        Thread.sleep(250);
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
