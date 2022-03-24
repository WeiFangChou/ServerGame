package com.lineage.server.model.Instance;

import com.lineage.server.model.L1Inventory;
import com.lineage.server.thread.NpcAiThreadPool;
import com.lineage.server.world.World;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class NpcAI implements Runnable {
    private static final Log _log = LogFactory.getLog(NpcAI.class);
    private final L1NpcInstance _npc;

    public NpcAI(L1NpcInstance npc) {
        this._npc = npc;
    }

    public void startAI() {
        NpcAiThreadPool.get().execute(this);
    }

    public void run() {
        try {
            this._npc.setAiRunning(true);

            while(!this._npc._destroyed && !this._npc.isDead() && this._npc.getCurrentHp() > 0 && this._npc.getHiddenStatus() == 0) {
                while(this._npc.isParalyzed() || this._npc.isSleeped()) {
                    try {
                        Thread.sleep(200L);
                    } catch (InterruptedException var2) {
                        this._npc.setParalyzed(false);
                    }
                }

                if (this.stopAIProcess()) {
                    break;
                }

                try {
                    Thread.sleep((long)this._npc.getSleepTime());
                } catch (Exception var4) {
                    break;
                }
            }

            this._npc.mobSkill().resetAllSkillUseCount();

            do {
                try {
                    Thread.sleep((long)this._npc.getSleepTime());
                } catch (Exception var3) {
                    break;
                }
            } while(this._npc.isDeathProcessing());

            this._npc.allTargetClear();
            this._npc.setAiRunning(false);
            Thread.sleep(20L);
        } catch (Exception var5) {
            _log.error("NpcAI發生例外狀況: " + this._npc.getName(), var5);
        }

    }

    private boolean stopAIProcess() {
        try {
            this._npc.setSleepTime(300);
            this._npc.checkTarget();
            if (this._npc.is_now_target() == null && this._npc.getMaster() == null) {
                this._npc.searchTarget();
            }

            this._npc.onItemUse();
            if (this._npc.is_now_target() == null) {
                this._npc.checkTargetItem();
                if (this._npc.isPickupItem() && this._npc.is_now_targetItem() == null) {
                    this._npc.searchTargetItem();
                }

                if (this._npc.is_now_targetItem() == null) {
                    boolean noTarget = this._npc.noTarget();
                    if (noTarget) {
                        return true;
                    }
                } else {
                    L1Inventory groundInventory = World.get().getInventory(this._npc.is_now_targetItem().getX(), this._npc.is_now_targetItem().getY(), this._npc.is_now_targetItem().getMapId());
                    if (!groundInventory.checkItem(this._npc.is_now_targetItem().getItemId())) {
                        this._npc._targetItemList.remove(this._npc.is_now_targetItem());
                        this._npc.set_now_targetItem((L1ItemInstance)null);
                        this._npc.setSleepTime(1000);
                        return false;
                    }

                    this._npc.onTargetItem();
                }
            } else {
                if (this._npc.getHiddenStatus() != 0) {
                    return true;
                }

                this._npc.onTarget();
            }

            Thread.sleep(20L);
        } catch (Exception var2) {
            _log.error("NpcAI發生例外狀況: " + this._npc.getName(), var2);
        }

        return false;
    }
}
