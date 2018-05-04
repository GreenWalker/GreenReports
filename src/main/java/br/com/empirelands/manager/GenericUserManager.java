package br.com.empirelands.manager;

import br.com.empirelands.ConsoleLogguerManager;
import br.com.empirelands.player.normal_user.GenericUser;

import java.util.List;
import java.util.UUID;

public class GenericUserManager {

    private List<GenericUser> genericUsers;

    public GenericUserManager(List<GenericUser> users) {
        this.genericUsers = users;
    }

    public GenericUser getPlayer(String nick){
        if(genericUsers != null){
            for(GenericUser gr : this.genericUsers){
                if(gr.getNick().equalsIgnoreCase(nick)){
                    return gr;
                }
            }
        }else{
            ConsoleLogguerManager.getInstance().logSevere("A lista de players está nula! reporte o desenvolvedor.");
        }
        return null;
    }

    public void addPlayer(GenericUser player){
        if(!existPlayer(player.getUuid())){
            this.genericUsers.add(player);
        }
    }

    public GenericUser getPlayer(UUID id){
        if(genericUsers != null) {
            for (GenericUser gr : this.genericUsers) {
                if (gr.getUuid().equals(id)) {
                    return gr;
                }
            }
        }
        return null;
    }

    public List<GenericUser> getAllPlayers(){
        return this.genericUsers;
    }

    public boolean existPlayer(String nick){
        return this.getPlayer(nick) != null;
    }

    public boolean existPlayer(UUID id){
        return this.getPlayer(id) != null;
    }

}
