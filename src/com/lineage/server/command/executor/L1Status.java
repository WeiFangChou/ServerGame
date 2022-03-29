package com.lineage.server.command.executor;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_Lawful;
import com.lineage.server.serverpackets.S_OwnCharStatus;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SystemMessage;
import com.lineage.server.world.World;
import java.util.StringTokenizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1Status implements L1CommandExecutor {
    private static final Log _log = LogFactory.getLog(L1Status.class);
    private static final int _max_int = 20000;

    private L1Status() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1Status();
    }

    @Override // com.lineage.server.command.executor.L1CommandExecutor
    public void execute(L1PcInstance pc, String cmdName, String arg) throws Exception {
        L1PcInstance target;
        if (pc == null) {
            try {
                _log.warn("系統命令執行: " + cmdName + " " + arg + " 重置指定人物屬性。");
            } catch (Exception e) {
                if (pc == null) {
                    _log.error("錯誤的命令格式: " + getClass().getSimpleName());
                    return;
                }
                _log.error("錯誤的GM指令格式: " + getClass().getSimpleName() + " 執行的GM:" + pc.getName());
                pc.sendPackets(new S_ServerMessage(261));
                return;
            }
        }
        StringTokenizer st = new StringTokenizer(arg);
        String char_name = st.nextToken();
        String param = st.nextToken();
        int value = Integer.parseInt(st.nextToken());
        if (!char_name.equalsIgnoreCase("me")) {
            target = World.get().getPlayer(char_name);
        } else if (pc == null) {
            _log.error("指令異常: 指定人物不在線上，這個命令必須輸入正確人物名稱才能執行。");
            return;
        } else {
            target = pc;
        }
        if (target != null) {
            if (param.equalsIgnoreCase("AC")) {
                target.addAc((byte) (value - target.getAc()));
            } else if (param.equalsIgnoreCase("MR")) {
                target.addMr( (value - target.getMr()));
            } else if (param.equalsIgnoreCase("HIT")) {
                target.addHitup( (value - target.getHitup()));
            } else if (param.equalsIgnoreCase("DMG")) {
                target.addDmgup( (value - target.getDmgup()));
            } else {
                if (param.equalsIgnoreCase("HP")) {
                    if (value > _max_int) {
                        value = _max_int;
                    }
                    int maxHP = value - target.getBaseMaxHp();
                    if (target.getBaseMaxHp() + maxHP > _max_int) {
                        maxHP = 20000 - target.getBaseMaxHp();
                    }
                    target.addBaseMaxHp( maxHP);
                    target.setCurrentHpDirect(target.getMaxHp());
                } else if (param.equalsIgnoreCase("MP")) {
                    if (value > _max_int) {
                        value = _max_int;
                    }
                    int maxMP = value - target.getBaseMaxMp();
                    if (target.getBaseMaxMp() + maxMP > _max_int) {
                        maxMP = 20000 - target.getBaseMaxMp();
                    }
                    target.addBaseMaxMp( maxMP);
                    target.setCurrentMpDirect(target.getMaxMp());
                } else if (param.equalsIgnoreCase("LAWFUL") || param.equalsIgnoreCase("L")) {
                    target.setLawful(value);
                    target.sendPacketsAll(new S_Lawful(target));
                } else if (param.equalsIgnoreCase("KARMA") || param.equalsIgnoreCase("K")) {
                    target.setKarma(value);
                } else if (param.equalsIgnoreCase("GM")) {
                    if (value > 200) {
                        value = 200;
                    }
                    target.setAccessLevel( value);
                    target.sendPackets(new S_SystemMessage("取得GM權限"));
                } else if (param.equalsIgnoreCase("STR")) {
                    target.addBaseStr((byte) (value - target.getBaseStr()));
                } else if (param.equalsIgnoreCase("CON")) {
                    target.addBaseCon((byte) (value - target.getBaseCon()));
                } else if (param.equalsIgnoreCase("DEX")) {
                    target.addBaseDex((byte) (value - target.getBaseDex()));
                } else if (param.equalsIgnoreCase("INT")) {
                    target.addBaseInt((byte) (value - target.getBaseInt()));
                } else if (param.equalsIgnoreCase("WIS")) {
                    target.addBaseWis((byte) (value - target.getBaseWis()));
                } else if (param.equalsIgnoreCase("CHA")) {
                    target.addBaseCha((byte) (value - target.getBaseCha()));
                } else {
                    String e2 = "指令異常: 指令 " + param + " 不明確";
                    if (pc == null) {
                        _log.error(e2);
                        return;
                    } else {
                        pc.sendPackets(new S_SystemMessage(e2));
                        return;
                    }
                }
                target.save();
            }
            target.sendPackets(new S_OwnCharStatus(target));
            String ok = String.valueOf(target.getName()) + "的" + param + "屬性" + value + "變更完成";
            if (pc == null) {
                _log.info(ok);
            } else {
                pc.sendPackets(new S_SystemMessage(ok));
            }
        } else if (pc == null) {
            _log.error("指令異常: 指定人物不在線上，這個命令必須輸入正確人物名稱才能執行。");
        } else {
            pc.sendPackets(new S_ServerMessage(73, char_name));
        }
    }
}
