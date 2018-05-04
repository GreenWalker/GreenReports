package br.com.empirelands.mysql.interfaces;

import br.com.empirelands.player.normal_user.GenericUser;
import br.com.empirelands.util.objects.UserBanned;

import java.util.List;

/**
 * Criado por Floydz.
 */
public interface BanDao extends DaoUtils {

    boolean isBanned(GenericUser user);

    boolean isBanned(String nick);

    boolean isBanned(int id);

    List<UserBanned> getAllBanneds();

}
