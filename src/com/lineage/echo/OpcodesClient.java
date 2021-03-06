package com.lineage.echo;

public class OpcodesClient {
	protected static final int _seed = 994303243;
	protected static final byte[] _firstPacket = new byte[]{18, 0, -95, 11, -35, 67, 59, 71, 113, -42, 108, 96, 126, 1, 0, 4, 8, 0};
	public static final int C_OPCODE_WINDOWS = 41;
	public static final int C_OPCODE_LOGINTOSERVEROK = 75;
	public static final int C_OPCODE_MAIL = 22;
	public static final int C_OPCODE_CLIENTVERSION = 127;
	public static final int C_OPCODE_CLAN = 246;
	public static final int C_OPCODE_QUITGAME = 104;
	public static final int C_OPCODE_CHAT = 190;
	public static final int C_OPCODE_MOVECHAR = 95;
	public static final int C_OPCODE_RANK = 88;
	public static final int C_OPCODE_ATTACK = 68;
	public static final int C_OPCODE_ARROWATTACK = 247;
	public static final int C_OPCODE_LOGINTOSERVER = 131;
	public static final int C_OPCODE_KEEPALIVE = 182;
	public static final int C_OPCODE_USEITEM = 44;
	public static final int C_OPCODE_USESKILL = 115;
	public static final int C_OPCODE_AUTO = 162;
	public static final int C_OPCODE_CHARACTERCONFIG = 129;
	public static final int C_OPCODE_CHANGECHAR = 237;
	public static final int C_OPCODE_BOARD = 73;
	public static final int C_OPCODE_BOARDREAD = 59;
	public static final int C_OPCODE_CHATWHISPER = 122;
	public static final int C_OPCODE_NPCACTION = 37;
	public static final int C_OPCODE_DROPITEM = 54;
	public static final int C_OPCODE_NPCTALK = 58;
	public static final int C_OPCODE_CHATGLOBAL = 62;
	public static final int C_OPCODE_DELETEINVENTORYITEM = 209;
	public static final int C_OPCODE_PICKUPITEM = 188;
	public static final int C_OPCODE_DOOR = 199;
	public static final int C_OPCODE_RESULT = 40;
	public static final int C_OPCODE_DELETECHAR = 10;
	public static final int C_OPCODE_PLEDGE = 225;
	public static final int C_OPCODE_NEWCHAR = 253;
	public static final int C_OPCODE_TELEPORT2 = 226;
	public static final int C_OPCODE_FIX_WEAPON_LIST = 106;
	public static final int C_OPCODE_SKILLBUY = 173;
	public static final int C_OPCODE_TELEPORT = 239;
	public static final int C_OPCODE_SHOP = 16;
	public static final int C_OPCODE_CHANGEHEADING = 65;
	public static final int C_OPCODE_RESTART = 71;
	public static final int C_OPCODE_WHO = 49;
	public static final int C_OPCODE_SELECTTARGET = 155;
	public static final int C_OPCODE_PWD = 81;
	public static final int C_OPCODE_TITLE = 96;
	public static final int C_OPCODE_CHECKPK = 137;
	public static final int C_OPCODE_ADDBUDDY = 99;
	public static final int C_OPCODE_BOOKMARK = 134;
	public static final int C_OPCODE_BOOKMARKDELETE = 223;
	public static final int C_OPCODE_BOARDWRITE = 14;
	public static final int C_OPCODE_BOARDDELETE = 12;
	public static final int C_OPCODE_GIVEITEM = 244;
	public static final int C_OPCODE_PETMENU = 217;
	public static final int C_OPCODE_BUDDYLIST = 60;
	public static final int C_OPCODE_DELBUDDY = 211;
	public static final int C_OPCODE_EXIT_GHOST = 210;
	public static final int C_OPCODE_EXTCOMMAND = 157;
	public static final int C_OPCODE_BANCLAN = 222;
	public static final int C_OPCODE_AMOUNT = 109;
	public static final int C_OPCODE_EXCLUDE = 101;
	public static final int C_OPCODE_ATTR = 61;
	public static final int C_OPCODE_TRADE = 103;
	public static final int C_OPCODE_TRADEADDITEM = 241;
	public static final int C_OPCODE_TRADEADDOK = 110;
	public static final int C_OPCODE_JOINCLAN = 30;
	public static final int C_OPCODE_CREATECLAN = 154;
	public static final int C_OPCODE_TRADEADDCANCEL = 167;
	public static final int C_OPCODE_PRIVATESHOPLIST = 193;
	public static final int C_OPCODE_FIGHT = 47;
	public static final int C_OPCODE_CREATEPARTY = 166;
	public static final int C_OPCODE_CAHTPARTY = 113;
	public static final int C_OPCODE_LEAVEPARTY = 204;
	public static final int C_OPCODE_PROPOSE = 185;
	public static final int C_OPCODE_ENTERPORTAL = 249;
	public static final int C_OPCODE_PARTY = 42;
	public static final int C_OPCODE_DRAWAL = 192;
	public static final int C_OPCODE_DEPOSIT = 35;
	public static final int C_OPCODE_BANPARTY = 70;
	public static final int C_OPCODE_FISHCLICK = 26;
	public static final int C_OPCODE_EMBLEM = 107;
	public static final int C_OPCODE_SELECTLIST = 238;
	public static final int C_OPCODE_WAR = 235;
	public static final int C_OPCODE_LEAVECLANE = 121;
	public static final int C_OPCODE_SKILLBUYOK = 207;
	public static final int C_OPCODE_CHARRESET = 236;
	public static final int C_OPCODE_CASTLESECURITY = 125;
	public static final int C_OPCODE_CLAN_RECOMMEND = 228;
	public static final int C_OPCODE_COMMONCLICK = 53;
	public static final int C_OPCODE_LOGINPACKET = 57;
	public static final int C_OPCODE_CALL = 144;
	public static final int C_OPCODE_USEPETITEM = 213;
	public static final int C_OPCODE_SMS = 45;
	public static final int C_OPCODE_BOARDBACK = 221;
	public static final int C_OPCODE_SHIP = 117;
	public static final int C_OPCODE_CHANGEWARTIME = 150;
	public static final int C_OPCODE_TAXRATE = 200;
	public static final int C_OPCODE_SETCASTLESECURITY = 149;
	public static final int C_OPCODE_HIRESOLDIER = -2;
	public static final int C_OPCODE_RETURNTOLOGIN = 56;
	public static final int C_OPCODE_NEWACC = 5;
	public static final int C_OPCODE_PUTSOLDIER = 3;
	public static final int C_OPCODE_PUTHIRESOLDIER = 5;
	public static final int C_OPCODE_PUTBOWSOLDIER = 7;
	public static final int C_OPCODE_COMMONINFO = 9;
	public static final int C_OPCODE_HORUN = -4;
	public static final int C_OPCODE_HORUNOK = -5;
	public static final int C_OPCODE_SOLDIERBUY = -6;
	public static final int C_OPCODE_SOLDIERGIVE = -7;
	public static final int C_OPCODE_SOLDIERGIVEOK = -8;
	public static final int C_OPCODE_WARTIMESET = -9;
	public static final int C_OPCODE_SECURITYSTATUS = -10;
	public static final int C_OPCODE_SECURITYSTATUSSET = -11;
	public static final int C_OPCODE_HOTEL_ENTER = -12;
	public static final int C_OPCODE_ADDSERVICE = -13;
	public static final int C_OPCODE_WHISPERINCLUDE = -14;
	public static final int C_OPCODE_WARTIMELIST = -15;
}
