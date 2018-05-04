package br.com.empirelands.listeners;

import br.com.empirelands.DreamReports;
import br.com.empirelands.listeners.events.PlayerCloseReportEvent;
import br.com.empirelands.player.normal_user.GenericUser;
import br.com.empirelands.player.report_user.ReportPlayer;
import br.com.empirelands.reflection.NmsVersion;
import br.com.empirelands.report.Report;
import br.com.empirelands.util.JsonVariables;
import br.com.empirelands.util.Util;
import br.com.empirelands.util.UtilMethods;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashSet;
import java.util.Set;

public class CloseReportHandler implements Listener {

    private NmsVersion nms;
    private UtilMethods utilMethods;
    @Getter
    private static Set<String> toRemoveEffects = new HashSet<>();

    public CloseReportHandler(NmsVersion nms, UtilMethods utilMethods) {
        this.nms = nms;
        this.utilMethods = utilMethods;
    }

    @EventHandler
    public void onReportClose(PlayerCloseReportEvent event) {
        Report report = event.getReport();
        ReportPlayer p = utilMethods.getReportPlayer(report.getReported().getUuid());
        if (p != null) {
            GenericUser us = event.getReport().getReporter();
            if (us != null && us.isOnline()) {
                DreamReports.getInstance().debugInfo("CloseReport: Reporter name " + report.getReporter().getNick());
                DreamReports.getInstance().debugInfo("CloseReport: Reported name " + p.getNick());
                DreamReports.getInstance().debugInfo("CloseReport: Reported is banned " + p.isBanned() + " " + utilMethods.isBannedPlayer(p).get());
                String s = utilMethods.isBannedPlayer(p).get() ? JsonVariables.PLAYER_BANNED.getMsg() : JsonVariables.PLAYER_NON_BANNED.getMsg();
                nms.sendTitle(us.toPlayer(), s, JsonVariables.REPORT_CLOSED.getMsg().replaceAll("%contra%", p.getNick()), 30, 60, 40);
            }
            DreamReports.getInstance().info("CloseReport: Reporter is null " + (us == null));
            if (p.isAlreadyLogout()) {
                if (p.isOnline()) {
                    Util.getInstancie().removePlayerEffects(p.toPlayer());
                } else {
                    if (!toRemoveEffects.contains(p.getNick())) {
                        toRemoveEffects.add(p.getNick());
                    }
                }
            }
        }
        DreamReports.getInstance().debugInfo("CloseReport: ReportPlayer is null " + (p == null));
    }
}
