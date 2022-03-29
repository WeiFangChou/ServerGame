package com.lineage.server.command;

import com.lineage.config.Config;
import com.lineage.server.command.executor.L1CommandExecutor;
import com.lineage.server.datatables.CommandsTable;
import com.lineage.server.datatables.lock.ServerGmCommandReading;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.serverpackets.S_HelpMessage;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Command;
import com.lineage.server.utils.L1SpawnUtil;
import com.lineage.server.world.World;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GMCommands {
    private static GMCommands _instance;
    private static Map<Integer, String> _lastCommands = new HashMap();
    private static final Log _log = LogFactory.getLog(GMCommands.class);

    private GMCommands() {
    }

    public static GMCommands getInstance() {
        if (_instance == null) {
            _instance = new GMCommands();
        }
        return _instance;
    }

    private String complementClassName(String className) {
        return className.contains(".") ? className : "com.lineage.server.command.executor." + className;
    }

    private void executeDatabaseCommand(String cmd, String arg) {
        try {
            if (Config.ISUBUNTU && cmd.equalsIgnoreCase("sudo")) {
                _log.info("******Linux 系統命令執行**************************");
                ubuntuCommand(String.valueOf(cmd) + " " + arg);
                _log.info("******Linux 系統命令完成**************************");
            } else if (cmd.equalsIgnoreCase("c")) {
                _log.info("系統公告: " + arg);
                World.get().broadcastPacketToAll(new S_HelpMessage(arg));
                ServerGmCommandReading.get().create(null, String.valueOf(cmd) + " " + arg);
            } else if (!cmd.equalsIgnoreCase("debug")) {
                L1Command command = CommandsTable.get().get(cmd.toLowerCase());
                if (command == null) {
                    _log.error("指令異常: 沒有這個命令(" + cmd.toLowerCase() + ")");
                } else if (!command.isSystem()) {
                    _log.error("指令異常: 這個命令必須進入遊戲才能執行(" + cmd.toLowerCase() + ")");
                } else {
                    ((L1CommandExecutor) Class.forName(complementClassName(command.getExecutorClassName())).getMethod("getInstance", new Class[0]).invoke(null, new Object[0])).execute(null, cmd.toLowerCase(), arg.toLowerCase());
                    ServerGmCommandReading.get().create(null, String.valueOf(cmd) + " " + arg);
                }
            } else if (Config.DEBUG) {
                _log.info("終止除錯模式!!");
                Config.DEBUG = false;
            } else {
                _log.info("啟用除錯模式!!");
                Config.DEBUG = true;
            }
        } catch (Exception e) {
            _log.error("管理者指令異常!", e);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    private void ubuntuCommand(String command) {
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec(command).getInputStream()));
            while (true) {
                String line = input.readLine();
                if (line != null) {
                    _log.info("Linux 系統命令執行: " + line);
                } else {
                    return;
                }
            }
        } catch (IOException e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public void handleCommands(String cmdLine) {
        StringTokenizer token = new StringTokenizer(cmdLine);
        try {
            String cmd = token.nextToken();
            String param = "";
            while (token.hasMoreTokens()) {
                param = param + token.nextToken() + ' ';
            }
            executeDatabaseCommand(cmd, param.trim());
        } catch (Exception e) {
            _log.error("系統命令空白!");
        }
    }

    private boolean executeDatabaseCommand(L1PcInstance pc, String cmd, String arg) {
        try {
            L1Command command = CommandsTable.get().get(cmd.toLowerCase());
            if (command == null) {
                return false;
            }
            if (pc.getAccessLevel() < command.getLevel()) {
                pc.sendPackets(new S_ServerMessage(74, "指令 " + cmd));
                return true;
            }
            ((L1CommandExecutor) Class.forName(complementClassName(command.getExecutorClassName())).getMethod("getInstance", new Class[0]).invoke(null, new Object[0])).execute(pc, cmd.toLowerCase(), arg.toLowerCase());
            if (pc.getAccessLevel() > 0) {
                _log.warn(String.valueOf(pc.getName()) + "管理者使用指令: " + cmd + " " + arg);
                ServerGmCommandReading.get().create(pc, String.valueOf(cmd) + " " + arg);
            }
            return true;
        } catch (Exception e) {
            _log.error("管理者指令異常!", e);
            return false;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return false;
        }
    }

    public void handleCommands(L1PcInstance gm, String cmdLine) {
        StringTokenizer token = new StringTokenizer(cmdLine);
        try {
            String cmd = token.nextToken();
            String param = "";
            while (token.hasMoreTokens()) {
                param = param + token.nextToken() + ' ';
            }
            String param2 = param.trim();
            if (cmd.equalsIgnoreCase("t")) {
                gm.setGm(false);
                L1Teleport.teleport(gm, 32707, 32846,  9000, 5, false);
                L1SpawnUtil.spawn(gm, 91268, 0, 0);
            } else if (executeDatabaseCommand(gm, cmd, param2)) {
                if (!cmd.equalsIgnoreCase("r")) {
                    _lastCommands.put(Integer.valueOf(gm.getId()), cmdLine);
                }
            } else if (cmd.equalsIgnoreCase("s")) {
                _lastCommands.put(Integer.valueOf(gm.getId()), param2);
            } else if (!cmd.equalsIgnoreCase("r")) {
                gm.sendPackets(new S_ServerMessage(329, cmd));
            } else if (!_lastCommands.containsKey(Integer.valueOf(gm.getId()))) {
                gm.sendPackets(new S_ServerMessage(261));
            } else {
                redo(gm, param2);
            }
        } catch (Exception e) {
            _log.error("管理者指令空白!");
        }
    }

    private void redo(L1PcInstance pc, String arg) {
        try {
            String lastCmd = _lastCommands.get(Integer.valueOf(pc.getId()));
            if (arg.isEmpty()) {
                pc.sendPackets(new S_ServerMessage(166, "指令 " + lastCmd + " 重複執行"));
                handleCommands(pc, lastCmd);
                return;
            }
            pc.sendPackets(new S_ServerMessage(166, "指令 " + lastCmd + " 紀錄"));
            handleCommands(pc, String.valueOf(new StringTokenizer(lastCmd).nextToken()) + " " + arg);
        } catch (Exception e) {
            pc.sendPackets(new S_ServerMessage(261));
        }
    }
}
