package br.com.empirelands.listeners;

import br.com.empirelands.DreamReports;
import br.com.empirelands.listeners.events.PlayerViewReportEvent;
import br.com.empirelands.report.Report;
import br.com.empirelands.manager.DatabaseDaoManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * Criado por Floydz.
 */
public class SeeReportEvent implements Listener {

    private DatabaseDaoManager daoManager;

    public SeeReportEvent(DatabaseDaoManager daoManager) {
        this.daoManager = daoManager;
    }

    @EventHandler
    public void onSeeReport(PlayerViewReportEvent event) {
        if (event.getViewer().isStaff()) {
            daoManager.getReportViewDatabaseMG().addReportView(event.getViewer(), event.getReport());
            DreamReports.getInstance().debugInfo("Player " + event.getViewer().getNick() + " see an report with id " + event.getReport().getReportid() + " with progress by " + event.getReport().getProcess().name());
            if(event.getReport().getProcess() == Report.ReportProcess.IN_WAITING){
                event.getReport().setProcess(Report.ReportProcess.IN_PROGRESS);
                DreamReports.getInstance().debugInfo("Report " + event.getReport().getReportid() + " recieve new process " + event.getReport().getProcess());
            }
        }
    }
}
