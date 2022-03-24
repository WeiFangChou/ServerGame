package com.lineage.server.datatables;

import com.lineage.DatabaseFactory;
import com.lineage.server.templates.L1SuperCard;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SuperCardTable {
    private static final SuperCardTable INSTANCE = new SuperCardTable();
    private static final Log _log = LogFactory.getLog(SuperCardTable.class);
    private final ArrayList<L1SuperCard> cards = new ArrayList<>();

    public static SuperCardTable getInstance() {
        return INSTANCE;
    }

    private SuperCardTable() {
    }

    public void load() {
        PerformanceTimer timer = new PerformanceTimer();
        this.cards.clear();
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("select * from 卡片能力系統");
            rs = pstm.executeQuery();
            while (rs.next()) {
                L1SuperCard card = new L1SuperCard();
                card.setCardId(rs.getInt("道具編號"));
                card.setCardName(rs.getString("道具名稱"));
                card.setCardType(rs.getInt("類型"));
                card.setExtraHp(rs.getInt("血量"));
                card.setExtraMp(rs.getInt("魔量"));
                card.setExtraHpr(rs.getInt("血量回復量"));
                card.setExtraMpr(rs.getInt("魔量回復量"));
                card.setExtraStr(rs.getInt("力量"));
                card.setExtraDex(rs.getInt("敏捷"));
                card.setExtraInt(rs.getInt("智力"));
                card.setExtraCon(rs.getInt("體質"));
                card.setExtraWis(rs.getInt("精神"));
                card.setExtraCha(rs.getInt("魅力"));
                card.setExtraMr(rs.getInt("抗魔"));
                card.setExtraSp(rs.getInt("魔攻"));
                card.setExtraHit(rs.getInt("近距離命中"));
                card.setExtraDmg(rs.getInt("近距離傷害"));
                card.setExtraBowhit(rs.getInt("遠距離命中"));
                card.setExtraBowdmg(rs.getInt("遠距離傷害"));
                card.setExtraExp(rs.getInt("經驗值"));
                card.setExtraPoly(rs.getInt("變身編號"));
                card.setExtraWeight(rs.getInt("負重值"));
                card.setExtraEarth(rs.getInt("地屬性"));
                card.setExtraWind(rs.getInt("風屬性"));
                card.setExtraWater(rs.getInt("水屬性"));
                card.setExtraFire(rs.getInt("火屬性"));
                card.setExtraStun(rs.getInt("昏迷耐性"));
                card.setExtraStone(rs.getInt("石化耐性"));
                card.setExtraSleep(rs.getInt("睡眠耐性"));
                card.setExtrafreeze(rs.getInt("凍結耐性"));
                card.setExtraSustain(rs.getInt("支撐耐性"));
                card.setExtraBlind(rs.getInt("暗黑耐性"));
                card.setExtraBamage(rs.getInt("傷害減免"));
                card.setWakeHp(rs.getInt("覺醒血量"));
                card.setWakeMp(rs.getInt("覺醒魔量"));
                card.setWakeHpr(rs.getInt("覺醒血量回復量"));
                card.setWakeMpr(rs.getInt("覺醒魔量回復量"));
                card.setWakeStr(rs.getInt("覺醒力量"));
                card.setWakeDex(rs.getInt("覺醒敏捷"));
                card.setWakeInt(rs.getInt("覺醒智力"));
                card.setWakeCon(rs.getInt("覺醒體質"));
                card.setWakeWis(rs.getInt("覺醒精神"));
                card.setWakeCha(rs.getInt("覺醒魅力"));
                card.setWakeMr(rs.getInt("覺醒抗魔"));
                card.setWakeSp(rs.getInt("覺醒魔攻"));
                card.setWakeHit(rs.getInt("覺醒近距離命中"));
                card.setWakeDmg(rs.getInt("覺醒近距離傷害"));
                card.setWakeBowhit(rs.getInt("覺醒遠距離命中"));
                card.setWakeBowdmg(rs.getInt("覺醒遠距離傷害"));
                card.setWakeExp(rs.getInt("覺醒經驗值"));
                card.setWakeWeight(rs.getInt("覺醒負重值"));
                card.setWakeEarth(rs.getInt("覺醒地屬性"));
                card.setWakeWind(rs.getInt("覺醒風屬性"));
                card.setWakeWater(rs.getInt("覺醒水屬性"));
                card.setWakeFire(rs.getInt("覺醒火屬性"));
                card.setWakeStun(rs.getInt("覺醒昏迷耐性"));
                card.setWakeStone(rs.getInt("覺醒石化耐性"));
                card.setWakeSleep(rs.getInt("覺醒睡眠耐性"));
                card.setWakefreeze(rs.getInt("覺醒凍結耐性"));
                card.setWakeSustain(rs.getInt("覺醒支撐耐性"));
                card.setWakeBlind(rs.getInt("覺醒暗黑耐性"));
                card.setWakeBamage(rs.getInt("覺醒傷害減免"));
                this.cards.add(card);
            }
            _log.info("載入卡片系統資料數量: " + this.cards.size() + "(" + timer.get() + "ms)");
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
            this.cards.clear();
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    public L1SuperCard getCard(int cardId) {
        Iterator<L1SuperCard> it = this.cards.iterator();
        while (it.hasNext()) {
            L1SuperCard card = it.next();
            if (card.getCardId() == cardId) {
                return card;
            }
        }
        return null;
    }

    public ArrayList<L1SuperCard> getSameTypeCards(int cardType) {
        ArrayList<L1SuperCard> result = new ArrayList<>();
        if (cardType != 0) {
            Iterator<L1SuperCard> it = this.cards.iterator();
            while (it.hasNext()) {
                L1SuperCard card = it.next();
                if (card.getCardType() == cardType) {
                    result.add(card);
                }
            }
        }
        return result;
    }
}
