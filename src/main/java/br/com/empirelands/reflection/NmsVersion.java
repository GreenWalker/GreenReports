package br.com.empirelands.reflection;

import org.bukkit.entity.Player;

public interface NmsVersion {
    void sendTitle(Player player, String json, int fadein, int show, int fadeout);
    void sendSubTitle(Player player, String json, int fadein, int show, int fadeout);
    void sendTitle(Player player, String t1, String t2, int fadein, int show, int fadeout);
    void sendActionBar(Player player, String messageInJson);
    void disconectPlayer(Player player, String reason);

}
