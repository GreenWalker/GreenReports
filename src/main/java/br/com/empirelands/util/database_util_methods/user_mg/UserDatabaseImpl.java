package br.com.empirelands.util.database_util_methods.user_mg;


import br.com.empirelands.exception.InvalidInput;
import br.com.empirelands.mysql.DaoManager;
import br.com.empirelands.player.normal_user.GenericUser;
import lombok.Getter;


import java.util.ArrayList;
import java.util.List;

/**
 * Criado por Floydz.
 */
public class UserDatabaseImpl implements UserDatabaseMG {

    @Getter
    private DaoManager daoManager;

    public UserDatabaseImpl(DaoManager daoManager) {
        this.daoManager = daoManager;
    }

    @Override
    public GenericUser getUser(String nick) {
        return getDaoManager().getPlayerDao().getUser(nick);
    }

    @Override
    public GenericUser getUserById(int id) {
        return getDaoManager().getPlayerDao().getUser(id);
    }

    @Override
    public int getUserId(String nick) {
        return getDaoManager().getPlayerDao().getUser(nick).getId();
    }

    @Override
    public boolean addUser(GenericUser user) {
        List<Object> obj = new ArrayList<>();
        obj.add(user.getUuid().toString());
        obj.add(user.getNick());
        obj.add(user.getReportedAllTimes());
        obj.add(user.getReports_sends());
        try {
            getDaoManager().getPlayerDao().add(obj);
            return true;
        } catch (InvalidInput invalidInput) {
            invalidInput.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean removeUser(GenericUser user) {
        return getDaoManager().getPlayerDao().delete(user.getId());
    }

    @Override
    public boolean updateReportedTimes(GenericUser user, int newValue) {
        return updateUser("reported_times", user.getId(), newValue);
    }

    @Override
    public boolean updateReportSents(GenericUser user, int newValue) {
        return updateUser("reports_sends", user.getId(), newValue);
    }

    private boolean updateUser(String column, Object id, Object value) {
        return getDaoManager().getPlayerDao().update(column, id, value);
    }

    @Override
    public List<GenericUser> getAllUsers() {
        return getDaoManager().getPlayerDao().getAllUsers();
    }

    @Override
    public void removeAllUser() {
        for(GenericUser user : getAllUsers()){
            getDaoManager().getPlayerDao().delete(user.getId());
        }
    }
}
