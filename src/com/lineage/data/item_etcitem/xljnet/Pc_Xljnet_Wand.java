package com.lineage.data.item_etcitem.xljnet;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.datatables.ExpTable;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Object;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_SystemMessage;
import com.lineage.server.world.World;

public class Pc_Xljnet_Wand extends ItemExecutor {
    private Pc_Xljnet_Wand() {
    }

    public static ItemExecutor get() {
        return new Pc_Xljnet_Wand();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) {
        int spellsc_objid = data[0];
        L1Object target = World.get().findObject(spellsc_objid);
        if (target != null) {
            String[] msg = new String[11];
            if (target instanceof L1PcInstance) {
                L1PcInstance target_pc = (L1PcInstance) target;
                msg[0] = "玩家名稱：" + target_pc.getName() + " 正義：" + target_pc.getLawful() + "。";
                msg[1] = String.valueOf("等級：" + target_pc.getLevel() + " 經驗：" + ExpTable.getExpPercentage(target_pc.getLevel(), (long) ((int) target_pc.getExp())) + "%。");
                msg[2] = String.valueOf(String.valueOf("血量：" + target_pc.getCurrentHp())) + " / " + String.valueOf(target_pc.getMaxHp()) + "。";
                msg[3] = String.valueOf(String.valueOf("魔量：" + target_pc.getCurrentMp())) + " / " + String.valueOf((int) target_pc.getMaxMp()) + "。";
                msg[4] = String.valueOf("防禦：" + target_pc.getAc() + " 抗地：" + target_pc.getEarth() + "。");
                msg[5] = String.valueOf("力量：" + ((int) target_pc.getStr()) + " 體質：" + ((int) target_pc.getCon()) + "。");
                msg[6] = String.valueOf("敏捷：" + ((int) target_pc.getDex()) + " 精神：" + ((int) target_pc.getWis()) + "。");
                msg[7] = String.valueOf("智力：" + ((int) target_pc.getInt()) + " 魅力：" + ((int) target_pc.getCha()) + "。");
                msg[8] = String.valueOf("迴避：" + target_pc.getEr() + " 魔攻：" + target_pc.getSp() + "。");
                msg[9] = String.valueOf("抗魔：" + target_pc.getMr() + "% 抗火：" + target_pc.getFire() + " %。");
                msg[10] = String.valueOf("抗水：" + target_pc.getWater() + "% 抗風：" + target_pc.getWind() + " %。");
                pc.setTouKuiName(target_pc.getName());
                pc.set_tuokui_objId(spellsc_objid);
                pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "toukuipc", msg));
                return;
            }
            pc.sendPackets(new S_SystemMessage("請選擇正確目標。"));
        }
    }
}
