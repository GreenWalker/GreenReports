package br.com.empirelands.util;

import br.com.empirelands.DreamReports;
import org.bukkit.entity.Player;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Cooldown {

    private Map<String, Long> delayed;
    private static Cooldown instance;

    private Cooldown() {
        this.delayed = new HashMap<>();
    }

    public static Cooldown getInstance(){
        if(null == instance){
            instance = new Cooldown();
        }
        return instance;
    }

    public void putPlayerOnDelay(Player player){
        Date date = new Date();
        date.setSeconds(date.getSeconds() + DreamReports.getInstance().getConfig().getInt("Tempo-Para-Novo-Report"));
        delayed.put(player.getName(), date.getTime());
    }

    public void removePlayerOfDelay(Player player){
        delayed.remove(player.getName());
    }

    public boolean containsPlayerInDelay(Player player){
        return delayed.containsKey(player.getName());
    }

    public long getPlayerDelay(Player player){
        return this.delayed.get(player.getName());
    }

    public boolean afterLong(Player p){
        Date date = new Date(getPlayerDelay(p));
        Date venc = new Date();
        return venc.after(date);
    }

    public String convertMillisToSeconds(long l){
        long seconds = (l / 1000) % 60;
        return String.valueOf(seconds).replaceAll("-", "");
    }

    public String convertMillisToMinutes(long l){
        long minutes = (l / 60000) % 60;
        return String.valueOf(minutes).replaceAll("-", "");
    }

    public long getVariation(Player p){
        return new Date(getPlayerDelay(p)).getTime() - new Date().getTime();
    }

}
