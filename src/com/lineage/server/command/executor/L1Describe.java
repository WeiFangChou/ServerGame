package com.lineage.server.command.executor;

import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.world.World;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1Describe implements L1CommandExecutor {
    private static final Log _log = LogFactory.getLog(L1Describe.class);

    private L1Describe() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1Describe();
    }

    @Override // com.lineage.server.command.executor.L1CommandExecutor
    public void execute(L1PcInstance pc, String cmdName, String arg) {
        if (pc == null) {
            try {
                _log.warn("系統命令執行: " + cmdName + " " + arg + " 顯示人物附加屬性。");
            } catch (Exception e) {
                if (pc == null) {
                    _log.error("錯誤的命令格式: " + getClass().getSimpleName());
                    return;
                }
                _log.error("錯誤的GM指令格式: " + getClass().getSimpleName() + " 執行的GM:" + pc.getName());
                pc.sendPackets(new S_ServerMessage(261));
                return;
            }
        }
        ArrayList<String> msg = new ArrayList<>();
        L1PcInstance target = World.get().getPlayer(arg);
        if (pc == null) {
            if (target == null) {
                _log.error("指令異常: 指定人物不在線上，這個命令必須輸入正確人物名稱才能執行。");
                return;
            }
        } else if (target == null) {
            target = pc;
        }
        msg.add("-- 顯示資訊人物: " + target.getName() + " --");
        msg.add("傷害附加: +" + target.getDmgup());
        msg.add("命中附加: +" + target.getHitup());
        msg.add("抗魔: " + target.getMr() + "%");
        int hpr = target.getHpr() + target.getInventory().hpRegenPerTick();
        int mpr = target.getMpr() + target.getInventory().mpRegenPerTick();
        msg.add("HP額外回復量: " + hpr);
        msg.add("MP額外回復量: " + mpr);
        msg.add("有好度: " + target.getKarma());
        msg.add("背包物品數量: " + target.getInventory().getSize());
        if (pc == null) {
            String items = "";
            for (L1ItemInstance item : target.getInventory().getItems()) {
                items = String.valueOf(items) + "[" + item.getNumberedName(item.getCount(), false) + "]";
            }
            msg.add(items);
        }
        Iterator<String> it = msg.iterator();
        while (it.hasNext()) {
            String info = it.next();
            if (pc == null) {
                _log.info(info);
            } else {
                pc.sendPackets(new S_ServerMessage(166, info));
            }
        }
    }
}
