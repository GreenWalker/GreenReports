package br.com.empirelands.commands.staff;

import br.com.empirelands.ConsoleLogguerManager;
import br.com.empirelands.DreamReports;
import br.com.empirelands.config.ConfigHandler;
import br.com.empirelands.listeners.JoinQuitEvent;
import br.com.empirelands.manager.GenericUserManager;
import br.com.empirelands.menu.Menus;
import br.com.empirelands.player.normal_user.GenericUser;
import br.com.empirelands.player.report_user.ReportPlayer;
import br.com.empirelands.manager.ReportPlayerManager;
import br.com.empirelands.reflection.NmsVersion;
import br.com.empirelands.report.Report;
import br.com.empirelands.manager.ReportManager;
import br.com.empirelands.util.JsonVariables;
import br.com.empirelands.util.Util;
import br.com.empirelands.util.UtilMethods;
import br.com.empirelands.manager.DatabaseDaoManager;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class ReportsCmd implements CommandExecutor {

    private NmsVersion nms;
    private ReportPlayerManager reportPlayerManager;
    private GenericUserManager genericUserManager;
    private ReportManager reportManager;
    private ConfigHandler language;
    private DatabaseDaoManager ddm;
    private Menus menus;
    private UtilMethods utilMethods;

    public ReportsCmd(NmsVersion nms, ReportPlayerManager rm, GenericUserManager genericUserManager, ReportManager repManager, Menus menus, DatabaseDaoManager ddm, UtilMethods methods) {
        this.reportPlayerManager = rm;
        this.nms = nms;
        this.genericUserManager = genericUserManager;
        this.ddm = ddm;
        this.reportManager = repManager;
        this.menus = menus;
        this.utilMethods = methods;
        this.language = DreamReports.getInstance().getLangFile();
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!command.getName().equalsIgnoreCase("reportes")) {
            return true;
        }
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("§cPara garantir que nao haja problemas, nao e possivel realizar este comando fora de jogo.");
            return true;
        }
        Player player = (Player) commandSender;
        GenericUser user = genericUserManager.getPlayer(player.getUniqueId());
        if (user == null) return false;
        if (args.length == 0) {
            if (!JoinQuitEvent.getOnlineStaffs().contains(user)) {
                nms.disconectPlayer(player, "you need stay online, reconnect at now.");
                return true;
            }
            nms.sendTitle(player, JsonVariables.LOADING_REPORTS.getMsg(), 10, 30, 10);
            new BukkitRunnable() {
                String percentage;
                int value = 0;

                @Override
                public void run() {
                    value += new Random().nextInt(30);
                    percentage = (value > 99 ? value = 100 : value) + "%";
                    if (value > 99) {
                        cancel();
                    }
                    nms.sendActionBar(player, JsonVariables.LOADING_REPORTS_SUB.getMsg().replaceAll("%percentage%", percentage));

                    if (value == 100) {
                        try {
                            menus.loadReportMenu(user, reportPlayerManager.getAllPlayers());
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }.runTaskTimer(DreamReports.getInstance(), 0, 2 * 10);
            return true;
        }
        if (args[0].equalsIgnoreCase("ajuda")) {
            Util.getInstancie().sendHelpMessage(commandSender);
            return true;
        }
        if (args[0].equalsIgnoreCase("limpar")) {
            if (!player.hasPermission("dcreports.staff.clear")) {
                player.sendMessage("§cVocê não tem permissão para limpar reports.");
                return true;
            }
            if (args.length == 1) {
                int var = reportManager.getReports().size();
                utilMethods.removeAllReports(user);
                nms.sendActionBar(player, JsonVariables.CLEAR_ALL_REPORTS.getMsg().replaceAll("%q%", String.valueOf(var)));

                ConsoleLogguerManager.getInstance().logWarn("Player " + player.getName() + " limpou todos os reportes!");
                return true;
            }
            if (args.length == 2) {
                ReportPlayer reportPlayer = reportPlayerManager.getPlayer(args[1]);
                if (reportPlayer != null) {
                    int var = reportPlayer.getReports().size();
                    utilMethods.deleteAllPlayerReports(user, reportPlayer);
                    nms.sendActionBar(player, JsonVariables.CLEAR_PLAYER_REPORTS.getMsg().replaceAll("%q%", String.valueOf(var)).replaceAll("%p%", args[1]));
                    ConsoleLogguerManager.getInstance().logWarn("Player " + player.getName() + " limpou todos os reportes do player " + args[1] + "!");
                    return true;
                }
            }
            if (args.length == 3) {
                ReportPlayer reportPlayer = reportPlayerManager.getPlayer(args[1]);
                if (reportPlayer != null) {
                    if (Util.getInstancie().isNumber(args[2])) {
                        int var;
                        for (Report r : reportPlayer.getReports()) {
                            if (r.getReportid() == Integer.valueOf(args[2])) {
                                var = r.getReportid();
                                utilMethods.deleteReport(user, r);
                                nms.sendActionBar(player, JsonVariables.CLEAR_PLAYER_REPORT.getMsg().replaceAll("%q%", String.valueOf(var)).replaceAll("%p%", args[1]));
                                ConsoleLogguerManager.getInstance().logWarn("Player " + player.getName() + " limpou o report " + var + " reportes do player " + args[1] + "!");
                                return true;
                            }
                        }
                    } else {
                        player.sendMessage("§cID do report só pode conter letras.");
                        return true;
                    }
                }
            }
        }
        if (reportPlayerManager.getPlayer(args[0]) != null) {
            ReportPlayer puser = reportPlayerManager.getPlayer(args[0]);
            if (puser != null) {
                DreamReports.getInstance().debugInfo("ReportedPlayer request: \nName: " + puser.getNick() + " Id: " + puser.getUuid() + " Online: " + puser.isOnline());
                menus.loadReportPlayerPerfil(user, puser);
                player.playSound(player.getLocation(), Sound.NOTE_PLING, 1F, 2F);
                return true;
            } else {
                player.sendMessage(language.getStringReplacedWithPrefix("Not-Reported"));
                return true;
            }
        }
        return false;
    }
}
