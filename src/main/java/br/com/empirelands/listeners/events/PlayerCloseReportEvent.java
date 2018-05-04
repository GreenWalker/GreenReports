package br.com.empirelands.listeners.events;

import br.com.empirelands.player.normal_user.GenericUser;
import br.com.empirelands.report.Report;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerCloseReportEvent extends Event {

    @Getter
    private GenericUser closer;
    @Getter
    private Report report;

    private static final HandlerList handlerList = new HandlerList();

    public PlayerCloseReportEvent(GenericUser closer, Report reportToClose) {
        this.closer = closer;
        this.report = reportToClose;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList(){
        return handlerList;
    }
}
