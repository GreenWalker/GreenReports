package br.com.empirelands.menu;

import br.com.empirelands.ConsoleLogguerManager;
import br.com.empirelands.util.Util;
import org.bukkit.Bukkit;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class MenuMG {

    private Inventory inventory;
    private String title;
    private InventoryHolder owner;

    public MenuMG(int size, String title) {
        this.title = title;
        try {
            inventory = Bukkit.createInventory(null, size, Util.getInstancie().colorize(title));
        } catch (Throwable er) {
            ConsoleLogguerManager.getInstance().logSevere("Erro ao criar o inventário [" + title + "]");
            er.printStackTrace();
        }
    }

    public MenuMG(int size, String title, InventoryHolder owner) {
        this.title = title;
        this.owner = owner;
        try {
            inventory = Bukkit.createInventory(owner, size, Util.getInstancie().colorize(title));
        } catch (Throwable er) {
            ConsoleLogguerManager.getInstance().logSevere("Erro ao criar o inventário [" + title + "]");
            er.printStackTrace();
        }
    }

    public String getTitle() {
        return this.title;
    }

    public Inventory getInventory() {
        return this.inventory;
    }

}
