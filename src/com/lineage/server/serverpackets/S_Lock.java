package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.utils.Teleportation;

public class S_Lock extends ServerBasePacket {
    private byte[] _byte = null;

    public S_Lock(int i) {
        buildPacket(i);
    }

    public S_Lock(){
        this.buildPacket(-1);
    }
    public S_Lock(final L1PcInstance pc){
        pc.sendPackets(new S_Paralysis(
                S_Paralysis.TYPE_TELEPORT_UNLOCK, false));
        pc.setTeleportX(pc.getOleLocX());
        pc.setTeleportY(pc.getOleLocY());
        pc.setTeleportMapId(pc.getMapId());
        pc.setTeleportHeading(pc.getHeading());
        // 传送回原始座标
        Teleportation.teleportation(pc);
    }

    private void buildPacket(int i) {

        if (i >= 0) {
            this.writeC(i);
        } else {
            System.out.println("===传送回原始座标s===");
            this.writeC(OpcodesServer.S_OPCODE_CHARLOCK);
        }
        this.writeC(0x00);

//        writeC(OpcodesServer.S_OPCODE_CHARLOCK);
//        writeC(0);
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
