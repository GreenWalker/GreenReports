package br.com.empirelands.util;

import br.com.empirelands.ConsoleLogguerManager;
import br.com.empirelands.DreamReports;
import br.com.empirelands.config.ConfigHandler;
import br.com.empirelands.listeners.events.PlayerCloseReportEvent;
import br.com.empirelands.listeners.events.PlayerCreateReportEvent;
import br.com.empirelands.manager.GenericUserManager;
import br.com.empirelands.player.normal_user.GenericUser;
import br.com.empirelands.player.report_user.ReportPlayer;
import br.com.empirelands.manager.ReportPlayerManager;
import br.com.empirelands.reflection.NmsVersion;
import br.com.empirelands.report.Report;
import br.com.empirelands.manager.ReportManager;
import br.com.empirelands.manager.DatabaseDaoManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public class UtilMethods {

    private NmsVersion nms;
    private GenericUserManager genericUserManager;
    private ReportManager reportManager;
    private ReportPlayerManager reportPlayerManager;
    private DatabaseDaoManager daoManager;

    public UtilMethods(GenericUserManager genericUserManager, ReportManager reportManager, ReportPlayerManager reportPlayerManager, DatabaseDaoManager daoManager, NmsVersion version, ConfigHandler l) {
        this.genericUserManager = genericUserManager;
        this.reportManager = reportManager;
        this.reportPlayerManager = reportPlayerManager;
        this.daoManager = daoManager;
        this.nms = version;
    }

    public Report createReport(GenericUser reporter, GenericUser reported, String motive, String proof) {
        Report r = new Report(reported, reporter, motive, new Timestamp(System.currentTimeMillis()));
        r.setProof(proof);
        new BukkitRunnable() {
            @Override
            public void run() {
                ReportPlayer reportPlayer = reportPlayerManager.getPlayer(reported.getNick());
                if (reportPlayer == null || reportPlayer.getReports() == null) {
                    reportPlayer = new ReportPlayer(reported, new ArrayList<>());
                    reportPlayerManager.addPlayer(reportPlayer);
                }
                reportPlayer.newReport(r);
                daoManager.getReportDatabaseMG().addReport(r);

                // reporter
                int i = reporter.getReports_sends();
                reporter.setReports_sends(i + 1);
                daoManager.getUserDatabaseMG().updateReportSents(reporter, i + 1);

                // reported
                int j = reported.getReportedAllTimes();
                reported.setReportedAllTimes(j + 1);
                reported.setReportedTimesAtLastRestart(reported.getReportedTimesAtLastRestart() + 1);
                daoManager.getUserDatabaseMG().updateReportedTimes(reported, j + 1);
                nms.sendTitle(reporter.toPlayer(), JsonVariables.REPORT_CREATED_SUCCEFULL.getMsg(), JsonVariables.REPORT_CREATED_THANKS.getMsg(), 30, 60, 40);
                Bukkit.getServer().getPluginManager().callEvent(new PlayerCreateReportEvent(reporter, reportPlayer, motive, r));
            }
        }.runTaskAsynchronously(DreamReports.getInstance());

        return r;
    }

    public GenericUser createNewUser(Player p) {
        if (p != null) {
            if (!genericUserManager.existPlayer(p.getUniqueId())) {
                GenericUser user = new GenericUser(p.getName(), p.getUniqueId(), 0);
                daoManager.getUserDatabaseMG().addUser(user);
                user = daoManager.getUserDatabaseMG().getUser(p.getName());
                genericUserManager.addPlayer(user);
                ConsoleLogguerManager.getInstance().log("Player " + user.getNick() + " adicionado ao banco de dados na tabela 'dr_user'.");
                return user;
            }
        }
        return null;
    }

    public void deleteReport(GenericUser whoDelet, ReportPlayer player, int reportid) {
        if (player != null) {
            if (player.getReports() != null && player.getReports().size() != 0) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        for (Report r : player.getReports()) {
                            if (r.getReportid() == reportid) {
                                Bukkit.getPluginManager().callEvent(new PlayerCloseReportEvent(whoDelet, r));
                                player.removeReport(reportid);
                                daoManager.getReportDatabaseMG().removeReport(r);
                                reportManager.deleteReport(reportid);
                                DreamReports.getInstance().debugInfo("Deleted an report id " + reportid + " of " + player.getNick() + " by " + whoDelet.getNick() + " current size of reports now " + player.getReports().size());
                                if (player.getReports().size() < 1) {
                                    reportPlayerManager.removePlayer(player);
                                }

                            }
                        }
                    }
                }.runTaskAsynchronously(DreamReports.getInstance());
            }
        }
    }

    public void banPlayer(GenericUser banned, String reason) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!banned.isBanned()) {
                    banned.setBanned(true);
                    daoManager.getBanDatabaseMG().add(banned);
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ban " + banned.getNick() + " " + reason);
                }
            }
        }.runTaskAsynchronously(DreamReports.getInstance());
    }

    public void banPlayer(GenericUser banned, Player punisher, String cmd) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!banned.isBanned()) {
                    banned.setBanned(true);
                    daoManager.getBanDatabaseMG().add(banned);
                    Bukkit.dispatchCommand(punisher, cmd);
                }

            }
        }.runTaskAsynchronously(DreamReports.getInstance());
    }

    public void deleteReport(GenericUser whoDelet, Report r) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (r != null) {
                    ReportPlayer player = getReportPlayer(r.getReported().getNick());
                    if (player != null && player.getReports() != null && player.getReports().size() != 0) {
                        Bukkit.getPluginManager().callEvent(new PlayerCloseReportEvent(whoDelet, r));
                        daoManager.getReportViewDatabaseMG().removeReportView(r.getReportid());
                        player.removeReport(r.getReportid());
                        daoManager.getReportDatabaseMG().removeReport(r);
                        reportManager.deleteReport(r.getReportid());
                    } else {
                        reportPlayerManager.removePlayer(player);
                    }
                }
            }
        }.runTaskAsynchronously(DreamReports.getInstance());
    }

    public boolean deleteAllPlayerReports(GenericUser whoDelet, ReportPlayer reportPlayer) {
        if (reportPlayer != null) {
            for (Report r : daoManager.getReportDatabaseMG().getReport(reportPlayer.getNick())) {
                Bukkit.getPluginManager().callEvent(new PlayerCloseReportEvent(whoDelet, r));
                daoManager.getReportViewDatabaseMG().removeReportView(r.getReportid());
                daoManager.getReportDatabaseMG().removeReport(r);
                reportManager.deleteReport(r.getReportid());
            }
            reportPlayerManager.removePlayer(reportPlayer);
            return true;
        }
        return false;
    }

    public GenericUser getStaff(GenericUser args) {
        return daoManager.getStaffDatabaseMG().getStaff(args.getId());
    }

    public ReportPlayer getReportPlayer(UUID player) {
        if (reportPlayerManager.existPlayer(player)) {
            return reportPlayerManager.getPlayer(player);
        }
        return null;
    }

    public GenericUser getUser(Player p) {
        if (genericUserManager.existPlayer(p.getUniqueId())) {
            return genericUserManager.getPlayer(p.getUniqueId());
        }
        return null;
    }

    public ReportPlayer getReportPlayer(String player) {
        return reportPlayerManager.getPlayer(player);
    }

    public GenericUser getUser(String p) {
        if (genericUserManager.existPlayer(p)) {
            return genericUserManager.getPlayer(p);
        }
        return null;
    }

    public Report getReport(int reportid) {
        try {
            return daoManager.getReportDatabaseMG().getReport(reportid);
        } catch (Exception reportDoesNotExist) {
            reportDoesNotExist.printStackTrace();
        }
        return null;
    }

    public void transformAllOnlinePlayersInUsers(boolean b) {
        new BukkitRunnable() {
            @Override
            public void run() {

                if (b) {
                    int i = 0;
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        try {
                            if (genericUserManager.getPlayer(p.getUniqueId()) == null) {
                                GenericUser uuuser = new GenericUser(p.getName(), p.getUniqueId(), 0);
                                genericUserManager.addPlayer(uuuser);
                                daoManager.getUserDatabaseMG().addUser(uuuser);
                                i++;
                            }
                        } catch (Exception ex) {
                            ConsoleLogguerManager.getInstance().logWarn("Erro ao transformar " + p.getName() + " em user! erro: " + ex.getMessage() + " na linha" + ex.getLocalizedMessage());
                        }
                    }
                    ConsoleLogguerManager.getInstance().log(i + " Players transformados em usuarios e inseridos no banco de dados.");
                }
            }
        }.runTaskAsynchronously(DreamReports.getInstance());
    }

    public void removeAllReports() {
        new BukkitRunnable() {
            @Override
            public void run() {

                daoManager.getReportViewDatabaseMG().removeAllViews();
                daoManager.getReportDatabaseMG().removeAllReport();
                reportManager.removeAllReports();
                reportPlayerManager.removeAllPlayers();
            }
        }.runTaskAsynchronously(DreamReports.getInstance());
    }

    public void removeAllReports(GenericUser whoDelet) {
        new BukkitRunnable() {
            @Override
            public void run() {

                for (Report r : daoManager.getReportDatabaseMG().getAllReports()) {
                    try {
                        Bukkit.getPluginManager().callEvent(new PlayerCloseReportEvent(whoDelet, r));
                        daoManager.getReportViewDatabaseMG().removeReportView(r.getReportid());
                        daoManager.getReportDatabaseMG().removeReport(r);
                        reportManager.deleteReport(r.getReportid());
                        reportPlayerManager.removePlayer(r.getReported().getNick());
                    } catch (Exception ignored) {
                        ConsoleLogguerManager.getInstance().log(ignored.getMessage());
                    }
                }
            }
        }.runTaskAsynchronously(DreamReports.getInstance());
    }

    public AtomicBoolean isBannedPlayer(GenericUser toVerify) {
        AtomicBoolean isBanned = new AtomicBoolean();
        new BukkitRunnable() {
            @Override
            public void run() {
                isBanned.set(daoManager.getBanDatabaseMG().isBanned(toVerify));
            }
        }.runTaskAsynchronously(DreamReports.getInstance());

        return isBanned;
    }
}
