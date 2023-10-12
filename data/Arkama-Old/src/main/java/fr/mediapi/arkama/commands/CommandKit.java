package fr.mediapi.arkama.commands;

import fr.mediapi.arkama.Arkama;
import fr.mediapi.arkama.objects.Kit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class CommandKit implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String arg, String[] args) {
        Player p = (Player) sender;
        if (args[0].equalsIgnoreCase("create") && !args[1].equalsIgnoreCase("create")) {
            if (args.length == 2) {
                HashMap<Integer, ItemStack> slot = new HashMap<>();
                int i = 0;
                for (ItemStack is : p.getInventory()) {
                    if (is == null) {
                        continue;
                    }
                    slot.put(i, is);
                    i++;
                }

                Arkama.skit.put(args[1], new Kit(slot));
                p.sendMessage("kit " + args[1] + " ajouté avec succès");
                Arkama.saveKit();
            } else {
                p.sendMessage("correct usage : /kit create <NOM>");
            }
        } else if (Arkama.skit != null && Arkama.skit.containsKey(args[1])) {
            HashMap<Integer, ItemStack> kit = Arkama.skit.get(args[1]).i;
            for (int n = 0; n < p.getInventory().getSize(); n++) {
                if (!kit.containsKey(n)) {
                    continue;
                }
                p.getInventory().setItem(n, kit.get(n));
            }
            p.sendMessage("kit " + args[1] + " récupéré");
        }
        return false;
    }
}