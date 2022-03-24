package com.lineage.server.serverpackets;

import com.lineage.config.Config;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.templates.L1Account;
import com.lineage.server.world.World;
import java.util.Calendar;
import java.util.TimeZone;

public class S_PacketBoxGm extends ServerBasePacket {
    public static final int CALL_SOMETHING = 45;
    private byte[] _byte = null;

    public S_PacketBoxGm(L1PcInstance srcpc, int mode) {
        writeC(40);
        writeC(45);
        if (srcpc.isGm()) {
            callSomething(srcpc, mode);
        }
    }

    private void callSomething(L1PcInstance srcpc, int mode) {
        writeC(World.get().getAllPlayers().size());
        for (L1PcInstance pc : World.get().getAllPlayers()) {
            L1Account acc = pc.getNetConnection().getAccount();
            if (acc == null) {
                writeD(0);
            } else {
                Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(Config.TIME_ZONE));
                cal.setTimeInMillis(acc.get_lastactive().getTime());
                cal.set(1, 1970);
                writeD((int) (cal.getTimeInMillis() / 1000));
            }
            writeS(String.valueOf(mode) + ":" + pc.getName());
            writeS(String.valueOf(pc.getLevel()));
        }
        String xmode = null;
        switch (mode) {
            case 0:
                xmode = "刪除已存人物保留技能";
                break;
            case 1:
                xmode = "移動座標至指定人物身邊";
                break;
            case 2:
                xmode = "召回指定人物";
                break;
            case 3:
                xmode = "召回指定隊伍";
                break;
            case 4:
                xmode = "全技能";
                break;
            case 5:
                xmode = "踢除下線";
                break;
            case 6:
                xmode = "封鎖IP/MAC";
                break;
            case 7:
                xmode = "帳號封鎖";
                break;
            case 8:
                xmode = "殺死指定人物";
                break;
        }
        if (xmode != null) {
            srcpc.setTempID(mode);
            srcpc.sendPackets(new S_ServerMessage(166, "請注意目前模式為: " + xmode));
        }
    }

    @Override // com.lineage.server.serverpackets.ServerBasePacket
    public byte[] getContent() {
        if (this._byte == null) {
            this._byte = getBytes();
        }
        return this._byte;
    }

    @Override // com.lineage.server.serverpackets.ServerBasePacket
    public String getType() {
        return getClass().getSimpleName();
    }
}
