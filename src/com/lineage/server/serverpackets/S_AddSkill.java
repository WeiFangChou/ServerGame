package com.lineage.server.serverpackets;

import com.lineage.server.datatables.SkillsTable;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.templates.L1Skills;

public class S_AddSkill extends ServerBasePacket {
    private byte[] _byte = null;

    public S_AddSkill(L1PcInstance pc, int skillid) {
        int[] skillList = new int[32];
        L1Skills skill = SkillsTable.get().getTemplate(skillid);
        int skillLevel = skill.getSkillLevel() - 1;
        skillList[skillLevel] = skillList[skillLevel] + skill.getId();
        writeC(48);
        writeC(32);
        for (int element : skillList) {
            writeC(element);
        }
        if (pc != null) {
            pc.setSkillMastery(skillid);
        }
    }

    public S_AddSkill(L1PcInstance pc, int[] skills) {
        int i6 = skills[4] + skills[5] + skills[6] + skills[7];
        int j6 = skills[8] + skills[9];
        writeC(48);
        if (i6 > 0 && j6 == 0) {
            writeC(50);
        } else if (j6 > 0) {
            writeC(100);
        } else {
            writeC(32);
        }
        writeC(skills[0]);
        writeC(skills[1]);
        writeC(skills[2]);
        writeC(skills[3]);
        writeC(skills[4]);
        writeC(skills[5]);
        writeC(skills[6]);
        writeC(skills[7]);
        writeC(skills[8]);
        writeC(skills[9]);
        writeC(skills[10]);
        writeC(skills[11]);
        writeC(skills[12]);
        writeC(skills[13]);
        writeC(skills[14]);
        writeC(skills[15]);
        writeC(skills[16]);
        writeC(skills[17]);
        writeC(skills[18]);
        writeC(skills[19]);
        writeC(skills[20]);
        writeC(skills[21]);
        writeC(skills[22]);
        writeC(skills[23]);
        writeC(skills[24]);
        writeC(skills[25]);
        writeC(skills[26]);
        writeC(skills[27]);
        int[] ix = {skills[0], skills[1], skills[2], skills[3], skills[4], skills[5], skills[6], skills[7], skills[8], skills[9], skills[10], skills[11], skills[12], skills[13], skills[14], skills[15], skills[16], skills[17], skills[18], skills[19], skills[20], skills[21], skills[22], skills[23], skills[24], skills[25], skills[26], skills[27]};
        for (int i = 0; i < ix.length; i++) {
            int type = ix[i];
            int rtType = 128;
            int rt = 0;
            int skillid = -1;
            while (rt < 8) {
                if (type - rtType >= 0) {
                    type -= rtType;
                    switch (rtType) {
                        case 1:
                            skillid = (i << 3) + 1;
                            break;
                        case 2:
                            skillid = (i << 3) + 2;
                            break;
                        case 4:
                            skillid = (i << 3) + 3;
                            break;
                        case 8:
                            skillid = (i << 3) + 4;
                            break;
                        case 16:
                            skillid = (i << 3) + 5;
                            break;
                        case 32:
                            skillid = (i << 3) + 6;
                            break;
                        case 64:
                            skillid = (i << 3) + 7;
                            break;
                        case 128:
                            skillid = (i << 3) + 8;
                            break;
                    }
                    if (!(skillid == -1 || pc == null)) {
                        pc.setSkillMastery(skillid);
                    }
                }
                rt++;
                rtType >>= 1;
            }
        }
    }

    public S_AddSkill(L1PcInstance pc, int level1, int level2, int level3, int level4, int level5, int level6, int level7, int level8, int level9, int level10, int knight1, int knight2, int de1, int de2, int royal, int un, int elf1, int elf2, int elf3, int elf4, int elf5, int elf6, int k1, int k2, int k3, int l1, int l2, int l3) {
        int i6 = level5 + level6 + level7 + level8;
        int j6 = level9 + level10;
        writeC(48);
        if (i6 > 0 && j6 == 0) {
            writeC(50);
        } else if (j6 > 0) {
            writeC(100);
        } else {
            writeC(32);
        }
        writeC(level1);
        writeC(level2);
        writeC(level3);
        writeC(level4);
        writeC(level5);
        writeC(level6);
        writeC(level7);
        writeC(level8);
        writeC(level9);
        writeC(level10);
        writeC(knight1);
        writeC(knight2);
        writeC(de1);
        writeC(de2);
        writeC(royal);
        writeC(un);
        writeC(elf1);
        writeC(elf2);
        writeC(elf3);
        writeC(elf4);
        writeC(elf5);
        writeC(elf6);
        writeC(k1);
        writeC(k2);
        writeC(k3);
        writeC(l1);
        writeC(l2);
        writeC(l3);
        int[] ix = {level1, level2, level3, level4, level5, level6, level7, level8, level9, level10, knight1, knight2, de1, de2, royal, un, elf1, elf2, elf3, elf4, elf5, elf6, k1, k2, k3, l1, l2, l3};
        for (int i = 0; i < ix.length; i++) {
            int type = ix[i];
            int rtType = 128;
            int rt = 0;
            int skillid = -1;
            while (rt < 8) {
                if (type - rtType >= 0) {
                    type -= rtType;
                    switch (rtType) {
                        case 1:
                            skillid = (i << 3) + 1;
                            break;
                        case 2:
                            skillid = (i << 3) + 2;
                            break;
                        case 4:
                            skillid = (i << 3) + 3;
                            break;
                        case 8:
                            skillid = (i << 3) + 4;
                            break;
                        case 16:
                            skillid = (i << 3) + 5;
                            break;
                        case 32:
                            skillid = (i << 3) + 6;
                            break;
                        case 64:
                            skillid = (i << 3) + 7;
                            break;
                        case 128:
                            skillid = (i << 3) + 8;
                            break;
                    }
                    if (!(skillid == -1 || pc == null)) {
                        pc.setSkillMastery(skillid);
                    }
                }
                rt++;
                rtType >>= 1;
            }
        }
    }

    public S_AddSkill(L1PcInstance pc, int mode, int dx) {
        writeC(48);
        writeC(32);
        for (int i = 0; i < 28; i++) {
            if (i == mode) {
                writeC(dx);
            } else {
                writeC(0);
            }
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
