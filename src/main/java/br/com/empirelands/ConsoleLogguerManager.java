package br.com.empirelands;

import org.bukkit.plugin.Plugin;

import java.util.logging.Level;

public class ConsoleLogguerManager {

    private static ConsoleLogguerManager instance;
    private final Plugin pl = DreamReports.getInstance();

    private ConsoleLogguerManager(){
    }

    public static ConsoleLogguerManager getInstance() {
        if(null == instance) instance = new ConsoleLogguerManager();
        return instance;
    }

    public void log(String msg){
        pl.getServer().getLogger().log(Level.INFO, msg);
    }

    public void logSevere(String msg){
        pl.getServer().getLogger().log(Level.SEVERE, msg);
    }

    public void logWarn(String msg){
        pl.getServer().getLogger().log(Level.WARNING, msg);
    }
}
