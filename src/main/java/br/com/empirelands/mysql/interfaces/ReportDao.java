package br.com.empirelands.mysql.interfaces;

import br.com.empirelands.report.Report;

import java.util.List;

public interface ReportDao extends DaoUtils{

    Report getReport(int reportid);

    List<Report> getReport(String nick);

    List<Report> getAllReports();

}
