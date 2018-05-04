package br.com.empirelands.commands.player;

import br.com.empirelands.DreamReports;
import br.com.empirelands.config.ConfigHandler;
import br.com.empirelands.menu.Menus;
import br.com.empirelands.player.normal_user.GenericUser;
import br.com.empirelands.report.Report;
import br.com.empirelands.util.Util;
import br.com.empirelands.util.UtilMethods;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Criado por Floydz.
 */
public class ReporteCmd implements CommandExecutor {

    private ConfigHandler language;
    private Menus menus;
    private UtilMethods utilMethods;

    public ReporteCmd(Menus menus, UtilMethods utilMethods) {
        this.menus = menus;
        this.utilMethods = utilMethods;
        this.language = DreamReports.getInstance().getLangFile();
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!command.getName().equalsIgnoreCase("reporte")) {
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
        GenericUser user = utilMethods.getUser(player);
        if (user == null){
            player.sendMessage(language.getStringReplacedWithPrefix("Need-Report"));
            return false;
        }
        if (args[0].equalsIgnoreCase("info")) {
            if (args.length == 1 || (args.length == 2 && args[1].equalsIgnoreCase(player.getName()))) {
                if (user.isStaff()) {
                    menus.loadStaffPerfil(user, user);
                    return true;
                }
                menus.loadUserPerfil(user, user);
                return true;
            }
            if (args.length == 2) {
                if (Util.getInstancie().isNumber(args[1])) {
                    if (utilMethods.getReport(Integer.valueOf(args[1])) != null) {
                        Report r = utilMethods.getReport(Integer.valueOf(args[1]));
                        if ((player.hasPermission("dcreports.staff")) || r.getReporter().equals(user) || r.getReported().equals(user)) {
                            menus.loadReportOptions(user, r);
                            return true;
                        } else {
                            player.sendMessage(language.getStringReplacedWithPrefix("Need-Permission"));
                            return true;
                        }
                    }
                    player.sendMessage("§cEste report não existe.");
                    return true;
                }
                if (player.hasPermission("dcreports.staff")) {
                    Player p = Bukkit.getPlayerExact(args[1]);
                    GenericUser target = utilMethods.getUser(args[1]);
                    if (target != null) {
                        if ((p != null && target.isStaff()) || utilMethods.getStaff(target) != null) {
                            menus.loadStaffPerfil(user, target);
                            return true;
                        }
                        menus.loadUserPerfil(user, target);
                        return true;
                    } else {
                        player.sendMessage(language.getStringReplacedWithPrefix("Target-Without-Account"));
                        return true;
                    }
                } else {
                    player.sendMessage(language.getStringReplacedWithPrefix("Need-Permission"));
                    return true;
                }
            }
            }
        return false;
    }
}

