package br.com.empirelands.config;

import br.com.empirelands.ConsoleLogguerManager;
import br.com.empirelands.util.Util;
import com.google.common.base.Charsets;
import org.apache.commons.lang.Validate;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

public class ConfigHandler extends YamlConfiguration {

    private File file = null;

    public ConfigHandler(Plugin pl, String name) throws IOException, InvalidConfigurationException {
        file = new File(pl.getDataFolder(), name);
        if (!this.file.exists()) {
            if (pl.getResource(name) != null) {
                pl.saveResource(name, false);
                pl.getServer().getConsoleSender().sendMessage("[" + pl.getName() + "] arquivo " + name + " foi criado com exito no diretorio " + pl.getDataFolder().getAbsolutePath() + " !");
            }
        } else {
            this.file.mkdirs();
            this.file.createNewFile();
        }
        this.load(this.file);
    }

    public void save() throws IOException {
        save(this.file);
    }

    public void reloaded() {
        try {
            load(this.file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void load(File file) throws IOException, InvalidConfigurationException {
        Validate.notNull(file, "file nao pode ser null");
        this.load(new InputStreamReader(new FileInputStream(file), Charsets.UTF_8));
    }

    public boolean trySave() {
        try {
            save();
            return true;
        } catch (IOException ex) {
            ConsoleLogguerManager.getInstance().logSevere("Nao foi possivel salvar " + this.file.getName() + " no disco!" + ex.getMessage() + " " + ex.getCause().getMessage());
        }
        return false;
    }

    public List<String> getStringList(String path, Map<? extends String, Object> map){
        List<String> listOfString = super.getStringList(path);
        if(!(listOfString != null && !listOfString.isEmpty())){ return null; }
        for(int i = 0; i < listOfString.size(); ++i){
            listOfString.set(i, listOfString.get(i).replaceAll("&", "§"));
        }
        if(map != null && map.size() > 0){
            for(String ks : map.keySet()){
                for(int i = 0; i < listOfString.size(); ++i){
                    if(listOfString.get(i).contains(ks)){
                        listOfString.set(i, listOfString.get(i).replaceAll(ks, String.valueOf(map.get(ks))));
                    }
                }
            }
        }
        return listOfString;
    }

    public String getStringReplaced(String path) {
        return this.getString(path).replaceAll("&", "§");
    }

    public String getStringReplacedWithPrefix(String path){
        return this.getString(path).replaceAll("&", "§").replace("%prefix%", Util.getInstancie().getPrefix());
    }

    public String getStringReplaced(String path, Map<? extends String, Object> map) {
       String toReplace = this.getStringReplaced(path);
       if(!(map != null && map.size() > 0)){return "";}
       for(String s : map.keySet()){
           if(toReplace.contains(s)){
              toReplace = toReplace.replaceAll(s, String.valueOf(map.get(s)));
           }
       }
       return toReplace;
    }

}
