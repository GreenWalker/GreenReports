package br.com.empirelands.mysql.implments;

import br.com.empirelands.exception.InvalidInput;
import br.com.empirelands.mysql.MySQL;
import br.com.empirelands.mysql.interfaces.ReportDao;
import br.com.empirelands.player.normal_user.GenericUser;
import br.com.empirelands.report.Report;
import br.com.empirelands.util.Patterns;
import br.com.empirelands.manager.DatabaseDaoManager;
import lombok.Getter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReportDaoImpl implements ReportDao {

    @Getter
    private DatabaseDaoManager dbm;

    public ReportDaoImpl(DatabaseDaoManager dbm) {
        this.dbm = dbm;
    }

    @Override
    public boolean add(List<Object> objectMap) throws InvalidInput { // index 1 = id_report, index 2 = player_reportado, index 3 = player_reportador, index 4 = motivo, index 5 = data
        if (objectMap.size() != 5) {
            throw new InvalidInput("invalid input for table 'dr_report'," + objectMap.size() + " if persists contact the developer.");
        }
        PreparedStatement ps = null;
        Connection cs = null;
        try {
            cs = MySQL.getInstance().getConnection();
            ps = cs.prepareStatement("INSERT INTO dr_report (`id_report`, `player_reported`, `player_reporter`, `report_reason`, `report_date`, `report_proof`) VALUES(?, ?, ?, ?, ?, ?)");
            ps.setObject(1, objectMap.get(0));
            ps.setObject(2, objectMap.get(1));
            ps.setObject(3, objectMap.get(2));
            ps.setObject(4, objectMap.get(3));
            ps.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
            ps.setObject(6, objectMap.get(4));
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            MySQL.getInstance().closeStatement(ps);
            MySQL.getInstance().closeConnection(cs);
        }
    }

    @Override
    public boolean delete(Object id) {
        PreparedStatement ps = null;
        Connection cs = null;
        try {
            cs = MySQL.getInstance().getConnection();
            ps = cs.prepareStatement("DELETE FROM `dr_report` WHERE `id_report` = ?");
            ps.setObject(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            MySQL.getInstance().closeStatement(ps);
            MySQL.getInstance().closeConnection(cs);
        }
    }

    @Override
    public boolean update(String column, Object id, Object v) {
        PreparedStatement ps = null;
        Connection cs = null;
        try {
            cs = MySQL.getInstance().getConnection();
            ps = cs.prepareStatement("UPDATE `dr_report` SET `" + column + "` = ? WHERE `id_report` = ?");
            ps.setObject(1, v);
            ps.setObject(2, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            MySQL.getInstance().closeStatement(ps);
            MySQL.getInstance().closeConnection(cs);
        }
            return false;
    }

    @Override
    public Report getReport(int reportid) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection cs = null;
        Report r = null;
        try{
            cs = MySQL.getInstance().getConnection();
            ps = cs.prepareStatement("SELECT * FROM `dr_report` WHERE `id_report` = ?");
            ps.setInt(1, reportid);
            rs = ps.executeQuery();
            while(rs.next()){
                GenericUser reporter = getDbm().getUserDatabaseMG().getUserById(rs.getInt("player_reporter"));
                GenericUser reported = getDbm().getUserDatabaseMG().getUserById(rs.getInt("player_reported"));
                r = new Report(reported, reporter, rs.getString("report_reason"), rs.getTimestamp("report_date"));
                r.setReportid(rs.getInt("id_report"));
                String proof = rs.getString("report_proof");
                if(proof != null && Patterns.WEB_URL.matcher(proof).matches()) {
                    r.setProof(proof);
                }
            }
            return r;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            MySQL.getInstance().closeStatement(ps);
            MySQL.getInstance().closeSet(rs);
            MySQL.getInstance().closeConnection(cs);
        }
        return null;
    }

    @Override
    public List<Report> getReport(String nick) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection cs = null;
        List<Report> reports = new ArrayList<>();
        GenericUser user = getDbm().getUserDatabaseMG().getUser(nick);
        try{
            cs = MySQL.getInstance().getConnection();
            ps = cs.prepareStatement("SELECT * FROM `dr_report` WHERE `player_reported` = ?");
            ps.setInt(1, user.getId());
            rs = ps.executeQuery();
            while(rs.next()){
                GenericUser reporter = getDbm().getUserDatabaseMG().getUserById(rs.getInt("player_reporter"));
                Report report = new Report(user, reporter, rs.getString("report_reason"), rs.getTimestamp("report_date"));
                report.setReportid(rs.getInt("id_report"));
                String proof = rs.getString("report_proof");
                if(proof != null && Patterns.WEB_URL.matcher(proof).matches()) {
                        report.setProof(proof);
                }
                reports.add(report);
            }
            return reports;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            MySQL.getInstance().closeStatement(ps);
            MySQL.getInstance().closeSet(rs);
            MySQL.getInstance().closeConnection(cs);
        }
        return null;
    }

    @Override
    public List<Report> getAllReports() {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection cs = null;
        List<Report> reports = new ArrayList<>();
        try{
            cs = MySQL.getInstance().getConnection();
            ps = cs.prepareStatement("SELECT * FROM `dr_report`");
            rs = ps.executeQuery();
            while(rs.next()){
                GenericUser reported = getDbm().getUserDatabaseMG().getUserById(rs.getInt("player_reported"));
                GenericUser reporter = getDbm().getUserDatabaseMG().getUserById(rs.getInt("player_reporter"));
                Report report = new Report(reported, reporter, rs.getString("report_reason"), rs.getTimestamp("report_date"));
                report.setReportid(rs.getInt("id_report"));
                String proof = rs.getString("report_proof");
                if(proof != null && Patterns.WEB_URL.matcher(proof).matches()) {
                    report.setProof(proof);
                }
                reports.add(report);
            }
            return reports;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            MySQL.getInstance().closeStatement(ps);
            MySQL.getInstance().closeSet(rs);
            MySQL.getInstance().closeConnection(cs);
        }
        return null;
    }
}
