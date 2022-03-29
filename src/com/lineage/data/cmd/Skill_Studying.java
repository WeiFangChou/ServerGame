package com.lineage.data.cmd;

import com.lineage.server.datatables.SkillsTable;
import com.lineage.server.datatables.lock.CharSkillReading;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_AddSkill;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.serverpackets.S_HPUpdate;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillSound;

public class Skill_Studying implements Skill_StudyingExecutor {
    @Override // com.lineage.data.cmd.Skill_StudyingExecutor
    public void magic(L1PcInstance pc, int skillId, int magicAtt, int attribute, int itemObj) throws Exception {
        int pclvl = pc.getLevel();
        boolean isUse = true;
        if (!pc.isCrown()) {
            if (!pc.isKnight()) {
                if (!pc.isWizard()) {
                    if (!pc.isElf()) {
                        if (!pc.isDarkelf()) {
                            if (!pc.isDragonKnight()) {
                                if (pc.isIllusionist()) {
                                    switch (magicAtt) {
                                        case 61:
                                            if (pclvl < 10) {
                                                isUse = false;
                                                break;
                                            } else {
                                                mapPosition(pc, skillId, attribute, itemObj);
                                                break;
                                            }
                                        case 62:
                                            if (pclvl < 20) {
                                                isUse = false;
                                                break;
                                            } else {
                                                mapPosition(pc, skillId, attribute, itemObj);
                                                break;
                                            }
                                        case 63:
                                            if (pclvl < 30) {
                                                isUse = false;
                                                break;
                                            } else {
                                                mapPosition(pc, skillId, attribute, itemObj);
                                                break;
                                            }
                                        case 64:
                                            if (pclvl < 40) {
                                                isUse = false;
                                                break;
                                            } else {
                                                mapPosition(pc, skillId, attribute, itemObj);
                                                break;
                                            }
                                        default:
                                            pc.sendPackets(new S_ServerMessage(79));
                                            break;
                                    }
                                }
                            } else {
                                switch (magicAtt) {
                                    case 51:
                                        if (pclvl < 15) {
                                            isUse = false;
                                            break;
                                        } else {
                                            mapPosition(pc, skillId, attribute, itemObj);
                                            break;
                                        }
                                    case 52:
                                        if (pclvl < 30) {
                                            isUse = false;
                                            break;
                                        } else {
                                            mapPosition(pc, skillId, attribute, itemObj);
                                            break;
                                        }
                                    case 53:
                                        if (pclvl < 45) {
                                            isUse = false;
                                            break;
                                        } else {
                                            mapPosition(pc, skillId, attribute, itemObj);
                                            break;
                                        }
                                    default:
                                        pc.sendPackets(new S_ServerMessage(79));
                                        break;
                                }
                            }
                        } else {
                            switch (magicAtt) {
                                case 1:
                                    if (pclvl < 12) {
                                        isUse = false;
                                        break;
                                    } else {
                                        mapPosition(pc, skillId, attribute, itemObj);
                                        break;
                                    }
                                case 2:
                                    if (pclvl < 24) {
                                        isUse = false;
                                        break;
                                    } else {
                                        mapPosition(pc, skillId, attribute, itemObj);
                                        break;
                                    }
                                case 41:
                                    if (pclvl < 15) {
                                        isUse = false;
                                        break;
                                    } else {
                                        mapPosition(pc, skillId, attribute, itemObj);
                                        break;
                                    }
                                case 42:
                                    if (pclvl < 30) {
                                        isUse = false;
                                        break;
                                    } else {
                                        mapPosition(pc, skillId, attribute, itemObj);
                                        break;
                                    }
                                case 43:
                                    if (pclvl < 45) {
                                        isUse = false;
                                        break;
                                    } else {
                                        mapPosition(pc, skillId, attribute, itemObj);
                                        break;
                                    }
                                case 44:
                                    if (pclvl < 60) {
                                        isUse = false;
                                        break;
                                    } else {
                                        mapPosition(pc, skillId, attribute, itemObj);
                                        break;
                                    }
                                default:
                                    pc.sendPackets(new S_ServerMessage(79));
                                    break;
                            }
                        }
                    } else {
                        switch (magicAtt) {
                            case 1:
                                if (pclvl < 8) {
                                    isUse = false;
                                    break;
                                } else {
                                    mapPosition(pc, skillId, attribute, itemObj);
                                    break;
                                }
                            case 2:
                                if (pclvl < 16) {
                                    isUse = false;
                                    break;
                                } else {
                                    mapPosition(pc, skillId, attribute, itemObj);
                                    break;
                                }
                            case 3:
                                if (pclvl < 24) {
                                    isUse = false;
                                    break;
                                } else {
                                    mapPosition(pc, skillId, attribute, itemObj);
                                    break;
                                }
                            case 4:
                                if (pclvl < 32) {
                                    isUse = false;
                                    break;
                                } else {
                                    mapPosition(pc, skillId, attribute, itemObj);
                                    break;
                                }
                            case 5:
                                if (pclvl < 40) {
                                    isUse = false;
                                    break;
                                } else {
                                    mapPosition(pc, skillId, attribute, itemObj);
                                    break;
                                }
                            case 6:
                                if (pclvl < 48) {
                                    isUse = false;
                                    break;
                                } else {
                                    mapPosition(pc, skillId, attribute, itemObj);
                                    break;
                                }
                            case 7:
                            case 8:
                            case 9:
                            case 10:
                            default:
                                pc.sendPackets(new S_ServerMessage(79));
                                break;
                            case 11:
                                if (pclvl < 10) {
                                    isUse = false;
                                    break;
                                } else {
                                    mapPosition(pc, skillId, attribute, itemObj);
                                    break;
                                }
                            case 12:
                                if (pclvl < 20) {
                                    isUse = false;
                                    break;
                                } else {
                                    mapPosition(pc, skillId, attribute, itemObj);
                                    break;
                                }
                            case 13:
                                if (pclvl < 30) {
                                    isUse = false;
                                    break;
                                } else {
                                    mapPosition(pc, skillId, attribute, itemObj);
                                    break;
                                }
                            case 14:
                                if (pclvl < 40) {
                                    isUse = false;
                                    break;
                                } else {
                                    mapPosition(pc, skillId, attribute, itemObj);
                                    break;
                                }
                            case 15:
                                if (pclvl < 50) {
                                    isUse = false;
                                    break;
                                } else {
                                    mapPosition(pc, skillId, attribute, itemObj);
                                    break;
                                }
                        }
                    }
                } else {
                    switch (magicAtt) {
                        case 1:
                            if (pclvl < 4) {
                                isUse = false;
                                break;
                            } else {
                                mapPosition(pc, skillId, attribute, itemObj);
                                break;
                            }
                        case 2:
                            if (pclvl < 8) {
                                isUse = false;
                                break;
                            } else {
                                mapPosition(pc, skillId, attribute, itemObj);
                                break;
                            }
                        case 3:
                            if (pclvl < 12) {
                                isUse = false;
                                break;
                            } else {
                                mapPosition(pc, skillId, attribute, itemObj);
                                break;
                            }
                        case 4:
                            if (pclvl < 16) {
                                isUse = false;
                                break;
                            } else {
                                mapPosition(pc, skillId, attribute, itemObj);
                                break;
                            }
                        case 5:
                            if (pclvl < 20) {
                                isUse = false;
                                break;
                            } else {
                                mapPosition(pc, skillId, attribute, itemObj);
                                break;
                            }
                        case 6:
                            if (pclvl < 24) {
                                isUse = false;
                                break;
                            } else {
                                mapPosition(pc, skillId, attribute, itemObj);
                                break;
                            }
                        case 7:
                            if (pclvl < 28) {
                                isUse = false;
                                break;
                            } else {
                                mapPosition(pc, skillId, attribute, itemObj);
                                break;
                            }
                        case 8:
                            if (pclvl < 32) {
                                isUse = false;
                                break;
                            } else {
                                mapPosition(pc, skillId, attribute, itemObj);
                                break;
                            }
                        case 9:
                            if (pclvl < 36) {
                                isUse = false;
                                break;
                            } else {
                                mapPosition(pc, skillId, attribute, itemObj);
                                break;
                            }
                        case 10:
                            if (pclvl < 40) {
                                isUse = false;
                                break;
                            } else {
                                mapPosition(pc, skillId, attribute, itemObj);
                                break;
                            }
                        default:
                            pc.sendPackets(new S_ServerMessage(79));
                            break;
                    }
                }
            } else {
                switch (magicAtt) {
                    case 1:
                        if (pclvl < 50) {
                            isUse = false;
                            break;
                        } else {
                            mapPosition(pc, skillId, attribute, itemObj);
                            break;
                        }
                    case 31:
                        if (pclvl < 50) {
                            isUse = false;
                            break;
                        } else {
                            mapPosition(pc, skillId, attribute, itemObj);
                            break;
                        }
                    case 32:
                        if (pclvl < 60) {
                            isUse = false;
                            break;
                        } else {
                            mapPosition(pc, skillId, attribute, itemObj);
                            break;
                        }
                    default:
                        pc.sendPackets(new S_ServerMessage(79));
                        break;
                }
            }
        } else {
            switch (magicAtt) {
                case 1:
                    if (pclvl < 10) {
                        isUse = false;
                        break;
                    } else {
                        mapPosition(pc, skillId, attribute, itemObj);
                        break;
                    }
                case 2:
                    if (pclvl < 20) {
                        isUse = false;
                        break;
                    } else {
                        mapPosition(pc, skillId, attribute, itemObj);
                        break;
                    }
                case 21:
                    if (pclvl < 15) {
                        isUse = false;
                        break;
                    } else {
                        mapPosition(pc, skillId, attribute, itemObj);
                        break;
                    }
                case 22:
                    if (pclvl < 30) {
                        isUse = false;
                        break;
                    } else {
                        mapPosition(pc, skillId, attribute, itemObj);
                        break;
                    }
                case 23:
                    if (pclvl < 40) {
                        isUse = false;
                        break;
                    } else {
                        mapPosition(pc, skillId, attribute, itemObj);
                        break;
                    }
                case 24:
                    if (pclvl < 45) {
                        isUse = false;
                        break;
                    } else {
                        mapPosition(pc, skillId, attribute, itemObj);
                        break;
                    }
                case 25:
                    if (pclvl < 50) {
                        isUse = false;
                        break;
                    } else {
                        mapPosition(pc, skillId, attribute, itemObj);
                        break;
                    }
                case 26:
                    if (pclvl < 55) {
                        isUse = false;
                        break;
                    } else {
                        mapPosition(pc, skillId, attribute, itemObj);
                        break;
                    }
                default:
                    pc.sendPackets(new S_ServerMessage(79));
                    break;
            }
        }
        if (!isUse) {
            pc.sendPackets(new S_ServerMessage(312));
        }
    }

