package br.com.empirelands.util.database_util_methods.report_player_mg;

import br.com.empirelands.player.report_user.ReportPlayer;

import java.util.List;

public interface ReportPlayerDatabaseMG {

    ReportPlayer getReportedPlayer(String nick);

    List<ReportPlayer> getAllReportedPlayers();

}
