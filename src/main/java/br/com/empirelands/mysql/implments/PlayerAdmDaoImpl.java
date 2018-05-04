package br.com.empirelands.mysql.implments;

import br.com.empirelands.DreamReports;
import br.com.empirelands.exception.InvalidInput;
import br.com.empirelands.mysql.MySQL;
import br.com.empirelands.mysql.interfaces.PlayerAdmDao;
import br.com.empirelands.player.normal_user.GenericUser;
import br.com.empirelands.manager.DatabaseDaoManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PlayerAdmDaoImpl implements PlayerAdmDao{


    private DatabaseDaoManager db;

    public PlayerAdmDaoImpl(DatabaseDaoManager dbm) {
        this.db = dbm;
    }

    @Override
    public boolean add(List<Object> objectMap) throws InvalidInput {
        if (objectMap.size() != 2) {
            throw new InvalidInput("invalid input for table 'dr_staff', if persists contact the developer.");
        }
        PreparedStatement ps = null;
        Connection cs = null;
        try {
            cs = MySQL.getInstance().getConnection();
            ps = cs.prepareStatement("INSERT INTO dr_staff (`id_player`, `completed_reports`) VALUES(?,?)");
            ps.setObject(1, objectMap.get(0));
            ps.setObject(2, objectMap.get(1));
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
    public boolean delete(Object id) {
        PreparedStatement ps = null;
        Connection cs = null;
        try {
            cs = MySQL.getInstance().getConnection();
            ps = cs.prepareStatement("DELETE FROM `dr_staff` WHERE `id_player` = ?");
            ps.setObject(1, id);
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
    public boolean update(String column, Object id, Object value) {
        PreparedStatement ps = null;
        Connection cs = null;
        try {
            cs = MySQL.getInstance().getConnection();
            ps = cs.prepareStatement("UPDATE `dr_staff` SET `" + column + "` = ? WHERE `id_player` = ?");
            ps.setObject(1, value);
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
    public GenericUser getStaff(String nick) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection cs = null;
        GenericUser user = db.getUserDatabaseMG().getUser(nick);
        if(user == null) return null;
        try {
            cs = MySQL.getInstance().getConnection();
            ps = cs.prepareStatement("SELECT `id_player` FROM `dr_staff` WHERE `id_player` = ?");
            ps.setInt(1, user.getId());
            rs = ps.executeQuery();
            while (rs.next()) {
                user.setFinishedReports(rs.getInt("completed_reports"));
                DreamReports.getInstance().debugInfo("Staff Database Request to " + nick + " with " + rs.getInt("completed_reports") + " completed reports");
            }
                return user;
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
    public List<GenericUser> getAllStaffs() {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection cs = null;
        List<GenericUser> staffs = new ArrayList<>();
        try{
            cs = MySQL.getInstance().getConnection();
            ps = cs.prepareStatement("SELECT * FROM `dr_staff`");
            rs = ps.executeQuery();
            while(rs.next()){
                GenericUser staff = db.getUserDatabaseMG().getUserById(rs.getInt("id_player"));
                if(staff != null) {
                    staff.setFinishedReports(rs.getInt("completed_reports"));
                    staffs.add(staff);
                }
            }
            return staffs;
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
