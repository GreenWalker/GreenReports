package br.com.empirelands.util;

import br.com.empirelands.ConsoleLogguerManager;
import br.com.empirelands.DreamReports;
import br.com.empirelands.player.normal_user.GenericUser;
import fancyfull.main.java.mkremins.fanciful.FancyMessage;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * Criado por Floydz.
 */
public class ConfigUtils {

    private static FileConfiguration config = DreamReports.getInstance().getConfig();

    public static List<String> getConfigReasons() {
        List<String> r = new ArrayList<>();
        if (isSection("Razoes")) {
            r.addAll(config.getConfigurationSection("Razoes").getKeys(false));
        }
        return r;
    }

    public static Map<Integer, String> getPreMessages() {
        Map<Integer, String> messages = new HashMap<>();
        for (String s : config.getStringList("pre-messages")) {
            if (s.contains(";")) {
                String[] x54 = s.split(";");
                int i = Integer.valueOf(x54[0]);
                    String replaced = "";
                for (UnicodeSymbols ss : UnicodeSymbols.values()) {
                    replaced = x54[1].replaceAll(ss.getToCallSymbol(), ss.getSymbol()).replaceAll("&", "§");
                }
                    messages.put(i, replaced);
            }
        }
        return messages;
    }

    public static List<String> getAlias(String reason) {
        return config.getStringList("Razoes." + reason + ".Alias");
    }

    public static Material getReasonMaterial(String reason) {
        String s = config.getString("Razoes." + reason + ".Menu.item");
        if(s != null){
        return Material.valueOf(s);
        }
        return Material.AIR;
    }

    public static String suchReportAlias(String toSuch) {
        if (isSection("Razoes")) {
            for (String reason : getConfigReasons()) {
                for (String s : config.getStringList("Razoes." + reason + ".Alias")) {
                    if (toSuch.equalsIgnoreCase(s)) {
                        return reason;
                    }
                }
            }
        }
        return null;
    }

    public static boolean isSection(String path) {
        if (!config.isConfigurationSection(path)) {
            ConsoleLogguerManager.getInstance().logWarn("Nao foi possivel encontrar nenhuma razao!.");
            return false;
        }
        return true;
    }

    public static void sentPlayerJsonMessage(Player p, GenericUser r) {
        if(r != null) {
            FancyMessage f = new FancyMessage("§3" + UnicodeSymbols.LARGEST_BOLL.getSymbol())
                    .then("§bReportar ")
                    .then("§f§n" + r.getNick() + "§r ");
            if (r.getId() != 0 && r.getRegister_date() != null) {
                f.tooltip("§f" + r.getNick(), "§7Reportado §f" + r.getReportedAllTimes() + "§7 vezes", "§7Enviou §f" +
                        r.getReports_sends() + "§7 reports", "§7Registrado em: §f" +
                        Util.getInstancie().getDate(r.getRegister_date().getTime())).then("§bpor:");
            }
            f.send(p);
            Collections.sort(getConfigReasons());
            Map<Integer, String> get = getPreMessages();
            for (String s : getConfigReasons()) {
                String path = "Razoes." + s;
                int index1 = config.getInt(path + ".Json.mensagem");
                FancyMessage msg = new FancyMessage(get.get(index1).replaceAll("%razao%", s));
                if (config.getBoolean(path + ".Json.hover-message.ativado")) {
                    int index2 = config.getInt(path + ".Json.hover-message.mensagem");
                    msg.tooltip(get.get(index2).replaceAll("%razao%", s));
                }
                if (config.getBoolean(path + ".Json.run-cmd.ativado")) {
                    int index3 = config.getInt(path + ".Json.run-cmd.mensagem");
                    msg.command(config.getString(path + ".Json.run-cmd.cmd").replace("%player%", r.getNick()));
                }
                msg.send(p);
            }
        }
    }
}
