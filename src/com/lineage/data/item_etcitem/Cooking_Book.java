package com.lineage.data.item_etcitem;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1EffectInstance;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_PacketBoxCooking;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillSound;
import com.lineage.server.world.World;
import java.util.Iterator;
import java.util.Random;

public class Cooking_Book extends ItemExecutor {
    private Cooking_Book() {
    }

    public static ItemExecutor get() {
        return new Cooking_Book();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        int itemId = item.getItemId();
        int cookStatus = data[0];
        int cookNo = data[1];
        if (cookStatus == 0) {
            pc.sendPackets(new S_PacketBoxCooking(itemId - 41255));
        } else {
            makeCooking(pc, cookNo);
        }
    }

    private void makeCooking(L1PcInstance pc, int cookNo) throws Exception {
        Random random = new Random();
        boolean isNearFire = false;
        Iterator<L1Object> it = World.get().getVisibleObjects(pc, 3).iterator();
        while (true) {
            if (it.hasNext()) {
                L1Object obj = it.next();
                if ((obj instanceof L1EffectInstance) && ((L1EffectInstance) obj).getGfxId() == 5943) {
                    isNearFire = true;
                    break;
                }
            } else {
                break;
            }
        }
        if (!isNearFire) {
            pc.sendPackets(new S_ServerMessage(1160));
        } else if (pc.getMaxWeight() <= ((double) pc.getInventory().getWeight())) {
            pc.sendPackets(new S_ServerMessage(1103));
        } else if (!pc.hasSkillEffect(L1SkillId.COOKING_NOW)) {
            pc.setSkillEffect(L1SkillId.COOKING_NOW, 3000);
            int chance = random.nextInt(100) + 1;
            boolean is6392 = false;
            boolean is6390 = false;
            boolean is6394 = false;
            boolean isErr = false;
            int itemid = 0;
            switch (cookNo) {
                case 0:
                    if (!pc.getInventory().checkItem(40057, 1)) {
                        isErr = true;
                        break;
                    } else {
                        pc.getInventory().consumeItem(40057, 1);
                        if (chance < 1 || chance > 90) {
                            if (chance < 91 || chance > 95) {
                                if (chance >= 96 && chance <= 100) {
                                    is6394 = true;
                                    break;
                                }
                            } else {
                                itemid = 41285;
                                is6390 = true;
                                break;
                            }
                        } else {
                            itemid = 41277;
                            is6392 = true;
                            break;
                        }
                    }
                    break;
                case 1:
                    if (!pc.getInventory().checkItem(41275, 1)) {
                        isErr = true;
                        break;
                    } else {
                        pc.getInventory().consumeItem(41275, 1);
                        if (chance < 1 || chance > 90) {
                            if (chance < 91 || chance > 95) {
                                if (chance >= 96 && chance <= 100) {
                                    is6394 = true;
                                    break;
                                }
                            } else {
                                itemid = 41286;
                                is6390 = true;
                                break;
                            }
                        } else {
                            itemid = 41278;
                            is6392 = true;
                            break;
                        }
                    }
                    break;
                case 2:
                    if (pc.getInventory().checkItem(41263, 1) && pc.getInventory().checkItem(41265, 1)) {
                        pc.getInventory().consumeItem(41263, 1);
                        pc.getInventory().consumeItem(41265, 1);
                        if (chance < 1 || chance > 90) {
                            if (chance < 91 || chance > 95) {
                                if (chance >= 96 && chance <= 100) {
                                    is6394 = true;
                                    break;
                                }
                            } else {
                                itemid = 41287;
                                is6390 = true;
                                break;
                            }
                        } else {
                            itemid = 41279;
                            is6392 = true;
                            break;
                        }
                    } else {
                        isErr = true;
                        break;
                    }
                    break;
                case 3:
                    if (pc.getInventory().checkItem(41274, 1) && pc.getInventory().checkItem(41267, 1)) {
                        pc.getInventory().consumeItem(41274, 1);
                        pc.getInventory().consumeItem(41267, 1);
                        if (chance < 1 || chance > 90) {
                            if (chance < 91 || chance > 95) {
                                if (chance >= 96 && chance <= 100) {
                                    is6394 = true;
                                    break;
                                }
                            } else {
                                itemid = 41288;
                                is6390 = true;
                                break;
                            }
                        } else {
                            itemid = 41280;
                            is6392 = true;
                            break;
                        }
                    } else {
                        isErr = true;
                        break;
                    }
                    break;
                case 4:
                    if (pc.getInventory().checkItem(40062, 1) && pc.getInventory().checkItem(40069, 1) && pc.getInventory().checkItem(40064, 1)) {
                        pc.getInventory().consumeItem(40062, 1);
                        pc.getInventory().consumeItem(40069, 1);
                        pc.getInventory().consumeItem(40064, 1);
                        if (chance < 1 || chance > 90) {
                            if (chance < 91 || chance > 95) {
                                if (chance >= 96 && chance <= 100) {
                                    is6394 = true;
                                    break;
                                }
                            } else {
                                itemid = 41289;
                                is6390 = true;
                                break;
                            }
                        } else {
                            itemid = 41281;
                            is6392 = true;
                            break;
                        }
                    } else {
                        isErr = true;
                        break;
                    }
                    break;
                case 5:
                    if (pc.getInventory().checkItem(40056, 1) && pc.getInventory().checkItem(40060, 1) && pc.getInventory().checkItem(40061, 1)) {
                        pc.getInventory().consumeItem(40056, 1);
                        pc.getInventory().consumeItem(40060, 1);
                        pc.getInventory().consumeItem(40061, 1);
                        if (chance < 1 || chance > 90) {
                            if (chance < 91 || chance > 95) {
                                if (chance >= 96 && chance <= 100) {
                                    is6394 = true;
                                    break;
                                }
                            } else {
                                itemid = 41290;
                                is6390 = true;
                                break;
                            }
                        } else {
                            itemid = 41282;
                            is6392 = true;
                            break;
                        }
                    } else {
                        isErr = true;
                        break;
                    }
                    break;
                case 6:
                    if (!pc.getInventory().checkItem(41276, 1)) {
                        isErr = true;
                        break;
                    } else {
                        pc.getInventory().consumeItem(41276, 1);
                        if (chance < 1 || chance > 90) {
                            if (chance < 91 || chance > 95) {
                                if (chance >= 96 && chance <= 100) {
                                    is6394 = true;
                                    break;
                                }
                            } else {
                                itemid = 41291;
                                is6390 = true;
                                break;
                            }
                        } else {
                            itemid = 41283;
                            is6392 = true;
                            break;
                        }
                    }
                    break;
                case 7:
                    if (pc.getInventory().checkItem(40499, 1) && pc.getInventory().checkItem(40060, 1)) {
                        pc.getInventory().consumeItem(40499, 1);
                        pc.getInventory().consumeItem(40060, 1);
                        if (chance < 1 || chance > 90) {
                            if (chance < 91 || chance > 95) {
                                if (chance >= 96 && chance <= 100) {
                                    is6394 = true;
                                    break;
                                }
                            } else {
                                itemid = 41292;
                                is6390 = true;
                                break;
                            }
                        } else {
                            itemid = 41284;
                            is6392 = true;
                            break;
                        }
                    } else {
                        isErr = true;
                        break;
                    }
                    break;
                case 8:
                    if (pc.getInventory().checkItem(49040, 1) && pc.getInventory().checkItem(49048, 1)) {
                        pc.getInventory().consumeItem(49040, 1);
                        pc.getInventory().consumeItem(49048, 1);
                        if (chance < 1 || chance > 90) {
                            if (chance < 91 || chance > 95) {
                                if (chance >= 96 && chance <= 100) {
                                    is6394 = true;
                                    break;
                                }
                            } else {
                                itemid = 49057;
                                is6390 = true;
                                break;
                            }
                        } else {
                            itemid = 49049;
                            is6392 = true;
                            break;
                        }
                    } else {
                        isErr = true;
                        break;
                    }
                    break;
                case 9:
                    if (pc.getInventory().checkItem(49041, 1) && pc.getInventory().checkItem(49048, 1)) {
                        pc.getInventory().consumeItem(49041, 1);
                        pc.getInventory().consumeItem(49048, 1);
                        if (chance < 1 || chance > 90) {
                            if (chance < 91 || chance > 95) {
                                if (chance >= 96 && chance <= 100) {
                                    is6394 = true;
                                    break;
                                }
                            } else {
                                itemid = 49058;
                                is6390 = true;
                                break;
                            }
                        } else {
                            itemid = 49050;
                            is6392 = true;
                            break;
                        }
                    } else {
                        isErr = true;
                        break;
                    }
                    break;
                case 10:
                    if (pc.getInventory().checkItem(49042, 1) && pc.getInventory().checkItem(41265, 1) && pc.getInventory().checkItem(49048, 1)) {
                        pc.getInventory().consumeItem(49042, 1);
                        pc.getInventory().consumeItem(41265, 1);
                        pc.getInventory().consumeItem(49048, 1);
                        if (chance < 1 || chance > 90) {
                            if (chance < 91 || chance > 95) {
                                if (chance >= 96 && chance <= 100) {
                                    is6394 = true;
                                    break;
                                }
                            } else {
                                itemid = 49059;
                                is6390 = true;
                                break;
                            }
                        } else {
                            itemid = 49051;
                            is6392 = true;
                            break;
                        }
                    } else {
                        isErr = true;
                        break;
                    }
                    break;
                case 11:
                    if (pc.getInventory().checkItem(49043, 1) && pc.getInventory().checkItem(49048, 1)) {
                        pc.getInventory().consumeItem(49043, 1);
                        pc.getInventory().consumeItem(49048, 1);
                        if (chance < 1 || chance > 90) {
                            if (chance < 91 || chance > 95) {
                                if (chance >= 96 && chance <= 100) {
                                    is6394 = true;
                                    break;
                                }
                            } else {
                                itemid = 49060;
                                is6390 = true;
                                break;
                            }
                        } else {
                            itemid = 49052;
                            is6392 = true;
                            break;
                        }
                    } else {
                        isErr = true;
                        break;
                    }
                    break;
                case 12:
                    if (pc.getInventory().checkItem(49044, 1) && pc.getInventory().checkItem(49048, 1)) {
                        pc.getInventory().consumeItem(49044, 1);
                        pc.getInventory().consumeItem(49048, 1);
                        if (chance < 1 || chance > 90) {
                            if (chance < 91 || chance > 95) {
                                if (chance >= 96 && chance <= 100) {
                                    is6394 = true;
                                    break;
                                }
                            } else {
                                itemid = 49061;
                                is6390 = true;
                                break;
                            }
                        } else {
                            itemid = 49053;
                            is6392 = true;
                            break;
                        }
                    } else {
                        isErr = true;
                        break;
                    }
                    break;
                case 13:
                    if (pc.getInventory().checkItem(49045, 1) && pc.getInventory().checkItem(49048, 1)) {
                        pc.getInventory().consumeItem(49045, 1);
                        pc.getInventory().consumeItem(49048, 1);
                        if (chance < 1 || chance > 90) {
                            if (chance < 91 || chance > 95) {
                                if (chance >= 96 && chance <= 100) {
                                    is6394 = true;
                                    break;
                                }
                            } else {
                                itemid = 49062;
                                is6390 = true;
                                break;
                            }
                        } else {
                            itemid = 49054;
                            is6392 = true;
                            break;
                        }
                    } else {
                        isErr = true;
                        break;
                    }
                    break;
                case 14:
                    if (pc.getInventory().checkItem(49046, 1) && pc.getInventory().checkItem(49048, 1)) {
                        pc.getInventory().consumeItem(49046, 1);
                        pc.getInventory().consumeItem(49048, 1);
                        if (chance < 1 || chance > 30) {
                            if (chance < 31 || chance > 65) {
                                if (chance >= 66 && chance <= 100) {
                                    is6394 = true;
                                    break;
                                }
                            } else {
                                itemid = 49063;
                                is6390 = true;
                                break;
                            }
                        } else {
                            itemid = 49055;
                            is6392 = true;
                            break;
                        }
                    } else {
                        isErr = true;
                        break;
                    }
                    break;
                case 15:
                    if (pc.getInventory().checkItem(49047, 1) && pc.getInventory().checkItem(40499, 1) && pc.getInventory().checkItem(49048, 1)) {
                        pc.getInventory().consumeItem(49047, 1);
                        pc.getInventory().consumeItem(40499, 1);
                        pc.getInventory().consumeItem(49048, 1);
                        if (chance < 1 || chance > 90) {
                            if (chance < 91 || chance > 95) {
                                if (chance >= 96 && chance <= 100) {
                                    is6394 = true;
                                    break;
                                }
                            } else {
                                itemid = 49064;
                                is6390 = true;
                                break;
                            }
                        } else {
                            itemid = 49056;
                            is6392 = true;
                            break;
                        }
                    } else {
                        isErr = true;
                        break;
                    }
                    break;
                case 16:
                    if (pc.getInventory().checkItem(49048, 1) && pc.getInventory().checkItem(49243, 1) && pc.getInventory().checkItem(49260, 1)) {
                        pc.getInventory().consumeItem(49048, 1);
                        pc.getInventory().consumeItem(49243, 1);
                        pc.getInventory().consumeItem(49260, 1);
                        if (chance < 1 || chance > 90) {
                            if (chance < 91 || chance > 95) {
                                if (chance >= 96 && chance <= 100) {
                                    is6394 = true;
                                    break;
                                }
                            } else {
                                itemid = 49252;
                                is6390 = true;
                                break;
                            }
                        } else {
                            itemid = 49244;
                            is6392 = true;
                            break;
                        }
                    } else {
                        isErr = true;
                        break;
                    }
                    break;
                case 17:
                    if (pc.getInventory().checkItem(49048, 1) && pc.getInventory().checkItem(49243, 1) && pc.getInventory().checkItem(49261, 1)) {
                        pc.getInventory().consumeItem(49048, 1);
                        pc.getInventory().consumeItem(49243, 1);
                        pc.getInventory().consumeItem(49261, 1);
                        if (chance < 1 || chance > 90) {
                            if (chance < 91 || chance > 95) {
                                if (chance >= 96 && chance <= 100) {
                                    is6394 = true;
                                    break;
                                }
                            } else {
                                itemid = 49253;
                                is6390 = true;
                                break;
                            }
                        } else {
                            itemid = 49245;
                            is6392 = true;
                            break;
                        }
                    } else {
                        isErr = true;
                        break;
                    }
                    break;
                case 18:
                    if (pc.getInventory().checkItem(49048, 1) && pc.getInventory().checkItem(49243, 1) && pc.getInventory().checkItem(49262, 1)) {
                        pc.getInventory().consumeItem(49048, 1);
                        pc.getInventory().consumeItem(49243, 1);
                        pc.getInventory().consumeItem(49262, 1);
                        if (chance < 1 || chance > 90) {
                            if (chance < 91 || chance > 95) {
                                if (chance >= 96 && chance <= 100) {
                                    is6394 = true;
                                    break;
                                }
                            } else {
                                itemid = 49254;
                                is6390 = true;
                                break;
                            }
                        } else {
                            itemid = 49246;
                            is6392 = true;
                            break;
                        }
                    } else {
                        isErr = true;
                        break;
                    }
                    break;
                case 19:
                    if (pc.getInventory().checkItem(49048, 1) && pc.getInventory().checkItem(49243, 1) && pc.getInventory().checkItem(49263, 1)) {
                        pc.getInventory().consumeItem(49048, 1);
                        pc.getInventory().consumeItem(49243, 1);
                        pc.getInventory().consumeItem(49263, 1);
                        if (chance < 1 || chance > 90) {
                            if (chance < 91 || chance > 95) {
                                if (chance >= 96 && chance <= 100) {
                                    is6394 = true;
                                    break;
                                }
                            } else {
                                itemid = 49255;
                                is6390 = true;
                                break;
                            }
                        } else {
                            itemid = 49247;
                            is6392 = true;
                            break;
                        }
                    } else {
                        isErr = true;
                        break;
                    }
                    break;
                case 20:
                    if (pc.getInventory().checkItem(49048, 1) && pc.getInventory().checkItem(49243, 1) && pc.getInventory().checkItem(49264, 1)) {
                        pc.getInventory().consumeItem(49048, 1);
                        pc.getInventory().consumeItem(49243, 1);
                        pc.getInventory().consumeItem(49264, 1);
                        if (chance < 1 || chance > 90) {
                            if (chance < 91 || chance > 95) {
                                if (chance >= 96 && chance <= 100) {
                                    is6394 = true;
                                    break;
                                }
                            } else {
                                itemid = 49256;
                                is6390 = true;
                                break;
                            }
                        } else {
                            itemid = 49248;
                            is6392 = true;
                            break;
                        }
                    } else {
                        isErr = true;
                        break;
                    }
                    break;
                case 21:
                    if (pc.getInventory().checkItem(49048, 1) && pc.getInventory().checkItem(49243, 1) && pc.getInventory().checkItem(49265, 1)) {
                        pc.getInventory().consumeItem(49048, 1);
                        pc.getInventory().consumeItem(49243, 1);
                        pc.getInventory().consumeItem(49265, 1);
                        if (chance < 1 || chance > 90) {
                            if (chance < 91 || chance > 95) {
                                if (chance >= 96 && chance <= 100) {
                                    is6394 = true;
                                    break;
                                }
                            } else {
                                itemid = 49257;
                                is6390 = true;
                                break;
                            }
                        } else {
                            itemid = 49249;
                            is6392 = true;
                            break;
                        }
                    } else {
                        isErr = true;
                        break;
                    }
                    break;
                case 22:
                    if (pc.getInventory().checkItem(49048, 1) && pc.getInventory().checkItem(49243, 1) && pc.getInventory().checkItem(49266, 1)) {
                        pc.getInventory().consumeItem(49048, 1);
                        pc.getInventory().consumeItem(49243, 1);
                        pc.getInventory().consumeItem(49266, 1);
                        if (chance < 1 || chance > 90) {
                            if (chance < 91 || chance > 95) {
                                if (chance >= 96 && chance <= 100) {
                                    is6394 = true;
                                    break;
                                }
                            } else {
                                itemid = 49258;
                                is6390 = true;
                                break;
                            }
                        } else {
                            itemid = 49250;
                            is6392 = true;
                            break;
                        }
                    } else {
                        isErr = true;
                        break;
                    }
                    break;
                case 23:
                    if (pc.getInventory().checkItem(49048, 1) && pc.getInventory().checkItem(49243, 1) && pc.getInventory().checkItem(49267, 1)) {
                        pc.getInventory().consumeItem(49048, 1);
                        pc.getInventory().consumeItem(49243, 1);
                        pc.getInventory().consumeItem(49267, 1);
                        if (chance < 1 || chance > 90) {
                            if (chance < 91 || chance > 95) {
                                if (chance >= 96 && chance <= 100) {
                                    is6394 = true;
                                    break;
                                }
                            } else {
                                itemid = 49259;
                                is6390 = true;
                                break;
                            }
                        } else {
                            itemid = 49251;
                            is6392 = true;
                            break;
                        }
                    } else {
                        isErr = true;
                        break;
                    }
                    break;
            }
            if (is6392) {
                if (itemid != 0) {
                    CreateNewItem.createNewItem(pc, itemid, 1);
                }
                pc.broadcastPacketX10(new S_SkillSound(pc.getId(), 6392));
            }
            if (is6390) {
                if (itemid != 0) {
                    CreateNewItem.createNewItem(pc, itemid, 1);
                }
                pc.sendPacketsAll(new S_SkillSound(pc.getId(), 6390));
            }
            if (is6394) {
                pc.sendPackets(new S_ServerMessage(1101));
                pc.broadcastPacketX10(new S_SkillSound(pc.getId(), 6394));
            }
            if (isErr) {
                pc.sendPackets(new S_ServerMessage(1102));
            }
        }
    }
}
