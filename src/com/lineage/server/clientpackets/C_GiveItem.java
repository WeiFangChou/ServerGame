package com.lineage.server.clientpackets;

import com.lineage.config.ConfigOther;
import com.lineage.echo.ClientExecutor;
import com.lineage.server.WriteLogTxt;
import com.lineage.server.datatables.ItemRestrictionsTable;
import com.lineage.server.datatables.PetTypeTable;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.model.Instance.L1SummonInstance;
import com.lineage.server.model.L1Inventory;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.L1PcInventory;
import com.lineage.server.serverpackets.OpcodesServer;
import com.lineage.server.serverpackets.S_BlueMessage;
import com.lineage.server.serverpackets.S_ItemName;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Npc;
import com.lineage.server.templates.L1PetType;
import com.lineage.server.types.ULong32;
import com.lineage.server.world.World;
import java.util.Random;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class C_GiveItem extends ClientBasePacket {
    private static final Log _log = LogFactory.getLog(C_GiveItem.class);
    private static final String[] receivableImpls = {"L1Npc", "L1Monster", "L1Guardian", "L1Teleporter", "L1Guard"};

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
                    int targetId = readD();
                    int x = readH();
                    int y = readH();
                    int itemObjId = readD();
                    long count = (long) readD();
                    if (count > ULong32.MAX_UNSIGNEDLONG_VALUE) {
                        count = ULong32.MAX_UNSIGNEDLONG_VALUE;
                    }
                    long count2 = Math.max(0L, count);
                    L1Object object = World.get().findObject(targetId);
                    if (object == null || !(object instanceof L1NpcInstance)) {
                        over();
                        return;
                    }
                    L1NpcInstance target = (L1NpcInstance) object;
                    if (!isNpcItemReceivable(target.getNpcTemplate())) {
                        over();
                        return;
                    }
                    L1Inventory targetInv = target.getInventory();
                    L1Inventory inv = pc.getInventory();
                    L1ItemInstance item = inv.getItem(itemObjId);
                    if (item == null) {
                        over();
                    } else if (item.getCount() <= 0) {
                        over();
                    } else if (item.isEquipped()) {
                        pc.sendPackets(new S_ServerMessage(141));
                        over();
                    } else if (!item.getItem().isTradable()) {
                        pc.sendPackets(new S_ServerMessage(210, item.getItem().getName()));
                        over();
                    } else if (item.getBless() >= 128) {
                        pc.sendPackets(new S_ServerMessage(210, item.getItem().getName()));
                        over();
                    } else if (item.get_time() != null) {
                        pc.sendPackets(new S_ServerMessage(210, item.getItem().getName()));
                        over();
                    } else if (ItemRestrictionsTable.RESTRICTIONS.contains(Integer.valueOf(item.getItemId()))) {
                        pc.sendPackets(new S_ServerMessage(210, item.getItem().getName()));
                        over();
                    } else if (pc.getAccessLevel() >= 200 || ConfigOther.GIVE_ITEM_LIST.contains(Integer.valueOf(item.getItemId()))) {
                        int pcx = pc.getX();
                        int pcy = pc.getY();
                        if (Math.abs(pcx - x) >= 3 || Math.abs(pcy - y) >= 3) {
                            pc.sendPackets(new S_ServerMessage((int) OpcodesServer.S_OPCODE_ATTACKPACKET));
                            over();
                            return;
                        }
                        for (Object petObject : pc.getPetList().values()) {
                            if ((petObject instanceof L1PetInstance) && item.getId() == ((L1PetInstance) petObject).getItemObjId()) {
                                pc.sendPackets(new S_ServerMessage(210, item.getItem().getName()));
                                over();
                                return;
                            }
                        }
                        if (pc.getDoll(item.getId()) != null) {
                            pc.sendPackets(new S_ServerMessage(1181));
                            over();
                        } else if (targetInv.checkAddItem(item, count2) != 0) {
                            pc.sendPackets(new S_ServerMessage(942));
                            over();
                        } else {
                            WriteLogTxt.Recording("給予物品記錄", "IP(" + ((Object) pc.getNetConnection().getIp()) + ")" + "玩家" + ":【" + pc.getName() + "】 " + "的" + "【+" + item.getEnchantLevel() + " " + item.getName() + "(" + count2 + ")" + "】" + " 道具丟給 :【" + target.getName() + "】" + "序號：【" + item.getId() + "】");
                            L1ItemInstance getItem = inv.tradeItem(item, count2, targetInv);
                            target.onGetItem(getItem);
                            target.turnOnOffLight();
                            pc.turnOnOffLight();
                            L1PetType petType = PetTypeTable.getInstance().get(target.getNpcTemplate().get_npcId());
                            if (petType == null || target.isDead()) {
                                over();
                                return;
                            }
                            if (getItem.getItemId() == petType.getItemIdForTaming()) {
                                target.getInventory().consumeItem(petType.getItemIdForTaming(), 1);
                                tamePet(pc, target);
                                WriteLogTxt.Recording("抓到寵物記錄", "玩家:【" + pc.getName() + "】 " + "使用【" + item.getName() + "】 " + "抓到【" + target.getName() + "】 " + "序號：【" + item.getId() + "】");
                            }
                            if (target instanceof L1PetInstance) {
                                L1PetInstance tgPet = (L1PetInstance) target;
                                if (getItem.getItemId() == petType.getEvolvItemId() && petType.canEvolve()) {
                                    evolvePet(pc, tgPet, target, item.getItemId());
                                    WriteLogTxt.Recording("進化記錄", "玩家:【" + pc.getName() + "】 " + "使用【" + item.getName() + "】 " + "進化到【" + target.getName() + "】 " + "序號：【" + item.getId() + "】");
                                }
                            }
                            over();
                        }
                    } else {
                        pc.sendPackets(new S_ServerMessage(210, item.getItem().getName()));
                        over();
                    }
                }
            }
        } catch (Exception ignored) {
        } finally {
            over();
        }
    }

    private boolean isNpcItemReceivable(L1Npc npc) {
        for (String impl : receivableImpls) {
            if (npc.getImpl().equals(impl)) {
                return true;
            }
        }
        return false;
    }

    private void tamePet(L1PcInstance pc, L1NpcInstance target) {
        int divisor;
        if (!((target instanceof L1PetInstance) || (target instanceof L1SummonInstance))) {
            int petcost = 0;
            for (Object pet : pc.getPetList().values().toArray()) {
                petcost += ((L1NpcInstance) pet).getPetcost();
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
            int charisma2 = charisma - petcost;
            int npcId = target.getNpcId();
            if (npcId == 45313 || npcId == 45710 || npcId == 45711 || npcId == 45712) {
                divisor = 12;
            } else {
                divisor = 6;
            }
            if (charisma2 / divisor <= 0) {
                pc.sendPackets(new S_ServerMessage(489));
                return;
            }
            L1PcInventory inv = pc.getInventory();
            if (inv.getSize() >= 180) {
                return;
            }
            if (isTamePet(target)) {
                L1ItemInstance petamu = inv.storeItem(40314, 1);
                if (petamu != null) {
                    new L1PetInstance(target, pc, petamu.getId());
                    pc.sendPackets(new S_ItemName(petamu));
                    return;
                }
                return;
            }
            pc.sendPackets(new S_ServerMessage(324));
        }
    }

    private void evolvePet(L1PcInstance pc, L1PetInstance pet, L1NpcInstance npc, int itemId) throws Exception {
        L1ItemInstance highpetamu;
        L1PetType petType = PetTypeTable.getInstance().get(npc.getNpcTemplate().get_npcId());
        L1PcInventory inv = pc.getInventory();
        L1ItemInstance petamu = inv.getItem(pet.getItemObjId());
        if ((pet.getLevel() >= petType.getLevel() || itemId == 41310) && pc == pet.getMaster() && petamu != null && (highpetamu = inv.storeItem(40316, 1)) != null) {
            pet.evolvePet(highpetamu.getId());
            pc.sendPackets(new S_ItemName(highpetamu));
            inv.removeItem(petamu, 1);
            switch (petType.Board()) {
                case 1:
                    World.get().broadcastPacketToAll(new S_ServerMessage("恭喜玩家【" + pc.getName() + "】進化到【" + npc.getName() + "】。"));
                    return;
                case 2:
                    World.get().broadcastPacketToAll(new S_BlueMessage(166, "\\f3恭喜 玩家【" + pc.getName() + "】進化到【" + npc.getName() + "】"));
                    return;
                default:
                    return;
            }
        }
    }

    private boolean isTamePet(L1NpcInstance npc) {
        boolean isSuccess = false;
        int npcId = npc.getNpcTemplate().get_npcId();
        if (npcId == 45313) {
            Random random = new Random();
            if (npc.getMaxHp() / 3 > npc.getCurrentHp() && random.nextInt(16) == 15) {
                isSuccess = true;
            }
        } else if (npc.getMaxHp() / 3 > npc.getCurrentHp()) {
            isSuccess = true;
        }
        if ((npcId == 45313 || npcId == 45044 || npcId == 45711) && npc.isResurrect()) {
            return false;
        }
        return isSuccess;
    }

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public String getType() {
        return getClass().getSimpleName();
    }
}
