package br.com.empirelands.manager;

import br.com.empirelands.exception.ReportDoesNotExist;
import br.com.empirelands.report.Report;

import java.util.ArrayList;
import java.util.List;

public class ReportManager {

    private List<Report> reports;

    public ReportManager(List<Report> reports) {
        this.reports = reports;
    }

    public boolean createReport(Report r) {
       if(!hasReport(r.getReportid())){
           return false;
       }
        this.reports.add(r);
        return true;
    }

    public boolean deleteReport(int id) {
        if(!hasReport(id)){
            return false;
        }
        for(Report report : this.reports){
            if(report.getReportid() == id) {
                this.reports.remove(report);
                return true;
            }
        }
        return false;
    }

    public boolean hasReport(int id){
        for(Report report : reports){
            if(report.getReportid() == id){
                return true;
            }
        }
        return false;
    }

    public void removeAllReports(){
        this.reports.clear();
    }

    /**
     *
     * @param id id do report desejado
     * @return o report desejado
     * @throws ReportDoesNotExist lança esta exceção caso o id do report não exista
     */
    public Report getReport(int id) throws ReportDoesNotExist{
        if(!hasReport(id)){
            throw new ReportDoesNotExist("Report com o id [" + id + "] não foi encontrado!");
        }
        for(Report r : this.reports){
            if(r.getReportid() == id){
                return r;
            }
        }
        return null;
    }

    public List<Report> getReports(String nick){
        List<Report> rep = new ArrayList<>();
        for(Report r : reports){
            if(r.getReported().getNick().equalsIgnoreCase(nick)){
                rep.add(r);
            }
        }
        return rep;
    }

    public List<Report> getReports(){
        return this.reports;
    }
}
