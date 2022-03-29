package com.lineage.server.datatables;

import com.lineage.DatabaseFactory;
import com.lineage.server.datatables.lock.CharItemsReading;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1CastleLocation;
import com.lineage.server.model.L1Clan;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.utils.SQLUtil;
import com.lineage.server.world.World;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CastleWarGiftTable {
    private static final Map<Integer, ArrayList<Gift>> _gifts = new HashMap();
    private static CastleWarGiftTable _instance;
    private static final Log _log = LogFactory.getLog(CastleWarGiftTable.class);

    public static CastleWarGiftTable get() {
        if (_instance == null) {
            _instance = new CastleWarGiftTable();
        }
        return _instance;
    }

    /* access modifiers changed from: private */
    public class Gift {
        private int _count;
        private int _itemid;
        private boolean _recover;

        private Gift() {
            this._recover = false;
        }

        /* synthetic */ Gift(CastleWarGiftTable castleWarGiftTable, Gift gift) {
            this();
        }
    }

    public void load() {
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("SELECT * FROM `server_castle_war_gift`");
            rs = ps.executeQuery();
            while (rs.next()) {
                int key = rs.getInt("castle_id");
                int itemid = rs.getInt("itemid");
                int count = rs.getInt("count");
                boolean recover = rs.getBoolean("recover");
                Gift e = new Gift(this, null);
                e._itemid = itemid;
                e._count = count;
                e._recover = recover;
                ArrayList<Gift> list = _gifts.get(Integer.valueOf(key));
                if (list == null) {
                    list = new ArrayList<>();
                }
                list.add(e);
                _gifts.put(Integer.valueOf(key), list);
            }
        } catch (SQLException e2) {
            _log.error(e2.getLocalizedMessage(), e2);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }

    public boolean isGift(int key) {
        if (_gifts.get(Integer.valueOf(key)) == null) {
            return false;
        }
        return true;
    }

    public void get_gift(int key) {
        L1Clan castle_clan = null;
        ArrayList<Gift> list = _gifts.get(key);
        if (list == null) {
            return;
        }
        try {
            castle_clan = L1CastleLocation.castleClan(key);

        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            if (castle_clan != null) {
                Iterator<Gift> iter = list.iterator();
                while (iter.hasNext()) {
                    Gift gift = iter.next();
                    if (gift._recover) {
                        recover_item(gift._itemid);
                    }
                    get_gift(castle_clan, gift._itemid, gift._count);
                }
            }
        }

    }

    private void get_gift(L1Clan castle_clan, int itemid, int count) {
        try {
            if (castle_clan.getOnlineClanMemberSize() > 0) {
                L1PcInstance[] onlineClanMember = castle_clan.getOnlineClanMember();
                for (L1PcInstance tgpc : onlineClanMember) {
                    L1ItemInstance item = ItemTable.get().createItem(itemid);
                    if (item != null) {
                        item.setCount((long) count);
                        tgpc.getInventory().storeItem(item);
                        tgpc.sendPackets(new S_ServerMessage("獲得攻城獎勵: " + item.getLogName()));
                    }
                }
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private void recover_item(int itemid) {
        try {
            for (L1PcInstance tgpc : World.get().getAllPlayers()) {
                L1ItemInstance t1 = tgpc.getInventory().findItemId(itemid);
                if (t1 != null) {
                    if (t1.isEquipped()) {
                        tgpc.getInventory().setEquipped(t1, false, false, false);
                    }
                    tgpc.getInventory().removeItem(t1);
                    tgpc.sendPackets(new S_ServerMessage((int) L1SkillId.NATURES_TOUCH, t1.getLogName()));
                }
            }
            CharItemsReading.get().del_item(itemid);
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
