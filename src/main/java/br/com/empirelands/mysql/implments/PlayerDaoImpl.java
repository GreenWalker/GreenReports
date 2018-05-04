package br.com.empirelands.mysql.implments;

import br.com.empirelands.exception.InvalidInput;
import br.com.empirelands.manager.DatabaseDaoManager;
import br.com.empirelands.mysql.MySQL;
import br.com.empirelands.mysql.interfaces.PlayerDao;
import br.com.empirelands.player.normal_user.GenericUser;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerDaoImpl implements PlayerDao {

    private DatabaseDaoManager daoManager;

    public PlayerDaoImpl(DatabaseDaoManager daoManager) {
        this.daoManager = daoManager;
    }

    @Override
    public boolean add(List<Object> objectMap) throws InvalidInput {
        if (objectMap.size() != 4) {
            throw new InvalidInput("invalid input for table 'dr_user', if persists contact the developer.");
        }
        PreparedStatement ps = null;
        Connection cs = null;
        try {
            cs = MySQL.getInstance().getConnection();
            ps = cs.prepareStatement("INSERT INTO dr_user (`id_player`, `uuid`, `nick`, `reported_times`, `reports_sends`,`register_date`) VALUES(default, ?, ?, ?, ?, ?)");
            ps.setObject(1, objectMap.get(0));
            ps.setObject(2, objectMap.get(1));
            ps.setObject(3, objectMap.get(2));
            ps.setObject(4, objectMap.get(3));
            ps.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
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
            ps = cs.prepareStatement("DELETE FROM dr_user WHERE id_player = ?");
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
    public boolean update(String column, Object id, Object value) {
        PreparedStatement ps = null;
        Connection cs = null;
        try {
            cs = MySQL.getInstance().getConnection();
            ps = cs.prepareStatement("UPDATE dr_user SET `" + column + "` = ? WHERE `id_player` = ?");
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
    public GenericUser getUser(String nick) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection cs = null;
        GenericUser user = null;
        try {
            cs = MySQL.getInstance().getConnection();
            ps = cs.prepareStatement("SELECT * FROM `dr_user` WHERE `nick` = ?");
            ps.setString(1, nick);
            rs = ps.executeQuery();
            while (rs.next()) {
                String nc = rs.getString("nick");
                if (nc.equals(nick)) {
                    user = new GenericUser(nick, UUID.fromString(rs.getString("uuid")), rs.getInt("reported_times"));
                    user.setId(rs.getInt("id_player"));
                    user.setReports_sends(rs.getInt("reports_sends"));
                    user.setRegister_date(rs.getTimestamp("register_date"));
                    user.setBanned(daoManager.getBanDatabaseMG().isBanned(user));
                    break;
                }
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
    public GenericUser getUser(int id) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection cs = null;
        GenericUser user = null;
        try {
            cs = MySQL.getInstance().getConnection();
            ps = cs.prepareStatement("SELECT * FROM `dr_user` WHERE `id_player` = ?");
            ps.setInt(1, id);
            rs = ps.executeQuery();
            while (rs.next()) {
                int pid = rs.getInt("id_player");
                if (id == pid) {
                    user = new GenericUser(rs.getString("nick"), UUID.fromString(rs.getString("uuid")), rs.getInt("reported_times"));
                    user.setId(pid);
                    user.setReports_sends(rs.getInt("reports_sends"));
                    user.setRegister_date(rs.getTimestamp("register_date"));
                    user.setBanned(daoManager.getBanDatabaseMG().isBanned(user));
                    break;
                }
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
    public List<GenericUser> getAllUsers() {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection cs = null;
        List<GenericUser> users = new ArrayList<>();
        try {
            cs = MySQL.getInstance().getConnection();
            ps = cs.prepareStatement("SELECT * FROM `dr_user`");
            rs = ps.executeQuery();
            while (rs.next()) {
                int id            = rs.getInt("id_player");
                String nick       = rs.getString("nick");
                UUID playerid     = UUID.fromString(rs.getString("uuid"));
                int reportedTimes = rs.getInt("reported_times");
                int reports_sends = rs.getInt("reports_sends");
                Timestamp date    = rs.getTimestamp("register_date");

                GenericUser user  = new GenericUser(nick, playerid, reportedTimes);
                user.setId(id);
                user.setReports_sends(reports_sends);
                user.setRegister_date(date);
                user.setBanned(daoManager.getBanDatabaseMG().isBanned(user));
                users.add(user);
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
}
