package com.lineage.server.clientpackets;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.command.executor.L1AllBuff;
import com.lineage.server.datatables.lock.CharBuffReading;
import com.lineage.server.datatables.lock.IpReading;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Location;
import com.lineage.server.model.L1Party;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillMode;
import com.lineage.server.serverpackets.S_CharVisualUpdate;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillHaste;
import com.lineage.server.serverpackets.S_SystemMessage;
import com.lineage.server.world.World;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.concurrent.RejectedExecutionException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class C_CallPlayer extends ClientBasePacket {
    private static final Log _log = LogFactory.getLog(C_CallPlayer.class);

    public C_CallPlayer() {
    }

    public void start(byte[] decrypt, ClientExecutor client) {
        try {
            this.read(decrypt);
            L1PcInstance pc = client.getActiveChar();
            if (!pc.isGm()) {
                return;
            }

            String arg = this.readS();
            StringTokenizer stringtokenizer = new StringTokenizer(arg, ":");
            int mode = pc.getTempID();

            L1PcInstance target;
            L1Location loc;
            try {
                int mapid = Integer.parseInt(stringtokenizer.nextToken());
                String name = stringtokenizer.nextToken();
                if (name.isEmpty()) {
                    return;
                }

                target = World.get().getPlayer(name);
                if (target == null) {
                    pc.sendPackets(new S_ServerMessage(73, name));
                    return;
                }

                switch(mode) {
                    case 0:
                        target.clearAllSkill();
                        CharBuffReading.get().deleteBuff(target);
                        this.stopSkill(target);
                        pc.sendPackets(new S_ServerMessage(166, target.getName() + " Buff清除!"));
                        break;
                    case 1:
                        loc = L1Location.randomLocation(target.getLocation(), 1, 2, false);
                        pc.set_showId(target.get_showId());
                        L1Teleport.teleport(pc, loc.getX(), loc.getY(), target.getMapId(), pc.getHeading(), false);
                        pc.sendPackets(new S_ServerMessage(166, "移動座標至指定人物身邊: " + name));
                        break;
                    case 2:
                        L1Location gmloc = L1Location.randomLocation(pc.getLocation(), 1, 2, false);
                        L1Teleport.teleport(target, gmloc.getX(), gmloc.getY(), pc.getMapId(), target.getHeading(), false);
                        pc.sendPackets(new S_ServerMessage(166, "召回指定人物: " + name));
                        break;
                    case 3:
                        L1Party party = target.getParty();
                        if (party == null) {
                            break;
                        }

                        int x = pc.getX();
                        int y = pc.getY() + 2;
                        int map = pc.getMapId();
                        HashMap<Integer, L1PcInstance> pcs = new HashMap();
                        pcs.putAll(party.partyUsers());
                        if (pcs.isEmpty()) {
                            return;
                        }

                        if (pcs.size() <= 0) {
                            return;
                        }

                        Iterator var18 = pcs.values().iterator();

                        while(var18.hasNext()) {
                            L1PcInstance pc2 = (L1PcInstance)var18.next();

                            try {
                                L1Teleport.teleport(pc2, x, y, map, 5, true);
                                pc2.sendPackets(new S_SystemMessage("管理員召喚!"));
                            } catch (Exception var25) {
                            }
                        }

                        pcs.clear();
                        break;
                    case 4:
                        L1AllBuff.startPc(target);
                        break;
                    case 5:
                        pc.sendPackets(new S_SystemMessage(target.getName() + " 踢除下線。"));
                        target.getNetConnection().kick();
                        break;
                    case 6:
                        StringBuilder ipaddr = target.getNetConnection().getIp();
                        StringBuilder macaddr = target.getNetConnection().getMac();
                        if (ipaddr != null) {
                            IpReading.get().add(ipaddr.toString(), "GM命令:L1PowerKick 封鎖");
                        }

                        if (macaddr != null) {
                            IpReading.get().add(macaddr.toString(), "GM命令:L1PowerKick 封鎖");
                        }

                        pc.sendPackets(new S_SystemMessage(target.getName() + " 封鎖IP/MAC。"));
                        target.getNetConnection().kick();
                        break;
                    case 7:
                        IpReading.get().add(target.getAccountName(), "GM命令:L1AccountBanKick 封鎖帳號");
                        pc.sendPackets(new S_SystemMessage(target.getName() + " 帳號封鎖。"));
                        target.getNetConnection().kick();
                        break;
                    case 8:
                        target.setCurrentHp(0);
                        target.death((L1Character)null);
                        pc.sendPackets(new S_SystemMessage(target.getName() + " 人物死亡。"));
                }
            } catch (RejectedExecutionException var26) {
                if (arg.isEmpty()) {
                    return;
                }

                target = World.get().getPlayer(arg);
                if (target == null) {
                    pc.sendPackets(new S_ServerMessage(73, arg));
                    return;
                }

                loc = L1Location.randomLocation(target.getLocation(), 1, 2, false);
                L1Teleport.teleport(pc, loc.getX(), loc.getY(), target.getMapId(), pc.getHeading(), false);
                pc.sendPackets(new S_ServerMessage(166, "移動座標至指定人物身邊: " + arg));
            }
        } catch (Exception var27) {
        } finally {
            this.over();
        }

    }

    private void stopSkill(L1PcInstance pc) {
        int skillNum;
        for(skillNum = 1; skillNum <= 220; ++skillNum) {
            if (!L1SkillMode.get().isNotCancelable(skillNum) || pc.isDead()) {
                pc.removeSkillEffect(skillNum);
            }
        }

        pc.curePoison();
        pc.cureParalaysis();

        for(skillNum = 998; skillNum <= 1026; ++skillNum) {
            pc.removeSkillEffect(skillNum);
        }

        for(skillNum = 3000; skillNum <= 3047; ++skillNum) {
            if (!L1SkillMode.get().isNotCancelable(skillNum)) {
                pc.removeSkillEffect(skillNum);
            }
        }

        if (pc.getHasteItemEquipped() > 0) {
            pc.setMoveSpeed(0);
            pc.sendPacketsAll(new S_SkillHaste(pc.getId(), 0, 0));
        }

        pc.removeSkillEffect(4000);
        pc.sendPacketsAll(new S_CharVisualUpdate(pc));
    }

    public String getType() {
        return this.getClass().getSimpleName();
    }
}
