package br.com.empirelands.util.database_util_methods.staff_mg;

import br.com.empirelands.exception.InvalidInput;
import br.com.empirelands.mysql.DaoManager;
import br.com.empirelands.player.normal_user.GenericUser;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Criado por Floydz.
 */
public class StaffDatabaseImpl implements StaffDatabaseMG{

    @Getter
    private DaoManager daoManager;

    public StaffDatabaseImpl(DaoManager daoManager) {
        this.daoManager = daoManager;
    }

    @Override
    public GenericUser getStaff(int id) {
        GenericUser staff = null;
        for (GenericUser genericUser : getDaoManager().getPlayerAdmDao().getAllStaffs()) {
            if(genericUser.getId() == id){
                staff = genericUser;
            }
        }
        return staff;
    }

    @Override
    public void addStaff(GenericUser user) {
        List<Object> obj = new ArrayList<>();
        obj.add(user.getId());
        obj.add(0);
        try {
            getDaoManager().getPlayerAdmDao().add(obj);
        } catch (InvalidInput invalidInput) {
            invalidInput.printStackTrace();
        }
    }

    public void updateCompletedReports(int id, int value) {
        daoManager.getPlayerAdmDao().update("completed_reports", id, value);
    }

    @Override
    public void removeStaff(GenericUser user) {
        getDaoManager().getPlayerAdmDao().delete(user.getId());
    }

    @Override
    public List<GenericUser> getAllStaffs() {
        return getDaoManager().getPlayerAdmDao().getAllStaffs();
    }

    @Override
    public void removeAllStaffs() {
        for(GenericUser user : getAllStaffs()){
            getDaoManager().getPlayerAdmDao().delete(user.getId());
        }
    }
}
