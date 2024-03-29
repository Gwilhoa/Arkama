package fr.guigui205.arkama;

import com.google.gson.reflect.TypeToken;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


public class ArkaEnchant extends JavaPlugin {

    @Override
    public void onEnable() {
        getCommand("enchant").setExecutor(this);
        getCommand("rename").setExecutor(this);

        getLogger().warning("[ArkaEnchant] chargé");
    }

    public boolean onCommand(CommandSender sender, Command cmd, String arg, String[] args) {
        Player p = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("rename"))
        {
            ItemStack iS = p.getInventory().getItemInMainHand();
            ItemMeta iM = iS.getItemMeta();
            iM.displayName(Component.text(Arrays.toString(args).replace('&', '§')));
        }
        if (cmd.getName().equalsIgnoreCase("enchant")) {
            if (args.length == 0) {
                p.sendMessage("§e§lArka§c§lE§1§lnchant §6§l>> §cmauvais usage : §a/enchant (enchantement) (level)");
                return false;
            }
            ItemStack it = p.getInventory().getItemInMainHand();
            if (it.getType() != Material.AIR) {
                try {
                    int lev = Integer.parseInt(args[1]);
                    Enchantment e;
                    try {
                        String[] sp = args[0].split(":");
                        if (sp.length == 1) {
                            e = Enchantment.getByKey(NamespacedKey.minecraft(sp[0]));
                        } else {
                            e = Enchantment.getByKey(NamespacedKey.minecraft(args[1]));
                        }
                    } catch (Exception er) {
                        er.printStackTrace();
                        e = null;
                    }
                    if (e == null) {
                        p.sendMessage("ERREUR ! nom invalide");
                        return true;
                    }
                    it.addUnsafeEnchantment(e, lev);
                    p.getInventory().setItemInMainHand(it);
                    p.sendMessage("§e§lArka§c§lE§1§lnchant §6§l>> §avous avez enchanté " + it.getAmount() + " " + it.getType().name() + " avec " + args[0] + " " + lev);
                } catch (NumberFormatException e) {
                    p.sendMessage("§e§lArka§c§lE§1§lnchant §6§l>> §cles niveax ne sont pas des lettres");
                    return true;
                }
            } else {
                p.sendMessage("§e§lArka§c§lE§1§lnchant §6§l>> §c tu peux pas enchant ta propre main");
            }
        }
        return false;
    }
}