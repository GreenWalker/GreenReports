package br.com.empirelands.listeners;

import br.com.empirelands.DreamReports;
import br.com.empirelands.config.ConfigHandler;
import br.com.empirelands.player.normal_user.GenericUser;
import br.com.empirelands.report.Report;
import br.com.empirelands.util.Util;
import br.com.empirelands.util.UtilMethods;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;


public class ChatHandler implements Listener {

    @Getter
    private static Set<Player> ChatListening = new HashSet<>();
    @Getter
    private static HashMap<Player, GenericUser> banCommand = new HashMap<>();
    @Getter
    private static HashMap<Player, GenericUser> reporteds = new HashMap<>();
    @Getter
    private static HashMap<Player, GenericUser> reporters = new HashMap<>();
    @Getter
    private static HashMap<Player, String> motives = new HashMap<>();


    private ConfigHandler language;
    private UtilMethods utilMethods;

    public ChatHandler(ConfigHandler language, UtilMethods methods) {
        this.language = language;
        this.utilMethods = methods;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String s = event.getMessage();
        if (getChatListening().contains(player)) {
            event.setCancelled(true);
            String proof = "";
            if (!Util.getInstancie().checkUrl(s)) {
                player.sendMessage(Util.getInstancie().colorize(Util.getInstancie().getPrefix() + " &cProva de report inválida!"));
                player.sendMessage(Util.getInstancie().colorize(Util.getInstancie().getPrefix() + " &cDigite &eCancelar&c para reportar sem prova."));
            } else {
                proof = s;
            }
            if (s.equalsIgnoreCase("cancelar") || s.equalsIgnoreCase("cancel")) {
                ChatListening.remove(player);
            }
            if (s.equalsIgnoreCase("cancelar") || Util.getInstancie().checkUrl(s)) {
                if (proof.equals("")) {
                    player.sendMessage(language.getStringReplacedWithPrefix("Without-Proof"));
                }
                GenericUser reported = reporteds.get(player);
                GenericUser reporter = reporters.get(player);
                String motive = motives.get(player);
                Report r = utilMethods.createReport(reporter, reported, motive, proof);

                player.sendMessage(language.getStringReplacedWithPrefix("Reported-By").replaceAll("%player%", reported.getNick()).replaceAll("%reason%", motive));
                Util.getInstancie().sendReportId(player, r).send(player);
                if (getChatListening().contains(player)) {
                    getChatListening().remove(player);
                }
            }
        }
        if (banCommand.containsKey(player)) {
            event.setCancelled(true);
            String cmd = event.getMessage();
            boolean t = (cmd.contains("ban") || cmd.contains("banir") || cmd.contains("punir") || cmd.contains("temp"));
            if(cmd.startsWith("/")){
                cmd = cmd.replaceAll("/", "");
            }
            if (!t) {
                player.sendMessage("§cComando inválido!");
            } else {
                utilMethods.banPlayer(banCommand.get(player), player, cmd);
                banCommand.remove(player);
                return;
            }
        }
        if(s.startsWith(".") && player.hasPermission("dcreports.staff.bind")){
            if(s.equalsIgnoreCase(".breload") && player.hasPermission("dcreports.staff.reload")) {
                event.setCancelled(new AtomicBoolean(true).get());
                FileConfiguration config = DreamReports.getInstance().getConfig();
                player.sendMessage("§aArquivos re-carregados.");
            } else if(s.equalsIgnoreCase(".debug") && player.hasPermission("dcreports.staff.debug")){
                event.setCancelled(new AtomicBoolean(true).get());
                boolean b = DreamReports.getInstance().getConfig().getBoolean("debug");
                DreamReports.getInstance().getConfig().set("debug", !b);
                String actived = !b ? "§aativado" : "§cdesativado";
                player.sendMessage("debug " + actived);
            }
        }
    }

}
