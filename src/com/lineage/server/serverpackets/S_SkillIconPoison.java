package com.lineage.server.serverpackets;

public class S_SkillIconPoison extends ServerBasePacket {
    public S_SkillIconPoison(int type, int time) {
        writeC(40);
        writeC(161);
        writeC(type);
        if (type == 2) {
            writeH(0);
            writeC(time);
            writeC(0);
            return;
        }
        writeH(time);
        writeC(0);
        writeC(0);
    }

    @Override // com.lineage.server.serverpackets.ServerBasePacket
    public byte[] getContent() {
        return getBytes();
    }

    @Override // com.lineage.server.serverpackets.ServerBasePacket
    public String getType() {
        return "[S] " + getClass().getSimpleName() + " [S->C 發送技能圖示封包(毒麻痺負面效果圖示)]";
    }
}
