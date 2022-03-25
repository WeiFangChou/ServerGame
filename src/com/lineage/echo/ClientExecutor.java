/* Decompiler 49ms, total 326ms, lines 312 */
package com.lineage.echo;

import com.lineage.commons.system.IpAttackCheck;
import com.lineage.commons.system.LanSecurityManager;
import com.lineage.config.Config;
import com.lineage.config.ConfigIpCheck;
import com.lineage.echo.encryptions.Encryption;
import com.lineage.list.OnlineUser;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_Disconnect;
import com.lineage.server.templates.L1Account;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.utils.StreamUtil;
import com.lineage.server.utils.SystemUtil;

import java.io.IOException;
import java.math.BigInteger;
import java.net.Socket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ClientExecutor extends OpcodesClient implements Runnable {
    private static final Log _log = LogFactory.getLog(ClientExecutor.class);
    private Socket _csocket = null;
    private L1Account _account = null;
    private L1PcInstance _activeChar = null;
    private StringBuilder _ip = null;
    private StringBuilder _mac = null;
    private int _kick = 0;
    private boolean _isStrat = true;
    private EncryptExecutor _encrypt;
    private DecryptExecutor _decrypt;
    private PacketHandlerExecutor _handler;
    private Encryption _keys;
    private int _error = -1;
    private static final int M = 3;
    private static final int O = 2;
    private int _saveInventory = 0;
    private int _savePc = 0;
    public int _xorByte = -16;
    public long _authdata;

    public ClientExecutor(Socket socket) throws IOException {
        this._csocket = socket;
        if (Config.LOGINS_TO_AUTOENTICATION) {
            int randomNumber = (int) (Math.random() * 9.0E8D) + 255;
            this._xorByte = randomNumber % 255 + 1;
            this._authdata = (new BigInteger(Integer.toString(randomNumber))).modPow(new BigInteger(Config.RSA_KEY_E), new BigInteger(Config.RSA_KEY_N)).longValue();
        }

        this._ip = (new StringBuilder()).append(socket.getInetAddress().getHostAddress());
        this._handler = new PacketHandler(this);
        this._keys = new Encryption();
        this._decrypt = new DecryptExecutor(this, socket.getInputStream());
        this._encrypt = new EncryptExecutor(this, socket.getOutputStream());
    }

    public void start() {
    }

    public void run() {
        PacketHc m = new PacketHc(this, M);
        GeneralThreadPool.get().schedule(m, 0);
        PacketHc o = new PacketHc(this, O);
        GeneralThreadPool.get().schedule(o, 0);
        this.set_savePc(Config.AUTOSAVE_INTERVAL);
        this.set_saveInventory(Config.AUTOSAVE_INTERVAL_INVENTORY);

        try {
            this._encrypt.satrt();// 開始處理封包輸出
            this._encrypt.outStart();// 把第一個封包送出去
            boolean isEcho = false;// 完成要求接收伺服器版本(防止惡意封包發送)

            while (this._isStrat) {

                byte[] decrypt;
                try {
                    decrypt = this.readPacket();
                } catch (Exception var10) {
                    break;
                }

                if (decrypt.length > 1440) {
                    _log.warn("客戶端送出長度異常封包:" + this._ip.toString() + " 帳號:" + (this._account != null ? this._account.get_login() : "未登入"));
                    LanSecurityManager.BANIPMAP.put(this._ip.toString(), 100);
                    break;
                }

                if (this._account != null) {
                    if (!OnlineUser.get().isLan(this._account.get_login())) {
                        break;
                    }
                    if (!this._account.is_isLoad()) {
                        break;
                    }
                }

                int opcode = decrypt[0] & 0xFF;
                if (this._activeChar == null) {
                    if (opcode == 127) {
                        if (ConfigIpCheck.IPCHECKPACK) {
                            LanSecurityManager.BANIPPACK.remove(this._ip.toString());
                        }

                        isEcho = true;
                    }

                    if (!isEcho) {
                        continue;
                    }
                    _handler.handlePacket(decrypt);
                    continue;
                }
                if (!isEcho) {
                    continue;
                }
                switch (opcode) {
                    case C_OPCODE_QUITGAME:
                    case C_OPCODE_CHANGECHAR:
                    case C_OPCODE_DROPITEM:
                    case C_OPCODE_DELETEINVENTORYITEM:
                        _handler.handlePacket(decrypt);
                        break;
                    case C_OPCODE_MOVECHAR:
                        m.requestWork(decrypt);
                        break;
                    default:
                        o.requestWork(decrypt);
                }

            }
        } catch (Exception var11) {
        } finally {
            if (ConfigIpCheck.IPCHECK) {
                IpAttackCheck.SOCKETLIST.remove(this);
            }

            this.set_savePc(-1);
            this.set_saveInventory(-1);
            this.close();
        }

    }

    public void close() {
        try {
            String mac = null;
            if (this._mac != null) {
                mac = this._mac.toString();
            }

            if (this._csocket == null) {
                return;
            }

            this._kick = 0;
            if (this._account != null) {
                OnlineUser.get().remove(this._account.get_login());
            }

            if (this._activeChar != null) {
                this.quitGame();
            }

            String ipAddr = this._ip.toString();
            String account = null;
            if (this._kick < 1 && this._account != null) {
                account = this._account.get_login();
            }

            this._decrypt.stop();
            this._encrypt.stop();
            StreamUtil.close(this._csocket);
            if (ConfigIpCheck.ISONEIP) {
                LanSecurityManager.ONEIPMAP.remove(ipAddr);
            }

            this._handler = null;
            this._mac = null;
            this._ip = null;
            this._activeChar = null;
            this._account = null;
            this._decrypt = null;
            this._encrypt = null;
            this._csocket = null;
            this._keys = null;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("\n--------------------------------------------------");
            stringBuilder.append("\n       客戶端 離線: (");
            if (account != null) {
                stringBuilder.append(account + " ");
            }

            if (mac != null) {
                stringBuilder.append(" " + mac + " / ");
            }

            stringBuilder.append(ipAddr + ") 完成連線中斷!!");
            stringBuilder.append("\n--------------------------------------------------");
            _log.info(stringBuilder.toString());
            SystemUtil.printMemoryUsage(_log);
        } catch (Exception var5) {
        }

    }

    public L1Account getAccount() {
        return this._account;
    }

    public void setAccount(L1Account account) {
        this._account = account;
    }

    public String getAccountName() {
        return this._account == null ? null : this._account.get_login();
    }

    public L1PcInstance getActiveChar() {
        return this._activeChar;
    }

    public void setActiveChar(L1PcInstance pc) {
        this._activeChar = pc;
    }

    public StringBuilder getIp() {
        return this._ip;
    }

    public StringBuilder getMac() {
        return this._mac;
    }

    public boolean setMac(StringBuilder mac) {
        this._mac = mac;
        return true;
    }

    public Socket get_socket() {
        return this._csocket;
    }

    public boolean kick() {
        try {
            this._encrypt.encrypt(new S_Disconnect());
        } catch (Exception var2) {
        }

        this.quitGame();
        this._kick = 1;
        this._isStrat = false;
        this.close();
        return true;
    }

    public void quitGame() {
        try {
            if (this._activeChar == null) {
                return;
            }

            synchronized (this._activeChar) {
                QuitGame.quitGame(this._activeChar);
                this._activeChar = null;
            }
        } catch (Exception var3) {
        }

    }

    private byte[] readPacket() {
        try {
            byte[] data = null;
            data = this._decrypt.decrypt();
            return data;
        } catch (Exception var2) {

        }
        return null;
    }

    public EncryptExecutor out() {
        return this._encrypt;
    }

    public void set_keys(Encryption keys) {
        this._keys = keys;
    }

    public Encryption get_keys() {
        return this._keys;
    }

    public int get_error() {
        return this._error;
    }

    public void set_error(int error) {
        this._error = error;
        if (error >= 2) {
            this.kick();
        }

    }

    public void set_saveInventory(int saveInventory) {
        this._saveInventory = saveInventory;
    }

    public int get_saveInventory() {
        return this._saveInventory;
    }

    public void set_savePc(int savePc) {
        this._savePc = savePc;
    }

    public int get_savePc() {
        return this._savePc;
    }
}