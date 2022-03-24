package com.eric.gui;

import com.lineage.commons.system.LanSecurityManager;
import com.lineage.config.Config;
import com.lineage.config.ConfigAlt;
import com.lineage.config.ConfigRate;
import com.lineage.echo.ClientExecutor;
import com.lineage.echo.OpcodesClient;
import com.lineage.server.EchoServerTimer;
import com.lineage.server.Shutdown;
import com.lineage.server.WriteLogTxt;
import com.lineage.server.datatables.ArmorSetTable;
import com.lineage.server.datatables.DropMapTable;
import com.lineage.server.datatables.DropMobTable;
import com.lineage.server.datatables.DropTable;
import com.lineage.server.datatables.ExpTable;
import com.lineage.server.datatables.ItemBoxTable;
import com.lineage.server.datatables.ItemBsgTable;
import com.lineage.server.datatables.ItemMsgTable;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.datatables.Item_Box_Table;
import com.lineage.server.datatables.Item_Dolls_Table;
import com.lineage.server.datatables.Item_Make_Table;
import com.lineage.server.datatables.Item_Poly_Table;
import com.lineage.server.datatables.NPCTalkDataTable;
import com.lineage.server.datatables.NpcTable;
import com.lineage.server.datatables.ShopCnTable;
import com.lineage.server.datatables.ShopTable;
import com.lineage.server.datatables.SkillsTable;
import com.lineage.server.datatables.TrapsSpawn;
import com.lineage.server.datatables.WeaponSkillTable;
import com.lineage.server.datatables.lock.AuctionBoardReading;
import com.lineage.server.datatables.lock.CastleReading;
import com.lineage.server.datatables.lock.CharSkillReading;
import com.lineage.server.datatables.lock.DwarfReading;
import com.lineage.server.datatables.lock.EzpayReading3;
import com.lineage.server.datatables.lock.IpReading;
import com.lineage.server.datatables.sql.CharacterTable;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Inventory;
import com.lineage.server.model.L1PolyMorph;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.L1War;
import com.lineage.server.model.item.L1ItemId;
import com.lineage.server.model.skill.L1BuffUtil;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.model.skill.L1SkillUse;
import com.lineage.server.serverpackets.OpcodesServer;
import com.lineage.server.serverpackets.S_AddSkill;
import com.lineage.server.serverpackets.S_BlueMessage;
import com.lineage.server.serverpackets.S_Disconnect;
import com.lineage.server.serverpackets.S_InvList;
import com.lineage.server.serverpackets.S_Message_YN;
import com.lineage.server.serverpackets.S_OwnCharStatus;
import com.lineage.server.serverpackets.S_SeerverMessage;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillBrave;
import com.lineage.server.serverpackets.S_SkillHaste;
import com.lineage.server.serverpackets.S_SkillSound;
import com.lineage.server.serverpackets.S_SystemMessage;
import com.lineage.server.templates.L1AuctionBoardTmp;
import com.lineage.server.templates.L1Castle;
import com.lineage.server.templates.L1Skills;
import com.lineage.server.timecontroller.server.ServerWarExecutor;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.Random;
import com.lineage.server.world.World;
import com.lineage.server.world.WorldWar;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.LayoutStyle;
import javax.swing.table.DefaultTableModel;

