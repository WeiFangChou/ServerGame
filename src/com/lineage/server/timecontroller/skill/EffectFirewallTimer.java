package com.lineage.server.timecontroller.skill;

import com.lineage.server.model.Instance.L1EffectInstance;
import com.lineage.server.model.Instance.L1EffectType;
import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1AttackList;
import com.lineage.server.model.L1Character;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.world.WorldEffect;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Random;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class EffectFirewallTimer extends TimerTask {
    private static final Log _log = LogFactory.getLog(EffectFirewallTimer.class);
    private static Random _random = new Random();
    private ScheduledFuture<?> _timer;

    public void start() {
        this._timer = GeneralThreadPool.get().scheduleAtFixedRate((TimerTask) this, 1650L, 1650L);
    }

    public void run() {
        try {
            Collection<L1EffectInstance> allNpc = WorldEffect.get().all();
            if (!allNpc.isEmpty()) {
                for (L1EffectInstance effect : allNpc) {
                    if (effect.effectType() == L1EffectType.isFirewall) {
                        firewall(effect);
                        Thread.sleep(1);
                    }
                }
            }
        } catch (Exception e) {
            _log.error("Npc L1Effect法師技能(火牢)狀態送出時間軸異常重啟", e);
            GeneralThreadPool.get().cancel(this._timer, false);
            new EffectFirewallTimer().start();
        }
    }

    private static void firewall(L1EffectInstance effect) {
        try {
            L1PcInstance user = (L1PcInstance) effect.getMaster();
            Iterator<L1Character> it = WorldEffect.get().getFirewall(effect).iterator();
            while (it.hasNext()) {
                L1Character object = it.next();
                if (effect.get_showId() == object.get_showId()) {
                    if (object instanceof L1PcInstance) {
                        topc(user, (L1PcInstance) object);
                    } else if (object instanceof L1MonsterInstance) {
                        tonpc(user, (L1MonsterInstance) object);
                    }
                }
            }
        } catch (Exception e) {
            _log.error("Npc L1Effect法師技能(火牢)狀態送出時間軸發生異常", e);
            effect.deleteMe();
        }
    }

    private static void tonpc(L1PcInstance user, L1MonsterInstance tgmob) {
        if (!dmg0(tgmob)) {
            double attrDeffence = 0.0d;
            int weakAttr = tgmob.getFire();
            if (weakAttr > 0) {
                attrDeffence = calcAttrResistance(weakAttr);
            }
            int damage = Math.max((int) ((1.0d - attrDeffence) * ((double) (_random.nextInt(Math.max(user.getInt() / 2, 1)) + 19))), 0);
            if (damage > 0) {
                tgmob.broadcastPacketX10(new S_DoActionGFX(tgmob.getId(), 2));
                tgmob.receiveDamage(user, damage);
            }
        }
    }

    private static void topc(L1PcInstance user, L1PcInstance tgpc) throws Exception {
        if ((user.getClanid() == 0 || tgpc.getClanid() != user.getClanid()) && !tgpc.isSafetyZone() && !dmg0(tgpc)) {
            double attrDeffence = 0.0d;
            int weakAttr = tgpc.getFire();
            if (weakAttr > 0) {
                attrDeffence = calcAttrResistance(weakAttr);
            }
            int damage = Math.max((int) ((1.0d - attrDeffence) * ((double) (_random.nextInt(Math.max(user.getInt() / 2, 1)) + 19))), 0);
            boolean dmgX2 = false;
            if (!tgpc.getSkillisEmpty() && tgpc.getSkillEffect().size() > 0) {
                try {
                    Object[] array = tgpc.getSkillEffect().toArray();
                    for (Object key : array) {
                        Integer integer = L1AttackList.SKD3.get(key);
                        if (integer != null) {
                            if (integer.equals(key)) {
                                dmgX2 = true;
                            } else {
                                damage += integer.intValue();
                            }
                        }
                    }
                } catch (ConcurrentModificationException ignored) {
                } catch (Exception e2) {
                    _log.error(e2.getLocalizedMessage(), e2);
                }
            }
            if (dmgX2) {
                damage /= 2;
            }
            if (damage > 0) {
                tgpc.sendPacketsAll(new S_DoActionGFX(tgpc.getId(), 2));
                tgpc.receiveDamage(user, (double) damage, false, true);
            }
        }
    }

    private static double calcAttrResistance(int resist) {
        int resistFloor;
        int resistFloor2 = (int) (0.32d * ((double) Math.abs(resist)));
        if (resist >= 0) {
            resistFloor = resistFloor2 * 1;
        } else {
            resistFloor = resistFloor2 * -1;
        }
        return ((double) resistFloor) / 32.0d;
    }

    private static boolean dmg0(L1Character character) {
        if (character == null) {
            return false;
        }
        try {
            if (character.getSkillisEmpty() || character.getSkillEffect().size() <= 0) {
                return false;
            }
            for (Integer key : character.getSkillEffect()) {
                if (L1AttackList.SKM0.get(key) != null) {
                    return true;
                }
            }
            return false;
        } catch (ConcurrentModificationException e) {
            return false;
        } catch (Exception e2) {
            _log.error(e2.getLocalizedMessage(), e2);
            return false;
        }
    }
}
