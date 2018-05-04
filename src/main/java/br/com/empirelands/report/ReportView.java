package br.com.empirelands.report;

import br.com.empirelands.player.normal_user.GenericUser;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

/**
 * Criado por Floydz.
 */
public class ReportView {
    // bean
    @Setter @Getter
    private GenericUser viewer;
    @Setter @Getter
    private Report view;
    @Setter @Getter
    private Timestamp viewDate;

    public ReportView(GenericUser viewer, Report view, Timestamp viewDate) {
        this.viewer = viewer;
        this.view = view;
        this.viewDate = viewDate;
    }
}
