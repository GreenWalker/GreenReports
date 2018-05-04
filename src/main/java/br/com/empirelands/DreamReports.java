package br.com.empirelands;

import br.com.empirelands.commands.player.ReportCmd;
import br.com.empirelands.commands.player.ReportTabCompleter;
import br.com.empirelands.commands.player.ReporteCmd;
import br.com.empirelands.commands.staff.ReportsCmd;
import br.com.empirelands.config.ConfigHandler;
import br.com.empirelands.exception.UnSupportedVersion;
import br.com.empirelands.listeners.*;
import br.com.empirelands.listeners.events.CommandPreProcess;
import br.com.empirelands.manager.GenericUserManager;
import br.com.empirelands.menu.Menus;
import br.com.empirelands.mysql.DaoManager;
import br.com.empirelands.mysql.MySQL;
import br.com.empirelands.manager.ReportPlayerManager;
import br.com.empirelands.reflection.ReflectionUtils;
import br.com.empirelands.manager.ReportManager;
import br.com.empirelands.util.Prefix;
import br.com.empirelands.util.Util;

import br.com.empirelands.util.UtilMethods;
import br.com.empirelands.manager.DatabaseDaoManager;
import lombok.Getter;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class DreamReports extends JavaPlugin{

    @Getter
    private static DreamReports instance;
    @Getter
    private ConfigHandler langFile;
    @Getter
    private ReflectionUtils supportVersion;
    @Getter
    private GenericUserManager userManager;
    @Getter
    private DatabaseDaoManager dbManager;
    @Getter
    private ReportManager reportManager;
    @Getter
    private ReportPlayerManager reportPlayerManager;
    private UtilMethods methods;

    private boolean debugg;

    private final String version = Bukkit.getServer().getClass().getPackage().getName();

    @Override
    public void onEnable() {
        instance = this;
        setupConfig();
        this.info(Util.getInstancie().getPrefix() + " &dInicializando conexao com o banco da dados...");
        MySQL.getInstance();
        this.info(Util.getInstancie().getPrefix() + " &dConfigurando Parametros...");
        setupPlugin();
        this.info(Util.getInstancie().getPrefix() + " &ePlugin iniciado!");
    }

    @Override
    public void onDisable() {
        this.info(Util.getInstancie().getPrefix() + " &cPlugin desabilitado!");
    }

    private void setupConfig(){
        try {
            this.langFile = new ConfigHandler(this, "language.yml");
            new Prefix(langFile.getStringReplaced("prefix"));
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        if(!new File(getDataFolder(), "config.yml").exists()){
            saveDefaultConfig();
            saveResource("symbols.txt", false);
        }
    }

    public void debugInfo(String info){
        this.debugg = getConfig().getBoolean("debug");
        if(debugg)
         info("[DEBUG] - " + info);
    }

    private void setupPlugin(){
        // hook
        this.hookToMinecraftVersion();

        PluginManager pm = getServer().getPluginManager();

        dbManager = new DatabaseDaoManager(DaoManager.getInstancie());
        CacheLoader loader = new CacheLoader(dbManager);

        reportManager = new ReportManager(loader.loadReportCache());
        reportPlayerManager = new ReportPlayerManager(loader.loadReportPlayerCache());
        userManager = new GenericUserManager(loader.loadUserCache());
        Menus menus = new Menus(dbManager);
        methods = new UtilMethods(userManager, reportManager, reportPlayerManager, dbManager, supportVersion.getNmsVersion(), getLangFile());

        pm.registerEvents(new InventoryClickHandler(methods, dbManager, menus), this);
        pm.registerEvents(new ChatHandler(getLangFile(), methods), this);
        pm.registerEvents(new JoinQuitEvent(getPluginConfig(), userManager, reportPlayerManager, dbManager, methods), this);
        pm.registerEvents(new CreateReportHandler(getLangFile()), this);
        pm.registerEvents(new CloseReportHandler(supportVersion.getNmsVersion(), methods), this);
        pm.registerEvents(new SeeReportEvent(dbManager), this);
        pm.registerEvents(new CommandPreProcess(), this);

        getCommand("reportar").setExecutor(new ReportCmd(supportVersion.getNmsVersion(), reportPlayerManager, userManager, reportManager, menus, dbManager, methods));
        getCommand("reportar").setTabCompleter(new ReportTabCompleter());
        getCommand("reportes").setExecutor(new ReportsCmd(supportVersion.getNmsVersion(), reportPlayerManager, userManager, reportManager, menus, dbManager, methods));
        getCommand("reporte").setExecutor(new ReporteCmd(menus, methods));
    }


    private void hookToMinecraftVersion() {
        String version = this.version.substring(this.version.lastIndexOf(".") + 1);
        try {
            supportVersion = new ReflectionUtils(version);
            info("&dHooked to '&c" + version.replaceAll("_", ".") + "'&d spigot/bukkit version.");
        } catch (UnSupportedVersion err) {
            ConsoleLogguerManager.getInstance().logSevere("Sua versao atual do bukkit/spigot " + version + " ainda nao e suportada pelo plugin!\ndesabilitando...");
            err.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    public void info(String msg){
        getServer().getConsoleSender().sendMessage(msg.replaceAll("&", "§"));
    }

    public FileConfiguration getPluginConfig(){
        return this.getConfig();
    }

}
