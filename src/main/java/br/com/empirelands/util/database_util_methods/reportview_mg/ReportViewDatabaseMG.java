package br.com.empirelands.util.database_util_methods.reportview_mg;

import br.com.empirelands.player.normal_user.GenericUser;
import br.com.empirelands.report.Report;
import br.com.empirelands.report.ReportView;

import java.util.List;
import java.util.Set;

public interface ReportViewDatabaseMG {

    List<ReportView> getUserViewReports(int userid);

    void addReportView(GenericUser viewer, Report reportView);

    void removeReportView(int reportid);

    Set<GenericUser> getUserThatViewSameReport(int reportid);

    void removeAllViews();

}
