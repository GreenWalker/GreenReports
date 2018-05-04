package br.com.empirelands.util.database_util_methods.reportview_mg;

import br.com.empirelands.exception.InvalidInput;
import br.com.empirelands.mysql.DaoManager;
import br.com.empirelands.player.normal_user.GenericUser;
import br.com.empirelands.report.Report;
import br.com.empirelands.report.ReportView;
import br.com.empirelands.util.database_util_methods.report_mg.ReportDatabaseMG;
import br.com.empirelands.util.database_util_methods.staff_mg.StaffDatabaseMG;
import lombok.Getter;

import java.util.*;

public class ReportViewDatabaseImpl implements ReportViewDatabaseMG {

    @Getter
    private DaoManager daoManager;
    @Getter
    private StaffDatabaseMG mg;
    @Getter
    private ReportDatabaseMG mg2;

    public ReportViewDatabaseImpl(DaoManager daoManager, StaffDatabaseMG mg, ReportDatabaseMG mg2) {
        this.daoManager = daoManager;
        this.mg = mg;
        this.mg2 = mg2;
    }

    @Override
    public List<ReportView> getUserViewReports(int userid) {
        return getDaoManager().getReportViewDao().getReportView(userid);
    }

    @Override
    public void addReportView(GenericUser viewer, Report reportView) {
        List<Object> obj = new ArrayList<>();
        obj.add(reportView.getReportid());
        obj.add(viewer.getId());
        try {
            getDaoManager().getReportViewDao().add(obj);
        } catch (InvalidInput invalidInput) {
            invalidInput.printStackTrace();
        }
    }

    @Override
    public void removeReportView(int reportid) {
        getDaoManager().getReportViewDao().delete(reportid);
    }

    @Override
    public Set<GenericUser> getUserThatViewSameReport(int reportid) {
        Set<GenericUser> users = new HashSet<>();
        for (Report r : mg2.getAllReports()) {
            if (r.getReportid() == reportid) {
                users = getDaoManager().getReportViewDao().getViewers(reportid);
                break;
            }
        }
        return users;
    }

    @Override
    public void removeAllViews() {
        for(Report view : mg2.getAllReports()){
            getDaoManager().getReportViewDao().delete(view.getReportid());
        }
    }
}
