package br.com.empirelands.manager;

import br.com.empirelands.mysql.DaoManager;
import br.com.empirelands.util.database_util_methods.ban_mg.BanDatabaseImpl;
import br.com.empirelands.util.database_util_methods.ban_mg.BanDatabaseMG;
import br.com.empirelands.util.database_util_methods.report_mg.ReportDatabaseImpl;
import br.com.empirelands.util.database_util_methods.report_mg.ReportDatabaseMG;
import br.com.empirelands.util.database_util_methods.report_player_mg.ReportPlayerDatabaseImpl;
import br.com.empirelands.util.database_util_methods.report_player_mg.ReportPlayerDatabaseMG;
import br.com.empirelands.util.database_util_methods.reportview_mg.ReportViewDatabaseImpl;
import br.com.empirelands.util.database_util_methods.reportview_mg.ReportViewDatabaseMG;
import br.com.empirelands.util.database_util_methods.staff_mg.StaffDatabaseImpl;
import br.com.empirelands.util.database_util_methods.staff_mg.StaffDatabaseMG;
import br.com.empirelands.util.database_util_methods.user_mg.UserDatabaseImpl;
import br.com.empirelands.util.database_util_methods.user_mg.UserDatabaseMG;

public class DatabaseDaoManager {


    private DaoManager daoManager;

    private ReportDatabaseMG reportDatabaseMG;

    private ReportPlayerDatabaseMG reportPlayerDatabaseMG;

    private ReportViewDatabaseMG reportViewDatabaseMG;

    private StaffDatabaseMG staffDatabaseMG;

    private UserDatabaseMG userDatabaseMG;

    private BanDatabaseMG banDatabaseMG;

    public BanDatabaseMG getBanDatabaseMG() {
        if (null == banDatabaseMG) {
            banDatabaseMG = new BanDatabaseImpl(getDaoManager());
        }
        return banDatabaseMG;
    }

    public DatabaseDaoManager(DaoManager daoManager) {
        this.daoManager = daoManager;
    }

    private DaoManager getDaoManager() {
        return daoManager;
    }

    public ReportDatabaseMG getReportDatabaseMG() {
        if (null == reportDatabaseMG) {
            reportDatabaseMG = new ReportDatabaseImpl(getDaoManager());
        }
        return reportDatabaseMG;
    }

    public ReportPlayerDatabaseMG getReportPlayerDatabaseMG() {
        if (null == reportPlayerDatabaseMG) {
            reportPlayerDatabaseMG = new ReportPlayerDatabaseImpl(getDaoManager());
        }
        return reportPlayerDatabaseMG;
    }

    public ReportViewDatabaseMG getReportViewDatabaseMG() {
        if (null == reportViewDatabaseMG) {
            reportViewDatabaseMG = new ReportViewDatabaseImpl(getDaoManager(), getStaffDatabaseMG(), getReportDatabaseMG());
        }
        return reportViewDatabaseMG;
    }

    public StaffDatabaseMG getStaffDatabaseMG() {
        if (null == staffDatabaseMG) {
            staffDatabaseMG = new StaffDatabaseImpl(getDaoManager());
        }
        return staffDatabaseMG;
    }

    public UserDatabaseMG getUserDatabaseMG() {
        if (null == userDatabaseMG) {
            userDatabaseMG = new UserDatabaseImpl(getDaoManager());
        }
        return userDatabaseMG;
    }

    //    public DatabaseDaoManager(DaoManager daoManager, GenericUserManager manager, ReportPlayerManager rpm) {
//        this.daoManager = daoManager;
//        this.manager = manager;
//        this.rpm = rpm;
//    }
//
//
//    public GenericUser getStaff(String nick) {
//        return getDaoManager().getPlayerAdmDao().getStaff(nick);
//    }
//
//
//    public List<GenericUser> getAllStaffs() {
//        return getDaoManager().getPlayerAdmDao().getAllStaffs();
//    }
//
//
//    public GenericUser getUser(String nick) {
//        return getDaoManager().getPlayerDao().getUser(nick);
//    }
//
//
//    public List<GenericUser> getAllUsers() {
//        return getDaoManager().getPlayerDao().getAllUsers();
//    }
//
//
//    public Report getReport(int reportid) {
//        return getDaoManager().getReportDao().getReport(reportid);
//    }
//
//
//    public List<Report> getReport(String nick) {
//        return getDaoManager().getReportDao().getReport(nick);
//    }
//
//    @Override
//    public List<Report> getAllReports() {
//        return getDaoManager().getReportDao().getAllReports();
//    }
//
//    @Override
//    public ReportPlayer getReportedPlayer(String nick) {
//        ReportPlayer rp = null;
//        if(getRpm().getPlayer(nick) != null){
//            rp = getRpm().getPlayer(nick);
//            return rp;
//        }
//        GenericUser user = getManager().getPlayer(nick);
//        if(user == null) return null;
//        rp = new ReportPlayer(user, getReport(nick));
//        return rp;
//    }
//
//    @Override
//    public List<ReportPlayer> getAllReportedPlayers() {
//        List<ReportPlayer> pl = new ArrayList<>();
//        for(Report report : getAllReports()){
//            if(report.getReported() != null){
//                ReportPlayer rpr = new ReportPlayer(report.getReported(), getReport(report.getReported().getNick()));
//                pl.add(rpr);
//            }
//        }
//        return pl;
//    }
//
//    @Override
//    public Map<GenericUser, Set<Report>> getUserViewReports(int userid) {
//        Map<GenericUser, Set<Report>> views = new HashMap<>();
//        for(GenericUser r : getAllStaffs()) {
//            if (r.getId() == userid) {
//                views.put(r, getDaoManager().getReportViewDao().getReportView(r.getId()));
//                break;
//            }
//        }
//        return views;
//    }
//
//    @Override
//    public Set<GenericUser> getUserThatViewSameReport(long reportid) {
//        Set<GenericUser> users = null;
//        for(Report r : getAllReports()){
//            if(r.getReportid() == reportid){
//                users = getDaoManager().getReportViewDao().getViewers(reportid);
//                break;
//            }
//        }
//        return users;
//    }
}
