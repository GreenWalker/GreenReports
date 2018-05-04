package br.com.empirelands.listeners.events;

import br.com.empirelands.player.normal_user.GenericUser;
import br.com.empirelands.report.Report;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


public class PlayerViewReportEvent extends Event {

    @Getter
    private GenericUser viewer;
    @Getter
    private Report report;

    private static final HandlerList handlerList = new HandlerList();

    public PlayerViewReportEvent(GenericUser viewer, Report reportView) {
        this.viewer = viewer;
        this.report = reportView;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList(){
        return handlerList;
    }
}
