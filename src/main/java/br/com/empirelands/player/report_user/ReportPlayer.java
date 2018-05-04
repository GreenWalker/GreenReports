package br.com.empirelands.player.report_user;

import br.com.empirelands.DreamReports;
import br.com.empirelands.player.normal_user.GenericUser;
import br.com.empirelands.report.Report;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class ReportPlayer extends GenericUser {

    @Getter
    private List<Report> reports;
    @Getter
    private Report lastReport;
    @Setter
    @Getter
    private boolean alreadyLogout;

    public ReportPlayer(int id, String nick, UUID uuid, int reportedTimes, List<Report> reports) {
        super(nick, uuid, reportedTimes);
        super.setId(id);
        this.reports = reports;
    }

    public ReportPlayer(GenericUser user, List<Report> reports) {
        super(user.getNick(), user.getUuid(), user.getReportedAllTimes());
        super.setId(user.getId());
        if (reports == null) {
            reports = new ArrayList<>();
        }
        this.reports = reports;
    }

    public void newReport(Report report) {
        this.lastReport = report;
        this.reports.add(report);
    }

    public void removeReport(int id) {
        Report toRemove = null;
        for (Report r : reports) {
            if (r.getReportid() == id) {
                toRemove = r;
            }
        }
        if (toRemove != null) {
            reports.remove(toRemove);
            if(lastReport != null && lastReport.getReportid() == toRemove.getReportid()){
                lastReport = null;
            }
        }
    }

    public String getAllReportsString() {
        StringBuilder sb = new StringBuilder();
        boolean size = reports.size() > 1;
        int i = 1;
        int b = 1;
        for (Report reasons : reports) {
            if (size) {
                sb.append(reasons.getMotive());
                if (i != reports.size()) {
                    sb.append(", ");
                }
                if (b == 4) {
                    sb.append(", ").append(System.getProperty("line.separator"));
                    b = 0;
                }
                i++;
                b++;
            } else sb.append(reasons.getMotive());
        }
        return sb.toString();
    }

}
