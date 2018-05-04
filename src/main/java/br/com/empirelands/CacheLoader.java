package br.com.empirelands;

import br.com.empirelands.player.normal_user.GenericUser;
import br.com.empirelands.player.report_user.ReportPlayer;
import br.com.empirelands.report.Report;
import br.com.empirelands.manager.DatabaseDaoManager;

import java.util.List;

public class CacheLoader {

    private final DatabaseDaoManager db;

    public CacheLoader(DatabaseDaoManager db) {
        this.db = db;
    }

    public List<GenericUser> loadUserCache(){
        return db.getUserDatabaseMG().getAllUsers();
    }

    public List<Report> loadReportCache(){
        return db.getReportDatabaseMG().getAllReports();
    }

    public List<ReportPlayer> loadReportPlayerCache(){
        return db.getReportPlayerDatabaseMG().getAllReportedPlayers();
    }
}
