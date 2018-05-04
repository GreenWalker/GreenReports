package br.com.empirelands.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class MySQLUtils {

    public void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException  e) {
                e.printStackTrace();
            }
        }

    }

    public void closeStatement(PreparedStatement ps) {
        if (ps != null)
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }

    public void closeSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException  e) {
                e.printStackTrace();
            }
        }

    }

    public abstract void createTables();

    public abstract Connection getConnection() throws SQLException;
}

