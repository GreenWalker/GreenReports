package br.com.empirelands.mysql;

import br.com.empirelands.ConsoleLogguerManager;
import br.com.empirelands.DreamReports;
import org.bukkit.configuration.file.FileConfiguration;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MySQL extends MySQLUtils {

    // Classe singleton
    private static MySQL instance;
    private FileConfiguration config;
    private String url;
    private String user;
    private String password;
    private String table;
    private String ip;
    private String port;

    private MySQL() {
        try {
            config = DreamReports.getInstance().getPluginConfig();
            ip = config.getString("host").trim();
            port = config.getString("porta").trim();
            table = config.getString("servidor").trim();
            url = "jdbc:mysql://" + ip + ":" + port + "/" + table;
            user = config.getString("usuario").trim();
            password = config.getString("senha");
            getConnection();
            createTables();
        } catch (SQLException e) {
           ConsoleLogguerManager.getInstance().logSevere("Erro ao se conectar ao banco de dados! Desabilitando plugin");
           DreamReports.getInstance().getServer().getPluginManager().disablePlugin(DreamReports.getInstance());
           e.printStackTrace();
        }
            DreamReports.getInstance().info("&aConectado com o banco de dados!");
    }

    public static MySQL getInstance() {
        if (instance == null) {
            instance = new MySQL();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return DriverManager.getConnection(url, user, password);
    }

    public void createTables() {
            PreparedStatement ps = null;
            Connection c = null;
        try {
            c = getConnection();

            // table dr_player
            ps = c.prepareStatement("CREATE TABLE IF NOT EXISTS dr_user ( `id_player` int(8) AUTO_INCREMENT, `uuid` varchar(50) NOT NULL UNIQUE, `nick` varchar(25) NOT NULL, `reported_times` int(8), `reports_sends` int(8), `register_date` timestamp default current_timestamp(), PRIMARY KEY(`id_player`)) ENGINE=InnoDB DEFAULT CHARSET=utf8");
            ps.executeUpdate();
            closeStatement(ps);

            // table dr_report
            ps = c.prepareStatement("CREATE TABLE IF NOT EXISTS dr_report (`id_report` int(8) NOT NULL, `player_reported` int(8) NOT NULL, `player_reporter` int(8) NOT NULL, `report_reason` varchar(50) NOT NULL, `report_date` timestamp default current_timestamp(), `report_proof` text, PRIMARY KEY(`id_report`), FOREIGN KEY(`player_reported`) REFERENCES `dr_user` (`id_player`) ON UPDATE CASCADE ON DELETE CASCADE, FOREIGN KEY(`player_reporter`) REFERENCES `dr_user` (`id_player`) ON UPDATE CASCADE ON DELETE CASCADE) ENGINE=InnoDB DEFAULT CHARSET=utf8");
            ps.executeUpdate();
            closeStatement(ps);

            // table dr_staff
            ps = c.prepareStatement("CREATE TABLE IF NOT EXISTS dr_staff (`id_player` int(8), `completed_reports` int(4), PRIMARY KEY(`id_player`), FOREIGN KEY(`id_player`) REFERENCES `dr_user` (`id_player`) ON UPDATE CASCADE ON DELETE CASCADE) ENGINE=InnoDB DEFAULT CHARSET=utf8");
            ps.executeUpdate();
            closeStatement(ps);

            // table dr_report_view
            ps = c.prepareStatement("CREATE TABLE IF NOT EXISTS dr_report_view ( `id_view` int(8) AUTO_INCREMENT, `id_report` int(8) NOT NULL, `id_player` int(8), `view_date` timestamp default current_timestamp(), PRIMARY KEY (`id_view`), FOREIGN KEY(`id_report`) REFERENCES `dr_report` (`id_report`) ON UPDATE CASCADE ON DELETE CASCADE, FOREIGN KEY(`id_player`) REFERENCES `dr_staff` (`id_player`) ON UPDATE CASCADE ON DELETE CASCADE) ENGINE=InnoDB DEFAULT CHARSET=utf8");
            ps.executeUpdate();
            closeStatement(ps);

            // table dr_banneds
            ps = c.prepareStatement("CREATE TABLE IF NOT EXISTS dr_banneds ( `id_ban` int(8) AUTO_INCREMENT, `id_banned` int(8) UNIQUE, `ban_date` timestamp default current_timestamp(), PRIMARY KEY (`id_ban`), FOREIGN KEY(`id_banned`) REFERENCES `dr_user` (`id_player`) ON UPDATE CASCADE ON DELETE CASCADE) ENGINE=InnoDB DEFAULT CHARSET=utf8");
            ps.executeUpdate();
            closeStatement(ps);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeStatement(ps);
            closeConnection(c);
        }
    }
}