public class J_Main extends JFrame implements ActionListener {
    private static J_Main instance = null;
    private static final long serialVersionUID = 1;
    private JComboBox CB_Channel;
    private JComboBox CB_Item;
    private DefaultTableModel DTM = new DefaultTableModel() {
        /* class com.eric.gui.J_Main.C00001 */
        private static final long serialVersionUID = 1;

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }
    };
    private DefaultTableModel DTM_Item = new DefaultTableModel() {
        /* class com.eric.gui.J_Main.C00012 */
        private static final long serialVersionUID = 1;

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }
    };
    private JDialog D_Item;
    private JFrame F_Player;
    private JLabel L_Image;

    private JMenuItem MI_ACCOUNT;
    private JMenuItem MI_ADENA;
    private JMenuItem MI_ATTR_ENCHANT_CHANCE;
    private JMenuItem MI_Adena;
    private JMenuItem MI_AllBuff;
    private JMenuItem MI_AllGiveItem;
    private JMenuItem MI_AllItem;
    private JMenuItem MI_AllPoly;
    private JMenuItem MI_AllRess;
    private JMenuItem MI_Angel;
    private JMenuItem MI_BanIP;
    private JMenuItem MI_CHANCE_ARMOR;
    private JMenuItem MI_CHANCE_WEAPON;
    private JMenuItem MI_Close;
    private JMenuItem MI_Close_F;
    private JMenuItem MI_DeadPc;
    private JMenuItem MI_DeleteCharacter;
    private JMenuItem MI_Drop;
    private JMenuItem MI_DwarItem;
    private JMenuItem MI_Exp;
    private JMenuItem MI_EzpayItem;
    private JMenuItem MI_GiveItem;
    private JMenuItem MI_House;
    private JMenuItem MI_Item2;
    private JMenuItem MI_Karma;
    private JMenuItem MI_Kill;
    private JMenuItem MI_LA;
    private JMenuItem MI_PC;
    private JMenuItem MI_Password;
    private JMenuItem MI_RDrop;
    private JMenuItem MI_RDropMap;
    private JMenuItem MI_RItem;
    private JMenuItem MI_RNpc;
    private JMenuItem MI_RShop;
    private JMenuItem MI_RShopCn;
    private JMenuItem MI_Save;
    private JMenuItem MI_Save_A;
    private JMenuItem MI_SetClose;
    private JMenuItem MI_Shop_Purchas;
    private JMenuItem MI_Shop_Sell;
    private JMenuItem MI_ShowPlayer;
    private JMenuItem MI_Skill;
    private JMenuItem MI_Table;
    private JMenuItem MI_Tele;
    private JMenuItem MI_Teleport;
    private JMenuItem MI_War;
    private JMenuItem MI_Warcastle;
    private JMenuItem MI_Weigh;
    private JMenuItem MI_Weigh_PET;
    private JMenuItem MI_Whisper;
    private JMenuItem MI_XP_PET;
    private JPopupMenu PM_Player;
    private JTextArea TA_AllChat;
    private JTextArea TA_Clan;
    private JTextArea TA_ClanAllance;
    private JTextArea TA_Consol;
    private JTextArea TA_Normal;
    private JTextArea TA_Private;
    private JTextArea TA_Team;
    private JTextArea TA_World;
    private JTextField TF_Ac;
    private JTextField TF_AccessLevel;
    private JTextField TF_Account;
    private JTextField TF_Cha;
    private JTextField TF_Clan;
    private JTextField TF_Con;
    private JTextField TF_Dex;
    private JTextField TF_Exp;
    private JTextField TF_Hp;
    private JTextField TF_Int;
    private JTextField TF_Level;
    private JTextField TF_Map;
    private JTextField TF_Mp;
    private JTextField TF_Msg;
    private JTextField TF_Name;
    private JTextField TF_Sex;
    private JTextField TF_Str;
    private JTextField TF_Target;
    private JTextField TF_Title;
    private JTextField TF_Wis;
    private JTextField TF_X;
    private JTextField TF_Y;

    private JTable T_Item;
    private JTable T_Player;
    ImageIcon img = new ImageIcon("img/icon.png");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
    private int select = 0;

    public J_Main() {
        iniPlayerTable();
        initComponents();
        this.TA_Consol.setForeground(Color.WHITE);
        this.TA_AllChat.setForeground(Color.WHITE);
        this.TA_Clan.setBackground(Color.BLACK);
        this.TA_Normal.setBackground(Color.BLACK);
        this.TA_Private.setBackground(Color.BLACK);
        this.TA_Team.setBackground(Color.BLACK);
        this.TA_World.setBackground(Color.BLACK);
        this.TA_Consol.setBackground(Color.BLACK);
        this.TA_AllChat.setBackground(Color.BLACK);
        this.TA_Clan.setForeground(Color.WHITE);
        this.TA_Normal.setForeground(Color.WHITE);
        this.TA_Private.setForeground(Color.WHITE);
        this.TA_Team.setForeground(Color.WHITE);
        this.TA_World.setForeground(Color.WHITE);
        setIconImage(this.img.getImage());
        iniAction();
        this.T_Item.setSize(300, 400);
        this.D_Item.pack();
        this.DTM_Item.setColumnIdentifiers(new String[]{"物品名稱", "物品數量", "物品ID", "物品强化值"});
    }

    private void iniAction() {
        this.MI_Password.addActionListener(this);
        this.MI_Teleport.addActionListener(this);
        this.MI_Tele.addActionListener(this);
        this.MI_GiveItem.addActionListener(this);
        this.MI_DwarItem.addActionListener(this);
        this.MI_EzpayItem.addActionListener(this);
        this.MI_Kill.addActionListener(this);
        this.MI_BanIP.addActionListener(this);
        this.MI_ACCOUNT.addActionListener(this);
        this.MI_DeleteCharacter.addActionListener(this);
        this.MI_ShowPlayer.addActionListener(this);
        this.MI_Whisper.addActionListener(this);
        this.MI_Save.addActionListener(this);
        this.MI_Close.addActionListener(this);
        this.MI_Angel.addActionListener(this);
        this.MI_SetClose.addActionListener(this);
        this.MI_AllBuff.addActionListener(this);
        this.MI_AllRess.addActionListener(this);
        this.MI_AllPoly.addActionListener(this);
        this.MI_AllItem.addActionListener(this);
        this.MI_AllGiveItem.addActionListener(this);
        this.MI_PC.addActionListener(this);
        this.MI_Skill.addActionListener(this);
        this.MI_ADENA.addActionListener(this);
        this.MI_Save_A.addActionListener(this);
        this.MI_Close_F.addActionListener(this);
        this.MI_Exp.addActionListener(this);
        this.MI_XP_PET.addActionListener(this);
        this.MI_CHANCE_WEAPON.addActionListener(this);
        this.MI_CHANCE_ARMOR.addActionListener(this);
        this.MI_ATTR_ENCHANT_CHANCE.addActionListener(this);
        this.MI_Item2.addActionListener(this);
        this.MI_LA.addActionListener(this);
        this.MI_Drop.addActionListener(this);
        this.MI_Adena.addActionListener(this);
        this.MI_Karma.addActionListener(this);
        this.MI_Weigh.addActionListener(this);
        this.MI_Weigh_PET.addActionListener(this);
        this.MI_Shop_Sell.addActionListener(this);
        this.MI_Shop_Purchas.addActionListener(this);
        this.MI_DeadPc.addActionListener(this);
        this.MI_Warcastle.addActionListener(this);
        this.MI_War.addActionListener(this);
        this.MI_House.addActionListener(this);
        this.MI_RDrop.addActionListener(this);
        this.MI_RShop.addActionListener(this);
        this.MI_RShopCn.addActionListener(this);
        this.MI_RItem.addActionListener(this);
        this.MI_RNpc.addActionListener(this);
        this.MI_RDropMap.addActionListener(this);
        this.MI_Table.addActionListener(this);
    }

    public static J_Main getInstance() {
        if (instance == null) {
            instance = new J_Main();
        }
        return instance;
    }

    public void addWorldChat(String from, String text) {
        AllChat(String.valueOf(this.sdf.format(Calendar.getInstance().getTime())) + "【" + from + "】:" + text + "\r\n");
        this.TA_World.append(String.valueOf(from) + " : " + text + "\r\n");
        this.TA_World.setCaretPosition(this.TA_World.getDocument().getLength());
    }

    public void addClanChat(String from, String text) {
        AllChat(String.valueOf(this.sdf.format(Calendar.getInstance().getTime())) + "「" + from + "」:" + text + "\r\n");
        this.TA_Clan.append(String.valueOf(from) + " : " + text + "\r\n");
        this.TA_Clan.setCaretPosition(this.TA_Clan.getDocument().getLength());
    }

    public void addNormalChat(String from, String text) {
        AllChat(String.valueOf(this.sdf.format(Calendar.getInstance().getTime())) + "{" + from + "}:" + text + "\r\n");
        this.TA_Normal.append(String.valueOf(from) + " : " + text + "\r\n");
        this.TA_Normal.setCaretPosition(this.TA_Normal.getDocument().getLength());
        this.TA_Consol.append(String.valueOf(from) + " : " + text + "\r\n");
        this.TA_Consol.setCaretPosition(this.TA_Consol.getDocument().getLength());
    }

    public void addTeamChat(String from, String text) {
        AllChat(String.valueOf(this.sdf.format(Calendar.getInstance().getTime())) + "[" + from + "]:" + text + "\r\n");
        this.TA_Team.append(String.valueOf(from) + " : " + text + "\r\n");
        this.TA_Team.setCaretPosition(this.TA_Team.getDocument().getLength());
    }

    public final void addChat(String from, String text) {
        AllChat(String.valueOf(this.sdf.format(Calendar.getInstance().getTime())) + "「" + from + "」:" + text + "\r\n");
        this.TA_ClanAllance.append(String.valueOf(from) + " : " + text + "\r\n");
        this.TA_ClanAllance.setCaretPosition(this.TA_ClanAllance.getDocument().getLength());
    }

    public void addConsol(String text) {
        this.TA_Consol.append(String.valueOf(text) + "\r\n");
        this.TA_Consol.setCaretPosition(this.TA_Consol.getDocument().getLength());
    }

    public void addConsolPost(String text) {
        this.TA_Consol.append(String.valueOf(text) + "\r\n");
        this.TA_Consol.setCaretPosition(this.TA_Consol.getDocument().getLength());
    }

    public void addConsolNoLR(String text) {
        this.TA_Consol.append(text);
        this.TA_Consol.setCaretPosition(this.TA_Consol.getDocument().getLength());
    }

    public void AllChat(String text) {
        this.TA_AllChat.append(String.valueOf(text) + "\r\n");
        this.TA_AllChat.setCaretPosition(this.TA_AllChat.getDocument().getLength());
    }

    public void addPrivateChat(String from, String to, String text) {
        AllChat(String.valueOf(this.sdf.format(Calendar.getInstance().getTime())) + "(" + from + "->" + to + "):" + text + "\r\n");
        this.TA_Private.append(String.valueOf(from) + "->" + to + " : " + text + "\r\n");
        this.TA_Private.setCaretPosition(this.TA_Private.getDocument().getLength());
    }

    public void addItemTable(String itemname, long l, long id, int i) {
        this.DTM_Item.addRow(new Object[]{itemname, Long.valueOf(l), Long.valueOf(id), Integer.valueOf(i)});
    }

    public void iniTable() {
        for (int cont = this.DTM_Item.getRowCount(); cont > 1; cont--) {
            this.DTM_Item.removeRow(cont - 1);
        }
    }

    public void addPlayerTable(String account, String name, StringBuilder IP) {
        this.DTM.addRow(new Object[]{account, name, IP});
    }

    private int findPlayer(String name) {
        for (int j = 0; j < this.DTM.getRowCount(); j++) {
            try {
                if (name.equals(this.DTM.getValueAt(j, 1).toString())) {
                    return j;
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
                return -1;
            }
        }
        return -1;
    }

    public void delPlayerTable(String name) {
        int findNum = findPlayer(name);
        if (findNum != -1) {
            this.DTM.removeRow(findNum);
        }
    }

    private void iniPlayerTable() {
        this.DTM.setColumnIdentifiers(new String[]{"帳號", "角色名稱", "IP"});
    }

    private void initComponents() {
        this.F_Player = new JFrame();
        JLabel l_Name = new JLabel();
        JLabel l_Title = new JLabel();
        JLabel l_Account = new JLabel();
        JLabel l_Leavl = new JLabel();
        JLabel l_AccessLevel = new JLabel();
        JLabel l_Exp = new JLabel();
        JLabel l_Hp = new JLabel();
        JLabel l_Mp = new JLabel();
        JLabel l_Int = new JLabel();
        JLabel l_Str = new JLabel();
        JLabel l_Con = new JLabel();
        JLabel l_Dex = new JLabel();
        JLabel l_Wis = new JLabel();
        JLabel l_Cha = new JLabel();
        JPanel jPanel1 = new JPanel();
        this.L_Image = new JLabel();
        JLabel l_Clan = new JLabel();
        JLabel l_AccessLevel7 = new JLabel();
        JLabel l_Mp1 = new JLabel();
        JLabel l_Map = new JLabel();
        JLabel l_X = new JLabel();
        JLabel l_Y = new JLabel();
        this.TF_Account = new JTextField();
        this.TF_Name = new JTextField();
        this.TF_Title = new JTextField();
        this.TF_Level = new JTextField();
        this.TF_AccessLevel = new JTextField();
        this.TF_Clan = new JTextField();
        this.TF_Exp = new JTextField();
        this.TF_Hp = new JTextField();
        this.TF_Mp = new JTextField();
        this.TF_Sex = new JTextField();
        this.TF_Str = new JTextField();
        this.TF_Con = new JTextField();
        this.TF_Dex = new JTextField();
        this.TF_Wis = new JTextField();
        this.TF_Int = new JTextField();
        this.TF_Cha = new JTextField();
        this.TF_Ac = new JTextField();
        this.TF_Map = new JTextField();
        this.TF_X = new JTextField();
        this.TF_Y = new JTextField();
        JButton b_Item = new JButton();
        this.CB_Item = new JComboBox();
        this.PM_Player = new JPopupMenu();
        this.MI_Kill = new JMenuItem();
        this.MI_GiveItem = new JMenuItem();
        this.MI_DwarItem = new JMenuItem();
        this.MI_EzpayItem = new JMenuItem();
        this.MI_Teleport = new JMenuItem();
        this.MI_Tele = new JMenuItem();
        this.MI_Password = new JMenuItem();
        this.MI_ACCOUNT = new JMenuItem();
        this.MI_BanIP = new JMenuItem();
        this.MI_PC = new JMenuItem();
        this.MI_Skill = new JMenuItem();
        this.MI_ADENA = new JMenuItem();
        this.MI_Item2 = new JMenuItem();
        JSeparator jSeparator1 = new JSeparator();
        this.MI_ShowPlayer = new JMenuItem();
        this.MI_DeleteCharacter = new JMenuItem();
        JSeparator jSeparator2 = new JSeparator();
        this.MI_Whisper = new JMenuItem();
        JLabel jLabel1 = new JLabel();
        this.D_Item = new JDialog();
        JScrollPane jScrollPane1 = new JScrollPane();
        this.T_Item = new JTable(this.DTM_Item);
        JSplitPane SP_Split = new JSplitPane();
        /* renamed from: TP */
        JTabbedPane f2TP = new JTabbedPane();
        JScrollPane SP_Consol = new JScrollPane();
        this.TA_Consol = new JTextArea();
        JScrollPane SP_AllChat = new JScrollPane();
        this.TA_AllChat = new JTextArea();
        JScrollPane SP_World = new JScrollPane();
        this.TA_World = new JTextArea();
        JScrollPane SP_Normal = new JScrollPane();
        this.TA_Normal = new JTextArea();
        JScrollPane SP_ = new JScrollPane();
        this.TA_Private = new JTextArea();
        JScrollPane SP_Clan = new JScrollPane();
        this.TA_Clan = new JTextArea();
        JScrollPane SP_ClanAllance = new JScrollPane();
        this.TA_ClanAllance = new JTextArea();
        JScrollPane SP_Team = new JScrollPane();
        this.TA_Team = new JTextArea();
        JScrollPane SP_player = new JScrollPane();
        this.T_Player = new JTable(this.DTM);
        JPanel jPanel2 = new JPanel();
        this.CB_Channel = new JComboBox();
        this.TF_Target = new JTextField();
        JButton b_Submit = new JButton();
        this.TF_Msg = new JTextField();
        /* renamed from: MB */
        JMenuBar f1MB = new JMenuBar();
        JMenu m_File = new JMenu();
        this.MI_Save = new JMenuItem();
        this.MI_Save_A = new JMenuItem();
        this.MI_Close_F = new JMenuItem();
        JSeparator jSeparator3 = new JSeparator();
        this.MI_SetClose = new JMenuItem();
        this.MI_Close = new JMenuItem();
        this.MI_Angel = new JMenuItem();
        JMenu m_Edit = new JMenu();
        JMenu m_Special = new JMenu();
        this.MI_AllBuff = new JMenuItem();
        this.MI_AllRess = new JMenuItem();
        this.MI_AllPoly = new JMenuItem();
        this.MI_AllItem = new JMenuItem();
        this.MI_AllGiveItem = new JMenuItem();
        this.MI_Exp = new JMenuItem();
        this.MI_Drop = new JMenuItem();
        this.MI_Adena = new JMenuItem();
        this.MI_LA = new JMenuItem();
        this.MI_ATTR_ENCHANT_CHANCE = new JMenuItem();
        this.MI_CHANCE_ARMOR = new JMenuItem();
        this.MI_CHANCE_WEAPON = new JMenuItem();
        this.MI_XP_PET = new JMenuItem();
        this.MI_Karma = new JMenuItem();
        this.MI_Weigh = new JMenuItem();
        this.MI_Weigh_PET = new JMenuItem();
        this.MI_Shop_Sell = new JMenuItem();
        this.MI_Shop_Purchas = new JMenuItem();
        this.MI_DeadPc = new JMenuItem();
        this.MI_Warcastle = new JMenuItem();
        this.MI_War = new JMenuItem();
        this.MI_House = new JMenuItem();
        JMenu m_Reset = new JMenu();
        this.MI_RDrop = new JMenuItem();
        this.MI_RShop = new JMenuItem();
        this.MI_RShopCn = new JMenuItem();
        this.MI_RItem = new JMenuItem();
        this.MI_RNpc = new JMenuItem();
        this.MI_RDropMap = new JMenuItem();
        this.MI_Table = new JMenuItem();
        l_Name.setText("名字:");
        l_Title.setText("稱號:");
        l_Account.setText("帳號:");
        l_Leavl.setText("等級:");
        l_AccessLevel.setText("權限:");
        l_Exp.setText("經驗值:");
        l_Hp.setText("體力:");
        l_Mp.setText("魔力:");
        l_Int.setText("智力:");
        l_Str.setText("力量:");
        l_Con.setText("體質:");
        l_Dex.setText("敏捷:");
        l_Wis.setText("精神:");
        l_Cha.setText("魅力:");
        GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addComponent(this.L_Image, -1, (int) L1SkillId.FINAL_BURN, 32767).addContainerGap()));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addComponent(this.L_Image, -1, (int) OpcodesServer.S_OPCODE_INVLIST, 32767).addContainerGap()));
        l_Clan.setText("血盟:");
        l_AccessLevel7.setText("防禦力:");
        l_Mp1.setText("性別:");
        l_Map.setText("Map:");
        l_X.setText("X:");
        l_Y.setText("Y:");
        this.TF_Account.setEditable(false);
        this.TF_Name.setEditable(false);
        this.TF_Title.setEditable(false);
        this.TF_Level.setEditable(false);
        this.TF_AccessLevel.setEditable(false);
        this.TF_Clan.setEditable(false);
        this.TF_Exp.setEditable(false);
        this.TF_Hp.setEditable(false);
        this.TF_Mp.setEditable(false);
        this.TF_Sex.setEditable(false);
        this.TF_Str.setEditable(false);
        this.TF_Con.setEditable(false);
        this.TF_Dex.setEditable(false);
        this.TF_Wis.setEditable(false);
        this.TF_Int.setEditable(false);
        this.TF_Cha.setEditable(false);
        this.TF_Ac.setEditable(false);
        this.TF_Map.setEditable(false);
        this.TF_X.setEditable(false);
        this.TF_Y.setEditable(false);
        b_Item.setText("物品欄顯示");
        b_Item.addActionListener(new ActionListener() {
            /* class com.eric.gui.J_Main.C00023 */

            public void actionPerformed(ActionEvent evt) {
                J_Main.this.B_ItemActionPerformed(evt);
            }
        });
        this.CB_Item.setModel(new DefaultComboBoxModel(new String[]{"0,身上物品", "1,倉庫", "2,血盟倉庫", "3,妖森倉庫", "4,裝備物品"}));
        GroupLayout F_PlayerLayout = new GroupLayout(this.F_Player.getContentPane());
        this.F_Player.getContentPane().setLayout(F_PlayerLayout);
        F_PlayerLayout.setHorizontalGroup(F_PlayerLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(F_PlayerLayout.createSequentialGroup().addComponent(jPanel1, -2, -1, -2).addGap(18, 18, 18).addGroup(F_PlayerLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(F_PlayerLayout.createSequentialGroup().addComponent(l_Account).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.TF_Account, -2, (int) L1SkillId.FINAL_BURN, -2)).addGroup(F_PlayerLayout.createSequentialGroup().addGroup(F_PlayerLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false).addComponent(l_Name).addComponent(l_Title).addComponent(l_Leavl).addComponent(l_AccessLevel).addComponent(l_Clan).addComponent(l_Exp, GroupLayout.Alignment.TRAILING, -1, 27, 32767).addComponent(l_Hp, GroupLayout.Alignment.TRAILING).addComponent(l_Mp, GroupLayout.Alignment.TRAILING).addComponent(l_Mp1, GroupLayout.Alignment.TRAILING)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(F_PlayerLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false).addComponent(this.TF_Mp, -1, (int) L1SkillId.FINAL_BURN, 32767).addComponent(this.TF_Sex, -1, (int) L1SkillId.FINAL_BURN, 32767).addComponent(this.TF_Hp, -1, (int) L1SkillId.FINAL_BURN, 32767).addComponent(this.TF_Exp, -1, (int) L1SkillId.FINAL_BURN, 32767).addComponent(this.TF_Clan, -1, (int) L1SkillId.FINAL_BURN, 32767).addComponent(this.TF_AccessLevel, -1, (int) L1SkillId.FINAL_BURN, 32767).addComponent(this.TF_Level, -1, (int) L1SkillId.FINAL_BURN, 32767).addComponent(this.TF_Title, -1, (int) L1SkillId.FINAL_BURN, 32767).addComponent(this.TF_Name, -1, (int) L1SkillId.FINAL_BURN, 32767).addComponent(this.CB_Item, 0, -1, 32767)))).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(F_PlayerLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(F_PlayerLayout.createParallelGroup(GroupLayout.Alignment.TRAILING).addGroup(F_PlayerLayout.createSequentialGroup().addGroup(F_PlayerLayout.createParallelGroup(GroupLayout.Alignment.TRAILING).addComponent(l_Int).addComponent(l_Wis).addComponent(l_Dex).addComponent(l_Cha).addComponent(l_AccessLevel7).addComponent(l_Con).addComponent(l_Str).addComponent(l_Map).addComponent(l_X)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(F_PlayerLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.TF_Str, -2, (int) L1SkillId.FINAL_BURN, -2).addComponent(this.TF_Con, -2, (int) L1SkillId.FINAL_BURN, -2).addComponent(this.TF_Dex, -2, (int) L1SkillId.FINAL_BURN, -2).addComponent(this.TF_Wis, -2, (int) L1SkillId.FINAL_BURN, -2).addComponent(this.TF_Int, -2, (int) L1SkillId.FINAL_BURN, -2).addComponent(this.TF_Cha, -2, (int) L1SkillId.FINAL_BURN, -2).addComponent(this.TF_Ac, -2, (int) L1SkillId.FINAL_BURN, -2).addComponent(this.TF_Map, -2, (int) L1SkillId.FINAL_BURN, -2).addComponent(this.TF_X, -2, (int) L1SkillId.FINAL_BURN, -2))).addGroup(F_PlayerLayout.createSequentialGroup().addComponent(l_Y).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.TF_Y, -2, (int) L1SkillId.FINAL_BURN, -2))).addComponent(b_Item)).addContainerGap(52, 32767)));
        F_PlayerLayout.setVerticalGroup(F_PlayerLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(F_PlayerLayout.createSequentialGroup().addGroup(F_PlayerLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(F_PlayerLayout.createSequentialGroup().addContainerGap().addGroup(F_PlayerLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(l_Account).addComponent(this.TF_Account, -2, 18, -2)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(F_PlayerLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(l_Name).addComponent(this.TF_Name, -2, 18, -2)).addGap(5, 5, 5).addGroup(F_PlayerLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(l_Title).addComponent(this.TF_Title, -2, 18, -2)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(F_PlayerLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(l_Leavl).addComponent(this.TF_Level, -2, 18, -2)).addGap(5, 5, 5).addGroup(F_PlayerLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(l_AccessLevel).addComponent(this.TF_AccessLevel, -2, 18, -2)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(F_PlayerLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(l_Clan).addComponent(this.TF_Clan, -2, 18, -2)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(F_PlayerLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(l_Exp).addComponent(this.TF_Exp, -2, 18, -2)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(F_PlayerLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(l_Hp).addComponent(this.TF_Hp, -2, 18, -2)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(F_PlayerLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(l_Mp).addComponent(this.TF_Mp, -2, 18, -2)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(F_PlayerLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(l_Mp1).addComponent(this.TF_Sex, -2, 18, -2).addComponent(l_Y).addComponent(this.TF_Y, -2, 18, -2))).addGroup(F_PlayerLayout.createSequentialGroup().addGap(26, 26, 26).addComponent(jPanel1, -2, -1, -2)).addGroup(F_PlayerLayout.createSequentialGroup().addContainerGap().addGroup(F_PlayerLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(l_Str).addComponent(this.TF_Str, -2, 18, -2)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(F_PlayerLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(l_Con).addComponent(this.TF_Con, -2, 18, -2)).addGap(5, 5, 5).addGroup(F_PlayerLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(l_Dex).addComponent(this.TF_Dex, -2, 18, -2)).addGap(5, 5, 5).addGroup(F_PlayerLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(l_Wis).addComponent(this.TF_Wis, -2, 18, -2)).addGap(5, 5, 5).addGroup(F_PlayerLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(l_Int).addComponent(this.TF_Int, -2, 18, -2)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(F_PlayerLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(l_Cha).addComponent(this.TF_Cha, -2, 18, -2)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(F_PlayerLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(l_AccessLevel7).addComponent(this.TF_Ac, -2, 18, -2)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(F_PlayerLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(l_Map).addComponent(this.TF_Map, -2, 18, -2)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(F_PlayerLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(l_X).addComponent(this.TF_X, -2, 18, -2)))).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(F_PlayerLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.CB_Item, -2, -1, -2).addComponent(b_Item)).addContainerGap(27, 32767)));
        this.MI_Kill.setMnemonic('K');
        this.MI_Kill.setText("強制踢除(K)");
        this.PM_Player.add(this.MI_Kill);
        this.MI_PC.setMnemonic('[');
        this.MI_PC.setText("屬性變更([)");
        this.PM_Player.add(this.MI_PC);
        this.MI_Skill.setMnemonic('Y');
        this.MI_Skill.setText("給予GM職業所有技能(Y)");
        this.PM_Player.add(this.MI_Skill);
        this.MI_ADENA.setMnemonic('X');
        this.MI_ADENA.setText("給予金幣(X)");
        this.PM_Player.add(this.MI_ADENA);
        this.PM_Player.add(jSeparator1);
        this.MI_Item2.setMnemonic('2');
        this.MI_Item2.setText("刪除物品(2)");
        this.PM_Player.add(this.MI_Item2);
        this.PM_Player.add(jSeparator1);
        this.MI_GiveItem.setMnemonic('V');
        this.MI_GiveItem.setText("發送道具(V)");
        this.PM_Player.add(this.MI_GiveItem);
        this.MI_DwarItem.setMnemonic('Q');
        this.MI_DwarItem.setText("發送道具到倉庫(Q)");
        this.PM_Player.add(this.MI_DwarItem);
        this.MI_EzpayItem.setMnemonic('J');
        this.MI_EzpayItem.setText("發送道具到即時獎勵(J)");
        this.PM_Player.add(this.MI_EzpayItem);
        this.MI_Teleport.setMnemonic('T');
        this.MI_Teleport.setText("解卡點(T)");
        this.PM_Player.add(this.MI_Teleport);
        this.MI_Password.setMnemonic('P');
        this.MI_Password.setText("改密碼(P)");
        this.PM_Player.add(this.MI_Password);
        this.MI_ACCOUNT.setMnemonic('A');
        this.MI_ACCOUNT.setText("封鎖帳號(A)");
        this.PM_Player.add(this.MI_ACCOUNT);
        this.MI_BanIP.setMnemonic('B');
        this.MI_BanIP.setText("封鎖IP(B)");
        this.PM_Player.add(this.MI_BanIP);
        this.PM_Player.add(jSeparator1);
        this.MI_Tele.setMnemonic('C');
        this.MI_Tele.setText("解錯位(C)");
        this.PM_Player.add(this.MI_Tele);
        this.MI_ShowPlayer.setMnemonic('P');
        this.MI_ShowPlayer.setText("玩家資料(P)");
        this.PM_Player.add(this.MI_ShowPlayer);
        this.PM_Player.add(jSeparator2);
        this.MI_DeleteCharacter.setMnemonic('R');
        this.MI_DeleteCharacter.setText("刪除角色(R)");
        this.PM_Player.add(this.MI_DeleteCharacter);
        this.PM_Player.add(jSeparator3);
        this.MI_Whisper.setMnemonic('W');
        this.MI_Whisper.setText("密語(W)");
        this.PM_Player.add(this.MI_Whisper);
        jLabel1.setText("jLabel1");
        this.D_Item.getContentPane().setLayout(new GridLayout(1, 0));
        jScrollPane1.setViewportView(this.T_Item);
        this.D_Item.getContentPane().add(jScrollPane1);
        setDefaultCloseOperation(3);
        setTitle("天堂管理介面");
        setLocationByPlatform(true);
        setMinimumSize(new Dimension(1024, 768));
        addWindowListener(new WindowAdapter() {
            /* class com.eric.gui.J_Main.C00034 */

            public void windowClosed(WindowEvent evt) {
                J_Main.this.formWindowClosed(evt);
            }
        });
        SP_Split.setDividerLocation(550);
        SP_AllChat.setAutoscrolls(true);
        this.TA_AllChat.setBackground(new Color(0, 0, 0));
        this.TA_AllChat.setColumns(30);
        this.TA_AllChat.setEditable(false);
        this.TA_AllChat.setForeground(new Color(255, 255, 255));
        this.TA_AllChat.setRows(5);
        SP_AllChat.setViewportView(this.TA_AllChat);
        f2TP.addTab("全部", SP_AllChat);
        SP_Consol.setAutoscrolls(true);
        this.TA_Consol.setBackground(new Color(0, 0, 0));
        this.TA_Consol.setColumns(30);
        this.TA_Consol.setEditable(false);
        this.TA_Consol.setForeground(new Color(255, 255, 255));
        this.TA_Consol.setRows(5);
        this.TA_Consol.setEnabled(false);
        SP_Consol.setViewportView(this.TA_Consol);
        f2TP.addTab("指令", SP_Consol);
        SP_World.setAutoscrolls(true);
        this.TA_World.setBackground(new Color(0, 0, 0));
        this.TA_World.setColumns(30);
        this.TA_World.setEditable(false);
        this.TA_World.setForeground(new Color(255, 255, 255));
        this.TA_World.setRows(5);
        this.TA_World.setEnabled(false);
        SP_World.setViewportView(this.TA_World);
        f2TP.addTab("世界 ", SP_World);
        SP_Normal.setAutoscrolls(true);
        SP_Normal.setBackground(new Color(0, 0, 0));
        this.TA_Normal.setColumns(30);
        this.TA_Normal.setEditable(false);
        this.TA_Normal.setForeground(new Color(255, 255, 255));
        this.TA_Normal.setRows(5);
        this.TA_Normal.setEnabled(false);
        SP_Normal.setViewportView(this.TA_Normal);
        f2TP.addTab("一般", SP_Normal);
        SP_.setAutoscrolls(true);
        this.TA_Private.setBackground(new Color(0, 0, 0));
        this.TA_Private.setColumns(30);
        this.TA_Private.setEditable(false);
        this.TA_Private.setForeground(new Color(255, 255, 255));
        this.TA_Private.setRows(5);
        this.TA_Private.setEnabled(false);
        SP_.setViewportView(this.TA_Private);
        f2TP.addTab("密語", SP_);
        SP_Clan.setAutoscrolls(true);
        this.TA_Clan.setBackground(new Color(0, 0, 0));
        this.TA_Clan.setColumns(30);
        this.TA_Clan.setEditable(false);
        this.TA_Clan.setForeground(new Color(255, 255, 255));
        this.TA_Clan.setRows(5);
        this.TA_Clan.setEnabled(false);
        SP_Clan.setViewportView(this.TA_Clan);
        f2TP.addTab("血盟", SP_Clan);
        SP_ClanAllance.setAutoscrolls(true);
        this.TA_ClanAllance.setBackground(new Color(0, 0, 0));
        this.TA_ClanAllance.setColumns(30);
        this.TA_ClanAllance.setEditable(false);
        this.TA_ClanAllance.setForeground(new Color(255, 255, 255));
        this.TA_ClanAllance.setRows(5);
        this.TA_ClanAllance.setEnabled(false);
        SP_ClanAllance.setViewportView(this.TA_ClanAllance);
        f2TP.addTab("聯盟", SP_ClanAllance);
        SP_Team.setAutoscrolls(true);
        this.TA_Team.setBackground(new Color(0, 0, 0));
        this.TA_Team.setColumns(30);
        this.TA_Team.setEditable(false);
        this.TA_Team.setForeground(new Color(255, 255, 255));
        this.TA_Team.setRows(5);
        this.TA_Team.setEnabled(false);
        SP_Team.setViewportView(this.TA_Team);
        f2TP.addTab("組隊", SP_Team);
        SP_Split.setLeftComponent(f2TP);
        this.T_Player.addMouseListener(new MouseAdapter() {
            /* class com.eric.gui.J_Main.C00045 */

            public void mousePressed(MouseEvent evt) {
                J_Main.this.T_PlayerMousePressed(evt);
            }

            public void mouseReleased(MouseEvent evt) {
                J_Main.this.T_PlayerMouseReleased(evt);
            }
        });
        SP_player.setViewportView(this.T_Player);
        SP_Split.setRightComponent(SP_player);
        getContentPane().add(SP_Split, "Center");
        this.CB_Channel.setModel(new DefaultComboBoxModel(new String[]{"訊息", "密語", "重要", "管理者", "關機"}));
        b_Submit.setText("發送");
        b_Submit.addActionListener(new ActionListener() {
            /* class com.eric.gui.J_Main.C00056 */

            public void actionPerformed(ActionEvent evt) {
                J_Main.this.B_SubmitActionPerformed(evt);
            }
        });
        this.TF_Msg.addKeyListener(new KeyAdapter() {
            /* class com.eric.gui.J_Main.C00067 */

            public void keyPressed(KeyEvent evt) {
                J_Main.this.TF_MsgKeyPressed(evt);
            }
        });
        GroupLayout jPanel2Layout = new GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addComponent(this.CB_Channel, -2, -1, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.TF_Target, -2, 68, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.TF_Msg, -2, 310, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(b_Submit).addGap((int) L1SkillId.SOUL_OF_FLAME, (int) L1SkillId.SOUL_OF_FLAME, (int) L1SkillId.SOUL_OF_FLAME)));
        jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addGap(6, 6, 6).addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.CB_Channel, -2, -1, -2).addComponent(this.TF_Target, -2, -1, -2).addComponent(this.TF_Msg, -2, -1, -2).addComponent(b_Submit))));
        getContentPane().add(jPanel2, "South");
        m_File.setMnemonic('F');
        m_File.setText("設置(F)");
        this.MI_Save.setAccelerator(KeyStroke.getKeyStroke(83, 2));
        this.MI_Save_A.setMnemonic('K');
        this.MI_Save_A.setText("啟動端口(K)");
        m_File.add(this.MI_Save_A);
        m_File.add(jSeparator3);
        this.MI_Close_F.setMnemonic('L');
        this.MI_Close_F.setText("關閉端口(L)");
        m_File.add(this.MI_Close_F);
        this.MI_Save.setMnemonic('S');
        this.MI_Save.setText("儲存訊息(S)");
        m_File.add(this.MI_Save);
        m_File.add(jSeparator3);
        this.MI_SetClose.setAccelerator(KeyStroke.getKeyStroke(69, 2));
        this.MI_SetClose.setMnemonic('E');
        this.MI_SetClose.setText("設定關閉伺服器(E)");
        m_File.add(this.MI_SetClose);
        this.MI_Close.setMnemonic('C');
        this.MI_Close.setText("關閉伺服器(C)");
        m_File.add(this.MI_Close);
        f1MB.add(m_File);
        m_Edit.setMnemonic('E');
        m_Edit.setText("編輯(E)");
        this.MI_Adena.setMnemonic('A');
        this.MI_Adena.setText("金幣倍率(A)");
        m_Edit.add(this.MI_Adena);
        this.MI_Drop.setMnemonic('B');
        this.MI_Drop.setText("掉寶倍率(B)");
        m_Edit.add(this.MI_Drop);
        this.MI_Karma.setMnemonic('C');
        this.MI_Karma.setText("友好度倍率(C)");
        m_Edit.add(this.MI_Karma);
        this.MI_Exp.setMnemonic('D');
        this.MI_Exp.setText("經驗倍率(D)");
        m_Edit.add(this.MI_Exp);
        this.MI_Weigh.setMnemonic('E');
        this.MI_Weigh.setText("負重倍率(E)");
        m_Edit.add(this.MI_Weigh);
        this.MI_LA.setMnemonic('F');
        this.MI_LA.setText("正義值倍率(F)");
        m_Edit.add(this.MI_LA);
        this.MI_XP_PET.setMnemonic('G');
        this.MI_XP_PET.setText("寵物經驗倍率(G)");
        m_Edit.add(this.MI_XP_PET);
        this.MI_Weigh_PET.setMnemonic('H');
        this.MI_Weigh_PET.setText("寵物負重倍率(H)");
        m_Edit.add(this.MI_Weigh_PET);
        this.MI_ATTR_ENCHANT_CHANCE.setMnemonic('I');
        this.MI_ATTR_ENCHANT_CHANCE.setText("屬性強化倍率(I)");
        m_Edit.add(this.MI_ATTR_ENCHANT_CHANCE);
        this.MI_CHANCE_ARMOR.setMnemonic('J');
        this.MI_CHANCE_ARMOR.setText("防具強化倍率(J)");
        m_Edit.add(this.MI_CHANCE_ARMOR);
        this.MI_CHANCE_WEAPON.setMnemonic('K');
        this.MI_CHANCE_WEAPON.setText("武器強化倍率(K)");
        m_Edit.add(this.MI_CHANCE_WEAPON);
        this.MI_Shop_Sell.setMnemonic('L');
        this.MI_Shop_Sell.setText("商店販賣倍率(L)");
        m_Edit.add(this.MI_Shop_Sell);
        this.MI_Shop_Purchas.setMnemonic('M');
        this.MI_Shop_Purchas.setText("商店收購倍率(M)");
        m_Edit.add(this.MI_Shop_Purchas);
        f1MB.add(m_Edit);
        m_Special.setMnemonic('S');
        m_Special.setText("特殊功能(S)");
        this.MI_Angel.setMnemonic('A');
        this.MI_Angel.setText("大天使祝福(A)");
        m_Special.add(this.MI_Angel);
        this.MI_AllBuff.setMnemonic('B');
        this.MI_AllBuff.setText("終極祝福(B)");
        m_Special.add(this.MI_AllBuff);
        this.MI_AllRess.setMnemonic('C');
        this.MI_AllRess.setText("全體復活補血魔(C)");
        m_Special.add(this.MI_AllRess);
        f1MB.add(m_Special);
        this.MI_AllPoly.setMnemonic('Z');
        this.MI_AllPoly.setText("全體變身(Z)");
        m_Special.add(this.MI_AllPoly);
        this.MI_AllItem.setMnemonic('X');
        this.MI_AllItem.setText("全體發送道具到倉庫(X)");
        m_Special.add(this.MI_AllItem);
        this.MI_AllGiveItem.setMnemonic('N');
        this.MI_AllGiveItem.setText("全體發送背包道具(N)");
        m_Special.add(this.MI_AllGiveItem);
        this.MI_DeadPc.setMnemonic('D');
        this.MI_DeadPc.setText("殺死所有玩家(D)");
        m_Special.add(this.MI_DeadPc);
        this.MI_Warcastle.setMnemonic('F');
        this.MI_Warcastle.setText("啟動-攻城戰(F)");
        m_Special.add(this.MI_Warcastle);
        this.MI_War.setMnemonic('G');
        this.MI_War.setText("停止-攻城戰(G)");
        m_Special.add(this.MI_War);
        this.MI_House.setMnemonic('S');
        this.MI_House.setText("血盟小屋結標(S)");
        m_Special.add(this.MI_House);
        m_Reset.setMnemonic('R');
        m_Reset.setText("重新讀取(R)");
        f1MB.add(m_Reset);
        this.MI_RDrop.setMnemonic('D');
        this.MI_RDrop.setText("全掉落物重讀(D)");
        m_Reset.add(this.MI_RDrop);
        this.MI_RShop.setMnemonic('S');
        this.MI_RShop.setText("全商店資訊重讀(S)");
        m_Reset.add(this.MI_RShop);
        this.MI_RShopCn.setMnemonic('C');
        this.MI_RShopCn.setText("寶箱資料重讀(C)");
        m_Reset.add(this.MI_RShopCn);
        this.MI_RItem.setMnemonic('I');
        this.MI_RItem.setText("武器/防具/道具資料重讀(I)");
        m_Reset.add(this.MI_RItem);
        this.MI_RNpc.setMnemonic('N');
        this.MI_RNpc.setText("NPC資料重讀(N)");
        m_Reset.add(this.MI_RNpc);
        this.MI_RDropMap.setMnemonic('M');
        this.MI_RDropMap.setText("陷阱資料重讀(M)");
        m_Reset.add(this.MI_RDropMap);
        this.MI_Table.setMnemonic('Y');
        this.MI_Table.setText("全公告資料重讀(Y)");
        m_Reset.add(this.MI_Table);
        setJMenuBar(f1MB);
        pack();
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void T_PlayerMouseReleased(MouseEvent evt) {
        if (evt.getClickCount() == 2 && evt.getButton() == 1) {
            this.select = this.T_Player.getSelectedRow();
            setPlayerView((String) this.DTM.getValueAt(this.select, 1));
            this.F_Player.pack();
            this.F_Player.setVisible(true);
        }
        if (evt.isPopupTrigger()) {
            this.select = this.T_Player.getSelectedRow();
            this.PM_Player.show(this.T_Player, evt.getX(), evt.getY());
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void formWindowClosed(WindowEvent evt) {
        closeServer();
    }

    private void closeServer() {
        saveChatData(false);
        System.exit(0);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void T_PlayerMousePressed(MouseEvent evt) {
        processEvent(evt);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void B_SubmitActionPerformed(ActionEvent evt) {
        submitMsg(this.CB_Channel.getSelectedIndex());
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void TF_MsgKeyPressed(KeyEvent evt) {
        if (evt.getKeyCode() == 10) {
            submitMsg(this.CB_Channel.getSelectedIndex());
        }
    }

    private void submitMsg(int select2) {
        String title4;
        String title2;
        String title1;
        if (!this.TF_Msg.getText().equals("")) {
            switch (select2) {
                case 0:
                    if (this.TF_Target.getText().equals(null) || this.TF_Target.getText().equals("")) {
                        title1 = "系統公告";
                    } else {
                        title1 = this.TF_Target.getText();
                    }
                    World.get().broadcastServerMessage("【" + title1 + "】:" + this.TF_Msg.getText());
                    addWorldChat("【" + title1 + "】", this.TF_Msg.getText());
                    break;
                case 1:
                    if (World.get().getPlayer(this.TF_Target.getText()) != null) {
                        World.get().getPlayer(this.TF_Target.getText()).sendPackets(new S_SystemMessage("【系統密語】:" + this.TF_Msg.getText()));
                        addPrivateChat("【系統密語】", this.TF_Target.getText(), this.TF_Msg.getText());
                        break;
                    } else {
                        return;
                    }
                case 2:
                    if (this.TF_Target.getText().equals(null) || this.TF_Target.getText().equals("")) {
                        title2 = "重要公告";
                    } else {
                        title2 = this.TF_Target.getText();
                    }
                    World.get().broadcastPacketToAll(new S_BlueMessage(166, "\\f3【" + title2 + "】:" + this.TF_Msg.getText()));
                    addWorldChat("【" + title2 + "】", this.TF_Msg.getText());
                    break;
                case 3:
                    String title3 = (this.TF_Target.getText().equals(null) || this.TF_Target.getText().equals("")) ? "" : this.TF_Target.getText();
                    World.get().broadcastPacketToAll(new S_SeerverMessage("[*******]:" + title3 + this.TF_Msg.getText()));
                    addWorldChat("管理者【" + title3 + "】", this.TF_Msg.getText());
                    break;
                case 4:
                    if (this.TF_Target.getText().equals(null) || this.TF_Target.getText().equals("")) {
                        title4 = "關機公告";
                    } else {
                        title4 = this.TF_Target.getText();
                    }
                    World.get().broadcastServerMessage("【" + title4 + "】:" + this.TF_Msg.getText());
                    addWorldChat("【" + title4 + "】", this.TF_Msg.getText());
                    break;
            }
            this.TF_Msg.setText("");
        }
    }

    private void showItemTable(int num) {
        iniTable();
        L1PcInstance pc = L1PcInstance.load(this.TF_Name.getText());
        if (pc.getInventory().getSize() == 0) {
            items(pc);
        }
        switch (num) {
            case 0:
                if (pc.getInventory() != null) {
                    L1Inventory inv = pc.getInventory();
                    this.D_Item.setTitle("身上物品");
                    for (L1ItemInstance item : inv.getItems()) {
                        addItemTable(String.valueOf(item.getName2()) + "物品編號：" + item.getItemId(), item.getCount(), (long) item.getId(), item.getEnchantLevel());
                    }
                    break;
                } else {
                    return;
                }
            case 1:
                if (pc.getDwarfInventory() != null) {
                    this.D_Item.setTitle("倉庫物品");
                    for (L1ItemInstance item2 : pc.getDwarfInventory().getItems()) {
                        addItemTable(String.valueOf(item2.getName2()) + "物品編號：" + item2.getItemId(), item2.getCount(), (long) item2.getId(), item2.getEnchantLevel());
                    }
                    break;
                } else {
                    return;
                }
            case 2:
                if (pc.getClanid() != 0) {
                    if (pc.getClan().getDwarfForClanInventory() != null) {
                        this.D_Item.setTitle("血盟倉庫物品");
                        for (L1ItemInstance item3 : pc.getClan().getDwarfForClanInventory().getItems()) {
                            addItemTable(String.valueOf(item3.getName2()) + "物品編號：" + item3.getItemId(), item3.getCount(), (long) item3.getId(), item3.getEnchantLevel());
                        }
                        break;
                    } else {
                        return;
                    }
                }
                break;
            case 3:
                if (pc.getDwarfForElfInventory() != null) {
                    this.D_Item.setTitle("妖森倉庫物品");
                    for (L1ItemInstance item4 : pc.getDwarfForElfInventory().getItems()) {
                        addItemTable(String.valueOf(item4.getName2()) + "物品編號：" + item4.getItemId(), item4.getCount(), (long) item4.getId(), item4.getEnchantLevel());
                    }
                    break;
                } else {
                    return;
                }
            case 4:
                if (pc.getDwarfInventory() != null) {
                    this.D_Item.setTitle("裝備物品");
                    for (L1ItemInstance item5 : pc.getInventory().getItems()) {
                        if (item5.isEquipped()) {
                            addItemTable(String.valueOf(item5.getName2()) + "物品編號：" + item5.getItemId(), item5.getCount(), (long) item5.getId(), item5.getEnchantLevel());
                        }
                    }
                    break;
                } else {
                    return;
                }
        }
        this.D_Item.setVisible(true);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void B_ItemActionPerformed(ActionEvent evt) {
        showItemTable(this.CB_Item.getSelectedIndex());
    }

    private void setPlayerView(String name) {
        L1PcInstance pc = L1PcInstance.load(name);
        int job = 0;
        switch (pc.getClassId()) {
            case 0:
                job = 715;
                break;
            case 1:
                job = 647;
                break;
            case 37:
                job = 198;
                break;
            case 48:
                job = 317;
                break;
            case 61:
                job = 384;
                break;
            case 138:
                job = OpcodesClient.C_OPCODE_ARROWATTACK;
                break;
            case L1PcInstance.CLASSID_WIZARD_MALE /*{ENCODED_INT: 734}*/:
                job = 532;
                break;
            case L1PcInstance.CLASSID_WIZARD_FEMALE /*{ENCODED_INT: 1186}*/:
                job = 452;
                break;
            case L1PcInstance.CLASSID_DARK_ELF_MALE /*{ENCODED_INT: 2786}*/:
                job = 145;
                break;
            case L1PcInstance.CLASSID_DARK_ELF_FEMALE /*{ENCODED_INT: 2796}*/:
                job = 25;
                break;
            case L1PcInstance.CLASSID_ILLUSIONIST_FEMALE /*{ENCODED_INT: 6650}*/:
                job = 1056;
                break;
            case L1PcInstance.CLASSID_DRAGON_KNIGHT_MALE /*{ENCODED_INT: 6658}*/:
                job = 903;
                break;
            case L1PcInstance.CLASSID_DRAGON_KNIGHT_FEMALE /*{ENCODED_INT: 6661}*/:
                job = 930;
                break;
            case 6671:
                job = 1029;
                break;
        }
        this.L_Image.setIcon(new ImageIcon("img/" + job + ".png"));
        this.TF_Account.setText(pc.getAccountName());
        this.TF_Name.setText(pc.getName());
        this.TF_Title.setText(pc.getTitle());
        this.TF_AccessLevel.setText(new StringBuilder().append((int) pc.getAccessLevel()).toString());
        this.TF_Sex.setText(pc.get_sex() == 1 ? "女" : "男");
        this.TF_Ac.setText(new StringBuilder(String.valueOf(pc.getAc())).toString());
        this.TF_Cha.setText(new StringBuilder(String.valueOf((int) pc.getCha())).toString());
        this.TF_Int.setText(new StringBuilder(String.valueOf((int) pc.getInt())).toString());
        this.TF_Str.setText(new StringBuilder(String.valueOf((int) pc.getStr())).toString());
        this.TF_Con.setText(new StringBuilder(String.valueOf((int) pc.getCon())).toString());
        this.TF_Wis.setText(new StringBuilder(String.valueOf((int) pc.getWis())).toString());
        this.TF_Dex.setText(new StringBuilder(String.valueOf((int) pc.getDex())).toString());
        this.TF_Exp.setText(new StringBuilder(String.valueOf(pc.getExp())).toString());
        this.TF_Map.setText(new StringBuilder(String.valueOf((int) pc.getMapId())).toString());
        this.TF_X.setText(new StringBuilder(String.valueOf(pc.getX())).toString());
        this.TF_Y.setText(new StringBuilder(String.valueOf(pc.getY())).toString());
        this.TF_Clan.setText(pc.getClanname());
        this.TF_Level.setText(new StringBuilder(String.valueOf(pc.getLevel())).toString());
        this.TF_Hp.setText(String.valueOf(pc.getCurrentHp()) + " / " + pc.getMaxHp());
        this.TF_Mp.setText(String.valueOf(pc.getCurrentMp()) + " / " + ((int) pc.getMaxMp()));
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            /* class com.eric.gui.J_Main.RunnableC00078 */
        	
            public void run() {

                new J_Main().setVisible(true);
            }
        });
    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if ((((long) e.getModifiers()) & 16) == 0 && !((e.getModifiers() & 4) == 0 && (e.getModifiers() & 8) == 0)) {
            return;
        }
        if (command.equals("強制踢除(K)")) {
            L1PcInstance target = World.get().getPlayer((String) this.DTM.getValueAt(this.select, 1));
            if (target != null) {
                addConsol(("您把玩家：" + ((String) this.DTM.getValueAt(this.select, 1)) + "強制剔除遊戲。"));
                target.sendPackets(new S_Disconnect());
                return;
            }
            addConsol("此玩家不在線上。");
        } else if (command.equals("刪除物品(2)")) {
            try {
                L1PcInstance target2 = World.get().getPlayer((String) this.DTM.getValueAt(this.select, 1));
                if (target2 == null) {
                    JOptionPane.showMessageDialog(this, "此玩家不在線上無法贈送");
                    return;
                }
                String temp2 = JOptionPane.showInputDialog("請輸入物品ID");
                if (temp2 != null) {
                    int second2 = Integer.valueOf(temp2).intValue();
                    L1ItemInstance item = ItemTable.get().createItem(second2);
                    if (item == null) {
                        JOptionPane.showMessageDialog(this, "物品ID錯誤 請重新輸入");
                        return;
                    }
                    String temp3 = JOptionPane.showInputDialog("你確定要刪除" + item.getName2() + "個");
                    if (!temp3.equals("")) {
                        int second3 = Integer.valueOf(temp3).intValue();
                        if (!target2.getInventory().checkItem(second2, (long) second3)) {
                            JOptionPane.showMessageDialog(this, "玩家沒有這麼多物品刪除");
                            return;
                        }
                        target2.getInventory().consumeItem(second2, (long) second3);
                        addConsol(String.valueOf(target2.getName()) + "刪除" + item.getName2() + second3 + "個成功");
                    }
                }
            } catch (NumberFormatException e2) {
                JOptionPane.showMessageDialog(this, "請輸入整數!");
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        } else if (command.equals("啟動端口(K)")) {
            try {
                EchoServerTimer.get().startPort(Integer.valueOf(JOptionPane.showInputDialog("請輸入要啟動的端口")).intValue());
            } catch (NumberFormatException e3) {
                JOptionPane.showMessageDialog(this, "輸入錯誤");
            }
        } else if (command.equals("關閉端口(L)")) {
            try {
                EchoServerTimer.get().stopPort(Integer.valueOf(JOptionPane.showInputDialog("請輸入要關閉的端口")).intValue());
            } catch (NumberFormatException e4) {
                JOptionPane.showMessageDialog(this, "輸入錯誤");
            }
        } else if (command.equals("給予金幣(X)")) {
            L1PcInstance target3 = World.get().getPlayer((String) this.DTM.getValueAt(this.select, 1));
            try {
                String temp = JOptionPane.showInputDialog("請輸入該金幣數量!");
                if (!(temp == null || temp.equals(""))) {
                    int count = Integer.parseInt(temp, 10);
                    L1ItemInstance item2 = ItemTable.get().createItem(L1ItemId.ADENA);
                    if (item2 != null) {
                        if (item2.isStackable()) {
                            item2.setCount((long) count);
                            item2.setIdentified(true);
                        } else {
                            item2.setCount(serialVersionUID);
                            item2.setIdentified(true);
                        }
                        if (item2 != null) {
                            if (target3.getInventory().checkAddItem(item2, (long) count) == 0) {
                                target3.getInventory().storeItem(item2);
                            } else {
                                World.get().getInventory(target3.getX(), target3.getY(), target3.getMapId()).storeItem(item2);
                            }
                            target3.sendPackets(new S_SystemMessage("遊戲管理者發送 " + item2.getName() + " " + count + "個。"));
                        }
                        addConsol("發送道具：" + item2.getName() + " " + count + "個 給玩家：" + ((String) this.DTM.getValueAt(this.select, 1)) + " 了。");
                    }
                }
            } catch (NumberFormatException e5) {
                JOptionPane.showMessageDialog(this, "請輸入整數!");
            }
        } else if (command.equals("給予GM職業所有技能(Y)")) {
            try {
                L1PcInstance target4 = World.get().getPlayer((String) this.DTM.getValueAt(this.select, 1));
                int object_id = target4.getId();
                target4.sendPacketsX8(new S_SkillSound(object_id, 227));
                if (target4.isCrown()) {
                    target4.sendPackets(new S_AddSkill(target4, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
                    for (int cnt = 1; cnt <= 16; cnt++) {
                        L1Skills l1skills = SkillsTable.get().getTemplate(cnt);
                        CharSkillReading.get().spellMastery(object_id, l1skills.getSkillId(), l1skills.getName(), 0, 0);
                    }
                    for (int cnt2 = 113; cnt2 <= 120; cnt2++) {
                        L1Skills l1skills2 = SkillsTable.get().getTemplate(cnt2);
                        CharSkillReading.get().spellMastery(object_id, l1skills2.getSkillId(), l1skills2.getName(), 0, 0);
                    }
                } else if (target4.isKnight()) {
                    target4.sendPackets(new S_AddSkill(target4, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0, 192, 7, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
                    for (int cnt3 = 1; cnt3 <= 8; cnt3++) {
                        L1Skills l1skills3 = SkillsTable.get().getTemplate(cnt3);
                        CharSkillReading.get().spellMastery(object_id, l1skills3.getSkillId(), l1skills3.getName(), 0, 0);
                    }
                    for (int cnt4 = 87; cnt4 <= 91; cnt4++) {
                        L1Skills l1skills4 = SkillsTable.get().getTemplate(cnt4);
                        CharSkillReading.get().spellMastery(object_id, l1skills4.getSkillId(), l1skills4.getName(), 0, 0);
                    }
                } else if (target4.isElf()) {
                    target4.sendPackets(new S_AddSkill(target4, 255, 255, 127, 255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 127, 3, 255, 255, 255, 255, 0, 0, 0, 0, 0, 0));
                    for (int cnt5 = 1; cnt5 <= 48; cnt5++) {
                        L1Skills l1skills5 = SkillsTable.get().getTemplate(cnt5);
                        CharSkillReading.get().spellMastery(object_id, l1skills5.getSkillId(), l1skills5.getName(), 0, 0);
                    }
                    for (int cnt6 = 129; cnt6 <= 176; cnt6++) {
                        L1Skills l1skills6 = SkillsTable.get().getTemplate(cnt6);
                        CharSkillReading.get().spellMastery(object_id, l1skills6.getSkillId(), l1skills6.getName(), 0, 0);
                    }
                } else if (target4.isWizard()) {
                    target4.sendPackets(new S_AddSkill(target4, 255, 255, 127, 255, 255, 255, 255, 255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
                    for (int cnt7 = 1; cnt7 <= 80; cnt7++) {
                        L1Skills l1skills7 = SkillsTable.get().getTemplate(cnt7);
                        CharSkillReading.get().spellMastery(object_id, l1skills7.getSkillId(), l1skills7.getName(), 0, 0);
                    }
                } else if (target4.isDarkelf()) {
                    target4.sendPackets(new S_AddSkill(target4, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
                    for (int cnt8 = 1; cnt8 <= 16; cnt8++) {
                        L1Skills l1skills8 = SkillsTable.get().getTemplate(cnt8);
                        CharSkillReading.get().spellMastery(object_id, l1skills8.getSkillId(), l1skills8.getName(), 0, 0);
                    }
                    for (int cnt9 = 97; cnt9 <= 112; cnt9++) {
                        L1Skills l1skills9 = SkillsTable.get().getTemplate(cnt9);
                        CharSkillReading.get().spellMastery(object_id, l1skills9.getSkillId(), l1skills9.getName(), 0, 0);
                    }
                } else if (target4.isDragonKnight()) {
                    target4.sendPackets(new S_AddSkill(target4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 240, 255, 7, 0, 0, 0));
                    for (int cnt10 = L1SkillId.DRAGON_SKIN; cnt10 <= 195; cnt10++) {
                        L1Skills l1skills10 = SkillsTable.get().getTemplate(cnt10);
                        CharSkillReading.get().spellMastery(object_id, l1skills10.getSkillId(), l1skills10.getName(), 0, 0);
                    }
                } else if (target4.isIllusionist()) {
                    target4.sendPackets(new S_AddSkill(target4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 255, 255, 15));
                    for (int cnt11 = 201; cnt11 <= 220; cnt11++) {
                        L1Skills l1skills11 = SkillsTable.get().getTemplate(cnt11);
                        CharSkillReading.get().spellMastery(object_id, l1skills11.getSkillId(), l1skills11.getName(), 0, 0);
                    }
                }
            } catch (NumberFormatException ignored) {
            }
        } else if (command.equals("屬性變更([)")) {
            Object[] options = {"力量", "體力", "敏捷", "智力", "精神", "魅力", "權限", "血量", "魔力", "等級", "正義"};
            int m = JOptionPane.showOptionDialog((Component) null, " 請選擇屬性 ", "玩家屬性變更", 0, 3, (Icon) null, options, options[0]);
            L1PcInstance target5 = World.get().getPlayer((String) this.DTM.getValueAt(this.select, 1));
            switch (m) {
                case 0:
                    String temp0 = JOptionPane.showInputDialog("變更力量為多少");
                    if (!(temp0 == null || temp0.equals(""))) {
                        target5.addBaseStr(((byte) (-target5.getBaseStr())) + Integer.valueOf(temp0).intValue());
                        target5.sendPackets(new S_OwnCharStatus(target5));
                        return;
                    }
                    return;
                case 1:
                    String temp1 = JOptionPane.showInputDialog("變更體力為多少");
                    if (!(temp1 == null || temp1.equals(""))) {
                        target5.addBaseCon(((byte) (-target5.getBaseCon())) + Integer.valueOf(temp1).intValue());
                        target5.sendPackets(new S_OwnCharStatus(target5));
                        return;
                    }
                    return;
                case 2:
                    String temp22 = JOptionPane.showInputDialog("變更敏捷為多少");
                    if (!(temp22 == null || temp22.equals(""))) {
                        target5.addBaseDex(((byte) (-target5.getBaseDex())) + Integer.valueOf(temp22).intValue());
                        target5.sendPackets(new S_OwnCharStatus(target5));
                        return;
                    }
                    return;
                case 3:
                    String temp32 = JOptionPane.showInputDialog("變更智力為多少");
                    if (!(temp32 == null || temp32.equals(""))) {
                        target5.addBaseInt(((byte) (-target5.getBaseInt())) + Integer.valueOf(temp32).intValue());
                        target5.sendPackets(new S_OwnCharStatus(target5));
                        return;
                    }
                    return;
                case 4:
                    String temp4 = JOptionPane.showInputDialog("變更精神為多少");
                    if (!(temp4 == null || temp4.equals(""))) {
                        target5.addBaseWis(((byte) (-target5.getBaseWis())) + Integer.valueOf(temp4).intValue());
                        target5.sendPackets(new S_OwnCharStatus(target5));
                        return;
                    }
                    return;
                case 5:
                    String temp5 = JOptionPane.showInputDialog("變更魅力為多少");
                    if (!(temp5 == null || temp5.equals(""))) {
                        target5.addBaseCha(((byte) (-target5.getBaseCha())) + Integer.valueOf(temp5).intValue());
                        target5.sendPackets(new S_OwnCharStatus(target5));
                        return;
                    }
                    return;
                case 6:
                    String temp6 = JOptionPane.showInputDialog("設置GM權限多少");
                    if (!(temp6 == null || temp6.equals(""))) {
                        target5.setAccessLevel((short) Integer.valueOf(temp6).intValue());
                        target5.sendPackets(new S_SystemMessage("取得GM權限或取消GM權限，請重新登入。"));
                        return;
                    }
                    return;
                case 7:
                    String temp7 = JOptionPane.showInputDialog("設置血量加多少");
                    if (!(temp7 == null || temp7.equals(""))) {
                        target5.addBaseMaxHp((short) Integer.valueOf(temp7).intValue());
                        target5.sendPackets(new S_OwnCharStatus(target5));
                        return;
                    }
                    return;
                case 8:
                    String temp8 = JOptionPane.showInputDialog("設置魔力加多少");
                    if (!(temp8 == null || temp8.equals(""))) {
                        target5.addBaseMaxMp((short) Integer.valueOf(temp8).intValue());
                        target5.sendPackets(new S_OwnCharStatus(target5));
                        return;
                    }
                    return;
                case 9:
                    String temp9 = JOptionPane.showInputDialog("設置角色等級多少");
                    if (!(temp9 == null || temp9.equals(""))) {
                        target5.setExp(ExpTable.getExpByLevel(Integer.valueOf(temp9).intValue()));
                        return;
                    }
                    return;
                case 10:
                    String temp10 = JOptionPane.showInputDialog("設置角色正義多少");
                    if (!(temp10 == null || temp10.equals(""))) {
                        target5.setLawful(Integer.valueOf(temp10).intValue());
                        return;
                    }
                    return;
                default:
                    return;
            }
        } else if (command.equals("發送道具(V)")) {
            L1PcInstance target6 = World.get().getPlayer((String) this.DTM.getValueAt(this.select, 1));
            try {
                String temp11 = JOptionPane.showInputDialog("請輸入道具編號!");
                String temp12 = JOptionPane.showInputDialog("請輸入該道具數量!");
                String temp23 = JOptionPane.showInputDialog("請輸入該道具強化值!");
                if (!(temp11 == null || temp11.equals(""))) {
                    int itemid = Integer.parseInt(temp11, 10);
                    int count2 = Integer.parseInt(temp12, 10);
                    int enchant = Integer.parseInt(temp23, 10);
                    L1ItemInstance item3 = ItemTable.get().createItem(itemid);
                    if (item3 != null) {
                        if (item3.isStackable()) {
                            item3.setCount((long) count2);
                            item3.setEnchantLevel(enchant);
                            item3.setIdentified(true);
                        } else {
                            item3.setCount(serialVersionUID);
                            item3.setEnchantLevel(enchant);
                            item3.setIdentified(true);
                        }
                        if (item3 != null) {
                            if (target6.getInventory().checkAddItem(item3, (long) count2) == 0) {
                                target6.getInventory().storeItem(item3);
                            } else {
                                World.get().getInventory(target6.getX(), target6.getY(), target6.getMapId()).storeItem(item3);
                            }
                            target6.sendPackets(new S_SystemMessage("遊戲管理者發送道具 " + item3.getName() + "個" + count2 + "個"));
                        }
                        addConsol("發送道具：" + item3.getName() + " " + count2 + "個 給玩家：" + ((String) this.DTM.getValueAt(this.select, 1)) + " 了。");
                    }
                }
            } catch (NumberFormatException e7) {
                JOptionPane.showMessageDialog(this, "請輸入整數!");
            }
        } else if (command.equals("發送道具到倉庫(Q)")) {
            L1PcInstance target7 = World.get().getPlayer((String) this.DTM.getValueAt(this.select, 1));
            try {
                String temp13 = JOptionPane.showInputDialog("請輸入道具編號!");
                String temp14 = JOptionPane.showInputDialog("請輸入該道具數量!");
                String temp24 = JOptionPane.showInputDialog("請輸入該道具強化值!");
                if (!(temp13 == null || temp13.equals(""))) {
                    int itemid2 = Integer.parseInt(temp13, 10);
                    int count3 = Integer.parseInt(temp14, 10);
                    int enchant2 = Integer.parseInt(temp24, 10);
                    L1ItemInstance item4 = ItemTable.get().createItem(itemid2);
                    if (item4 != null) {
                        if (item4.isStackable()) {
                            item4.setCount((long) count3);
                            item4.setEnchantLevel(enchant2);
                            item4.setIdentified(true);
                        } else {
                            item4.setCount(serialVersionUID);
                            item4.setEnchantLevel(enchant2);
                            item4.setIdentified(true);
                        }
                        if (item4 != null) {
                            if (target7.getInventory().checkAddItem(item4, (long) count3) == 0) {
                                DwarfReading.get().insertItem(target7.getAccountName(), item4);
                            }
                            target7.getDwarfInventory().loadItems();
                            target7.sendPackets(new S_SystemMessage("遊戲管理者發送道具到倉庫： 【" + item4.getName() + "(" + count3 + ")】。"));
                        }
                        addConsol("發送道具到倉庫：" + item4.getName() + " " + count3 + "個 給玩家：" + ((String) this.DTM.getValueAt(this.select, 1)) + " 了。");
                    }
                }
            } catch (NumberFormatException e8) {
                JOptionPane.showMessageDialog(this, "請輸入整數!");
            }
        } else if (command.equals("發送道具到即時獎勵(J)")) {
            L1PcInstance target8 = World.get().getPlayer((String) this.DTM.getValueAt(this.select, 1));
            try {
                String temp15 = JOptionPane.showInputDialog("請輸入道具編號!");
                String temp16 = JOptionPane.showInputDialog("請輸入該道具數量!");
                if (!(temp15 == null || temp15.equals(""))) {
                    int itemid3 = Integer.parseInt(temp15, 10);
                    int count4 = Integer.parseInt(temp16, 10);
                    L1ItemInstance item5 = ItemTable.get().createItem(itemid3);
                    if (item5 != null) {
                        if (item5.isStackable()) {
                            item5.setCount((long) count4);
                            item5.setIdentified(true);
                        } else {
                            item5.setCount(serialVersionUID);
                            item5.setIdentified(true);
                        }
                        if (item5 != null) {
                            if (target8.getInventory().checkAddItem(item5, (long) count4) == 0) {
                                EzpayReading3.get().insertItem(target8.getAccountName(), item5);
                            }
                            target8.sendPackets(new S_SystemMessage("遊戲管理者發送道具到即時獎勵： 【" + item5.getName() + "(" + count4 + ")】。"));
                        }
                        addConsol("發送道具到即時獎勵：" + item5.getName() + " " + count4 + "個 給玩家：" + ((String) this.DTM.getValueAt(this.select, 1)) + " 了。");
                    }
                }
            } catch (NumberFormatException e9) {
                JOptionPane.showMessageDialog(this, "請輸入整數!");
            }
        } else if (command.equals("解卡點(T)")) {
            L1PcInstance target9 = World.get().getPlayer((String) this.DTM.getValueAt(this.select, 1));
            if (target9 != null) {
                addConsol("玩家：" + ((String) this.DTM.getValueAt(this.select, 1)) + "已被移送新的座標。");
                L1Teleport.teleport(target9, 33442, 32797, (short) 4, target9.getHeading(), true);
                return;
            }
            addConsol("此玩家不在線上。");
        } else if (command.equals("改密碼(P)")) {
            try {
                L1PcInstance target10 = World.get().getPlayer((String) this.DTM.getValueAt(this.select, 1));
                String temp17 = JOptionPane.showInputDialog("請輸入要修改的密碼!");
                if (!(temp17 == null || temp17.equals(""))) {
                    target10.ChangPassword(temp17);
                    addConsol("玩家：" + ((String) this.DTM.getValueAt(this.select, 1)) + " 的密碼已被修改為【" + temp17 + "】");
                }
            } catch (NumberFormatException e10) {
                JOptionPane.showMessageDialog(this, "請輸入整數!");
            }
        } else if (command.equals("封鎖帳號(A)")) {
            L1PcInstance target11 = World.get().getPlayer((String) this.DTM.getValueAt(this.select, 1));
            if (target11 != null) {
                start(target11, "玩家:【 " + target11.getName() + "】管理器:封鎖帳號");
            }
            addConsol("封鎖帳號：玩家:【" + target11.getName() + "】帳號：" + target11.getAccountName());
        } else if (command.equals("封鎖IP(B)")) {
            L1PcInstance target12 = World.get().getPlayer((String) this.DTM.getValueAt(this.select, 1));
            if (target12 != null) {
                ClientExecutor targetClient = target12.getNetConnection();
                String ipaddr = targetClient.getIp().toString();
                if (ipaddr != null) {
                    if (!LanSecurityManager.BANIPMAP.containsKey(ipaddr)) {
                        IpReading.get().add(ipaddr.toString(), "玩家:【 " + target12.getName() + "】管理器:封鎖IP");
                    }
                    targetClient.kick();
                }
                addConsol("封鎖：玩家:【" + target12.getName() + "】IP：" + ipaddr);
            }
        } else if (command.equals("解錯位(C)")) {
            L1PcInstance target13 = World.get().getPlayer((String) this.DTM.getValueAt(this.select, 1));
            if (target13 != null) {
                addConsol("玩家：" + ((String) this.DTM.getValueAt(this.select, 1)) + "已被系統解除錯位。");
                L1Teleport.teleport(target13, (target13.getX() + Random.nextInt(2)) - 1, (target13.getY() + Random.nextInt(2)) - 1, target13.getMapId(), target13.getHeading(), true, 1);
                return;
            }
            addConsol("此玩家不在線上。");
            addConsol("玩家:已【" + getName() + "】被解除錯位。");
        } else if (command.equals("刪除角色(R)")) {
            try {
                String temp18 = JOptionPane.showInputDialog("是否連該角色的倉庫一併刪除!是的話，請輸入1");
                if (!(temp18 == null || temp18.equals(""))) {
                    int all = Integer.parseInt(temp18, 10);
                    L1PcInstance target14 = World.get().getPlayer((String) this.DTM.getValueAt(this.select, 1));
                    String ipaddr2 = target14.getNetConnection().getIp().toString();
                    if (target14 != null) {
                        IpReading.get().add(target14.getAccountName(), "玩家:【 " + target14.getName() + "】管理器:封鎖帳號");
                    }
                    if (ipaddr2 != null && !LanSecurityManager.BANIPMAP.containsKey(ipaddr2)) {
                        IpReading.get().add(ipaddr2.toString(), "玩家:【 " + target14.getName() + "】管理器:封鎖IP");
                    }
                    target14.sendPackets(new S_SystemMessage("10秒後，你將會斷線。"));
                    if (all == 1) {
                        target14.AllDeleteCharacter(10000);
                        WriteLogTxt.Recording("刪除玩家紀錄", "玩家:【 " + target14.getName() + " 】 " + "被管理器給刪除了，");
                    } else {
                        target14.DeleteCharacter(10000);
                        WriteLogTxt.Recording("刪除玩家紀錄", "玩家:【 " + target14.getName() + " 】 " + "被管理器給刪除了，");
                    }
                    addConsol("玩家：" + ((String) this.DTM.getValueAt(this.select, 1)) + "已被你刪除。");
                }
            } catch (NumberFormatException e11) {
                JOptionPane.showMessageDialog(this, "請輸入整數!");
            }
        } else if (command.equals("玩家資料(P)")) {
            setPlayerView((String) this.DTM.getValueAt(this.select, 1));
            this.F_Player.pack();
            this.F_Player.setVisible(true);
        } else if (command.equals("密語(W)")) {
            this.TF_Target.setText((String) this.DTM.getValueAt(this.select, 1));
            this.CB_Channel.setSelectedIndex(1);
        } else if (command.equals("儲存訊息(S)")) {
            saveChatData(false);
        } else if (command.equals("大天使祝福(A)")) {
            angel();
        } else if (command.equals("關閉伺服器(C)")) {
            closeServer();
        } else if (command.equals("正義值倍率(F)")) {
            try {
                String temp19 = JOptionPane.showInputDialog("當前正義值倍率：" + ConfigRate.RATE_LA + " 請輸入新倍率：");
                if (!(temp19 == null || temp19.equals(""))) {
                    ConfigRate.RATE_LA = (double) Integer.valueOf(temp19).intValue();
                    World.get().broadcastServerMessage("正義值倍率變更為：" + ConfigRate.RATE_LA + "倍。");
                    addConsol(" 正義值倍率變更為：" + ConfigRate.RATE_LA + "倍。");
                }
            } catch (NumberFormatException e12) {
                JOptionPane.showMessageDialog(this, "請輸入整數!");
            }
        } else if (command.equals("屬性強化倍率(I)")) {
            try {
                String temp20 = JOptionPane.showInputDialog("當前屬性強化倍率(%)：" + ConfigRate.ATTR_ENCHANT_CHANCE + " 請輸入新倍率：");
                if (!(temp20 == null || temp20.equals(""))) {
                    ConfigRate.ATTR_ENCHANT_CHANCE = Integer.valueOf(temp20).intValue();
                    World.get().broadcastServerMessage("屬性強化倍率(%)變更為：" + ConfigRate.ATTR_ENCHANT_CHANCE + "%。");
                    addConsol(" 屬性強化倍率變更為：" + ConfigRate.ATTR_ENCHANT_CHANCE + "%。");
                }
            } catch (NumberFormatException e13) {
                JOptionPane.showMessageDialog(this, "請輸入整數!");
            }
        } else if (command.equals("防具強化倍率(J)")) {
            try {
                String temp21 = JOptionPane.showInputDialog("當前防具強化倍率(%)：" + ConfigRate.ENCHANT_CHANCE_ARMOR + " 請輸入新倍率：");
                if (!(temp21 == null || temp21.equals(""))) {
                    ConfigRate.ENCHANT_CHANCE_ARMOR = Integer.valueOf(temp21).intValue();
                    World.get().broadcastServerMessage("防具強化倍率(%)變更為：" + ConfigRate.ENCHANT_CHANCE_ARMOR + "%。");
                    addConsol(" 防具強化倍率變更為：" + ConfigRate.ENCHANT_CHANCE_ARMOR + "%。");
                }
            } catch (NumberFormatException e14) {
                JOptionPane.showMessageDialog(this, "請輸入整數!");
            }
        } else if (command.equals("武器強化倍率(K)")) {
            try {
                String temp25 = JOptionPane.showInputDialog("當前武器強化倍率(%)：" + ConfigRate.ENCHANT_CHANCE_WEAPON + " 請輸入新倍率：");
                if (!(temp25 == null || temp25.equals(""))) {
                    ConfigRate.ENCHANT_CHANCE_WEAPON = Integer.valueOf(temp25).intValue();
                    World.get().broadcastServerMessage("武器強化倍率(%)變更為：" + ConfigRate.ENCHANT_CHANCE_WEAPON + "%。");
                    addConsol(" 武器強化倍率變更為：" + ConfigRate.ENCHANT_CHANCE_WEAPON + "%。");
                }
            } catch (NumberFormatException e15) {
                JOptionPane.showMessageDialog(this, "請輸入整數!");
            }
        } else if (command.equals("經驗倍率(D)")) {
            try {
                String temp26 = JOptionPane.showInputDialog("當前經驗倍率：" + ConfigRate.RATE_XP + " 請輸入新倍率：");
                if (!(temp26 == null || temp26.equals(""))) {
                    ConfigRate.RATE_XP = (double) Integer.valueOf(temp26).intValue();
                    World.get().broadcastServerMessage("經驗倍率變更為：" + ConfigRate.RATE_XP + "倍。");
                    addConsol(" 經驗倍率變更為：" + ConfigRate.RATE_XP + "倍。");
                }
            } catch (NumberFormatException e16) {
                JOptionPane.showMessageDialog(this, "請輸入整數!");
            }
        } else if (command.equals("金幣倍率(A)")) {
            try {
                String temp27 = JOptionPane.showInputDialog("當前金幣倍率：" + ConfigRate.RATE_DROP_ADENA + " 請輸入新倍率：");
                if (!(temp27 == null || temp27.equals(""))) {
                    ConfigRate.RATE_DROP_ADENA = (double) Integer.valueOf(temp27).intValue();
                    World.get().broadcastServerMessage("金幣倍率變更為：" + ConfigRate.RATE_DROP_ADENA + "倍。");
                    addConsol(" 金幣倍率變更為：" + ConfigRate.RATE_DROP_ADENA + "倍。");
                }
            } catch (NumberFormatException e17) {
                JOptionPane.showMessageDialog(this, "請輸入整數!");
            }
        } else if (command.equals("掉寶倍率(B)")) {
            try {
                String temp28 = JOptionPane.showInputDialog("當前掉寶倍率：" + ConfigRate.RATE_DROP_ITEMS + " 請輸入新倍率：");
                if (!(temp28 == null || temp28.equals(""))) {
                    ConfigRate.RATE_DROP_ITEMS = (double) Integer.valueOf(temp28).intValue();
                    World.get().broadcastServerMessage("掉寶倍率變更為：" + ConfigRate.RATE_DROP_ITEMS + "倍。");
                    addConsol(" 掉寶倍率變更為：" + ConfigRate.RATE_DROP_ITEMS + "倍。");
                }
            } catch (NumberFormatException e18) {
                JOptionPane.showMessageDialog(this, "請輸入整數!");
            }
        } else if (command.equals("友好度倍率(C)")) {
            try {
                String temp29 = JOptionPane.showInputDialog("當前友好度倍率：" + ConfigRate.RATE_KARMA + " 請輸入新倍率：");
                if (!(temp29 == null || temp29.equals(""))) {
                    ConfigRate.RATE_KARMA = (double) Integer.valueOf(temp29).intValue();
                    World.get().broadcastServerMessage("友好度倍率變更為：" + ConfigRate.RATE_KARMA + "倍。");
                    addConsol(" 友好度倍率變更為：" + ConfigRate.RATE_KARMA + "倍。");
                }
            } catch (NumberFormatException e19) {
                JOptionPane.showMessageDialog(this, "請輸入整數!");
            }
        } else if (command.equals("負重倍率(E)")) {
            try {
                String temp30 = JOptionPane.showInputDialog("當前負重倍率：" + ConfigRate.RATE_WEIGHT_LIMIT + " 請輸入新倍率：");
                if (!(temp30 == null || temp30.equals(""))) {
                    ConfigRate.RATE_WEIGHT_LIMIT = (double) Integer.valueOf(temp30).intValue();
                    World.get().broadcastServerMessage("負重倍率變更為：" + ConfigRate.RATE_WEIGHT_LIMIT + "倍。");
                    addConsol(" 負重倍率變更為：" + ConfigRate.RATE_WEIGHT_LIMIT + "倍。");
                }
            } catch (NumberFormatException e20) {
                JOptionPane.showMessageDialog(this, "請輸入整數!");
            }
        } else if (command.equals("寵物經驗倍率(G)")) {
            try {
                String temp31 = JOptionPane.showInputDialog("當前寵物經驗倍率：" + ConfigRate.RATE_PET_XP + " 請輸入新倍率：");
                if (!(temp31 == null || temp31.equals(""))) {
                    ConfigRate.RATE_PET_XP = (double) Integer.valueOf(temp31).intValue();
                    World.get().broadcastServerMessage("寵物經驗倍率變更為：" + ConfigRate.RATE_PET_XP + "倍。");
                    addConsol(" 寵物經驗倍率變更為：" + ConfigRate.RATE_PET_XP + "倍。");
                }
            } catch (NumberFormatException e21) {
                JOptionPane.showMessageDialog(this, "請輸入整數!");
            }
        } else if (command.equals("寵物負重倍率(H)")) {
            try {
                String temp33 = JOptionPane.showInputDialog("當前寵物負重倍率：" + ConfigRate.RATE_WEIGHT_LIMIT_PET + " 請輸入新倍率：");
                if (!(temp33 == null || temp33.equals(""))) {
                    ConfigRate.RATE_WEIGHT_LIMIT_PET = (double) Integer.valueOf(temp33).intValue();
                    World.get().broadcastServerMessage("寵物負重倍率變更為：" + ConfigRate.RATE_WEIGHT_LIMIT_PET + "倍。");
                    addConsol(" 寵物負重倍率變更為：" + ConfigRate.RATE_WEIGHT_LIMIT_PET + "倍。");
                }
            } catch (NumberFormatException e22) {
                JOptionPane.showMessageDialog(this, "請輸入整數!");
            }
        } else if (command.equals("商店販賣倍率(L)")) {
            try {
                String temp34 = JOptionPane.showInputDialog("當前商店販賣倍率：" + ConfigRate.RATE_SHOP_SELLING_PRICE + " 請輸入新倍率：");
                if (!(temp34 == null || temp34.equals(""))) {
                    ConfigRate.RATE_SHOP_SELLING_PRICE = (double) Integer.valueOf(temp34).intValue();
                    World.get().broadcastServerMessage("商店販賣倍率變更為：" + ConfigRate.RATE_SHOP_SELLING_PRICE + "倍。");
                    addConsol(" 商店販賣倍率變更為：" + ConfigRate.RATE_SHOP_SELLING_PRICE + "倍。");
                }
            } catch (NumberFormatException e23) {
                JOptionPane.showMessageDialog(this, "請輸入整數!");
            }
        } else if (command.equals("商店收購倍率(M)")) {
            try {
                String temp35 = JOptionPane.showInputDialog("當前商店收購倍率：" + ConfigRate.RATE_SHOP_PURCHASING_PRICE + " 請輸入新倍率：");
                if (!(temp35 == null || temp35.equals(""))) {
                    ConfigRate.RATE_SHOP_PURCHASING_PRICE = (double) Integer.valueOf(temp35).intValue();
                    World.get().broadcastServerMessage("商店收購倍率變更為：" + ConfigRate.RATE_SHOP_PURCHASING_PRICE + "倍。");
                    addConsol(" 商店收購倍率變更為：" + ConfigRate.RATE_SHOP_PURCHASING_PRICE + "倍。");
                }
            } catch (NumberFormatException e24) {
                JOptionPane.showMessageDialog(this, "請輸入整數!");
            }
        } else if (command.equals("殺死所有玩家(D)")) {
            for (L1PcInstance tg : World.get().getAllPlayers()) {
                if (tg != null) {
                    tg.setCurrentHp(0);
                    tg.death(null);
                }
            }
        } else if (command.equals("設定關閉伺服器(E)")) {
            try {
                String temp36 = JOptionPane.showInputDialog("請輸入幾秒重後重開!");
                if (!(temp36 == null || temp36.equals(""))) {
                    int second = Integer.valueOf(temp36).intValue();
                    if (second == 0) {
                        closeServer();
                    }
                    Shutdown.getInstance().startShutdown(null, second, true);
                    World.get().broadcastServerMessage("伺服器將於(" + second + ")秒鐘後關閉伺服器!");
                    addWorldChat("管理器", "伺服器將於(" + second + ")秒鐘後關閉伺服器!");
                }
            } catch (NumberFormatException e25) {
                JOptionPane.showMessageDialog(this, "請輸入整數!");
            }
        } else if (command.equals("終極祝福(B)")) {
            int[] allBuffSkill = {14, 26, 42, 48, 55, 68, 79, 88, 89, 90, 98, L1SkillId.BURNING_SPIRIT, 104, 105, 106, 111, L1SkillId.GLOWING_AURA, 117, 129, 137, L1SkillId.ELEMENTAL_PROTECTION, L1SkillId.AQUA_PROTECTER, L1SkillId.BURNING_WEAPON, L1SkillId.IRON_SKIN, L1SkillId.EXOTIC_VITALIZE, 170, L1SkillId.ELEMENTAL_FIRE, L1SkillId.SOUL_OF_FLAME, L1SkillId.ADDITIONAL_FIRE};
            for (L1PcInstance targetpc : World.get().getAllPlayers()) {
                L1BuffUtil.haste(targetpc, 3600000);
                L1BuffUtil.brave(targetpc, 3600000);
                for (int element : allBuffSkill) {
                    if (element == 26 || element == 42) {
                        new L1SkillUse().handleCommands(targetpc, element, targetpc.getId(), targetpc.getX(), targetpc.getY(), SkillsTable.get().getTemplate(element).getBuffDuration(), 4);
                    } else {
                        new L1SkillUse().handleCommands(targetpc, element, targetpc.getId(), targetpc.getX(), targetpc.getY(), SkillsTable.get().getTemplate(element).getBuffDuration(), 4);
                    }
                }
                targetpc.sendPackets(new S_ServerMessage(166, "奇緣祝福降臨人世,全體玩家得到祝福GM是個大好人"));
            }
        } else if (command.equals("全體復活補血魔(C)")) {
            for (L1PcInstance tg2 : World.get().getAllPlayers()) {
                if (tg2.getCurrentHp() != 0 || !tg2.isDead()) {
                    tg2.sendPackets(new S_SystemMessage("GM幫你治癒嚕。"));
                    tg2.broadcastPacketX10(new S_SkillSound(tg2.getId(), 832));
                    tg2.sendPackets(new S_SkillSound(tg2.getId(), 832));
                    tg2.setCurrentHp(tg2.getMaxHp());
                    tg2.setCurrentMp(tg2.getMaxMp());
                } else {
                    tg2.sendPackets(new S_SystemMessage("GM幫你復活嚕。"));
                    tg2.broadcastPacketX10(new S_SkillSound(tg2.getId(), 3944));
                    tg2.sendPackets(new S_SkillSound(tg2.getId(), 3944));
                    tg2.setTempID(tg2.getId());
                    tg2.sendPackets(new S_Message_YN(322, ""));
                }
            }
        } else if (command.equals("血盟小屋結標(S)")) {
            try {
                String temp37 = JOptionPane.showInputDialog("請輸入分鐘。");
                if (!(temp37 == null || temp37.equals(""))) {
                    Calendar DeadTime = getRealTime();
                    DeadTime.add(12, Integer.parseInt(temp37));
                    for (L1AuctionBoardTmp board : AuctionBoardReading.get().getAuctionBoardTableList().values()) {
                        board.setDeadline(DeadTime);
                        AuctionBoardReading.get().updateAuctionBoard(board);
                    }
                    World.get().broadcastPacketToAll(new S_SystemMessage("血盟小屋將在" + temp37 + "分鐘後結標，需要購買的請盡快下標。"));
                    addConsol("管理器：血盟小屋將在" + temp37 + "分鐘後結標，需要購買的請盡快下標。");
                }
            } catch (NumberFormatException e26) {
                JOptionPane.showMessageDialog(this, "請輸入整數!");
            }
        } else if (command.equals("全掉落物重讀(D)")) {
            DropTable.get().load();
            DropMapTable.get().load();
            DropMobTable.get();
            addConsol("[droplist]+[droplist_map]+[droplist_mob]資料庫資料更新完畢。");
        } else if (command.equals("全商店資訊重讀(S)")) {
            ShopTable.get().restshop();
            ShopCnTable.get().restshopCn();
            addConsol("[shop]+[shop_cn]+[shop_rates]資料庫資料更新完畢。");
        } else if (command.equals("寶箱資料重讀(C)")) {
            ItemBoxTable.get().load();
            Item_Box_Table.get().load();
            addConsol("┌etcitem_box┬etcitem_box_key┬etcitem_box_questit┬emetcitem_boxs┐資料更新完畢。");
        } else if (command.equals("武器/防具/道具資料重讀(I)")) {
            try {
                ItemTable.get().load();
                WeaponSkillTable.get().load();
                ArmorSetTable.get().load();
                addConsol("[armor]+[armor_set]+[etcitem]+[weapon]+[weapon_skill]資料庫資料更新完畢。");
            } catch (Throwable ex) {
                ex.printStackTrace();
            }

        } else if (command.equals("NPC資料重讀(N)")) {
            NpcTable.get().load();
            NPCTalkDataTable.get().load();
            addConsol("NPC資料更新完畢。");
        } else if (command.equals("陷阱資料重讀(M)")) {
            TrapsSpawn.get().reloadTraps();
            addConsol("陷阱資料更新完畢。");
        } else if (command.equals("全公告資料重讀(Y)")) {
            ItemMsgTable.get().load();
            ItemBsgTable.get().load();
            Item_Make_Table.get().load();
            Item_Poly_Table.get().load();
            Item_Dolls_Table.get().load();
            addConsol("全公告資料更新完畢。");
        } else if (command.equals("停止-攻城戰(G)")) {
            try {
                String temp38 = JOptionPane.showInputDialog("請輸入-停止-攻城戰代號→┌1:肯特城┐┌2:妖魔城┐┌3:風木城┐┌4:奇岩城┐┌5:海音城┐┌6:侏儒城┐┌7:亞丁城┐←。");
                if (!(temp38 == null || temp38.equals(""))) {
                    int castleId = Integer.valueOf(temp38).intValue();
                    String name = null;
                    Calendar calendar = PerformanceTimer.getRealTime();
                    for (L1War war : WorldWar.get().getWarList()) {
                        if (war.get_castleId() == 0) {
                            L1Castle castle = CastleReading.get().getCastleTable(war.get_castleId());
                            calendar.add(ConfigAlt.ALT_WAR_TIME_UNIT, -(ConfigAlt.ALT_WAR_TIME * 2));
                            castle.setWarTime(calendar);
                            ServerWarExecutor.get().setWarTime(war.get_castleId(), calendar);
                            ServerWarExecutor.get().setEndWarTime(war.get_castleId(), calendar);
                        }
                    }
                    switch (castleId) {
                        case 1:
                            castleId = 1;
                            calendar.add(11, -4);
                            name = "肯特城";
                            break;
                        case 2:
                            castleId = 2;
                            calendar.add(11, -4);
                            name = "妖魔城";
                            break;
                        case 3:
                            castleId = 3;
                            calendar.add(11, -4);
                            name = "風木城";
                            break;
                        case 4:
                            castleId = 4;
                            calendar.add(11, -4);
                            name = "奇岩城";
                            break;
                        case 5:
                            castleId = 5;
                            calendar.add(11, -4);
                            name = "海音城";
                            break;
                        case 6:
                            castleId = 6;
                            calendar.add(11, -4);
                            name = "侏儒城";
                            break;
                        case 7:
                            castleId = 7;
                            calendar.add(11, -4);
                            name = "亞丁城";
                            break;
                    }
                    if (castleId != 0) {
                        CastleReading.get().getCastleTable(castleId).setWarTime(calendar);
                        ServerWarExecutor.get().setWarTime(castleId, calendar);
                        ServerWarExecutor.get().setEndWarTime(castleId, calendar);
                    }
                    if (0 != 0) {
                        World.get().broadcastServerMessage("準備-停止：┌" + name + "┐攻城戰。");
                        addConsol("準備-停止：┌" + name + "┐攻城戰。");
                    }
                }
            } catch (NumberFormatException e27) {
                JOptionPane.showMessageDialog(this, "請輸入整數!");
            }
        } else if (command.equals("啟動-攻城戰(F)")) {
            try {
                String temp39 = JOptionPane.showInputDialog("請輸入-啟動-攻城戰代號→┌1:肯特城┐┌2:妖魔城┐┌3:風木城┐┌4:奇岩城┐┌5:海音城┐┌6:侏儒城┐┌7:亞丁城┐←。");
                if (!(temp39 == null || temp39.equals(""))) {
                    int castleId2 = Integer.valueOf(temp39).intValue();
                    String name2 = null;
                    boolean start = false;
                    Calendar calendar2 = PerformanceTimer.getRealTime();
                    for (L1War war2 : WorldWar.get().getWarList()) {
                        if (war2.get_castleId() == 0) {
                            war2.ceaseWar();
                        }
                    }
                    switch (castleId2) {
                        case 1:
                            castleId2 = 1;
                            start = true;
                            name2 = "肯特城";
                            break;
                        case 2:
                            castleId2 = 2;
                            start = true;
                            name2 = "妖魔城";
                            break;
                        case 3:
                            castleId2 = 3;
                            start = true;
                            name2 = "風木城";
                            break;
                        case 4:
                            castleId2 = 4;
                            start = true;
                            name2 = "奇岩城";
                            break;
                        case 5:
                            castleId2 = 5;
                            start = true;
                            name2 = "海音城";
                            break;
                        case 6:
                            castleId2 = 6;
                            start = true;
                            name2 = "侏儒城";
                            break;
                        case 7:
                            castleId2 = 7;
                            start = true;
                            name2 = "亞丁城";
                            break;
                    }
                    if (castleId2 != 0) {
                        CastleReading.get().getCastleTable(castleId2).setWarTime(calendar2);
                        ServerWarExecutor.get().setWarTime(castleId2, calendar2);
                        ServerWarExecutor.get().setEndWarTime(castleId2, calendar2);
                    }
                    if (start) {
                        World.get().broadcastServerMessage("準備-啟動：┌" + name2 + "┐攻城戰。");
                        addConsol("準備-啟動：┌" + name2 + "┐攻城戰。");
                    }
                }
            } catch (NumberFormatException e28) {
                JOptionPane.showMessageDialog(this, "請輸入整數!");
            }
        } else if (command.equals("全體發送背包道具(N)")) {
            try {
                String temp40 = JOptionPane.showInputDialog("請輸入道具編號!");
                String temp110 = JOptionPane.showInputDialog("請輸入該道具數量!");
                String temp210 = JOptionPane.showInputDialog("請輸入該道具強化值!");
                if (!(temp40 == null || temp40.equals(""))) {
                    int itemid4 = Integer.parseInt(temp40, 10);
                    int count5 = Integer.parseInt(temp110, 10);
                    int enchant3 = Integer.parseInt(temp210, 10);
                    for (L1PcInstance target15 : World.get().getAllPlayers()) {
                        L1ItemInstance item6 = ItemTable.get().createItem(itemid4);
                        if (item6 != null) {
                            if (item6.isStackable()) {
                                item6.setCount((long) count5);
                                item6.setEnchantLevel(enchant3);
                                item6.setIdentified(true);
                            } else {
                                item6.setCount(serialVersionUID);
                                item6.setEnchantLevel(enchant3);
                                item6.setIdentified(true);
                            }
                            if (item6 != null) {
                                if (target15.getInventory().checkAddItem(item6, (long) count5) == 0) {
                                    target15.getInventory().storeItem(item6);
                                } else {
                                    World.get().getInventory(target15.getX(), target15.getY(), target15.getMapId()).storeItem(item6);
                                }
                                World.get().broadcastPacketToAll(new S_BlueMessage(166, "\\f3遊戲管理者全體發送背包道具 " + item6.getName() + " " + count5 + "個"));
                                target15.sendPackets(new S_SystemMessage("遊戲管理者全體發送背包道具 " + item6.getName() + " " + count5 + "個。"));
                            }
                            addConsol("全體發送背包道具：" + item6.getName() + " " + count5 + "個 給全體玩家：" + ((String) this.DTM.getValueAt(this.select, 1)) + " 了。");
                        } else {
                            return;
                        }
                    }
                }
            } catch (NumberFormatException e29) {
                JOptionPane.showMessageDialog(this, "請輸入整數!");
            }
        } else if (command.equals("全體發送道具到倉庫(X)")) {
            try {
                String temp41 = JOptionPane.showInputDialog("請輸入道具編號!");
                String temp111 = JOptionPane.showInputDialog("請輸入該道具數量!");
                String temp211 = JOptionPane.showInputDialog("請輸入該道具強化值!");
                if (!(temp41 == null || temp41.equals(""))) {
                    int itemid5 = Integer.parseInt(temp41, 10);
                    int count6 = Integer.parseInt(temp111, 10);
                    int enchant4 = Integer.parseInt(temp211, 10);
                    for (L1PcInstance target16 : World.get().getAllPlayers()) {
                        L1ItemInstance item7 = ItemTable.get().createItem(itemid5);
                        if (item7 != null) {
                            if (item7.isStackable()) {
                                item7.setCount((long) count6);
                                item7.setEnchantLevel(enchant4);
                                item7.setIdentified(true);
                            } else {
                                item7.setCount(serialVersionUID);
                                item7.setEnchantLevel(enchant4);
                                item7.setIdentified(true);
                            }
                            if (item7 != null) {
                                if (target16.getInventory().checkAddItem(item7, (long) count6) == 0) {
                                    DwarfReading.get().insertItem(target16.getAccountName(), item7);
                                }
                                target16.getDwarfInventory().loadItems();
                                World.get().broadcastPacketToAll(new S_BlueMessage(166, "\\f3遊戲管理者全體發送道具到倉庫： " + item7.getName() + " " + count6 + "個"));
                                target16.sendPackets(new S_SystemMessage("遊戲管理者全體發送道具到倉庫： " + item7.getName() + " " + count6 + "個。"));
                            }
                            addConsol("全體發送道具到倉庫：" + item7.getName() + " " + count6 + "個 給全體玩家：" + ((String) this.DTM.getValueAt(this.select, 1)) + " 了。");
                        } else {
                            return;
                        }
                    }
                }
            } catch (NumberFormatException e30) {
                JOptionPane.showMessageDialog(this, "請輸入整數!");
            }
        } else if (command.equals("全體變身(Z)")) {
            try {
                String temp42 = JOptionPane.showInputDialog("請輸入近戰職業變身的代號。");
                String temp112 = JOptionPane.showInputDialog("請輸入遠戰職業變身的代號。");
                if (!(temp42 == null || temp42.equals(""))) {
                    int PolyID = Integer.valueOf(temp42).intValue();
                    int Poly_0 = Integer.valueOf(temp42).intValue();
                    int Poly_1 = Integer.valueOf(temp112).intValue();
                    for (L1PcInstance pc : World.get().getAllPlayers()) {
                        switch (pc.getType()) {
                            case 0:
                            case 1:
                            case 3:
                            case 4:
                            case 5:
                            case 6:
                                PolyID = Poly_0;
                                break;
                            case 2:
                                PolyID = Poly_1;
                                break;
                        }
                        L1PolyMorph.doPoly(pc, PolyID, 1800, 1);
                    }
                }
            } catch (NumberFormatException e31) {
                JOptionPane.showMessageDialog(this, "請輸入整數!");
            }
        }
    }

    private void angel() {
        for (L1PcInstance pc : World.get().getAllPlayers()) {
            if (pc.hasSkillEffect(71)) {
                pc.sendPackets(new S_ServerMessage(698));
                return;
            }
            if (pc.hasSkillEffect(78)) {
                pc.killSkillEffectTimer(78);
                pc.startHpRegeneration();
                pc.startMpRegeneration();
                pc.startMpRegeneration();
            }
            if (pc.hasSkillEffect(L1SkillId.STATUS_ELFBRAVE)) {
                pc.killSkillEffectTimer(L1SkillId.STATUS_ELFBRAVE);
                pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0));
                pc.broadcastPacketAll(new S_SkillBrave(pc.getId(), 0, 0));
                pc.setBraveSpeed(0);
            }
            if (pc.hasSkillEffect(52)) {
                pc.killSkillEffectTimer(52);
                pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0));
                pc.broadcastPacketAll(new S_SkillBrave(pc.getId(), 0, 0));
                pc.setBraveSpeed(0);
            }
            if (pc.hasSkillEffect(101)) {
                pc.killSkillEffectTimer(101);
                pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0));
                pc.broadcastPacketAll(new S_SkillBrave(pc.getId(), 0, 0));
                pc.setBraveSpeed(0);
            }
            if (pc.hasSkillEffect(150)) {
                pc.killSkillEffectTimer(150);
                pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0));
                pc.broadcastPacketAll(new S_SkillBrave(pc.getId(), 0, 0));
                pc.setBraveSpeed(0);
            }
            if (pc.hasSkillEffect(L1SkillId.STATUS_RIBRAVE)) {
                pc.killSkillEffectTimer(L1SkillId.STATUS_RIBRAVE);
                pc.setBraveSpeed(0);
            }
            pc.sendPackets(new S_SkillBrave(pc.getId(), 1, 3600));
            pc.broadcastPacketAll(new S_SkillBrave(pc.getId(), 1, 0));
            pc.sendPackets(new S_SkillSound(pc.getId(), 751));
            pc.broadcastPacketAll(new S_SkillSound(pc.getId(), 751));
            pc.setSkillEffect(L1SkillId.STATUS_BRAVE, 3600000);
            pc.setBraveSpeed(1);
            pc.setDrink(false);
            if (pc.hasSkillEffect(43)) {
                pc.killSkillEffectTimer(43);
                pc.sendPackets(new S_SkillHaste(pc.getId(), 0, 0));
                pc.broadcastPacketAll(new S_SkillHaste(pc.getId(), 0, 0));
                pc.setMoveSpeed(0);
            } else if (pc.hasSkillEffect(54)) {
                pc.killSkillEffectTimer(54);
                pc.sendPackets(new S_SkillHaste(pc.getId(), 0, 0));
                pc.broadcastPacketAll(new S_SkillHaste(pc.getId(), 0, 0));
                pc.setMoveSpeed(0);
            } else if (pc.hasSkillEffect(L1SkillId.STATUS_HASTE)) {
                pc.killSkillEffectTimer(L1SkillId.STATUS_HASTE);
                pc.sendPackets(new S_SkillHaste(pc.getId(), 0, 0));
                pc.broadcastPacketAll(new S_SkillHaste(pc.getId(), 0, 0));
                pc.setMoveSpeed(0);
            }
            if (pc.hasSkillEffect(29)) {
                pc.killSkillEffectTimer(29);
                pc.sendPackets(new S_SkillHaste(pc.getId(), 0, 0));
                pc.broadcastPacketAll(new S_SkillHaste(pc.getId(), 0, 0));
            } else if (pc.hasSkillEffect(76)) {
                pc.killSkillEffectTimer(76);
                pc.sendPackets(new S_SkillHaste(pc.getId(), 0, 0));
                pc.broadcastPacketAll(new S_SkillHaste(pc.getId(), 0, 0));
            } else if (pc.hasSkillEffect(L1SkillId.ENTANGLE)) {
                pc.killSkillEffectTimer(L1SkillId.ENTANGLE);
                pc.sendPackets(new S_SkillHaste(pc.getId(), 0, 0));
                pc.broadcastPacketAll(new S_SkillHaste(pc.getId(), 0, 0));
            } else {
                pc.sendPackets(new S_SkillHaste(pc.getId(), 1, 3600));
                pc.broadcastPacketAll(new S_SkillHaste(pc.getId(), 1, 0));
                pc.setMoveSpeed(1);
                pc.setSkillEffect(L1SkillId.STATUS_HASTE, 3600000);
            }
            new L1SkillUse().handleCommands(pc, 42, pc.getId(), pc.getX(), pc.getY(), 3600, 4);
            new L1SkillUse().handleCommands(pc, 26, pc.getId(), pc.getX(), pc.getY(), 3600, 4);
            new L1SkillUse().handleCommands(pc, 79, pc.getId(), pc.getX(), pc.getY(), 3600, 4);
            pc.setCurrentHp(pc.getMaxHp());
            pc.setCurrentMp(pc.getMaxMp());
        }
        World.get().broadcastServerMessage("大天使祝福降臨!所有玩家獲得狀態1小時!");
    }

    private void saveChatData(boolean bool) {
        String date = " " + new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        new File("管理器對話紀錄").mkdir();
        try {
            FileOutputStream fos = new FileOutputStream("管理器對話紀錄/指令頻道" + date + ".txt");
            fos.write(this.TA_Consol.getText().getBytes());
            fos.close();
            FileOutputStream fos2 = new FileOutputStream("管理器對話紀錄/所有頻道" + date + ".txt");
            fos2.write(this.TA_AllChat.getText().getBytes());
            fos2.close();
            FileOutputStream fos3 = new FileOutputStream("管理器對話紀錄/世界頻道" + date + ".txt");
            fos3.write(this.TA_World.getText().getBytes());
            fos3.close();
            FileOutputStream fos4 = new FileOutputStream("管理器對話紀錄/血盟頻道" + date + ".txt");
            fos4.write(this.TA_Clan.getText().getBytes());
            fos4.close();
            FileOutputStream fos5 = new FileOutputStream("管理器對話紀錄/一般頻道" + date + ".txt");
            fos5.write(this.TA_Normal.getText().getBytes());
            fos5.close();
            FileOutputStream fos6 = new FileOutputStream("管理器對話紀錄/隊伍頻道" + date + ".txt");
            fos6.write(this.TA_Team.getText().getBytes());
            fos6.close();
            FileOutputStream fos7 = new FileOutputStream("管理器對話紀錄/密語頻道" + date + ".txt");
            fos7.write(this.TA_Private.getText().getBytes());
            fos7.close();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    private void start(L1PcInstance target, String info) {
        IpReading.get().add(target.getAccountName(), info);
        target.getNetConnection().kick();
    }

    public static void items(L1PcInstance pc) {
        try {
            CharacterTable.restoreInventory(pc);
            if (pc.getInventory().getItems().size() > 0) {
                pc.sendPackets(new S_InvList(pc.getInventory().getItems()));
            }
        } catch (Exception ignored) {
        }
    }

    private void processEvent(MouseEvent e) {
        if ((e.getModifiers() & 4) != 0) {
            this.T_Player.dispatchEvent(new MouseEvent(e.getComponent(), e.getID(), e.getWhen(), (e.getModifiers() - 4) | 16, e.getX(), e.getY(), e.getClickCount(), false));
        }
    }

    private Calendar getRealTime() {
        return Calendar.getInstance(TimeZone.getTimeZone(Config.TIME_ZONE));
    }
}
