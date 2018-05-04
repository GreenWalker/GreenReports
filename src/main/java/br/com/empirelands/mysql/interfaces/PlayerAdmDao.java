package br.com.empirelands.mysql.interfaces;

import br.com.empirelands.player.normal_user.GenericUser;

import java.util.List;

public interface PlayerAdmDao extends DaoUtils {

    GenericUser getStaff(String nick);

    List<GenericUser> getAllStaffs();

}
