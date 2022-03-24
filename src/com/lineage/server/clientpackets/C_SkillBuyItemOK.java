package com.lineage.server.clientpackets;

import com.lineage.echo.ClientExecutor;
import com.lineage.list.PcLvSkillList;
import com.lineage.server.datatables.SkillsItemTable;
import com.lineage.server.datatables.SkillsTable;
import com.lineage.server.datatables.lock.CharSkillReading;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.OpcodesServer;
import com.lineage.server.serverpackets.S_AddSkill;
import com.lineage.server.serverpackets.S_ItemError;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillSound;
import com.lineage.server.templates.L1SkillItem;
import com.lineage.server.templates.L1Skills;
import java.util.ArrayList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class C_SkillBuyItemOK extends ClientBasePacket {
    private static final Log _log = LogFactory.getLog(C_EnterPortal.class);

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
                    int count = readH();
                    for (int i = 0; i < count; i++) {
                        int skillId = readD() + 1;
                        if (!CharSkillReading.get().spellCheck(pc.getId(), skillId) && skillList.contains(new Integer(skillId - 1))) {
                            L1Skills l1skills = SkillsTable.get().getTemplate(skillId);
                            L1SkillItem priceItem = SkillsItemTable.get().getTemplate(skillId);
                            if (priceItem == null || priceItem.get_items() == null) {
                                pc.sendPackets(new S_ServerMessage(939));
                                _log.error("購買技能 材料 設置資料異常 材料未設置: " + skillId);
                            } else {
                                int length = priceItem.get_items().length;
                                boolean[] isOks = new boolean[length];
                                for (int x = 0; x < length; x++) {
                                    if (!pc.getInventory().checkItem(priceItem.get_items()[x], (long) priceItem.get_counts()[x])) {
                                        isOks[x] = false;
                                    } else {
                                        isOks[x] = true;
                                    }
                                }
                                boolean isShopOk = true;
                                for (boolean isOk : isOks) {
                                    if (!isOk) {
                                        isShopOk = false;
                                    }
                                }
                                if (isShopOk) {
                                    for (int x2 = 0; x2 < priceItem.get_items().length; x2++) {
                                        int itemId = priceItem.get_items()[x2];
                                        int itemCount = priceItem.get_counts()[x2];
                                        if (pc.getInventory().checkItem(itemId, (long) itemCount)) {
                                            pc.getInventory().consumeItem(itemId, (long) itemCount);
                                        }
                                    }
                                    CharSkillReading.get().spellMastery(pc.getId(), l1skills.getSkillId(), l1skills.getName(), 0, 0);
                                    pc.sendPackets(new S_AddSkill(pc, skillId));
                                    isGfx = true;
                                } else {
                                    pc.sendPackets(new S_ItemError(skillId - 1));
                                }
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
