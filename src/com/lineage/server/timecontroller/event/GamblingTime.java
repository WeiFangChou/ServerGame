package com.lineage.server.timecontroller.event;

import com.lineage.data.event.GamblingSet;
import com.lineage.data.event.gambling.Gambling;
import com.lineage.data.event.gambling.GamblingNpc;
import com.lineage.server.datatables.lock.GamblingReading;
import com.lineage.server.model.Instance.L1DoorInstance;
import com.lineage.server.model.Instance.L1GamInstance;
import com.lineage.server.model.Instance.L1GamblingInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.OpcodesServer;
import com.lineage.server.serverpackets.S_NpcChat;
import com.lineage.server.serverpackets.S_NpcChatPacket;
import com.lineage.server.serverpackets.S_NpcChatShouting;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Gambling;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.timecontroller.server.ServerRestartTimer;
import com.lineage.server.world.World;
import java.util.Collection;
import java.util.Random;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GamblingTime extends TimerTask {
    private static Gambling _gambling;
    private static int _gamblingNo = 100;
    private static boolean _isStart = false;
    private static boolean _issystem = true;
    private static final Log _log = LogFactory.getLog(GamblingTime.class);
    private static final String[] _msg = {"剛剛喝到的是綠水嗎?等等跑就知道~^^~", "隔壁跑道聽說昨天踩到釘子.....", "買我啦!!看我的臉就知道我贏!!", "快點跑完我也想去打一下副本~~", "你在看我嗎??你可以在靠近一點...", "飛龍都不一定跑贏我!看我強壯的雞腿!", "那個誰誰誰!!等等不要跑超過我黑...", "有沒騎士在場阿?給瓶勇水喝喝~~", "地球是很危險的...", "誰給我來一下祝福!加持!加持~", "咦~~有一個參賽者是傳說中的跑道之王...", "沒事!沒事!!哥只是個傳說~~", "隔壁的~你剛剛喝什麼?你是不是作弊??", "肚子好餓...沒吃飯能贏嗎??", "哇靠~~今天感覺精力充沛耶!!", "隔壁的!!你控制一下不要一直放屁!!", "嗯......嗯......其他幾個是憋三,我會贏....", "我剛剛好像喝多了...頭還在暈...", "昨晚的妞真正丫，喝綠水算三小。", "肚子餓死了，跑不動了。", "輸贏都行啦，娛樂而已。", "小賭怡情，大賭傷身。", "我要放點水。經常贏都有點不好意思了。", "【強化勇氣的藥水】是幹嘛的？我這有一罐。", "昨晚被吵死了，現在都覺得好累。", "阿幹....不要看我啦!!會影響我心情!!", "說什麼呢~~你們不想我贏阿!!!", "小賭可以養家活口!!大賭可以興邦建國!!", "賭是不好的....不賭是萬萬不行的...."};
    private static Random _random = new Random();
    private ScheduledFuture<?> _timer;

    public void start() {
        _gamblingNo = GamblingReading.get().maxId();
        int timeMillis = GamblingSet.GAMADENATIME * 60 * L1SkillId.STATUS_BRAVE;
        this._timer = GeneralThreadPool.get().scheduleAtFixedRate((TimerTask) this, (long) timeMillis, (long) timeMillis);
    }

    public static int get_gamblingNo() {
        return _gamblingNo;
    }

    public static Gambling get_gambling() {
        return _gambling;
    }

    public static boolean isStart() {
        return _isStart;
    }

    public static void set_status(boolean b) {
        _issystem = b;
    }

    public void run() {
        try {
            if (!_issystem) {
                if (_gambling != null) {
                    _gambling.delAllNpc();
                    _gambling.clear();
                    _gambling = null;
                    if (_isStart) {
                        _isStart = false;
                    }
                }
            } else if (_gambling != null) {
                if (_gambling != null) {
                    _gambling.delAllNpc();
                    _gambling.clear();
                    _gambling = null;
                    if (_isStart) {
                        _isStart = false;
                    }
                }
            } else if (!ServerRestartTimer.isRtartTime()) {
                doorOpen(false);
                for (L1PcInstance listner : World.get().getAllPlayers()) {
                    if (listner.isShowWorldChat()) {
                        listner.sendPackets(new S_ServerMessage("奇岩賽狗場比賽即將開始。"));
                    }
                }
                _gambling = new Gambling();
                _gambling.set_gmaNpc(0);
                boolean is5m = true;
                int timeS = 600;
                while (is5m) {
                    Thread.sleep(1000);
                    switch (timeS) {
                        case 0:
                            doorOpen(true);
                            npcChat(0, 3, null);
                            _gambling.startGam();
                            is5m = false;
                            break;
                        case 1:
                            _isStart = true;
                            npcChat(0, 2, null);
                            break;
                        case 2:
                        case 3:
                        case 4:
                        case 5:
                        case 6:
                        case 7:
                        case 8:
                        case 9:
                        case 10:
                            npcChat(timeS, 1, null);
                            break;
                        case 60:
                        case OpcodesServer.S_OPCODE_STRUP:
                        case OpcodesServer.S_OPCODE_INVLIST:
                        case 240:
                        case 300:
                        case 360:
                        case 420:
                        case 480:
                        case 540:
                        case 600:
                            npcChat(timeS / 60, 0, null);
                            break;
                    }
                    timeS--;
                }
                _gambling.set_allRate();
                Thread.sleep(2000);
                for (GamblingNpc gamblingNpc : _gambling.get_allNpc().values()) {
                    npcChat(0, 4, gamblingNpc);
                    Thread.sleep(1000);
                }
                while (_gambling.get_oneNpc() == null) {
                    Thread.sleep(100);
                }
                if (_gambling.get_oneNpc() != null) {
                    GamblingNpc one = _gambling.get_oneNpc();
                    npcChat(0, 5, one);
                    String onename = one.get_npc().getNameId();
                    for (L1PcInstance listner2 : World.get().getAllPlayers()) {
                        if (listner2.isShowWorldChat()) {
                            listner2.sendPackets(new S_ServerMessage(166, "$375(" + _gamblingNo + ")$366 " + onename + " 賠率: " + _gambling.get_oneNpc().get_rate() + " 下注金額: " + one.get_adena()));
                        }
                    }
                    _log.info("奇岩賭場:" + _gamblingNo + " 優勝者:" + one.get_npc().getName() + "/" + one.get_xId() + "-" + _gambling.WIN + " (" + one.get_adena() + ")");
                    int npcid = _gambling.get_oneNpc().get_npc().getNpcId();
                    double rate = _gambling.get_oneNpc().get_rate();
                    long adena = _gambling.get_allAdena();
                    int outcount = (int) (_gambling.get_oneNpc().get_adena() / ((long) GamblingSet.GAMADENA));
                    L1Gambling gambling = new L1Gambling();
                    gambling.set_id(_gamblingNo);
                    gambling.set_adena(adena);
                    gambling.set_rate(rate);
                    gambling.set_gamblingno(String.valueOf(_gamblingNo) + "-" + npcid);
                    gambling.set_outcount(outcount);
                    GamblingReading.get().add(gambling);
                }
                synchronized (this) {
                    _gamblingNo++;
                }
                Thread.sleep(20000);
                if (_gambling != null) {
                    _gambling.delAllNpc();
                    _gambling.clear();
                    _gambling = null;
                    if (_isStart) {
                        _isStart = false;
                    }
                }
            } else if (_gambling != null) {
                _gambling.delAllNpc();
                _gambling.clear();
                _gambling = null;
                if (_isStart) {
                    _isStart = false;
                }
            }
        } catch (Exception e) {
            _log.error("奇岩賭場時間軸異常重啟", e);
            GeneralThreadPool.get().cancel(this._timer, false);
            new GamblingTime().start();
            if (_gambling != null) {
                _gambling.delAllNpc();
                _gambling.clear();
                _gambling = null;
                if (_isStart) {
                    _isStart = false;
                }
            }
        } catch (Throwable th) {
            if (_gambling != null) {
                _gambling.delAllNpc();
                _gambling.clear();
                _gambling = null;
                if (_isStart) {
                    _isStart = false;
                    throw th;
                }
            }
        }
    }

    private void doorOpen(boolean isOpen) {
        L1DoorInstance.openGam(isOpen);
    }

    private void npcChat(int i, int mode, GamblingNpc gamblingNpc) {
        Collection<L1Object> allObj = World.get().getObject();
        for (L1Object obj : allObj) {
            if (obj instanceof L1GamInstance) {
                L1GamInstance npc = (L1GamInstance) obj;
                switch (mode) {
                    case 0:
                        if (_random.nextInt(100) < 20) {
                            npc.broadcastPacketX10(new S_NpcChat(npc, _msg[_random.nextInt(_msg.length)]));
                            break;
                        } else {
                            continue;
                        }
                }
            }
        }
        for (L1Object obj2 : allObj) {
            if (obj2 instanceof L1GamblingInstance) {
                L1GamblingInstance npc2 = (L1GamblingInstance) obj2;
                int npcId = npc2.getNpcId();
                switch (mode) {
                    case 0:
                        npc2.broadcastPacketX10(new S_NpcChatShouting(npc2, "$376 " + i + "$377"));
                        break;
                    case 1:
                        if (npcId != 91172) {
                            npc2.broadcastPacketX10(new S_NpcChatShouting(npc2, String.valueOf(i)));
                            break;
                        } else {
                            break;
                        }
                    case 2:
                        if (npcId != 91172) {
                            npc2.broadcastPacketX10(new S_NpcChatShouting(npc2, "$363"));
                            break;
                        } else {
                            break;
                        }
                    case 3:
                        if (npcId != 91172) {
                            npc2.broadcastPacketX10(new S_NpcChatShouting(npc2, "$364"));
                            break;
                        } else {
                            break;
                        }
                    case 4:
                        if (npcId != 91172) {
                            String npcname = gamblingNpc.get_npc().getNameId();
                            String si = String.valueOf(gamblingNpc.get_rate());
                            int re = si.indexOf(".");
                            if (re != -1) {
                                si = si.substring(0, re + 2);
                            }
                            String toUser = String.valueOf(npcname) + " $402 " + si + "$367";
                            if (npc2 != null) {
                                npc2.broadcastPacketAll(new S_NpcChatPacket(npc2, toUser, 2));
                                break;
                            } else {
                                break;
                            }
                        }
                    case 5:
                        npc2.broadcastPacketX10(new S_NpcChatShouting(npc2, "$375 " + _gamblingNo + " $366 " + gamblingNpc.get_npc().getNameId()));
                        break;
                }
            }
        }
    }
}
