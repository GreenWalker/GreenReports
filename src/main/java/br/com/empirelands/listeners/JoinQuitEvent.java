package br.com.empirelands.listeners;

import br.com.empirelands.manager.GenericUserManager;
import br.com.empirelands.player.normal_user.GenericUser;
import br.com.empirelands.player.report_user.ReportPlayer;
import br.com.empirelands.manager.ReportPlayerManager;
import br.com.empirelands.util.Util;
import br.com.empirelands.util.UtilMethods;
import br.com.empirelands.manager.DatabaseDaoManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashSet;
import java.util.Set;

/**
 * Criado por Floydz.
 */
public class JoinQuitEvent implements Listener {

    @Getter
    private static Set<GenericUser> onlineStaffs = new HashSet<>();

    private FileConfiguration config;
    private GenericUserManager manager;
    private ReportPlayerManager rpm;
    private DatabaseDaoManager daoManager;
    private UtilMethods methods;

    public JoinQuitEvent(FileConfiguration config, GenericUserManager manager, ReportPlayerManager rpm, DatabaseDaoManager daoManager, UtilMethods methods) {
        this.config = config;
        this.manager = manager;
        this.rpm = rpm;
        this.daoManager = daoManager;
        this.methods = methods;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        if (p == null) return;
            GenericUser user = manager.getPlayer(p.getUniqueId());
        if (p.hasPermission("dcreports.staff")) {
            if (user != null) {
                onlineStaffs.add(user);
                if (daoManager.getStaffDatabaseMG().getStaff(user.getId()) == null) {
                    daoManager.getStaffDatabaseMG().addStaff(user);
                }
            }
        } else if(user != null && daoManager.getStaffDatabaseMG().getStaff(user.getId()) != null){
                if(!user.isStaff()){
                    daoManager.getStaffDatabaseMG().removeStaff(user);
                }
        }
        ReportPlayer rp = methods.getReportPlayer(p.getUniqueId());
        if (rp != null && rp.isAlreadyLogout() && rp.getReports().size() > 0) {
            if (config.getBoolean("enable-effects-on-logout")) {
                p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 9999, 108, true, false));
                p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 9999, 200, true, false));
                p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 9999, 108, true, false));
                p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 9999, 2, true, false));
            }
            if (config.getBoolean("teleport-player-to-jail-on-logout")) {
                String s = config.getString("jail-cmd").replaceAll("%player%", p.getName());
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s.replaceAll("/", ""));
            }
            for (int i = 0; i < 5; ++i) {
                p.sendMessage("§cVocê foi reportado e deslogou! para voltar a jogar espere que um staff analise seu reporte.");
            }
            if (CloseReportHandler.getToRemoveEffects().contains(p.getName())) {
                Util.getInstancie().removePlayerEffects(p);
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player p = event.getPlayer();
        if (p == null) return;
        if (p.hasPermission("dcreports.staff")) {
            if (manager.getPlayer(p.getUniqueId()) != null) {
                onlineStaffs.remove(manager.getPlayer(p.getUniqueId()));
            }
        }
        if (rpm.getPlayer(p.getUniqueId()) != null) {
            ReportPlayer reportPlayer = rpm.getPlayer(p.getUniqueId());
            if (!reportPlayer.isAlreadyLogout()) {
                reportPlayer.setAlreadyLogout(true);
                onlineStaffs.forEach(staff -> staff.toPlayer().sendMessage("§cPlayer reportado " + reportPlayer.getNick() + " deslogou."));
            }
        }
    }

    public static GenericUser getStaff(String nick) {
        for (GenericUser user : onlineStaffs) {
            if (user.getNick().equalsIgnoreCase(nick)) {
                return user;
            }
        }
        return null;
    }
}
