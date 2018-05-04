package br.com.empirelands.mysql.interfaces;

import br.com.empirelands.player.normal_user.GenericUser;
import br.com.empirelands.report.ReportView;

import java.util.List;
import java.util.Set;

public interface ReportViewDao extends DaoUtils{

    Set<GenericUser> getViewers(int reportviewid);

    List<ReportView> getReportView(int userid);

}
