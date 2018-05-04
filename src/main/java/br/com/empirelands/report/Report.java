package br.com.empirelands.report;

import br.com.empirelands.player.normal_user.GenericUser;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Random;

public class Report {

    @Getter
    @Setter
    private int reportid;
    @Getter
    @Setter
    private GenericUser reported;
    @Getter
    @Setter
    private GenericUser reporter;
    @Getter
    @Setter
    private String motive;
    @Getter
    @Setter
    private String proof;
    @Getter
    @Setter
    private ReportProcess process;
    @Getter
    @Setter
    private Timestamp report_date;

    public Report(GenericUser reported, GenericUser reporter, String motive, Timestamp report_date) {
        this.reported = reported;
        this.reporter = reporter;
        this.motive = motive;
        this.report_date = report_date;
        this.process = ReportProcess.IN_WAITING;
        this.reportid = new Random().nextInt(100_000);
    }

    public void generateNewId(){
        this.reportid = new Random().nextInt(100_000);
    }

    public enum ReportProcess {
        IN_PROGRESS, IN_WAITING, ACCEPTED
    }

}
