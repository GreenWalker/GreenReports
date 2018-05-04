package br.com.empirelands.commands.player;

import br.com.empirelands.DreamReports;
import br.com.empirelands.config.ConfigHandler;
import br.com.empirelands.listeners.ChatHandler;
import br.com.empirelands.manager.GenericUserManager;
import br.com.empirelands.menu.Menus;
import br.com.empirelands.player.normal_user.GenericUser;
import br.com.empirelands.manager.ReportPlayerManager;
import br.com.empirelands.reflection.NmsVersion;
import br.com.empirelands.report.Report;

import br.com.empirelands.manager.ReportManager;
import br.com.empirelands.util.*;

import br.com.empirelands.manager.DatabaseDaoManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;

public class ReportCmd implements CommandExecutor {

    private NmsVersion nms;
    private ReportPlayerManager manager;
    private GenericUserManager gm;
    private ReportManager repManager;
    private ConfigHandler language;
    private FileConfiguration config;
    private DatabaseDaoManager ddm;
    private Menus menus;
    private UtilMethods utilMethods;

    private Cooldown delay;
    private Map<String, Integer> delayed;

    @Getter
    private static Map<String, String> whoReported;

    public ReportCmd(NmsVersion nms, ReportPlayerManager rm, GenericUserManager gm, ReportManager repManager, Menus menus, DatabaseDaoManager ddm, UtilMethods methods) {
        this.manager = rm;
        this.nms = nms;
        this.gm = gm;
        this.ddm = ddm;
        this.repManager = repManager;
        this.menus = menus;
        this.utilMethods = methods;
        this.delayed = new HashMap<>();
        whoReported = new HashMap<>();
        this.delay = Cooldown.getInstance();
        this.config = DreamReports.getInstance().getConfig();
        this.language = DreamReports.getInstance().getLangFile();
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!command.getName().equalsIgnoreCase("reportar")) {
            return true;
        }
        if (args.length == 0) {
            Util.getInstancie().sendHelpMessage(commandSender);
            return true;
        }
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("§cPara garantir que nao haja problemas, nao e possivel realizar este comando fora de jogo.");
            return true;
        }
        Player player = (Player) commandSender;
        if (player.getName().equalsIgnoreCase(args[0])) {
            player.sendMessage(language.getStringReplacedWithPrefix("dont-report-you-self"));
            return true;
        }
        // Cooldown
        if (delay.containsPlayerInDelay(player)) {
            if (!delay.afterLong(player)) {
                nms.sendTitle(player, JsonVariables.IN_COOLDOWN.getMsg(), 30, 60, 40);
                nms.sendSubTitle(player, JsonVariables.IN_COOLDOWN_SUB.getMsg().replaceAll("%segundos%", delay.convertMillisToSeconds(delay.getVariation(player))), 30, 60, 40);
                if (!delayed.containsKey(player.getName())) {
                    delayed.put(player.getName(), 1);
                } else {
                    int i = delayed.get(player.getName());
                    delayed.put(player.getName(), i + 1);
                }
                return true;
            } else {
                delay.removePlayerOfDelay(player);
                delayed.remove(player.getName());
            }
            checkAtDelay(player);
        }
        // exit to Cooldown
        GenericUser reporter = gm.getPlayer(player.getName());
        if (reporter == null) {
            reporter = utilMethods.createNewUser(player);
        }
        if (Util.getInstancie().checkIsStaff(Bukkit.getPlayerExact(args[0]))) {
            player.sendMessage(language.getStringReplacedWithPrefix("Player-Bypass"));
            return true;
        }
        GenericUser reported = gm.getPlayer(args[0]);
        if (reported == null) {
            Player reportedPlayer = Bukkit.getPlayerExact(args[0]);
            if (reportedPlayer == null) {
                player.sendMessage(language.getStringReplacedWithPrefix("Need-Player-Online"));
                player.playSound(player.getLocation(), Sound.BAT_DEATH, 1, 1);
                return true;
            }
            reported = utilMethods.createNewUser(reportedPlayer);
        }
        if(utilMethods.isBannedPlayer(reported).get()){
            player.sendMessage(language.getStringReplacedWithPrefix("Player-Already-Banned"));
            return true;
        }
        DreamReports.getInstance().debugInfo("Reports of player " + reported.getNick() + " is null " + (repManager.getReports(reported.getNick()) == null));
        for (Report r : repManager.getReports(reported.getNick())) {
            DreamReports.getInstance().debugInfo("Reporter nick " + r.getReporter().getNick() + " is equals to " + player.getName());
            if (r.getReporter().getNick().equalsIgnoreCase(player.getName())) {
                player.sendMessage(language.getStringReplacedWithPrefix("Already-reported"));
                return true;
            }
        }
        String motivo;
        if (args.length >= 2) {
            if (ConfigUtils.getConfigReasons().contains(args[1])) {
                motivo = args[1];
            } else {
                motivo = ConfigUtils.suchReportAlias(args[1]);
                if (motivo == null) {
                    player.sendMessage(Util.getInstancie().colorize(Util.getInstancie().getPrefix() + "&c " + args[1] + " não é um motivo de report válido."));
                    return true;
                }
            }
        } else {
            whoReported.put(player.getName(), args[0]);
            if (config.getBoolean("Chat-Ou-Menu")) {
                ConfigUtils.sentPlayerJsonMessage(player, reported);
                return true;
            }
            menus.loadReportOptionsMenu(reporter, reported);
            return true;
        }
        String proof = "";
        if (args.length >= 3) {
            if (Util.getInstancie().checkUrl(args[2])) {
                proof = args[2];
                Report r = utilMethods.createReport(reporter, reported, motivo, proof);
                player.sendMessage(language.getStringReplacedWithPrefix("Reported-By").replaceAll("%player%", reported.getNick()).replaceAll("%reason%", motivo));
                Util.getInstancie().sendReportId(player, r);
            } else {
                player.sendMessage(Util.getInstancie().colorize(Util.getInstancie().getPrefix() + " &cProva de reporte inválida!"));
            }
        }
        if (args.length < 3) {
            player.sendMessage(language.getStringReplacedWithPrefix("Past-Proof-In-Chat"));
            ChatHandler.getChatListening().add(player);
            ChatHandler.getReporters().put(player, reporter);
            ChatHandler.getReporteds().put(player, reported);
            ChatHandler.getMotives().put(player, motivo);
        }
        delay.putPlayerOnDelay(player);
        return true;
    }

//    private boolean isValidPlayer(String nick) {
//        return Bukkit.getPlayerExact(nick) != null;
//     }

    private void checkAtDelay(Player p) {
        if (delayed.containsKey(p.getName())) {
            if (delayed.get(p.getName()) > 4) {
                p.sendMessage("§cVocê abusou de comando em cooldown!");
                p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 60 * 20, 5, true, false));
            }
        }
    }
}
