package br.com.empirelands.util.objects;

import br.com.empirelands.player.normal_user.GenericUser;

import java.sql.Timestamp;

/**
 * Criado por Floydz.
 */
public class UserBanned {

    private GenericUser userbanned;
    private Timestamp bandate;
    private int banid;

    public UserBanned(int banid, GenericUser userbanned, Timestamp bandate) {
        this.userbanned = userbanned;
        this.bandate = bandate;
        this.banid = banid;
    }

    public GenericUser getUserbanned() {
        return userbanned;
    }

    public void setUserbanned(GenericUser userbanned) {
        this.userbanned = userbanned;
    }

    public Timestamp getBandate() {
        return bandate;
    }

    public void setBandate(Timestamp bandate) {
        this.bandate = bandate;
    }

    public int getBanid() {
        return banid;
    }

    public void setBanid(int banid) {
        this.banid = banid;
    }
}
