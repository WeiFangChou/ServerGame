package com.lineage.server.clientpackets;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.datatables.PetItemTable;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.serverpackets.S_PetEquipment;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1PetItem;
import com.lineage.server.world.WorldPet;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class C_UsePetItem extends ClientBasePacket {
    private static final Log _log = LogFactory.getLog(C_UsePetItem.class);

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public void start(byte[] decrypt, ClientExecutor client) {
        try {
            read(decrypt);
            int data = readC();
            int petId = readD();
            int listNo = readC();
            L1PcInstance pc = client.getActiveChar();
            if (pc != null) {
                L1PetInstance pet = WorldPet.get().get(Integer.valueOf(petId));
                if (pet == null) {
                    over();
                } else if (pc.getPetList().get(Integer.valueOf(petId)) == null) {
                    over();
                } else {
                    switch (pet.getNpcId()) {
                        case 45034:
                        case 45039:
                        case 45040:
                        case 45042:
                        case 45043:
                        case 45044:
                        case 45046:
                        case 45047:
                        case 45048:
                        case 45049:
                        case 45053:
                        case 45054:
                        case 45313:
                        case 45711:
                        case 46042:
                        case 46044:
                        case 71019:
                        case 71020:
                            over();
                            return;
                        default:
                            List<L1ItemInstance> itemList = pet.getInventory().getItems();
                            if (itemList.size() <= 0) {
                                over();
                                return;
                            }
                            L1ItemInstance item = itemList.get(listNo);
                            if (item == null) {
                                over();
                                return;
                            }
                            L1PetItem petItem = PetItemTable.get().getTemplate(item.getItemId());
                            if (petItem == null) {
                                pc.sendPackets(new S_ServerMessage(79));
                            } else if (petItem.isWeapom()) {
                                pet.usePetWeapon(pet, item);
                                pc.sendPackets(new S_PetEquipment(data, pet, listNo));
                            } else {
                                pet.usePetArmor(pet, item);
                                pc.sendPackets(new S_PetEquipment(data, pet, listNo));
                            }
                            over();
                            return;
                    }
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
