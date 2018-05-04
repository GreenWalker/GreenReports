package br.com.empirelands.util.database_util_methods.report_mg;

import br.com.empirelands.exception.InvalidInput;
import br.com.empirelands.mysql.DaoManager;
import br.com.empirelands.report.Report;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class ReportDatabaseImpl implements ReportDatabaseMG{

    @Getter
    private DaoManager daoManager;

    public ReportDatabaseImpl(DaoManager daoManager) {
        this.daoManager = daoManager;
    }

    @Override
    public Report getReport(int reportid) {
        return getDaoManager().getReportDao().getReport(reportid);
    }

    @Override
    public List<Report> getReport(String nick) {
        return getDaoManager().getReportDao().getReport(nick);
    }

    @Override
    public boolean addReport(Report r) {
        List<Object> obj = new ArrayList<>();
        obj.add(0, r.getReportid()); // index 0 - id_report
        obj.add(1, r.getReported().getId()); // index 1 - player_reported
        obj.add(2, r.getReporter().getId()); // index 2 - player_reporter
        obj.add(3, r.getMotive());   // index 3 - report_reason
        if(r.getProof() == null){
            obj.add(4, "null"); // index 4 - report_proof
        } else obj.add(4, r.getProof());
        try {
            getDaoManager().getReportDao().add(obj);
            return true;
        } catch (InvalidInput invalidInput) {
            invalidInput.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean removeReport(Report r) {
        return getDaoManager().getReportDao().delete(r.getReportid());
    }

    @Override
    public boolean updateMotive(Report r, String newReason) {
        return updateReport("report_reason", r.getReportid(), newReason);
    }

    @Override
    public boolean updateReportId(Report r, long newReportId) {
        return updateReport("id_report", r.getReportid(), newReportId);
    }

    private boolean updateReport(String column, Object id, Object value) {
        return getDaoManager().getReportDao().update(column,id, value);
    }

    @Override
    public List<Report> getAllReports() {
        return getDaoManager().getReportDao().getAllReports();
    }

    @Override
    public void removeAllReport() {
        for(Report r : getAllReports()){
            getDaoManager().getReportDao().delete(r.getReportid());
        }
    }
}
