package com.lineage.server.clientpackets;

import com.lineage.data.event.BaseResetSet;
import com.lineage.echo.ClientExecutor;
import com.lineage.server.datatables.ExpTable;
import com.lineage.server.datatables.sql.CharacterTable;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.serverpackets.S_CharReset;
import com.lineage.server.serverpackets.S_OwnCharStatus;
import com.lineage.server.utils.CalcInitHpMp;
import com.lineage.server.utils.CalcStat;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class C_CharReset extends ClientBasePacket {
    private static final Log _log = LogFactory.getLog(C_CharReset.class);

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public void start(byte[] decrypt, ClientExecutor client) {
        int hp;
        int mp;
        try {
            read(decrypt);
            L1PcInstance pc = client.getActiveChar();
            switch (readC()) {
                case 1:
                    int str = readC();
                    int intel = readC();
                    int wis = readC();
                    int dex = readC();
                    int con = readC();
                    int cha = readC();
                    if (BaseResetSet.RETAIN != 0) {
                        hp = (pc.getMaxHp() * BaseResetSet.RETAIN) / 100;
                        mp = (pc.getMaxMp() * BaseResetSet.RETAIN) / 100;
                    } else {
                        hp = CalcInitHpMp.calcInitHp(pc);
                        mp = CalcInitHpMp.calcInitMp(pc);
                    }
                    pc.sendPackets(new S_CharReset(pc, 1, hp, mp, 10, str, intel, wis, dex, con, cha));
                    initCharStatus(pc, hp, mp, str, intel, wis, dex, con, cha);
                    CharacterTable.get();
                    CharacterTable.saveCharStatus(pc);
                    break;
                case 2:
                    switch (readC()) {
                        case 0:
                            setLevelUp(pc, 1);
                            break;
                        case 1:
                            pc.addBaseStr(1);
                            setLevelUp(pc, 1);
                            break;
                        case 2:
                            pc.addBaseInt(1);
                            setLevelUp(pc, 1);
                            break;
                        case 3:
                            pc.addBaseWis(1);
                            setLevelUp(pc, 1);
                            break;
                        case 4:
                            pc.addBaseDex(1);
                            setLevelUp(pc, 1);
                            break;
                        case 5:
                            pc.addBaseCon(1);
                            setLevelUp(pc, 1);
                            break;
                        case 6:
                            pc.addBaseCha(1);
                            setLevelUp(pc, 1);
                            break;
                        case 7:
                            if (pc.getTempMaxLevel() - pc.getTempLevel() >= 10) {
                                setLevelUp(pc, 10);
                                break;
                            } else {
                                over();
                                return;
                            }
                        case 8:
                            switch (readC()) {
                                case 1:
                                    pc.addBaseStr(1);
                                    break;
                                case 2:
                                    pc.addBaseInt(1);
                                    break;
                                case 3:
                                    pc.addBaseWis(1);
                                    break;
                                case 4:
                                    pc.addBaseDex(1);
                                    break;
                                case 5:
                                    pc.addBaseCon(1);
                                    break;
                                case 6:
                                    pc.addBaseCha(1);
                                    break;
                            }
                            if (pc.getElixirStats() <= 0) {
                                saveNewCharStatus(pc);
                                break;
                            } else {
                                pc.sendPackets(new S_CharReset(pc.getElixirStats()));
                                over();
                                return;
                            }
                    }
                case 3:
                    int read1 = readC();
                    int read2 = readC();
                    int read3 = readC();
                    int read4 = readC();
                    int read5 = readC();
                    int read6 = readC();
                    pc.addBaseStr((byte) (read1 - pc.getBaseStr()));
                    pc.addBaseInt((byte) (read2 - pc.getBaseInt()));
                    pc.addBaseWis((byte) (read3 - pc.getBaseWis()));
                    pc.addBaseDex((byte) (read4 - pc.getBaseDex()));
                    pc.addBaseCon((byte) (read5 - pc.getBaseCon()));
                    pc.addBaseCha((byte) (read6 - pc.getBaseCha()));
                    saveNewCharStatus(pc);
                    break;
            }
        } catch (Exception ignored) {
        } finally {
            over();
        }
    }

    private void saveNewCharStatus(L1PcInstance pc) {
        pc.setInCharReset(false);
        if (pc.getOriginalAc() > 0) {
            pc.addAc(pc.getOriginalAc());
        }
        if (pc.getOriginalMr() > 0) {
            pc.addMr(0 - pc.getOriginalMr());
        }
        pc.refresh();
        pc.setCurrentHp(pc.getMaxHp());
        pc.setCurrentMp(pc.getMaxMp());
        if (pc.getTempMaxLevel() != pc.getLevel()) {
            pc.setLevel(pc.getTempMaxLevel());
            pc.setExp(ExpTable.getExpByLevel(pc.getTempMaxLevel()));
        }
        if (pc.getLevel() > 50) {
            pc.setBonusStats(pc.getLevel() - 50);
        } else {
            pc.setBonusStats(0);
        }
        pc.sendPackets(new S_OwnCharStatus(pc));
        L1ItemInstance item = pc.getInventory().findItemId(49142);
        if (item != null) {
            try {
                pc.getInventory().removeItem(item, 1);
                pc.save();
            } catch (Exception e) {
                _log.error(e.getLocalizedMessage(), e);
            }
        }
        L1Teleport.teleport(pc, 32628, 32772,  4, 4, false);
    }

    private void initCharStatus(L1PcInstance pc, int hp, int mp, int str, int intel, int wis, int dex, int con, int cha) {
        pc.addBaseMaxHp( (hp - pc.getBaseMaxHp()));
        pc.addBaseMaxMp( (mp - pc.getBaseMaxMp()));
        pc.addBaseStr((byte) (str - pc.getBaseStr()));
        pc.addBaseInt((byte) (intel - pc.getBaseInt()));
        pc.addBaseWis((byte) (wis - pc.getBaseWis()));
        pc.addBaseDex((byte) (dex - pc.getBaseDex()));
        pc.addBaseCon((byte) (con - pc.getBaseCon()));
        pc.addBaseCha((byte) (cha - pc.getBaseCha()));
        pc.addMr(0 - pc.getMr());
        pc.addDmgup(0 - pc.getDmgup());
        pc.addHitup(0 - pc.getHitup());
    }

    private void setLevelUp(L1PcInstance pc, int addLv) {
        pc.setTempLevel(pc.getTempLevel() + addLv);
        for (int i = 0; i < addLv; i++) {
            int randomHp = CalcStat.calcStatHp(pc.getType(), pc.getBaseMaxHp(), pc.getBaseCon(), pc.getOriginalHpup());
            int randomMp = CalcStat.calcStatMp(pc.getType(), pc.getBaseMaxMp(), pc.getBaseWis(), pc.getOriginalMpup());
            pc.addBaseMaxHp(randomHp);
            pc.addBaseMaxMp(randomMp);
        }
        pc.sendPackets(new S_CharReset(pc, pc.getTempLevel(), pc.getBaseMaxHp(), pc.getBaseMaxMp(), CalcStat.calcAc(pc.getTempLevel(), pc.getBaseDex()), pc.getBaseStr(), pc.getBaseInt(), pc.getBaseWis(), pc.getBaseDex(), pc.getBaseCon(), pc.getBaseCha()));
    }

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public String getType() {
        return getClass().getSimpleName();
    }
}
