package br.com.empirelands.mysql.interfaces;

import br.com.empirelands.player.normal_user.GenericUser;

import java.util.List;

public interface PlayerDao extends DaoUtils{

    GenericUser getUser(String nick);

    GenericUser getUser(int id);

    List<GenericUser> getAllUsers();
}
