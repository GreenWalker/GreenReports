package br.com.empirelands.reflection.v1_8_R1;

import br.com.empirelands.reflection.NmsVersion;
import net.minecraft.server.v1_8_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;

public class VersionControl1_8_R1 implements NmsVersion {

    private static VersionControl1_8_R1 instance;

    private VersionControl1_8_R1(){
    }

    public static VersionControl1_8_R1 getInstance(){
        if(null == instance){
            instance = new VersionControl1_8_R1();
        }
        return instance;
    }

    @Override
    public void sendActionBar(Player player, String json) {
        try {
            IChatBaseComponent baseComponent = ChatSerializer.a(json);
            PacketPlayOutChat cht = new PacketPlayOutChat(baseComponent, (byte)2);
            ((CraftPlayer)player).getHandle().playerConnection.sendPacket(cht);
        }catch (Throwable ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void disconectPlayer(Player player, String reason) {
        try{
            PlayerConnection pc = ((CraftPlayer) player).getHandle().playerConnection;
            pc.disconnect(reason);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void sendTitle(Player player, String json, int fadein, int show, int fadeout) {
        try{
            PacketPlayOutTitle cht = new PacketPlayOutTitle(EnumTitleAction.TITLE, ChatSerializer.a(json), fadein, show, fadeout);
            PlayerConnection pc = ((CraftPlayer)player).getHandle().playerConnection;
            pc.sendPacket(cht);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void sendSubTitle(Player player, String json, int fadein, int show, int fadeout) {
        try{
            PacketPlayOutTitle cht = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, ChatSerializer.a(json), fadein, show, fadeout);
            PlayerConnection pc = ((CraftPlayer)player).getHandle().playerConnection;
            pc.sendPacket(cht);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void sendTitle(Player player, String t1, String t2, int fadein, int show, int fadeout) {
        try{
            PacketPlayOutTitle pos = new PacketPlayOutTitle(EnumTitleAction.TITLE, ChatSerializer.a(t1), fadein, show, fadeout);
            PacketPlayOutTitle cht = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, ChatSerializer.a(t2), fadein, show, fadeout);
            PlayerConnection pc = ((CraftPlayer)player).getHandle().playerConnection;
            pc.sendPacket(pos);
            pc.sendPacket(cht);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

//    @Override
//    public void sentAllBoties(){
//
//    }

}
