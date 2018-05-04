package br.com.empirelands.manager;

import br.com.empirelands.DreamReports;
import br.com.empirelands.player.report_user.ReportPlayer;

import java.util.List;
import java.util.UUID;

public class ReportPlayerManager {

    private List<ReportPlayer> reportPlayerList;

    public ReportPlayerManager(List<ReportPlayer> reportPlayerList) {
        this.reportPlayerList = reportPlayerList;
    }

    public ReportPlayer getPlayer(String nick) {
        if (reportPlayerList != null) {
            for (ReportPlayer reportPlayer : this.reportPlayerList) {
                DreamReports.getInstance().debugInfo("ReportPlayer requested with param '" + nick + "' result '" + reportPlayer.getNick() + "'");
                if (reportPlayer.getNick().equalsIgnoreCase(nick)) {
                    DreamReports.getInstance().debugInfo(nick + " Founded in collection list, more details \n" + "name: " + reportPlayer.getNick() + " uuid: " + reportPlayer.getUuid());
                    return reportPlayer;
                }
            }
        }
        return null;
    }

    public void addPlayer(ReportPlayer player) {
        if (!existPlayer(player.getUuid()) || !existPlayer(player.getNick())) {
            this.reportPlayerList.add(player);
        }
    }

    public ReportPlayer getPlayer(UUID id) {
        if (reportPlayerList != null) {
            for (ReportPlayer reportPlayer : this.reportPlayerList) {
                if (reportPlayer.getUuid().equals(id)) {
                    return reportPlayer;
                }
            }
        }
        return null;
    }

    public void removeAllPlayers() {
        this.reportPlayerList.clear();
    }

    public void removePlayer(ReportPlayer reportPlayer) {
        this.reportPlayerList.remove(reportPlayer);
    }

    public void removePlayer(UUID reportPlayerid) {
        if (existPlayer(reportPlayerid)) {
            this.reportPlayerList.remove(getPlayer(reportPlayerid));
        }
    }

    public void removePlayer(String nick) {
        if (existPlayer(nick)) {
            this.reportPlayerList.remove(getPlayer(nick));
        }
    }

    public List<ReportPlayer> getAllPlayers() {
        return this.reportPlayerList;
    }

    public boolean existPlayer(String nick) {
        return this.getPlayer(nick) != null;
    }

    public boolean existPlayer(UUID id) {
        return this.getPlayer(id) != null;
    }
}
