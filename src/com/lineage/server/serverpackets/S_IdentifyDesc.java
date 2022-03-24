package com.lineage.server.serverpackets;

import com.lineage.server.datatables.ItemTable;
import com.lineage.server.model.Instance.L1ItemInstance;

public class S_IdentifyDesc extends ServerBasePacket {
    private byte[] _byte = null;

    public S_IdentifyDesc(L1ItemInstance item) {
        buildPacket(item);
    }

    private void buildPacket(L1ItemInstance item) {
        writeC(43);
        writeH(item.getItem().getItemDescId());
        StringBuilder name = new StringBuilder();
        switch (item.getItem().getBless()) {
            case 0:
                name.append("$227 ");
                break;
            case 2:
                name.append("$228 ");
                break;
        }
        name.append(item.getItem().getName());
        if (item.getItem().getItemId() == 40312 && item.getKeyId() != 0) {
            name.append(item.getInnKeyName());
        }
        switch (item.getItem().getType2()) {
            case 0:
                switch (item.getItem().getType()) {
                    case 1:
                        writeH(137);
                        writeC(3);
                        writeS(name.toString());
                        writeS(String.valueOf(item.getChargeCount()));
                        break;
                    case 2:
                        writeH(138);
                        writeC(2);
                        name.append(": $231 ");
                        name.append(String.valueOf(item.getRemainingTime()));
                        writeS(name.toString());
                        break;
                    case 7:
                        writeH(136);
                        writeC(3);
                        writeS(name.toString());
                        writeS(String.valueOf(item.getItem().getFoodVolume()));
                        break;
                    default:
                        writeH(138);
                        writeC(2);
                        writeS(name.toString());
                        break;
                }
                writeS(String.valueOf(item.getWeight()));
                return;
            case 1:
                writeH(134);
                writeC(3);
                writeS(name.toString());
                writeS(String.valueOf(item.getItem().getDmgSmall()) + "+" + item.getEnchantLevel());
                writeS(String.valueOf(item.getItem().getDmgLarge()) + "+" + item.getEnchantLevel());
                return;
            case 2:
                item.getItem().getItemId();
                writeH(OpcodesServer.S_OPCODE_CHARLOCK);
                writeC(2);
                writeS(name.toString());
                writeS(String.valueOf(Math.abs(item.getItem().get_ac())) + "+" + item.getEnchantLevel());
                return;
            default:
                return;
        }
    }

    public S_IdentifyDesc() {
        L1ItemInstance item = ItemTable.get().createItem(2);
        writeC(43);
        writeH(item.getItem().getItemDescId());
        writeH(134);
        writeC(3);
        writeS(item.getName());
        writeS(String.valueOf(item.getItem().getDmgSmall()) + "+" + item.getEnchantLevel());
        writeS(String.valueOf(item.getItem().getDmgLarge()) + "+" + item.getEnchantLevel());
        writeS(String.valueOf(item.getWeight()));
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
