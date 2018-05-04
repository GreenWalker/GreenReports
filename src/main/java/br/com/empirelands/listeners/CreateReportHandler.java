package br.com.empirelands.listeners;

import br.com.empirelands.config.ConfigHandler;
import br.com.empirelands.listeners.events.PlayerCreateReportEvent;
import br.com.empirelands.player.normal_user.GenericUser;
import com.google.common.base.Strings;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;

/**
 * Criado por Floydz.
 */
public class CreateReportHandler implements Listener {

    private ConfigHandler language;
    private HashMap<String, Object> var;

    public CreateReportHandler(ConfigHandler language) {
        this.language = language;
        this.var = new HashMap<>();
    }

    @EventHandler
    public void onCreateReport(PlayerCreateReportEvent event){
        GenericUser user = event.getReporter();
        var.put("%reporter%", user.getNick());
        var.put("%reported%", event.getReported().getNick());
        var.put("%motive%", event.getMotive());
        var.put("%reportid%", event.getReport().getReportid());
        JoinQuitEvent.getOnlineStaffs().forEach(staff -> {
            if(staff.toPlayer() != null){
                Player p = staff.toPlayer();
                p.sendMessage("§b§m" + Strings.repeat("-", 53));
                for(String s : language.getStringList("New-report-created", var)){
                    p.sendMessage(s);
                }
                p.sendMessage("§b§m" + Strings.repeat("-", 53));
                p.playSound(p.getLocation(), Sound.ITEM_PICKUP, 2F, 1F);
            }
        });
    }
}
