package br.com.empirelands.mysql;

import br.com.empirelands.DreamReports;
import br.com.empirelands.mysql.implments.*;
import br.com.empirelands.mysql.interfaces.*;

public class DaoManager {

    private static DaoManager instancie;
    private PlayerAdmDao playerAdmDao;
    private PlayerDao playerDao;
    private ReportDao reportDao;
    private ReportViewDao reportViewDao;
    private BanDao banDao;

    public BanDao getBanDao() {
        if (null == banDao) {
            banDao = new BanDaoImpl(DreamReports.getInstance().getDbManager());
        }
        return banDao;
    }

    public static DaoManager getInstancie() {
        if(null == instancie){
            instancie = new DaoManager();
        }
        return instancie;

    }

    public PlayerDao getPlayerDao() {
        if(playerDao == null){
            playerDao = new PlayerDaoImpl(DreamReports.getInstance().getDbManager());
        }
        return playerDao;
    }

    public ReportDao getReportDao() {
        if(reportDao == null){
            reportDao = new ReportDaoImpl(DreamReports.getInstance().getDbManager());
        }
        return reportDao;
    }

    public PlayerAdmDao getPlayerAdmDao() {
        if(playerAdmDao == null){
            playerAdmDao = new PlayerAdmDaoImpl(DreamReports.getInstance().getDbManager());
        }
        return playerAdmDao;
    }

    public ReportViewDao getReportViewDao() {
        if(reportViewDao == null){
            reportViewDao = new ReportViewImpl(DreamReports.getInstance().getDbManager());
        }
        return reportViewDao;
    }
}
