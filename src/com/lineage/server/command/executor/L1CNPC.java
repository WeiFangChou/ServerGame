package com.lineage.server.command.executor;

import com.lineage.echo.ClientExecutor;
import com.lineage.list.OnlineUser;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.datatables.lock.EzpayReading3;
import com.lineage.server.datatables.sql.CharacterTable;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.world.World;
import java.util.StringTokenizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1CNPC implements L1CommandExecutor {
    private static final Log _log = LogFactory.getLog(L1CNPC.class);

    private L1CNPC() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1CNPC();
    }

    @Override // com.lineage.server.command.executor.L1CommandExecutor
    public void execute(L1PcInstance pc, String cmdName, String arg) throws Exception {
        int count;
        L1PcInstance tgpc;
        if (pc == null) {
            try {
                _log.warn("這個命令只能在遊戲中執行!");
            } catch (Exception e) {
                _log.error("錯誤的GM指令格式: " + getClass().getSimpleName() + " 執行的GM:" + pc.getName());
                pc.sendPackets(new S_ServerMessage(261));
                return;
            }
        }
        StringTokenizer st = new StringTokenizer(arg);
        String pcname = st.nextToken();
        L1PcInstance target = World.get().getPlayer(pcname);
        if (target == null) {
            target = CharacterTable.get().restoreCharacter(pcname);
        }
        if (target != null) {
            int item_id = Integer.parseInt(st.nextToken());
            try {
                count = Integer.parseInt(st.nextToken());
            } catch (Exception e2) {
                count = 1;
            }
            L1ItemInstance item = ItemTable.get().createItem(item_id);
            if (item != null) {
                item.setCount((long) count);
                pc.sendPackets(new S_ServerMessage(166, "指定角色(" + pcname + ") 已加入獎勵" + item_id + " 數量" + count));
                EzpayReading3.get().insertItem(target.getAccountName(), item);
                ClientExecutor cl = OnlineUser.get().get(target.getAccountName());
                if (cl != null && (tgpc = cl.getActiveChar()) != null) {
                    tgpc.sendPackets(new S_ServerMessage(166, "伺服器已經把獎勵送到即時獎勵→道具：" + item.getName() + " 數量" + count));
                    return;
                }
                return;
            }
            return;
        }
        pc.sendPackets(new S_ServerMessage(166, "指令異常: 沒有該角色(" + pcname + ")!!"));
    }
}
