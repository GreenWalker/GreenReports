package br.com.empirelands.util.database_util_methods.report_player_mg;

import br.com.empirelands.ConsoleLogguerManager;
import br.com.empirelands.DreamReports;
import br.com.empirelands.mysql.DaoManager;
import br.com.empirelands.player.normal_user.GenericUser;
import br.com.empirelands.player.report_user.ReportPlayer;
import br.com.empirelands.report.Report;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class ReportPlayerDatabaseImpl implements ReportPlayerDatabaseMG{

    @Getter
    private DaoManager daoManager;

    public ReportPlayerDatabaseImpl(DaoManager daoManager) {
        this.daoManager = daoManager;
    }

    @Override
    public ReportPlayer getReportedPlayer(String nick) {
        ReportPlayer rp = null;
        GenericUser user = getDaoManager().getPlayerDao().getUser(nick);
        if(user != null){
            List<Report> report = getDaoManager().getReportDao().getReport(nick);
            if(report != null && report.size() != 0){
            report.forEach(s -> DreamReports.getInstance().debugInfo("ReportPlayer requested by " + s.getReportid() + " report id"));
                rp = new ReportPlayer(user, report);
            }else{
                ConsoleLogguerManager.getInstance().logWarn("Reports of player " + nick + " are null");
            }
        }
        return rp;
    }

    @Override
    public List<ReportPlayer> getAllReportedPlayers() {
        List<String> already = new ArrayList<>();
        List<ReportPlayer> rp = new ArrayList<>();
        getDaoManager().getReportDao().getAllReports().forEach(report -> {
            try {
                if (report != null) {
                    GenericUser reported = report.getReported();
                    if (!already.contains(reported.getNick())) {
                        ReportPlayer reportPlayer = new ReportPlayer(reported, getDaoManager().getReportDao().getReport(reported.getNick()));
                        already.add(reported.getNick());
                        rp.add(reportPlayer);
                    }
                }
            }catch (Exception err){
                err.printStackTrace();
            }
        });
        return rp;
    }
}
