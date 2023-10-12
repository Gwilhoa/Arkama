package fr.mediapi.arkama.objects;

import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class Kit {
    public HashMap<Integer, ItemStack> i;

    public Kit(HashMap<Integer, ItemStack> i) {
        this.i = i;
    }

    @Override
    public String toString() {
        return "Kit{" +
                "i=" + i +
                '}';
    }
}
