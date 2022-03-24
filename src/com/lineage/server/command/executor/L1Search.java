package com.lineage.server.command.executor;

import com.lineage.DatabaseFactory;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_SystemMessage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.StringTokenizer;

public class L1Search implements L1CommandExecutor {
    private L1Search() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1Search();
    }

    @Override // com.lineage.server.command.executor.L1CommandExecutor
    public void execute(L1PcInstance pc, String cmdName, String arg) {
        try {
            StringTokenizer st = new StringTokenizer(arg);
            String type = "";
            String name = "";
            String add = "";
            boolean simpleS = true;
            int itCount = 0;
            while (st.hasMoreTokens()) {
                if (itCount == 1) {
                    add = "%";
                }
                String tempVar = st.nextToken();
                if (itCount != 0 || (!tempVar.equals("防具") && !tempVar.equals("武器") && !tempVar.equals("道具") && !tempVar.equals("變身") && !tempVar.equals("NPC"))) {
                    name = String.valueOf(name) + add + tempVar;
                } else {
                    simpleS = false;
                    type = tempVar;
                }
                itCount++;
            }
            if (!simpleS) {
                find_object(pc, type, name);
            } else {
                find_object(pc, name);
            }
        } catch (Exception e) {
            pc.sendPackets(new S_SystemMessage("導入 .find [防具,武器,道具,變身,NPC] [物品名稱]"));
        }
    }

    private void find_object(L1PcInstance pc, String type, String name) {
        String blessed;
        int count = 0;
        try {
            Connection con = DatabaseFactory.get().getConnection();
            PreparedStatement statement = null;
            boolean error = false;
            pc.sendPackets(new S_SystemMessage(" "));
            if (type.equals("防具")) {
                statement = con.prepareStatement("SELECT item_id,name,bless FROM armor WHERE name Like '%" + name + "%'");
            } else if (type.equals("武器")) {
                statement = con.prepareStatement("SELECT item_id,name,bless FROM weapon WHERE name Like '%" + name + "%'");
            } else if (type.equals("道具")) {
                statement = con.prepareStatement("SELECT item_id,name,bless FROM etcitem WHERE name Like '%" + name + "%'");
            } else if (type.equals("變身")) {
                statement = con.prepareStatement("SELECT polyid,name FROM polymorphs WHERE name Like '%" + name + "%'");
            } else if (type.equals("NPC")) {
                statement = con.prepareStatement("SELECT npcid,name FROM npc WHERE name Like '%" + name + "%'");
            } else {
                error = true;
                pc.sendPackets(new S_SystemMessage("導入 .find [防具,武器,道具,變身,NPC] [物品名稱]"));
            }
            if (!error) {
                ResultSet rs = statement.executeQuery();
                pc.sendPackets(new S_SystemMessage("正在搜索符合 '" + name.replace("%", " ") + " ' 的" + type + "名稱"));
                while (rs.next()) {
                    String str1 = rs.getString(1);
                    String str2 = rs.getString(2);
                    if (type.equals("防具") || type.equals("武器") || type.equals("道具")) {
                        int bless = rs.getInt(3);
                        if (bless == 1) {
                            blessed = "";
                        } else if (bless == 0) {
                            blessed = "\\fR";
                        } else {
                            blessed = "\\fY";
                        }
                        pc.sendPackets(new S_SystemMessage(String.valueOf(blessed) + "導入: " + str1 + ", " + str2));
                    } else {
                        pc.sendPackets(new S_SystemMessage("編號: " + str1 + ", " + str2));
                    }
                    count++;
                }
                rs.close();
                statement.close();
                con.close();
                pc.sendPackets(new S_SystemMessage("找到 " + count + "導入物品符合" + type + "類型。"));
            }
        } catch (Exception ignored) {
        }
    }

    private void find_object(L1PcInstance pc, String name) {
        String found;
        String blessed;
        String blessed2;
        String blessed3;
        try {
            Connection con = DatabaseFactory.get().getConnection();
            pc.sendPackets(new S_SystemMessage(" "));
            pc.sendPackets(new S_SystemMessage("正在搜索符合 '" + name.replace("%", " ") + " ' 的物品名稱:"));
            PreparedStatement statement = con.prepareStatement("SELECT item_id,name,bless FROM armor WHERE name Like '%" + name + "%'");
            int count1 = 0;
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                if (count1 == 0) {
                    pc.sendPackets(new S_SystemMessage("防具:"));
                }
                String str1 = rs.getString(1);
                String str2 = rs.getString(2);
                int bless = rs.getInt(3);
                if (bless == 1) {
                    blessed3 = "";
                } else if (bless == 0) {
                    blessed3 = "\\fR";
                } else {
                    blessed3 = "\\fY";
                }
                pc.sendPackets(new S_SystemMessage(String.valueOf(blessed3) + "導入: " + str1 + ", " + str2));
                count1++;
            }
            rs.close();
            statement.close();
            PreparedStatement statement2 = con.prepareStatement("SELECT item_id,name,bless FROM weapon WHERE name Like '%" + name + "%'");
            int count2 = 0;
            ResultSet rs2 = statement2.executeQuery();
            while (rs2.next()) {
                if (count2 == 0) {
                    pc.sendPackets(new S_SystemMessage("武器:"));
                }
                String str12 = rs2.getString(1);
                String str22 = rs2.getString(2);
                int bless2 = rs2.getInt(3);
                if (bless2 == 1) {
                    blessed2 = "";
                } else if (bless2 == 0) {
                    blessed2 = "\\fR";
                } else {
                    blessed2 = "\\fY";
                }
                pc.sendPackets(new S_SystemMessage(String.valueOf(blessed2) + "導入: " + str12 + ", " + str22));
                count2++;
            }
            rs2.close();
            statement2.close();
            PreparedStatement statement3 = con.prepareStatement("SELECT item_id,name,bless FROM etcitem WHERE name Like '%" + name + "%'");
            int count3 = 0;
            ResultSet rs3 = statement3.executeQuery();
            while (rs3.next()) {
                if (count3 == 0) {
                    pc.sendPackets(new S_SystemMessage("道具:"));
                }
                String str13 = rs3.getString(1);
                String str23 = rs3.getString(2);
                int bless3 = rs3.getInt(3);
                if (bless3 == 1) {
                    blessed = "";
                } else if (bless3 == 0) {
                    blessed = "\\fR";
                } else {
                    blessed = "\\fY";
                }
                pc.sendPackets(new S_SystemMessage(String.valueOf(blessed) + "導入: " + str13 + ", " + str23));
                count3++;
            }
            rs3.close();
            statement3.close();
            PreparedStatement statement4 = con.prepareStatement("SELECT polyid,name FROM polymorphs WHERE name Like '%" + name + "%'");
            int count4 = 0;
            ResultSet rs4 = statement4.executeQuery();
            while (rs4.next()) {
                if (count4 == 0) {
                    pc.sendPackets(new S_SystemMessage("變身:"));
                }
                pc.sendPackets(new S_SystemMessage("導入: " + rs4.getString(1) + ", " + rs4.getString(2)));
                count4++;
            }
            rs4.close();
            statement4.close();
            PreparedStatement statement5 = con.prepareStatement("SELECT npcid,name FROM npc WHERE name Like '%" + name + "%'");
            int count5 = 0;
            ResultSet rs5 = statement5.executeQuery();
            while (rs5.next()) {
                if (count5 == 0) {
                    pc.sendPackets(new S_SystemMessage("NPC:"));
                }
                pc.sendPackets(new S_SystemMessage("導入: " + rs5.getString(1) + ", " + rs5.getString(2)));
                count5++;
            }
            rs5.close();
            statement5.close();
            con.close();
            pc.sendPackets(new S_SystemMessage("搜索結果:"));
            String found2 = "";
            if (count1 > 0) {
                found2 = String.valueOf(found2) + "防具: " + count1 + "、";
            }
            if (count2 > 0) {
                found2 = String.valueOf(found2) + "武器: " + count2 + "、";
            }
            if (count3 > 0) {
                found2 = String.valueOf(found2) + "道具: " + count3 + "、";
            }
            if (count4 > 0) {
                found2 = String.valueOf(found2) + "變身: " + count4 + "、";
            }
            if (count5 > 0) {
                found2 = String.valueOf(found2) + "NPC: " + count5 + "。";
            }
            if (found2.length() > 0) {
                found = String.valueOf(found2.substring(0, found2.length() - 1)) + "。";
            } else {
                found = "找到 0 個物品";
            }
            pc.sendPackets(new S_SystemMessage(found));
        } catch (Exception ignored) {
        }
    }
}
