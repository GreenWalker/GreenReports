package br.com.empirelands.util.database_util_methods.user_mg;

import br.com.empirelands.player.normal_user.GenericUser;

import java.util.List;

public interface UserDatabaseMG {

    GenericUser getUser(String nick);

    GenericUser getUserById(int id);

    int getUserId(String nick);

    boolean addUser(GenericUser user);

    boolean removeUser(GenericUser user);

    boolean updateReportedTimes(GenericUser user, int newValue);

    boolean updateReportSents(GenericUser user, int newValue);

    List<GenericUser> getAllUsers();

    void removeAllUser();

}
