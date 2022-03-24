package com.lineage.server.serverpackets;

import com.lineage.echo.OpcodesClient;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillId;

public class S_PacketBoxCooking extends ServerBasePacket {
    public static final int COOK_WINDOW = 52;
    public static final int ICON_COOKING = 53;
    private byte[] _byte = null;

    public S_PacketBoxCooking(int value) {
        writeC(40);
        writeC(52);
        writeC(L1SkillId.ILLUSION_AVATAR);
        writeC(49);
        writeC(OpcodesClient.C_OPCODE_BOOKMARKDELETE);
        writeC(2);
        writeC(1);
        writeC(value);
    }

    public S_PacketBoxCooking(L1PcInstance pc, int type, int time) {
        writeC(40);
        writeC(53);
        int food = (pc.get_food() * 10) - 250;
        food = food < 0 ? 0 : food;
        switch (type) {
            case 7:
                writeC(pc.getStr());
                writeC(pc.getInt());
                writeC(pc.getWis());
                writeC(pc.getDex());
                writeC(pc.getCon());
                writeC(pc.getCha());
                writeH(food);
                writeC(type);
                writeC(36);
                writeH(time);
                writeC(0);
                return;
            case 54:
                writeC(0);
                writeC(0);
                writeC(0);
                writeC(0);
                writeC(0);
                writeC(0);
                writeH(0);
                writeC(type);
                writeC(42);
                writeH(time);
                writeC(0);
                return;
            default:
                writeC(pc.getStr());
                writeC(pc.getInt());
                writeC(pc.getWis());
                writeC(pc.getDex());
                writeC(pc.getCon());
                writeC(pc.getCha());
                writeH(food);
                writeC(type);
                writeC(38);
                writeH(time);
                writeC(0);
                return;
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
