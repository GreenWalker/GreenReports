package br.com.empirelands.mysql.implments;

import br.com.empirelands.exception.InvalidInput;
import br.com.empirelands.mysql.MySQL;
import br.com.empirelands.mysql.interfaces.ReportViewDao;
import br.com.empirelands.player.normal_user.GenericUser;
import br.com.empirelands.report.Report;
import br.com.empirelands.report.ReportView;
import br.com.empirelands.manager.DatabaseDaoManager;
import lombok.Getter;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ReportViewImpl implements ReportViewDao {

    @Getter
    private DatabaseDaoManager dbm;

    public ReportViewImpl(DatabaseDaoManager dbm) {
        this.dbm = dbm;
    }

    @Override
    public boolean add(List<Object> objectMap) throws InvalidInput { // index 1 = id_report, index 2 = id_player
        if (objectMap.size() != 2) {
            throw new InvalidInput("invalid input for table 'dr_report_view', need values 'id_report' and 'id_player', if persists contact the developer.");
        }
        PreparedStatement ps = null;
        Connection cs = null;
        try {
            cs = MySQL.getInstance().getConnection();
            ps = cs.prepareStatement("INSERT INTO dr_report_view (`id_report`, `id_player`, `view_date`) VALUES(?, ?, ?)");
            ps.setObject(1, objectMap.get(0));
            ps.setObject(2, objectMap.get(1));
            ps.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
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
    public boolean delete(Object idview) {
        PreparedStatement ps = null;
        Connection cs = null;
        try {
            cs = MySQL.getInstance().getConnection();
            ps = cs.prepareStatement("DELETE FROM `dr_report_view` WHERE `id_report` = ?");
            ps.setObject(1, idview);
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
    public boolean update(String column, Object id, Object v) {
        PreparedStatement ps = null;
        Connection cs = null;
        try {
            cs = MySQL.getInstance().getConnection();
            ps = cs.prepareStatement("UPDATE `dr_report_view` SET `" + column + "` = ? WHERE `id_view` = ?");
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
    public Set<GenericUser> getViewers(int reportid) {
        Set<GenericUser> users = new HashSet<>();
        Set<String> alreadyNicks = new HashSet<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection cs = null;
        try {
            cs = MySQL.getInstance().getConnection();
            ps = cs.prepareStatement("SELECT `id_player` FROM `dr_report_view` WHERE `id_report` = ?");
            ps.setInt(1, reportid);
            rs = ps.executeQuery();
            while (rs.next()) {
                GenericUser user = getDbm().getStaffDatabaseMG().getStaff(rs.getInt("id_player"));
                if (user != null && !alreadyNicks.contains(user.getNick())) {
                    users.add(user);
                    alreadyNicks.add(user.getNick());
                    System.out.println("Ola " + user.getNick());
                    alreadyNicks.forEach(s -> System.out.println("nick " + s));
                }
            }
            return users;
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
    public List<ReportView> getReportView(int userid) {
        List<ReportView> reports = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection cs = null;
        try {
            cs = MySQL.getInstance().getConnection();
            ps = cs.prepareStatement("SELECT * FROM dr_report_view WHERE `id_player` = ?");
            ps.setInt(1, userid);
            rs = ps.executeQuery();
            while (rs.next()) {
                Report report = getDbm().getReportDatabaseMG().getReport(rs.getInt("id_report"));
                GenericUser user = getDbm().getStaffDatabaseMG().getStaff(userid);
                if (report != null && user != null) {
                    reports.add(new ReportView(user, report, rs.getTimestamp("view_date")));
                }
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
