package com.lineage.server.clientpackets;

import com.lineage.config.ConfigAlt;
import com.lineage.config.ConfigRate;
import com.lineage.echo.ClientExecutor;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Character;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_WhoCharinfo;
import com.lineage.server.timecontroller.server.ServerRestartTimer;
import com.lineage.server.world.World;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class C_Who extends ClientBasePacket {
    private static final Log _log = LogFactory.getLog(C_Who.class);

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public void start(byte[] decrypt, ClientExecutor client) {
        try {
            read(decrypt);
            L1Character find = World.get().getPlayer(readS());
            L1PcInstance pc = client.getActiveChar();
            if (find == null) {
                String amount = String.valueOf((int) (((double) World.get().getAllPlayers().size()) * ConfigAlt.ALT_WHO_COUNT));
                String nowDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());
                switch (ConfigAlt.ALT_WHO_TYPE) {
                    case 0:
                        pc.sendPackets(new S_ServerMessage("重啟時間 ：" + ServerRestartTimer.get_restartTime()));
                        pc.sendPackets(new S_ServerMessage("目前線上有 ：" + amount));
                        break;
                    case 1:
                        pc.sendPackets(new S_ServerMessage("經驗：" + ConfigRate.RATE_XP + " 倍  掉寶：" + ConfigRate.RATE_DROP_ITEMS + " 倍 金錢：" + ConfigRate.RATE_DROP_ADENA + " 倍。"));
                        pc.sendPackets(new S_ServerMessage("衝武：" + ConfigRate.ENCHANT_CHANCE_WEAPON + " ％  衝防：" + ConfigRate.ENCHANT_CHANCE_ARMOR + " ％ 屬性：" + ConfigRate.ATTR_ENCHANT_CHANCE + " ％。"));
                        pc.sendPackets(new S_ServerMessage("現實時間 ：" + nowDate));
                        pc.sendPackets(new S_ServerMessage("重啟時間 ：" + ServerRestartTimer.get_restartTime()));
                        pc.sendPackets(new S_ServerMessage("目前線上有 ：" + amount));
                        break;
                    case 2:
                        pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "y_who", new String[]{String.valueOf("經驗：" + ConfigRate.RATE_XP + " 倍。"), String.valueOf("掉寶：" + ConfigRate.RATE_DROP_ITEMS + " 倍。"), String.valueOf("金錢：" + ConfigRate.RATE_DROP_ADENA + " 倍。"), String.valueOf("衝武：" + ConfigRate.ENCHANT_CHANCE_WEAPON + " ％。"), String.valueOf("衝防：" + ConfigRate.ENCHANT_CHANCE_ARMOR + " ％。"), String.valueOf("屬性：" + ConfigRate.ATTR_ENCHANT_CHANCE + " ％。"), String.valueOf("目前時間：" + nowDate + "。"), String.valueOf("重啟時間：" + ServerRestartTimer.get_restartTime() + "。"), String.valueOf("目前線上有 ：" + amount)}));
                        break;
                }
            } else {
                pc.sendPackets(new S_WhoCharinfo(find, client));
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
