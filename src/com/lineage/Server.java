package com.lineage;

import com.eric.gui.J_Main;
import com.lineage.commons.system.LanSecurityManager;
import com.lineage.config.Config;
import com.lineage.config.ConfigAlt;
import com.lineage.config.ConfigBad;
import com.lineage.config.ConfigCharSetting;
import com.lineage.config.ConfigDarkElfSkill;
import com.lineage.config.ConfigDescs;
import com.lineage.config.ConfigDoll;
import com.lineage.config.ConfigElfSkill;
import com.lineage.config.ConfigIpCheck;
import com.lineage.config.ConfigKill;
import com.lineage.config.ConfigKnightSkill;
import com.lineage.config.ConfigOther;
import com.lineage.config.ConfigPoly;
import com.lineage.config.ConfigRate;
import com.lineage.config.ConfigRecord;
import com.lineage.config.ConfigSQL;
import com.lineage.config.ConfigWizardSkill;
import com.lineage.server.GameServer;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.LogManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;

public class Server {
    private static final String LOG_4J = "./config/log4j.properties";
    private static final String LOG_PROP = "./config/logging.properties";
    private static final String _back = "./back";
    private static final String _loginfo = "./loginfo";

    public static void main(String[] args) throws Exception {
        try {
            if (args[0].equalsIgnoreCase("test")) {
                Config.DEBUG = true;
            }
        } catch (Exception ignored) {
        }
        CompressFile bean = new CompressFile();
        try {
            File file = new File(_back);
            if (!file.exists()) {
                file.mkdir();
            }
            bean.zip(_loginfo, "./back/" + new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date()) + ".zip");
            String[] loginfofileList = new File(_loginfo).list();
            int length = loginfofileList.length;
            for (int i = 0; i < length; i++) {
                File readfile = new File("./loginfo/" + loginfofileList[i]);
                if (readfile.exists() && !readfile.isDirectory()) {
                    readfile.delete();
                }
            }
        } catch (IOException e2) {
            System.out.println("資料夾不存在: ./back 已經自動建立!");
        }
        boolean error = false;
        try {
            InputStream is = new BufferedInputStream(new FileInputStream(LOG_PROP));
            LogManager.getLogManager().readConfiguration(is);
            is.close();
        } catch (IOException e3) {
            System.out.println("檔案遺失: ./config/logging.properties");
            error = true;
        }
        try {
            PropertyConfigurator.configure(LOG_4J);
        } catch (Exception e4) {
            System.out.println("檔案遺失: ./config/log4j.properties");
            System.exit(0);
        }
        try {
            Config.load();
            ConfigAlt.load();
            ConfigCharSetting.load();
            ConfigOther.load();
            ConfigRate.load();
            ConfigSQL.load();
            ConfigRecord.load();
            ConfigDescs.load();
            ConfigBad.load();
            ConfigKill.load();
            ConfigWizardSkill.load();
            ConfigElfSkill.load();
            ConfigDarkElfSkill.load();
            ConfigKnightSkill.load();
            ConfigPoly.load();
            ConfigDoll.load();
            ConfigIpCheck.load();
        } catch (Exception e5) {
            System.out.println("CONFIG 資料加載異常! " + e5);
            error = true;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        Log log = LogFactory.getLog(Server.class);
        log.info("\n\r##################################################\n\r       服務器 (核心版本:3.63C/Lineage3.63C)\n\r##################################################");
        String[] fileNameList = new File("./libs").list();
        int length2 = fileNameList.length;
        for (int i2 = 0; i2 < length2; i2++) {
            String fileName = fileNameList[i2];
            if (!new File(fileName).isDirectory()) {
                log.info("加載引用JAR: " + fileName);
            }
        }
        if (error) {
            System.exit(0);
        }
        DatabaseFactoryLogin.setDatabaseSettings();
        DatabaseFactory.setDatabaseSettings();
        DatabaseFactoryLogin.get();
        DatabaseFactory.get();
        if (ConfigOther.GUI) {
            J_Main.getInstance().setVisible(true);
        }
        System.setSecurityManager(new LanSecurityManager());
        log.info("加載 安全管理器: LanSecurityManager");
        if (System.getProperties().getProperty("os.name").lastIndexOf("Linux") != -1) {
            Config.ISUBUNTU = true;
        }
        GameServer.getInstance().initialize();
    }
}
