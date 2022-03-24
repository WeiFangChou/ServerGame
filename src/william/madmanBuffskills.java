package william;

import com.lineage.DatabaseFactory;
import com.lineage.Server;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillUse;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.serverpackets.S_SystemMessage;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class madmanBuffskills {
    private static boolean NO_MORE_GET_DATA15b = false;
    public static final String TOKEN = ",";
    private static ArrayList<ArrayList<Object>> aData15b = new ArrayList<>();

    public static void main(String[] a) {
        try {
            Server.main(null);
        } catch (Exception ignored) {
        }
    }

    public static void forItemUSe(L1PcInstance user, L1ItemInstance itemInstance) throws Exception {
        int itemid = itemInstance.getItemId();
        if (!NO_MORE_GET_DATA15b) {
            NO_MORE_GET_DATA15b = true;
            getData15b();
        }
        for (int i = 0; i < aData15b.size(); i++) {
            ArrayList<?> aTempData = aData15b.get(i);
            if (((Integer) aTempData.get(0)).intValue() == itemid) {
                if (((Integer) aTempData.get(1)).intValue() != 0) {
                    byte class_id = 0;
                    String msg = "";
                    if (user.isCrown()) {
                        class_id = 1;
                    } else if (user.isKnight()) {
                        class_id = 2;
                    } else if (user.isWizard()) {
                        class_id = 3;
                    } else if (user.isElf()) {
                        class_id = 4;
                    } else if (user.isDarkelf()) {
                        class_id = 5;
                    } else if (user.isDragonKnight()) {
                        class_id = 6;
                    } else if (user.isIllusionist()) {
                        class_id = 7;
                    }
                    switch (((Integer) aTempData.get(1)).intValue()) {
                        case 1:
                            msg = "王族";
                            break;
                        case 2:
                            msg = "騎士";
                            break;
                        case 3:
                            msg = "法師";
                            break;
                        case 4:
                            msg = "妖精";
                            break;
                        case 5:
                            msg = "黑妖";
                            break;
                        case 6:
                            msg = "龍騎";
                            break;
                        case 7:
                            msg = "幻術";
                            break;
                    }
                    if (((Integer) aTempData.get(1)).intValue() != class_id) {
                        user.sendPackets(new S_SystemMessage("你的職業無" + msg + "的專屬道具。"));
                        return;
                    }
                }
                if (((Integer) aTempData.get(2)).intValue() == 0 || user.getLevel() >= ((Integer) aTempData.get(2)).intValue()) {
                    if (((Integer) aTempData.get(3)).intValue() != 0) {
                        user.getInventory().removeItem(user.getInventory().findItemId(((Integer) aTempData.get(0)).intValue()).getId(), 1);
                    }
                    if (((int[]) aTempData.get(4)) != null) {
                        int[] Skills = (int[]) aTempData.get(4);
                        int time = ((Integer) aTempData.get(5)).intValue();
                        for (int j = 0; j < Skills.length; j++) {
                            new L1SkillUse().handleCommands(user, Skills[j], user.getId(), user.getX(), user.getY(), time, 4);
                            user.sendPacketsAll(new S_DoActionGFX(user.getId(), 19));
                        }
                        return;
                    }
                    return;
                }
                user.sendPackets(new S_SystemMessage("等級" + ((Integer) aTempData.get(2)).intValue() + "以上才可使用此道具。"));
                return;
            }
        }
    }

    private static void getData15b() {
        try {
            Connection con = DatabaseFactory.get().getConnection();
            ResultSet rset = con.createStatement().executeQuery("SELECT * FROM 道具魔法卷軸系統");
            if (rset != null) {
                while (rset.next()) {
                    ArrayList<Object> aReturn = new ArrayList<>();
                    aReturn.add(0, new Integer(rset.getInt("道具編號")));
                    aReturn.add(1, new Integer(rset.getInt("職業限定")));
                    aReturn.add(2, new Integer(rset.getInt("等級限定")));
                    aReturn.add(3, new Integer(rset.getInt("是否刪除")));
                    if (rset.getString("魔法編號") == null || rset.getString("魔法編號").equals("") || rset.getString("魔法編號").equals("0")) {
                        aReturn.add(4, null);
                    } else {
                        aReturn.add(4, getArray(rset.getString("魔法編號"), ",", 1));
                    }
                    aReturn.add(5, new Integer(rset.getInt("魔法時間秒")));
                    aData15b.add(aReturn);
                }
            }
            if (con != null && !con.isClosed()) {
                con.close();
            }
        } catch (Exception ignored) {
        }
    }

    private static Object getArray(String s, String sToken, int iType) {
        StringTokenizer st = new StringTokenizer(s, sToken);
        int iSize = st.countTokens();
        if (iType == 1) {
            int[] iReturn = new int[iSize];
            for (int i = 0; i < iSize; i++) {
                iReturn[i] = Integer.parseInt(st.nextToken());
            }
            return iReturn;
        } else if (iType == 2) {
            String[] sReturn = new String[iSize];
            for (int i2 = 0; i2 < iSize; i2++) {
                sReturn[i2] = st.nextToken();
            }
            return sReturn;
        } else if (iType != 3) {
            return null;
        } else {
            String sReturn2 = null;
            for (int i3 = 0; i3 < iSize; i3++) {
                sReturn2 = st.nextToken();
            }
            return sReturn2;
        }
    }
}
