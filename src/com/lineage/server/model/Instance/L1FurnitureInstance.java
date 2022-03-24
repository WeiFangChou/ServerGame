package com.lineage.server.model.Instance;

import com.lineage.server.serverpackets.S_RemoveObject;
import com.lineage.server.templates.L1Npc;
import com.lineage.server.world.World;
import java.util.Iterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1FurnitureInstance extends L1NpcInstance {
    private static final Log _log = LogFactory.getLog(L1FurnitureInstance.class);
    private static final long serialVersionUID = 1;
    private int _itemObjId;

    public L1FurnitureInstance(L1Npc template) {
        super(template);
    }

    @Override // com.lineage.server.model.L1Object
    public void onAction(L1PcInstance player) {
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

    public int getItemObjId() {
        return this._itemObjId;
    }

    public void setItemObjId(int i) {
        this._itemObjId = i;
    }
}
