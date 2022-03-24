package com.lineage.data.npc;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.datatables.lock.ClanEmblemReading;
import com.lineage.server.datatables.lock.ClanReading;
import com.lineage.server.datatables.sql.CharacterTable;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Clan;
import com.lineage.server.model.L1ClanMatching;
import com.lineage.server.model.L1War;
import com.lineage.server.model.item.L1ItemId;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_CharTitle;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Item;
import com.lineage.server.world.World;
import com.lineage.server.world.WorldWar;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Npc_clan extends NpcExecutor {
    public static final String[] SKILLINFO = {"\\fR血盟狂暴的爆發力流遍全身!", "\\fR血盟寂靜的氣息壟罩著你!", "\\fR血盟魔擊的光緩緩的發亮!", "\\fR血盟消魔的光環保護著你!"};
    private static final int[][] _items = {new int[]{40044, 500}, new int[]{40045, 500}, new int[]{40046, 500}, new int[]{40047, 500}, new int[]{40048, 300}, new int[]{40049, 300}, new int[]{40050, 300}, new int[]{40051, 300}, new int[]{40052, 100}, new int[]{40053, 100}, new int[]{40054, 100}, new int[]{40055, 100}, new int[]{40398, 50}, new int[]{40399, 50}, new int[]{40400, 50}, new int[]{40397, 50}, new int[]{40318, 50}, new int[]{40304, 50}, new int[]{40305, 50}, new int[]{40306, 50}, new int[]{40307, 50}, new int[]{L1ItemId.SCROLL_OF_ENCHANT_WEAPON, L1SkillId.STATUS_BRAVE}, new int[]{L1ItemId.SCROLL_OF_ENCHANT_ARMOR, L1SkillId.STATUS_BRAVE}, new int[]{L1ItemId.POTION_OF_BLINDNESS, 50}};
    private static final Log _log = LogFactory.getLog(Npc_clan.class);
    private static final String[] _skillName = {"無", "狂暴", "寂靜", "魔擊", "消魔"};

    private Npc_clan() {
    }

    public static NpcExecutor get() {
        return new Npc_clan();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 3;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void talk(L1PcInstance pc, L1NpcInstance npc) {
        int clanid = pc.getClanid();
        if (pc.isCrown()) {
            if (clanid == 0) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "y_c1"));
            } else if (!pc.getClan().isClanskill()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "y_c1b"));
            } else {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "y_c1aL", new String[]{new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(pc.getClan().get_skilltime())}));
            }
        } else if (clanid == 0) {
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "y_c1c"));
        } else if (!pc.getClan().isClanskill()) {
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "y_c1g"));
        } else {
            Timestamp time = pc.getClan().get_skilltime();
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "y_c1a", new String[]{time.toString().substring(0, time.toString().indexOf("."))}));
        }
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void action(L1PcInstance pc, L1NpcInstance npc, String cmd, long amount) throws Exception {
        String htmlid = null;
        boolean html = false;
        boolean updatePc = false;
        if (pc.getClanid() == 0) {
            if (pc.isCrown()) {
                htmlid = "y_c1d";
            } else {
                htmlid = "y_c1e";
            }
        }
        if (htmlid != null) {
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), htmlid));
            return;
        }
        if (cmd.equals("1")) {
            if (pc.getClan().isClanskill()) {
                html = true;
                htmlid = "y_c3";
            } else {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "y_c1g"));
            }
        } else if (cmd.equals("2")) {
            if (!pc.isCrown()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "y_c1f"));
            } else if (!pc.getClan().isClanskill()) {
                get_clanSkill(pc, npc);
            }
        } else if (cmd.equals("3")) {
            if (pc.isCrown()) {
                if (pc.getId() == pc.getClan().getLeaderId()) {
                    delClan(pc);
                    pc.sendPackets(new S_CloseList(pc.getId()));
                } else {
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "y_c1f"));
                }
            }
        } else if (cmd.equals("a")) {
            if (pc.getClan().isClanskill()) {
                pc.get_other().set_clanskill(1);
                updatePc = true;
            }
        } else if (cmd.equals("b")) {
            if (pc.getClan().isClanskill()) {
                pc.get_other().set_clanskill(2);
                updatePc = true;
            }
        } else if (cmd.equals("c")) {
            if (pc.getClan().isClanskill()) {
                pc.get_other().set_clanskill(4);
                updatePc = true;
            }
        } else if (cmd.equals("d") && pc.getClan().isClanskill()) {
            pc.get_other().set_clanskill(8);
            updatePc = true;
        }
        if (updatePc) {
            html = true;
            htmlid = "y_c3a";
            try {
                pc.save();
            } catch (Exception e) {
                _log.error(e.getLocalizedMessage(), e);
            }
        }
        if (html) {
            int clanMan = pc.getClan().getOnlineClanMemberSize();
            String skillName = "";
            String start1 = " ";
            String start2 = " ";
            String start3 = " ";
            String start4 = " ";
            switch (pc.get_other().get_clanskill()) {
                case 0:
                    skillName = _skillName[0];
                    break;
                case 1:
                    skillName = _skillName[1];
                    start1 = "啟用";
                    break;
                case 2:
                    skillName = _skillName[2];
                    start2 = "啟用";
                    break;
                case 4:
                    skillName = _skillName[3];
                    start3 = "啟用";
                    break;
                case 8:
                    skillName = _skillName[4];
                    start4 = "啟用";
                    break;
            }
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), htmlid, new String[]{skillName, String.valueOf(clanMan), start1, start2, start3, start4, _skillName[1], _skillName[2], _skillName[3], _skillName[4]}));
        }
    }

    private void get_clanSkill(L1PcInstance pc, L1NpcInstance npc) throws Exception {
        Queue<String> itemListX = new ConcurrentLinkedQueue<>();
        if (!pc.isGm()) {
            int[][] iArr = _items;
            int length = iArr.length;
            for (int i = 0; i < length; i++) {
                int[] itemid = iArr[i];
                if (pc.getInventory().checkItemX(itemid[0], (long) itemid[1]) == null) {
                    long countX = pc.getInventory().countItems(itemid[0]);
                    L1Item itemX2 = ItemTable.get().getTemplate(itemid[0]);
                    if (countX > 0) {
                        itemListX.offer(String.valueOf(itemX2.getName()) + " (" + (((long) itemid[1]) - countX) + ")");
                    } else {
                        itemListX.offer(String.valueOf(itemX2.getName()) + " (" + itemid[1] + ")");
                    }
                }
            }
            if (itemListX.size() > 0) {
                for (String tgitem : itemListX) {
                    pc.sendPackets(new S_ServerMessage(337, tgitem));
                }
                itemListX.clear();
                pc.sendPackets(new S_CloseList(pc.getId()));
                return;
            }
            itemListX.clear();
            int[][] iArr2 = _items;
            int length2 = iArr2.length;
            for (int i2 = 0; i2 < length2; i2++) {
                int[] itemid2 = iArr2[i2];
                int ritemid = itemid2[0];
                int rcount = itemid2[1];
                L1ItemInstance item = pc.getInventory().checkItemX(ritemid, (long) rcount);
                if (item != null && pc.getInventory().removeItem(item, (long) rcount) != ((long) rcount)) {
                    pc.sendPackets(new S_CloseList(pc.getId()));
                    return;
                }
            }
        }
        Timestamp ts = new Timestamp((2592000 * 1000) + System.currentTimeMillis());
        pc.getClan().set_clanskill(true);
        pc.getClan().set_skilltime(ts);
        ClanReading.get().updateClan(pc.getClan());
        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "y_c2a"));
    }

    private void delClan(L1PcInstance pc) {
        L1PcInstance tg_pc;
        boolean isReTitle;
        L1Clan clan = pc.getClan();
        if (clan != null) {
            int clan_id = clan.getClanId();
            String player_name = pc.getName();
            String clan_name = clan.getClanName();
            String[] clan_member_name = clan.getAllMembers();
            int castleId = clan.getCastleId();
            int houseId = clan.getHouseId();
            if (castleId != 0) {
                pc.sendPackets(new S_ServerMessage(665));
            } else if (houseId != 0) {
                pc.sendPackets(new S_ServerMessage(665));
            } else {
                for (L1War war : WorldWar.get().getWarList()) {
                    if (war.checkClanInWar(clan_name)) {
                        pc.sendPackets(new S_ServerMessage(302));
                        return;
                    }
                }
                for (int i = 0; i < clan_member_name.length; i++) {
                    try {
                        L1PcInstance online_pc = World.get().getPlayer(clan_member_name[i]);
                        if (online_pc != null) {
                            tg_pc = online_pc;
                            online_pc.sendPacketsAll(new S_CharTitle(online_pc.getId()));
                            isReTitle = true;
                            online_pc.sendPackets(new S_ServerMessage(269, player_name, clan_name));
                        } else {
                            tg_pc = CharacterTable.get().restoreCharacter(clan_member_name[i]);
                            isReTitle = true;
                        }
                        if (tg_pc != null) {
                            tg_pc.setClanid(0);
                            tg_pc.setClanname("");
                            tg_pc.setClanRank(0);
                            if (isReTitle) {
                                tg_pc.setTitle("");
                            }
                            tg_pc.save();
                        }
                    } catch (Exception e) {
                        _log.error(e.getLocalizedMessage(), e);
                        return;
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }
                ClanEmblemReading.get().deleteIcon(clan_id);
                ClanReading.get().deleteClan(clan_name);
                L1ClanMatching cml = L1ClanMatching.getInstance();
                cml.deleteClanMatching(clan_name);
                cml.deleteClanMatchingApcList(clan);
            }
        }
    }
}
