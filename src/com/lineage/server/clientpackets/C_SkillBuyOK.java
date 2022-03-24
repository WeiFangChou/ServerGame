package com.lineage.server.clientpackets;

import com.lineage.echo.ClientExecutor;
import com.lineage.list.PcLvSkillList;
import com.lineage.server.datatables.SkillsTable;
import com.lineage.server.datatables.lock.CharSkillReading;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.item.L1ItemId;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.OpcodesServer;
import com.lineage.server.serverpackets.S_AddSkill;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillBuyCN;
import com.lineage.server.serverpackets.S_SkillSound;
import com.lineage.server.templates.L1Skills;
import java.util.ArrayList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class C_SkillBuyOK extends ClientBasePacket {
    private static final int[] PRICE;
    private static final Log _log = LogFactory.getLog(C_SkillBuyOK.class);

    static {
        int[] iArr = new int[28];
        iArr[0] = 1;
        iArr[1] = 4;
        iArr[2] = 9;
        iArr[3] = 16;
        iArr[4] = 25;
        iArr[5] = 36;
        iArr[6] = 49;
        iArr[7] = 64;
        iArr[8] = 81;
        iArr[9] = 100;
        iArr[10] = 121;
        iArr[11] = 144;
        iArr[12] = 169;
        iArr[13] = 196;
        iArr[14] = 225;
        iArr[16] = 289;
        iArr[17] = 324;
        iArr[18] = 361;
        iArr[19] = 400;
        iArr[20] = 441;
        iArr[21] = 484;
        iArr[22] = 529;
        iArr[23] = 576;
        iArr[24] = 625;
        iArr[25] = 676;
        iArr[26] = 729;
        iArr[27] = 784;
        PRICE = iArr;
    }

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public void start(byte[] decrypt, ClientExecutor client) {
        try {
            read(decrypt);
            L1PcInstance pc = client.getActiveChar();
            if (!pc.isGhost()) {
                if (pc.isDead()) {
                    over();
                } else if (pc.isTeleport()) {
                    over();
                } else if (pc.isPrivateShop()) {
                    over();
                } else {
                    int count = readH();
                    ArrayList<Integer> skillList = null;
                    if (pc.isCrown()) {
                        skillList = PcLvSkillList.isCrown(pc);
                    } else if (pc.isKnight()) {
                        skillList = PcLvSkillList.isKnight(pc);
                    } else if (pc.isElf()) {
                        skillList = PcLvSkillList.isElf(pc);
                    } else if (pc.isWizard()) {
                        skillList = PcLvSkillList.isWizard(pc);
                    } else if (pc.isDarkelf()) {
                        skillList = PcLvSkillList.isDarkelf(pc);
                    } else if (pc.isDragonKnight()) {
                        skillList = PcLvSkillList.isDragonKnight(pc);
                    } else if (pc.isIllusionist()) {
                        skillList = PcLvSkillList.isIllusionist(pc);
                    }
                    if (skillList == null) {
                        over();
                        return;
                    }
                    boolean isGfx = false;
                    boolean shopSkill = false;
                    if (pc.get_other().get_shopSkill()) {
                        shopSkill = true;
                    }
                    for (int i = 0; i < count; i++) {
                        int skillId = readD() + 1;
                        if (!CharSkillReading.get().spellCheck(pc.getId(), skillId) && skillList.contains(new Integer(skillId - 1))) {
                            L1Skills l1skills = SkillsTable.get().getTemplate(skillId);
                            int price = (shopSkill ? S_SkillBuyCN.PCTYPE[pc.getType()] : 100) * PRICE[l1skills.getSkillLevel() - 1];
                            if (pc.getInventory().checkItem(L1ItemId.ADENA, (long) price)) {
                                pc.getInventory().consumeItem(L1ItemId.ADENA, (long) price);
                                CharSkillReading.get().spellMastery(pc.getId(), l1skills.getSkillId(), l1skills.getName(), 0, 0);
                                pc.sendPackets(new S_AddSkill(pc, skillId));
                                isGfx = true;
                            } else {
                                pc.sendPackets(new S_ServerMessage((int) L1SkillId.SHOCK_SKIN));
                            }
                        }
                    }
                    if (isGfx) {
                        pc.sendPacketsX8(new S_SkillSound(pc.getId(), OpcodesServer.S_OPCODE_DRAWAL));
                    }
                    over();
                }
            }
        } catch (Exception ignored) {
        } finally {
            over();
        }
    }

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public String getType() {
        return getClass().getSimpleName();
    }
}
