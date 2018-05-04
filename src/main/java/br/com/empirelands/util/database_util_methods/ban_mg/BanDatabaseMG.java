package br.com.empirelands.util.database_util_methods.ban_mg;

import br.com.empirelands.player.normal_user.GenericUser;
import br.com.empirelands.util.objects.UserBanned;

import java.util.List;

/**
 * Criado por Floydz.
 */
public interface BanDatabaseMG {

    boolean add(GenericUser user);

    boolean isBanned(String nick);

    boolean isBanned(int id);

    boolean isBanned(GenericUser user);

    List<UserBanned> getAllUsersBanneds();
}
