package br.com.empirelands.util.database_util_methods.ban_mg;

import br.com.empirelands.exception.InvalidInput;
import br.com.empirelands.mysql.DaoManager;
import br.com.empirelands.player.normal_user.GenericUser;
import br.com.empirelands.util.objects.UserBanned;

import java.util.ArrayList;
import java.util.List;

/**
 * Criado por Floydz.
 */
public class BanDatabaseImpl implements BanDatabaseMG {

    private DaoManager daoManager;

    public BanDatabaseImpl(DaoManager daoManager) {
        this.daoManager = daoManager;
    }

    @Override
    public boolean add(GenericUser user) {
        List<Object> obj = new ArrayList<>();
        obj.add(0, user.getId()); // index 0 - id_banned
        try {
            daoManager.getBanDao().add(obj);
            return true;
        } catch (InvalidInput invalidInput) {
            invalidInput.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean isBanned(String nick) {
        return daoManager.getBanDao().isBanned(nick);
    }

    @Override
    public boolean isBanned(int id) {
        return daoManager.getBanDao().isBanned(id);
    }

    @Override
    public boolean isBanned(GenericUser user) {
        return daoManager.getBanDao().isBanned(user);
    }

    @Override
    public List<UserBanned> getAllUsersBanneds() {
        return daoManager.getBanDao().getAllBanneds();
    }
}
