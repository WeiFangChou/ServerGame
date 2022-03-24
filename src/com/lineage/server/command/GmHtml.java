package com.lineage.server.command;

import com.lineage.commons.system.LanSecurityManager;
import com.lineage.server.datatables.lock.IpReading;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Location;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.world.World;
import java.util.TreeMap;

public class GmHtml {
    private final TreeMap<Integer, L1PcInstance> _allPcList = new TreeMap<>();
    private final TreeMap<Integer, String> _banList = new TreeMap<>();
    private TreeMap<Integer, String> _banTmpList;
    private final int _mode;
    private final L1PcInstance _pc;
    private final int _users = World.get().getAllPlayers().size();

    public GmHtml(L1PcInstance pc, int mode) {
        this._pc = pc;
        this._pc.get_other().set_page(0);
        this._pc.get_other().set_gmHtml(this);
        this._mode = mode;
        int keyPc = 0;
        for (L1PcInstance tgpc : World.get().getAllPlayers()) {
            this._allPcList.put(new Integer(keyPc), tgpc);
            keyPc++;
        }
        int keyBan = 0;
        for (String ban : LanSecurityManager.BANIPMAP.keySet()) {
            this._banList.put(new Integer(keyBan), ban);
            keyBan++;
        }
        for (String ban2 : LanSecurityManager.BANNAMEMAP.keySet()) {
            this._banList.put(new Integer(keyBan), ban2);
            keyBan++;
        }
        int keyTmpBan = 0;
        for (String ban3 : LanSecurityManager.BANIPPACK.keySet()) {
            this._banTmpList.put(new Integer(keyTmpBan), ban3);
            keyTmpBan++;
        }
    }

    public void show() {
        showPage(0);
    }

    public void action(String cmd) {
        if (cmd.equals("up")) {
            showPage(this._pc.get_other().get_page() - 1);
        } else if (cmd.equals("dn")) {
            showPage(this._pc.get_other().get_page() + 1);
        } else if (cmd.startsWith("K")) {
            startCmd(1, Integer.parseInt(cmd.substring(1)));
        } else if (cmd.startsWith("D")) {
            startCmd(2, Integer.parseInt(cmd.substring(1)));
        } else if (cmd.startsWith("M")) {
            startCmd(3, Integer.parseInt(cmd.substring(1)));
        } else if (cmd.startsWith("T")) {
            startCmd(4, Integer.parseInt(cmd.substring(1)));
        }
    }

    private void startCmd(int mode, int cmd) {
        int page = this._pc.get_other().get_page();
        if (page > 0) {
            page *= 10;
        }
        int xcmd = page + cmd;
        if (this._mode == 0) {
            switch (mode) {
                case 1:
                    L1PcInstance target_pc1 = this._allPcList.get(Integer.valueOf(xcmd));
                    this._pc.sendPackets(new S_ServerMessage(String.valueOf(target_pc1.getName()) + " 踢除下線。"));
                    L1PcInstance target_pcX1 = World.get().getPlayer(target_pc1.getName());
                    if (target_pcX1 != null) {
                        target_pcX1.getNetConnection().kick();
                        return;
                    }
                    return;
                case 2:
                    L1PcInstance target_pc2 = this._allPcList.get(Integer.valueOf(xcmd));
                    IpReading.get().add(target_pc2.getAccountName(), "GM命令:L1AccountBanKick 封鎖帳號");
                    this._pc.sendPackets(new S_ServerMessage(String.valueOf(target_pc2.getName()) + " 帳號封鎖。"));
                    L1PcInstance target_pcX2 = World.get().getPlayer(target_pc2.getName());
                    if (target_pcX2 != null) {
                        target_pcX2.getNetConnection().kick();
                        return;
                    }
                    return;
                case 3:
                    L1PcInstance target_pc3 = this._allPcList.get(Integer.valueOf(xcmd));
                    L1PcInstance target_pcX3 = World.get().getPlayer(target_pc3.getName());
                    if (target_pcX3 != null) {
                        StringBuilder macaddr = target_pcX3.getNetConnection().getMac();
                        if (macaddr != null) {
                            IpReading.get().add(macaddr.toString(), "GM命令:L1PowerKick 封鎖");
                            this._pc.sendPackets(new S_ServerMessage(String.valueOf(target_pcX3.getName()) + " 封鎖MAC。"));
                        }
                        target_pcX3.getNetConnection().kick();
                        return;
                    }
                    IpReading.get().add(target_pc3.getAccountName(), "GM命令:L1AccountBanKick 封鎖帳號");
                    this._pc.sendPackets(new S_ServerMessage(String.valueOf(target_pc3.getName()) + " (離線)帳號封鎖。"));
                    return;
                case 4:
                    L1PcInstance target_pc4 = this._allPcList.get(Integer.valueOf(xcmd));
                    L1PcInstance target_pcX4 = World.get().getPlayer(target_pc4.getName());
                    if (target_pcX4 != null) {
                        L1Location loc = L1Location.randomLocation(target_pcX4.getLocation(), 1, 2, false);
                        L1Teleport.teleport(this._pc, loc.getX(), loc.getY(), target_pcX4.getMapId(), this._pc.getHeading(), false);
                        this._pc.sendPackets(new S_ServerMessage("移動座標至指定人物身邊: " + target_pcX4.getName()));
                        return;
                    }
                    this._pc.sendPackets(new S_ServerMessage(73, target_pc4.getName()));
                    return;
                default:
                    return;
            }
        } else if (this._mode == 2) {
            switch (mode) {
                case 1:
                    String banInfo = this._banList.get(Integer.valueOf(xcmd));
                    IpReading.get().remove(banInfo);
                    this._pc.sendPackets(new S_ServerMessage("解除封鎖: " + banInfo));
                    return;
                default:
                    return;
            }
        } else if (this._mode == 3) {
            switch (mode) {
                case 1:
                    String banInfo2 = this._banTmpList.get(Integer.valueOf(xcmd));
                    LanSecurityManager.BANIPPACK.remove(banInfo2);
                    this._pc.sendPackets(new S_ServerMessage("解除暫時封鎖: " + banInfo2));
                    return;
                default:
                    return;
            }
        }
    }

