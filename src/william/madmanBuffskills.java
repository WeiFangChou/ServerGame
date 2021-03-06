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
                            msg = "??????";
                            break;
                        case 2:
                            msg = "??????";
                            break;
                        case 3:
                            msg = "??????";
                            break;
                        case 4:
                            msg = "??????";
                            break;
                        case 5:
                            msg = "??????";
                            break;
                        case 6:
                            msg = "??????";
                            break;
                        case 7:
                            msg = "??????";
                            break;
                    }
                    if (((Integer) aTempData.get(1)).intValue() != class_id) {
                        user.sendPackets(new S_SystemMessage("???????????????" + msg + "??????????????????"));
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
                user.sendPackets(new S_SystemMessage("??????" + ((Integer) aTempData.get(2)).intValue() + "??????????????????????????????"));
                return;
            }
        }
    }

    private static void getData15b() {
        try {
            Connection con = DatabaseFactory.get().getConnection();
            ResultSet rset = con.createStatement().executeQuery("SELECT * FROM ????????????????????????");
            if (rset != null) {
                while (rset.next()) {
                    ArrayList<Object> aReturn = new ArrayList<>();
                    aReturn.add(0, new Integer(rset.getInt("????????????")));
                    aReturn.add(1, new Integer(rset.getInt("????????????")));
                    aReturn.add(2, new Integer(rset.getInt("????????????")));
                    aReturn.add(3, new Integer(rset.getInt("????????????")));
                    if (rset.getString("????????????") == null || rset.getString("????????????").equals("") || rset.getString("????????????").equals("0")) {
                        aReturn.add(4, null);
                    } else {
                        aReturn.add(4, getArray(rset.getString("????????????"), ",", 1));
                    }
                    aReturn.add(5, new Integer(rset.getInt("???????????????")));
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
