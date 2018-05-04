package br.com.empirelands.listeners.events;

import br.com.empirelands.util.Util;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.HashSet;
import java.util.Set;

/**
 * Criado por Floydz.
 */
public class CommandPreProcess implements Listener {

    @Getter
    private static Set<Player> inCommandListening = new HashSet<>();

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if (inCommandListening.contains(event.getPlayer())) {
            event.getPlayer().sendMessage(Util.getInstancie().colorize(Util.getInstancie().getPrefix() + " &cVocê não pode utilizar nenhum comando."));
            event.setCancelled(true);
        }
    }

}
