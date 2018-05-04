package br.com.empirelands.mysql.implments;

import br.com.empirelands.exception.InvalidInput;
import br.com.empirelands.manager.DatabaseDaoManager;
import br.com.empirelands.mysql.MySQL;
import br.com.empirelands.mysql.interfaces.BanDao;
import br.com.empirelands.player.normal_user.GenericUser;
import br.com.empirelands.util.objects.UserBanned;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Criado por Floydz.
 */
public class BanDaoImpl implements BanDao {

    private DatabaseDaoManager databaseDaoManager;

    public BanDaoImpl(DatabaseDaoManager databaseDaoManager) {
        this.databaseDaoManager = databaseDaoManager;
    }

    @Override
    public boolean isBanned(GenericUser user) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection cs = null;
        try {
            cs = MySQL.getInstance().getConnection();
            ps = cs.prepareStatement("SELECT `id_banned` FROM dr_banneds WHERE `id_banned` = ?");
            ps.setInt(1, user.getId());
            rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            MySQL.getInstance().closeStatement(ps);
            MySQL.getInstance().closeSet(rs);
            MySQL.getInstance().closeConnection(cs);
        }
    }

    @Override
    public boolean isBanned(String nick) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection cs = null;
        GenericUser user = databaseDaoManager.getUserDatabaseMG().getUser(nick);
        if (user == null) return false;
        try {
            cs = MySQL.getInstance().getConnection();
            ps = cs.prepareStatement("SELECT `id_banned` FROM dr_banneds WHERE `id_banned` = ?");
            ps.setInt(1, user.getId());
            rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            MySQL.getInstance().closeStatement(ps);
            MySQL.getInstance().closeSet(rs);
            MySQL.getInstance().closeConnection(cs);
        }
    }

    @Override
    public boolean isBanned(int id) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection cs = null;
        try {
            cs = MySQL.getInstance().getConnection();
            ps = cs.prepareStatement("SELECT `id_banned` FROM dr_banneds WHERE `id_banned` = ?");
            ps.setInt(1, id);
            rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            MySQL.getInstance().closeStatement(ps);
            MySQL.getInstance().closeSet(rs);
            MySQL.getInstance().closeConnection(cs);
        }
    }

    @Override
    public List<UserBanned> getAllBanneds() {
        List<UserBanned> users = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection cs = null;
        try {
            cs = MySQL.getInstance().getConnection();
            ps = cs.prepareStatement("SELECT * FROM dr_banneds");
            rs = ps.executeQuery();
            while (rs.next()) {
                GenericUser user = databaseDaoManager.getUserDatabaseMG().getUserById(rs.getInt("id_banned"));
                if (user != null) {
                    users.add(new UserBanned(rs.getInt("id_ban"), user, rs.getTimestamp("ban_date")));
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
    public boolean add(List<Object> objectMap) throws InvalidInput {
        if (objectMap.size() != 1) {
            throw new InvalidInput("invalid input for table 'dr_banneds', if persists contact the developer.");
        }
        PreparedStatement ps = null;
        Connection cs = null;
        try {
            cs = MySQL.getInstance().getConnection();
            ps = cs.prepareStatement("INSERT INTO `dr_banneds` (`id_ban`, `id_banned`, `ban_date`) VALUES(default, ?,?)");
            ps.setObject(1, objectMap.get(0));
            ps.setObject(2, new Timestamp(System.currentTimeMillis()));
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
            ps = cs.prepareStatement("DELETE FROM `dr_banneds` WHERE `id_banned` = ?");
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
            ps = cs.prepareStatement("UPDATE `dr_banneds` SET `" + column + "` = ? WHERE `id_banned` = ?");
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
}