    private void showPage(int page) {
        String banIp;
        String banIp2;
        L1PcInstance tgpc;
        int allpage = 0;
        StringBuilder stringBuilder = new StringBuilder();
        if (this._mode == 0) {
            allpage = this._allPcList.size() / 10;
            if (page > allpage || page < 0) {
                page = 0;
            }
            if (this._allPcList.size() % 10 != 0) {
                allpage++;
            }
            this._pc.get_other().set_page(page);
            int or = page * 10;
            stringBuilder.append(String.valueOf(String.valueOf(this._users)) + ",");
            int i = 0;
            for (Integer key : this._allPcList.keySet()) {
                if (i >= or && i < or + 10 && (tgpc = this._allPcList.get(key)) != null) {
                    stringBuilder.append(String.valueOf(tgpc.getName()) + "(" + tgpc.getAccountName() + ") PcLv:" + tgpc.getLevel() + ",");
                }
                i++;
            }
        } else if (this._mode == 2) {
            allpage = this._banList.size() / 10;
            if (page > allpage || page < 0) {
                page = 0;
            }
            if (this._banList.size() % 10 != 0) {
                allpage++;
            }
            this._pc.get_other().set_page(page);
            int or2 = page * 10;
            int i2 = 0;
            for (Integer key2 : this._banList.keySet()) {
                if (i2 >= or2 && i2 < or2 + 10 && (banIp2 = this._banList.get(key2)) != null) {
                    stringBuilder.append(String.valueOf(banIp2) + ",");
                }
                i2++;
            }
        } else if (this._mode == 3) {
            allpage = this._banTmpList.size() / 10;
            if (page > allpage || page < 0) {
                page = 0;
            }
            if (this._banTmpList.size() % 10 != 0) {
                allpage++;
            }
            this._pc.get_other().set_page(page);
            int or3 = page * 10;
            int i3 = 0;
            for (Integer key3 : this._banTmpList.keySet()) {
                if (i3 >= or3 && i3 < or3 + 10 && (banIp = this._banTmpList.get(key3)) != null) {
                    stringBuilder.append(String.valueOf(banIp) + ",");
                }
                i3++;
            }
        }
        if (allpage >= page + 1) {
            String[] clientStrAry = stringBuilder.toString().split(",");
            int length = clientStrAry.length - 1;
            if (this._mode == 2) {
                this._pc.sendPackets(new S_NPCTalkReturn(this._pc.getId(), "y_GmE", clientStrAry));
            } else if (this._mode == 3) {
                this._pc.sendPackets(new S_NPCTalkReturn(this._pc.getId(), "y_GmE", clientStrAry));
            } else if (length > 0) {
                this._pc.sendPackets(new S_NPCTalkReturn(this._pc.getId(), "y_Gm" + length, clientStrAry));
            }
        } else {
            this._pc.sendPackets(new S_ServerMessage("沒有可以顯示的項目"));
        }
    }
}
