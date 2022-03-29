package com.lineage.server.model.Instance;

import com.lineage.server.datatables.ItemTable;
import com.lineage.server.model.L1HauntedHouse;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.skill.L1SkillUse;
import com.lineage.server.serverpackets.S_NPCPack_F;
import com.lineage.server.serverpackets.S_RemoveObject;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Npc;
import com.lineage.server.world.World;
import java.util.Iterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1FieldObjectInstance extends L1NpcInstance {
    private static final Log _log = LogFactory.getLog(L1FieldObjectInstance.class);
    private static final long serialVersionUID = 1;

    public L1FieldObjectInstance(L1Npc template) {
        super(template);
    }

    @Override // com.lineage.server.model.L1Object, com.lineage.server.model.Instance.L1NpcInstance
    public void onPerceive(L1PcInstance perceivedFrom) {
        try {
            perceivedFrom.addKnownObject(this);
            perceivedFrom.sendPackets(new S_NPCPack_F(this));
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.server.model.L1Object
    public void onAction(L1PcInstance pc) {
        try {
            if (getNpcTemplate().get_npcId() == 81171 && L1HauntedHouse.getInstance().getHauntedHouseStatus() == 2) {
                int winnersCount = L1HauntedHouse.getInstance().getWinnersCount();
                int goalCount = L1HauntedHouse.getInstance().getGoalCount();
                if (winnersCount == goalCount + 1) {
                    L1ItemInstance item = ItemTable.get().createItem(41308);
                    if (item != null && pc.getInventory().checkAddItem(item, serialVersionUID) == 0) {
                        item.setCount(serialVersionUID);
                        pc.getInventory().storeItem(item);
                        pc.sendPackets(new S_ServerMessage(403, item.getLogName()));
                    }
                    L1HauntedHouse.getInstance().endHauntedHouse();
                } else if (winnersCount > goalCount + 1) {
                    L1HauntedHouse.getInstance().setGoalCount(goalCount + 1);
                    L1HauntedHouse.getInstance().removeMember(pc);
                    L1ItemInstance item2 = ItemTable.get().createItem(41308);
                    if (item2 != null && pc.getInventory().checkAddItem(item2, serialVersionUID) == 0) {
                        item2.setCount(serialVersionUID);
                        pc.getInventory().storeItem(item2);
                        pc.sendPackets(new S_ServerMessage(403, item2.getLogName()));
                    }
                    new L1SkillUse().handleCommands(pc, 44, pc.getId(), pc.getX(), pc.getY(), 0, 1);
                    L1Teleport.teleport(pc, 32624, 32813,  4, 5, true);
                }
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.server.model.Instance.L1NpcInstance
    public void deleteMe() {
        try {
            this._destroyed = true;
            if (getInventory() != null) {
                getInventory().clearItems();
            }
            World.get().removeVisibleObject(this);
            World.get().removeObject(this);
            Iterator<L1PcInstance> it = World.get().getRecognizePlayer(this).iterator();
            while (it.hasNext()) {
                L1PcInstance pc = it.next();
                pc.removeKnownObject(this);
                pc.sendPackets(new S_RemoveObject(this));
            }
            removeAllKnownObjects();
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
