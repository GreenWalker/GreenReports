package br.com.empirelands.player.normal_user;

import br.com.empirelands.menu.MenuHelper;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.sql.Timestamp;
import java.util.UUID;

public class GenericUser {

    private int id;
    private UUID uuid;
    private String nick;
    private int reportedTimesAtLastRestart;
    private int reportedAllTimes;
    private int finishedReports;
    private Timestamp register_date;
    private int reports_sends;
    private MenuHelper helper;
    @Getter
    @Setter
    private boolean isBanned;

    public GenericUser(String nick, UUID uuid, int reportedAllTimes) {
        this.nick = nick;
        this.uuid = uuid;
        this.reportedAllTimes = reportedAllTimes;
        this.helper = new MenuHelper();
    }

    public Player toPlayer(){
        Player p = null;
        Player onlinePlayer = Bukkit.getPlayer(this.uuid);

        if(onlinePlayer != null){
            p = onlinePlayer;
        }else{
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(this.uuid);
            if(offlinePlayer != null){
                p = offlinePlayer.getPlayer();
            }
        }
        return p;
    }

    public boolean isStaff(){
        return toPlayer().hasPermission("dcreports.staff");
    }

    public boolean isOnline(){
        return Bukkit.getPlayer(uuid) != null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public int getReportedTimesAtLastRestart() {
        return reportedTimesAtLastRestart;
    }

    public void setReportedTimesAtLastRestart(int reportedTimesAtLastRestart) {
        this.reportedTimesAtLastRestart = reportedTimesAtLastRestart;
    }

    public int getReportedAllTimes() {
        return reportedAllTimes;
    }

    public void setReportedAllTimes(int reportedAllTimes) {
        this.reportedAllTimes = reportedAllTimes;
    }

    public int getFinishedReports() {
        return finishedReports;
    }

    public void setFinishedReports(int finishedReports) {
        this.finishedReports = finishedReports;
    }

    public Timestamp getRegister_date() {
        return register_date;
    }

    public void setRegister_date(Timestamp register_date) {
        this.register_date = register_date;
    }

    public int getReports_sends() {
        return reports_sends;
    }

    public void setReports_sends(int reports_sends) {
        this.reports_sends = reports_sends;
    }

    public MenuHelper getHelper() {
        return helper;
    }

    public void setHelper(MenuHelper helper) {
        this.helper = helper;
    }
}
