package br.com.empirelands.util.database_util_methods.staff_mg;

import br.com.empirelands.player.normal_user.GenericUser;

import java.util.List;

public interface StaffDatabaseMG {

    GenericUser getStaff(int id);

    void addStaff(GenericUser user);

    void removeStaff(GenericUser user);

    void updateCompletedReports(int id, int value);

    List<GenericUser> getAllStaffs();

    void removeAllStaffs();

}
