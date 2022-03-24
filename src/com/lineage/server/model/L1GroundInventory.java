package com.lineage.server.model;

import com.lineage.config.ConfigAlt;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_NPCPack_Item;
import com.lineage.server.serverpackets.S_RemoveObject;
import com.lineage.server.utils.collections.Maps;
import com.lineage.server.world.World;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

public class L1GroundInventory extends L1Inventory {
    static Logger _log = Logger.getLogger(L1PcInventory.class.getName());
    private static final Timer _timer = new Timer();
    private static final long serialVersionUID = 1;
    private final Map<Integer, DeletionTimer> _reservedTimers = Maps.newHashMap();

    /* access modifiers changed from: private */
    public class DeletionTimer extends TimerTask {
        private final L1ItemInstance _item;

        public DeletionTimer(L1ItemInstance item) {
            this._item = item;
        }

        public void run() {
            try {
                synchronized (L1GroundInventory.this) {
                    if (L1GroundInventory.this._items.contains(this._item)) {
                        L1GroundInventory.this.removeItem(this._item);
                    }
                }
            } catch (Throwable t) {
                L1GroundInventory._log.log(Level.SEVERE, t.getLocalizedMessage(), t);
            }
        }
    }

    public L1GroundInventory(int objectId, int x, int y, short map) {
        setId(objectId);
        setX(x);
        setY(y);
        setMap(map);
        World.get().addVisibleObject(this);
    }

    private void cancelTimer(L1ItemInstance item) {
        DeletionTimer timer = this._reservedTimers.get(Integer.valueOf(item.getId()));
        if (timer != null) {
            timer.cancel();
        }
    }

    @Override // com.lineage.server.model.L1Inventory
    public void deleteItem(L1ItemInstance item) {
        cancelTimer(item);
        Iterator<L1PcInstance> it = World.get().getRecognizePlayer(item).iterator();
        while (it.hasNext()) {
            L1PcInstance pc = it.next();
            pc.sendPackets(new S_RemoveObject(item));
            pc.removeKnownObject(item);
        }
        this._items.remove(item);
        if (this._items.size() == 0) {
            World.get().removeVisibleObject(this);
        }
    }

    @Override // com.lineage.server.model.L1Inventory
    public void insertItem(L1ItemInstance item) {
        setTimer(item);
        Iterator<L1PcInstance> it = World.get().getRecognizePlayer(item).iterator();
        while (it.hasNext()) {
            L1PcInstance pc = it.next();
            pc.sendPackets(new S_NPCPack_Item(item));
            pc.addKnownObject(item);
        }
    }

    @Override // com.lineage.server.model.L1Object
    public void onPerceive(L1PcInstance perceivedFrom) {
        for (L1ItemInstance item : getItems()) {
            if (!perceivedFrom.knownsObject(item)) {
                perceivedFrom.addKnownObject(item);
                perceivedFrom.sendPackets(new S_NPCPack_Item(item));
            }
        }
    }

    private void setTimer(L1ItemInstance item) {
        if (ConfigAlt.ALT_ITEM_DELETION_TYPE.equalsIgnoreCase("std") && item.getItemId() != 40515) {
            _timer.schedule(new DeletionTimer(item), (long) (ConfigAlt.ALT_ITEM_DELETION_TIME * 60 * L1SkillId.STATUS_BRAVE));
        }
    }

    @Override // com.lineage.server.model.L1Inventory
    public void updateItem(L1ItemInstance item) {
        Iterator<L1PcInstance> it = World.get().getRecognizePlayer(item).iterator();
        while (it.hasNext()) {
            it.next().sendPackets(new S_RemoveObject(item));
        }
    }
}
