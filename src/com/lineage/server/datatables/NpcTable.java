package com.lineage.server.datatables;

import com.lineage.DatabaseFactory;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.templates.L1Npc;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class NpcTable {
    public static int ORC = -1;
    private static final Map<String, Constructor<?>> _constructorCache = new HashMap();
    private static final Map<String, Integer> _familyTypes = buildFamily();
    private static NpcTable _instance;
    private static final Log _log = LogFactory.getLog(NpcTable.class);
    private static final Map<Integer, L1Npc> _npcs = new HashMap();

    public static NpcTable get() {
        if (_instance == null) {
            _instance = new NpcTable();
        }
        return _instance;
    }

    public void load() {
        PerformanceTimer timer = new PerformanceTimer();
        loadNpcData();
        _log.info("載入NPC設置資料數量: " + _npcs.size() + "(" + timer.get() + "ms)");
    }

    private Constructor<?> getConstructor(String implName) {
        try {
            return Class.forName("com.lineage.server.model.Instance." + implName + "Instance").getConstructors()[0];
        } catch (ClassNotFoundException e) {
            _log.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    private void registerConstructorCache(String implName) {
        if (!implName.isEmpty() && !_constructorCache.containsKey(implName)) {
            _constructorCache.put(implName, getConstructor(implName));
        }
    }

    private void loadNpcData() {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("SELECT * FROM `npc`");
            rs = pstm.executeQuery();
            while (rs.next()) {
                L1Npc npc = new L1Npc();
                int npcId = rs.getInt("npcid");
                npc.set_npcId(npcId);
                npc.set_name(rs.getString("name"));
                npc.set_nameid(rs.getString("nameid"));
                String classname = rs.getString("classname");
                npc.set_classname(classname);
                npc.setNpcExecutor(this.addClass(npcId, classname));
                npc.setImpl(rs.getString("impl"));
                npc.set_gfxid(rs.getInt("gfxid"));
                npc.set_level(rs.getInt("lvl"));
                npc.set_hp(rs.getInt("hp"));
                npc.set_mp(rs.getInt("mp"));
                npc.set_ac(rs.getInt("ac"));
                npc.set_str(rs.getByte("str"));
                npc.set_con(rs.getByte("con"));
                npc.set_dex(rs.getByte("dex"));
                npc.set_wis(rs.getByte("wis"));
                npc.set_int(rs.getByte("intel"));
                npc.set_mr(rs.getInt("mr"));
                npc.set_exp(rs.getInt("exp"));
                npc.set_lawful(rs.getInt("lawful"));
                npc.set_size(rs.getString("size"));
                npc.set_weakAttr(rs.getInt("weakAttr"));
                npc.set_ranged(rs.getInt("ranged"));
                npc.setTamable(rs.getBoolean("tamable"));
                npc.set_passispeed(rs.getInt("passispeed"));
                npc.set_atkspeed(rs.getInt("atkspeed"));
                npc.setAtkMagicSpeed(rs.getInt("atk_magic_speed"));
                npc.setSubMagicSpeed(rs.getInt("sub_magic_speed"));
                npc.set_undead(rs.getInt("undead"));
                npc.set_poisonatk(rs.getInt("poison_atk"));
                npc.set_paralysisatk(rs.getInt("paralysis_atk"));
                npc.set_agro(rs.getBoolean("agro"));
                npc.set_agrososc(rs.getBoolean("agrososc"));
                npc.set_agrocoi(rs.getBoolean("agrocoi"));
                Integer family = _familyTypes.get(rs.getString("family"));
                if (family == null) {
                    npc.set_family(0);
                } else {
                    npc.set_family(family.intValue());
                }
                int agrofamily = rs.getInt("agrofamily");
                if (npc.get_family() == 0 && agrofamily == 1) {
                    npc.set_agrofamily(0);
                } else {
                    npc.set_agrofamily(agrofamily);
                }
                npc.set_agrogfxid1(rs.getInt("agrogfxid1"));
                npc.set_agrogfxid2(rs.getInt("agrogfxid2"));
                npc.set_picupitem(rs.getBoolean("picupitem"));
                npc.set_digestitem(rs.getInt("digestitem"));
                npc.set_bravespeed(rs.getBoolean("bravespeed"));
                npc.set_hprinterval(rs.getInt("hprinterval"));
                npc.set_hpr(rs.getInt("hpr"));
                npc.set_mprinterval(rs.getInt("mprinterval"));
                npc.set_mpr(rs.getInt("mpr"));
                npc.set_teleport(rs.getBoolean("teleport"));
                npc.set_randomlevel(rs.getInt("randomlevel"));
                npc.set_randomhp(rs.getInt("randomhp"));
                npc.set_randommp(rs.getInt("randommp"));
                npc.set_randomac(rs.getInt("randomac"));
                npc.set_randomexp(rs.getInt("randomexp"));
                npc.set_randomlawful(rs.getInt("randomlawful"));
                npc.set_damagereduction(rs.getInt("damage_reduction"));
                npc.set_hard(rs.getBoolean("hard"));
                npc.set_doppel(rs.getBoolean("doppel"));
                npc.set_IsTU(rs.getBoolean("IsTU"));
                npc.set_IsErase(rs.getBoolean("IsErase"));
                npc.setBowActId(rs.getInt("bowActId"));
                npc.setKarma(rs.getInt("karma"));
                npc.setTransformId(rs.getInt("transform_id"));
                npc.setTransformGfxId(rs.getInt("transform_gfxid"));
                npc.setLightSize(rs.getInt("light_size"));
                npc.setAmountFixed(rs.getBoolean("amount_fixed"));
                npc.setChangeHead(rs.getBoolean("change_head"));
                npc.setCantResurrect(rs.getBoolean("cant_resurrect"));
                registerConstructorCache(npc.getImpl());
                _npcs.put(Integer.valueOf(npcId), npc);
            }
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x004c, code lost:
        com.lineage.server.datatables.NpcTable._log.error("發生[NPC檔案]錯誤, 檢查檔案是否存在:" + r13 + " NpcId:" + r12);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x006c, code lost:
        r1 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x006d, code lost:
        com.lineage.server.datatables.NpcTable._log.error(r1.getLocalizedMessage(), r1);
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:13:0x004b A[ExcHandler: ClassNotFoundException (e java.lang.ClassNotFoundException), Splitter:B:1:0x0003] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private NpcExecutor addClass(int npcid, String className) {
        try {
            if (!className.equals("0")) {
                String newclass = className;
                String[] set = null;
                if (className.indexOf(" ") != -1) {
                    set = className.split(" ");

                    try {
                        newclass = set[0];
                    } catch (Exception var8) {
                    }
                }

                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("com.lineage.data.npc.");
                stringBuilder.append(newclass);
                Class<?> cls = Class.forName(stringBuilder.toString());
                NpcExecutor exe = (NpcExecutor)cls.getMethod("get").invoke((Object)null);
                if (set != null) {
                    exe.set_set(set);
                }

                return exe;
            }
        } catch (ClassNotFoundException var9) {
            String error = "發生[NPC檔案]錯誤, 檢查檔案是否存在:" + className + " NpcId:" + npcid;
            _log.error(error);
        } catch (Exception var10) {
            _log.error(var10.getLocalizedMessage(), var10);
        }

        return null;
    }

    public L1Npc getTemplate(int id) {
        return _npcs.get(Integer.valueOf(id));
    }

    public String getNpcName(int id) {
        L1Npc npcTemp = getTemplate(id);
        if (npcTemp != null) {
            return npcTemp.get_name();
        }
        _log.error("取回NPC名稱錯誤 沒有這個編號的NPC: " + id);
        return null;
    }

    public L1NpcInstance newNpcInstance(int id) {
        try {
            L1Npc npcTemp = getTemplate(id);
            if (npcTemp != null) {
                return newNpcInstance(npcTemp);
            }
            _log.error("依照NPCID取回新的L1NpcInstance資料發生異常(沒有這編號的NPC): " + id);
            return null;
        } catch (Exception e) {
            _log.error("NPCID:" + id + "/" + e.getLocalizedMessage(), e);
            return null;
        }
    }

    public L1NpcInstance newNpcInstance(L1Npc template) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        if (template == null) {
            try {
                _log.error("依照NPCID取回新的L1NpcInstance資料發生異常(NPC資料為空)");
                return null;
            } catch (Exception e) {
                _log.error("NPCID:" + template.get_npcId() + "/" + e.getLocalizedMessage(), e);
                return null;
            }
        } else {
            return (L1NpcInstance) _constructorCache.get(template.getImpl()).newInstance(template);
        }
    }

    private static Map<String, Integer> buildFamily() {
        Map<String, Integer> result = new HashMap<>();
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("select distinct(family) as family from npc WHERE NOT trim(family) =''");
            rs = pstm.executeQuery();
            int id = 1;
            while (rs.next()) {
                String family = rs.getString("family");
                id++;
                if (family.equalsIgnoreCase("orc")) {
                    ORC = id;
                }
                result.put(family, Integer.valueOf(id));
            }
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
        return result;
    }

    public int findNpcIdByName(String name) {
        for (L1Npc npc : _npcs.values()) {
            if (npc.get_name().equals(name)) {
                return npc.get_npcId();
            }
        }
        return 0;
    }

    public int findNpcIdByNameWithoutSpace(String name) {
        for (L1Npc npc : _npcs.values()) {
            if (npc.get_name().replace(" ", "").equals(name)) {
                return npc.get_npcId();
            }
        }
        return 0;
    }
}
