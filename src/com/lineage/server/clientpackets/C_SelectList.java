package com.lineage.server.clientpackets;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.datatables.NpcTable;
import com.lineage.server.datatables.lock.PetReading;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.L1PcInventory;
import com.lineage.server.model.item.L1ItemId;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Pet;
import com.lineage.server.world.World;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class C_SelectList extends ClientBasePacket {
    private static final Log _log = LogFactory.getLog(C_SelectList.class);

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public void start(byte[] decrypt, ClientExecutor client) {
        int divisor;
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
                    int itemObjectId = readD();
                    int npcObjectId = readD();
                    if (npcObjectId != 0) {
                        L1Object obj = World.get().findObject(npcObjectId);
                        if (obj != null && (obj instanceof L1NpcInstance)) {
                            L1NpcInstance npc = (L1NpcInstance) obj;
                            int difflocx = Math.abs(pc.getX() - npc.getX());
                            int difflocy = Math.abs(pc.getY() - npc.getY());
                            if (difflocx > 3 || difflocy > 3) {
                                over();
                                return;
                            }
                        }
                        L1PcInventory pcInventory = pc.getInventory();
                        L1ItemInstance item = pcInventory.getItem(itemObjectId);
                        if (!pc.getInventory().consumeItem(L1ItemId.ADENA, (long) (item.get_durability() * 200))) {
                            pc.sendPackets(new S_ServerMessage((int) L1SkillId.SHOCK_SKIN));
                            over();
                            return;
                        }
                        item.set_durability(0);
                        pc.sendPackets(new S_ServerMessage(464, item.getLogName()));
                        pcInventory.updateItem(item, 1);
                    } else {
                        boolean chackAdena = true;
                        int petCost = 0;
                        Object[] petList = pc.getPetList().values().toArray();
                        int length = petList.length;
                        for (int i = 0; i < length; i++) {
                            petCost += ((L1NpcInstance) petList[i]).getPetcost();
                        }
                        int charisma = pc.getCha();
                        if (pc.isCrown()) {
                            charisma += 6;
                        } else if (pc.isElf()) {
                            charisma += 12;
                        } else if (pc.isWizard()) {
                            charisma += 6;
                        } else if (pc.isDarkelf()) {
                            charisma += 6;
                        } else if (pc.isDragonKnight()) {
                            charisma += 6;
                        } else if (pc.isIllusionist()) {
                            charisma += 6;
                        }
                        if (!pc.getInventory().consumeItem(L1ItemId.ADENA, 70)) {
                            chackAdena = false;
                        }
                        L1Pet l1pet = PetReading.get().getTemplate(itemObjectId);
                        if (l1pet != null && chackAdena) {
                            int npcId = l1pet.get_npcid();
                            int charisma2 = charisma - petCost;
                            if (npcId == 45313 || npcId == 45710 || npcId == 45711 || npcId == 45712) {
                                divisor = 12;
                            } else {
                                divisor = 6;
                            }
                            if (charisma2 / divisor <= 0) {
                                pc.sendPackets(new S_ServerMessage(489));
                                over();
                                return;
                            }
                            new L1PetInstance(NpcTable.get().getTemplate(npcId), pc, l1pet).setPetcost(divisor);
                        }
                        if (!chackAdena) {
                            pc.sendPackets(new S_ServerMessage((int) L1SkillId.SHOCK_SKIN));
                        }
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
