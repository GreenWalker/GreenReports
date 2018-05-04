package br.com.empirelands.listeners.events;

import br.com.empirelands.player.normal_user.GenericUser;
import br.com.empirelands.player.report_user.ReportPlayer;
import br.com.empirelands.report.Report;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerCreateReportEvent extends Event {

    @Getter
    private GenericUser reporter;
    @Getter
    private ReportPlayer reported;
    @Getter
    private String motive;
    @Getter
    private Report report;

    private static final HandlerList handlerList = new HandlerList();

    public PlayerCreateReportEvent(GenericUser reporter, ReportPlayer reported, String motive, Report r) {
        this.reporter = reporter;
        this.reported = reported;
        this.motive = motive;
        this.report = r;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList(){
        return handlerList;
    }
}
