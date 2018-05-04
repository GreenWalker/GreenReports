package br.com.empirelands.menu;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class MenuHelper {

    @Setter @Getter
    private MenuType atualMenu;
    @Getter @Setter
    private int atualPage;
    @Getter @Setter
    private List<ItemStack> atualItemStackView;

    public MenuHelper() {
        this.atualPage = 1;
        this.atualItemStackView = null;
        this.atualMenu = null;
    }
}
