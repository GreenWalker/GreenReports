package br.com.empirelands.commands.player;

import br.com.empirelands.util.ConfigUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Criado por Floydz.
 */
public class ReportTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        if (command.getName().equalsIgnoreCase("reportar")) {
            List<String> sre = new ArrayList<>();
            if (args.length > 1) {
                if (args[1] != null) {
                    if (args[1].equalsIgnoreCase("")) {
                        for (String rs : ConfigUtils.getConfigReasons()) {
                            if (rs.toLowerCase().startsWith(args[1].toLowerCase())) {
                                sre.add(rs);
                            }
                        }
                    } else {
                        sre.addAll(ConfigUtils.getConfigReasons());
                    }
                    Collections.sort(sre);
                    return sre;
                }
            }
        }
        return null;
    }

}
