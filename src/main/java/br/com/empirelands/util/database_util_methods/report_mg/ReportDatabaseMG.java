package br.com.empirelands.util.database_util_methods.report_mg;

import br.com.empirelands.report.Report;

import java.util.List;

public interface ReportDatabaseMG {

    Report getReport(int reportid);

    List<Report> getReport(String nick);

    boolean addReport(Report r);

    boolean removeReport(Report r);

    boolean updateMotive(Report r, String newReason);

    boolean updateReportId(Report r, long newReportId);

    List<Report> getAllReports();

    void removeAllReport();
}
