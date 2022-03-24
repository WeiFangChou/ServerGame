package com.lineage.data.npc.xljnet;

import com.lineage.DatabaseFactory;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_SystemMessage;
import com.lineage.server.templates.L1Item;
import com.lineage.server.utils.SQLUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import william.Poly.NpcPower;
import william.Poly.Npc_PowerLog;

public class Npc_NpcPower extends NpcExecutor {
    private static final Log _log = LogFactory.getLog(Npc_NpcPower.class);
    Random random = new Random();

    public static NpcExecutor get() {
        return new Npc_NpcPower();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 3;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void talk(L1PcInstance pc, L1NpcInstance npc) {
        try {
            pc.setCmd(-1);
            if (NpcPower.get().NpcPowerSize() > 0) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "y_npcp_0"));
            } else {
                pc.sendPackets(new S_SystemMessage("尚未開放。"));
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void action(L1PcInstance pc, L1NpcInstance npc, String cmd, long amount) {
        NpcPower Np;
        try {
            if (cmd.matches("powerlist")) {
                int size = NpcPower.get().NpcPowerSize();
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i <= size; i++) {
                    NpcPower NP = NpcPower.get().getPower(i);
                    if (NP != null) {
                        stringBuilder.append(String.valueOf(String.valueOf(NP.getNote())) + ",");
                    }
                }
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "y_npcp_list", stringBuilder.toString().split(",")));
            } else if (cmd.matches("[0-999]+")) {
                NpcPower Np2 = NpcPower.get().getPower(Integer.valueOf(cmd).intValue() + 1);
                if (Np2 != null) {
                    StringBuilder stringBuilder2 = new StringBuilder();
                    pc.setCmd(Integer.valueOf(cmd).intValue());
                    stringBuilder2.append("收藏品：,");
                    if (Np2.getPItemId() != null) {
                        int i2 = 0;
                        String[] item = Np2.getPItemId().split(",");
                        int j = item.length;
                        for (byte b = 0; b < j; b = (byte) (b + 1)) {
                            L1Item items = ItemTable.get().getTemplate(Integer.parseInt(item[b]));
                            String my = Np2.getPEnchant().split(",")[i2];
                            i2++;
                            stringBuilder2.append("+" + my + " " + items.getName() + ",");
                        }
                    }
                    stringBuilder2.append(" ,能力加成：,");
                    if (Np2.getPHp() != 0) {
                        stringBuilder2.append("體力 +" + Np2.getPHp() + ",");
                    }
                    if (Np2.getPMp() != 0) {
                        stringBuilder2.append("魔力 +" + Np2.getPMp() + ",");
                    }
                    if (Np2.getPHpr() != 0) {
                        stringBuilder2.append("體力回復量 +" + Np2.getPHpr() + ",");
                    }
                    if (Np2.getPMpr() != 0) {
                        stringBuilder2.append("魔力回復量 +" + Np2.getPMpr() + ",");
                    }
                    if (Np2.getPDmg() != 0) {
                        stringBuilder2.append("近距離傷害 +" + Np2.getPDmg() + ",");
                    }
                    if (Np2.getPBowDmg() != 0) {
                        stringBuilder2.append("遠距離傷害 +" + Np2.getPBowDmg() + ",");
                    }
                    if (Np2.getPHit() != 0) {
                        stringBuilder2.append("近距離命中 +" + Np2.getPHit() + ",");
                    }
                    if (Np2.getPBowHit() != 0) {
                        stringBuilder2.append("遠距離命中 +" + Np2.getPBowHit() + ",");
                    }
                    if (Np2.getPAc() != 0) {
                        stringBuilder2.append("防禦 " + Np2.getPAc() + ",");
                    }
                    if (Np2.getPReduction() != 0) {
                        stringBuilder2.append("傷害減免 +" + Np2.getPReduction() + ",");
                    }
                    if (Np2.getPDodge() != 0) {
                        stringBuilder2.append("閃避 +" + Np2.getPDodge() + ",");
                    }
                    if (Np2.getPSp() != 0) {
                        stringBuilder2.append("魔法攻擊 +" + Np2.getPSp() + ",");
                    }
                    if (Np2.getPMr() != 0) {
                        stringBuilder2.append("魔法防禦 +" + Np2.getPMr() + ",");
                    }
                    if (Np2.getPStr() != 0) {
                        stringBuilder2.append("力量 +" + Np2.getPStr() + ",");
                    }
                    if (Np2.getPDex() != 0) {
                        stringBuilder2.append("敏捷 +" + Np2.getPDex() + ",");
                    }
                    if (Np2.getPCon() != 0) {
                        stringBuilder2.append("體質 +" + Np2.getPCon() + ",");
                    }
                    if (Np2.getPInt() != 0) {
                        stringBuilder2.append("智力 +" + Np2.getPInt() + ",");
                    }
                    if (Np2.getPWis() != 0) {
                        stringBuilder2.append("精神 +" + Np2.getPWis() + ",");
                    }
                    if (Np2.getPCha() != 0) {
                        stringBuilder2.append("魅力 +" + Np2.getPCha() + ",");
                    }
                    if (Np2.getPFire() != 0) {
                        stringBuilder2.append("火屬性 +" + Np2.getPFire() + ",");
                    }
                    if (Np2.getPWind() != 0) {
                        stringBuilder2.append("風屬性 +" + Np2.getPWind() + ",");
                    }
                    if (Np2.getPEarth() != 0) {
                        stringBuilder2.append("地屬性 +" + Np2.getPEarth() + ",");
                    }
                    if (Np2.getPWater() != 0) {
                        stringBuilder2.append("水屬性 +" + Np2.getPWater() + ",");
                    }
                    if (Np2.getPFreeze() != 0) {
                        stringBuilder2.append("寒冰耐性 +" + Np2.getPFreeze() + ",");
                    }
                    if (Np2.getPSturn() != 0) {
                        stringBuilder2.append("昏迷耐性 +" + Np2.getPSturn() + ",");
                    }
                    if (Np2.getPStone() != 0) {
                        stringBuilder2.append("石化耐性 +" + Np2.getPStone() + ",");
                    }
                    if (Np2.getPSleep() != 0) {
                        stringBuilder2.append("睡眠耐性 +" + Np2.getPSleep() + ",");
                    }
                    if (Np2.getPSustain() != 0) {
                        stringBuilder2.append("支撐耐性 +" + Np2.getPSustain() + ",");
                    }
                    if (Np2.getPBlind() != 0) {
                        stringBuilder2.append("黑暗耐性 +" + Np2.getPBlind() + ",");
                    }
                    if (Np2.getPGiftId() != 0) {
                        stringBuilder2.append(String.valueOf(String.valueOf(ItemTable.get().getTemplate(Np2.getPGiftId()).getName())) + "(" + Np2.getPGiftCount() + "),");
                    }
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "y_npcp_1", stringBuilder2.toString().split(",")));
                }
            } else if (cmd.equalsIgnoreCase("NP1")) {
                if (!(pc.getCmd() == -1 || (Np = NpcPower.get().getPower(pc.getCmd() + 1)) == null)) {
                    if (search(pc.getAccountName(), Np.getQuest())) {
                        pc.sendPackets(new S_SystemMessage("已經完成此收藏品。"));
                        pc.setCmd(-1);
                        return;
                    }
                    boolean ok = false;
                    int i3 = 0;
                    String[] item2 = Np.getPItemId().split(",");
                    int j2 = item2.length;
                    byte b2 = 0;
                    while (true) {
                        if (b2 >= j2) {
                            break;
                        }
                        if (!pc.getInventory().checkNPItem(Integer.parseInt(item2[b2]), Integer.parseInt(Np.getPEnchant().split(",")[i3]))) {
                            ok = false;
                            break;
                        }
                        ok = true;
                        i3++;
                        b2 = (byte) (b2 + 1);
                    }
                    if (ok) {
                        int i22 = 0;
                        String[] item22 = Np.getPItemId().split(",");
                        int k = item22.length;
                        for (byte b1 = 0; b1 < k; b1 = (byte) (b1 + 1)) {
                            pc.getInventory().consumeNPItem(Integer.parseInt(item22[b1]), Integer.parseInt(Np.getPEnchant().split(",")[i22]));
                            i22++;
                        }
                        if (Np.getPGiftId() != 0) {
                            pc.getInventory().storeItem(Np.getPGiftId(), (long) Np.getPGiftCount());
                        }
                        pc.sendPackets(new S_SystemMessage("成功完成收藏，請重新登入獲取能力加成。"));
                        Npc_PowerLog.get().storeOther(pc.getId(), Np);
                        Index(pc, Np.getQuest());
                        pc.getQuest().set_end(Np.getQuest());
                    } else {
                        pc.sendPackets(new S_SystemMessage("收藏品不足。"));
                    }
                }
                pc.sendPackets(new S_CloseList(pc.getId()));
                pc.setCmd(-1);
            } else if (cmd.equalsIgnoreCase("NP3")) {
                print(pc);
            } else if (cmd.equalsIgnoreCase("NP2")) {
                Npc_PowerLog Np3 = Npc_PowerLog.get().getPower(pc.getId());
                if (Np3 != null) {
                    StringBuilder stringBuilder3 = new StringBuilder();
                    pc.setCmd(-1);
                    if (Np3.getPHp() != 0) {
                        stringBuilder3.append("體力 +" + Np3.getPHp() + ",");
                    }
                    if (Np3.getPMp() != 0) {
                        stringBuilder3.append("魔力 +" + Np3.getPMp() + ",");
                    }
                    if (Np3.getPHpr() != 0) {
                        stringBuilder3.append("體力回復量 +" + Np3.getPHpr() + ",");
                    }
                    if (Np3.getPMpr() != 0) {
                        stringBuilder3.append("魔力回復量 +" + Np3.getPMpr() + ",");
                    }
                    if (Np3.getPDmg() != 0) {
                        stringBuilder3.append("近距離傷害 +" + Np3.getPDmg() + ",");
                    }
                    if (Np3.getPBowDmg() != 0) {
                        stringBuilder3.append("遠距離傷害 +" + Np3.getPBowDmg() + ",");
                    }
                    if (Np3.getPHit() != 0) {
                        stringBuilder3.append("近距離命中 +" + Np3.getPHit() + ",");
                    }
                    if (Np3.getPBowHit() != 0) {
                        stringBuilder3.append("遠距離命中 +" + Np3.getPBowHit() + ",");
                    }
                    if (Np3.getPAc() != 0) {
                        stringBuilder3.append("防禦 " + Np3.getPAc() + ",");
                    }
                    if (Np3.getPReduction() != 0) {
                        stringBuilder3.append("傷害減免 +" + Np3.getPReduction() + ",");
                    }
                    if (Np3.getPDodge() != 0) {
                        stringBuilder3.append("閃避 +" + Np3.getPDodge() + ",");
                    }
                    if (Np3.getPSp() != 0) {
                        stringBuilder3.append("魔法攻擊 +" + Np3.getPSp() + ",");
                    }
                    if (Np3.getPMr() != 0) {
                        stringBuilder3.append("魔法防禦 +" + Np3.getPMr() + ",");
                    }
                    if (Np3.getPStr() != 0) {
                        stringBuilder3.append("力量 +" + Np3.getPStr() + ",");
                    }
                    if (Np3.getPDex() != 0) {
                        stringBuilder3.append("敏捷 +" + Np3.getPDex() + ",");
                    }
                    if (Np3.getPCon() != 0) {
                        stringBuilder3.append("體質 +" + Np3.getPCon() + ",");
                    }
                    if (Np3.getPInt() != 0) {
                        stringBuilder3.append("智力 +" + Np3.getPInt() + ",");
                    }
                    if (Np3.getPWis() != 0) {
                        stringBuilder3.append("精神 +" + Np3.getPWis() + ",");
                    }
                    if (Np3.getPCha() != 0) {
                        stringBuilder3.append("魅力 +" + Np3.getPCha() + ",");
                    }
                    if (Np3.getPFire() != 0) {
                        stringBuilder3.append("火屬性 +" + Np3.getPFire() + ",");
                    }
                    if (Np3.getPWind() != 0) {
                        stringBuilder3.append("風屬性 +" + Np3.getPWind() + ",");
                    }
                    if (Np3.getPEarth() != 0) {
                        stringBuilder3.append("地屬性 +" + Np3.getPEarth() + ",");
                    }
                    if (Np3.getPWater() != 0) {
                        stringBuilder3.append("水屬性 +" + Np3.getPWater() + ",");
                    }
                    if (Np3.getPFreeze() != 0) {
                        stringBuilder3.append("寒冰耐性 +" + Np3.getPFreeze() + ",");
                    }
                    if (Np3.getPSturn() != 0) {
                        stringBuilder3.append("昏迷耐性 +" + Np3.getPSturn() + ",");
                    }
                    if (Np3.getPStone() != 0) {
                        stringBuilder3.append("石化耐性 +" + Np3.getPStone() + ",");
                    }
                    if (Np3.getPSleep() != 0) {
                        stringBuilder3.append("睡眠耐性 +" + Np3.getPSleep() + ",");
                    }
                    if (Np3.getPSustain() != 0) {
                        stringBuilder3.append("支撐耐性 +" + Np3.getPSustain() + ",");
                    }
                    if (Np3.getPBlind() != 0) {
                        stringBuilder3.append("黑暗耐性 +" + Np3.getPBlind() + ",");
                    }
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "y_npcp_2", stringBuilder3.toString().split(",")));
                    return;
                }
                pc.sendPackets(new S_SystemMessage("沒有完成任何收藏品。"));
                pc.sendPackets(new S_CloseList(pc.getId()));
                pc.setCmd(-1);
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private boolean search(String s, int i) {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("SELECT `quest` FROM `道具兌換能力系統log_1` WHERE `account_name`=?");
            pstm.setString(1, s);
            rs = pstm.executeQuery();
            while (rs.next()) {
                if (rs.getInt(1) == i) {
                    return true;
                }
            }
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
        return false;
    }

    private void print(L1PcInstance pc) {
        HashSet<Integer> id = new HashSet<>();
        StringBuilder s = new StringBuilder();
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("SELECT `quest` FROM `道具兌換能力系統log_1` WHERE `account_name`=?");
            pstm.setString(1, pc.getAccountName());
            rs = pstm.executeQuery();
            while (rs.next()) {
                id.add(Integer.valueOf(rs.getInt(1)));
            }
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
        int k = 0;
        for (int i = 0; i <= NpcPower.get().NpcPowerSize(); i++) {
            NpcPower Np = NpcPower.get().getPower(i);
            if (Np != null) {
                Iterator<Integer> it = id.iterator();
                while (it.hasNext()) {
                    if (it.next().intValue() == Np.getQuest()) {
                        k++;
                        s.append(String.valueOf(String.valueOf(Np.getNote())) + ",");
                    }
                }
            }
        }
        if (k == 0) {
            s.append("無,");
        }
        pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "y_npcp_10", s.toString().split(",")));
    }

    private void Index(L1PcInstance pc, int i) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("INSERT INTO `道具兌換能力系統log_1` SET `account_name`=?,`quest`=?");
            int x = 0 + 1;
            pstm.setString(x, pc.getAccountName());
            pstm.setInt(x + 1, i);
            pstm.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close((ResultSet) null);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }
}