    private void mapPosition(L1PcInstance pc, int skillId, int attribute, int itemObj) throws Exception {
        int x = pc.getX();
        int y = pc.getY();
        int m = pc.getMapId();
        switch (attribute) {
            case 0:
                if ((x <= 32880 || x >= 32892 || y <= 32646 || y >= 32658 || m != 4) && ((x <= 32662 || x >= 32674 || y <= 32297 || y >= 32309 || m != 4) && ((x <= 33135 || x >= 33146 || y <= 32232 || y >= 32249 || m != 4) && ((x <= 33116 || x >= 33128 || y <= 32930 || y >= 32942 || m != 4) && (x <= 32791 || x >= 32796 || y <= 32842 || y >= 32848 || m != 76))))) {
                    pc.sendPackets(new S_ServerMessage(79));
                    return;
                } else {
                    spellBook(pc, skillId, itemObj);
                    return;
                }
            case 1:
                if ((x > 33116 && x < 33128 && y > 32930 && y < 32942 && m == 4) || ((x > 33135 && x < 33146 && y > 32232 && y < 32249 && m == 4) || (x > 32791 && x < 32796 && y > 32842 && y < 32848 && m == 76))) {
                    spellBook(pc, skillId, itemObj);
                    return;
                } else if ((x <= 32880 || x >= 32892 || y <= 32646 || y >= 32658 || m != 4) && (x <= 32662 || x >= 32674 || y <= 32297 || y >= 32309 || m != 4)) {
                    pc.sendPackets(new S_ServerMessage(79));
                    return;
                } else {
                    pc.getInventory().removeItem(pc.getInventory().getItem(itemObj), 1);
                    pc.receiveDamage(pc, (double) ( ((int) ((Math.random() * 50.0d) + 30.0d))), false, true);
                    if (pc.isInvisble()) {
                        pc.delInvis();
                    }
                    pc.sendPacketsX8(new S_SkillSound(pc.getId(), 10));
                    pc.sendPacketsX8(new S_DoActionGFX(pc.getId(), 2));
                    pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
                    if (pc.isInParty()) {
                        pc.getParty().updateMiniHP(pc);
                        return;
                    }
                    return;
                }
            case 2:
                if ((x > 32880 && x < 32892 && y > 32646 && y < 32658 && m == 4) || (x > 32662 && x < 32674 && y > 32297 && y < 32309 && m == 4)) {
                    spellBook(pc, skillId, itemObj);
                    return;
                } else if ((x <= 33116 || x >= 33128 || y <= 32930 || y >= 32942 || m != 4) && ((x <= 33135 || x >= 33146 || y <= 32232 || y >= 32249 || m != 4) && (x <= 32791 || x >= 32796 || y <= 32842 || y >= 32848 || m != 76))) {
                    pc.sendPackets(new S_ServerMessage(79));
                    return;
                } else {
                    pc.getInventory().removeItem(pc.getInventory().getItem(itemObj), 1);
                    pc.receiveDamage(pc, (double) ( ((int) ((Math.random() * 50.0d) + 30.0d))), false, true);
                    if (pc.isInvisble()) {
                        pc.delInvis();
                    }
                    pc.sendPacketsX8(new S_SkillSound(pc.getId(), 10));
                    pc.sendPacketsX8(new S_DoActionGFX(pc.getId(), 2));
                    pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
                    if (pc.isInParty()) {
                        pc.getParty().updateMiniHP(pc);
                        return;
                    }
                    return;
                }
            case 3:
                if ((x <= 33049 || x >= 33061 || y <= 32330 || y >= 32343 || m != 4) && (x <= 32788 || x >= 32794 || y <= 32847 || y >= 32853 || m != 75)) {
                    pc.sendPackets(new S_ServerMessage(79));
                    return;
                } else {
                    spellBook(pc, skillId, itemObj);
                    return;
                }
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
                spellBook(pc, skillId, itemObj);
                return;
            default:
                return;
        }
    }

    private void spellBook(L1PcInstance pc, int skillId, int itemObj) throws Exception {
        pc.getInventory().removeItem(pc.getInventory().getItem(itemObj), 1);
        pc.sendPackets(new S_AddSkill(pc, skillId));
        pc.sendPacketsX8(new S_SkillSound(pc.getId(), 227));
        CharSkillReading.get().spellMastery(pc.getId(), skillId, SkillsTable.get().getTemplate(skillId).getName(), 0, 0);
    }
}
