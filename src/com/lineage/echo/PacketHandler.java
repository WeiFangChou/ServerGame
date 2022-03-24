//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.lineage.echo;

import com.lineage.config.Config;
import com.lineage.echo.encryptions.PacketPrint;
import com.lineage.server.clientpackets.C_AddBookmark;
import com.lineage.server.clientpackets.C_AddBuddy;
import com.lineage.server.clientpackets.C_Amount;
import com.lineage.server.clientpackets.C_Attack;
import com.lineage.server.clientpackets.C_AttackBow;
import com.lineage.server.clientpackets.C_Attr;
import com.lineage.server.clientpackets.C_AuthLogin;
import com.lineage.server.clientpackets.C_AutoLogin;
import com.lineage.server.clientpackets.C_BanClan;
import com.lineage.server.clientpackets.C_BanParty;
import com.lineage.server.clientpackets.C_Board;
import com.lineage.server.clientpackets.C_BoardBack;
import com.lineage.server.clientpackets.C_BoardDelete;
import com.lineage.server.clientpackets.C_BoardRead;
import com.lineage.server.clientpackets.C_BoardWrite;
import com.lineage.server.clientpackets.C_Buddy;
import com.lineage.server.clientpackets.C_CallPlayer;
import com.lineage.server.clientpackets.C_ChangeHeading;
import com.lineage.server.clientpackets.C_ChangeWarTime;
import com.lineage.server.clientpackets.C_CharReset;
import com.lineage.server.clientpackets.C_CharcterConfig;
import com.lineage.server.clientpackets.C_Chat;
import com.lineage.server.clientpackets.C_ChatGlobal;
import com.lineage.server.clientpackets.C_ChatParty;
import com.lineage.server.clientpackets.C_ChatWhisper;
import com.lineage.server.clientpackets.C_CheckPK;
import com.lineage.server.clientpackets.C_Clan;
import com.lineage.server.clientpackets.C_ClanMatching;
import com.lineage.server.clientpackets.C_CommonClick;
import com.lineage.server.clientpackets.C_CreateChar;
import com.lineage.server.clientpackets.C_CreateClan;
import com.lineage.server.clientpackets.C_CreateParty;
import com.lineage.server.clientpackets.C_DelBuddy;
import com.lineage.server.clientpackets.C_DeleteBookmark;
import com.lineage.server.clientpackets.C_DeleteChar;
import com.lineage.server.clientpackets.C_DeleteInventoryItem;
import com.lineage.server.clientpackets.C_Deposit;
import com.lineage.server.clientpackets.C_Disconnect;
import com.lineage.server.clientpackets.C_Door;
import com.lineage.server.clientpackets.C_Drawal;
import com.lineage.server.clientpackets.C_DropItem;
import com.lineage.server.clientpackets.C_Emblem;
import com.lineage.server.clientpackets.C_EnterPortal;
import com.lineage.server.clientpackets.C_Exclude;
import com.lineage.server.clientpackets.C_ExitGhost;
import com.lineage.server.clientpackets.C_ExtraCommand;
import com.lineage.server.clientpackets.C_Fight;
import com.lineage.server.clientpackets.C_FishClick;
import com.lineage.server.clientpackets.C_FixWeaponList;
import com.lineage.server.clientpackets.C_GiveItem;
import com.lineage.server.clientpackets.C_ItemUSe;
import com.lineage.server.clientpackets.C_JoinClan;
import com.lineage.server.clientpackets.C_KeepALIVE;
import com.lineage.server.clientpackets.C_LeaveClan;
import com.lineage.server.clientpackets.C_LeaveParty;
import com.lineage.server.clientpackets.C_LoginToServer;
import com.lineage.server.clientpackets.C_LoginToServerOK;
import com.lineage.server.clientpackets.C_Mail;
import com.lineage.server.clientpackets.C_MoveChar;
import com.lineage.server.clientpackets.C_NPCAction;
import com.lineage.server.clientpackets.C_NPCTalk;
import com.lineage.server.clientpackets.C_NewAccess;
import com.lineage.server.clientpackets.C_NewCharSelect;
import com.lineage.server.clientpackets.C_Party;
import com.lineage.server.clientpackets.C_Password;
import com.lineage.server.clientpackets.C_PetMenu;
import com.lineage.server.clientpackets.C_PickUpItem;
import com.lineage.server.clientpackets.C_Pledge;
import com.lineage.server.clientpackets.C_Propose;
import com.lineage.server.clientpackets.C_Rank;
import com.lineage.server.clientpackets.C_Restart;
import com.lineage.server.clientpackets.C_Result;
import com.lineage.server.clientpackets.C_ReturnToLogin;
import com.lineage.server.clientpackets.C_SelectList;
import com.lineage.server.clientpackets.C_SelectTarget;
import com.lineage.server.clientpackets.C_ServerVersion;
import com.lineage.server.clientpackets.C_Ship;
import com.lineage.server.clientpackets.C_Shop;
import com.lineage.server.clientpackets.C_ShopList;
import com.lineage.server.clientpackets.C_SkillBuy;
import com.lineage.server.clientpackets.C_SkillBuyOK;
import com.lineage.server.clientpackets.C_TaxRate;
import com.lineage.server.clientpackets.C_Teleport;
import com.lineage.server.clientpackets.C_Title;
import com.lineage.server.clientpackets.C_Trade;
import com.lineage.server.clientpackets.C_TradeAddItem;
import com.lineage.server.clientpackets.C_TradeCancel;
import com.lineage.server.clientpackets.C_TradeOK;
import com.lineage.server.clientpackets.C_Unkonwn;
import com.lineage.server.clientpackets.C_UsePetItem;
import com.lineage.server.clientpackets.C_UseSkill;
import com.lineage.server.clientpackets.C_War;
import com.lineage.server.clientpackets.C_Who;
import com.lineage.server.clientpackets.C_Windows;
import com.lineage.server.clientpackets.ClientBasePacket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PacketHandler extends PacketHandlerExecutor {
    private static final Log _log = LogFactory.getLog(PacketHandler.class);
    private static final Map<Integer, ClientBasePacket> _opListClient = new HashMap();
    private ClientExecutor _client;

    public void handlePacket(byte[] decrypt)   {

        ClientBasePacket basePacket = null;
        if (decrypt != null) {
            if (decrypt.length > 0) {
                try {
                    int key = decrypt[0] & 255;
                    basePacket = _opListClient.get(key);
                    if (Config.DEBUG) {
                        if (basePacket != null) {
                            _log.info("客戶端: " + basePacket.getType() + "\nOP ID: " + key + " length:" + decrypt.length + "\nInfo:\n" + PacketPrint.get().printData(decrypt, decrypt.length));

                        }
                    }

                    if (basePacket == null) {
                        _log.info("\nClient: " + key + "\n" + PacketPrint.get().printData(decrypt, decrypt.length) + this.getNow_YMDHMS());
                    }else{
                        basePacket.start(decrypt, this._client);
                    }
                } catch (Exception var8) {
                    if (Config.DEBUG) {
                        String name = "Not Login Pc";
                        if (this._client.getActiveChar() != null) {
                            name = this._client.getActiveChar().getName();
                        }

                        _log.error("OP ID: " + (decrypt[0] & 255) + " Pc Name: " + name + "\n" + basePacket.getType() + "\n" + PacketPrint.get().printData(decrypt, decrypt.length), var8);
                    }
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                } finally {
                    //basePacket = null;
                }

            }
        }
    }

    private String getNow_YMDHMS() {
        String nowDate = (new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")).format(new Date());
        return nowDate;
    }

    public PacketHandler(ClientExecutor client) {
        this._client = client;
    }

    public static void load() {
        put(236, new C_CharReset());
        put(104, new C_Disconnect());
        put(101, new C_Exclude());
        put(129, new C_CharcterConfig());
        put(199, new C_Door());
        put(96, new C_Title());
        put(12, new C_BoardDelete());
        put(225, new C_Pledge());
        put(65, new C_ChangeHeading());
        put(37, new C_NPCAction());
        put(115, new C_UseSkill());
        put(107, new C_Emblem());
        put(167, new C_TradeCancel());
        put(150, new C_ChangeWarTime());
        put(134, new C_AddBookmark());
        put(154, new C_CreateClan());
        put(127, new C_ServerVersion());
        put(185, new C_Propose());
        put(221, new C_BoardBack());
        put(16, new C_Shop());
        put(59, new C_BoardRead());
        put(103, new C_Trade());
        put(10, new C_DeleteChar());
        put(61, new C_Attr());
        put(57, new C_AuthLogin());
        put(40, new C_Result());
        put(35, new C_Deposit());
        put(192, new C_Drawal());
        put(75, new C_LoginToServerOK());
        put(173, new C_SkillBuy());
        put(207, new C_SkillBuyOK());
        put(241, new C_TradeAddItem());
        put(99, new C_AddBuddy());
        put(56, new C_ReturnToLogin());
        put(190, new C_Chat());
        put(110, new C_TradeOK());
        put(137, new C_CheckPK());
        put(200, new C_TaxRate());
        put(237, new C_NewCharSelect());
        put(60, new C_Buddy());
        put(54, new C_DropItem());
        put(204, new C_LeaveParty());
        put(68, new C_Attack());
        put(247, new C_AttackBow());
        put(222, new C_BanClan());
        put(73, new C_Board());
        put(209, new C_DeleteInventoryItem());
        put(122, new C_ChatWhisper());
        put(42, new C_Party());
        put(188, new C_PickUpItem());
        put(49, new C_Who());
        put(244, new C_GiveItem());
        put(95, new C_MoveChar());
        put(223, new C_DeleteBookmark());
        put(71, new C_Restart());
        put(121, new C_LeaveClan());
        put(58, new C_NPCTalk());
        put(70, new C_BanParty());
        put(211, new C_DelBuddy());
        put(235, new C_War());
        put(131, new C_LoginToServer());
        put(193, new C_ShopList());
        put(62, new C_ChatGlobal());
        put(30, new C_JoinClan());
        put(53, new C_CommonClick());
        put(253, new C_CreateChar());
        put(157, new C_ExtraCommand());
        put(14, new C_BoardWrite());
        put(44, new C_ItemUSe());
        put(166, new C_CreateParty());
        put(249, new C_EnterPortal());
        put(109, new C_Amount());
        put(106, new C_FixWeaponList());
        put(238, new C_SelectList());
        put(210, new C_ExitGhost());
        put(144, new C_CallPlayer());
        put(155, new C_SelectTarget());
        put(217, new C_PetMenu());
        put(213, new C_UsePetItem());
        put(88, new C_Rank());
        put(113, new C_ChatParty());
        put(47, new C_Fight());
        put(22, new C_Mail());
        put(117, new C_Ship());
        put(246, new C_Clan());
        put(5, new C_NewAccess());
        put(239, new C_Teleport());
        put(226, new C_Teleport());
        put(81, new C_Password());
        put(182, new C_KeepALIVE());
        put(41, new C_Windows());
        put(162, new C_AutoLogin());
        put(26, new C_FishClick());
        put(-2, new C_Unkonwn());
        put(228, new C_ClanMatching());
    }

    public static void put(Integer key, ClientBasePacket value) {
        if (_opListClient.get(key) == null) {
            _opListClient.put(key, value);
        } else if (!key.equals(-1)) {
            _log.error("重複標記的OPID: " + key + " " + value.getType());
        }

    }
}
