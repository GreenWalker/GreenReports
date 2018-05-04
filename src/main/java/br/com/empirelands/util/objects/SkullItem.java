package br.com.empirelands.util.objects;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class SkullItem extends Item{

    public SkullItem(){
        super(new ItemStack(Material.SKULL_ITEM));
    }

    public Item skull(String owner){
        setDurability((short) 3);
        SkullMeta skull = (SkullMeta) build().getItemMeta();
        skull.setOwner(owner);
        build().setItemMeta(skull);
        return getItem();
    }
}
